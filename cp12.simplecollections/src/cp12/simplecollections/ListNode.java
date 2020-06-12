package cp12.simplecollections;

/**
 * 
 * @author Ethan Kim
 *
 * @param <T>
 */
public class ListNode<T> {
	
	//The data that the single ListNode contains
	protected T data;
	
	//The ListNode that this ListNode Points to
	protected ListNode<T> next;

	public T getData() {
		return data;
	}

	public ListNode<T> getNext() {
		return next;
	}

	public Object getPrev() {
		return null;
	}	
	
}
