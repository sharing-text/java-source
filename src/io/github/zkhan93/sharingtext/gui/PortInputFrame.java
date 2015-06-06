package io.github.zkhan93.sharingtext.gui;

import io.github.zkhan93.sharingtext.AppProperties;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

public class PortInputFrame extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JSpinner textField;
	private MainFrame mFrame;

	/**
	 * Create the dialog.
	 */
	public PortInputFrame(MainFrame mfra) {
		mFrame = mfra;
		setResizable(false);
		setIconImage(Toolkit
				.getDefaultToolkit()
				.getImage(
						PortInputFrame.class
								.getResource("/io/github/zkhan93/sharingtext/images/icon.png")));
		setTitle("Set Port");
		setBounds(100, 100, 242, 116);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(0, 2, 0, 0));
		{
			JLabel lblPort = new JLabel("New Port Number");
			contentPanel.add(lblPort);
		}
		{
			SpinnerModel model = new SpinnerNumberModel(1025, 1025, 65536, 1);
			textField = new JSpinner(model);
			contentPanel.add(textField);
		}
		{
			JLabel lblWarning = new JLabel("");
			contentPanel.add(lblWarning);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						int new_port = (int) textField.getValue();
						if ((checkInput(new_port)) == GuiConstants.PortError.VALID) {
							// set new port
							savePort(new_port);
							// refresh label
							if (mFrame != null)
								mFrame.updateIP();
							close();
						} else {
							// show error
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						// discard changes
						close();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	// custom methods
	private int checkInput(int port) {
		try {

			if (port <= 1024) {
				return GuiConstants.PortError.INVALID;
			}

			else {
				return GuiConstants.PortError.VALID;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return GuiConstants.PortError.INVALID;
		}
	}

	private void close() {
		this.dispose();
	}

	public void setValue(int val) {
		textField.setValue(val);
	}

	private void savePort(int port) {
		AppProperties.set(AppProperties.KEYS.PORT, String.valueOf(port));
		// Constants.PORT=prefs.getInt(Constants.PREF_PORT, Constants.PORT);
	}
}
