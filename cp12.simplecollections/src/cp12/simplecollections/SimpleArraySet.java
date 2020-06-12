package cp12.simplecollections;

import java.lang.reflect.Array;

/**
 * Sets do not allow duplicates, unlike lists.
 * @author Mr. Hapke
 *
 * @param <T>
 */
public class SimpleArraySet<T extends Comparable<T>> extends SimpleIndexedCollection<T> {

	/**
	 * @param c You'll need this for createArray to work later
	 * @param intialCapacity the number of slots that should be available when we start
	 */
	public SimpleArraySet(Class<T> c, int intialCapacity) {

	}

	/*-
	 * When using parameterized types (i.e.: the T), Java will not allow you to do this:
	 * T[] var = new T[n];
	 * 
	 * So instead, you'll need to use this method, for example:
	 * T[] var = createArray(c,n);
	 */
	@SuppressWarnings("unchecked")
	private T[] createArray(Class<T> c, int n) {
		return (T[]) Array.newInstance(c, n);
	}

	public int capacity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contains(T input) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean add(T input) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(T input) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the index of input, or -1 if not found
	 */
	public int findIndex(T input) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @param n the index to start searching from
	 * @return the index of input, or -1 if not found
	 */
	public int findIndex(T input, int n) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public T get(int n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T maximum() {
		// TODO Auto-generated method stub
		return null;
	}

}
