package cp12.simplecollections;

/**
 * A simplified version of {@link java.util.Collection}
 * 
 * @author Mr. Hapke
 */
public interface SimpleCollection<T extends Comparable<T>> {

	/**
	 * 
	 * @return number of elements in the collection
	 */
	public abstract int size();

	/**
	 * 
	 * @return true if there are no elements in the collection
	 */
	public abstract boolean isEmpty();

	/**
	 * 
	 * @param input
	 * @return true if there is no element that .equals() the input in the
	 *         collection
	 */
	public abstract boolean contains(T input);

	/**
	 * 
	 * @param input
	 * @return true if the collection changed
	 */
	public abstract boolean add(T input);

	/**
	 * 
	 * @param input
	 * @return true if the collection changed
	 */
	public abstract boolean remove(T input);

	/**
	 * removes all elements
	 */
	public abstract void clear();

	/**
	 * Finds the maximum value.
	 * Returns null when the collection is empty.
	 */
	public T maximum();
}