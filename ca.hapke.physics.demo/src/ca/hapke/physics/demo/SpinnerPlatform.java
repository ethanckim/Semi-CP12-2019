package ca.hapke.physics.demo;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import ca.hapke.physics.demo.bodies.StdPhysicsObject;
import jgame.JGColor;

/**
 * @author Mr. Hapke
 *
 */
public class SpinnerPlatform extends StdPhysicsObject {

	public static final int SPINNER_COLISSION = 8;
	public static final String SPINNER_NAME = "Spinner";
	public static final String SPINNER_GRAPHICS = "sPl";
	public static final String TILE = "sp";

	private static final int halfWidth = 64;
	private static final int halfHeight = 8;

	public SpinnerPlatform(World world, double x, double y) {
		super(SPINNER_NAME, SPINNER_GRAPHICS, SPINNER_COLISSION, x, y, world, .5236f, 0f, 0.15f, 0.85f);
	}

	@Override
	protected int getXOffset() {
//		getImageBBox().getWidth() / 2;
		return 0 - getImageBBox().getWidth() / 2;
	}

	@Override
	protected int getYOffset() {
//		return -halfHeight;
		return 0 - getImageBBox().getHeight() / 2;
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
	public void move() {
		super.move();

	}

	@Override
	public void paint() {
		float rotRad = body.getAngle();
		int rotDeg = ((int) Math.toDegrees(rotRad)) % 180;
		setGraphic(SPINNER_GRAPHICS + rotDeg);

		super.paint();
//		int xOffset = getXOffset();
//		int yOffset = getYOffset();
		Transform transform = body.m_xf;

		PolygonShape poly = (PolygonShape) shape;
		for (int i = 0; i < poly.getVertexCount(); i++) {
			Vec2 vertex = poly.m_vertices[i];
//			Vec2 out = new Vec2(vertex);

			Vec2 out = Transform.mul(transform, vertex);
//			Vec2 cen = body.getPosition();

			float xWorld = out.x;
			float yWorld = out.y;
			float xPx = Units.xUnitsToPixels(xWorld);
			float yPx = Units.yUnitsToPixels(yWorld);
			String s = getLocationString(xWorld, yWorld);

			eng.setColor(JGColor.red);
			eng.drawLine(xPx - 2, yPx, xPx + 2, yPx);
			eng.drawLine(xPx, yPx - 2, xPx, yPx + 2);

			eng.setColor(JGColor.green);
			eng.drawString(s, xPx, yPx, 0);
		}
	}

}
