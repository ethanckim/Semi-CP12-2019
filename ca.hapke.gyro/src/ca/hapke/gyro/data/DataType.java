package ca.hapke.gyro.data;

/**
 * @author Mr. Hapke
 *
 */
public abstract class DataType<T> {
	public enum InputType {
		ADC(2), Gyro(1), ArcadeButton(3);
		public final byte id;

		private InputType(int id) {
			this.id = (byte) id;
		}

		public static InputType fromId(int x) {
			for (InputType dt : values()) {
				if (x == dt.id) {
					return dt;
				}
			}
			return null;
		}
	}

	protected final String[] dimensionNames;
	protected final T[] data;
	public final InputType type;
	protected final int dims;

	protected DataType(InputType type, String... dimensionNames) {
		this.type = type;
		this.dimensionNames = dimensionNames;
		this.dims = dimensionNames.length;
		this.data = createData(dims);
	}

	protected abstract T[] createData(int dims);

	public static DataType create(InputType t) {
		switch (t) {
		case ADC:
			return new AdcDataType();
		case ArcadeButton:
			return new ArcadeButtonDataType();
		case Gyro:
			return new GyroDataType();
		}
		return null;
	}

	public abstract byte[] getBytes();

	public abstract void update(byte[] vals);

	public T[] getData() {
		return data;
	}

	public T getData(int i) {
		if (i < data.length)
			return data[i];
		else
			return null;
	}

	public void updateByIndex(int i, T value) {
		if (i >= dims)
			return;
		data[i] = value;
	}

	public int getDimensions() {
		return dims;
	}

	public String getName(int i) {
		return dimensionNames[i];
	}

	public String getValueString(int i) {
		T x = data[i];
		if (x != null)
			return x.toString();
		else
			return "null";
	}

}
