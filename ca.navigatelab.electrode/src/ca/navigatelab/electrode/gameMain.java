package ca.navigatelab.electrode;

import java.text.NumberFormat;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import ca.hapke.net.udp.IUdpServerListener;
import jgame.JGColor;
import jgame.JGFont;
import jgame.JGTimer;
import jgame.platform.StdGame;

/**
 * @author ethanc.kim
 * @author geo
 *
 */
public class gameMain extends StdGame {
	private static final int FPS = 30;
	private static final int X_TILES = 40;
	private static final int Y_TILES = 30;
	private static final float GRAVITY_STRENGTH = 0;
	private static final int WORLD_MARGIN = 1;
	private static final int PORT = 8002;

	/**
	 * The lower the value, the more sensitive the gyro is
	 */
	private static final double GYRO_SENSITIVITY = 10;

	/**
	 * The lower the value, less friction force is applied.
	 */
	private static final float P1_FRICTION_FORCE = (1 / 2f);

	/**
	 * the lower the value, less applied force is applied.
	 */
	private static final int P1_APPLIED_FORCE = 10;

	/**
	 * Charge gauge. Is increased by collecting batteries.
	 */
	private static final int P1_CHARGE = 0;

	/**
	 * the lower the value, less boost force is applied when batteries are
	 * collected.
	 */
	private static final int P1_BOOST_FORCE = 20;

	private Vec2 gravity;
	private World world;
	private Walls w;
	private Ground g;
	private GoalPost gp;
	private Electrode p1;
	private NumberFormat nf;
	private boolean boost = false;

	private double gyroinity = 0;
	private double gyroinitx = 0;
	
	private int min = 0;
	private double sec = 0;
	private JGTimer time;

//looking cool Kobe - Ethan Kim 2019/06/06
//Thanks Kobe! Best friends

	/**
	 * @param args Main method of the game. Creates a new main game object, and
	 *             starts up the game.
	 */
	public static void main(String[] args) {
		new gameMain();
	}

	/**
	 * Constructor. Calls initEngine();
	 */
	public gameMain() {
		initEngine(X_TILES * Units.SIZE, Y_TILES * Units.SIZE);
	}

	@Override
	public void initCanvas() {
		setCanvasSettings(X_TILES, Y_TILES, Units.SIZE, Units.SIZE, JGColor.cyan, JGColor.orange, null);
	} // x = 640, y = 480

	@Override
	public void initGame() {
		setFrameRate(60, 2);
		
		// set Gyro
		startAccelGyroFromUdp(PORT, new IUdpServerListener() {
			@Override
			public void serverOnline() {
				System.out.println("RPi Connected");
			}

			@Override
			public void serverOffline() {
				System.out.println("RPi Disconnected");
			}

			@Override
			public void serverAbort(String msg) {
			}

			@Override
			public void sentenceReceived(String sentence) {
			}

			@Override
			public void accelGyroUpdated() {
			}
		});

		nf = NumberFormat.getInstance();
		nf.setMinimumIntegerDigits(3);
		nf.setMaximumIntegerDigits(3);
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);

		// Load all 21 images
		// Name for each og frame is "electrode + [frame #]"
		for (int i = 1; i <= 21; i++) {
			defineImage(Electrode.NAME + i, Electrode.TILE, Electrode.COL_ID, "Elec_" + i + ".png", "-");
		}

		// Load 21 * 360 rotated images
		// Name for each rotated image is "electode + [frame #] + r + [rotation in
		// degrees]"
		for (int i = 0; i < 360; i++) {
			double rad = -Math.toRadians(i);
			for (int j = 1; j <= 21; j++) {
				defineImageRotated(Electrode.NAME + j + "r" + i, Electrode.TILE, Electrode.COL_ID, Electrode.NAME + j,
						rad);
			}
		}

		// Set these 21 images to a string array for frames
		for (int deg = 0; deg < 360; deg++) {
			String[] p1Frames = new String[21];
			for (int j = 1; j <= 21; j++) {
				p1Frames[j - 1] = Electrode.NAME + j + "r" + deg;
			}
			defineAnimation(Electrode.NAME + deg, p1Frames, 1);
		}

