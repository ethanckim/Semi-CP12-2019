package ca.hapke.net.udp.data;

import java.io.IOException;

import ca.hapke.gyro.data.DataCluster;
import ca.hapke.gyro.data.DataType;
import ca.hapke.net.udp.TransmitMode;
import ca.hapke.net.udp.UdpUtil;

/**
 * @author Mr. Hapke
 *
 */
public class DataTransmit {

	private int port;
	private String ip;
	private boolean udpEnabled = false;
	private SendThread t;

	private class SendThread extends Thread {

		@Override
		public void run() {
			while (udpEnabled) {
				byte[] bytes = getPacket();
				try {
					UdpUtil.send(ip, port, bytes, TransmitMode.AccelGyroData.mode);
				} catch (IOException e) {
					udpEnabled = false;
				}
			}
		}

	}

	public void setTarget(String ip, int port) {
		this.ip = ip;
		this.port = port;
		udpEnabled = true;
		if (t == null || !t.isAlive()) {
			t = new SendThread();
			t.start();
		}
	}

	public void stop() {
		udpEnabled = false;
	}

	private DataCluster inputs;

	public DataTransmit(DataCluster inputs) {
		this.inputs = inputs;
	}

	public void add(DataType arg0) {
		inputs.add(arg0);
	}

	public byte[] getPacket() {
		int size = inputs.size();
		int len = 1 + 2 * size;
		// 1 for the count at beginning of packet, and 2 for each input

		byte[][] datas = new byte[size][];

		int i = 0;
		for (DataType input : inputs.getValues()) {
//		for (int i = 0; i < size; i++) {
//			DataType input = inputs.get(i);
			datas[i] = input.getBytes();
			len += datas[i].length;
			i++;
		}

		byte[] output = new byte[len];

		output[0] = (byte) size;
		i = 0;
		for (DataType input : inputs.getValues()) {
//		for (int i = 0; i < size; i++) {
//			DataType input = inputs.get(i);
			output[2 * i + 1] = input.type.id;
			output[2 * i + 2] = (byte) datas[i].length;
			i++;
		}

		int pos = size * 2 + 1;
		for (i = 0; i < datas.length; i++) {
			int length = datas[i].length;
			for (int j = 0; j < length; j++) {
				output[pos + j] = datas[i][j];
			}
			pos += length;
		}

		return output;
	}
}
