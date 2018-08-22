import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
/**
 * Creates the window that the program opens, while keeping track of points, lives and the tiles you have 
 * stepped on.
 * @author Colby Allen
 *
 */
public class MineWalkerPanel extends JPanel {

	private MineFieldPanel board;
	private JButton showMines;
	private JButton showPath;
	private JButton giveUp;
	private JTextField sizeOfGrid;
	private JLabel points;
	private JLabel lives;
	private int dimension;
	private int newDimension;
	private JPanel southPanel;
	private JPanel westPanel;
	private JPanel eastPanel;
	public String newInput;

	/**
	 * Creates the panel where all is stored
	 * @param width is the width of the panel
	 * @param height is the height of the panel
	 */
	public MineWalkerPanel(int width, int height) {
		setLayout(new BorderLayout());
		dimension = 10;
		board = new MineFieldPanel(new MineFieldButtonListener(), dimension);

		// Creates the buttons and Text Field
		// Create showMines button
		showMines = new JButton("Show Mines");
		showMines.addActionListener(new ShowMinesButtonListener());
		// Create showPath button
		showPath = new JButton("Show Path");
		showPath.addActionListener(new ShowPathButtonListener());
		// Create giveUp Button
		giveUp = new JButton("Give Up?");
		giveUp.addActionListener(new GiveUpButtonListener());
		// Create setPreferredSize text field

		sizeOfGrid = new JTextField(Integer.toString(dimension));
		sizeOfGrid.setMaximumSize(showMines.getPreferredSize());

		// Creates the southpanel
		southPanel = new JPanel();
		southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.X_AXIS));
		southPanel.add(Box.createHorizontalGlue());
		southPanel.add(sizeOfGrid);
		southPanel.add(showMines);
		southPanel.add(showPath);
		southPanel.add(giveUp);
		southPanel.add(Box.createHorizontalGlue());

		// Creating the West Panel
		westPanel = new JPanel();
		westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));
		westPanel.setBorder(BorderFactory.createTitledBorder("Color Key"));
		for (int i = 1; i < MineFieldButton.COLORS.length - 3; i++) {
			JLabel label = new JLabel((i - 1) + " Nearby Mines");
			label.setOpaque(true);
			label.setPreferredSize(new Dimension(100, 70));
			label.setBackground(MineFieldButton.COLORS[i]);
			label.setFont(new Font("Arial", Font.BOLD, 9));
			westPanel.add(label);
		}
		JLabel blackLabel = new JLabel("Exploded Mine ");
		blackLabel.setOpaque(true);
		blackLabel.setPreferredSize(new Dimension(120, 70));
		blackLabel.setBackground(MineFieldButton.COLORS[5]);
		blackLabel.setForeground(Color.white);
		blackLabel.setFont(new Font("Arial", Font.BOLD, 9));
		westPanel.add(blackLabel);
		westPanel.add(Box.createVerticalGlue());

		JLabel cyanLabel = new JLabel("Start");
		cyanLabel.setOpaque(true);
		cyanLabel.setPreferredSize(new Dimension(150, 70));
		// cyanLabel.setSize(180, 100);
		cyanLabel.setBackground(MineFieldButton.COLORS[6]);
		cyanLabel.setFont(new Font("Arial", Font.BOLD, 20));
		westPanel.add(cyanLabel);

		JLabel magLabel = new JLabel("End");
		magLabel.setOpaque(true);
		magLabel.setPreferredSize(new Dimension(150, 70));
		magLabel.setBackground(MineFieldButton.COLORS[7]);
		magLabel.setFont(new Font("Arial", Font.BOLD, 20));
		westPanel.add(magLabel);

		// Creating the East Panel
		eastPanel = new JPanel();
		eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
		TitledBorder tb = BorderFactory.createTitledBorder("Score");
		tb.setTitleJustification(TitledBorder.RIGHT);
		eastPanel.setBorder(tb);
		points = new JLabel("Points: " + board.getPoints());
		lives = new JLabel("Lives: " + board.getLives());
		eastPanel.add(Box.createVerticalGlue());
		eastPanel.add(points);
		eastPanel.add(lives);
		eastPanel.add(Box.createVerticalGlue());

		// Adds the components to the Application
		this.add(board, BorderLayout.CENTER);
		this.add(southPanel, BorderLayout.SOUTH);
		this.add(westPanel, BorderLayout.WEST);
		this.add(eastPanel, BorderLayout.EAST);

	}

	/**
	 * Allows the buttons on the grid to be pressed
	 *
	 */
	private class MineFieldButtonListener implements ActionListener {

		// TODO: Add methods to this Stub!
		@Override
		public void actionPerformed(ActionEvent e) {
			MineFieldButton tiles = (MineFieldButton) e.getSource();
			board.stopAnimation();
			tiles.setText("X");
			points.setText("Points: " + board.getPoints());
			lives.setText("Lives: " + board.getLives());
			board.setCurrent(tiles);
			board.movePanel(tiles.getXcoor(), tiles.getYcoor());
			board.setColor(tiles.getXcoor(), tiles.getYcoor());
			board.returnFromMine(tiles.getXcoor(), tiles.getYcoor());
			
		}

	}

	/**
	 * Allows the show mines button to be pressed and do actions
	 *
	 */
	private class ShowMinesButtonListener implements ActionListener {

		// TODO Add methods to this stub!
		@Override
		public void actionPerformed(ActionEvent e) {
			if (showMines.getText().equals("Show Mines")) {
				board.showMines();
				showMines.setText("Hide Mines");
			} else {
				board.hideMines();
				showMines.setText("Show Mines");
			}
		}

	}

	/**
	 * Allows the showpath button to be pressed and perform its actions
	 *
	 */
	private class ShowPathButtonListener implements ActionListener {

		// TODO Add methods to this stub!
		@Override
		public void actionPerformed(ActionEvent e) {
			if (showPath.getText().equals("Show Path")) {
				board.showPath();
				showPath.setText("Hide Path");
			} else {
				board.hidePath();
				showPath.setText("Show Path");

			}
		}

	}

	/**
	 * Allows the give up button to be pressed and perform its action
	 *
	 */
	private class GiveUpButtonListener implements ActionListener {

		// TODO Add methods to this stub!
		@Override
		public void actionPerformed(ActionEvent e) {
			newInput = sizeOfGrid.getText();
			newDimension = Integer.parseInt(newInput);
			if (newDimension < 21 || newDimension > 4) {
				newDimension = Integer.parseInt(newInput);
			} else if (newDimension < 5) {
				newDimension = 5;
			} else if (newDimension > 20) {
				newDimension = 20;
			}

			if (giveUp.getText().equals("Give Up?")) {
				board.hideMines();
				board.hidePath();
				giveUp.setText("New Game");
				board.newStart();
			} else {

				// 1. remove
				remove(board);
				// 2. recreate
				board = new MineFieldPanel(new MineFieldButtonListener(), newDimension);
				// 3. add()
				add(board, BorderLayout.CENTER);
				// 4. revalidate
				revalidate();

				showPath.setText("Show Path");
				showMines.setText("Show Mines");
				giveUp.setText("Give Up?");
				board.newStart();

			}

		}

	}

}
