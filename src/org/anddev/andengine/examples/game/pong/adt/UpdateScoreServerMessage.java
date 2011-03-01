package org.anddev.andengine.examples.game.pong.adt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.anddev.andengine.examples.game.pong.util.constants.PongConstants;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.BaseServerMessage;

/**
 * @author Nicolas Gramlich
 * @since 02:02:12 - 01.03.2011
 */
public class UpdateScoreServerMessage extends BaseServerMessage implements PongConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	public final int mPaddleID;
	public final int mScore;

	// ===========================================================
	// Constructors
	// ===========================================================

	public UpdateScoreServerMessage(final int pPaddleID, final int pScore) {
		this.mPaddleID = pPaddleID;
		this.mScore = pScore;
	}

	public UpdateScoreServerMessage(final DataInputStream pDataInputStream) throws IOException {
		this.mPaddleID = pDataInputStream.readInt();
		this.mScore = pDataInputStream.readInt();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public short getFlag() {
		return FLAG_MESSAGE_SERVER_UPDATE_SCORE;
	}

	@Override
	protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
		pDataOutputStream.writeInt(this.mPaddleID);
		pDataOutputStream.writeInt(this.mScore);
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