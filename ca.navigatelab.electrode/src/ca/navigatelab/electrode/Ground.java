package ca.navigatelab.electrode;

import jgame.JGObject;

public class Ground extends JGObject {
	public static final String NAME = "grass";
	public static final int COL_ID = 2;
	public static final String TILE = "G";
	

	public Ground(int x, int y) {
		super(NAME, true, Units.xUnitsToPixels(x), Units.yUnitsToPixels(y), COL_ID, NAME);
		
	}
}
