package org.anddev.andengine.examples;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnAreaTouchListener;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.Scene.ITouchArea;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.IMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.BaseClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.BaseServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionAcceptedServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionRefusedServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.client.IServerConnectionListener;
import org.anddev.andengine.extension.multiplayer.protocol.client.IServerMessageHandler.DefaultServerMessageHandler;
import org.anddev.andengine.extension.multiplayer.protocol.client.ServerConnection;
import org.anddev.andengine.extension.multiplayer.protocol.server.BaseServer;
import org.anddev.andengine.extension.multiplayer.protocol.server.BaseServer.IServerStateListener;
import org.anddev.andengine.extension.multiplayer.protocol.server.ClientConnection;
import org.anddev.andengine.extension.multiplayer.protocol.server.ClientConnection.IClientConnectionListener;
import org.anddev.andengine.extension.multiplayer.protocol.server.IClientMessageHandler.DefaultClientMessageHandler;
import org.anddev.andengine.extension.multiplayer.protocol.shared.Connection;
import org.anddev.andengine.extension.multiplayer.protocol.util.IPUtils;
import org.anddev.andengine.extension.multiplayer.protocol.util.MessagePool;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.util.Debug;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.SparseArray;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author Nicolas Gramlich
 * @since 17:10:24 - 19.06.2010
 */
public class MultiplayerExample extends BaseExample {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final String LOCALHOST_IP = "127.0.0.1";

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	private static final int SERVER_PORT = 4444;

	private static final short FLAG_MESSAGE_SERVER_ADD_FACE = 1;
	private static final short FLAG_MESSAGE_SERVER_MOVE_FACE = FLAG_MESSAGE_SERVER_ADD_FACE + 1;

	private static final int DIALOG_CHOOSE_SERVER_OR_CLIENT_ID = 0;
	private static final int DIALOG_ENTER_SERVER_IP_ID = DIALOG_CHOOSE_SERVER_OR_CLIENT_ID + 1;
	private static final int DIALOG_SHOW_SERVER_IP_ID = DIALOG_ENTER_SERVER_IP_ID + 1;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;

	private Texture mTexture;
	private TextureRegion mFaceTextureRegion;

	private int mFaceIDCounter;
	private final SparseArray<Sprite> mFaces = new SparseArray<Sprite>();

	private String mServerIP = LOCALHOST_IP;
	private BaseServer<ClientConnection> mServer;
	private ServerConnection mServerConnection;

	private final MessagePool<IMessage> mMessagePool = new MessagePool<IMessage>();

	// ===========================================================
	// Constructors
	// ===========================================================

	public MultiplayerExample() {
		this.initMessagePool();
	}

