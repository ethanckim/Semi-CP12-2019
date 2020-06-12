package ca.kim.accordchat;

import java.awt.Color;
import java.awt.EventQueue;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

public class RecieveThread extends Thread {

	private SocketsAndStreams sas;
	private boolean killThread = false;
	private JTextArea txtOutput;
	private JTextArea txtUsers;
	private JTextField txtName;
	private JScrollBar sbOutput;
	private JButton btnToBottom;
	private JButton btnConnectStatus;
	private JTextComponent txtMessage;
	private FrmAccord frmAccord;
	private FrmDm frmDm;

	public RecieveThread(SocketsAndStreams sas, JTextArea txtOutput, JScrollBar sbOutput, JTextArea txtUsers,
			JTextField txtName, JButton btnToBottom, JButton btnConnectStatus, JTextField txtMessage, FrmAccord frmAccord) {
		this.sas = sas;
		this.txtOutput = txtOutput;
		this.sbOutput = sbOutput;
		this.txtUsers = txtUsers;
		this.txtName = txtName;
		this.btnToBottom = btnToBottom;
		this.btnConnectStatus = btnConnectStatus;
		this.txtMessage = txtMessage;
		this.frmAccord = frmAccord;
	}

	@Override
	public void run() {
		while (!killThread) {
			String recievedProtocal = sas.receiveRead();
			if (recievedProtocal != null && recievedProtocal.length() >= 4) {
				String msg = recievedProtocal.substring(6);
				switch (recievedProtocal.substring(0, 4)) {
				case "WELC":
					printLog("Joined the chat as " + txtName.getText(), "Friendly Bot");
					txtName.setEditable(false);
					txtMessage.setEditable(true);
					frmAccord.setConnectStatus(ConnectStatus.JOINED);
					btnConnectStatus.setBackground(new Color(127, 255, 0));
					txtUsers.setText(msg.replaceAll(",", "\r\n"));
					System.out.println("Recieved WELC protocal");
					break;
				case "FAIL":
					printLog("Failed to Join the Chat. Reason: " + msg, "Friendly Bot");
					System.out.println("Recieved FAIL protocal");
					break;
				case "SPOK":
					printLog(msg, txtName.getText());
					System.out.println("Recieved SPOK protocal");
					frmAccord.setNewMessages(true);
					break;
				case "MESG":
					String usrTyping = msg.substring(msg.indexOf("<") + 1, msg.indexOf(">"));
					String chatMsg = msg.substring(msg.indexOf(">") + 1);
					printLog(chatMsg, usrTyping);
					System.out.println("Recieved MESG protocal");
					frmAccord.setNewMessages(true);
					break;
				case "ENTR":
					printLog("Welcome! " + msg + " joins the chat!", msg);
					txtUsers.setText(txtUsers.getText() + "\r\n" + msg);
					System.out.println("Recieved ENTR protocal");
					frmAccord.setNewMessages(true);
					break;
				case "LEAV":
					String usrLeaving = msg.substring(msg.indexOf("<") + 1, msg.indexOf(">"));
					String leaveMsg = msg.substring(msg.indexOf(">") + 1);
					printLog(usrLeaving + " leaves the chat! Message: " + leaveMsg, usrLeaving);
					txtUsers.setText(txtUsers.getText().replaceAll(usrLeaving + "\r\n", ""));
					System.out.println("Recieved LEAV protocal");
					frmAccord.setNewMessages(true);
					break;
				case "GBYE":
					printLog(msg, "Friendly Bot");
					txtName.setEditable(true);
					txtMessage.setEditable(false);
					frmAccord.setConnectStatus(ConnectStatus.DISCONNECTING);
					btnConnectStatus.setBackground(new Color(255, 0, 0));
					System.out.println("Recieved GBYE protocal");
					break;
				case "PUNT":
					String usrPunting = msg.substring(msg.indexOf("<") + 1, msg.indexOf(">"));
					String puntMsg = msg.substring(msg.indexOf(">") + 1);
					printLog(usrPunting + " has been kicked! Reason: " + puntMsg, usrPunting);
					txtUsers.setText(txtUsers.getText().replaceFirst(usrPunting + "\r\n", ""));
					System.out.println("Recieved PUNT protocal");
					break;
				case "KICK":
					printLog("You have been kicked from the chat. Reason: " + msg, "Friendly Bot");
					frmAccord.setConnectStatus(ConnectStatus.DISCONNECTING);
					btnConnectStatus.setBackground(new Color(255, 0, 0));
					System.out.println("Recieved KICK protocal");
					break;
				case "INCM":
					String usrIncm = msg.substring(0, msg.indexOf(" "));
					String dmIp = msg.substring(msg.indexOf(" ") + 1, msg.indexOf(":"));
					String dmPort = msg.substring(msg.indexOf(":") + 1);
					System.out.println("Recieved INCM protocal: " + msg);
					frmAccord.dmAccept(usrIncm, dmIp, dmPort);
					break;
				case "PYES": 
					String cfmUser = msg.substring(0, msg.indexOf(" "));
					String cfmIp = msg.substring(msg.indexOf(" ") + 1, msg.indexOf(":"));
					String cfmPort = msg.substring(msg.indexOf(":") + 1);
					System.out.println("Recieved PYES protocal");
					if (frmDm != null) frmDm.dmConfirmed(cfmUser, cfmIp, cfmPort);
					else System.out.println("Welp, looks like frmDm is null, code error!");
					break;
				}
				if (sbOutput.getMaximum() != sbOutput.getValue() + sbOutput.getVisibleAmount()) {
					btnToBottom.setEnabled(true);
					btnToBottom.setVisible(true);
				}
			}

		}

	}

	public void setKillThread(boolean killThread) {
		this.killThread = killThread;
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

	public FrmDm getFrmdm() {
		return frmDm;
	}

	public void setFrmdm(FrmDm frmdm) {
		this.frmDm = frmdm;
	}
	
	

}
