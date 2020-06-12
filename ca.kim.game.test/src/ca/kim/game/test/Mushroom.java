package ca.kim.game.test;

import jgame.JGObject;

public class Mushroom extends JGObject {

	public static final int COL_ID = 4;
	public static final String NAME = "mushroom";
	
	public Mushroom(int x, int y) {
		super(NAME, true, x, y, COL_ID, NAME);
	}

}
