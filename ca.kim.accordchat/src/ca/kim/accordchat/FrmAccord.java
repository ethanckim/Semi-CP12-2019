package ca.kim.accordchat;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class FrmAccord extends JFrame {

	private JLayeredPane contentPane;
	private JTextField txtServerIp;
	private JTextField txtName;
	private JTextField txtMessage;
	private JTextField txtPort;
	private JTextArea txtOutput;
	private JTextArea txtUsers;
	private SocketsAndStreams sas = new SocketsAndStreams();
	private RecieveThread thread;
	private JScrollBar sbOutput;
	private JButton btnToBottom;
	private JButton btnConnectStatus;
	private ConnectStatus cs = ConnectStatus.DISCONNECTED;
	private boolean newMessages = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrmAccord frame = new FrmAccord();
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
	public FrmAccord() {
		setTitle("Accord Chat");
		setBackground(new Color(51, 51, 51));
		setForeground(new Color(255, 255, 255));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 723, 641);
		contentPane = new JLayeredPane();
		contentPane.setForeground(new Color(51, 51, 51));
		contentPane.setBackground(new Color(51, 51, 51));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblServerIp = new JLabel("Server IP:");
		lblServerIp.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblServerIp.setForeground(Color.WHITE);
		lblServerIp.setBackground(Color.WHITE);
		lblServerIp.setHorizontalAlignment(SwingConstants.RIGHT);
		lblServerIp.setBounds(10, 11, 57, 14);
		contentPane.add(lblServerIp);

		JLabel lblName = new JLabel("Name:");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblName.setForeground(Color.WHITE);
		lblName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblName.setBounds(10, 36, 57, 14);
		contentPane.add(lblName);

		txtServerIp = new JTextField();
		txtServerIp.setText("10.50.31.91");
		txtServerIp.setForeground(new Color(255, 255, 255));
		txtServerIp.setBackground(new Color(102, 102, 102));
		txtServerIp.setBounds(77, 8, 108, 20);
		contentPane.add(txtServerIp);
		txtServerIp.setColumns(10);

		txtName = new JTextField();
		txtName.setText("paperairplain");
		txtName.setForeground(new Color(255, 255, 255));
		txtName.setBackground(new Color(102, 102, 102));
		txtName.setColumns(10);
		txtName.setBounds(77, 33, 214, 20);
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
		txtMessage.setBounds(10, 564, 588, 30);
		contentPane.add(txtMessage);

		JLabel lblPort = new JLabel(":");
		lblPort.setForeground(Color.WHITE);
		lblPort.setBounds(195, 11, 46, 14);
		contentPane.add(lblPort);

		txtPort = new JTextField();
		txtPort.setText("8000");
		txtPort.setForeground(new Color(255, 255, 255));
		txtPort.setBackground(new Color(102, 102, 102));
		txtPort.setBounds(205, 8, 86, 20);
		contentPane.add(txtPort);
		txtPort.setColumns(10);

		JScrollPane sclOutput = new JScrollPane();
		sclOutput.setBounds(10, 67, 535, 486);
		contentPane.add(sclOutput);
		sbOutput = sclOutput.getVerticalScrollBar();
		sbOutput.addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
				if (newMessages && sbOutput.getMaximum() != sbOutput.getValue() + sbOutput.getVisibleAmount()) {
					btnToBottom.setEnabled(true);
					btnToBottom.setVisible(true);
				} else {
					btnToBottom.setEnabled(false);
					btnToBottom.setVisible(false);
					newMessages = false;
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

		JLabel lblUsers = new JLabel("Users");
		lblUsers.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblUsers.setForeground(Color.WHITE);
		lblUsers.setHorizontalAlignment(SwingConstants.LEFT);
		lblUsers.setBounds(560, 107, 57, 14);
		contentPane.add(lblUsers);

		JScrollPane sclUsers = new JScrollPane();
		sclUsers.setBounds(555, 132, 142, 409);
		contentPane.add(sclUsers);

		txtUsers = new JTextArea();
		txtUsers.setEditable(false);
		txtUsers.setForeground(Color.WHITE);
		txtUsers.setFont(new Font("Calibri", Font.PLAIN, 13));
		sclUsers.setViewportView(txtUsers);
		txtUsers.setBackground(new Color(102, 102, 102));
		txtUsers.setLineWrap(true);

		JButton btnDm = new JButton("Open DM");
		btnDm.setEnabled(false);
		btnDm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FrmDm dmForm = new FrmDm(FrmAccord.this, thread);
				dmForm.setVisible(true);
			}
		});
		btnDm.setBackground(new Color(192, 192, 192));
		btnDm.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnDm.setBounds(599, 70, 98, 26);
		contentPane.add(btnDm);

		btnConnectStatus = new JButton("");
		btnConnectStatus.setBackground(new Color(160, 160, 160));
		btnConnectStatus.setBounds(278, 40, 20, 20);
		contentPane.add(btnConnectStatus, 2, 0);
		btnConnectStatus.setVisible(true);
		btnConnectStatus.setEnabled(false);

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
		btnToBottom.setBounds(200, 67, 159, 23);
		contentPane.add(btnToBottom, 2, 0);
		btnToBottom.setForeground(new Color(255, 255, 255));
		btnToBottom.setBackground(new Color(51, 153, 204));
		btnToBottom.setVisible(false);
		btnToBottom.setEnabled(false);

		JButton btnJoin = new JButton("Join");
		btnJoin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (cs != ConnectStatus.CONNECTING) {
					if (cs == ConnectStatus.JOINED)
						printLog("Can not Join chat when you are already in it!", "Friendly Bot");
					else
						printLog("Please open a new Socket to join the chat.", "Friendly Bot");
					return;
				}
				boolean success = sas.sJoin(txtName.getText());
				if (success) {
					System.out.println("Successfully sent JOIN protocal");
					btnDm.setEnabled(true);
				} else {
					printLog("Failed to join the Chat.", "Friendly Bot");
				}
			}
		});
		btnJoin.setForeground(new Color(0, 0, 0));
		btnJoin.setBackground(new Color(192, 192, 192));
		btnJoin.setBounds(316, 8, 89, 23);
		contentPane.add(btnJoin);

		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}
		});
		btnSend.setForeground(new Color(255, 255, 255));
		btnSend.setBackground(new Color(255, 153, 0));
		btnSend.setBounds(608, 567, 89, 23);
		contentPane.add(btnSend);

		JButton btnPart = new JButton("Part");
		btnPart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cs != ConnectStatus.JOINED) {
					printLog("Can not part chat when you didn't join it yet!", "Friendly Bot");
					return;
				}
				boolean success = sas.sPart(txtName.getText() + " Has left the chat.");
				if (success) {
					System.out.println("Successfully sent PART protocal");
					btnDm.setEnabled(false);
				} else {
					printLog("Failed to leave the Chat.", "Friendly Bot");
				}
			}
		});
		btnPart.setForeground(new Color(0, 0, 0));
		btnPart.setBackground(new Color(192, 192, 192));
		btnPart.setBounds(316, 33, 89, 23);
		contentPane.add(btnPart);

		JButton btnOpenSocket = new JButton("Open Socket");
		btnOpenSocket.setBackground(new Color(192, 192, 192));
		btnOpenSocket.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cs != ConnectStatus.DISCONNECTED) {
					printLog("Can not Open Socket when it is already open.", "Friendly Bot");
					return;
				}

				boolean success = sas.connect(txtServerIp.getText(), Integer.parseInt(txtPort.getText()));
				if (success) {
					txtOutput.setText("");
					printLog("Success! Connected to Server " + txtServerIp.getText() + " on port " + txtPort.getText()
							+ ".", "Friendly Bot");
					thread = new RecieveThread(sas, txtOutput, sbOutput, txtUsers, txtName, btnToBottom,
							btnConnectStatus, txtMessage, FrmAccord.this);
					thread.start();
					txtServerIp.setEditable(false);
					txtPort.setEditable(false);
					cs = ConnectStatus.CONNECTING;
					btnConnectStatus.setBackground(new Color(255, 165, 0));
				} else {
					printLog("Failed to Connect to the Server.", "Friendly Bot");
				}
			}
		});
		btnOpenSocket.setBounds(575, 11, 122, 23);
		contentPane.add(btnOpenSocket);

		JButton btnCloseSocket = new JButton("Close Socket");
		btnCloseSocket.setBackground(new Color(192, 192, 192));
		btnCloseSocket.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cs == ConnectStatus.DISCONNECTED) {
					printLog("Can not Close Socket when you already closed it.", "Friendly Bot");
					return;
				} else if (cs == ConnectStatus.JOINED) {
					printLog("If you want to leave the chat, please [part] first.", "Friendly Bot");
					return;
				}

				boolean success = sas.disconnect();
				if (success) {
					thread.setKillThread(true);
					txtServerIp.setEditable(true);
					txtPort.setEditable(true);
					txtName.setEditable(true);
					txtMessage.setText("");
					txtUsers.setText("");
					txtOutput.setText("");
					printLog("Successfully disconnected from server.", "Friendly Bot");
					cs = ConnectStatus.DISCONNECTED;
					btnConnectStatus.setBackground(new Color(160, 160, 160));
				} else {
					printLog("Failed to close socket.", "Friendly Bot");
				}

			}
		});
		btnCloseSocket.setBounds(575, 36, 122, 23);
		contentPane.add(btnCloseSocket);

		BufferedImage logo;
		try {
			//TODO Change location of oofCord.png image file.
			logo = ImageIO.read(new File("H:\\workspace\\ca.kim.accordchat\\src\\ca\\kim\\accordchat\\oofCord.png"));
			JLabel picLabel = new JLabel(new ImageIcon(logo.getScaledInstance(150, 50, Image.SCALE_FAST)));
			picLabel.setBounds(415, 8, 140, 50);
			contentPane.add(picLabel, 3);
		} catch (IOException e1) {
		}

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

	private void sendMessage() {
		boolean success = sas.sSpeak(txtMessage.getText());
		txtMessage.setText("");
		if (!success) {
			printLog("Failed to send Message.", "Friendly Bot");
		}
	}
	
	public void dmAccept(String user, String dmIp, String dmPort) {
		FrmDm dmForm = new FrmDm(this, thread);
		dmForm.setVisible(true);
		dmForm.dmAccept(user, dmIp, dmPort);
	}	

	public ConnectStatus getConnectStatus() {
		return cs;
	}

	public void setConnectStatus(ConnectStatus connectStatus) {
		cs = connectStatus;
	}

	public boolean isNewMessages() {
		return newMessages;
	}

	public void setNewMessages(boolean newmsg) {
		newMessages = newmsg;
	}

	public SocketsAndStreams getSas() {
		return sas;
	}

	
	public String getyourUsername() {
		return txtName.getText();
	}

}
