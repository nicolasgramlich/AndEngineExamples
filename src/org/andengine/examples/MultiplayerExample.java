package org.andengine.examples;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.examples.adt.messages.client.ClientMessageFlags;
import org.andengine.examples.adt.messages.server.ConnectionCloseServerMessage;
import org.andengine.examples.adt.messages.server.ServerMessageFlags;
import org.andengine.extension.multiplayer.adt.message.IMessage;
import org.andengine.extension.multiplayer.adt.message.server.IServerMessage;
import org.andengine.extension.multiplayer.adt.message.server.ServerMessage;
import org.andengine.extension.multiplayer.client.IServerMessageHandler;
import org.andengine.extension.multiplayer.client.connector.ServerConnector;
import org.andengine.extension.multiplayer.client.connector.SocketConnectionServerConnector;
import org.andengine.extension.multiplayer.client.connector.SocketConnectionServerConnector.ISocketConnectionServerConnectorListener;
import org.andengine.extension.multiplayer.server.SocketServer;
import org.andengine.extension.multiplayer.server.SocketServer.ISocketServerListener;
import org.andengine.extension.multiplayer.server.connector.ClientConnector;
import org.andengine.extension.multiplayer.server.connector.SocketConnectionClientConnector;
import org.andengine.extension.multiplayer.server.connector.SocketConnectionClientConnector.ISocketConnectionClientConnectorListener;
import org.andengine.extension.multiplayer.shared.SocketConnection;
import org.andengine.extension.multiplayer.util.MessagePool;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.bitmap.AssetBitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.WifiUtils;
import org.andengine.util.WifiUtils.WifiUtilsException;
import org.andengine.util.debug.Debug;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 17:10:24 - 19.06.2010
 */
public class MultiplayerExample extends SimpleBaseGameActivity implements ClientMessageFlags, ServerMessageFlags {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final String LOCALHOST_IP = "127.0.0.1";

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	private static final int SERVER_PORT = 4444;

	private static final short FLAG_MESSAGE_SERVER_ADD_SPRITE = 1;
	private static final short FLAG_MESSAGE_SERVER_MOVE_SPRITE = FLAG_MESSAGE_SERVER_ADD_SPRITE + 1;

	private static final int DIALOG_CHOOSE_SERVER_OR_CLIENT_ID = 0;
	private static final int DIALOG_ENTER_SERVER_IP_ID = DIALOG_CHOOSE_SERVER_OR_CLIENT_ID + 1;
	private static final int DIALOG_SHOW_SERVER_IP_ID = DIALOG_ENTER_SERVER_IP_ID + 1;

	// ===========================================================
	// Fields
	// ===========================================================

	private ITexture mFaceTexture;
	private ITextureRegion mFaceTextureRegion;

	private int mSpriteIDCounter;
	private final SparseArray<Sprite> mSprites = new SparseArray<Sprite>();

	private String mServerIP = LOCALHOST_IP;
	private SocketServer<SocketConnectionClientConnector> mSocketServer;
	private ServerConnector<SocketConnection> mServerConnector;

	private final MessagePool<IMessage> mMessagePool = new MessagePool<IMessage>();

	// ===========================================================
	// Constructors
	// ===========================================================

	public MultiplayerExample() {
		this.initMessagePool();
	}

