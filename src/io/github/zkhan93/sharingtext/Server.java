package io.github.zkhan93.sharingtext;

import io.github.zkhan93.sharingtext.contants.Constants;
import io.github.zkhan93.sharingtext.contants.ServerState;
import io.github.zkhan93.sharingtext.gui.MainFrame;
import io.github.zkhan93.sharingtext.gui.Warning;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JDialog;

public class Server {

	PrintWriter output;
	InputStream in;
	BufferedReader input;
	MainFrame mFrame;
	ServerSocket ss = null;
	Socket cs = null;
	static Thread t, state_monitor;
	static int CLIENT_STATE = ServerState.WAITING_FOR_CLIENT_CONN;
	int x;
	int y;

	public void setGui() {
		try {
			mFrame = new MainFrame(this);
			mFrame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendData(String data) {
		output.println(data);
		if (output.checkError())
			disconnected();
		else
			mFrame.updateMsgState(Constants.SENT_TEXT);
	}

	public void disconnected() {
		// textarea.setEditable(false);
		mFrame.disableSend();
		mFrame.updateMsgState(Constants.CLIENT_NOT_CONNECTED);
		lookForClient();
	}

	public void lookForClient() {
		if (t != null) {
			t.interrupt();
			while (t.isAlive())
				;
		}
		t = new WaitForClient();
		t.start();
	}

	public class stateCheck extends Thread {
		public void run() {
			while (!isInterrupted()) {
				if (CLIENT_STATE == ServerState.ERROR_READING_CLIENT
						&& !t.isAlive()) {
					disconnected();
					// System.out.println("called disconnected");
				}
				if (CLIENT_STATE == ServerState.STATE_FOUR)
					break;

			}
			// System.out.println("monitor thread closed");
		}
	}

	public class WaitForClient extends Thread {
		public void run() {
			// System.out.println("I am a new client thread");
			try {
				CLIENT_STATE = ServerState.WAITING_FOR_CLIENT_CONN;
				cs = ss.accept();
				mFrame.updateMsgState(Constants.ENTER_TEXT);

				// textarea.setEditable(true);
				mFrame.enableSend();

				output = new PrintWriter(cs.getOutputStream(), true);
				in = cs.getInputStream();
				input = new BufferedReader(new InputStreamReader(in));
				// System.out.println("initialization completed");
				String msg;
				CLIENT_STATE = ServerState.CLIENT_CONNECTED;
				while ((msg = input.readLine()) != null) {

					// textarea.append(msg + Constants.NEW_LINE);
					mFrame.appendText(msg + Constants.NEW_LINE);

					mFrame.updateMsgState(Constants.RECEIVE_TEXT);
					// System.out.println("got input");

					// textarea.setCaretPosition(textarea.getText().length());

				}
				CLIENT_STATE = ServerState.ERROR_READING_CLIENT;
				// System.out.println("client Thread completed");
			} catch (Exception e) {
				// System.out.println("client Thread dead " + e);
				CLIENT_STATE = ServerState.ERROR_READING_CLIENT;
			}
		}
	}

	public void setNw() {
		try {
			int port=Integer.parseInt(AppProperties.get(AppProperties.KEYS.PORT));
			System.out.println("getting this on start"+port);
			ss = new ServerSocket(port);
			state_monitor = new stateCheck();
			state_monitor.start();
			lookForClient();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (cs != null)
					cs.close();
				if (ss != null)
					ss.close();
				mFrame.dispose();
				showWarning();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}
	}

	public void go() {
		setGui();
		setLocalIp();
		setNw();
	}

	public static void main(String args[]) {
		new Server().go();
	}

	private void setLocalIp() {
		mFrame.updateIP();
	}

	private void showWarning() {
		Warning dialog = new Warning();
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);
	}
}
