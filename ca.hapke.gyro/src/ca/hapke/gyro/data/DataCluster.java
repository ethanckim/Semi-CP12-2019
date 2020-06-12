package ca.hapke.gyro.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ca.hapke.gyro.data.DataType.InputType;

/**
 * @author Mr. Hapke
 *
 */
public class DataCluster {
//
//	private List<IDataListener> listeners = new ArrayList<>();
//
//	protected void notifyListeners() {
//		for (IDataListener l : listeners) {
//			l.updateOccurred(this);
//		}
//	}
//
//	public boolean addListener(IDataListener l) {
//		return listeners.add(l);
//	}

//	private List<DataType> inputs = new ArrayList<DataType>();

	private Map<InputType, DataType> typeMap = new HashMap<>();

	public DataType getData(InputType it) {
		DataType val = typeMap.get(it);
		if (val == null) {
			val = DataType.create(it);
			typeMap.put(it, val);
		}
		return val;
	}

	public void add(DataType dt) {
//		return inputs.add(arg0);
		typeMap.put(dt.type, dt);
	}

//	public DataType get(int arg0) {
//		return inputs.get(arg0);
//	}

	public int size() {
		return typeMap.size();
	}

	public Collection<DataType> getValues() {
		return typeMap.values();
	}
}