	private void initMessagePool() {
		this.mMessagePool.registerMessage(FLAG_MESSAGE_SERVER_ADD_FACE, AddFaceServerMessage.class);
		this.mMessagePool.registerMessage(FLAG_MESSAGE_SERVER_MOVE_FACE, MoveFaceServerMessage.class);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected Dialog onCreateDialog(final int pID) {
		switch(pID) {
			case DIALOG_SHOW_SERVER_IP_ID:
				return new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle("Your Server-IP ...")
				.setCancelable(false)
				.setMessage("The IP of your Server is:\n" + IPUtils.getIPAddress(this))
				.setPositiveButton(android.R.string.ok, null)
				.create();
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
						MultiplayerExample.this.initServer();
						MultiplayerExample.this.showDialog(DIALOG_SHOW_SERVER_IP_ID);
					}
				})
				.setNegativeButton("Both", new OnClickListener() {
					@Override
					public void onClick(final DialogInterface pDialog, final int pWhich) {
						MultiplayerExample.this.toast("You can move sprites, by dragging them.");
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
	public Engine onLoadEngine() {
		this.showDialog(DIALOG_CHOOSE_SERVER_OR_CLIENT_ID);

		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera));
	}

	@Override
	protected void onDestroy() {
		if(this.mServer != null) {
			this.mServer.interrupt();
		}

		if(this.mServerConnection != null) {
			this.mServerConnection.interrupt();
		}

		super.onDestroy();
	}

	@Override
	public void onLoadResources() {
		this.mTexture = new Texture(32, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFaceTextureRegion = TextureRegionFactory.createFromAsset(this.mTexture, this, "gfx/face_box.png", 0, 0);

		this.mEngine.getTextureManager().loadTexture(this.mTexture);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene(1);
		scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

		if(MultiplayerExample.this.mServer != null) {
			scene.setOnSceneTouchListener(new IOnSceneTouchListener() {
				@Override
				public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
					if(pSceneTouchEvent.isActionDown()) {
						try {
							final AddFaceServerMessage addFaceServerMessage = (AddFaceServerMessage) MultiplayerExample.this.mMessagePool.obtainMessage(FLAG_MESSAGE_SERVER_ADD_FACE);
							addFaceServerMessage.set(MultiplayerExample.this.mFaceIDCounter++, pSceneTouchEvent.getX(), pSceneTouchEvent.getY());

							MultiplayerExample.this.mServer.sendBroadcastServerMessage(addFaceServerMessage);

							MultiplayerExample.this.mMessagePool.recycleMessage(addFaceServerMessage);
						} catch (final IOException e) {
							Debug.e(e);
						}
						return true;
					} else {
						return false;
					}
				}
			});

			scene.setOnAreaTouchListener(new IOnAreaTouchListener() {
				@Override
				public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final ITouchArea pTouchArea, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
					try {
						final Sprite face = (Sprite)pTouchArea;
						final Integer faceID = (Integer)face.getUserData();

						final MoveFaceServerMessage moveFaceServerMessage = (MoveFaceServerMessage) MultiplayerExample.this.mMessagePool.obtainMessage(FLAG_MESSAGE_SERVER_ADD_FACE);
						moveFaceServerMessage.set(faceID, pSceneTouchEvent.getX(), pSceneTouchEvent.getY());

						MultiplayerExample.this.mServer.sendBroadcastServerMessage(moveFaceServerMessage);

						MultiplayerExample.this.mMessagePool.recycleMessage(moveFaceServerMessage);
					} catch (final IOException e) {
						Debug.e(e);
						return false;
					}
					return true;
				}
			});

			scene.setTouchAreaBindingEnabled(true);
		}

		return scene;
	}

