package io.github.zkhan93.sharingtext;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Server {
	public static BufferedReader br = new BufferedReader(new InputStreamReader(
			System.in));
	PrintWriter output;
	InputStream in;
	BufferedReader input;
	JFrame frame;
	JTextArea textarea;
	
	JLabel label;
	ServerSocket ss = null;
	Socket cs = null;
	static Thread t, state_monitor;
	static int CLIENT_STATE = 0;
	int x;
	int y;

	public void setGui() {
		try {
			
			frame = new JFrame();
			JScrollPane panel = new JScrollPane();
			frame.setUndecorated(true);
			frame.getRootPane().setBorder(BorderFactory.createMatteBorder(10, 4, 4, 4, new ImageIcon()));
			frame.setResizable(true);
			textarea = new JTextArea();
			textarea.setBackground(new Color(77,182,166));
			textarea.setForeground(Color.white);
			textarea.setFont(new Font(Font.SANS_SERIF ,Font.BOLD,12));
			textarea.setCaretColor(Color.WHITE);
			label = new JLabel();
			updateState("Client not Connected..");
			
			frame.setType(JFrame.Type.UTILITY);
			// panel.setLayout(new BorderLayout());
			panel.setColumnHeaderView(label);
			panel.setViewportView(textarea);
			frame.getContentPane().add(panel);
			frame.setBounds(new Rectangle(350, 350));
			KeyListener klistner = new KeyListener() {

				@Override
				public void keyTyped(KeyEvent e) {
				}

				@Override
				public void keyReleased(KeyEvent e) {
				}

				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER ) {
						if(e.isShiftDown())
							textarea.append("\n");
						else if(textarea.isEditable()
								&& textarea.getText().toString().trim()
										.length() > 0) {
							sendData(textarea.getText());
							textarea.setText("");
							e.consume();
						}
						
					}

				}
			};
			textarea.addKeyListener(klistner);
			textarea.setEditable(false);
			frame.addMouseListener(new MouseListener() {

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mousePressed(MouseEvent e) {
					x = e.getX();
					y = e.getY();

				}

				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub

				}
			});
			frame.addMouseMotionListener(new MouseMotionListener() {

				@Override
				public void mouseMoved(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseDragged(MouseEvent e) {
					frame.setLocation(e.getXOnScreen() - x, e.getYOnScreen() - y);

				}
			});
			frame.setVisible(true);

			// setting tary icon
			if (SystemTray.isSupported()) {
				//System.out.println("Supported");
				final SystemTray tray = SystemTray.getSystemTray();
				// new ImageIcon("../res/img/icon.png").getImage();
				final TrayIcon ticon = new TrayIcon(new ImageIcon(
						"res/img/icon.png").getImage(),
						"Server running at "
								+ InetAddress.getLocalHost().getHostAddress()
								+ ":2345");
				ticon.setImageAutoSize(true);
				MouseAdapter madapter = new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						switch (e.getButton()) {
						case MouseEvent.BUTTON1:
							if(e.getClickCount()==2){
								frame.setVisible(!frame.isVisible());
							}
							else{
								frame.toFront();
								frame.repaint();
							}
							
							break;
						case MouseEvent.BUTTON3:
							frame.dispose();
							tray.remove(ticon);
							try {
								CLIENT_STATE = 4;
								if (cs != null)
									cs.close();
								if (ss != null)
									ss.close();
								if (t != null && t.isAlive())
									t.interrupt();
								if (state_monitor != null
										&& state_monitor.isAlive())
									state_monitor.interrupt();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							break;
						}
					}
				};
				ticon.addMouseListener(madapter);
				tray.add(ticon);
			}
			//System.out.println("Done gui");
		} catch (AWTException | IOException e1) {
			e1.printStackTrace();
		}
	}

	public void sendData(String data) {
		output.println(data);
		if (output.checkError())
			disconnected();
		else
			updateState("Sent Successfully");
	}

	public void updateState(String msg) {
		label.setText(msg);
		frame.repaint();
	}

	public void disconnected() {
		textarea.setEditable(false);
		updateState("Client not Connected..");
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
				if (CLIENT_STATE == 2 && !t.isAlive()) {
					disconnected();
					//System.out.println("called disconnected");
				}
				if (CLIENT_STATE == 4)
					break;

			}
			//System.out.println("monitor thread closed");
		}
	}

	public class WaitForClient extends Thread {
		public void run() {
			//System.out.println("I am a new client thread");
			try {
				CLIENT_STATE = 0;
				cs = ss.accept();
				updateState("Enter Text");
				textarea.setEditable(true);
				output = new PrintWriter(cs.getOutputStream(), true);
				in = cs.getInputStream();
				input = new BufferedReader(new InputStreamReader(in));
				//System.out.println("initialization completed");
				String msg;
				CLIENT_STATE = 1;
				while ((msg = input.readLine()) != null) {
					textarea.append(msg + "\n");
					updateState("Text Received");
					//System.out.println("got input");
					textarea.setCaretPosition(textarea.getText().length());
				}
				CLIENT_STATE = 2;
				//System.out.println("client Thread completed");
			} catch (Exception e) {
				//System.out.println("client Thread dead " + e);

			}
		}
	}

	public void go() {
		setGui();
		try {
			ss = new ServerSocket(2345);
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
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}
	}

	public static void main(String args[]) {
		new Server().go();

	}
}
