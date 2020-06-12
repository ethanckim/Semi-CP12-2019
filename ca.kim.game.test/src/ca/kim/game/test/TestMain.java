/**
 * 
 */
package ca.kim.game.test;

import java.awt.event.KeyEvent;

import javax.print.attribute.standard.MediaSize.Engineering;

import jgame.JGColor;
import jgame.platform.StdGame;

/**
 * @author ethanc.kim
 *
 */
public class TestMain extends StdGame {

	private static final int X_TILES = 40;
	private static final int Y_TILES = 30;
	private static final int TILE_SIZE = 16;
	private Mario m;
	private Koopa k;
	private Mushroom i;
	private Bowser b;

	public TestMain() {
		initEngine(X_TILES * TILE_SIZE, Y_TILES * TILE_SIZE);
	}

	public static void main(String[] args) {
		new TestMain();
		
	}
	
	@Override
	public void initCanvas() {
		setCanvasSettings(X_TILES, Y_TILES, TILE_SIZE, TILE_SIZE, JGColor.cyan, JGColor.orange, null);
	}

	@Override
	public void initGame() {
		setFrameRate(60,2);
		defineImage(Mario.NAME, "m", Mario.COL_ID, "mario_32.png", "-");
		defineImage(Koopa.NAME, "k", Koopa.COL_ID, "koopa_32.png", "-");
		defineImage(Mushroom.NAME, "i", Mushroom.COL_ID, "mushroom_32.png", "-");
		defineImage(Bowser.NAME, "b", Bowser.COL_ID, "bowser.png", "-");
	}

	@Override
	public void initNewLife() {
		super.initNewLife();
		m = new Mario(30, 100);
		k = new Koopa(30, 130);
		i = new Mushroom(200, 200);
		b = new Bowser(300, 300, m, k);
		
	}

	@Override
	public void doFrame() {
		super.doFrame();
		if (inGameState("InGame")) {
			if (getKey(key_left)) {
				clearKey(key_left);
				m.xspeed = 5;
				m.xdir = -1;
			} else if (getKey(key_right)) {
				clearKey(key_right);
				m.xspeed = 5;
				m.xdir = 1;
			} else {
				m.xspeed = 0;
				m.xdir = 0;
			}
			if (getKey(key_up)) {
				clearKey(key_up);
				m.yspeed = 5;
				m.ydir = -1;
			} else if (getKey(key_down)) {
				clearKey(key_down);
				m.yspeed = 5;
				m.ydir = 1;
			} else {
				m.yspeed = 0;
				m.ydir = 0;
			}
			
			if (getKey('A')) {
				clearKey('A');
				k.xspeed = 5;
				k.xdir = -1;
			} else if (getKey('D')) {
				clearKey('D');
				k.xspeed = 5;
				k.xdir = 1;
			} else {
				k.xspeed = 0;
				k.xdir = 0;
			}
			if (getKey('W')) {
				clearKey('W');
				k.yspeed = 5;
				k.ydir = -1;
			} else if (getKey('S')) {
				clearKey('S');
				k.yspeed = 5;
				k.ydir = 1;
			} else {
				k.yspeed = 0;
				k.ydir = 0;
			}

		}
		
		moveObjects();
		
		checkCollision(Koopa.COL_ID, Mario.COL_ID);
		checkCollision(Bowser.COL_ID, Mario.COL_ID);
	}
}
