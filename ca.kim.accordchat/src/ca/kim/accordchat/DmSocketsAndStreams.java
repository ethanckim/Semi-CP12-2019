package ca.kim.accordchat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class DmSocketsAndStreams {

	private DatagramSocket dmSocket;
	private byte[] recvData = new byte[256];
	InetAddress ip;
	
	public DmSocketsAndStreams() {
	}
	
	public boolean openDm(FrmDm frmDm) {
		try {
			int port = Integer.parseInt(frmDm.getDmAddress().getMyPort());
			dmSocket = new DatagramSocket(port);
			// Connection Success! :)

		} catch (SocketException e) {
			dmSocket = null;
			// Connection Failed... :(
			return false;
		}
		return true;
	}

	public boolean sendDm(String sendThis, FrmDm frmDm) {
		try {
			byte[] sendData = sendThis.getBytes();

			String theirIp = frmDm.getDmAddress().getTheirIp();
			InetAddress inetAddress = InetAddress.getByName(theirIp);
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, inetAddress,
					Integer.parseInt(frmDm.getDmAddress().getTheirPort()));
			dmSocket.send(sendPacket);
			return true;
		} catch (IOException e) {
			// Join Failed... :(
			e.printStackTrace();
			return false;
		}
	}

	public String recieveDm() {
		try {
			DatagramPacket recvPacket = new DatagramPacket(recvData, recvData.length);
			dmSocket.receive(recvPacket);
			String recvMsg = new String(recvPacket.getData(), 0, recvPacket.getLength());
			return recvMsg;
		} catch (IOException e) {
			return null;
		}

	}

}