		//goalpost
		defineImage(GoalPost.NAME + 1, "GP", GoalPost.COL_ID, "GoalPost_1.png", "-");
		defineImage(GoalPost.NAME + 2, "GP", GoalPost.COL_ID, "GoalPost_2.png", "-");
		defineImage(GoalPost.NAME + 3, "GP", GoalPost.COL_ID, "GoalPost_3.png", "-");
		String[] goalFrames = {GoalPost.NAME + 1,GoalPost.NAME + 2,GoalPost.NAME + 3};
		defineAnimation(GoalPost.NAME, goalFrames, 0.1);
		
		defineImage(Walls.NAME, "W", Walls.COL_ID, "Brick Wall.png", "-");
		defineImage(Ground.NAME, "G", Ground.COL_ID, "Grass_tile.png", "-");
		defineImage(Bumpers.NAME, "Bu", Bumpers.COL_ID, "Bumpers_new.png", "-");
		defineImage(Battery.NAME, "B", Battery.COL_ID, "Battery.png", "-");
		
		setPFSize(X_TILES * 4, Y_TILES * 4);
		setPFWrap(false, false, -10, -10);
		
		leveldone_ticks = 240;
		startgame_ticks = 120;

		defineAudioClip("BounceAudio", "Cartoon Sound Effect - Bounce.wav");
		
