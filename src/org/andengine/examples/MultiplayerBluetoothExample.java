package org.andengine.examples;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
import org.andengine.examples.util.BluetoothListDevicesActivity;
import org.andengine.extension.multiplayer.adt.message.IMessage;
import org.andengine.extension.multiplayer.adt.message.server.IServerMessage;
import org.andengine.extension.multiplayer.adt.message.server.ServerMessage;
import org.andengine.extension.multiplayer.client.IServerMessageHandler;
import org.andengine.extension.multiplayer.client.connector.BluetoothSocketConnectionServerConnector;
import org.andengine.extension.multiplayer.client.connector.BluetoothSocketConnectionServerConnector.IBluetoothSocketConnectionServerConnectorListener;
import org.andengine.extension.multiplayer.client.connector.ServerConnector;
import org.andengine.util.exception.BluetoothException;
import org.andengine.extension.multiplayer.server.BluetoothSocketServer;
import org.andengine.extension.multiplayer.server.BluetoothSocketServer.IBluetoothSocketServerListener;
import org.andengine.extension.multiplayer.server.connector.BluetoothSocketConnectionClientConnector;
import org.andengine.extension.multiplayer.server.connector.BluetoothSocketConnectionClientConnector.IBluetoothSocketConnectionClientConnectorListener;
import org.andengine.extension.multiplayer.server.connector.ClientConnector;
import org.andengine.extension.multiplayer.shared.BluetoothSocketConnection;
import org.andengine.extension.multiplayer.util.MessagePool;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.bitmap.AssetBitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.debug.Debug;

import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 11:45:03 - 06.03.2011
 */
public class MultiplayerBluetoothExample extends SimpleBaseGameActivity implements ClientMessageFlags, ServerMessageFlags {
	// ===========================================================
	// Constants
	// ===========================================================

	/** Create your own unique UUID at: http://www.uuidgenerator.com/ */
	private static final String EXAMPLE_UUID = "6D2DF50E-06EF-C21C-7DB0-345099A5F64E";

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	private static final short FLAG_MESSAGE_SERVER_ADD_SPRITE = 1;
	private static final short FLAG_MESSAGE_SERVER_MOVE_SPRITE = FLAG_MESSAGE_SERVER_ADD_SPRITE + 1;

	private static final int DIALOG_CHOOSE_SERVER_OR_CLIENT_ID = 0;
	private static final int DIALOG_SHOW_SERVER_IP_ID = DIALOG_CHOOSE_SERVER_OR_CLIENT_ID + 1;

	private static final int REQUESTCODE_BLUETOOTH_ENABLE = 0;
	private static final int REQUESTCODE_BLUETOOTH_CONNECT = REQUESTCODE_BLUETOOTH_ENABLE + 1;

	// ===========================================================
	// Fields
	// ===========================================================

	private ITexture mFaceTexture;
	private ITextureRegion mFaceTextureRegion;

	private int mSpriteIDCounter;
	private final SparseArray<Sprite> mSprites = new SparseArray<Sprite>();

	private String mServerMACAddress;
	private BluetoothSocketServer<BluetoothSocketConnectionClientConnector> mBluetoothSocketServer;
	private ServerConnector<BluetoothSocketConnection> mServerConnector;

	private final MessagePool<IMessage> mMessagePool = new MessagePool<IMessage>();

	private BluetoothAdapter mBluetoothAdapter;

	// ===========================================================
	// Constructors
	// ===========================================================

