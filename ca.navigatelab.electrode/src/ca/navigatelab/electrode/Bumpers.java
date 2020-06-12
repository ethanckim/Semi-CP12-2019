package ca.navigatelab.electrode;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.dynamics.World;

public class Bumpers extends StdPhysicsObject {

	public static final String NAME = "bumper";
	public static final int COL_ID = 4;
	public static final String TILE = "Bu";

	private static final int halfWidth = 16;
	private static final int halfHeight = 16;
	
	public Bumpers(World world, double x, double y) {
		super(NAME, NAME, COL_ID, x, y, world, 0f, 0f, 0f, 2f);
		// TODO Auto-generated constructor stub
	}
	
	
	protected Shape createShape() {
		PolygonShape sd = new PolygonShape();
		float w2 = Units.xPixelsToUnits(halfWidth);
		float h2 = Units.yPixelsToUnits(halfHeight);
		sd.setAsBox(w2, h2);
		return sd;
	}


	protected int getXOffset() {
		return -halfWidth;
	}

	
	protected int getYOffset() {
		return -halfHeight;
	}

}
