package ca.hapke.net.udp.ui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import ca.hapke.net.udp.IUdpServerListener;
import ca.hapke.net.udp.ReceiveMode;
import ca.hapke.net.udp.UdpServerThread;

/**
 * @author Mr. Hapke
 */
public class FrmUdpReceiver extends AccelGyroFrame {

	private static final long serialVersionUID = -3471270600310979442L;
	private JTextField txtPort;
	private ReceiveMode mode = ReceiveMode.Stopped;
	private JButton btnStartStop;
	protected UdpServerThread udpReceiver;
	protected Queue<String> messages = new ConcurrentLinkedQueue<>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					AccelGyroFrame frame = new FrmUdpReceiver();
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
	public FrmUdpReceiver() {
		super(109, 109, 850, 820);
		addElements(109, 109);
		setTitle("UDP Receiver");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLabel lblInputPort = new JLabel("Input Port:");
		lblInputPort.setBounds(22, 24, 134, 29);
		contentPane.add(lblInputPort);

		txtPort = new JTextField();
		txtPort.setText("8002");
		txtPort.setBounds(145, 21, 206, 35);
		contentPane.add(txtPort);
		txtPort.setColumns(10);

		btnStartStop = new JButton("Start");
		btnStartStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (mode == ReceiveMode.Stopped) {

					udpReceiver = new UdpServerThread(Integer.parseInt(txtPort.getText()), cluster);
					udpReceiver.add(new IUdpServerListener() {
						@Override
						public void serverOnline() {
							btnStartStop.setText("Stop");
							mode = ReceiveMode.Running;
						}

						@Override
						public void serverOffline() {
							btnStartStop.setText("Start");
							mode = ReceiveMode.Stopped;
						}

						@Override
						public void accelGyroUpdated() {
						}

						@Override
						public void sentenceReceived(String sentence) {
							messages.add(sentence);
						}

						@Override
						public void serverAbort(String msg) {
//							messages.add("ABORT:" + msg);
							System.err.println("ABORT:" + msg);
						}
					});
					udpReceiver.start();

				} else {
					if (udpReceiver != null)
						udpReceiver.kill();
				}
			}
		});
		btnStartStop.setBounds(361, 20, 155, 37);
		contentPane.add(btnStartStop);

		JButton btnBaseline = new JButton("Baseline");
		btnBaseline.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pnlCube.rebaseline();
			}
		});
		btnBaseline.setBounds(581, 206, 89, 23);
		contentPane.add(btnBaseline);
	}

	@Override
	public void dispose() {
		super.dispose();
		if (udpReceiver != null)
			udpReceiver.kill();
	}

}
