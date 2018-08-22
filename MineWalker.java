import javax.swing.JFrame;

public class MineWalker {

	/**
	 * Creates a JFrame and adds the main JPanel to the JFrame.
	 * @param args (unused)
	 */
	public static void main(String args[])
	{
		JFrame frame = new JFrame("MineWalker");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new MineWalkerPanel(40, 40));
		frame.setSize(675,675);
		frame.setVisible(true);
	}
}