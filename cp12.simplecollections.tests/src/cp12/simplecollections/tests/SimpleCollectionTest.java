package cp12.simplecollections.tests;

import org.junit.Test;

import cp12.simplecollections.SimpleCollection;
import junit.framework.TestCase;

/**
 * @author Mr. Hapke
 */
public abstract class SimpleCollectionTest extends TestCase {

	protected SimpleCollection<Integer> a;
	private boolean reverseOrder;
	private boolean firstOrderCheck;

	protected abstract SimpleCollection<Integer> createCollection();

	protected abstract SimpleCollection<String> createCollectionString();

	protected abstract SimpleCollection<Double> createCollectionDouble();

	@Override
	protected void setUp() throws Exception {
		firstOrderCheck = false;
		a = createCollection();
	}

	protected abstract <T extends Comparable<T>> void scan(SimpleCollection<T> structure);

	protected void addDefaults(boolean testAsYouGo) {
		if (testAsYouGo) {
			assertEquals(0, a.size());
		}

		a.add(45);
		if (testAsYouGo) {
			assertEquals(1, a.size());
			scan(a);
		}

		a.add(37);
		if (testAsYouGo) {
			assertEquals(2, a.size());
			scan(a);
		}

		a.add(22);
		if (testAsYouGo) {
			assertEquals(3, a.size());
			scan(a);
		}

		a.add(41);
		if (testAsYouGo) {
			assertEquals(4, a.size());
			scan(a);
		}

		a.add(65);
		if (testAsYouGo) {
			assertEquals(5, a.size());
			scan(a);
		}

		a.add(81);
		if (testAsYouGo) {
			assertEquals(6, a.size());
			scan(a);
		}

	}

	@Override
	protected void tearDown() throws Exception {
		a = null;
	}

	@Test
	public void testRemove() {
		addDefaults(false);
		assertEquals(6, a.size());
		assertTrue(a.contains(45));
		assertTrue(a.contains(37));
		assertTrue(a.contains(22));
		assertTrue(a.contains(41));
		assertTrue(a.contains(65));
		assertTrue(a.contains(81));

		a.remove(37);

		assertEquals(5, a.size());
		scan(a);
		assertTrue(a.contains(45));
		assertFalse(a.contains(37));
		assertTrue(a.contains(22));
		assertTrue(a.contains(41));
		assertTrue(a.contains(65));
		assertTrue(a.contains(81));

		a.remove(65);

		assertEquals(4, a.size());
		scan(a);
		assertTrue(a.contains(45));
		assertFalse(a.contains(37));
		assertTrue(a.contains(22));
		assertTrue(a.contains(41));
		assertFalse(a.contains(65));
		assertTrue(a.contains(81));

		a.remove(22);

		assertEquals(3, a.size());
		scan(a);
		assertTrue(a.contains(45));
		assertFalse(a.contains(37));
		assertFalse(a.contains(22));
		assertTrue(a.contains(41));
		assertFalse(a.contains(65));
		assertTrue(a.contains(81));
	}

	@Test
	/**
	 * Removes the least recently added element from the list
	 */
	public void testRemoveOldest() {
		addDefaults(false);
		assertEquals(6, a.size());
		assertTrue(a.contains(45));
		assertTrue(a.contains(37));
		assertTrue(a.contains(22));
		assertTrue(a.contains(41));
		assertTrue(a.contains(65));
		assertTrue(a.contains(81));

		a.remove(45);

		assertEquals(5, a.size());
		scan(a);
		assertFalse(a.contains(45));
		assertTrue(a.contains(37));
		assertTrue(a.contains(22));
		assertTrue(a.contains(41));
		assertTrue(a.contains(65));
		assertTrue(a.contains(81));
	}

	@Test
	/**
	 * Removes the most recently added element from the list
	 */
	public void testRemoveNewest() {
		addDefaults(false);
		assertEquals(6, a.size());
		scan(a);
		assertTrue(a.contains(45));
		assertTrue(a.contains(37));
		assertTrue(a.contains(22));
		assertTrue(a.contains(41));
		assertTrue(a.contains(65));
		assertTrue(a.contains(81));

		a.remove(81);

		assertEquals(5, a.size());
		scan(a);
		assertTrue(a.contains(45));
		assertTrue(a.contains(37));
		assertTrue(a.contains(22));
		assertTrue(a.contains(41));
		assertTrue(a.contains(65));
		assertFalse(a.contains(81));
	}

