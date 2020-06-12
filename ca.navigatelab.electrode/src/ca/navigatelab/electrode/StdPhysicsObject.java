package ca.navigatelab.electrode;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.jbox2d.collision.shapes.MassData;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import ca.navigatelab.electrode.Units;
import jgame.JGColor;
import jgame.JGObject;

/**
 * @author Mr. Hapke
 *
 */
public abstract class StdPhysicsObject extends JGObject {

	protected final NumberFormat nf = new DecimalFormat("#.##");
	protected final static boolean textOutput = true;

	protected final World world;
	protected final Shape shape;
	protected final BodyDef bodydef;
	protected final Body body;
	protected final Fixture fixture;

	protected float rot;
	protected float mass;
	protected float friction;
	protected float restitution;

	/**
	 * Static: Doesn't move.
	 */
	public StdPhysicsObject(String name, String gfxname, int colid, double x, double y, World w, double friction,
			double restitution) {
		super(name, true, Units.xUnitsToPixels(x), Units.yUnitsToPixels(y), colid, gfxname);
		this.world = w;
		this.rot = 0f;
		this.mass = 0f;
		this.friction = (float) friction;
		this.restitution = (float) restitution;

		shape = createShape();
		bodydef = createBodyDef(BodyType.STATIC, x, y, rot);

		body = world.createBody(bodydef);
		fixture = body.createFixture(shape, 0);

		// for getting the JGObject back from the body. Useful in ContactListeners
		body.setUserData(this);
	}

	/*-
	 * Kinematic/Dynamic
	 * Kinematic: You can move the object around manually
	 * Dynamic:   Fully simulated by the physics engine.
	 */
	public StdPhysicsObject(String name, String gfxname, int colid, double x, double y, World w, float rot, float mass,
			float friction, float restitution) {
		super(name, true, Units.xUnitsToPixels(x), Units.yUnitsToPixels(y), colid, gfxname);
		this.world = w;
		this.rot = rot;

		shape = createShape();
		BodyType type;
		if (mass == 0) {
			type = BodyType.KINEMATIC;
		} else {
			type = BodyType.DYNAMIC;
		}
		bodydef = createBodyDef(type, x, y, rot);
		FixtureDef fd = createMaterial(shape, mass, friction, restitution);

		body = world.createBody(bodydef);
		if (mass > 0) {
			MassData massData = new MassData();
			massData.mass = mass;
			shape.computeMass(massData, mass);
		}
		fixture = body.createFixture(fd);

		body.setUserData(this); // for following body back to JGObject
	}

	protected abstract Shape createShape();

	protected BodyDef createBodyDef(BodyType t, double x, double y, float rot) {
		BodyDef bodydef = new BodyDef();
		bodydef.type = t;
		float x2 = (float) x;
		float y2 = (float) y;
		bodydef.position.set(x2, y2);
		bodydef.angle = rot;
		return bodydef;
	}

	protected FixtureDef createMaterial(Shape shape, float mass, float friction, float restitution) {
		this.mass = mass;
		this.friction = friction;
		this.restitution = restitution;

		FixtureDef fd = new FixtureDef();
		fd.shape = shape;
		fd.friction = friction;
		fd.restitution = restitution;

		return fd;
	}

	/**
	 * Make JGame position and angle track physics position. Also check if object is
	 * still in actual world. If not, destroy.
	 */
	@Override
	public void move() {
		Vec2 position = body.getPosition();
		rot = -body.getAngle();
		x = Units.xUnitsToPixels(position.x) + getXOffset();
		y = Units.yUnitsToPixels(position.y) + getYOffset();
		if (body.m_world != world)
			remove();
	}

	protected abstract int getXOffset();

	protected abstract int getYOffset();

	@Override
	public void destroy() {
		// body may not be in actual world. If not, do not call destroyBody.
		if (body.m_world == world) {
			// also destroys associated joints
			world.destroyBody(body);
		}
	}
/*
	@Override
	public void paint() {
		super.paint();
		if (textOutput) {
			eng.setColor(JGColor.white);
			// subtracting brings us to the centre
			eng.drawString(getLocationString(), x - getXOffset(), y - getYOffset(), 0);
		}
	}
*/
	public String getLocationString() {
		Vec2 cen = body.getPosition();
		String xy = getLocationString(x, y) + ":" + getLocationString(cen.x, cen.y);
		String th = Math.abs(rot) > 0.01 ? " th=" + rot : "";
		return xy + th;
	}

	public String getLocationString(double x, double y) {
		return "(" + nf.format(x) + "," + nf.format(y) + ")";
	}
}
