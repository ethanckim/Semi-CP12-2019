package ca.hapke.net.udp.ui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ca.hapke.gyro.ArcadeButtonUpdater;
import ca.hapke.gyro.GyroDataUpdater;
import ca.hapke.gyro.JoystickDataUpdater;
import ca.hapke.net.udp.data.DataTransmit;

/**
 * 
 * @author Mr. Hapke
 */
public class FrmUdpSender extends AccelGyroFrame {

	private static final long serialVersionUID = 1432629860045856616L;
	// private JTextField txtDestIp;
	private JTextField txtPort;
	private JLabel label;
	private JLabel lblPort;
	private JTextField txtInput;
	protected int i = 0;
	private JButton btnStartStop;

	private static final String _10_50 = "10.50.";
	private JTextField txtIp;
	private JComboBox<String> cmbHost;
	private JRadioButton radIp;
	private JRadioButton radHostname;
	private int destPort = 8002;
	private DataTransmit transmitter = new DataTransmit(cluster);
	private GyroDataUpdater gdu;
	private JoystickDataUpdater jdu;
	protected ArcadeButtonUpdater abu;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					FrmUdpSender frame = new FrmUdpSender();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FrmUdpSender() {
		super(109, 166, 810, 500);
		addElements(109, 166);
		radHostname = new JRadioButton("Hostname");
		radHostname.setHorizontalAlignment(SwingConstants.LEFT);
		radHostname.setBounds(6, 7, 109, 23);
		contentPane.add(radHostname);

		radIp = new JRadioButton("IP");
		radIp.setSelected(true);
		radIp.setBounds(6, 33, 109, 23);
		contentPane.add(radIp);

		txtIp = new JTextField();
		txtIp.setText(_10_50);
		txtIp.setBounds(121, 34, 127, 20);
		contentPane.add(txtIp);
		txtIp.setColumns(10);

		cmbHost = new JComboBox<String>();
		cmbHost.setEditable(true);
		List<String> hosts = new ArrayList<>();
		for (int i = 1; i <= 32; i++) {
			hosts.add("SEMI-R123-W0" + (i < 10 ? "0" : "") + i);
		}
		hosts.add("SEMI-SURF-W006");
		cmbHost.setModel(new DefaultComboBoxModel(hosts.toArray()));
		cmbHost.setBounds(121, 8, 127, 20);
		contentPane.add(cmbHost);

		ButtonGroup group = new ButtonGroup();
		group.add(radIp);
		group.add(radHostname);

		ChangeListener ipHostListener = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				updateIpHostUi();
			}

		};
		radIp.addChangeListener(ipHostListener);
		radHostname.addChangeListener(ipHostListener);

		updateIpHostUi();

		txtPort = new JTextField();
		txtPort.setText("" + destPort);
		txtPort.setBounds(319, 36, 86, 20);
		contentPane.add(txtPort);
		txtPort.setColumns(10);

		label = new JLabel(":");
		label.setBounds(309, 39, 16, 14);
		contentPane.add(label);

		lblPort = new JLabel("Port");
		lblPort.setBounds(319, 11, 86, 14);
		contentPane.add(lblPort);

		txtInput = new JTextField();
		txtInput.setBounds(20, 109, 294, 20);
		contentPane.add(txtInput);
		txtInput.setColumns(10);

		btnStartStop = new JButton("Start");
		btnStartStop.addActionListener(new StartStopListener());
		btnStartStop.setBounds(478, 20, 100, 31);
		contentPane.add(btnStartStop);

		JButton btnStartButtons = new JButton("Start Buttons");
		btnStartButtons.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				abu = new ArcadeButtonUpdater(cluster);
//				gdu.start();

			}
		});
		btnStartButtons.setBounds(478, 60, 140, 31);
		contentPane.add(btnStartButtons);

		JButton btnStartGyro = new JButton("Start Gyro");
		btnStartGyro.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				gdu = new GyroDataUpdater(cluster);
//				gdu.start();

			}
		});
		btnStartGyro.setBounds(585, 20, 140, 31);
		contentPane.add(btnStartGyro);

		JButton btnStartJoystick = new JButton("Start Joystick");
		btnStartJoystick.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				jdu = new JoystickDataUpdater(cluster);
				jdu.start();
			}
		});
		btnStartJoystick.setBounds(585, 60, 140, 31);
		contentPane.add(btnStartJoystick);

	}

	private void updateIpHostUi() {
		boolean ip = radIp.isSelected();
		cmbHost.setEnabled(!ip);
		txtIp.setEnabled(ip);
	}

	public String getIp() {
		if (radIp.isSelected())
			return txtIp.getText();
		else
			return cmbHost.getSelectedItem().toString();
	}

	private class StartStopListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

//			MdsToUdpAdapter adapter = (MdsToUdpAdapter) sender;
			if (btnStartStop.getText().equalsIgnoreCase("Start")) {
				String target = getIp();
				if (_10_50.equals(target)) {
					JOptionPane.showMessageDialog(rootPane, "Make sure you select a target first");
					return;
				}
				// updatingThread = new UpdatingThread();
				// updatingThread.start();
				transmitter.setTarget(getIp(), Integer.parseInt(txtPort.getText()));
				btnStartStop.setText("Stop");
			} else {
				// updatingThread.kill();
//				adapter.disableUdp();
				transmitter.stop();
				btnStartStop.setText("Start");
				if (gdu != null)
					gdu.stop();
				if (jdu != null)
					jdu.kill();

			}
		}
	}

}