	public MultiplayerBluetoothExample() {
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
	protected void onCreate(final Bundle pSavedInstanceState) {
		super.onCreate(pSavedInstanceState);

		this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (this.mBluetoothAdapter == null) {
			Toast.makeText(this, "Bluetooth is not available!", Toast.LENGTH_LONG).show();
			this.finish();
			return;
		} else {
			this.mServerMACAddress = this.mBluetoothAdapter.getAddress();

			if (this.mBluetoothAdapter.isEnabled()) {
				this.showDialog(DIALOG_CHOOSE_SERVER_OR_CLIENT_ID);
			} else {
				final Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				this.startActivityForResult(enableIntent, REQUESTCODE_BLUETOOTH_ENABLE);
			}
		}
	}

	@Override
	protected Dialog onCreateDialog(final int pID) {
		switch(pID) {
			case DIALOG_SHOW_SERVER_IP_ID:
				return new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle("Server-Details")
				.setCancelable(false)
				.setMessage("The Name of your Server is:\n" + BluetoothAdapter.getDefaultAdapter().getName() + "\n" + "The MACAddress of your Server is:\n" + this.mServerMACAddress)
				.setPositiveButton(android.R.string.ok, null)
				.create();
			case DIALOG_CHOOSE_SERVER_OR_CLIENT_ID:
				return new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle("Be Server or Client ...")
				.setCancelable(false)
				.setPositiveButton("Client", new OnClickListener() {
					@Override
					public void onClick(final DialogInterface pDialog, final int pWhich) {
						final Intent intent = new Intent(MultiplayerBluetoothExample.this, BluetoothListDevicesActivity.class);
						MultiplayerBluetoothExample.this.startActivityForResult(intent, REQUESTCODE_BLUETOOTH_CONNECT);
					}
				})
				.setNeutralButton("Server", new OnClickListener() {
					@Override
					public void onClick(final DialogInterface pDialog, final int pWhich) {
						MultiplayerBluetoothExample.this.toast("You can add and move sprites, which are only shown on the clients.");
						MultiplayerBluetoothExample.this.initServer();
						MultiplayerBluetoothExample.this.showDialog(DIALOG_SHOW_SERVER_IP_ID);
					}
				})
				.setNegativeButton("Both", new OnClickListener() {
					@Override
					public void onClick(final DialogInterface pDialog, final int pWhich) {
						MultiplayerBluetoothExample.this.toast("You can add sprites and move them, by dragging them.");
						MultiplayerBluetoothExample.this.initServerAndClient();
						MultiplayerBluetoothExample.this.showDialog(DIALOG_SHOW_SERVER_IP_ID);
					}
				})
				.create();
			default:
				return super.onCreateDialog(pID);
		}
	}

	@Override
	public EngineOptions onCreateEngineOptions() {
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}

	@Override
	protected void onDestroy() {
		if(this.mBluetoothSocketServer != null) {
			this.mBluetoothSocketServer.sendBroadcastServerMessage(new ConnectionCloseServerMessage());
			this.mBluetoothSocketServer.terminate();
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
		if(MultiplayerBluetoothExample.this.mBluetoothSocketServer != null) {
			scene.setOnSceneTouchListener(new IOnSceneTouchListener() {
				@Override
				public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
					if(pSceneTouchEvent.isActionDown()) {
						final AddSpriteServerMessage addSpriteServerMessage = (AddSpriteServerMessage) MultiplayerBluetoothExample.this.mMessagePool.obtainMessage(FLAG_MESSAGE_SERVER_ADD_SPRITE);
						addSpriteServerMessage.set(MultiplayerBluetoothExample.this.mSpriteIDCounter++, pSceneTouchEvent.getX(), pSceneTouchEvent.getY());

						MultiplayerBluetoothExample.this.mBluetoothSocketServer.sendBroadcastServerMessage(addSpriteServerMessage);

						MultiplayerBluetoothExample.this.mMessagePool.recycleMessage(addSpriteServerMessage);
						return true;
					} else {
						return false;
					}
				}
			});

			scene.setOnAreaTouchListener(new IOnAreaTouchListener() {
				@Override
				public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final ITouchArea pTouchArea, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
					final Sprite sprite = (Sprite)pTouchArea;
					final int spriteID = (Integer)sprite.getUserData();

					final MoveSpriteServerMessage moveSpriteServerMessage = (MoveSpriteServerMessage) MultiplayerBluetoothExample.this.mMessagePool.obtainMessage(FLAG_MESSAGE_SERVER_MOVE_SPRITE);
					moveSpriteServerMessage.set(spriteID, pSceneTouchEvent.getX(), pSceneTouchEvent.getY());

					MultiplayerBluetoothExample.this.mBluetoothSocketServer.sendBroadcastServerMessage(moveSpriteServerMessage);

					MultiplayerBluetoothExample.this.mMessagePool.recycleMessage(moveSpriteServerMessage);
					return true;
				}
			});

			scene.setTouchAreaBindingOnActionDownEnabled(true);
		}

		return scene;
	}

