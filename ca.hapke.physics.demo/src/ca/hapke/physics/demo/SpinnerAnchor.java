package ca.hapke.physics.demo;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import ca.hapke.physics.demo.bodies.StdPhysicsObject;
import jgame.JGColor;

/**
 * @author Mr. Hapke
 *
 */
public class SpinnerAnchor extends StdPhysicsObject {

	public static final int SPINNER_COLISSION = 16;
	public static final String SPINNER_NAME = "SpinnerAnchor";
//	public static final String SPINNER_GRAPHICS = "sAn";
	public static final String TILE = "sa";
	private SpinnerPlatform sp;

	private static final int halfWidth = 8;
	private static final int halfHeight = 8;

	public SpinnerAnchor(World world, double x, double y, SpinnerPlatform sp) {
		super(SPINNER_NAME, null, SPINNER_COLISSION, x, y, world, 0.15f, 0.85f);
		this.sp = sp;

		float xPx = Units.xUnitsToPixels((float) x);
		float yPx = Units.yUnitsToPixels((float) y);
		setBBox((int) xPx - halfWidth, (int) yPx - halfHeight, halfWidth * 2, halfHeight * 2);
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

	@Override
	public void paint() {
		eng.setColor(JGColor.red);
		Vec2 cen = body.getPosition();
		double xPx = Units.xUnitsToPixels(cen.x);
		double yPx = Units.yUnitsToPixels(cen.y);
		eng.drawRect(xPx, yPx, halfWidth * 2, halfHeight * 2, false, true);

		int l = 20;

		eng.drawLine(xPx - l, yPx, xPx + l, yPx);
		eng.drawLine(xPx, yPx - l, xPx, yPx + l);

	}
}
