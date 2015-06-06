package io.github.zkhan93.sharingtext.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class Warning extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public Warning() {
		setMinimumSize(new Dimension(360, 240));
		setIconImage(Toolkit
				.getDefaultToolkit()
				.getImage(
						Warning.class
								.getResource("/io/github/zkhan93/sharingtext/images/icon.png")));
		setTitle("Error");
		setBounds(100, 100, 369, 241);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, BorderLayout.CENTER);
			{
				JEditorPane txtrCannotStartSharing = new JEditorPane();
				scrollPane.setViewportView(txtrCannotStartSharing);
				txtrCannotStartSharing.setEditable(false);
				txtrCannotStartSharing
						.setText("Cannot start Sharing Text. Might be already running or the port address is not free, try closing the port you assigned to sharing text (2345 is default port)");
			}
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
				{
					JButton btnChangePort = new JButton("Change Port");
					btnChangePort.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							try {

								PortInputFrame dialog = new PortInputFrame(null);
								dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
								dialog.setVisible(true);
							} catch (Exception ee) {
								ee.printStackTrace();
							}
						}
					});
					buttonPane.add(btnChangePort);
				}
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}

	private void close() {
		this.dispose();
	}

	private void setActive(boolean active) {
		this.setEnabled(active);
	}

}
