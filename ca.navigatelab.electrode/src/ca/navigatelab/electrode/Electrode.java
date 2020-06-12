package ca.navigatelab.electrode;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.dynamics.World;

import jgame.JGObject;
import jgame.JGRectangle;
import jgame.platform.StdGame;

public class Electrode extends StdPhysicsObject {

	public static final String NAME = "zelectrode";
	public static final int COL_ID = 1;
	public static final String TILE = "p1";
	public static final int ELEC_RADIUS = 30;
	
	/**
	 * The lower the number, the faster the animation.
	 */
	private static final int ANIMATION_SPEED = 6;
	
	private int deg;

	public Electrode(World w, double x, double y) {
		super(NAME, NAME, COL_ID, x, y, w, 0f, 10f, 0.4f, 0.75f);
		setBBox(12, 12, 52, 52);
	}

	public void move() {
		super.move();
				
		//Find xspeed and yspeed.
		String currentVelocity = this.body.getLinearVelocity().toString();
		double p1Xspeed = Double.parseDouble(currentVelocity.substring(currentVelocity.indexOf("(") + 1, currentVelocity.indexOf(",")));
		double p1Yspeed = Double.parseDouble(currentVelocity.substring(currentVelocity.indexOf(",") + 1, currentVelocity.indexOf(")")));
		
		// Animation and angles
		Double theta = Math.atan(-p1Yspeed / p1Xspeed);
		deg = (int) Math.toDegrees(theta);
		if (p1Xspeed > 0 && -p1Yspeed > 0) {
			deg -= 90;
			if (deg < 0)
				deg += 360;
		} else if (p1Xspeed < 0 && -p1Yspeed > 0) {
			deg = 90 + deg;
		} else if (p1Xspeed < 0 && -p1Yspeed < 0) {
			deg += 90;
		} else if (p1Xspeed > 0 && -p1Yspeed < 0) {
			deg = 270 + deg;
		} else if (p1Xspeed > 0 && -p1Yspeed == 0) {
			deg = 270;
		} else if (p1Xspeed < 0 && -p1Yspeed == 0) {
			deg = 90;
		} else if (p1Xspeed == 0 && -p1Yspeed > 0) {
			deg = 360;
		} else if (p1Xspeed == 0 && -p1Yspeed < 0) {
			deg = 180;
		}

		if (deg != 0) {
			int currentFrame = 0;
			if (anim != null) {
				currentFrame = anim.framenr;
			} else {
				System.out.println("Before");
			}

			if (deg == 360)
				setAnim(Electrode.NAME + 0);
			else
				setAnim(Electrode.NAME + deg);

			if (anim != null) {
				anim.framenr = currentFrame;
			} else {
				System.out.println("After");
			}
		}
		setAnimSpeed((Math.abs(p1Xspeed) > Math.abs(p1Yspeed) ? Math.abs(p1Xspeed) : Math.abs(p1Yspeed)) / ANIMATION_SPEED);
	}

	@Override
	protected Shape createShape() {
		CircleShape shape = new CircleShape();
		shape.m_radius = Units.xPixelsToUnits(ELEC_RADIUS);
		return shape;
	}

	@Override
	protected int getXOffset() {
		return -ELEC_RADIUS;
	}

	@Override
	protected int getYOffset() {
		return -ELEC_RADIUS;
	}

	@Override
	public void hit(JGObject obj) {
		System.out.println(obj.colid);
	}

}
