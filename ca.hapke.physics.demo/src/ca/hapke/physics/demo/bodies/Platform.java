package ca.hapke.physics.demo.bodies;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.dynamics.World;

import ca.hapke.physics.demo.Units;

/**
 * @author Mr. Hapke
 *
 */
public class Platform extends StdPhysicsObject {

	public static final int PLATFORM_COLISSION = 4;
	public static final String PLATFORM_NAME = "platform";
	public static final String PLATFORM_GRAPHICS = "plat";
	public static final String TILE = "p";

	private static final int halfWidth = 48;
	private static final int halfHeight = 16;

	public Platform(World world, double x, double y) {
		super(PLATFORM_NAME, PLATFORM_GRAPHICS, PLATFORM_COLISSION, x, y, world, 0f, 0f, 0.15f, 0.85f);
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