	private void initMessagePool() {
		this.mMessagePool.registerMessage(FLAG_MESSAGE_SERVER_ADD_SPRITE, AddSpriteServerMessage.class);
		this.mMessagePool.registerMessage(FLAG_MESSAGE_SERVER_MOVE_SPRITE, MoveSpriteServerMessage.class);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public EngineOptions onCreateEngineOptions() {
		this.showDialog(DIALOG_CHOOSE_SERVER_OR_CLIENT_ID);

		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}

	@Override
	public void onCreateResources() throws IOException {
		this.mFaceTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/face_box.png", TextureOptions.BILINEAR);
		this.mFaceTextureRegion = TextureRegionFactory.extractFromTexture(this.mFaceTexture);
		this.mFaceTexture.load();
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.getBackground().setColor(0.09804f, 0.6274f, 0.8784f);

		/* We allow only the server to actively send around messages. */
		if(MultiplayerExample.this.mSocketServer != null) {
			scene.setOnSceneTouchListener(new IOnSceneTouchListener() {
				@Override
				public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
					if(pSceneTouchEvent.isActionDown()) {
						final AddSpriteServerMessage addSpriteServerMessage = (AddSpriteServerMessage) MultiplayerExample.this.mMessagePool.obtainMessage(FLAG_MESSAGE_SERVER_ADD_SPRITE);
            addSpriteServerMessage.set(MultiplayerExample.this.mSpriteIDCounter++, pSceneTouchEvent.getX(), pSceneTouchEvent.getY());

            MultiplayerExample.this.mSocketServer.sendBroadcastServerMessage(addSpriteServerMessage);

            MultiplayerExample.this.mMessagePool.recycleMessage(addSpriteServerMessage);
						return true;
					} else {
						return true;
					}
				}
			});

			scene.setOnAreaTouchListener(new IOnAreaTouchListener() {
				@Override
				public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final ITouchArea pTouchArea, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
					final Sprite sprite = (Sprite)pTouchArea;
          final int spriteID = (Integer)sprite.getUserData();

          final MoveSpriteServerMessage moveSpriteServerMessage = (MoveSpriteServerMessage) MultiplayerExample.this.mMessagePool.obtainMessage(FLAG_MESSAGE_SERVER_MOVE_SPRITE);
          moveSpriteServerMessage.set(spriteID, pSceneTouchEvent.getX(), pSceneTouchEvent.getY());

          MultiplayerExample.this.mSocketServer.sendBroadcastServerMessage(moveSpriteServerMessage);

          MultiplayerExample.this.mMessagePool.recycleMessage(moveSpriteServerMessage);
					return true;
				}
			});

			scene.setTouchAreaBindingOnActionDownEnabled(true);
		}

