import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * The Class GameField. This is where all of the magic happens. This is the main
 * menu, the actual fighting, and all of the Gameover information. This class
 * has the most variables and items because this is the grand compilation of all
 * of the other classes. This is the culmination to be put in the runner JFrame
 */
@SuppressWarnings("serial")
public class GameField extends JPanel implements ActionListener {

	public static final int BOARD_WIDTH = (int) (Runner.BOARD_WIDTH * 0.95625), // BoardWidth
			BOARD_HEIGHT = (int) (Runner.BOARD_HEIGHT * 0.95625); // BoardHeight
	private static int shotsFired, // SE (Self Explanitory)
			shotsHit, // SE
			points, // SE
			enemiesKilled, // SE
			toNextLife; // SE
	private static volatile int level, // Has to be volatile because it can be
										// changed at various points
			count; // A count variable
	private static Timer timer; // The timer so that the game can run
	private static Clip introPlayer, // Plays the Intro
			levelPlayer; // Plays new Level
	private static final Font gameFont = new Font("impact", 0, 16); // SE
	private static boolean isStartingUp, isGameOver, isPause, isReset = false;
	private Ship ship; // SE
	private ArrayList<Alien> enemies; // SE
	private ArrayList<Star> sky; // SE
	private ImageIcon logo; // The logo that drops in at the start
	@SuppressWarnings("unused")
	private static Graphics graph; // To paint various stuffs

