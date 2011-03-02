package org.anddev.andengine.examples.game.pong;

import java.io.IOException;
import java.net.Socket;

import org.anddev.andengine.examples.game.pong.adt.SetPaddleIDServerMessage;
import org.anddev.andengine.examples.game.pong.adt.UpdateBallServerMessage;
import org.anddev.andengine.examples.game.pong.adt.UpdatePaddleServerMessage;
import org.anddev.andengine.examples.game.pong.adt.UpdateScoreServerMessage;
import org.anddev.andengine.examples.game.pong.util.constants.PongConstants;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.IServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionAcceptedServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionPongServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionRefusedServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.client.IServerConnectionListener;
import org.anddev.andengine.extension.multiplayer.protocol.client.IServerMessageHandler.DefaultServerMessageHandler;
import org.anddev.andengine.extension.multiplayer.protocol.client.ServerConnection;
import org.anddev.andengine.util.Debug;

/**
 * @author Nicolas Gramlich
 * @since 19:59:01 - 28.02.2011
 */
public class PongServerConnection extends ServerConnection implements PongConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public PongServerConnection(final String pServerIP, final IServerConnectionListener pServerConnectionListener, final IPongServerConnectionListener pPongServerConnectionListener) throws IOException {
		super(new Socket(pServerIP, SERVER_PORT), pServerConnectionListener,
			new DefaultServerMessageHandler() {
				@Override
				protected void onHandleConnectionAcceptedServerMessage(final ServerConnection pServerConnection, final ConnectionAcceptedServerMessage pServerMessage) {
					Debug.d("CLIENT: Connection accepted.");
				}
	
				@Override
				protected void onHandleConnectionRefusedServerMessage(final ServerConnection pServerConnection, final ConnectionRefusedServerMessage pServerMessage) {
					Debug.d("CLIENT: Connection refused.");
				}
				
				@Override
				protected void onHandleConnectionPongServerMessage(final ServerConnection pServerConnection, final ConnectionPongServerMessage pConnectionPongServerMessage) {
					Debug.v("Ping: " + (System.currentTimeMillis() - pConnectionPongServerMessage.getOriginalPingTimestamp()) / 2 + "ms");
				}
	
				@Override
				public void onHandleMessage(final ServerConnection pServerConnection, final IServerMessage pServerMessage) throws IOException {
					switch(pServerMessage.getFlag()) {
						case FLAG_MESSAGE_SERVER_SET_PADDLEID:
							final SetPaddleIDServerMessage setPaddleIDServerMessage = (SetPaddleIDServerMessage)pServerMessage;
							pPongServerConnectionListener.setPaddleID(setPaddleIDServerMessage.mPaddleID);
							break;
						case FLAG_MESSAGE_SERVER_UPDATE_SCORE:
							final UpdateScoreServerMessage updateScoreServerMessage = (UpdateScoreServerMessage)pServerMessage;
							pPongServerConnectionListener.updateScore(updateScoreServerMessage.mPaddleID, updateScoreServerMessage.mScore);
							break;
						case FLAG_MESSAGE_SERVER_UPDATE_BALL:
							final UpdateBallServerMessage updateBallServerMessage = (UpdateBallServerMessage)pServerMessage;
							pPongServerConnectionListener.updateBall(updateBallServerMessage.mX, updateBallServerMessage.mY);
							break;
						case FLAG_MESSAGE_SERVER_UPDATE_PADDLE:
							final UpdatePaddleServerMessage updatePaddleServerMessage = (UpdatePaddleServerMessage)pServerMessage;
							pPongServerConnectionListener.updatePaddle(updatePaddleServerMessage.mPaddleID, updatePaddleServerMessage.mX, updatePaddleServerMessage.mY);
							break;
						default:
							super.onHandleMessage(pServerConnection, pServerMessage);
							Debug.d("CLIENT: ServerMessage received: " + pServerMessage.toString());
					}
				}
			}
		);
		
		this.registerServerMessage(FLAG_MESSAGE_SERVER_SET_PADDLEID, SetPaddleIDServerMessage.class);
		this.registerServerMessage(FLAG_MESSAGE_SERVER_UPDATE_SCORE, UpdateScoreServerMessage.class);
		this.registerServerMessage(FLAG_MESSAGE_SERVER_UPDATE_BALL, UpdateBallServerMessage.class);
		this.registerServerMessage(FLAG_MESSAGE_SERVER_UPDATE_PADDLE, UpdatePaddleServerMessage.class);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IPongServerConnectionListener {
		public void setPaddleID(final int pPaddleID);
		public void updateScore(final int pPaddleID, final int pScore);
		public void updateBall(final float pX, final float pY);
		public void updatePaddle(final int pPaddleID, final float pX, final float pY);
	}
}
