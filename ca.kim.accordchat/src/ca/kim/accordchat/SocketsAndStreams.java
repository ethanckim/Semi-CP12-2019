package ca.kim.accordchat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SocketsAndStreams {

	// For Main Server
	private Socket socket;
	private BufferedReader reader;
	private BufferedWriter writer;

	// DM
//	private DatagramSocket dmSocket;
//	private byte[] recvData = new byte[256];
//	InetAddress ip;

	public SocketsAndStreams() {
	}

	public boolean connect(String ip, int port) {

		try {
			if (socket != null)
				socket = null;
			if (reader != null)
				reader = null;
			if (writer != null)
				writer = null;

			socket = new Socket(ip, port);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			// Connection Success! :)
		} catch (Exception e) {
			socket = null;
			reader = null;
			writer = null;
			// Connection Failed... :(
			return false;
		}

		return true;
	}

	public boolean disconnect() {
		if (!socket.isClosed())
			try {
				socket.close();
				reader.close();
				writer.close();
			} catch (Exception e) {
				return false;
			}
		return true;
	}

	private boolean sendWrite(String msg) {
		try {
			if (!msg.endsWith("\r\n")) {
				msg = msg + "\r\n";
			}
			writer.write(msg);
			// flush means, send everything you can right now,
			// but do not close the writer so we cah keep writing on it
			writer.flush();
			return true;
		} catch (IOException e) {
			// Join Failed... :(
			return false;
		}
	}

	public String receiveRead() {
		String msg;
		try {
			msg = reader.readLine();
			return msg;
		} catch (Exception e) {
			return null;
		}
	}

	public boolean sJoin(String usrName) {
		String message = "JOIN: " + usrName;
		return sendWrite(message);
	}

	public boolean sSpeak(String chatMsg) {
		String message = "SPEK: " + chatMsg;
		return sendWrite(message);
	}

	public boolean sPart(String leaveMsg) {
		String message = "PART: " + leaveMsg;
		return sendWrite(message);
	}

	public boolean sDmRequest(String username, String port) {
		String message = "REQS: " + username + " " + port;
		return sendWrite(message);
	}

	public boolean sDmAccept(String username, String port) {
		String message = "ACPT: " + username + " " + port;
		return sendWrite(message);
	}

//	public boolean openDm(FrmDm frmDm) {
//		try {
//			int port = Integer.parseInt(frmDm.getDmAddress().getMyPort());
//			dmSocket = new DatagramSocket(port);
//			// Connection Success! :)
//
//		} catch (SocketException e) {
//			dmSocket = null;
//			// Connection Failed... :(
//			return false;
//		}
//		return true;
//	}
//
//	public boolean sendDm(String sendThis, FrmDm frmDm) {
//		try {
//			byte[] sendData = sendThis.getBytes();
//
//			String theirIp = frmDm.getDmAddress().getTheirIp();
//			InetAddress inetAddress = InetAddress.getByName(theirIp);
//			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, inetAddress,
//					Integer.parseInt(frmDm.getDmAddress().getTheirPort()));
//			dmSocket.send(sendPacket);
//			return true;
//		} catch (IOException e) {
//			// Join Failed... :(
//			e.printStackTrace();
//			return false;
//		}
//	}
//
//	public String recieveDm() {
//		try {
//			DatagramPacket recvPacket = new DatagramPacket(recvData, recvData.length);
//			dmSocket.receive(recvPacket);
//			String recvMsg = new String(recvPacket.getData(), 0, recvPacket.getLength());
//			return recvMsg;
//		} catch (IOException e) {
//			return null;
//		}
//
//	}

}
