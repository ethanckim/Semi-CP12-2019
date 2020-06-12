package ca.kim.game.test;

import jgame.JGColor;
import jgame.JGObject;

public class Mario extends JGObject {
	
	private static final int BBOX_X = 6;
	private static final int BBOX_Y = 5;
	private static final int BBOX_WIDTH = 20;
	private static final int BBOX_HEIGHT = 23;
	
	public static final int COL_ID = 1;
	public static final String NAME = "mario";
	
	public Mario(int x, int y) {
		super(NAME, true, x, y, COL_ID, NAME);
		setBBox(BBOX_X, BBOX_Y, BBOX_WIDTH, BBOX_HEIGHT);
	}
	
	public void paint() {
		eng.setColor(JGColor.green);
		eng.drawRect(x+BBOX_X, y+BBOX_Y, BBOX_WIDTH, BBOX_HEIGHT, false, false);
	}

	@Override
	public void hit(JGObject obj) {
		eng.setColor(JGColor.white);
		System.out.println("HIT");
		if (obj.colid == Bowser.COL_ID) {
			System.out.println("You lost a life!");
		}
	}

	
	
}
