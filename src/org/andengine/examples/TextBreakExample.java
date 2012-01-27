package org.andengine.examples;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.Text.TextOptions;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleLayoutGameActivity;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;

import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class TextBreakExample extends SimpleLayoutGameActivity implements TextWatcher {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	private static final float AUTOWRAP_WIDTH = 720 - 50 - 50;

	// ===========================================================
	// Fields
	// ===========================================================

	private EditText mEditText;

	private Font mFont;
	private Text mText;
	private Line mRight;
	private Line mLeft;

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
	protected int getLayoutID() {
		return R.layout.textbreakexample;
	}

	@Override
	protected int getRenderSurfaceViewID() {
		return R.id.textbreakexample_rendersurfaceview;
	}

	@Override
	protected void onSetContentView() {
		super.onSetContentView();

		this.mEditText = (EditText)this.findViewById(R.id.textbreakexample_text);
		this.mEditText.addTextChangedListener(this);
	}

	@Override
	public EngineOptions onCreateEngineOptions() {
		final Camera camera = new Camera(0, 0, TextBreakExample.CAMERA_WIDTH, TextBreakExample.CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(TextBreakExample.CAMERA_WIDTH, TextBreakExample.CAMERA_HEIGHT), camera);
	}

	@Override
	public void onCreateResources() {
		this.mFont = FontFactory.create(256, 256, TextureOptions.BILINEAR, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 24).load(this.getTextureManager(), this.getFontManager());
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
		this.mText = new Text(50, 40, this.mFont, "", new TextOptions(true, AUTOWRAP_WIDTH, Text.LEADING_DEFAULT, HorizontalAlign.LEFT), vertexBufferObjectManager);
		scene.attachChild(this.mText);

		this.mLeft = new Line(0, 0, 0, TextBreakExample.CAMERA_HEIGHT, this.getVertexBufferObjectManager());
		this.mRight = new Line(0, 0, 0, TextBreakExample.CAMERA_HEIGHT, this.getVertexBufferObjectManager());

		this.mText.attachChild(this.mLeft);
		this.mText.attachChild(this.mRight);

		final Line breakLine = new Line(AUTOWRAP_WIDTH, 0, AUTOWRAP_WIDTH, TextBreakExample.CAMERA_HEIGHT, this.getVertexBufferObjectManager());
		breakLine.setLineWidth(2);
		breakLine.setColor(Color.RED);
		this.mText.attachChild(breakLine);

		this.updateText();

		return scene;
	}

	@Override
	public void afterTextChanged(final Editable pEditable) {
		this.updateText();
	}

	private void updateText() {
		final String string = this.mEditText.getText().toString();
		this.mText.setText(string);
		
		final float width = this.mText.getMaximumLineWidth();
		this.mRight.setPosition(width, 0, width, TextBreakExample.CAMERA_HEIGHT);
	}

	@Override
	public void beforeTextChanged(final CharSequence pCharSequence, final int pStart, final int pCount, final int pAfter) {
		/* Nothing. */
	}

	@Override
	public void onTextChanged(final CharSequence pCharSequence, final int pStart, final int pBefore, final int pCount) {
		/* Nothing. */
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
