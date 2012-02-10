var AndEnginePackages = JavaImporter(
	// ###########
	// ENGINE
	// ###########

	// Widgets
	Packages.android.widget,

	// ###########
	// ENGINE
	// ###########

	// Engine
	Packages.org.andengine.engine,
	Packages.org.andengine.engine.options,

	// Handlers
	Packages.org.andengine.engine.handler,
	Packages.org.andengine.engine.handler.collision,
	Packages.org.andengine.engine.handler.physics,
	Packages.org.andengine.engine.handler.runnable,
	Packages.org.andengine.engine.handler.timer,
	
	// Camera
	Packages.org.andengine.engine.camera,
	
	// Touch
	Packages.org.andengine.input.touch,
	Packages.org.andengine.input.touch.controller,
	Packages.org.andengine.input.touch.detector,

	// ###########
	// OPENGL
	// ###########

	// Textures
	Packages.org.andengine.opengl.texture,
	Packages.org.andengine.opengl.texture.atlas,
	Packages.org.andengine.opengl.texture.atlas.bitmap,
	Packages.org.andengine.opengl.texture.atlas.bitmap.source,
	Packages.org.andengine.opengl.texture.atlas.buildable,
	Packages.org.andengine.opengl.texture.bitmap,
	Packages.org.andengine.opengl.texture.compressed.etc1,
	Packages.org.andengine.opengl.texture.compressed.pvr,
	Packages.org.andengine.opengl.texture.region,
	Packages.org.andengine.opengl.texture.render,

	// Util
	Packages.org.andengine.opengl.texture.util,
	
	// ###########
	// ENTITY
	// ###########

	// Entity
	Packages.org.andengine.entity.sprite,
	Packages.org.andengine.entity.primitive,
	Packages.org.andengine.entity.scene,
	Packages.org.andengine.entity.shape,
	Packages.org.andengine.entity.sprite,
	Packages.org.andengine.entity.sprite.batch,
	Packages.org.andengine.entity.text,

	// Modifier
	Packages.org.andengine.entity.modifier,
	Packages.org.andengine.util.modifier.ease,

	// Util
	Packages.org.andengine.entity.util,
	
	// ###########
	// UTIL
	// ###########
	Packages.org.andengine.util,
	Packages.org.andengine.util.math,
	Packages.org.andengine.util.spatial,
	Packages.org.andengine.util.spatial.adt.bounds,
	Packages.org.andengine.util.spatial.adt.bounds.util,
	Packages.org.andengine.util.spatial.quadtree,

	Packages.android.graphics
);

with(AndEnginePackages) {
	var engine = mContext.getEngine();
	var scene = engine.getScene();
	var camera = engine.getCamera();
	
	var size = 100;
	
	var x = MathUtils.random(0, camera.getWidth()) - size / 2;
	var y = MathUtils.random(0, camera.getHeight()) - size / 2;

	/*
	var rectangle = new Rectangle(x, y, size, size, engine.getVertexBufferObjectManager());
	*/

	var bounds = new AbstractIntBounds(0, 1, 2, 3) {
		getXMin: function() {
			return 4;
		}
	}

	var point = new AbstractPoint(0, 1) {
		set: function(pX, pY) {
			return;
		}
	}

	var rectangle = new AbstractRectangle(x, y, size, size, engine.getVertexBufferObjectManager()) {
		onAreaTouched: function(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY) {
			pTouchArea.setColor(1, 1, 0, 1);
		}
	};

	rectangle.setColor(1, 0, 0, 1);
	scene.attachChild(rectangle);
	scene.registerTouchArea(rectangle);
	scene.setOnAreaTouchListener(new Packages.org.andengine.entity.scene.Scene.IOnAreaTouchListener() {
		onAreaTouched: function(pSceneTouchEvent, pTouchArea, pTouchAreaLocalX, pTouchAreaLocalY) {
			pTouchArea.setColor(0, 1, 0, 1);
			pTouchArea.registerEntityModifier(new LoopEntityModifier(new RotationModifier(6, 0, 360, EaseExponentialInOut.getInstance())));
			return true;
		}
	});
}
