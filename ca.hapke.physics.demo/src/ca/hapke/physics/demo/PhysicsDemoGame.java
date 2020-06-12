package ca.hapke.physics.demo;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import ca.hapke.physics.demo.bodies.Neonball;
import ca.hapke.physics.demo.bodies.Platform;
import ca.hapke.physics.demo.bodies.Sun;
import jgame.platform.StdGame;

/**
 * @author Mr. Hapke
 *
 */
public class PhysicsDemoGame extends StdGame {
	private static final int FPS = 45;
	private static final int TILES_X = 40;
	private static final int TILES_Y = 50;
	private static final float WORLD_MARGIN = 1;
	private static final float GRAVITY_STRENGTH = 9.8f;

	public static void main(String[] args) {
		new PhysicsDemoGame();
	}

	private Vec2 gravity;
	private World world;
	private Sun sun;
	private SpinnerPlatform sp1;
	private SpinnerAnchor sa1;

	public PhysicsDemoGame() {
		initEngine(TILES_X * Units.SIZE, TILES_Y * Units.SIZE);
	}

	@Override
	public void initCanvas() {
		setCanvasSettings(TILES_X, TILES_Y, Units.SIZE, Units.SIZE, null, null, null);
//		setCanvasSettings(TILES_X, TILES_Y, Units.SIZE, Units.SIZE, JGColor.black, JGColor.white, null);

	}

	@Override
	public void initGame() {
		setFrameRate(FPS, 3);

		defineImage(Sun.SUN_GRAPHICS, Sun.TILE, Sun.SUN_COLISSION, "sun_64.png", "-");
		defineImage(Neonball.NEONBALL_GRAPHICS, Neonball.NEONBALL_TILE, Neonball.NEON_COLISSION,
				"bullet_ball_pink12.png", "-");
		defineImage(Platform.PLATFORM_GRAPHICS, Platform.TILE, Platform.PLATFORM_COLISSION, "platform_96_32.png", "-");
		defineImage(SpinnerPlatform.SPINNER_GRAPHICS, SpinnerPlatform.TILE, SpinnerPlatform.SPINNER_COLISSION,
				"platform_128_16.png", "-");
		for (int i = 0; i < 180; i++) {
			double angle = Math.toRadians(i);
			defineImageRotated(SpinnerPlatform.SPINNER_GRAPHICS + i, SpinnerPlatform.TILE,
					SpinnerPlatform.SPINNER_COLISSION, SpinnerPlatform.SPINNER_GRAPHICS, angle);
		}
	}

	private World createWorld(float lowerx, float lowery, float upperx, float uppery) {

		gravity = new Vec2(0, GRAVITY_STRENGTH);
		World w = new World(gravity, true);
		return w;
	}

	@Override
	public void defineLevel() {
		world = createWorld(-WORLD_MARGIN, -WORLD_MARGIN, TILES_X + WORLD_MARGIN, TILES_Y + WORLD_MARGIN);
		world.setContactListener(new PhysicsDemoContactListener(this));
		world.setContactFilter(new PhysicsDemoContactFilter());

		sun = new Sun(world, 8.25, 9.5);

		Platform p1 = new Platform(world, 14, 17);
		sp1 = new SpinnerPlatform(world, 24, 30);
		sa1 = new SpinnerAnchor(world, 24, 30, sp1);
	}

	@Override
	public void doFrame() {
		super.doFrame();

		if (inGameState("InGame")) {
//			s1.body.applyTorque(10);
//			s1.body.getPosition().
			float timeStep = 1.0f / FPS;

			world.step(timeStep, 6, 2);

			moveObjects(null, 0);
		}
	}
}
