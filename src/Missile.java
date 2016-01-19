import java.awt.Color;

/**
 * The Class Missile. A simple missile object that has coordinates 
 * and a few other variables.
 */
public class Missile extends GameSprite implements Projectile {
	
	public final static int HEIGHT = 10, WIDTH = 3; //SE
	private Color myColor; //The color of the missile which defaults to green
	private static int count = 0; //Counts to make a rainbow effect
	private static boolean isColorful = false; //If it is going to use the colors
	private static final Color[] arr = 
		{Color.RED, Color.ORANGE, Color.YELLOW, 
		Color.GREEN, Color.CYAN, Color.BLUE,
		Color.MAGENTA, Color.PINK, Color.WHITE
		}; //RAINBOWS
	
	/**
	 * Instantiates a new missile.
	 * 
	 * @param right
	 *            the right coordinate NOT A 0-100
	 * @param edge
	 *            the edge coordinate NOT A 0-100
	 */
	public Missile(int right, int edge)
	{
		toRight = right;
		toEdge = edge;
		image = null;
		if(isColorful())
		{
			setMyColor(arr[count]);
			count++;
			if(count > arr.length-1)
				count = 0;
		}
		else
			setMyColor(Color.GREEN);
	}
	
	//Moves the Missile toward the edge
	/**
	 * Instantiates a new missile.
	 * 
	 * @param right
	 *            the right
	 * @param edge
	 *            the edge
	 * @param input
	 *            the input color is missile color
	 */
	public Missile(int right, int edge, Color input)
	{
		toRight = right;
		toEdge = edge;
		image = null;
		setMyColor(input);
	}
	
	/* (non-Javadoc)
	 * @see Projectile#move()
	 */
	public void move()
	{
		toEdge-=2;
	} 
	
	/* (non-Javadoc)
	 * @see Projectile#isVisible()
	 */
	public boolean isVisible()
	{
		return toEdge > 0;
	}

	public Color getMyColor() {
		return myColor;
	}

	public void setMyColor(Color myColor) {
		this.myColor = myColor;
	}

	public static boolean isColorful() {
		return isColorful;
	}

	public static void setColorful(boolean isColorful) {
		Missile.isColorful = isColorful;
	}
	

}
