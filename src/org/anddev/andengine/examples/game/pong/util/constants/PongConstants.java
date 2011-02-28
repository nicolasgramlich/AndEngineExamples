package org.anddev.andengine.examples.game.pong.util.constants;

/**
 * @author Nicolas Gramlich
 * @since 19:49:20 - 28.02.2011
 */
public interface PongConstants {
	// ===========================================================
	// Final Fields
	// ===========================================================

	public static final int GAME_WIDTH = 480;
	public static final int GAME_WIDTH_HALF = GAME_WIDTH / 2;
	public static final int GAME_HEIGHT = 720;
	public static final int GAME_HEIGHT_HALF = GAME_HEIGHT / 2;

	public static final int PADDLE_WIDTH = 80;
	public static final int PADDLE_WIDTH_HALF = PADDLE_WIDTH / 2;
	public static final int PADDLE_HEIGHT = 20;
	public static final int PADDLE_HEIGHT_HALF = PADDLE_HEIGHT / 2;

	public static final int BALL_WIDTH = 10;
	public static final int BALL_WIDTH_HALF = BALL_WIDTH / 2;
	public static final int BALL_HEIGHT = 10;
	public static final int BALL_HEIGHT_HALF = BALL_HEIGHT / 2;

	public static final int SERVER_PORT = 4444;

	/* Server --> Client */
	public static final short FLAG_MESSAGE_SERVER_SET_PADDLEID = 1;
	public static final short FLAG_MESSAGE_SERVER_UPDATE_BALL = FLAG_MESSAGE_SERVER_SET_PADDLEID + 1;
	public static final short FLAG_MESSAGE_SERVER_UPDATE_PADDLE = FLAG_MESSAGE_SERVER_UPDATE_BALL + 1;

	/* Client --> Server */
	public static final short FLAG_MESSAGE_CLIENT_MOVE_PADDLE = 1;

	// ===========================================================
	// Methods
	// ===========================================================
}