	@Test
	public void testContains() {
		addDefaults(false);
		assertTrue(a.contains(45));
		assertTrue(a.contains(37));
		assertTrue(a.contains(81));
		assertFalse(a.contains(43));
		assertFalse(a.contains(0));
		assertFalse(a.contains(-12));
		assertFalse(a.contains(900));
		assertFalse(a.contains(55));
	}

	@Test
	public void testContainsWithStrings() {
		SimpleCollection<String> b = createCollectionString();
		b.add("Y");
		b.add("HELO");
		b.add("THAR");
		b.add("(i'm a pirate)");
		assertEquals(4, b.size());

		// in no particular order
		assertTrue(b.contains("HELO"));
		assertTrue(b.contains("(i'm a pirate)"));
		assertTrue(b.contains("Y"));
		assertFalse(b.contains("walk the plank, scoundrel"));
		assertTrue(b.contains("THAR"));
	}

	@Test
	public void testSize() {
		addDefaults(false);
		assertEquals(6, a.size());
	}

	@Test
	public void testAdd() {
		addDefaults(true);
		assertEquals(6, a.size());
		a.add(38);
		assertEquals(7, a.size());
		a.add(46);
		assertEquals(8, a.size());
		a.add(67);
		assertEquals(9, a.size());
		a.add(901);
		assertEquals(10, a.size());
		scan(a);
	}

	@Test
	public void testAddWithDecimals() {
		SimpleCollection<Double> d = createCollectionDouble();
		assertEquals(0, d.size());
		d.add(32d);
		assertEquals(1, d.size());
		scan(d);
		d.add(28d);
		assertEquals(2, d.size());
		scan(d);
		d.add(33d);
		assertEquals(3, d.size());
		scan(d);
		d.add(1d);
		assertEquals(4, d.size());
		scan(d);
		d.add(3d);
		assertEquals(5, d.size());
		scan(d);
		d.add(6d);
		assertEquals(6, d.size());
		scan(d);
		d.add(29d);
		assertEquals(7, d.size());
		scan(d);
		d.add(28.5);
		assertEquals(8, d.size());
		scan(d);
	}

	@Test
	public void testClear() {
		addDefaults(false);

		assertTrue(a.contains(45));
		assertTrue(a.contains(37));
		assertTrue(a.contains(45));
		assertFalse(a.contains(43));
		assertFalse(a.contains(0));
		assertFalse(a.contains(-12));
		assertFalse(a.contains(900));
		assertFalse(a.contains(55));

		assertEquals(6, a.size());
		a.clear();
		assertEquals(0, a.size());

		assertFalse(a.contains(45));
		assertFalse(a.contains(37));
		assertFalse(a.contains(45));
		assertFalse(a.contains(43));
		assertFalse(a.contains(0));
		assertFalse(a.contains(-12));
		assertFalse(a.contains(900));
		assertFalse(a.contains(55));

		// clear an already empty one
		a.clear();
		assertEquals(0, a.size());

		assertFalse(a.contains(45));
		assertFalse(a.contains(37));
		assertFalse(a.contains(45));
		assertFalse(a.contains(43));
		assertFalse(a.contains(0));
		assertFalse(a.contains(-12));
		assertFalse(a.contains(900));
		assertFalse(a.contains(55));
	}

	@Test
	public void testMaximumDefaults() {
		addDefaults(false);
		assertEquals(81, a.maximum().intValue());
	}

	@Test
	public void testMaximum_ClearAndTestAsYouAddMore() {
		assertNull(a.maximum());
		a.add(15);
		assertEquals(15, a.maximum().intValue());
		a.add(-1);
		assertEquals(15, a.maximum().intValue());
		a.add(50);
		assertEquals(50, a.maximum().intValue());
		a.add(83);
		assertEquals(83, a.maximum().intValue());
		a.add(12);
		assertEquals(83, a.maximum().intValue());
		a.add(16);
		assertEquals(83, a.maximum().intValue());
		a.add(14);
		assertEquals(83, a.maximum().intValue());
	}

	@Test
	public void testIsEmpty() {
		assertTrue(a.isEmpty());
		addDefaults(false);
		assertFalse(a.isEmpty());
	}

	protected void acceptEither(int i, int forwards, int backwards) {
		if (!firstOrderCheck) {
			if (i == forwards) {
				reverseOrder = false;
				return;
			} else if (i == backwards) {
				reverseOrder = true;
				return;
			} else {
				fail("Did not get either " + forwards + " or " + backwards);
			}
			firstOrderCheck = true;
		} else {
			if (reverseOrder)
				assertEquals(backwards, i);
			else
				assertEquals(forwards, i);
		}
	}

}
