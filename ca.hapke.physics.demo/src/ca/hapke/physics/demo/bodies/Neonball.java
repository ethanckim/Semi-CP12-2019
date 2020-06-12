package ca.hapke.physics.demo.bodies;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.dynamics.World;

import ca.hapke.physics.demo.Units;

/**
 * @author Mr. Hapke
 *
 */
public class Neonball extends StdPhysicsObject {

	public static final int NEON_RADIUS = 6;
	public static final int NEON_COLISSION = 2;
	public static final String NEONBALL_NAME = "neonball";
	public static final String NEONBALL_GRAPHICS = "neon";
	public static final String NEONBALL_TILE = "n";

	public Neonball(World w, double x, double y) {
		super(NEONBALL_NAME, NEONBALL_GRAPHICS, NEON_COLISSION, x, y, w, 0f, 6.0f, 1.2f, 0.85f);
		expiry = expire_off_view;
	}

	@Override
	protected Shape createShape() {
		CircleShape shape = new CircleShape();
		shape.m_radius = Units.xPixelsToUnits(NEON_RADIUS);
		return shape;
	}

	@Override
	protected int getXOffset() {
		return -NEON_RADIUS;
	}

	@Override
	protected int getYOffset() {
		return -NEON_RADIUS;
	}

}
