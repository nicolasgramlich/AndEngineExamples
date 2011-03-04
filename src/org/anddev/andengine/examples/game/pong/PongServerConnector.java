package org.anddev.andengine.examples.game.pong;

import java.io.IOException;
import java.net.Socket;

import org.anddev.andengine.examples.game.pong.adt.SetPaddleIDServerMessage;
import org.anddev.andengine.examples.game.pong.adt.UpdateBallServerMessage;
import org.anddev.andengine.examples.game.pong.adt.UpdatePaddleServerMessage;
import org.anddev.andengine.examples.game.pong.adt.UpdateScoreServerMessage;
import org.anddev.andengine.examples.game.pong.util.constants.PongConstants;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.IServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionPongServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.client.IServerMessageHandler;
import org.anddev.andengine.extension.multiplayer.protocol.client.connector.ServerConnector;
import org.anddev.andengine.extension.multiplayer.protocol.client.connector.SocketConnectionServerConnector.ISocketConnectionServerConnectorListener;
import org.anddev.andengine.extension.multiplayer.protocol.shared.SocketConnection;
import org.anddev.andengine.extension.multiplayer.protocol.util.constants.ServerMessageFlags;
import org.anddev.andengine.util.Debug;

/**
 * @author Nicolas Gramlich
 * @since 19:59:01 - 28.02.2011
 */
public class PongServerConnector extends ServerConnector<SocketConnection> implements PongConstants, ServerMessageFlags {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public PongServerConnector(final String pServerIP, final ISocketConnectionServerConnectorListener pSocketConnectionServerConnectorListener, final IPongServerConnectorListener pPongServerConnectorListener) throws IOException {
		super(new SocketConnection(new Socket(pServerIP, SERVER_PORT)), pSocketConnectionServerConnectorListener);

		this.registerServerMessageHandler(FLAG_MESSAGE_SERVER_CONNECTION_ACCEPTED, new IServerMessageHandler<SocketConnection>() {
			@Override
			public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
				Debug.d("CLIENT: Connection accepted.");
			}
		});

		this.registerServerMessageHandler(FLAG_MESSAGE_SERVER_CONNECTION_REFUSED, new IServerMessageHandler<SocketConnection>() {
			@Override
			public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
				Debug.d("CLIENT: Connection refused.");
			}
		});

		this.registerServerMessageHandler(FLAG_MESSAGE_SERVER_CONNECTION_PONG, new IServerMessageHandler<SocketConnection>() {
			@Override
			public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
				final ConnectionPongServerMessage connectionPongServerMessage = (ConnectionPongServerMessage) pServerMessage;
				Debug.v("Ping: " + (System.currentTimeMillis() - connectionPongServerMessage.getOriginalPingTimestamp()) / 2 + "ms");
			}
		});

		this.registerServerMessage(FLAG_MESSAGE_SERVER_SET_PADDLEID, SetPaddleIDServerMessage.class, new IServerMessageHandler<SocketConnection>() {
			@Override
			public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
				final SetPaddleIDServerMessage setPaddleIDServerMessage = (SetPaddleIDServerMessage) pServerMessage;
				pPongServerConnectorListener.setPaddleID(setPaddleIDServerMessage.mPaddleID);
			}
		});

		this.registerServerMessage(FLAG_MESSAGE_SERVER_UPDATE_SCORE, UpdateScoreServerMessage.class, new IServerMessageHandler<SocketConnection>() {
			@Override
			public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
				final UpdateScoreServerMessage updateScoreServerMessage = (UpdateScoreServerMessage) pServerMessage;
				pPongServerConnectorListener.updateScore(updateScoreServerMessage.mPaddleID, updateScoreServerMessage.mScore);
			}
		});

		this.registerServerMessage(FLAG_MESSAGE_SERVER_UPDATE_BALL, UpdateBallServerMessage.class, new IServerMessageHandler<SocketConnection>() {
			@Override
			public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
				final UpdateBallServerMessage updateBallServerMessage = (UpdateBallServerMessage) pServerMessage;
				pPongServerConnectorListener.updateBall(updateBallServerMessage.mX, updateBallServerMessage.mY);
			}
		});

		this.registerServerMessage(FLAG_MESSAGE_SERVER_UPDATE_PADDLE, UpdatePaddleServerMessage.class, new IServerMessageHandler<SocketConnection>() {
			@Override
			public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
				final UpdatePaddleServerMessage updatePaddleServerMessage = (UpdatePaddleServerMessage) pServerMessage;
				pPongServerConnectorListener.updatePaddle(updatePaddleServerMessage.mPaddleID, updatePaddleServerMessage.mX, updatePaddleServerMessage.mY);
			}
		});
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
