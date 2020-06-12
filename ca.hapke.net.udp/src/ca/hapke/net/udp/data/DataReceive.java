package ca.hapke.net.udp.data;

import java.util.Arrays;

import ca.hapke.gyro.data.DataCluster;
import ca.hapke.gyro.data.DataType;
import ca.hapke.gyro.data.DataType.InputType;

/**
 * @author Mr. Hapke
 *
 */
public abstract class DataReceive {

	public static void update(DataCluster cluster, byte[] data) {
		byte sets = data[0];
		byte[] ids = new byte[sets];
		int[] lens = new int[sets];
		for (int i = 0; i < sets; i++) {
			ids[i] = data[2 * i + 1];
			lens[i] = Byte.toUnsignedInt(data[2 * i + 2]);
		}

		int from = 2 * sets + 1;
		for (int i = 0; i < sets; i++) {
			byte id = ids[i];

			DataType dataSet = cluster.getData(InputType.fromId(id));

			int end = from + lens[i];
			byte[] vals = Arrays.copyOfRange(data, from, end);
			dataSet.update(vals);

			from = end;
		}
	}
}