	@Override
	public void onLoadComplete() {

	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void addFace(final int pID, final float pX, final float pY) {
		final Scene scene = this.mEngine.getScene();
		/* Create the face and add it to the scene. */
		final Sprite face = new Sprite(0, 0, this.mFaceTextureRegion);
		face.setPosition(pX - face.getWidth() * 0.5f, pY - face.getHeight() * 0.5f);
		face.setUserData(pID);
		this.mFaces.put(pID, face);
		scene.registerTouchArea(face);
		scene.getLastChild().attachChild(face);
	}

	public void moveFace(final int pID, final float pX, final float pY) {
		/* Find and move the face. */
		final Sprite face = this.mFaces.get(pID);
		face.setPosition(pX - face.getWidth() * 0.5f, pY - face.getHeight() * 0.5f);
	}

	private void initServerAndClient() {
		MultiplayerExample.this.initServer();

		/* Wait some time after the server has been started, so it actually can start up. */
		try {
			Thread.sleep(500);
		} catch (final Throwable t) {
			Debug.e("Error", t);
		}

		MultiplayerExample.this.initClient();
	}

	private void initServer() {
		this.mServer = new BaseServer<ClientConnection>(SERVER_PORT, new ExampleClientConnectionListener(), new ExampleServerStateListener()){
			@Override
			protected ClientConnection newClientConnection(final Socket pClientSocket, final IClientConnectionListener pClientConnectionListener) throws Exception {
				return new ClientConnection(pClientSocket, pClientConnectionListener, new DefaultClientMessageHandler() {
					@Override
					public void onHandleMessage(final ClientConnection pClientConnection, final BaseClientMessage pClientMessage) throws IOException {
						super.onHandleMessage(pClientConnection, pClientMessage);
						MultiplayerExample.this.log("SERVER: ClientMessage received: " + pClientMessage.toString());
					}
				});
			}
		};

		this.mServer.start();
	}

	private void initClient() {
		try {
			this.mServerConnection = new ServerConnection(new Socket(this.mServerIP, SERVER_PORT), new ExampleServerConnectionListener(), new DefaultServerMessageHandler() {

				@Override
				protected void onHandleConnectionAcceptedServerMessage(final ServerConnection pServerConnection, final ConnectionAcceptedServerMessage pConnectionAcceptedServerMessage) {
					MultiplayerExample.this.log("CLIENT: Connection accepted.");
				}
				@Override
				protected void onHandleConnectionRefusedServerMessage(final ServerConnection pServerConnection, final ConnectionRefusedServerMessage pConnectionRefusedServerMessage) {
					MultiplayerExample.this.log("CLIENT: Connection refused.");
				}

				@Override
				public void onHandleMessage(final ServerConnection pServerConnection, final BaseServerMessage pServerMessage) throws IOException {
					switch(pServerMessage.getFlag()) {
						case FLAG_MESSAGE_SERVER_ADD_FACE:
							final AddFaceServerMessage addFaceServerMessage = (AddFaceServerMessage)pServerMessage;
							MultiplayerExample.this.addFace(addFaceServerMessage.mID, addFaceServerMessage.mX, addFaceServerMessage.mY);
							break;
						case FLAG_MESSAGE_SERVER_MOVE_FACE:
							final MoveFaceServerMessage moveFaceServerMessage = (MoveFaceServerMessage)pServerMessage;
							MultiplayerExample.this.moveFace(moveFaceServerMessage.mID, moveFaceServerMessage.mX, moveFaceServerMessage.mY);
							break;
						default:
							super.onHandleMessage(pServerConnection, pServerMessage);
							MultiplayerExample.this.log("CLIENT: ServerMessage received: " + pServerMessage.toString());
					}
				}
			});

			this.mServerConnection.registerServerMessage(FLAG_MESSAGE_SERVER_ADD_FACE, AddFaceServerMessage.class);
			this.mServerConnection.registerServerMessage(FLAG_MESSAGE_SERVER_MOVE_FACE, MoveFaceServerMessage.class);

			this.mServerConnection.start();
		} catch (final Throwable t) {
			Debug.e("Error", t);
		}
	}

	private void log(final String pMessage) {
		Debug.d(pMessage);
	}

	private void toast(final String pMessage) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(MultiplayerExample.this, pMessage, Toast.LENGTH_SHORT).show();
			}
		});
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class AddFaceServerMessage extends BaseServerMessage {
		private int mID;
		private float mX;
		private float mY;

		public AddFaceServerMessage() {

		}

		public AddFaceServerMessage(final int pID, final float pX, final float pY) {
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
			return FLAG_MESSAGE_SERVER_ADD_FACE;
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

		@Override
		protected void onAppendTransmissionDataForToString(final StringBuilder pStringBuilder) {

		}
	}

	public static class MoveFaceServerMessage extends BaseServerMessage {
		private int mID;
		private float mX;
		private float mY;

		public MoveFaceServerMessage() {

		}

		public MoveFaceServerMessage(final int pID, final float pX, final float pY) {
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
			return FLAG_MESSAGE_SERVER_MOVE_FACE;
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

		@Override
		protected void onAppendTransmissionDataForToString(final StringBuilder pStringBuilder) {

		}
	}

	private class ExampleServerConnectionListener implements IServerConnectionListener {
		@Override
		public void onConnected(final Connection pConnection) {
			MultiplayerExample.this.toast("CLIENT: Connected to server.");
		}

		@Override
		public void onDisconnected(final Connection pConnection) {
			MultiplayerExample.this.toast("CLIENT: Disconnected from Server...");
			MultiplayerExample.this.finish();
		}
	}

	private class ExampleServerStateListener implements IServerStateListener {
		@Override
		public void onStarted(final int pPort) {
			MultiplayerExample.this.log("SERVER: Started at port: " + pPort);
		}

		@Override
		public void onTerminated(final int pPort) {
			MultiplayerExample.this.log("SERVER: Terminated at port: " + pPort);
		}

		@Override
		public void onException(final Throwable pThrowable) {
			MultiplayerExample.this.log("SERVER: Exception: " + pThrowable);
		}
	}

	private class ExampleClientConnectionListener implements IClientConnectionListener {
		@Override
		public void onConnected(final Connection pConnection) {
			MultiplayerExample.this.toast("SERVER: Client connected: " + pConnection.getSocket().getInetAddress().getHostAddress());
		}

		@Override
		public void onDisconnected(final Connection pConnection) {
			MultiplayerExample.this.toast("SERVER: Client disconnected: " + pConnection.getSocket().getInetAddress().getHostAddress());
		}
	}
}
