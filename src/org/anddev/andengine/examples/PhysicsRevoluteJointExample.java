package org.anddev.andengine.examples;

import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.util.MathUtils;

import android.widget.Toast;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
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

	private RevoluteJoint mRevoluteJoint;

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

		final float anchorFaceX = centerX - this.mBoxFaceTextureRegion.getWidth() * 0.5f;
		final float anchorFaceY = centerY - this.mBoxFaceTextureRegion.getHeight() * 0.5f;

		final AnimatedSprite anchorFace = new AnimatedSprite(anchorFaceX, anchorFaceY, this.mBoxFaceTextureRegion);
		final Body anchorBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, anchorFace, BodyType.StaticBody);

		final AnimatedSprite movingFace = new AnimatedSprite(anchorFaceX, anchorFaceY + 100, this.mBoxFaceTextureRegion);
		final Body movingBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, movingFace, BodyType.DynamicBody);

		anchorFace.animate(200);
		anchorFace.animate(200);
		anchorFace.setUpdatePhysics(false);
		movingFace.setUpdatePhysics(false);

		pScene.getTopLayer().addEntity(anchorFace);
		pScene.getTopLayer().addEntity(movingFace);
		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(anchorFace, anchorBody, true, true, false, false));
		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(movingFace, movingBody, true, true, false, false));

		final RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
		revoluteJointDef.initialize(anchorBody, movingBody, anchorBody.getWorldCenter());
		revoluteJointDef.enableMotor = true;
		revoluteJointDef.motorSpeed = MathUtils.degToRad(45);
		revoluteJointDef.maxMotorTorque = 100;
		//		revoluteJointDef.enableLimit = true;
		//		revoluteJointDef.lowerAngle = MathUtils.degToRad(180);
		//		revoluteJointDef.upperAngle = MathUtils.degToRad(360);

		this.mRevoluteJoint = (RevoluteJoint)this.mPhysicsWorld.createJoint(revoluteJointDef);
		Toast.makeText(this, "Motor speed: " + this.mRevoluteJoint.getMotorSpeed(), Toast.LENGTH_LONG).show();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
