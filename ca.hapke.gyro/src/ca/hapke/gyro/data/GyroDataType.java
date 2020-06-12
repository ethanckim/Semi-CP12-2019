package ca.hapke.gyro.data;

import javax.vecmath.Point3d;

import ca.hapke.util.AngleFormatter;
import ca.hapke.util.ByteUtil;

/**
 * @author Mr. Hapke
 *
 */
public class GyroDataType extends DataType<Point3d> {

//	private Point3d[] data;
	private AngleFormatter af;

	public GyroDataType() {
		super(InputType.Gyro, "accelAngles", "accelAccelerations", "gyroAngles", "gyroAngularSpeeds", "filteredAngles");

		af = new AngleFormatter(3, 3, 1, 1, 0.02);
		double[][] defaults = new double[dims][3];
		for (int i = 0; i < defaults.length; i++) {
			defaults[i][0] = 0;
			defaults[i][1] = 0;
			defaults[i][2] = 1;
		}
		update(defaults);
	}

	@Override
	protected Point3d[] createData(int dims) {
		return new Point3d[dims];
	}

	/**
	 * FIXME worried about []... vs [][] calling incorrect
	 * 
	 * @param values
	 */
	public void update(double[]... values) {
//		public void update(double[]... values) {
		for (int i = 0; i < values.length && i < data.length; i++) {
			Point3d axis = data[i];
			if (axis == null)
				axis = data[i] = new Point3d();

			axis.x = values[i][0];
			axis.y = values[i][1];
			axis.z = values[i][2];
		}
//		notifyListeners();
	}

	public void update(double[] values) {
		for (int set = 0; set < data.length; set++) {
			int x = set * 3;
			int y = set * 3 + 1;
			int z = set * 3 + 2;

			if (z >= values.length)
				return;

			data[set].x = values[x];
			data[set].y = values[y];
			data[set].z = values[z];
		}

	}

	@Override
	public byte[] getBytes() {
		int dims = data.length;
		double[] input = new double[3 * dims];
		for (int i = 0; i < dims; i++) {
			Point3d axis = data[i];
			input[3 * i] = axis.x;
			input[3 * i + 1] = axis.y;
			input[3 * i + 2] = axis.z;
		}
		return ByteUtil.doublesToBytes(input);
	}

	@Override
	public void update(byte[] vals) {
		double[] doubles = ByteUtil.bytesToDouble(vals);
		update(doubles);
	}

	@Override
	public String getValueString(int dimension) {
		return af.format(data[dimension].x) + "  " + af.format(data[dimension].y) + "  " + af.format(data[dimension].z);
	}

}
