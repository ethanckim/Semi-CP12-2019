package ca.hapke.physics.demo.bodies;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

import ca.hapke.physics.demo.Units;

/**
 * Kinematic type -> does not collide with static objects like the platforms,
 * but will collide with the marbles (because they're dynamic).
 * 
 * @author Mr. Hapke
 *
 */
public class Sun extends StdPhysicsObject {

	private static final float INCREMENT_STRENGTH = 0.2f;
	private static final float WINDOW_MARGIN = 100;
	private static final float SLOWDOWN_ZONE = 50;
	private static final Vec2 INCREMENT_LEFT = new Vec2(-INCREMENT_STRENGTH, 0f);
	private static final Vec2 INCREMENT_RIGHT = new Vec2(INCREMENT_STRENGTH, 0f);

	private int step = 0;
	private static final int SPIT_BALL = 25;
	public static final int SUN_COLISSION = 1;
	public static final int SUN_RADIUS = 32;
	public static final String SUN_GRAPHICS = "sun";
	public static final String TILE = "s";

	public Sun(World world, double x, double y) {
		super(SUN_GRAPHICS, SUN_GRAPHICS, SUN_COLISSION, x, y, world, 0f, 0f, 0.15f, 0.85f);
	}

	@Override
	public void move() {
		super.move();
		int pfWidth = eng.pfWidth();
		float leftSide = WINDOW_MARGIN + SLOWDOWN_ZONE;
		Vec2 v1 = body.getLinearVelocity();
		if (x < leftSide || x >= pfWidth - leftSide) {

			Vec2 v2;
			if (x < leftSide) {
				v2 = v1.add(INCREMENT_RIGHT);
			} else {
				v2 = v1.add(INCREMENT_LEFT);
			}
			body.setLinearVelocity(v2);
		}

		step++;
		if (step >= SPIT_BALL) {
			step = 0;

			Vec2 cen = body.getPosition();
			double r = Units.yPixelsToUnits(SUN_RADIUS);
			Neonball ball = new Neonball(world, cen.x, cen.y + r);
			Vec2 v = new Vec2(v1.x, 0);

			Body bb = ball.body;
			bb.applyForce(v, bb.getLocalCenter());
		}

	}

	@Override
	protected Shape createShape() {
		CircleShape shape = new CircleShape();
		shape.m_radius = Units.xPixelsToUnits(SUN_RADIUS);
		return shape;
	}

	@Override
	protected int getXOffset() {
		return -SUN_RADIUS;
	}

	@Override
	protected int getYOffset() {
		return -SUN_RADIUS;
	}
}
