package ca.navigatelab.electrode;

import jgame.JGObject;

public class GoalPost extends JGObject {
	
	

	public static final String NAME = "GoalPost";
	public static final int COL_ID = 16;
	public static final String TILE = "GP";

	public GoalPost(int x, int y) {
		super(NAME, true, Units.xUnitsToPixels(x), Units.yUnitsToPixels(y), COL_ID, NAME);
		clearAnim();
	}

}
