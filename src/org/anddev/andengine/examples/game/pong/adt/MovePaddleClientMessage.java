package org.anddev.andengine.examples.game.pong.adt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.anddev.andengine.examples.game.pong.util.constants.PongConstants;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.BaseClientMessage;

/**
 * @author Nicolas Gramlich
 * @since 19:52:27 - 28.02.2011
 */
public class MovePaddleClientMessage extends BaseClientMessage implements PongConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	
	public final int mPaddleID;
	public final float mX;

	// ===========================================================
	// Constructors
	// ===========================================================

	public MovePaddleClientMessage(final int pID, final float pX) {
		this.mPaddleID = pID;
		this.mX = pX;
	}

	public MovePaddleClientMessage(final DataInputStream pDataInputStream) throws IOException {
		this.mPaddleID = pDataInputStream.readInt();
		this.mX = pDataInputStream.readFloat();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public short getFlag() {
		return FLAG_MESSAGE_CLIENT_MOVE_PADDLE;
	}

	@Override
	protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
		pDataOutputStream.writeInt(this.mPaddleID);
		pDataOutputStream.writeFloat(this.mX);
	}

	@Override
	protected void onAppendTransmissionDataForToString(final StringBuilder pStringBuilder) {

	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}