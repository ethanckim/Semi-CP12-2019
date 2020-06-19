# Simple Collections

For Junit tests that assess the accuracy of the datastructure functions, please refer to [cp12.simplecollections.tests](https://github.com/ethanckim/Semi-CP12-2019/tree/master/cp12.simplecollections.tests).

This subdirectory contains the following:
## datastructures
 * Simple Linked List (List Node Data Objects)
 * Binary Tree (Tree Node Data Objects)
 
## Datastructures NOT developed
 * Simple Array Set 
 * Sorted Double Linked List
 
 **Note**: simplecollection and simpleindexedcollection are parent interfaces for most of the data stuctures above.
datastructure functions.

## datastructure Functions
### Default
 * size();
 * isEmpty();
 * contains(T input);
 * add(T input);
 * remove(T input);
 * clear();
 * maximum();
 ### Linked list
 * findIndex(T input, int n);
 * get(int n);
 * getHead();
 ### Binary Tree
 * depthFirstTraversal();
 * recursiveDepthTraversal(TreeNode<T> target);
 * breadthFirstTraversal();
 * getRoot();
