import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JButton;

@SuppressWarnings("serial")
/**
 * Creates a single JButton peg that will be utilized  by the MineFieldPanel
 * @author Colby Allen
 *
 */
public class MineFieldButton extends JButton{
	
	public static final Color[] COLORS = {Color.white, Color.GREEN, Color.YELLOW, 
										  Color.ORANGE, Color.RED, Color.BLACK, 
										  Color.CYAN, Color.MAGENTA};
	private int x1;
	private int y1;

	
	public MineFieldButton()
	{
		this.setPreferredSize(new Dimension(40,40));
		this.setBackground(Color.WHITE);	
	}
	
	public void position(int x, int y)
	{
		x1 = x;
		y1 = y;
	}
	
	public int getXcoor()
	{
		return x1;
	}
	
	public int getYcoor()
	{
		return y1;
	}


}
