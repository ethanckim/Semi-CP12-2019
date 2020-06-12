package cp12.simplecollections.tests;

import org.junit.Test;

import cp12.simplecollections.SimpleArraySet;
import cp12.simplecollections.SimpleCollection;

/**
 * @author Mr. Hapke
 *
 */
public class SimpleArraySetTest extends SimpleIndexedCollectionTest {

	@Override
	protected SimpleCollection<Integer> createCollection() {
		return new SimpleArraySet<Integer>(Integer.class, 10);
	}

	@Override
	protected SimpleCollection<String> createCollectionString() {
		return new SimpleArraySet<String>(String.class, 10);
	}

	@Override
	protected SimpleCollection<Double> createCollectionDouble() {
		return new SimpleArraySet<Double>(Double.class, 10);
	}

	@Override
	protected <T extends Comparable<T>> void scan(SimpleCollection<T> structure) {
		SimpleArraySet<T> sas = (SimpleArraySet<T>) structure;
		int firstNull = -1;
		for (int i = 0; i < sas.size(); i++) {
			T x = sas.get(i);
			if (x == null) {
				firstNull = i;
				break;
			}
		}
		if (firstNull >= 0) {
			for (int j = firstNull; j < sas.capacity(); j++) {
				assertNull(sas.get(j));
			}
		}
	}

	@Test
	public void testAddRejectDuplicates() {
		addDefaults(true);
		assertEquals(6, a.size());
		scan(a);

		a.add(38);
		assertEquals(7, a.size());
		scan(a);

		a.add(38);
		assertEquals(7, a.size());
		scan(a);

		a.add(38);
		assertEquals(7, a.size());
		scan(a);

		a.add(38);
		assertEquals(7, a.size());
		scan(a);

		a.add(46);
		assertEquals(8, a.size());
		scan(a);

		a.add(46);
		assertEquals(8, a.size());
		scan(a);

		a.add(46);
		assertEquals(8, a.size());
		scan(a);

		a.add(38);
		assertEquals(8, a.size());
		scan(a);

		a.add(67);
		assertEquals(9, a.size());
		scan(a);

		a.add(901);
		assertEquals(10, a.size());
		scan(a);

	}

	@Test
	public void testAddAndResize() {
		addDefaults(true);
		SimpleArraySet<Integer> sas = (SimpleArraySet<Integer>) a;
		assertEquals(6, a.size());
		assertEquals(10, sas.capacity());
		scan(a);

		a.add(38);
		assertEquals(7, a.size());
		assertEquals(10, sas.capacity());
		scan(a);

		a.add(46);
		assertEquals(8, a.size());
		assertEquals(10, sas.capacity());
		scan(a);

		a.add(46);
		assertEquals(8, a.size());
		assertEquals(10, sas.capacity());
		scan(a);

		a.add(67);
		assertEquals(9, a.size());
		assertEquals(10, sas.capacity());
		scan(a);

		a.add(901);
		assertEquals(10, a.size());
		assertEquals(10, sas.capacity());
		scan(a);

		a.add(100);
		assertEquals(11, a.size());
		assertEquals(20, sas.capacity());
		scan(a);

		a.add(101);
		assertEquals(12, a.size());
		assertEquals(20, sas.capacity());
		scan(a);

		a.add(102);
		assertEquals(13, a.size());
		assertEquals(20, sas.capacity());
		scan(a);

		a.add(103);
		assertEquals(14, a.size());
		assertEquals(20, sas.capacity());
		scan(a);

		a.add(104);
		assertEquals(15, a.size());
		assertEquals(20, sas.capacity());
		scan(a);
	}

}