		//BG Image
		defineImage("BG", "bg", 256, "World_Map_refined.png", "-");
		setBGImage("BG");
	}

	private World createWorld(float lowerx, float lowery, float upperx, float uppery) {

		gravity = new Vec2(0, GRAVITY_STRENGTH);
		World w = new World(gravity, true);
		return w;
	}

	@Override
	public void initNewLife() {

		// Spawn player 1 at a certain location depending on level
		if (level == 0) {
			if (p1 != null)
				p1.remove();
			p1 = new Electrode(world, 15, 45);
			p1.setImage(Electrode.NAME + 1);
		} else if (level == 1) {
			if (p1 != null)
				p1.remove();
			p1 = new Electrode(world, 15, 45);
			p1.setImage(Electrode.NAME + 1);
		} else if (level == 2) {
			if (p1 != null)
				p1.remove();
			p1 = new Electrode(world, 15, 45);
			p1.setImage(Electrode.NAME + 1);
		} else if (level == 3) {
			if (p1 != null)
				p1.remove();
			p1 = new Electrode(world, 15, 45);
			p1.setImage(Electrode.NAME + 1);
		} else if (level == 4) {
			if (p1 != null)
				p1.remove();
			p1 = new Electrode(world, 12, 15);
			p1.setImage(Electrode.NAME + 1);
		}
	}

	@Override
	public void startInGame() {
		// TODO Auto-generated method stub
		super.startInGame();
	}

	@Override
	public void startStartLevel() {
		System.out.println("Calibrated");
		gyroinitx = getGyroAngleX();
		gyroinity = getGyroAngleY();
	}

	@Override
	public void paintFrameTitle() {
		drawString("Electrode the Game", viewWidth() / 2, viewHeight() / 3, 0, new JGFont("Calibri", 0, 48),
				title_color);
		drawString("Press " + getKeyDesc(key_startgame) + " to start", viewWidth() / 2, 6 * viewHeight() / 10, 0,
				new JGFont("Calibri", 0, 36), title_color);
		// TODO
		// setBGImage(Electrode.NAME + 1);
	}

	@Override
	public void paintFramePaused() {
		// TODO Auto-generated method stub
		super.paintFramePaused();
	}

	@Override
	public void paintFrameStartLevel() {
		
		drawString("Gyro Callibrated.", viewWidth() / 2, viewHeight() / 1.25, 0, new JGFont("Calibri", 0, 30), new JGColor(135,206,235));

		drawString("Level " + (stage + 1), viewWidth() / 2, 3 * viewHeight() / 5, 0, new JGFont("Calibri", 0, 25),
				title_color);

	}

	@Override
	public void paintFrameStartGame() {
		drawString("Ready. Set. And Begin!", viewWidth() / 2, viewHeight() / 3, 0, new JGFont("Calibri", 0, 35), new JGColor(255,215,0));
	}

	@Override
	public void paintFrameLevelDone() {
		drawString("Level Finished!", viewWidth() / 2, viewHeight() / 3, 0, new JGFont("Calibri", 0, 50), new JGColor(255,215,0));
		drawString("Please leave the gyro flat, as calibration is about to begin.", viewWidth() / 2,  4 * viewHeight() / 5, 0, new JGFont("Calibri", 0, 25), title_color);
	}

	@Override
	public void paintFrameLifeLost() {
		drawString("Fall Out!", viewWidth() / 2,  viewHeight() / 2, 0, new JGFont("Calibri", 0, 50), new JGColor(255,69,0));
	}

	@Override
	public void paintFrameGameOver() {
		// TODO Auto-generated method stub
		super.paintFrameGameOver();
	}
	
	

	@Override
	public void defineLevel() {
		world = createWorld(-WORLD_MARGIN, -WORLD_MARGIN, X_TILES + WORLD_MARGIN, Y_TILES + WORLD_MARGIN);
		world.setContactListener(new PhysicsDemoContactListener(this));
		world.setContactFilter(new PhysicsDemoContactFilter());
		removeObjects(null, 0);
		// ********************LEVEL 1*****************
		if (level == 0) {
			// create some tiles. "W" is walls, "G" is ground.
			char[][] level1Map = {
					{ 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w',
							'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w',
							'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'f' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'f' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'f' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'f' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'b', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'b', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'b', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'b', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'f' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'b', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'b', 'g', 'g', 'g', 'g', 'g', 'g', 'b', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'b', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'f' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'f' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'f' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'f' },

					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'f' },
					{ 'w', 'g', 'g', 'g', 'b', 'g', 'g', 'g', 'g', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w',
							'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w',
							'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'b', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
					{ 'w', 'g', 'g', 'g', 'b', 'g', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },

					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'b', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
					{ 'w', 'g', 'g', 'g', 'b', 'g', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'b', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },

					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
					{ 'w', 'g', 'g', 'g', 'b', 'g', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
					{ 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' } };

			for (int i = 0; i < 40; i++) { // y
				for (int j = 0; j < 50; j++) { // x
					switch (level1Map[i][j]) {
					case 'w':
						new Walls(world, j + 10.5, i + 10.5);
						break;
					case 'g':
						new Ground(j + 10, i + 10);
						break;
					case 'f':
						new GoalPost(j + 10, i + 10);
						break;
					case 'j':
						new Bumpers(world, j + 10.5, i + 10.5);
						break;
					case '-':
						break;
					case 'b':
						new Ground(j + 10, i + 10);
						new Battery(j + 10, i + 10);
						break;
					}
				}
			}

			// ********************LEVEL 2*****************
		} else if (level == 1) {
			if (p1 != null)
				p1.remove();
			p1 = new Electrode(world, 15, 45);
			p1.setImage(Electrode.NAME + 1);
			char[][] level2Map = {
					{ 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w',
							'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w',
							'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'f' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'f' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'f' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'f' },
					{ 'w', 'g', 'g', 'g', 'g', 'b', 'g', 'g', 'g', 'g', 'b', 'g', 'g', 'g', 'g', 'g', 'b', 'g', 'g',
							'w', 'w', 'g', 'g', 'g', 'g', 'g', 'b', 'g', 'g', 'g', 'g', 'g', 'b', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', 'w', 'g', 'g', 'g', 'b', 'g', 'g', 'g', 'g', 'f' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'b', 'g', 'f' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'f' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'f' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'f' },

					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'g', 'g', 'b', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'g', 'g', 'b', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'b', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w' },

					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'b', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'b', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', 'w', 'g', 'b', 'g', 'g', 'g', 'g', 'g', 'g', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w' },

					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'b', 'g', 'g', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'b', 'g', 'g', 'g', 'g', 'g', 'g', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w',
							'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w',
							'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w' } };

			for (int i = 0; i < 40; i++) { // y
				for (int j = 0; j < 50; j++) { // x
					switch (level2Map[i][j]) {
					case 'w':
						new Walls(world, j + 10.5, i + 10.5);
						break;
					case 'g':
						new Ground(j + 10, i + 10);
						break;
					case 'f':
						new GoalPost(j + 10, i + 10);
						break;
					case 'j':
						new Bumpers(world, j + 10.5, i + 10.5);
						break;
					case '-':
						break;
					case 'b':
						new Ground(j + 10, i + 10);
						new Battery(j + 10, i + 10);
						break;
					}
				}
			}
			// ********************LEVEL 3*****************
		} else if (level == 2) {
			if (p1 != null)
				p1.remove();
			p1 = new Electrode(world, 15, 45);
			p1.setImage(Electrode.NAME + 1);
			char[][] level3Map = {
					{ 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w',
							'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w',
							'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'f' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'f' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'f' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'f' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'f' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'f' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'f' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'f' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'f' },

					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'f' },
					{ 'w', 'g', 'g', 'g', 'b', 'g', 'g', 'g', 'g', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w',
							'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w',
							'w', 'w', 'w', 'w', '-', '-', '-', 'g', 'b', 'g', '-', '-', 'f' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', 'w', '-', '-', '-', 'g', 'b', 'g', '-', '-', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', 'j', 'g', 'g', 'g', 'g', 'g', 'b', 'g', 'g', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', 'j', 'g', 'g', 'g', 'g', 'g', 'g', 'b', 'g', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', 'j', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'b', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', 'j', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', '-', '-', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-' },

					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w',
							'w', 'w', 'w', 'j', 'j', 'j', 'j', 'j', 'j', 'w', 'w', 'j', 'j', 'j', 'w', '-', '-', '-',
							'-', 'w', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'j', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'b', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'b', 'g',
							'g', 'g', 'b', 'g', 'g', 'g', 'b', 'g', 'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'b', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'b', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'j' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'j' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'j', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'b', 'g', 'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'j' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'w', 'w', 'j', 'j', 'j', 'j', 'w', 'w',
							'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'j', 'j', 'j', 'j', 'j', 'w', 'w', 'w', 'w', 'w', 'w' },

					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', 'w', 'g', 'g', 'g', 'g', 'b', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', 'j', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', 'j', 'g', 'b', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'b', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', 'j', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', 'j', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'b', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', 'j', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', 'w', 'g', 'g', 'g', 'b', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-',
							'-', 'w', '-', '-', '-', '-', 'w', 'w', 'j', 'j', 'j', 'w', 'w', 'w', 'w', 'w', 'j', 'j',
							'j', 'j', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' } };

			for (int i = 0; i < 40; i++) { // y
				for (int j = 0; j < 50; j++) { // x
					switch (level3Map[i][j]) {
					case 'w':
						new Walls(world, j + 10.5, i + 10.5);
						break;
					case 'g':
						new Ground(j + 10, i + 10);
						break;
					case 'f':
						new GoalPost(j + 10, i + 10);
						break;
					case 'j':
						new Bumpers(world, j + 10.5, i + 10.5);
						break;
					case '-':
						break;
					case 'b':
						new Ground(j + 10, i + 10);
						new Battery(j + 10, i + 10);
						break;
					}
				}
			}

			// ********************LEVEL 4*****************
		} else if (level == 3) {
			if (p1 != null)
				p1.remove();
			p1 = new Electrode(world, 15, 45);
			p1.setImage(Electrode.NAME + 1);
			char[][] level4Map = {
					{ 'w', '-', '-', '-', '-', '-', '-', '-', '-', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w',
							'w', 'j', 'j', '-', '-', '-', '-', '-', '-', '-', '-', 'w', 'j', 'j', 'j', 'w', 'w', 'w',
							'w', 'w', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'f' },
					{ 'w', 'g', 'g', 'g', 'b', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'b', 'g', 'g', 'g',
							'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'f' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'b', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'g', 'g', 'b', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'f' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'g', 'b', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'f' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'f' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'f' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'f' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'g', 'g', 'b', 'g', 'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'f' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'f' },

					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'j', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-' },
					{ 'w', 'g', 'g', 'b', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'j', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'b', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'j', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'j', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'b',
							'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'j', 'g', 'g', 'b', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'g', 'g', 'g', 'b', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'b', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-' },
					{ 'w', 'b', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-' },

					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'j', 'j', 'g', 'g', 'g', 'g', 'g', 'g', 'b', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', 'j', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'j', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'-', '-', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'w', 'j', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'j', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'-', '-', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'j', 'w', 'g', 'g', 'g', 'b', 'g', 'g',
							'g', 'g', 'w', 'j', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'j', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'-', '-', 'b', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'j', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'-', '-', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'j', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'-', '-', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'j', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'-', '-', 'g', 'g', 'g', 'g', 'g', 'g', 'b', 'g', 'j', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'j', 'j', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'j', 'j', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'j', 'j', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-' },

					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'b', 'g', 'g', 'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'b', 'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'b', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w' },
					{ 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w',
							'w', 'w', 'w', 'w', 'j', 'j', 'j', 'j', '-', '-', '-', '-', '-', '-', 'w', 'w', 'j', 'j',
							'j', 'j', 'w', 'w', 'w', 'w', 'w', 'j', 'j', 'j', 'j', 'j', 'w' } };

			for (int i = 0; i < 40; i++) { // y
				for (int j = 0; j < 50; j++) { // x
					switch (level4Map[i][j]) {
					case 'w':
						new Walls(world, j + 10.5, i + 10.5);
						break;
					case 'g':
						new Ground(j + 10, i + 10);
						break;
					case 'f':
						new GoalPost(j + 10, i + 10);
						break;
					case 'j':
						new Bumpers(world, j + 10.5, i + 10.5);
						break;
					case '-':
						break;
					case 'b':
						new Ground(j + 10, i + 10);
						new Battery(j + 10, i + 10);
						break;
					}
				}
			}

			// ********************LEVEL 5*****************
		} else if (level == 4) {
			if (p1 != null)
				p1.remove();
			p1 = new Electrode(world, 12, 15);
			p1.setImage(Electrode.NAME + 1);
			char[][] level5Map = {
					{ 'j', 'g', 'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g', '-', '-', '-', '-', 'g', 'g', 'g',
							'g', '-', '-', '-', '-', 'g', 'g', 'g', '-', '-', '-', 'g', '-', '-', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'b', 'b', 'f' },
					{ 'j', 'g', 'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g', '-', '-', '-', '-', '-', '-', 'g',
							'g', '-', '-', '-', '-', 'g', 'g', 'g', '-', '-', '-', 'g', '-', '-', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'b', 'b', 'f' },
					{ 'j', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-', '-', 'g',
							'g', '-', '-', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-', '-', 'g', 'g', '-', '-',
							'-', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'b', 'b', 'f' },
					{ 'j', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-', '-', 'g',
							'g', '-', '-', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-', '-', 'g', 'g', '-', '-',
							'-', 'g', 'g', 'g', 'g', 'g', '-', '-', 'g', 'g', '-', '-', 'f' },
					{ 'j', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', '-', '-', '-', 'g', 'g', '-', '-', 'f' },
					{ 'j', 'g', 'g', 'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'-', '-', 'g', 'g', 'g', '-', '-', '-', '-', '-', '-', '-', 'f' },
					{ 'j', 'g', 'g', 'g', 'g', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'-', '-', 'g', 'g', 'g', '-', '-', '-', '-', '-', '-', '-', 'f' },
					{ 'j', 'g', 'g', 'g', 'g', '-', '-', 'g', 'g', '-', '-', '-', 'g', 'g', 'g', 'g', 'g', 'g', '-',
							'-', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-', '-', 'f' },
					{ 'j', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-', '-', '-', 'g', '-', '-', 'g', 'g', 'g', '-',
							'-', 'g', 'g', '-', '-', '-', '-', 'g', '-', '-', '-', 'g', '-', '-', '-', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-', '-', 'f' },
					{ 'j', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', '-', '-', 'g', 'g', 'g', '-',
							'-', 'g', 'g', '-', '-', '-', '-', 'g', '-', '-', '-', 'g', '-', '-', '-', 'g', 'g', 'g',
							'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'b', '-', '-', '-', 'f' } };

			for (int i = 0; i < 10; i++) { // y
				for (int j = 0; j < 50; j++) { // x
					switch (level5Map[i][j]) {
					case 'w':
						new Walls(world, j + 10.5, i + 10.5);
						break;
					case 'g':
						new Ground(j + 10, i + 10);
						break;
					case 'f':
						new GoalPost(j + 10, i + 10);
						break;
					case 'j':
						new Bumpers(world, j + 10.5, i + 10.5);
						break;
					case '-':
						break;
					case 'b':
						new Ground(j + 10, i + 10);
						new Battery(j + 10, i + 10);
						break;
					}
				}
			}

		}
	}

	int xofs = 0, yofs = 0;
	private int batteryCount;

	@Override
	public void doFrame() {

		super.doFrame();

		if (inGameState("InGame")) {

			float timeStep = 1.0f / FPS;
			world.step(timeStep, 6, 2);
			moveObjects(null, 0);

			// **************control levels*****************
			if (getKey('N')) {
				levelDone();
			}
			if (getKey('D')) {
				lifeLost();
			}

			if (getKey(key_left) || ((getGyroAngleX() < gyroinitx - GYRO_SENSITIVITY) && hasGyro())) {
				p1.body.applyForce(new Vec2(-P1_APPLIED_FORCE, 0), p1.body.getWorldCenter());
			} else if (getKey(key_right) || ((getGyroAngleX() > gyroinitx + GYRO_SENSITIVITY) && hasGyro())) {
				p1.body.applyForce(new Vec2(P1_APPLIED_FORCE, 0), p1.body.getWorldCenter());
			}

			if (getKey(key_up) || ((getGyroAngleY() > gyroinity + GYRO_SENSITIVITY) && hasGyro())) {
				p1.body.applyForce(new Vec2(0, -P1_APPLIED_FORCE), p1.body.getWorldCenter());
			} else if (getKey(key_down) || ((getGyroAngleY() < gyroinity - GYRO_SENSITIVITY) && hasGyro())) {
				p1.body.applyForce(new Vec2(0, P1_APPLIED_FORCE), p1.body.getWorldCenter());
			}

			Vec2 vel = p1.body.getLinearVelocity();

			// ***********BOOST***********
			float bx = vel.x;
			float by = vel.y + 5;
			if (getKey('Z')) {
				if (boost == true) {
					boost = false;
					if (getKey(key_left) || ((getGyroAngleX() < gyroinitx - GYRO_SENSITIVITY) && hasGyro())) {
						p1.body.applyForce(new Vec2(-P1_BOOST_FORCE, 0), p1.body.getWorldCenter());
					} else if (getKey(key_right) || ((getGyroAngleX() > gyroinitx + GYRO_SENSITIVITY) && hasGyro())) {
						p1.body.applyForce(new Vec2(P1_BOOST_FORCE, 0), p1.body.getWorldCenter());
					}

					if (getKey(key_up) || ((getGyroAngleY() > gyroinity + GYRO_SENSITIVITY) && hasGyro())) {
						p1.body.applyForce(new Vec2(0, -P1_BOOST_FORCE), p1.body.getWorldCenter());
					} else if (getKey(key_down) || ((getGyroAngleY() < gyroinity - GYRO_SENSITIVITY) && hasGyro())) {
						p1.body.applyForce(new Vec2(0, P1_BOOST_FORCE), p1.body.getWorldCenter());
					}
				}
			}

			// ***********FRICTION**********
			float fx = -P1_FRICTION_FORCE * vel.x;
			float fy = -P1_FRICTION_FORCE * vel.y;
			Vec2 fric = new Vec2(fx, fy);

			p1.body.applyForce(fric, p1.body.getWorldCenter());

			// **************moving map*********************
			xofs = (int) (p1.x);
			yofs = (int) (p1.y);
			setViewOffset(xofs, yofs, // the position within the playfield
					true // true means the given position is center of the view, false means it is
							// topleft.
			);

			// Lose condition
			if (checkCollision(Ground.COL_ID, p1) != Ground.COL_ID) {
				lifeLost();
			}

			// Win Condition
			if (checkCollision(GoalPost.COL_ID, p1) == GoalPost.COL_ID) {
				getAnimation(GoalPost.NAME).start();
				if (level == 4) {
					gameOver();
				} else {
					levelDone();
				}
			}

			if (checkCollision(Battery.COL_ID, p1) == Battery.COL_ID) {
				boost = true;
				batteryCount++;
			}
			
			if (checkCollision(Bumpers.COL_ID, p1) == Bumpers.COL_ID) {
				playAudio("BounceAudio");
			}
			
			checkCollision(Battery.COL_ID, Electrode.COL_ID);
			checkCollision(Electrode.COL_ID, Battery.COL_ID);
			checkCollision(GoalPost.COL_ID, Electrode.COL_ID);
			checkCollision(Electrode.COL_ID, GoalPost.COL_ID);

		}
	}

	@Override
	public void paintFrame() {
		// TODO Auto-generated method stub

		// drawString("Player (P1) cordinates is now (" + xofs + "," + yofs + ").",
		// viewWidth() / 2, 120, 0);
//		drawString("TOP LEFT", 0, 8, -1, true // indicate it should be drawn relative to playfield
//		);
//		drawString("BOTTOM LEFT", 0, pfHeight() - 20, -1, true);
//		drawString("TOP RIGHT", pfWidth(), 8, 1, true);
//		drawString("BOTTOM RIGHT", pfWidth(), pfHeight() - 20, 1, true);

		// Gyro Log
		double[] gyroAngles = getGyroVec();
		double[] accelAngles = getAccelVec();

		setColor(JGColor.green);
//				drawString("gyro x:" + nf.format(gyroAngles[0]), 20, 10, -1);
//				drawString("gyro y:" + nf.format(gyroAngles[1]), 20, 60, -1);
//				drawString("gyro z:" + nf.format(gyroAngles[2]), 20, 110, -1);
//		drawString("gyro x:" + nf.format(getGyroAngleX()), 20, 10, -1);
//		drawString("gyro y:" + nf.format(getGyroAngleY()), 20, 60, -1);
//		drawString("gyro z:" + nf.format(getGyroAngleZ()), 20, 110, -1);
//		drawString("gyro init x: " + nf.format(gyroinitx), 20, 160, -1);
//		drawString("gyro init y: " + nf.format(gyroinity), 20, 210, -1);
//		setColor(JGColor.magenta);
//		drawString("accel x:" + nf.format(accelAngles[0]), 250, 10, -1);
//		drawString("accel y:" + nf.format(accelAngles[1]), 250, 60, -1);
//		drawString("accel z:" + nf.format(accelAngles[2]), 250, 110, -1);

		if (inGameState("InGame")) {
			// Score:
			drawString("Score: " + batteryCount, viewWidth() - 20, 10, 1, new JGFont("Calibri", 0, 24), status_color);
			drawString("Lives: " + lives, viewWidth() - 20, 40, 1, new JGFont("Calibri", 0, 24), status_color);
			
			//Time:
			drawString("Time: " + min + " : " + sec, viewWidth() - 20, 70, 1, new JGFont("Calibri", 0, 24), status_color);
		}
	}
}