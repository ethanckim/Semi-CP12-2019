package ca.kim.game.test;

import jgame.JGColor;
import jgame.JGObject;

public class Koopa extends JGObject {

	private static final int BBOX_X = 9;
	private static final int BBOX_Y = 5;
	private static final int BBOX_WIDTH = 20;
	private static final int BBOX_HEIGHT = 23;
	
	public static final int COL_ID = 2;
	public static final String NAME = "koopa";
	
	public Koopa(int x, int y) {
		super(NAME, true, x, y, COL_ID, NAME);
		setBBox(BBOX_X, BBOX_Y, BBOX_WIDTH, BBOX_HEIGHT);
	}

	public void paint() {
		eng.setColor(JGColor.green);
		eng.drawRect(x+BBOX_X, y+BBOX_Y, BBOX_WIDTH, BBOX_HEIGHT, false, false);
	}
	
}