		return scene;
	}

	@Override
	protected Dialog onCreateDialog(final int pID) {
		switch(pID) {
			case DIALOG_SHOW_SERVER_IP_ID:
				try {
					return new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_info)
					.setTitle("Your Server-IP ...")
					.setCancelable(false)
					.setMessage("The IP of your Server is:\n" + WifiUtils.getWifiIPv4Address(this))
					.setPositiveButton(android.R.string.ok, null)
					.create();
				} catch (final WifiUtilsException e) {
					return new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("Your Server-IP ...")
					.setCancelable(false)
					.setMessage("Error retrieving IP of your Server: " + e)
					.setPositiveButton(android.R.string.ok, new OnClickListener() {
						@Override
						public void onClick(final DialogInterface pDialog, final int pWhich) {
							MultiplayerExample.this.finish();
						}
					})
					.create();
				}
			case DIALOG_ENTER_SERVER_IP_ID:
				final EditText ipEditText = new EditText(this);
				return new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle("Enter Server-IP ...")
				.setCancelable(false)
				.setView(ipEditText)
				.setPositiveButton("Connect", new OnClickListener() {
					@Override
					public void onClick(final DialogInterface pDialog, final int pWhich) {
						MultiplayerExample.this.mServerIP = ipEditText.getText().toString();
						MultiplayerExample.this.initClient();
					}
				})
				.setNegativeButton(android.R.string.cancel, new OnClickListener() {
					@Override
					public void onClick(final DialogInterface pDialog, final int pWhich) {
						MultiplayerExample.this.finish();
					}
				})
				.create();
			case DIALOG_CHOOSE_SERVER_OR_CLIENT_ID:
				return new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle("Be Server or Client ...")
				.setCancelable(false)
				.setPositiveButton("Client", new OnClickListener() {
					@Override
					public void onClick(final DialogInterface pDialog, final int pWhich) {
						MultiplayerExample.this.showDialog(DIALOG_ENTER_SERVER_IP_ID);
					}
				})
				.setNeutralButton("Server", new OnClickListener() {
					@Override
					public void onClick(final DialogInterface pDialog, final int pWhich) {
						MultiplayerExample.this.toast("You can add and move sprites, which are only shown on the clients.");
						MultiplayerExample.this.initServer();
						MultiplayerExample.this.showDialog(DIALOG_SHOW_SERVER_IP_ID);
					}
				})
				.setNegativeButton("Both", new OnClickListener() {
					@Override
					public void onClick(final DialogInterface pDialog, final int pWhich) {
						MultiplayerExample.this.toast("You can add sprites and move them, by dragging them.");
						MultiplayerExample.this.initServerAndClient();
						MultiplayerExample.this.showDialog(DIALOG_SHOW_SERVER_IP_ID);
					}
				})
				.create();
			default:
				return super.onCreateDialog(pID);
		}
	}

	@Override
	protected void onDestroy() {
		if(this.mSocketServer != null) {
			this.mSocketServer.sendBroadcastServerMessage(new ConnectionCloseServerMessage());
			this.mSocketServer.terminate();
		}

		if(this.mServerConnector != null) {
			this.mServerConnector.terminate();
		}

		super.onDestroy();
	}

	@Override
	public boolean onKeyUp(final int pKeyCode, final KeyEvent pEvent) {
		switch(pKeyCode) {
			case KeyEvent.KEYCODE_BACK:
				this.finish();
				return true;
		}
		return super.onKeyUp(pKeyCode, pEvent);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void addSprite(final int pID, final float pX, final float pY) {
		final Scene scene = this.mEngine.getScene();

		/* Create the sprite and add it to the scene. */
		final Sprite sprite = new Sprite(pX, pY, this.mFaceTextureRegion, this.getVertexBufferObjectManager());
		sprite.setUserData(pID);

		this.mSprites.put(pID, sprite);
		scene.registerTouchArea(sprite);
		scene.attachChild(sprite);
	}

	public void moveSprite(final int pID, final float pX, final float pY) {
		/* Find and move the sprite. */
		final Sprite sprite = this.mSprites.get(pID);
		sprite.setPosition(pX, pY);
	}

	private void initServerAndClient() {
		this.initServer();

		/* Wait some time after the server has been started, so it actually can start up. */
		try {
			Thread.sleep(500);
		} catch (final Throwable t) {
			Debug.e(t);
		}

		this.initClient();
	}

	private void initServer() {
		this.mSocketServer = new SocketServer<SocketConnectionClientConnector>(SERVER_PORT, new ExampleClientConnectorListener(), new ExampleServerStateListener()) {
			@Override
			protected SocketConnectionClientConnector newClientConnector(final SocketConnection pSocketConnection) throws IOException {
				return new SocketConnectionClientConnector(pSocketConnection);
			}
		};

		this.mSocketServer.start();
	}

	private void initClient() {
		try {
			this.mServerConnector = new SocketConnectionServerConnector(new SocketConnection(new Socket(this.mServerIP, SERVER_PORT)), new ExampleServerConnectorListener());

			this.mServerConnector.registerServerMessage(FLAG_MESSAGE_SERVER_CONNECTION_CLOSE, ConnectionCloseServerMessage.class, new IServerMessageHandler<SocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					MultiplayerExample.this.finish();
				}
			});

			this.mServerConnector.registerServerMessage(FLAG_MESSAGE_SERVER_ADD_SPRITE, AddSpriteServerMessage.class, new IServerMessageHandler<SocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					final AddSpriteServerMessage addSpriteServerMessage = (AddSpriteServerMessage)pServerMessage;
					MultiplayerExample.this.addSprite(addSpriteServerMessage.mID, addSpriteServerMessage.mX, addSpriteServerMessage.mY);
				}
			});

			this.mServerConnector.registerServerMessage(FLAG_MESSAGE_SERVER_MOVE_SPRITE, MoveSpriteServerMessage.class, new IServerMessageHandler<SocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					final MoveSpriteServerMessage moveSpriteServerMessage = (MoveSpriteServerMessage)pServerMessage;
					MultiplayerExample.this.moveSprite(moveSpriteServerMessage.mID, moveSpriteServerMessage.mX, moveSpriteServerMessage.mY);
				}
			});

			this.mServerConnector.getConnection().start();
		} catch (final Throwable t) {
			Debug.e(t);
		}
	}

	private void log(final String pMessage) {
		Debug.d(pMessage);
	}

	private void toast(final String pMessage) {
		this.log(pMessage);
		this.toastOnUiThread(pMessage);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class AddSpriteServerMessage extends ServerMessage {
		private int mID;
		private float mX;
		private float mY;

		public AddSpriteServerMessage() {

		}

		public AddSpriteServerMessage(final int pID, final float pX, final float pY) {
			this.mID = pID;
			this.mX = pX;
			this.mY = pY;
		}

		public void set(final int pID, final float pX, final float pY) {
			this.mID = pID;
			this.mX = pX;
			this.mY = pY;
		}

		@Override
		public short getFlag() {
			return FLAG_MESSAGE_SERVER_ADD_SPRITE;
		}

		@Override
		protected void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException {
			this.mID = pDataInputStream.readInt();
			this.mX = pDataInputStream.readFloat();
			this.mY = pDataInputStream.readFloat();
		}

		@Override
		protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
			pDataOutputStream.writeInt(this.mID);
			pDataOutputStream.writeFloat(this.mX);
			pDataOutputStream.writeFloat(this.mY);
		}
	}

	public static class MoveSpriteServerMessage extends ServerMessage {
		private int mID;
		private float mX;
		private float mY;

		public MoveSpriteServerMessage() {

		}

		public MoveSpriteServerMessage(final int pID, final float pX, final float pY) {
			this.mID = pID;
			this.mX = pX;
			this.mY = pY;
		}

		public void set(final int pID, final float pX, final float pY) {
			this.mID = pID;
			this.mX = pX;
			this.mY = pY;
		}

		@Override
		public short getFlag() {
			return FLAG_MESSAGE_SERVER_MOVE_SPRITE;
		}

		@Override
		protected void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException {
			this.mID = pDataInputStream.readInt();
			this.mX = pDataInputStream.readFloat();
			this.mY = pDataInputStream.readFloat();
		}

		@Override
		protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
			pDataOutputStream.writeInt(this.mID);
			pDataOutputStream.writeFloat(this.mX);
			pDataOutputStream.writeFloat(this.mY);
		}
	}

	private class ExampleServerConnectorListener implements ISocketConnectionServerConnectorListener {
		@Override
		public void onStarted(final ServerConnector<SocketConnection> pConnector) {
			MultiplayerExample.this.toast("CLIENT: Connected to server.");
		}

		@Override
		public void onTerminated(final ServerConnector<SocketConnection> pConnector) {
			MultiplayerExample.this.toast("CLIENT: Disconnected from Server...");
			MultiplayerExample.this.finish();
		}
	}

	private class ExampleServerStateListener implements ISocketServerListener<SocketConnectionClientConnector> {
		@Override
		public void onStarted(final SocketServer<SocketConnectionClientConnector> pSocketServer) {
			MultiplayerExample.this.toast("SERVER: Started.");
		}

		@Override
		public void onTerminated(final SocketServer<SocketConnectionClientConnector> pSocketServer) {
			MultiplayerExample.this.toast("SERVER: Terminated.");
		}

		@Override
		public void onException(final SocketServer<SocketConnectionClientConnector> pSocketServer, final Throwable pThrowable) {
			Debug.e(pThrowable);
			MultiplayerExample.this.toast("SERVER: Exception: " + pThrowable);
		}
	}

	private class ExampleClientConnectorListener implements ISocketConnectionClientConnectorListener {
		@Override
		public void onStarted(final ClientConnector<SocketConnection> pConnector) {
			MultiplayerExample.this.toast("SERVER: Client connected: " + pConnector.getConnection().getSocket().getInetAddress().getHostAddress());
		}

		@Override
		public void onTerminated(final ClientConnector<SocketConnection> pConnector) {
			MultiplayerExample.this.toast("SERVER: Client disconnected: " + pConnector.getConnection().getSocket().getInetAddress().getHostAddress());
		}
	}
}
