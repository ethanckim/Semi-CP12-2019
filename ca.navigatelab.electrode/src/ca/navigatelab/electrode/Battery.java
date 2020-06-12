package ca.navigatelab.electrode;

import jgame.JGColor;
import jgame.JGObject;

public class Battery extends JGObject {
	
	public static final int COL_ID = 32;
	public static final String NAME = "zbattery";
	public static final String TILE = "B";
	
	public Battery(int x, int y) {
		super(NAME, true, Units.xUnitsToPixels(x), Units.yUnitsToPixels(y), COL_ID, NAME);
		//setBBox(2, 2, 30, 30);
	}
	
	@Override
	public void hit(JGObject obj) {
		super.hit(obj);
		this.remove();
	}

	@Override
	public void paint() {
	}
	
}
