package ca.kim.game.test;

import jgame.JGColor;
import jgame.JGObject;
import jgame.RectangleData;

public class Bowser extends JGObject {

	public static final int COL_ID = 8;
	public static final String NAME = "bowser";
	private Mario m;
	private Koopa k;
	
	public Bowser(int x, int y, Mario m, Koopa k) {
		super(NAME, true, x, y, COL_ID, NAME);
		this.m = m;
		this.k = k;
		
		setBBoxMulti(
				new RectangleData(5, 55, 28, 33), //Left Arm
				new RectangleData(33, 15, 62, 100), //Body
				new RectangleData(95, 42, 26, 33)); //Right Arm
		
		xdir = -1;
		ydir = -1;
	}

	public void paint() {
		eng.setColor(JGColor.green);
		eng.drawRect(x+5, y+55, 28, 33, false, false);
		eng.drawRect(x+33, y+15, 62, 100, false, false);
		eng.drawRect(x+95, y+42, 26, 33, false, false);
		
		eng.setColor(JGColor.red);
		eng.drawRect(x, y, 128, 128, false, false);
	}

	@Override
	public void move() {
		double mx = this.x + 44 - m.x;
		double my = this.y + 44 - m.y;
		double mhyp = Math.sqrt(mx*mx + my*my);
		
		double kx = this.x + 44 - k.x;
		double ky = this.y + 44 - k.y;
		double khyp = Math.sqrt(kx*kx + ky*ky);
		
		if (mhyp < khyp) {
			//Attack Mario
			xspeed = mx / mhyp;
			yspeed = my / mhyp;
		} else {
			//Attack Koopa
			xspeed = kx / khyp;
			yspeed = ky / khyp;
		}
		
		//Stack Overflow's approch
//        float angle = (float)Math.atan2(m.y - y, m.x - x);
//
//        xspeed = Math.cos(angle) * 1.5;
//        yspeed = Math.sin(angle) * 1.5;
//
//		xdir = 1;
//		ydir = 1;
	}
	
	
	
}