	@Override
	protected void onActivityResult(final int pRequestCode, final int pResultCode, final Intent pData) {
		switch(pRequestCode) {
			case REQUESTCODE_BLUETOOTH_ENABLE:
				this.showDialog(DIALOG_CHOOSE_SERVER_OR_CLIENT_ID);
				break;
			case REQUESTCODE_BLUETOOTH_CONNECT:
				this.mServerMACAddress = pData.getExtras().getString(BluetoothListDevicesActivity.EXTRA_DEVICE_ADDRESS);
				this.initClient();
				break;
			default:
				super.onActivityResult(pRequestCode, pResultCode, pData);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void addSprite(final int pID, final float pX, final float pY) {
		final Scene scene = this.mEngine.getScene();
		/* Create the spriteand add it to the scene. */
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
		this.mServerMACAddress = BluetoothAdapter.getDefaultAdapter().getAddress();
		try {
			this.mBluetoothSocketServer = new BluetoothSocketServer<BluetoothSocketConnectionClientConnector>(EXAMPLE_UUID, new ExampleClientConnectorListener(), new ExampleServerStateListener()) {
				@Override
				protected BluetoothSocketConnectionClientConnector newClientConnector(final BluetoothSocketConnection pBluetoothSocketConnection) throws IOException {
					try {
						return new BluetoothSocketConnectionClientConnector(pBluetoothSocketConnection);
					} catch (final BluetoothException e) {
						Debug.e(e);
						/* Actually cannot happen. */
						return null;
					}
				}
			};
		} catch (final BluetoothException e) {
			Debug.e(e);
		}

		this.mBluetoothSocketServer.start();
	}

	private void initClient() {
		try {
			this.mServerConnector = new BluetoothSocketConnectionServerConnector(new BluetoothSocketConnection(this.mBluetoothAdapter, this.mServerMACAddress, EXAMPLE_UUID), new ExampleServerConnectorListener());

			this.mServerConnector.registerServerMessage(FLAG_MESSAGE_SERVER_CONNECTION_CLOSE, ConnectionCloseServerMessage.class, new IServerMessageHandler<BluetoothSocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<BluetoothSocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					MultiplayerBluetoothExample.this.finish();
				}
			});

			this.mServerConnector.registerServerMessage(FLAG_MESSAGE_SERVER_ADD_SPRITE, AddSpriteServerMessage.class, new IServerMessageHandler<BluetoothSocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<BluetoothSocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					final AddSpriteServerMessage addSpriteServerMessage = (AddSpriteServerMessage)pServerMessage;
					MultiplayerBluetoothExample.this.addSprite(addSpriteServerMessage.mID, addSpriteServerMessage.mX, addSpriteServerMessage.mY);
				}
			});

			this.mServerConnector.registerServerMessage(FLAG_MESSAGE_SERVER_MOVE_SPRITE, MoveSpriteServerMessage.class, new IServerMessageHandler<BluetoothSocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<BluetoothSocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					final MoveSpriteServerMessage moveSpriteServerMessage = (MoveSpriteServerMessage)pServerMessage;
					MultiplayerBluetoothExample.this.moveSprite(moveSpriteServerMessage.mID, moveSpriteServerMessage.mX, moveSpriteServerMessage.mY);
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

	private class ExampleServerConnectorListener implements IBluetoothSocketConnectionServerConnectorListener {
		@Override
		public void onStarted(final ServerConnector<BluetoothSocketConnection> pConnector) {
			MultiplayerBluetoothExample.this.toast("CLIENT: Connected to server.");
		}

		@Override
		public void onTerminated(final ServerConnector<BluetoothSocketConnection> pConnector) {
			MultiplayerBluetoothExample.this.toast("CLIENT: Disconnected from Server...");
			MultiplayerBluetoothExample.this.finish();
		}
	}

	private class ExampleServerStateListener implements IBluetoothSocketServerListener<BluetoothSocketConnectionClientConnector> {
		@Override
		public void onStarted(final BluetoothSocketServer<BluetoothSocketConnectionClientConnector> pBluetoothSocketServer) {
			MultiplayerBluetoothExample.this.toast("SERVER: Started.");
		}

		@Override
		public void onTerminated(final BluetoothSocketServer<BluetoothSocketConnectionClientConnector> pBluetoothSocketServer) {
			MultiplayerBluetoothExample.this.toast("SERVER: Terminated.");
		}

		@Override
		public void onException(final BluetoothSocketServer<BluetoothSocketConnectionClientConnector> pBluetoothSocketServer, final Throwable pThrowable) {
			Debug.e(pThrowable);
			MultiplayerBluetoothExample.this.toast("SERVER: Exception: " + pThrowable);
		}
	}

	private class ExampleClientConnectorListener implements IBluetoothSocketConnectionClientConnectorListener {
		@Override
		public void onStarted(final ClientConnector<BluetoothSocketConnection> pConnector) {
			MultiplayerBluetoothExample.this.toast("SERVER: Client connected: " + pConnector.getConnection().getBluetoothSocket().getRemoteDevice().getAddress());
		}

		@Override
		public void onTerminated(final ClientConnector<BluetoothSocketConnection> pConnector) {
			MultiplayerBluetoothExample.this.toast("SERVER: Client disconnected: " + pConnector.getConnection().getBluetoothSocket().getRemoteDevice().getAddress());
		}
	}
}
