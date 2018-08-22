import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.Timer;
import javax.swing.JOptionPane;
import sun.audio.*;
import java.io.*;
import javax.swing.JPanel;

/**
 * Creates the board of pegs, as well as many methods within that allow for the tiles to be clicked and change
 * color.
 * @author Colby Allen
 *
 */
@SuppressWarnings("serial")
public class MineFieldPanel extends JPanel {

	private MineFieldButton[][] tiles;
	private Point mines;
	private MineFieldButton current; // TODO: Make this blink a X to indicate it is the current tile
	private MineFieldButton start;
	private MineFieldButton end;
	private int dimension;
	private ArrayList<Point> rightPath;
	private ArrayList<Point> mineTile;
	private ArrayList<Point> explodedMines;
	private ArrayList<Point> pathTravelled;
	private Random rand;
	private double numMines;
	private int numMinesSurround;
	private int numLives;
	private int numPoints;
	private String boing = "boing.wav";
	private String timAllen = "timAllen.wav";
	private String explosion = "explosion.wav";
	private String congrats = "congrats.wav";
	private final int DELAY = 500;

	/**
	 * Creates the entire grid of panels
	 * @param listener Allows the buttons on the grid to be pressed by the user
	 * @param dimension sets the dimension of the board
	 */
	public MineFieldPanel(ActionListener listener, int dimension) {
		this.dimension = dimension;
		numLives = 5;
		numPoints = 100;

		setLayout(new GridLayout(dimension, dimension));
		tiles = new MineFieldButton[dimension][dimension];
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				tiles[i][j] = new MineFieldButton();
				tiles[i][j].addActionListener(listener);
				tiles[i][j].setEnabled(false);
				tiles[i][j].position(i, j);
				tiles[i][j].setBackground(Color.white);
				this.add(tiles[i][j]);
			}
		}

		start = tiles[tiles.length - 1][0];
		start.setEnabled(true);
		current = start;
		end = tiles[0][tiles.length - 1];

		RandomWalk correctPath = new RandomWalk(dimension);
		correctPath.createWalk();
		rightPath = correctPath.getPath();

		rand = new Random();
		numMines = ((dimension * dimension - (rightPath.size())) / 4);
		mineTile = new ArrayList<Point>();
		for (int x = 0; x <= numMines; x++) {
			int i = rand.nextInt(dimension);
			int j = rand.nextInt(dimension);

			mines = new Point(i, j);
			if (!mineTile.contains(mines) && !rightPath.contains(mines)) {
				mineTile.add(mines);
			} else {
				x--;
			}

			tiles[dimension - 1][0].setBackground(Color.CYAN);
			tiles[0][dimension - 1].setBackground(Color.MAGENTA);
		}
		current = tiles[dimension - 1][0];
		explodedMines = new ArrayList<Point>();
		pathTravelled = new ArrayList<Point>();
		startAnimation();
	}

	/**
	 * Shows the mines on the whole board, sets their colors to black.
	 */
	public void showMines() {
		for (Point i : mineTile) {
			tiles[i.x][i.y].setBackground(Color.BLACK);
		}
	}

	/**
	 * Sets the mines colors back to white, as well as the start and end tiles to their original colors
	 */
	public void hideMines() {
		for (Point i : mineTile) {
			tiles[i.x][i.y].setBackground(Color.WHITE);
			tiles[dimension - 1][0].setBackground(Color.CYAN);
			tiles[0][dimension - 1].setBackground(Color.MAGENTA);
		}
	}

	/**
	 * Shows the correct path, sets the color to blue
	 */
	public void showPath() {
		for (Point i : rightPath) {
			tiles[i.x][i.y].setBackground(Color.BLUE);
			tiles[dimension - 1][0].setBackground(Color.CYAN);
			tiles[0][dimension - 1].setBackground(Color.MAGENTA);
		}
	}

	/**
	 * Hides the correct path, sets the colo back to white.
	 */
	public void hidePath() {
		for (Point i : rightPath) {
			tiles[i.x][i.y].setBackground(Color.WHITE);
			tiles[dimension - 1][0].setBackground(Color.CYAN);
			tiles[0][dimension - 1].setBackground(Color.MAGENTA);
		}
	}

	// This information was gathered from Stackoverflow
	// https://stackoverflow.com/questions/7080205/popup-message-boxes
	/**
	 * This allows pop up messages to happen when necessary
	 * @param infoMessage declares the message within the pop up
	 * @param titleBar declares a message on the top of the pop up
	 */
	public static void infoBox(String infoMessage, String titleBar) {
		JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Allows the panels adjacent to the current tile, unless is a mine
	 * @param x is the x point
	 * @param y is the y point
	 */
	public void movePanel(int x, int y) {
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				tiles[i][j].setEnabled(false);
			}
		}
		if (y + 1 < dimension) {
			if (!explodedMines.contains(new Point(x, y + 1))) {
				tiles[x][y + 1].setEnabled(true);
			}
		}
		if (y - 1 >= 0) {
			if (!explodedMines.contains(new Point(x, y - 1))) {
				tiles[x][y - 1].setEnabled(true);
			}
		}
		if (x + 1 < dimension) {
			if (!explodedMines.contains(new Point(x + 1, y))) {
				tiles[x + 1][y].setEnabled(true);
			}
		}
		if (x - 1 >= 0) {
			if (!explodedMines.contains(new Point(x - 1, y))) {
				tiles[x - 1][y].setEnabled(true);
			}
		}
		// This will make it so you cannot move diagonally
		if (x - 1 >= 0 && y - 1 >= 0) {
			tiles[x - 1][y - 1].setEnabled(false);
		}
		if (x + 1 < dimension && y - 1 >= 0) {
			tiles[x + 1][y - 1].setEnabled(false);
		}
		if (x - 1 >= 0 && y + 1 < dimension) {
			tiles[x - 1][y + 1].setEnabled(false);
		}
		if (x + 1 < dimension && y + 1 < dimension) {
			tiles[x + 1][y + 1].setEnabled(false);
		}
		if (numLives == 0 || numPoints <= 0) {
			try {
				InputStream in1 = new FileInputStream(timAllen);
				try {
					AudioStream as = new AudioStream(in1);
					AudioPlayer.player.start(as);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			MineFieldPanel.infoBox("Try again to complete MineWalker!", "Game Over!!");
			showMines();
			for (int i = 0; i < dimension; i++) {
				for (int j = 0; j < dimension; j++) {
					tiles[i][j].setEnabled(false);
				}
			}
		}

		if (current == end) {
			try {
				InputStream in1 = new FileInputStream(congrats);
				try {
					AudioStream as = new AudioStream(in1);
					AudioPlayer.player.start(as);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			MineFieldPanel.infoBox(
					"You have completed MineWalker! You have " + numPoints + " points and " + numLives + " lives!",
					"Congrats!");
			showMines();
			for (int i = 0; i < dimension; i++) {
				for (int j = 0; j < dimension; j++) {
					tiles[i][j].setEnabled(false);
				}
			}

		}
	}

	/**
	 * Sets the color of the clicked tile based off of how many surrounding mines there are
	 * @param x checks the current x coordinate
	 * @param y checks the current y coordinate
	 */
	public void setColor(int x, int y) {
		pathTravelled.add(new Point(x,y));
		numMinesSurround = 0;
		if (y + 1 < dimension) {
			if (mineTile.contains(new Point(x, y + 1))) {
				numMinesSurround++;
			}
		}
		if (y - 1 >= 0) {
			if (mineTile.contains(new Point(x, y - 1))) {
				numMinesSurround++;
			}
		}
		if (x + 1 < dimension) {
			if (mineTile.contains(new Point(x + 1, y))) {
				numMinesSurround++;
			}
		}
		if (x - 1 >= 0) {
			if (mineTile.contains(new Point(x - 1, y))) {
				numMinesSurround++;
			}
		}
		// For this and all of the other soundfiles that were put in, I was helped by
		// stackoverflow at
		// https://stackoverflow.com/questions/26305/how-can-i-play-sound-in-java
		try {
			InputStream in = new FileInputStream(boing);
			try {
				AudioStream as = new AudioStream(in);
				AudioPlayer.player.start(as);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		current.setBackground(MineFieldButton.COLORS[numMinesSurround + 1]);
		numPoints --;
		if (mineTile.contains(new Point(x, y))) {
			if (explodedMines.contains(new Point(x, y))) {
				current.setBackground(Color.BLACK);

			} else {
				try {
					InputStream in = new FileInputStream(explosion);
					try {
						AudioStream as = new AudioStream(in);
						AudioPlayer.player.start(as);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				explodedMines.add(new Point(x, y));
				numLives--;
				numPoints = (numPoints - 10);
				current.setBackground(Color.BLACK);
			}
		}

	}
	public void returnFromMine(int x, int y)
	{
		if(mineTile.contains(new Point(x,y)))
		{
			current = tiles[(int) pathTravelled.get(pathTravelled.size()-1).getX()][(int) pathTravelled.get(pathTravelled.size()-1).getY()];
		}
	}

	/**
	 * Returns the number of lives remaining
	 * @return the number of lives remaining
	 */
	public int getLives() {
		return numLives;
	}


/**
 * Returns the number of points remaining
 * @return the number of points remaining
 */
	public int getPoints() {
		return numPoints;
	}

	/**
	 * Resets the number of lives and points, and sets current point to the start
	 */
	public void newStart() {
		numLives = 5;
		numPoints = 100;
		current = tiles[dimension - 1][0];

	}

	/**
	 * Sets the text on the current tile to a flashing X
	 *
	 */
	public class TimerActionListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			if (current.getText().equals("")) {
				current.setText("X");
			} else {
				current.setText("");
			}
		}
	}

	/**
	 * Starts the animation for the X to flash
	 */
	public void startAnimation() {
		TimerActionListener blinkingX = new TimerActionListener();
		new Timer(DELAY, blinkingX).start();
	}

	/**
	 * Stops the x from flashing on tiless that aren't the current
	 */
	public void stopAnimation() {
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles.length; j++) {
				tiles[i][j].setText("");
			}
		}
	}

	/**
	 * Returns the current tile
	 * @return the current tile
	 */
	public MineFieldButton getCurrent() {
		return current;
	}

	/**
	 * Sets the current tile to the the MineFieldButton clicked
	 * @param m is the MineFieldButton clicked
	 */
	public void setCurrent(MineFieldButton m) {
		current = m;

	}

}
