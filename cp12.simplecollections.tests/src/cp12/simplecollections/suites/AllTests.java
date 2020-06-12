package cp12.simplecollections.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import cp12.simplecollections.tests.*;

/**
 * @author Mr. Hapke
 */
@RunWith(Suite.class)
@SuiteClasses({ BinaryTreeTest.class, SimpleArraySetTest.class, SimpleLinkedListTest.class, 
		SortedDoubleLinkedListTest.class, })
public class AllTests {

}
