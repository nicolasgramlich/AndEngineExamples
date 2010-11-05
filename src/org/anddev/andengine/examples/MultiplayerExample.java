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
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.BaseClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.BaseServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionAcceptedServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionRefusedServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.client.BaseServerConnectionListener;
import org.anddev.andengine.extension.multiplayer.protocol.client.BaseServerMessageSwitch;
import org.anddev.andengine.extension.multiplayer.protocol.client.ServerConnector;
import org.anddev.andengine.extension.multiplayer.protocol.client.ServerMessageExtractor;
import org.anddev.andengine.extension.multiplayer.protocol.server.BaseClientConnectionListener;
import org.anddev.andengine.extension.multiplayer.protocol.server.BaseClientMessageSwitch;
import org.anddev.andengine.extension.multiplayer.protocol.server.BaseServer;
import org.anddev.andengine.extension.multiplayer.protocol.server.ClientConnector;
import org.anddev.andengine.extension.multiplayer.protocol.server.ClientMessageExtractor;
import org.anddev.andengine.extension.multiplayer.protocol.server.BaseServer.IServerStateListener;
import org.anddev.andengine.extension.multiplayer.protocol.shared.BaseConnector;
import org.anddev.andengine.extension.multiplayer.protocol.util.IPUtils;
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

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	private static final int SERVER_PORT = 4444;

	private static final short FLAG_MESSAGE_SERVER_ADD_FACE = 1;

	private static final int DIALOG_CHOOSE_SERVER_OR_CLIENT_ID = 0;
	private static final int DIALOG_ENTER_SERVER_IP_ID = DIALOG_CHOOSE_SERVER_OR_CLIENT_ID + 1;
	private static final int DIALOG_SHOW_SERVER_IP_ID = DIALOG_ENTER_SERVER_IP_ID + 1;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;
	private Texture mTexture;
	private TextureRegion mFaceTextureRegion;

	private BaseServer<ClientConnector> mServer;

	private ServerConnector mServerConnector;

	private String mServerIP = "127.0.0.1";

	// ===========================================================
	// Constructors
	// ===========================================================

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

		if(this.mServerConnector != null) {
			this.mServerConnector.interrupt();
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

		scene.setOnSceneTouchListener(new IOnSceneTouchListener() {
			@Override
			public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
				if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
					if(MultiplayerExample.this.mServer != null) {
						try {
							MultiplayerExample.this.mServer.sendBroadcastServerMessage(new AddFaceServerMessage(pSceneTouchEvent.getX(), pSceneTouchEvent.getY()));
						} catch (final IOException e) {
							Debug.e(e);
						}
					}
					return true;
				} else {
					return false;
				}
			}
		});

		return scene;
	}

	@Override
	public void onLoadComplete() {

	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void addFace(final Scene pScene, final float pX, final float pY) {
		/* Create the face and add it to the scene. */
		final Sprite face = new Sprite(pX, pY, this.mFaceTextureRegion);
		pScene.getTopLayer().addEntity(face);
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
		this.mServer = new BaseServer<ClientConnector>(SERVER_PORT, new ExampleClientConnectionListener(), new ExampleServerStateListener()){
			@Override
			protected ClientConnector newClientConnector(final Socket pClientSocket, final BaseClientConnectionListener pClientConnectionListener) throws Exception {
				return new ClientConnector(pClientSocket, pClientConnectionListener,
						new ClientMessageExtractor(){
					@Override
					public BaseClientMessage readMessage(final short pFlag, final DataInputStream pDataInputStream) throws IOException {
						return super.readMessage(pFlag, pDataInputStream);
					}
				},
				new BaseClientMessageSwitch() {
					@Override
					public void doSwitch(final ClientConnector pClientConnector, final BaseClientMessage pClientMessage) throws IOException {
						super.doSwitch(pClientConnector, pClientMessage);
						MultiplayerExample.this.log("SERVER: ClientMessage received: " + pClientMessage.toString());
					}
				}
				);
			}
		};

		this.mServer.start();
	}

	private void initClient() {
		try {
			this.mServerConnector = new ServerConnector(new Socket(this.mServerIP, SERVER_PORT), new ExampleServerConnectionListener(),
					new ServerMessageExtractor() {
				@Override
				public BaseServerMessage readMessage(final short pFlag, final DataInputStream pDataInputStream) throws IOException {
					switch(pFlag) {
						case FLAG_MESSAGE_SERVER_ADD_FACE:
							return new AddFaceServerMessage(pDataInputStream);
						default:
							return super.readMessage(pFlag, pDataInputStream);
					}
				}
			},
			new BaseServerMessageSwitch() {
				@Override
				public void doSwitch(final ServerConnector pServerConnector, final BaseServerMessage pServerMessage) throws IOException {
					switch(pServerMessage.getFlag()) {
						case FLAG_MESSAGE_SERVER_ADD_FACE:
							final AddFaceServerMessage addFaceServerMessage = (AddFaceServerMessage)pServerMessage;
							MultiplayerExample.this.addFace(MultiplayerExample.this.mEngine.getScene(), addFaceServerMessage.mX, addFaceServerMessage.mY);
							break;
						default:
							super.doSwitch(pServerConnector, pServerMessage);
							MultiplayerExample.this.log("CLIENT: ServerMessage received: " + pServerMessage.toString());
					}
				}

				@Override
				protected void onHandleConnectionAcceptedServerMessage(final ServerConnector pServerConnector, final ConnectionAcceptedServerMessage pServerMessage) {
					MultiplayerExample.this.log("CLIENT: Connection accepted.");
				}

				@Override
				protected void onHandleConnectionRefusedServerMessage(final ServerConnector pServerConnector, final ConnectionRefusedServerMessage pServerMessage) {
					MultiplayerExample.this.log("CLIENT: Connection refused.");
				}
			}
			);

			this.mServerConnector.start();
		} catch (final Throwable t) {
			Debug.e("Error", t);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	private static class AddFaceServerMessage extends BaseServerMessage {
		public final float mX;
		public final float mY;

		public AddFaceServerMessage(final float pX, final float pY) {
			this.mX = pX;
			this.mY = pY;
		}

		public AddFaceServerMessage(final DataInputStream pDataInputStream) throws IOException {
			this.mX = pDataInputStream.readFloat();
			this.mY = pDataInputStream.readFloat();
		}

		@Override
		public short getFlag() {
			return FLAG_MESSAGE_SERVER_ADD_FACE;
		}

		@Override
		protected void onAppendTransmissionDataForToString(final StringBuilder pStringBuilder) {

		}

		@Override
		protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
			pDataOutputStream.writeFloat(this.mX);
			pDataOutputStream.writeFloat(this.mY);
		}
	}

	private class ExampleServerConnectionListener extends BaseServerConnectionListener {
		@Override
		protected void onConnectInner(final BaseConnector<BaseServerMessage> pConnector) {
			MultiplayerExample.this.toast("CLIENT: Connected to server.");
		}

		@Override
		protected void onDisconnectInner(final BaseConnector<BaseServerMessage> pConnector) {
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

	private class ExampleClientConnectionListener extends BaseClientConnectionListener {
		@Override
		protected void onConnectInner(final BaseConnector<BaseClientMessage> pConnector) {
			MultiplayerExample.this.toast("SERVER: Client connected: " + pConnector.getSocket().getInetAddress().getHostAddress());
		}

		@Override
		protected void onDisconnectInner(final BaseConnector<BaseClientMessage> pConnector) {
			MultiplayerExample.this.toast("SERVER: Client disconnected: " + pConnector.getSocket().getInetAddress().getHostAddress());
		}
	}
}
