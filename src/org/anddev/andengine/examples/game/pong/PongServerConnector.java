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
import org.anddev.andengine.extension.multiplayer.protocol.client.IServerMessageHandler.DefaultServerMessageHandler;
import org.anddev.andengine.extension.multiplayer.protocol.client.ServerConnector;
import org.anddev.andengine.extension.multiplayer.protocol.shared.SocketConnection;
import org.anddev.andengine.util.Debug;

/**
 * @author Nicolas Gramlich
 * @since 19:59:01 - 28.02.2011
 */
public class PongServerConnector extends ServerConnector<SocketConnection> implements PongConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public PongServerConnector(final String pServerIP, final IServerConnectorListener<SocketConnection> pServerConnectorListener, final IPongServerConnectorListener pPongServerConnectorListener) throws IOException {
		super(new SocketConnection(new Socket(pServerIP, SERVER_PORT)),
			new DefaultServerMessageHandler<SocketConnection>() {
				@Override
				protected void onHandleConnectionAcceptedServerMessage(final ServerConnector<SocketConnection> pServerConnector, final ConnectionAcceptedServerMessage pServerMessage) {
					Debug.d("CLIENT: Connection accepted.");
				}
	
				@Override
				protected void onHandleConnectionRefusedServerMessage(final ServerConnector<SocketConnection> pServerConnector, final ConnectionRefusedServerMessage pServerMessage) {
					Debug.d("CLIENT: Connection refused.");
				}
	
				@Override
				protected void onHandleConnectionPongServerMessage(final ServerConnector<SocketConnection> pServerConnector, final ConnectionPongServerMessage pConnectionPongServerMessage) {
					Debug.v("Ping: " + (System.currentTimeMillis() - pConnectionPongServerMessage.getOriginalPingTimestamp()) / 2 + "ms");
				}
	
				@Override
				public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					switch(pServerMessage.getFlag()) {
						case FLAG_MESSAGE_SERVER_SET_PADDLEID:
							final SetPaddleIDServerMessage setPaddleIDServerMessage = (SetPaddleIDServerMessage) pServerMessage;
							pPongServerConnectorListener.setPaddleID(setPaddleIDServerMessage.mPaddleID);
							break;
						case FLAG_MESSAGE_SERVER_UPDATE_SCORE:
							final UpdateScoreServerMessage updateScoreServerMessage = (UpdateScoreServerMessage) pServerMessage;
							pPongServerConnectorListener.updateScore(updateScoreServerMessage.mPaddleID, updateScoreServerMessage.mScore);
							break;
						case FLAG_MESSAGE_SERVER_UPDATE_BALL:
							final UpdateBallServerMessage updateBallServerMessage = (UpdateBallServerMessage) pServerMessage;
							pPongServerConnectorListener.updateBall(updateBallServerMessage.mX, updateBallServerMessage.mY);
							break;
						case FLAG_MESSAGE_SERVER_UPDATE_PADDLE:
							final UpdatePaddleServerMessage updatePaddleServerMessage = (UpdatePaddleServerMessage) pServerMessage;
							pPongServerConnectorListener.updatePaddle(updatePaddleServerMessage.mPaddleID, updatePaddleServerMessage.mX, updatePaddleServerMessage.mY);
							break;
						default:
							super.onHandleMessage(pServerConnector, pServerMessage);
							Debug.d("CLIENT: ServerMessage received: " + pServerMessage.toString());
					}
				}
			}, pServerConnectorListener);
		
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

	public static interface IPongServerConnectorListener {
		public void setPaddleID(final int pPaddleID);
		public void updateScore(final int pPaddleID, final int pScore);
		public void updateBall(final float pX, final float pY);
		public void updatePaddle(final int pPaddleID, final float pX, final float pY);
	}
}
