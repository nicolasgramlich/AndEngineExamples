package org.andengine.examples;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Ellipse;
import org.andengine.entity.primitive.PolyLine;
import org.andengine.entity.primitive.Polygon;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;

/**
 * 
 * @author Rodrigo Castro
 * @since 23:05:11 - 28.01.2012
 */
public class PolygonExample extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

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
	public EngineOptions onCreateEngineOptions() {
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}

	@Override
	public void onCreateResources() {

	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
		
		// The polygon is closed automatically
		final float[] vertexX1 = { 0.0f, 200.0f, 250.0f, 200.0f, 200.0f, 100.0f, 100.0f,   0.0f};
		final float[] vertexY1 = { 0.0f,   0.0f, 50.0f, 100.0f,  200.0f, 150.0f,  50.0f,  50.0f};
		
		// This polygon won't be drawn because its vertices are not CCW (Counter ClockWise)
		final float[] vertexX2 = { 0.0f,   0.0f, 100.0f, 100.0f, 200.0f, 200.0f, 250.0f,  200.0f};
		final float[] vertexY2 = { 0.0f,  50.0f,  50.0f, 150.0f,  200.0f, 100.0f,  50.0f,   0.0f};
		
		final float[] vertexX3 = { 0.0f,   0.0f, 200.0f, 200.0f  };
		final float[] vertexY3 = { 0.0f, 100.0f, 100.0f, 0.0f };
		
		// Dummy vertices (won't be used)
		final float[] vertexX4Dummy = { 0.0f,   0.0f,   0.0f,   0.0f,   0.0f };
		final float[] vertexY4Dummy = { 0.0f,   0.0f,   0.0f,   0.0f,   0.0f };
		// This polygon can't be triangulated with the current algorithm and will trigger a null pointer exception
		final float[] vertexX4 = { 0.0f,  50.0f, 100.0f, 100.0f,   0.0f };
		final float[] vertexY4 = { 0.0f, 150.0f,   0.0f, 100.0f, 100.0f };

		final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();

		final Polygon polygon1 = new Polygon(100, 100, vertexX1, vertexY1, vertexBufferObjectManager);
		polygon1.setColor(Color.RED);
		
		final Polygon polygon2 = new Polygon(400, 500, vertexX2, vertexY2, vertexBufferObjectManager);
		polygon2.setColor(Color.GREEN);
		
		final Polygon polygon3 = new Polygon(20, 350, vertexX3, vertexY3, vertexBufferObjectManager);
		polygon3.setColor(Color.PINK);
		
		//final Polygon polygon4 = new Polygon(20, 350, vertexX4, vertexY4, vertexBufferObjectManager);
		//polygon4.setColor(Color.CYAN);
		
		final PolyLine polyLine = new PolyLine(500, 50, vertexX4Dummy, vertexY4Dummy, vertexBufferObjectManager);
		polyLine.setColor(Color.YELLOW);
		polyLine.setLineWidth( 7f );
		// Update and use real vertices
		polyLine.updateVertices(vertexX4, vertexY4);
		
		final Ellipse ellipse = new Ellipse(430, 200, 100.0f, 50.0f, vertexBufferObjectManager);
		ellipse.setColor(Color.CYAN);
		
		final Rectangle rectangle = new Rectangle(300, 300, 200, 100, vertexBufferObjectManager);

		scene.attachChild(polygon1);
		scene.attachChild(polygon2);
		scene.attachChild(polygon3);
		//scene.attachChild(polygon4);
		scene.attachChild(polyLine);
		scene.attachChild(ellipse);
		scene.attachChild(rectangle);

		return scene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
