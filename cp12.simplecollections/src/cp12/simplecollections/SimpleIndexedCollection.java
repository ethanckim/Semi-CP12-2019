package cp12.simplecollections;

/**
 * @author Mr. Hapke
 *
 */
public abstract class SimpleIndexedCollection<T extends Comparable<T>> implements SimpleCollection<T> {

	/**
	 * @return the index of input, or -1 if not found
	 */
	public int findIndex(T input) {

		return findIndex(input, 0);
	}

	/**
	 * @param n the index to start searching from
	 * @return the index of input, or -1 if not found
	 */
	public abstract int findIndex(T input, int n);

	/**
	 * @return the element at index n
	 */
	public abstract T get(int n);
}
