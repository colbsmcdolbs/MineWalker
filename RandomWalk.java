import java.util.Random;
import java.util.ArrayList;
import java.awt.Point;
/**
 * This program will create a randomized walk from the bottom left corner of a grid to
 * the top right hand corner.
 * @author Colby Allen
 *
 */

public class RandomWalk implements RandomWalkInterface {
	
	// Instance Variable
	// According to rubric, all must be set to private.
	private int gridSize;
	private Random rand;
	private boolean done;
	private ArrayList<Point> path;
	private Point start;
	private Point end;
	private Point current;
	
	
	
	// Constructors
	/**
	 * This sets the gridSize without the value being tied to a seed.
	 * 
	 * @param gridSizeis the size of the Grid.
	 */
	public RandomWalk(int gridSize)
	{
		this.done = false;
		this.gridSize = gridSize;
		rand = new Random();
		start = new Point(0, gridSize - 1);
		current = start;
		end = new Point(gridSize - 1, 0);
		path = new ArrayList<Point>();
		path.add(current);
		
	}
	/**
	 * This sets the grid size as well as tying the path to a seed.
	 * Professor Schmidt put a note into my p2, that this is the correct way to implement a seed.
	 * @param gridSize allows the user to input the size of the grid.
	 * @param seed allows the random walk to be tied to a seed.
	 */
	public RandomWalk(int gridSize, long seed)
	{
		this.gridSize = gridSize;
		rand = new Random(seed);
		start = new Point(0, gridSize - 1);
		current = start;
		end = new Point(gridSize -1, 0);
		path = new ArrayList<Point>();
		path.add(current);
	}
	
	/**
	 * When certain conditions are met, the walk will make one step. Until the boolean done is set to true.
	 */
	// Methods
	public void step()
	{
			int upRight = rand.nextInt(2);
			if(upRight == 0)
			{
				if(current.x != end.x)
				{
					Point newCurrent = new Point(current.x + 1, current.y);
					path.add(newCurrent);
					current = newCurrent;
				}
				else if(current.y != end.y)
				{
					Point newCurrent = new Point(current.x, current.y -1);
					path.add(newCurrent);
					current = newCurrent;
				}
				else
				{
					done = true;
				}
			}
			else
			{
				if(current.y != end.y)
				{
					Point newCurrent = new Point(current.x, current.y - 1);
					path.add(newCurrent);
					current = newCurrent;
				}
				else if(current.x != end.x)
				{
					Point newCurrent = new Point(current.x + 1, current.y);
					path.add(newCurrent);
					current = newCurrent;
				}
				else
				{
					done = true;
				}
			}
			
	}
	
	/**
	 * 
	 */
	public void createWalk()
	{
		while(done != true)
		{
			step();
		}
	}
	/**
	 * Returns the status of the done boolean.
	 * @return the status of the done boolean.
	 */
	public boolean isDone()
	{
		return done;
	}
	
	/**
	 * Returns the size of the grid.
	 * @return the size of the grid.
	 */
	public int getGridSize()
	{
		return gridSize;
	}
	
	/**
	 * Returns the start point of the walk.
	 * @return the start point of the walk.
	 */
	public Point getStartPoint()
	{
		return start;
	}
	
	/**
	 * Returns the end point of the walk.
	 * @return the end point of the walk.
	 */
	public Point getEndPoint()
	{
		return end;
	}
	
	/**
	 * Returns the current point of the program.
	 * @return the current point of the program.
	 */
	public Point getCurrentPoint()
	{
		return current;
	}
	/**
	 * Returns the path of the RandomWalk.
	 * @return the path of the RandomWalk.
	 */
	public ArrayList<Point> getPath()
	{
		ArrayList<Point> copyPath = new ArrayList<Point>();
		for (Point i : path) {
			copyPath.add(i);
		}
		return copyPath;
	}
	
	/**
	 * Returns the path that the RandomWalk takes, 
	 * as was illustrated in class with the CupOfDice example.
	 * @return the String provided.
	 */
	public String toString()
	{
		String result = "";
		for(Point p : path)
		{
			result += "[" + p.x + ", " + p.y + "] ";
		}
		return result;
	}
	// These methods are not used, but Onyx yelled at me for not having them in the code.
	@Override
	public void stepEC() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void createWalkEC() {
		// TODO Auto-generated method stub
		
	}


}
