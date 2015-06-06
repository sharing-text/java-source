package io.github.zkhan93.sharingtext.gui;

import io.github.zkhan93.sharingtext.AppProperties;
import io.github.zkhan93.sharingtext.Server;
import io.github.zkhan93.sharingtext.contants.Constants;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JTextArea txtrText;
	private Server server;
	private JLabel lblMsgState;
	private JButton btnSend;
	JLabel lblLocalIp;
	/**
	 * Launch the application.
	 * 
	 * public static void main(String[] args) { EventQueue.invokeLater(new
	 * Runnable() { public void run() { try { MainFrame frame = new MainFrame();
	 * frame.setVisible(true); } catch (Exception e) { e.printStackTrace(); } }
	 * }); }
	 */
	/**
	 * Create the frame.
	 */
	public MainFrame(Server s) {
		setMinimumSize(new Dimension(300, 350));
		server = s;
		setSize(new Dimension(300, 350));
		setPreferredSize(new Dimension(300, 350));
		setIconImage(Toolkit
				.getDefaultToolkit()
				.getImage(
						MainFrame.class
								.getResource("/io/github/zkhan93/sharingtext/images/icon.png")));
		setTitle("Sharing Text");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 302, 350);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
				JMenu mnSettings = new JMenu("Settings");
				menuBar.add(mnSettings);
				
						JMenuItem mntmChangePort = new JMenuItem("Change Port");
						mntmChangePort.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								try {
									PortInputFrame dialog = new PortInputFrame(getFrame());
									dialog.setValue(Integer.parseInt(AppProperties.get(AppProperties.KEYS.PORT)));
									dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
									dialog.setVisible(true);
								} catch (Exception ee) {
									ee.printStackTrace();
								}
							}
						});
						mnSettings.add(mntmChangePort);
						
						JMenu mnHelp = new JMenu("Help");
						menuBar.add(mnHelp);
						
						JMenuItem mntmAbout = new JMenuItem("About");
						
						mntmAbout.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								try {
									AboutDialog dialog = new AboutDialog();
									dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
									dialog.setVisible(true);
								} catch (Exception ee) {
									ee.printStackTrace();
								}
								
							}
						});
						mnHelp.add(mntmAbout);
						
						JMenuItem mntmHelp = new JMenuItem("Help");
						
						mntmHelp.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								try {
									Help dialog = new Help();
									dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
									dialog.setVisible(true);
								} catch (Exception ee) {
									ee.printStackTrace();
								}
								
							}
						});
						mnHelp.add(mntmHelp);
						
						
						
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);

		txtrText = new JTextArea();
		scrollPane.setViewportView(txtrText);
		txtrText.setLineWrap(true);
		txtrText.setWrapStyleWord(true);
		txtrText.setText("Text Here");

		btnSend = new JButton("Send");
		btnSend.setEnabled(false);
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// send action here
				server.sendData(getText());
			}
		});
		contentPane.add(btnSend, BorderLayout.SOUTH);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));

		lblMsgState = new JLabel("Client not Connected..");
		panel.add(lblMsgState, BorderLayout.WEST);

		lblLocalIp = new JLabel("Server  ");
		lblLocalIp.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblLocalIp, BorderLayout.SOUTH);
	}

	public void appendText(String txt) {
		txtrText.append(txt);
		txtrText.setCaretPosition(txtrText.getText().length());
	}

	public String getText() {
		String msg = txtrText.getText();
		txtrText.setText("");
		return msg;
	}
	private MainFrame getFrame(){
		return this;
	}
	public void clearText() {
		txtrText.setText("");
	}

	public void updateMsgState(String msg) {
		lblMsgState.setText(msg);
		lblMsgState.repaint();
	}

	public void disableSend() {
		btnSend.setEnabled(false);
	}

	public void enableSend() {
		btnSend.setEnabled(true);
	}
	public void updateIP(){
		try {
			
			lblLocalIp.setText(InetAddress.getLocalHost().getHostAddress()+Constants.COLON+AppProperties.get(AppProperties.KEYS.PORT));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
