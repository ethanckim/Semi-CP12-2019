package ca.hapke.gyro.data;

/**
 * @author Mr. Hapke
 *
 */
public class ArcadeButtonDataType extends DataType<Boolean> {

	public ArcadeButtonDataType() {
		super(InputType.ArcadeButton, "a", "b");
	}

	@Override
	public byte[] getBytes() {
		byte[] out = new byte[data.length];
		for (int i = 0; i < data.length; i++) {
			if (data[i])
				out[i] = (byte) 0xFF;
			else
				out[i] = (byte) 0;
		}
		return null;
	}

	@Override
	public void update(byte[] vals) {
		for (int i = 0; i < vals.length && i < data.length; i++) {
			byte v = vals[i];
			if (v != 0)
				data[i] = true;
			else
				data[i] = false;
		}
	}

	@Override
	protected Boolean[] createData(int dims) {
		return new Boolean[dims];
	}

//	@Override
//	public String getValueString(int i) {
//		if ()
//		return data[i] ? "true " : "false";
//	}

}
