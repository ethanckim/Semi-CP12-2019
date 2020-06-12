package ca.navigatelab.electrode;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.dynamics.World;

import ca.navigatelab.electrode.Units;

public class Walls extends StdPhysicsObject {
	public static final String NAME = "wall";
	public static final int COL_ID = 8;
	public static final String TILE = "W";

	private static final int halfWidth = 16;
	private static final int halfHeight = 16;
	
	
	public Walls(World world, double x, double y) {
		super(NAME, NAME, COL_ID,  x, y, world, 0f, 0f, 0.15f, 0.5f);
	}

	@Override
	protected Shape createShape() {
		PolygonShape sd = new PolygonShape();
		float w2 = Units.xPixelsToUnits(halfWidth);
		float h2 = Units.yPixelsToUnits(halfHeight);
		sd.setAsBox(w2, h2);
		return sd;
	}

	@Override
	protected int getXOffset() {
		return -halfWidth;
	}

	@Override
	protected int getYOffset() {
		return -halfHeight;
	}

}
