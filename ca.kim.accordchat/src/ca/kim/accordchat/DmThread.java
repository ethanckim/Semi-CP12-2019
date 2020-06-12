package ca.kim.accordchat;

import java.awt.EventQueue;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;

public class DmThread extends Thread {

	private boolean killThread = false;
	private DmSocketsAndStreams sas;
	private JTextArea txtOutput;
	private JScrollBar sbOutput;
	private JButton btnToBottom;
	private FrmDm frmDm;

	public DmThread(DmSocketsAndStreams sas, JTextArea txtOutput, JScrollBar sbOutput, JButton btnToBottom, FrmDm frmDm) {
		super();
		this.sas = sas;
		this.txtOutput = txtOutput;
		this.sbOutput = sbOutput;
		this.btnToBottom = btnToBottom;
		this.frmDm = frmDm;
	}

	@Override
	public void run() {
		while (!killThread) {
			String recievedDm = sas.recieveDm();
			if (recievedDm != null) {
				printLog(recievedDm, frmDm.getDmAddress().getTheirUsername());
				frmDm.setNewDm(true);
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


}
