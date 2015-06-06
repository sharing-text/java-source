package io.github.zkhan93.sharingtext.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Window.Type;

public class AboutDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();


	/**
	 * Create the dialog.
	 */
	public AboutDialog() {
		setType(Type.POPUP);
		setTitle("About");
		setResizable(false);
		setBounds(100, 100, 440, 340);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			ImageIcon image=new ImageIcon(Toolkit
					.getDefaultToolkit()
					.getImage(
							PortInputFrame.class
									.getResource("/io/github/zkhan93/sharingtext/images/icon.png")));
			JLabel lblNewLabel = new JLabel(image);
			lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblNewLabel, BorderLayout.CENTER);
		}
		{
			JLabel lblNewLabel_1 = new JLabel("<html><center><strong>hi!!</strong></center></br> this is a small project of mine </br>\r\nvisit more projects on <a href=\"http://github.com/zkhan93\" title=\"Github link\"> http://github.com/zkhan93</a></html>");
			lblNewLabel_1.setFont(new Font("Roboto", Font.PLAIN, 15));
			lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblNewLabel_1, BorderLayout.SOUTH);
		}
		{
			JLabel lblSharingText = new JLabel("Sharing Text");
			lblSharingText.setFont(new Font("Roboto", Font.PLAIN, 47));
			lblSharingText.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblSharingText, BorderLayout.NORTH);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						close();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}
	private void close(){
		this.dispose();
	}
}
