package org.anddev.andengine.examples;

import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

/**
 * @author Nicolas Gramlich
 * @since 18:47:08 - 19.03.2010
 */
public class PhysicsRevoluteJointExample extends BasePhysicsJointExample {
	// ===========================================================
	// Constants
	// ===========================================================

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
	public Scene onLoadScene() {
		final Scene scene = super.onLoadScene();
		this.initJoints(scene);
		return scene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void initJoints(final Scene pScene) {
		final int centerX = CAMERA_WIDTH / 2;
		final int centerY = CAMERA_HEIGHT / 2;
		
		final int spriteWidth = this.mBoxFaceTextureRegion.getTileWidth();
		final int spriteHeight = this.mBoxFaceTextureRegion.getTileHeight();

		final FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);

		for(int i = 0; i < 4; i++) {
			final float anchorFaceX = centerX - spriteWidth * 0.5f - (spriteWidth + 2) * (i - 2);
			final float anchorFaceY = centerY - spriteHeight * 0.5f - spriteHeight;

			final AnimatedSprite anchorFace = new AnimatedSprite(anchorFaceX, anchorFaceY, this.mBoxFaceTextureRegion);
			final Body anchorBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, anchorFace, BodyType.StaticBody, objectFixtureDef);

			final AnimatedSprite movingFace = new AnimatedSprite(anchorFaceX, anchorFaceY + 100, this.mCircleFaceTextureRegion);
			final Body movingBody = PhysicsFactory.createCircleBody(this.mPhysicsWorld, movingFace, BodyType.DynamicBody, objectFixtureDef);

			anchorFace.animate(200);
			anchorFace.animate(200);
			anchorFace.setUpdatePhysics(false);
			movingFace.setUpdatePhysics(false);

			pScene.getTopLayer().addEntity(anchorFace);
			pScene.getTopLayer().addEntity(movingFace);
			
			final Line connectionLine = new Line(anchorFaceX + spriteWidth / 2, anchorFaceY + spriteHeight / 2, anchorFaceX + spriteWidth / 2, anchorFaceY + spriteHeight / 2);
			pScene.getBottomLayer().addEntity(connectionLine);
			this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(anchorFace, anchorBody, true, true, false, false){
				@Override
				public void onUpdate(float pSecondsElapsed) {
					super.onUpdate(pSecondsElapsed);
					final Vector2 movingBodyWorldCenter = movingBody.getWorldCenter();
					connectionLine.setPosition(connectionLine.getX1(), connectionLine.getY1(), movingBodyWorldCenter.x, movingBodyWorldCenter.y);
				}
			});
			this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(movingFace, movingBody, true, true, false, false));


			final RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
			revoluteJointDef.initialize(anchorBody, movingBody, anchorBody.getWorldCenter());

			this.mPhysicsWorld.createJoint(revoluteJointDef);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
