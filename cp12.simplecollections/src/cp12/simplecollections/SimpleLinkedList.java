/**
 * 
 */
package cp12.simplecollections;

/**
 * @author Ethan Kim
 *
 */
public class SimpleLinkedList<T extends Comparable<T>> extends SimpleIndexedCollection<T> {

	protected ListNode<T> root;
	
	/**
	 * 
	 */
	public SimpleLinkedList() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see cp12.simplecollections.SimpleCollection#size()
	 */
	@Override
	public int size() {
		int count = 0;
		ListNode<T> target = root;
		
		while (target != null) {
			count ++;
			target = target.next;
		}
		
		return count;
	}

	/* (non-Javadoc)
	 * @see cp12.simplecollections.SimpleCollection#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		boolean isEmpty = false;
		if (this.size() == 0) isEmpty = true;
		else isEmpty = false;
		
		return isEmpty;
	}

	/* (non-Javadoc)
	 * @see cp12.simplecollections.SimpleCollection#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(T input) {
		ListNode<T> target = root;
		boolean contains = false;
		
		while (target != null) {
			if (target.data.equals(input)) contains = true;
			target = target.next;
		}
		return contains;
	}

	/* (non-Javadoc)
	 * @see cp12.simplecollections.SimpleCollection#add(java.lang.Object)
	 */
	@Override
	public boolean add(T input) {

		ListNode<T> newElement = new ListNode<T>();
		newElement.data = input;
		ListNode<T> target = root;
		
		if (target == null) {
			root = newElement;
			return true;
		}
		while (target.next != null) {
			target = target.next;
		}
		target.next = newElement;
		
		return true;
	}

	/* (non-Javadoc)
	 * @see cp12.simplecollections.SimpleCollection#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(T input) {		
		
		ListNode<T> target = root;
		ListNode<T> prev = null;
		
		while (target != null) {
			
			if (target.data.equals(input)) {
				if (prev == null) this.root = target.next;
				else prev.next = target.next;
			}
			
			prev = target;
			target = target.next;
		}
		
		return true;
	}

	/* (non-Javadoc)
	 * @see cp12.simplecollections.SimpleCollection#clear()
	 */
	@Override
	public void clear() {
		this.root = null;		
	}

	@Override
	public int findIndex(T input, int n) {
		int i = 0;
		ListNode<T> target = root;
		
		while (i < n) {
			target = target.next;
			i++;
		}
		
		while (target != null) {
			
			if (target.data.equals(input)) break;
			
			i ++;
			target = target.next;
		}
		
		if (i == this.size()) i = -1;
		
		return i;
	}

	@Override
	public T get(int n) {
		ListNode<T> target = root;
		
		for (int i = 0; i < n ; i++) {
			target = target.next;
		}
		
		return target.data;
	}

	@Override
	public T maximum() {
		//Collection Empty
		if (root == null) {
			return null;
		}
		
		//have at least one element...
		
		ListNode<T> target = root;
		T largestSoFar = target.data;
		
		while (target != null) {
			if (target.data.compareTo(largestSoFar) > 0 /* target.data > largetstSoFar */) {
				largestSoFar = target.data;
			}
			
			target = target.next;
		}
		
		return largestSoFar;
	}

	public ListNode<T> getHead() {
		return root;
	}
}
