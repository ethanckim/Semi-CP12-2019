package ca.navigatelab.electrode;

public abstract class Units {

	public static final int SIZE = 32;

	public static float xPixelsToUnits(int x) {
		return ((float) x) / SIZE;
	}

	public static float xPixelsToUnits(double x) {
		return (float) (x / SIZE);
	}

	public static float yPixelsToUnits(int y) {
		return ((float) y) / SIZE;
	}

	public static float yPixelsToUnits(double y) {
		return (float) (y / SIZE);
	}

	public static int xUnitsToPixels(float x) {
		return (int) (x * SIZE);
	}

	public static int xUnitsToPixels(double x) {
		return (int) (x * SIZE);
	}

	public static int yUnitsToPixels(float y) {
		return (int) (y * SIZE);
	}

	public static int yUnitsToPixels(double y) {
		return (int) (y * SIZE);
	}
}
