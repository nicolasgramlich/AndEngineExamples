package org.anddev.andengine.examples.game.pong;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.examples.game.pong.adt.MovePaddleClientMessage;
import org.anddev.andengine.examples.game.pong.adt.SetPaddleIDServerMessage;
import org.anddev.andengine.examples.game.pong.adt.UpdateBallServerMessage;
import org.anddev.andengine.examples.game.pong.adt.UpdatePaddleServerMessage;
import org.anddev.andengine.examples.game.pong.util.constants.PongConstants;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.BaseClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.server.BaseClientConnectionListener;
import org.anddev.andengine.extension.multiplayer.protocol.server.BaseClientMessageSwitch;
import org.anddev.andengine.extension.multiplayer.protocol.server.BaseServer;
import org.anddev.andengine.extension.multiplayer.protocol.server.BaseServer.IServerStateListener.DefaultServerStateListener;
import org.anddev.andengine.extension.multiplayer.protocol.server.ClientConnection;
import org.anddev.andengine.extension.multiplayer.protocol.server.ClientMessageExtractor;
import org.anddev.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.anddev.andengine.util.Debug;

import android.util.SparseArray;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

/**
 * @author Nicolas Gramlich
 * @since 20:00:09 - 28.02.2011
 */
public class PongServer extends BaseServer<ClientConnection> implements IUpdateHandler, PongConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final PhysicsWorld mPhysicsWorld;
	private final Body mBallBody;
	private final SparseArray<Body> mPaddleBodies = new SparseArray<Body>();

	// ===========================================================
	// Constructors
	// ===========================================================

	public PongServer(final BaseClientConnectionListener pClientConnectionListener) {
		super(SERVER_PORT, pClientConnectionListener, new DefaultServerStateListener());

		this.mPhysicsWorld = new FixedStepPhysicsWorld(60, 2, new Vector2(0.1f, 0.1f), false, 8, 8);

		this.mBallBody = PhysicsFactory.createCircleBody(this.mPhysicsWorld, new Rectangle(-BALL_WIDTH_HALF, -BALL_HEIGHT_HALF, BALL_WIDTH, BALL_HEIGHT), BodyType.DynamicBody, PhysicsFactory.createFixtureDef(1, 1, 0));
		// TODO Sth different from 0 and 1 . Or constants.
		this.mPaddleBodies.put(0, PhysicsFactory.createBoxBody(this.mPhysicsWorld, new Rectangle(- PADDLE_WIDTH_HALF, - GAME_HEIGHT_HALF, PADDLE_WIDTH, PADDLE_HEIGHT), BodyType.StaticBody, PhysicsFactory.createFixtureDef(1, 1, 0)));
		this.mPaddleBodies.put(1, PhysicsFactory.createBoxBody(this.mPhysicsWorld, new Rectangle(- PADDLE_WIDTH_HALF, GAME_HEIGHT_HALF - PADDLE_HEIGHT, PADDLE_WIDTH, PADDLE_HEIGHT), BodyType.StaticBody, PhysicsFactory.createFixtureDef(1, 1, 0)));
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		this.mPhysicsWorld.onUpdate(pSecondsElapsed);
		
		final ArrayList<ClientConnection> clientConnections = this.mClientConnections;
		for(int i = 0; i < clientConnections.size(); i++) {
			try {
				/* Update Ball. */
				final ClientConnection clientConnection = clientConnections.get(i);
				final Vector2 ballPosition = this.mBallBody.getPosition();
				final float ballX = ballPosition.x * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT - BALL_WIDTH_HALF;
				final float ballY = ballPosition.y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT - BALL_HEIGHT_HALF;

				clientConnection.sendServerMessage(new UpdateBallServerMessage(ballX, ballY));

				/* Update Paddles. */
				final SparseArray<Body> paddleBodies = this.mPaddleBodies;
				for(int j = 0; j < paddleBodies.size(); j++) {
					final int paddleID = paddleBodies.keyAt(j);
					final Body paddleBody = paddleBodies.get(paddleID);
					final Vector2 paddlePosition = paddleBody.getPosition();

					final float paddleX = paddlePosition.x * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT - PADDLE_WIDTH_HALF;
					final float paddleY = paddlePosition.y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT - PADDLE_HEIGHT_HALF;
					clientConnection.sendServerMessage(new UpdatePaddleServerMessage(paddleID, paddleX, paddleY));
				}
			} catch (IOException e) {
				Debug.e(e);
			}
		}
	}

	@Override
	public void reset() {
		/* Nothing. */
	}

	@Override
	protected ClientConnection newClientConnection(final Socket pClientSocket, final BaseClientConnectionListener pClientConnectionListener) throws Exception {
		final ClientConnection clientConnection = new ClientConnection(pClientSocket, pClientConnectionListener,
			new ClientMessageExtractor(){
				@Override
				public BaseClientMessage readMessage(final short pFlag, final DataInputStream pDataInputStream) throws IOException {
					switch(pFlag) {
						case FLAG_MESSAGE_CLIENT_MOVE_PADDLE:
							return new MovePaddleClientMessage(pDataInputStream);
						default:
							return super.readMessage(pFlag, pDataInputStream);
					}
				}
			},
			new BaseClientMessageSwitch() {
				@Override
				public void switchMessage(final ClientConnection pClientConnection, final BaseClientMessage pClientMessage) throws IOException {
					switch(pClientMessage.getFlag()) {
						case FLAG_MESSAGE_CLIENT_MOVE_PADDLE:
							final MovePaddleClientMessage movePaddleClientMessage = (MovePaddleClientMessage)pClientMessage;
							final Body paddleBody = PongServer.this.mPaddleBodies.get(movePaddleClientMessage.mPaddleID);
							final Vector2 paddlePosition = paddleBody.getTransform().getPosition();
							paddlePosition.set(movePaddleClientMessage.mX / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, paddlePosition.y);
							paddleBody.setTransform(paddlePosition, 0);
					}
					super.switchMessage(pClientConnection, pClientMessage);
					Debug.d("SERVER: ClientMessage received: " + pClientMessage.toString());
				}
			}
		);
		clientConnection.sendServerMessage(new SetPaddleIDServerMessage(this.mClientConnections.size())); // TODO should not be size();
		return clientConnection;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
