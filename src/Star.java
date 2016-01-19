import java.awt.Color;

/**
 * This is for the Galaga Project in APCS
 * The Class Star s a star to be painted.
 */

public class Star extends GameSprite
{
	private static final Color[] colors = 
		{Color.BLACK, Color.BLUE, Color.CYAN, 
			Color.DARK_GRAY, Color.GRAY, 
			Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE,
			Color.PINK, Color.RED, Color.YELLOW
	};
	private Color starColor;
	private int count;
	public static final int length = 2;
	
	/**
	 * Instantiates a new star.
	 * 
	 * @param toRight
	 *            IN THIS CASE TO RIGHT ARE PIXELS NOT 1 TO 100
	 * @param toEdge
	 *            SAME
	 */
	public Star(int toRight, int toEdge)
	{
		this.toRight = toRight; //IN THIS CASE TO RIGHT ARE PIXELS NOT 1 TO 100
		this.toEdge = toEdge; //SAME
		count = 0;
		image = null;
		setStarColor(colors[(int) (Math.random()*colors.length)]);
	}
	
	/**
	 * Instantiates a new star.
	 * 
	 * @param toRight
	 *            IN THIS CASE TO RIGHT ARE PIXELS NOT 1 TO 100
	 * @param toEdge
	 *            SAME
	 * @param a
	 * 			is the Color of the star
	 */
	public Star(int toRight, int toEdge, Color a)
	{
		this.toRight = toRight; //IN THIS CASE TO RIGHT ARE PIXELS NOT 1 TO 100
		this.toEdge = toEdge; //SAME
		count = 0;
		setStarColor(a);
		image = null;
	}
	
	/**
	 * Move.
	 */
	public void move()
	{
		if(count%(Alien.DELAY) == 0)
			toEdge++;
		if(toEdge > GameField.BOARD_HEIGHT)
			toEdge = 0;
		count++;
	}
	
	/**
	 * Fade.
	 */
	public void fade()
	{
		if(!(count%(Alien.DELAY*2) == 0))
			return;
		
		switch(count/10 % 10)
		{
		case 0:
			break;
		case 1:
			setStarColor(getStarColor().brighter());
			break;
		case 2:
			break;
		case 3:
			setStarColor(getStarColor().brighter());
		case 4:
			break;
		case 5:
			setStarColor(getStarColor().brighter());
		case 6:
			break;
		case 7:
			setStarColor(getStarColor().darker());
		}
		
	}
	//Getters and setters
	public Color getStarColor() {
		return starColor;
	}
	public void setStarColor(Color starColor) {
		this.starColor = starColor;
	}
}