	/**
	 * Instantiates a new game field, A whole lot is done in the constructor, an
	 * actionlistener is used, Background is set to Black, it is doubleBuffered,
	 * variable grah is instinated level = 1, count, shots fired, shots hit,
	 * points, enemies killed, to next 1 up, are all given a value of 0. Ship,
	 * enemies, the sky, are all instinated and the arrays are populated. The
	 * timer is initiated and pause is set to false, because the game is
	 * starting up. The image and the music are loaded, Alien is initialized,
	 * the starting up is set to true and finally the timer is started
	 */
	public GameField() {
		// -----------------------------------JPANEL
		// INITIALIZE-----------------------//
		addKeyListener(new KeyListener() {

			public void keyReleased(KeyEvent e) {
				ship.keyReleased(e);
			}

			public void keyPressed(KeyEvent e) {
				ship.keyPressed(e);
			}

			public void keyTyped(KeyEvent arg0) {
				/* No Implementation Needed */
			}

		});
		setFocusable(true);
		setBackground(Color.BLACK);
		setDoubleBuffered(true);
		// -----------------------------------VARIABLE
		// INITIALIZE---------------------//
		graph = getGraphics();
		level = 1;
		count = 0;
		setShotsFired(0);
		setShotsHit(0);
		points = 0;
		enemiesKilled = 0;
		toNextLife = 0;
		count = 0;
		ship = new BasicShip();
		enemies = new ArrayList<Alien>();
		sky = new ArrayList<Star>();
		populate(true);
		setTimer(new Timer(0, this));
		setPause(false);
		// POPULATES THE SKY
		for (int i = 0; i < Runner.BOARD_HEIGHT * 1.5; i += Star.length) {
			for (int j = 0; j < Runner.BOARD_WIDTH * 1.5; j += Star.length) {
				if (Math.random() > .98)
					sky.add(new Star(i, j));
			}
		}
		AudioInputStream stream = null;
		BufferedImage img = null;
		try {
			img = ImageIO.read(getClass().getResource("GalagaLogo.gif"));
			introPlayer = AudioSystem.getClip();
			levelPlayer = AudioSystem.getClip();
			stream = AudioSystem.getAudioInputStream(getClass().getResource(
					"Level_Start.wav"));
			levelPlayer.open(stream);
			stream = AudioSystem.getAudioInputStream(getClass().getResource(
					"Galaga_Theme_Song.wav"));
			introPlayer.open(stream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logo = new ImageIcon(img);
		getTimer().start();
		setStartingUp(true);
		setGameOver(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics a) {

		super.paintComponent(a);
		// Paints the pause screen
		if (isPause()) {
			a.setFont(gameFont);
			a.setColor(Color.RED);
			a.drawString("PAUSE", BOARD_WIDTH / 2, BOARD_HEIGHT / 2);
			return;
		}
		// Resets if need be
		if (isReset) {
			reset();
			isReset = false;
		}
		// Repopulates the world
		if (enemies.size() == 0) // Repopulate
		{
			levelPlayer.loop(0);
			level++;
			Alien.DELAY = Alien.MAX_DELAY;
			if (level % 2 == 0) {
				Alien.baseHealth++;
				Alien.basePoints += 100;
			}
			populate(false);
			ship.getStorage().removeAll(ship.getStorage());
		}
		// Paints all the stars in the sky
		for (Star star : sky) {
			a.setColor(star.getStarColor());
			a.drawRect(star.getRight(), star.getEdge(), Star.length,
					Star.length);
			a.fillRect(star.getRight(), star.getEdge(), Star.length,
					Star.length);
			star.move();
			if (Math.random() > .5)
				star.fade();
		}
		a.setColor(Color.WHITE);
		a.setFont(gameFont);
		// Cheat that has to be put up here
		if (ship.isCanThrottle())
			a.drawString("CAN THROTTLE GAME " + Alien.DELAY, 320, 20);
		// Paints GameOver
		if (isGameOver()) {
			a.setColor(Color.WHITE);
			a.setFont(gameFont);
			a.drawString("GAME OVER", BOARD_WIDTH / 12, BOARD_HEIGHT / 2 - 40);
			a.drawString("Shots Fired: " + getShotsFired(), BOARD_WIDTH / 12,
					BOARD_HEIGHT / 2 - 20);
			a.drawString("Shots Hit: " + getShotsHit(), BOARD_WIDTH / 12,
					BOARD_HEIGHT / 2);
			a.drawString("Total Points: " + points, BOARD_WIDTH / 12,
					BOARD_HEIGHT / 2 + 20);
			a.drawString("Max Level Achieved: " + level, BOARD_WIDTH / 12,
					BOARD_HEIGHT / 2 + 40);
			a.drawString("Enemies Killed: " + enemiesKilled, BOARD_WIDTH / 12,
					BOARD_HEIGHT / 2 + 60);
			a.drawString("If you want to play again press R", BOARD_WIDTH / 12,
					BOARD_HEIGHT / 2 + 80);
			a.drawString(
					"Press M, I, T (Up and Down Arrows), or C and find out...",
					BOARD_WIDTH / 12, BOARD_HEIGHT / 2 + 100);
			return;

		}
		// Paints the title screen
		if (isStartingUp()) {
			logo.paintIcon(this, a, BOARD_WIDTH / 12, BOARD_HEIGHT / 8);
			a.setFont(gameFont);
			a.setColor(Color.WHITE);
			a.drawString(
					"A Game Ported to Java by: Bhuvan Venkatesh, Chris Schulstad",
					BOARD_WIDTH / 12, BOARD_HEIGHT / 2 - 40);
			a.drawString("and Matt Scislowski \\( O o O )/", BOARD_WIDTH / 12,
					BOARD_HEIGHT / 2 - 20);
			a.drawString("Use left and right to move and space to shoot",
					BOARD_WIDTH / 12, BOARD_HEIGHT / 2);
			a.drawString("Press Shift and a Direction to jump",
					BOARD_WIDTH / 12, BOARD_HEIGHT / 2 + 20);
			a.drawString("Press enter to pause at any time", BOARD_WIDTH / 12,
					BOARD_HEIGHT / 2 + 40);
			a.drawString("Press R button to reset to this screen at any time",
					BOARD_WIDTH / 12, BOARD_HEIGHT / 2 + 60);
			a.drawString("Press W to start Playing! and Have fun",
					BOARD_WIDTH / 12, BOARD_HEIGHT / 2 + 80);
			return;
		}

		count++;
		// The game has officially started
		if (count > Alien.MUSIC_DELAY) {
			ship.setCanFire(true);
			ship.setCanMove(true);
			introPlayer.close();
		} else {
			a.setColor(Color.RED);
			a.setFont(gameFont);
			a.drawString("READY!", BOARD_WIDTH / 2, BOARD_HEIGHT / 2);
		}

		// ---------------------------------------Draw
		// Missiles--------------------------------------------------------//

		for (Missile b : ship.getStorage()) // Draws all of the missiles
		{
			a.setColor(b.getMyColor());
			a.drawRect(b.getRight(), b.getEdge(), Missile.WIDTH, Missile.HEIGHT);
			a.fillRect(b.getRight(), b.getEdge(), Missile.WIDTH, Missile.HEIGHT);
			b.move();
		}

		// -----------------------------Draw Aliens and Their Missiles in one
		// loop-------------------------------------------//

		Alien in;
		AlienMissile c;
		for (int i = 0; i < enemies.size(); i++) {
			in = enemies.get(i);
			for (int j = 0; j < in.list.size(); j++) // Draws the Missiles
			{
				c = in.list.get(j);
				if (!c.isVisible()) {
					in.list.remove(j);
					j--;
				}
				c.move();
				a.setColor(Color.GREEN);
				a.drawLine(GameField.getFormattedRight(c.getRight()),
						GameField.getFormattedEdge(c.getEdge()),
						GameField.getFormattedRight(c.toRightNext),
						GameField.getFormattedEdge(c.toEdgeNext));
			}

			if (Alien.amountAttacking < level + 1 && Math.random() > .999
					|| Alien.amountAttacking == 0) {
				if (in instanceof BasicAlien)
					in.isAttacking = true;
				else if (in instanceof BlueAlien) {
					((BlueAlien) in).startAttack();
				} else if (in instanceof RedAlien) {
					((RedAlien) in).startAttack();
				}
			}
			in.attack();

			if (!in.isHit(ship.getStorage())) {
				in.move();
				in.getImage().paintIcon(this, a,
						GameField.getFormattedRight(in),
						GameField.getFormattedEdge(in));
			} else if ((in.health > 1)) {
				in.hit();
				in.move();
				if (in instanceof BlueAlien)
					((BlueAlien) in).change();
				in.getImage().paintIcon(this, a,
						GameField.getFormattedRight(in),
						GameField.getFormattedEdge(in));
			} else {
				points += enemies.get(i).getPoints();
				toNextLife += enemies.get(i).getPoints();
				if (toNextLife >= 5000) {
					toNextLife = 0;
					if (ship.getLives() <= 16)
						ship.setLives(ship.getLives() + 1);
				}
				enemies.get(0).list.addAll(enemies.get(i).list);
				enemies.remove(i);
				enemies.trimToSize();
				Alien.DELAY++;
				enemiesKilled++;
				i--;
			}

		}

		// -----------------------------Draw Ship if it isn't hit and then
		// end-------------------------------//

		ship.removeInvisible();

		if (ship.isInvincible() || !ship.isHit(enemies)) {
			ship.getImage().paintIcon(this, a,
					GameField.getFormattedRight(ship),
					this.getHeight() - ship.getImage().getIconHeight() - 20);
		} else if (ship.getLives() > 0) {
			ship.setLives(ship.getLives() - 1);
			resetAttack();
		} else {
			setGameOver(true);
			count = 0;
		}
		a.setColor(Color.GREEN);
		for (int i = 0; i < ship.getLives(); i++) {
			a.drawRect(50 + i * 20, this.getHeight() - 20, 10, 10);
			a.fillRect(50 + i * 20, this.getHeight() - 20, 10, 10);
		}
		a.setColor(Color.WHITE);
		a.setFont(gameFont);

		if (Ship.MAX_SHOTS != 2)
			a.drawString("MAX SHOTS ON", 60, 20);
		if (ship.isInvincible())
			a.drawString("SHIP IS INVINCIBLE", 180, 20);
		if (Missile.isColorful()) {
			a.drawString("C", 0, GameField.BOARD_HEIGHT / 3);
			a.drawString("O", 0, GameField.BOARD_HEIGHT / 3 + 20);
			a.drawString("L", 0, GameField.BOARD_HEIGHT / 3 + 40);
			a.drawString("O", 0, GameField.BOARD_HEIGHT / 3 + 60);
			a.drawString("R", 0, GameField.BOARD_HEIGHT / 3 + 80);
			a.drawString("C", GameField.BOARD_WIDTH + 5,
					GameField.BOARD_HEIGHT / 3);
			a.drawString("O", GameField.BOARD_WIDTH + 5,
					GameField.BOARD_HEIGHT / 3 + 20);
			a.drawString("L", GameField.BOARD_WIDTH + 5,
					GameField.BOARD_HEIGHT / 3 + 40);
			a.drawString("O", GameField.BOARD_WIDTH + 5,
					GameField.BOARD_HEIGHT / 3 + 60);
			a.drawString("R", GameField.BOARD_WIDTH + 5,
					GameField.BOARD_HEIGHT / 3 + 80);
		}
		if (ship.isMultipleShots()) {
			a.drawString("M", 0, GameField.BOARD_HEIGHT / 3 - 100);
			a.drawString("U", 0, GameField.BOARD_HEIGHT / 3 - 80);
			a.drawString("L", 0, GameField.BOARD_HEIGHT / 3 - 60);
			a.drawString("T", 0, GameField.BOARD_HEIGHT / 3 - 40);
			a.drawString("I", 0, GameField.BOARD_HEIGHT / 3 - 20);
			a.drawString("M", GameField.BOARD_WIDTH + 5,
					GameField.BOARD_HEIGHT / 3 - 100);
			a.drawString("U", GameField.BOARD_WIDTH + 5,
					GameField.BOARD_HEIGHT / 3 - 80);
			a.drawString("L", GameField.BOARD_WIDTH + 5,
					GameField.BOARD_HEIGHT / 3 - 60);
			a.drawString("T", GameField.BOARD_WIDTH + 5,
					GameField.BOARD_HEIGHT / 3 - 40);
			a.drawString("I", GameField.BOARD_WIDTH + 5,
					GameField.BOARD_HEIGHT / 3 - 20);
		} else if (Missile.isColorful()) {
			a.drawString("F", 0, GameField.BOARD_HEIGHT / 3 + 100);
			a.drawString("U", 0, GameField.BOARD_HEIGHT / 3 + 120);
			a.drawString("L", 0, GameField.BOARD_HEIGHT / 3 + 140);
			a.drawString("F", GameField.BOARD_WIDTH + 5,
					GameField.BOARD_HEIGHT / 3 + 100);
			a.drawString("U", GameField.BOARD_WIDTH + 5,
					GameField.BOARD_HEIGHT / 3 + 120);
			a.drawString("L", GameField.BOARD_WIDTH + 5,
					GameField.BOARD_HEIGHT / 3 + 140);
		}
		a.drawString("Points", 10, 20);
		a.drawString("" + points, 10, 35);
		a.drawString("Lives: ", 10, this.getHeight() - 10);
		a.drawString("Level: ", (int) (this.getWidth() * .8),
				this.getHeight() - 10);
		a.drawString("" + level, (int) (this.getWidth() * .9),
				this.getHeight() - 10);
		Toolkit.getDefaultToolkit().sync();
		a.dispose();
		count++;
	}

	/**
	 * Reset aliens in enemies back to attack formation after being hit
	 */
	public void resetAttack() {
		for (Alien a : enemies) {
			a.list.removeAll(a.list);
			if (a.isAttacking)
				Alien.amountAttacking--;
			a.isAttacking = false;
			a.isMoving = true;
			a.returnToPosition();
		}

	}

	/**
	 * Populate.
	 * 
	 * @param isStart
	 *            if the game is starting it's true else it's false
	 */
	public void populate(boolean isStart) {
		Alien in;
		for (int[] a : Alien.BasicPosition) {
			in = new BasicAlien(a[1], a[0]);
			if (!isStart) {
				in.count = Alien.MUSIC_DELAY - 200;
				in.isMoving = true;
			}
			enemies.add(in);
		}
		for (int[] a : Alien.RedPosition) {
			in = new RedAlien(a[1], a[0]);
			if (!isStart) {
				in.count = Alien.MUSIC_DELAY - 200;
				in.isMoving = true;
			}
			enemies.add(new RedAlien(a[1], a[0]));
		}
		for (int[] a : Alien.BossPosition) {
			in = new BlueAlien(a[1], a[0]);
			if (!isStart) {
				in.count = Alien.MUSIC_DELAY - 200;
				in.isMoving = true;
			}
			enemies.add(new BlueAlien(a[1], a[0]));
		}

	}

	/**
	 * Play intro.
	 */
	public static void playIntro() {
		try {
			introPlayer.loop(0);
		} catch (Exception e) {
		}
	}

	/**
	 * Play's the music using invoke later to paint then play
	 */
	public static void play() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GameField.playIntro();
			}
		});

	}

	/**
	 * Resets the gamefield for a new game
	 */
	public void reset() {
		graph = getGraphics();
		level = 1;
		count = 0;
		setShotsFired(0);
		setShotsHit(0);
		points = 0;
		enemiesKilled = 0;
		toNextLife = 0;
		ship = new BasicShip();
		enemies = new ArrayList<Alien>();
		sky = new ArrayList<Star>();
		sky.add(new Star(100, 100));
		populate(true);
		setTimer(new Timer(0, this));
		setPause(false);

		for (int i = 0; i < Runner.BOARD_HEIGHT * 1.5; i += Star.length) {
			for (int j = 0; j < Runner.BOARD_WIDTH * 1.5; j += Star.length) {
				if (Math.random() > .98)
					sky.add(new Star(i, j));
			}
		}

		BufferedImage img = null;
		try {
			img = ImageIO.read(getClass().getResource("GalagaLogo.gif"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		logo = new ImageIcon(img);
		getTimer().start();
		setStartingUp(true);
		setGameOver(false);
		count = 0;
	}

	// Getters setters and various other one liners

	public static int getShotsFired() {
		return shotsFired;
	}

	public static void setPause(boolean isPause) {
		GameField.isPause = isPause;
	}

	public static boolean isReset() {
		return isReset;
	}

	public static void setReset(boolean isReset) {
		GameField.isReset = isReset;
	}

	public static boolean isGameOver() {
		return isGameOver;
	}

	public static void setGameOver(boolean isGameOver) {
		GameField.isGameOver = isGameOver;
	}

	public static void setShotsFired(int shotsFired) {
		GameField.shotsFired = shotsFired;
	}

	public static int getShotsHit() {
		return shotsHit;
	}

	public static void setShotsHit(int shotsHit) {
		GameField.shotsHit = shotsHit;
	}

	public static boolean isStartingUp() {
		return isStartingUp;
	}

	public static void setStartingUp(boolean isStartingUp) {
		GameField.isStartingUp = isStartingUp;
	}

	public static Timer getTimer() {
		return timer;
	}

	public static void setTimer(Timer timer) {
		GameField.timer = timer;
	}

	public static boolean isPause() {
		return isPause;
	}

	public void actionPerformed(ActionEvent arg0) {
		repaint();

	}

	// Gets the actual number of pixels if toRight and toEdge
	// Are actually 0-100 if not, they are actual pixels
	public static int getFormattedRight(GameSprite in) {
		return (int) (in.getRight() / 100.0 * BOARD_WIDTH);
	}

	public static int getFormattedEdge(GameSprite in) {
		return (int) (in.getEdge() / 100.0 * BOARD_HEIGHT);
	}

	public static int getFormattedRight(int right) {
		return (int) (right / 100.0 * BOARD_WIDTH);
	}

	public static int getFormattedEdge(int edge) {
		return (int) (edge / 100.0 * BOARD_HEIGHT);
	}

}
