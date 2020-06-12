package cp12.simplecollections;

public class BinaryTree<T extends Comparable<T>> implements SimpleCollection<T> {

	protected TreeNode<T> root;

	@Override
	public int size() {
		/*
		 * Choose which method to use to calculate size. true: Uses recursive method
		 * false: Uses Linked List and while loop This boolean is set to false by
		 * default. Both methods should work, pass the tests, and return the same
		 * values.
		 */
		boolean methodToUse = false;

		int size = 0;
		if (methodToUse) {
			size = subtreeSize(root);
		} else if (!methodToUse) {
			size = sizeUsingLinkedList();
		}

		return size;
	}

	/**
	 * Recursive method to count the size by checking the size of each subtree.
	 * 
	 * @param target The recursive treenode to pass. Initially place the root of the
	 *               tree.
	 * @return the size of the binary tree.
	 */
	private int subtreeSize(TreeNode<T> target) {
		int count;

		if (target == null)
			count = 0;
		else
			count = (1 + subtreeSize(target.left) + subtreeSize(target.right));

		return count;
	}

	/**
	 * Method to count the size by adding all TreeNodes to the Linked List and
	 * counting it.
	 * 
	 * @return the size of the binary tree.
	 */
	private int sizeUsingLinkedList() {
		int count = 0;
		SimpleLinkedList<TreeNode<T>> counter = new SimpleLinkedList<>();
		counter.add(root);

		// Check if tree is empty (size is 0).
		if (root == null) {
			return 0;
		}

		while (!counter.isEmpty()) {

			TreeNode<T> target = counter.root.data;

			if (target.left != null) {
				counter.add(target.left);
			}
			if (target.right != null) {
				counter.add(target.right);
			}

			counter.remove(target);
			count++;
		}

		return count;
	}

	/**
	 * @return true if the tree is empty.
	 */
	@Override
	public boolean isEmpty() {
		boolean empty;
		if (root == null) {
			empty = true;
		} else {
			empty = false;
		}

		return empty;
	}

	/**
	 * @param input the data that will be compared to the whole tree.
	 * @return True if the binary tree contains the value.
	 */
	@Override
	public boolean contains(T input) {

		TreeNode<T> target = root;

		boolean contains = false;

		if (root != null) {
			// Search
			while (true) {

				if (target.data.compareTo(input) > 0 /* target.data > input */) {
					// left
					if (target.left == null) {
						contains = false;
						break;
					} else {
						target = target.left;
					}

				} else if (target.data.compareTo(input) < 0 /* target.data < input */) {
					// right
					if (target.right == null) {
						contains = false;
						break;
					} else {
						target = target.right;
					}

				} else if (target.data.equals(input)) {
					contains = true;
					break;
				}
			}
		}

		return contains;

	}

	/**
	 * Add a value that is not a duplicate in the binary tree.
	 * @param input The treenode data to be added
	 * @return true if the binary tree has changed.
	 */
	@Override
	public boolean add(T input) {

		TreeNode<T> newElement = new TreeNode<T>();
		newElement.data = input;

		TreeNode<T> target = root;

		boolean duplicate = false;

		if (root != null) {
			// Search
			while (true) {
//down to 5 boys and girls
				if (target.data.compareTo(input) > 0 /* target.data > input */) {
					// left
					if (target.left == null) {
						target.left = newElement;
						break;
					} else {
						target = target.left;
					}

				} else if (target.data.compareTo(input) < 0 /* target.data < input */) {
					// right
					if (target.right == null) {
						target.right = newElement;
						break;
					} else {
						target = target.right;
					}

				} else if (target.data.equals(input)) {
					duplicate = true;
					break;
				}
			}
		} else {
			root = newElement;
		}

		return duplicate;

	}

