package cp12.simplecollections;

/**
 * 
 * @author ethanc.kim
 * 
 * @param <T>
 */
public class TreeNode<T extends Comparable<T>> implements Comparable<TreeNode<T>> {

	//The data that the tree element contains
	protected T data;
	
	//The tree element that this tree element points to, which has a smaller data than this one
	protected TreeNode<T> left;
	
	//The tree element that this tree element points to, which has a larger data than this one
	protected TreeNode<T> right;
	
	
	public T getData() {
		return data;
	}

	public int compareTo(TreeNode<T> o) {
		return data.compareTo(o.data);
	}

	public TreeNode<T> getRight() {
		return right;
	}

	public TreeNode<T> getLeft() {
		return left;
	}

}
