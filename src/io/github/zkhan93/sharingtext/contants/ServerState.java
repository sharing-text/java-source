package io.github.zkhan93.sharingtext.contants;

public interface ServerState {
	public static final int WAITING_FOR_CLIENT_CONN=0; // waiting for client
	public static final int CLIENT_CONNECTED=1; //	Connected to client
	public static final int ERROR_READING_CLIENT=2; //disconnected due to error while reading
	public static final int STATE_FOUR=4; // close action by user i.e., right click the tray icon
}