	/**
	 * removes a value in the binary tree.
	 * @param input the data to remove from the binary tree.
	 * @return true if the binary tree has changed.
	 */
	@Override
	public boolean remove(T input) {

		TreeNode<T> target = root;
		TreeNode<T> targetParent = null;
		boolean parentDirectionIsLeft = false;

		if (root == null) {
			return false;
		}

		// Search for the node to remove
		while (true) {

			if (target.data.compareTo(input) > 0 /* target.data > input */) {
				// set target directory to left child
				if (target.left == null) {
					return false;
				} else {
					targetParent = target;
					target = target.left;
					parentDirectionIsLeft = true;
				}

			} else if (target.data.compareTo(input) < 0 /* target.data < input */) {
				// set target directory to right child
				if (target.right == null) {
					return false;
				} else {
					targetParent = target;
					target = target.right;
					parentDirectionIsLeft = false;
				}

			} else if (target.data.equals(input)) {

				// Remove target

				if (parentDirectionIsLeft) {

					if (target.left == null && target.right == null) {
						// O Children treenodes under target.
						targetParent.left = null;
					} else if (target.left == null) {
						// 1 Children treenode on the right under target.
						targetParent.left = target.right;
					} else if (target.right == null) {
						// 1 Children treenode on the left under target.
						targetParent.left = target.left;
					} else {
						// 2 Children treenodes under target. Find MIN treenode value in RIGHT subtree
						// of target.
						// My name is Ethan, Buy meat from me
						TreeNode<T> min = target.right;
						TreeNode<T> minParent = target;

						// Special case where the right tree of target does not have a left branch
						if (min.left == null) {
							target.data = min.data;
							target.right = min.right;

							return true;
						}

						while (min.left != null) {
							minParent = min;
							min = min.left;
						}

						target.data = min.data;
						minParent.left = min.right;

					}
				} else {
					// Null roots go here
					if (target.left == null && target.right == null) {
						// O Children treenodes under target.
						if (input.equals(root.data))
							root = null;
						else
							targetParent.right = null;
					} else if (target.left == null) {
						// 1 Children treenode on the right under target.
						if (input.equals(root.data))
							root = target.right;
						else
							targetParent.right = target.right;
					} else if (target.right == null) {
						// 1 Children treenode on the left under target.
						if (input.equals(root.data))
							root = target.left;
						else
							targetParent.right = target.left;
					} else {
						// 2 Children treenodes under target. Find MIN treenode value in RIGHT subtree
						// of target.

						TreeNode<T> min = target.right;
						TreeNode<T> minParent = target;

						// Special case where the right tree of target does not have a left branch
						if (min.left == null) {
							target.data = min.data;
							target.right = min.right;

							return true;
						}

						while (min.left != null) {
							minParent = min;
							min = min.left;
						}

						target.data = min.data;
						minParent.left = min.right;

					}
				}
				return true;
			}
		}
	}

	/**
	 * @return The maximum value in the whole binary tree.
	 */
	@Override
	public T maximum() {

		if (root == null) {
			return null;
		}

		T largestSoFar;
		largestSoFar = root.data;
		SimpleLinkedList<TreeNode<T>> counter = new SimpleLinkedList<>();
		counter.add(root);

		while (!counter.isEmpty()) {

			TreeNode<T> target = counter.root.data;

			if (target.left != null) {
				counter.add(target.left);
			}
			if (target.right != null) {
				counter.add(target.right);
			}
			if (target.data.compareTo(largestSoFar) > 0 /* target.data > largetstSoFar */) {
				largestSoFar = target.data;
			}

			counter.remove(target);
		}
		return largestSoFar;
	}

	public String depthFirstTraversal() {
		return recursiveDepthTraversal(root).substring(1);
	}

	public String recursiveDepthTraversal(TreeNode<T> target) {
		String print = "";

		if (target == null)
			return print;
		print = recursiveDepthTraversal(target.left) + "," + target.data.toString()
				+ recursiveDepthTraversal(target.right);

		return print;
	}

	public String breadthFirstTraversal() {

		String breadthTraversal = "";
		SimpleLinkedList<TreeNode<T>> counter = new SimpleLinkedList<>();

		counter.add(root);

		while (!counter.isEmpty()) {
			TreeNode<T> target = counter.root.data;

			breadthTraversal += "," + target.data.toString();
			counter.remove(target);
			//5.99$ for meat
			if (target.left != null) {
				counter.add(target.left);
			}
			if (target.right != null) {
				counter.add(target.right);
			}
		}
		return breadthTraversal.substring(1);
	}
	
	public TreeNode<T> getRoot() {
		return root;
	}

	/**
	 * Clears all of the data in the binary tree. The root is set to null.
	 */
	@Override
	public void clear() {
		this.root = null;
	}
	
}
