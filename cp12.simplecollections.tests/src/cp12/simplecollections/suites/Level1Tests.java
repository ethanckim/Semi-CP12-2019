package cp12.simplecollections.suites;

import cp12.simplecollections.tests.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author Mr. Hapke
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ SimpleLinkedListTest.class, SimpleArraySetTest.class, SortedDoubleLinkedListTest.class })
public class Level1Tests {

}
