package io.github.zkhan93.sharingtext.contants;

public interface ServerState {
	public static final int STATE_ZERO=0; // waiting for client
	public static final int STATE_ONE=1; //	Connected to client
	public static final int STATE_TWO=2; //disconnected due to error while reading
	public static final int STATE_THREE=3;
	public static final int STATE_FOUR=4; // close action by user i.e., right click the tray icon
}
