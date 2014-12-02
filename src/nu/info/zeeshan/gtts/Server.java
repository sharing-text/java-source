package nu.info.zeeshan.gtts;

import java.awt.AWTException;
import java.awt.BorderLayout;
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
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Server {
	public static BufferedReader br =new BufferedReader(new InputStreamReader(System.in));
	static PrintWriter output;
	static JFrame frame;
	static JTextField textfeild;
	static JLabel label;
	static ServerSocket ss=null;
	static Socket cs=null;
	static int x;
	static int y;
	static public void setGui(){
		try {
			frame=new JFrame();
			JPanel panel=new JPanel();
			textfeild=new JTextField();
			label=new JLabel();
			updateState("Client not Connected..");
			frame.setUndecorated(true);
			frame.setType(JFrame.Type.UTILITY);
			panel.setLayout(new BorderLayout());
			panel.add(label,BorderLayout.NORTH);
			panel.add(textfeild,BorderLayout.SOUTH);
			frame.getContentPane().add(panel);
			frame.setBounds(new Rectangle(150,40));
			KeyListener klistner=new KeyListener() {
				
				@Override
				public void keyTyped(KeyEvent e) {}
				
				@Override
				public void keyReleased(KeyEvent e) {}
				
				@Override
				public void keyPressed(KeyEvent e) {
					if(e.getKeyCode()==KeyEvent.VK_ENTER){
						sendData(textfeild.getText());
						textfeild.setText("");
						
					}
				}
			};
			textfeild.addKeyListener(klistner);
			textfeild.setEnabled(false);
			frame.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					x=e.getX();
					y=e.getY();
					
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
					frame.setLocation(e.getXOnScreen()-x, e.getYOnScreen()-y);
					
				}
			});
			frame.setVisible(true);
			
			//setting tary icon
			if(SystemTray.isSupported()){
				System.out.println("Supported");
				final SystemTray tray=SystemTray.getSystemTray();
				String path="D:\\gttserver\\icon.png";
				final TrayIcon ticon=new TrayIcon(new ImageIcon(path).getImage(),"Get The Text Server");
				ticon.setImageAutoSize(true);
				MouseAdapter madapter=new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						frame.dispose();
						tray.remove(ticon);
						try {
							if(cs!=null)
								cs.close();
							if(ss!=null)
								ss.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				};
				ticon.addMouseListener(madapter);
				tray.add(ticon);
			}
			System.out.println("Done gui");
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
	}
	public static void sendData(String data){
		output.println(data);
		if(output.checkError())
			disconnected();
		else
			updateState("Sent Successfully");
	}
	public static void updateState(String msg){
		label.setText(msg);
		frame.repaint();
	}
	public static void disconnected(){
		textfeild.setEnabled(false);
		updateState("Client not Connected..");
		lookForClient();
	}
	public static void lookForClient(){
		Thread t=new Thread(new WaitForClient());
		t.start();
	}
	public static class WaitForClient implements Runnable{
		
		@Override
		public void run() {
			try{
				cs=ss.accept();
				updateState("Enter Text");
				textfeild.setEnabled(true);
				output=new PrintWriter(cs.getOutputStream(),true);
				}catch(Exception e){
					e.printStackTrace();
				}
		}
	}
	public static void main(String args[]){
		setGui();
		try{
			ss=new ServerSocket(23456);
			lookForClient();
		}catch(Exception e){
			e.printStackTrace();
			try {
				if(cs!=null)
					cs.close();
				if(ss!=null)
					ss.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		}
	}
}
