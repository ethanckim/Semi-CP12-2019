package ca.kim.accordchat;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class FrmDm extends JFrame {

	private JLayeredPane contentPane;
	private JTextField txtTheirUsername;
	private JTextField txtName;
	private JTextField txtMessage;
	private JTextField txtPort;
	private JTextArea txtOutput;
	private JScrollBar sbOutput;
	private JButton btnToBottom;
	private JButton btnConnectStatus;
	private ConnectStatus cs = ConnectStatus.DISCONNECTED;
	private DmAddress dmAddress = new DmAddress();
	private SocketsAndStreams sas;
	private DmSocketsAndStreams dmSas;
	private DmThread dmThread;
	private JButton btnAcceptReq;
	private JButton btnSendReq;
	private boolean newDm = false;

	/**
	 * Create the frame.
	 */
	public FrmDm(FrmAccord fa, RecieveThread recieveThread) {
		sas = fa.getSas();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Accord Chat DM");
		setBackground(new Color(51, 51, 51));
		setForeground(new Color(255, 255, 255));
		setBounds(100, 100, 723, 581);
		contentPane = new JLayeredPane();
		contentPane.setForeground(new Color(51, 51, 51));
		contentPane.setBackground(new Color(51, 51, 51));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblPort = new JLabel("Port: ");
		lblPort.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPort.setForeground(Color.WHITE);
		lblPort.setHorizontalAlignment(SwingConstants.LEFT);
		lblPort.setBounds(10, 160, 41, 14);
		contentPane.add(lblPort);

		txtTheirUsername = new JTextField();
		txtTheirUsername.setForeground(new Color(255, 255, 255));
		txtTheirUsername.setBackground(new Color(102, 102, 102));
		txtTheirUsername.setBounds(10, 110, 142, 20);
		contentPane.add(txtTheirUsername);
		txtTheirUsername.setColumns(10);

		txtName = new JTextField();
		try {
			txtName.setText(fa.getyourUsername());
		} catch (NullPointerException e) {
			System.out.println("WARNING: You need to load FrmAccord First!");
			txtName.setText("");
		}
		txtName.setForeground(new Color(255, 255, 255));
		txtName.setBackground(new Color(102, 102, 102));
		txtName.setColumns(10);
		txtName.setBounds(10, 36, 142, 20);
		contentPane.add(txtName);

		txtMessage = new JTextField();
		txtMessage.setEditable(false);
		txtMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}
		});
		txtMessage.setForeground(Color.WHITE);
		txtMessage.setHorizontalAlignment(SwingConstants.LEFT);
		txtMessage.setFont(new Font("Calibri", Font.PLAIN, 12));
		txtMessage.setBackground(new Color(102, 102, 102));
		txtMessage.setColumns(10);
		txtMessage.setBounds(10, 497, 588, 30);
		contentPane.add(txtMessage);

		txtPort = new JTextField();
		txtPort.setForeground(new Color(255, 255, 255));
		txtPort.setBackground(new Color(102, 102, 102));
		txtPort.setBounds(61, 158, 91, 20);
		contentPane.add(txtPort);
		txtPort.setColumns(10);

		JScrollPane sclOutput = new JScrollPane();
		sclOutput.setBounds(162, 11, 535, 475);
		contentPane.add(sclOutput);
		sbOutput = sclOutput.getVerticalScrollBar();
		sbOutput.addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
				if (newDm && sbOutput.getMaximum() != sbOutput.getValue() + sbOutput.getVisibleAmount()) {
					btnToBottom.setEnabled(true);
					btnToBottom.setVisible(true);
				} else {
					btnToBottom.setEnabled(false);
					btnToBottom.setVisible(false);
					newDm = false;
				}
			}
		});

		txtOutput = new JTextArea();
		txtOutput.setEditable(false);
		txtOutput.setFont(new Font("Calibri", Font.PLAIN, 13));
		sclOutput.setViewportView(txtOutput);
		txtOutput.setBackground(new Color(102, 102, 102));
		txtOutput.setForeground(new Color(255, 255, 255));
		txtOutput.setLineWrap(true);

		btnToBottom = new JButton("View New Messages");
		btnToBottom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						EventQueue.invokeLater(new Runnable() {
							public void run() {
								sbOutput.setValue(sbOutput.getMaximum());
								btnToBottom.setEnabled(false);
								btnToBottom.setVisible(false);
							}
						});
					}
				});
			}
		});
		btnToBottom.setBounds(350, 11, 159, 23);
		contentPane.add(btnToBottom, 2, 0);
		btnToBottom.setForeground(new Color(255, 255, 255));
		btnToBottom.setBackground(new Color(51, 153, 204));
		btnToBottom.setVisible(false);
		btnToBottom.setEnabled(false);

		btnConnectStatus = new JButton("");
		btnConnectStatus.setBackground(new Color(160, 160, 160));
		btnConnectStatus.setBounds(126, 294, 20, 20);
		contentPane.add(btnConnectStatus, 2, 0);
		btnConnectStatus.setVisible(true);
		btnConnectStatus.setEnabled(false);

		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}
		});
		btnSend.setForeground(new Color(255, 255, 255));
		btnSend.setBackground(new Color(255, 153, 0));
		btnSend.setBounds(608, 500, 89, 23);
		contentPane.add(btnSend);

		btnSendReq = new JButton("Send Request");
		btnSendReq.setBackground(new Color(192, 192, 192));
		btnSendReq.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dmAddress.setTheirUsername(txtTheirUsername.getText());
				dmAddress.setMyUsername(txtName.getText());
				setMyPort();
				boolean success = sas.sDmRequest(dmAddress.getTheirUsername(), dmAddress.getMyPort());
				if (success) {
					System.out.println("Successfully sent REQS protocal");
					printLog("Successfully sent the Request. Please wait for the user's response.",
							dmAddress.getMyUsername());
					btnConnectStatus.setBackground(new Color(255, 165, 0));
					recieveThread.setFrmdm(FrmDm.this);
				} else {
					printLog("Failed to Send the DM Request. Please Check your connection and try again.",
							"Friendly Bot");
				}
			}
		});
		btnSendReq.setBounds(20, 243, 122, 23);
		contentPane.add(btnSendReq);

		JLabel lblUsername = new JLabel("Your Username: ");
		lblUsername.setHorizontalAlignment(SwingConstants.LEFT);
		lblUsername.setForeground(Color.WHITE);
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblUsername.setBackground(Color.WHITE);
		lblUsername.setBounds(10, 11, 116, 14);
		contentPane.add(lblUsername);

		JTextArea txtToWhom = new JTextArea();
		txtToWhom.setBackground(new Color(51, 51, 51));
		txtToWhom.setForeground(new Color(255, 255, 255));
		txtToWhom.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtToWhom.setText("To Whom\r\n(Provide Username):\r\n");
		txtToWhom.setBounds(10, 67, 132, 32);
		contentPane.add(txtToWhom);

		JTextArea txtrifLeftBlank = new JTextArea();
		txtrifLeftBlank.setLineWrap(true);
		txtrifLeftBlank.setText("* If left blank or is\r\ninvalid, will use a\r\nrandomized port.");
		txtrifLeftBlank.setForeground(Color.WHITE);
		txtrifLeftBlank.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtrifLeftBlank.setBackground(new Color(51, 51, 51));
		txtrifLeftBlank.setBounds(10, 185, 142, 55);
		contentPane.add(txtrifLeftBlank);

		JLabel lblConnectionStatus = new JLabel("Connection Status:");
		lblConnectionStatus.setHorizontalAlignment(SwingConstants.LEFT);
		lblConnectionStatus.setForeground(Color.WHITE);
		lblConnectionStatus.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblConnectionStatus.setBounds(10, 294, 116, 14);
		contentPane.add(lblConnectionStatus);

		btnAcceptReq = new JButton("Accept Request");
		btnAcceptReq.setBackground(new Color(192, 192, 192));
		btnAcceptReq.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setMyPort();
				boolean success = sas.sDmAccept(dmAddress.getTheirUsername(), dmAddress.getMyPort());
				if (success) {
					System.out.println("Successfully sent REQS protocal");
					dmSas = new DmSocketsAndStreams();
					dmSas.openDm(FrmDm.this);
					dmThread = new DmThread(dmSas, txtOutput, sbOutput, btnSendReq, FrmDm.this);
					dmThread.start();
					btnAcceptReq.setEnabled(false);
					btnAcceptReq.setBackground(new Color(192, 192, 192));
					txtMessage.setEditable(true);
					printLog("Successfully Accepted the Request. Now start Talking!", dmAddress.getMyUsername());
					btnConnectStatus.setBackground(new Color(127, 255, 0));
				} else {
					JOptionPane.showMessageDialog(null,
							"Failed to send the DM accept. Please check your connection and try again.");

				}
			}
		});
		btnAcceptReq.setEnabled(false);
		btnAcceptReq.setBounds(20, 456, 122, 23);
		contentPane.add(btnAcceptReq);

	}

	private void printLog(String text, String user) {
		boolean end = sbOutput.getMaximum() == sbOutput.getValue() + sbOutput.getVisibleAmount();
		String temp = txtOutput.getText();

		SimpleDateFormat format = new SimpleDateFormat("[MM-dd, hh:mm:ss a]");
		String dateString = format.format(new Date());

		txtOutput.setText(temp + dateString + " <" + user + "> " + "\r\n" + " " + text + "\r\n" + "\r\n");

		if (end) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							sbOutput.setValue(sbOutput.getMaximum());
							btnToBottom.setEnabled(false);
							btnToBottom.setVisible(false);
						}
					});
				}
			});
		}
	}

	public void dmAccept(String user, String dmIp, String dmPort) {
		printLog("You have recieved a DM request by " + user
				+ ". Please put in a port number and a Username to use and press [Accept Request].", user);
		txtTheirUsername.setText(user);
		txtTheirUsername.setEditable(false);
		btnAcceptReq.setEnabled(true);
		btnAcceptReq.setBackground(new Color(255, 182, 193));
		dmAddress.setTheirUsername(user);
		dmAddress.setTheirPort(dmPort);
		dmAddress.setTheirIp(dmIp);
		dmAddress.setMyUsername(txtName.getText());
		txtName.setEditable(false);
		btnConnectStatus.setBackground(new Color(255, 165, 0));
	}

	public void dmConfirmed(String cfmUser, String cfmIp, String cfmPort) {
		printLog(cfmUser + " accepted the dm request. Now start talking!", cfmUser);
		dmAddress.setTheirUsername(cfmUser);
		dmAddress.setTheirPort(cfmPort);
		dmAddress.setTheirIp(cfmIp);
		dmAddress.setMyUsername(txtName.getText());
		txtName.setEditable(false);
		dmSas = new DmSocketsAndStreams();
		dmSas.openDm(FrmDm.this);
		dmThread = new DmThread(dmSas, txtOutput, sbOutput, btnSendReq, FrmDm.this);
		dmThread.start();
		txtMessage.setEditable(true);
		btnConnectStatus.setBackground(new Color(127, 255, 0));
	}

	private void sendMessage() {
		boolean success = dmSas.sendDm(txtMessage.getText(), FrmDm.this);
		if (success) {
			printLog(txtMessage.getText(), dmAddress.getMyUsername());
			txtMessage.setText("");
		} else 
			printLog("Failed to send Message.", "Friendly Bot");
	}

	public ConnectStatus getConnectStatus() {
		return cs;
	}

	public void setConnectStatus(ConnectStatus connectStatus) {
		cs = connectStatus;
	}

	public DmAddress getDmAddress() {
		return dmAddress;
	}
	
	public boolean isNewDm() {
		return newDm;
	}

	public void setNewDm(boolean setNewDm) {
		newDm = setNewDm;
	}


	private void setMyPort() {
		if (txtPort.getText().length() == 4)
			dmAddress.setMyPort(txtPort.getText());
		else {
			int randomPort = (int) (Math.random() * 10000);
			txtPort.setText(Integer.toString(randomPort));
			dmAddress.setMyPort(Integer.toString(randomPort));
		}
	}

	public void startDmThread() {
		dmThread = new DmThread(dmSas, txtOutput, sbOutput, btnConnectStatus, FrmDm.this);
		dmThread.start();
	}

}
