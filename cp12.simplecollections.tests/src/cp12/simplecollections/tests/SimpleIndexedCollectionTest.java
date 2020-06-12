package cp12.simplecollections.tests;

import org.junit.Test;

import cp12.simplecollections.SimpleIndexedCollection;

/**
 * @author Mr. Hapke
 *
 */
public abstract class SimpleIndexedCollectionTest extends SimpleCollectionTest {

	protected SimpleIndexedCollection<Integer> b;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		b = (SimpleIndexedCollection<Integer>) a;
	}

	@Test
	public void testGet() {
		addDefaults(false);
		acceptEither(b.get(0).intValue(), 45, 81);
		acceptEither(b.get(1).intValue(), 37, 65);
		acceptEither(b.get(2).intValue(), 22, 41);
		acceptEither(b.get(3).intValue(), 41, 22);
		acceptEither(b.get(4).intValue(), 65, 37);
		acceptEither(b.get(5).intValue(), 81, 45);
	}

	@Test
	public void testFindIndex() {
		addDefaults(false);
		acceptEither(b.findIndex(45), 0, 5);
		acceptEither(b.findIndex(37), 1, 4);
		acceptEither(b.findIndex(22), 2, 3);
		acceptEither(b.findIndex(41), 3, 2);
		acceptEither(b.findIndex(65), 4, 1);
		acceptEither(b.findIndex(81), 0, 5);

		assertEquals(-1, b.findIndex(-37));
	}

	@Test
	public void testFindIndexFromIndex() {
		addDefaults(false);
		acceptEither(b.findIndex(41), 3, 2);
		acceptEither(b.findIndex(41, 2), 3, 2);
		assertEquals(-1, b.findIndex(41, 4));
	}

	@Test
	public void testFindIndexWithRemoval() {
		addDefaults(false);
		acceptEither(b.findIndex(65), 4, 1);
		acceptEither(b.findIndex(81), 0, 5);

		b.remove(65);

		assertEquals(-1, b.findIndex(65));
		acceptEither(b.findIndex(81), 0, 4);
	}
}
