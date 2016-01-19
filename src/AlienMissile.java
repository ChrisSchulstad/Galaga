/**
 * This is for the Galaga Project in APCS
 * The Class AlienMissile is a missile caried by an object of type Alien
 */
public class AlienMissile extends Missile implements Projectile
{
	private static final int[] slopes = 
		{-3, -2, -1, 0, 0, 1, 2, 3}; //Selects a random slope to fire if none is provided
	protected int toRightNext, //This is the next x coordinate of the missile
	toEdgeNext, //The next y coordinate of the missile
	slope, //What slope the missile is at
	count; //Mediate how fast the missile gets fired
	private static final int LENGTH = 2; //Length of how long the missile is
	
	
	/**
	 * Instantiates a new alien missile.
	 * 
	 * @param Owner is the owner of said missile
	 */
	public AlienMissile(Alien Owner)
	{
		super(Owner.getRight(), Owner.getEdge());
		int r;
		if(GameField.getFormattedRight(Owner) < GameField.BOARD_WIDTH/2)
			r = (int) (Math.random() * slopes.length/2);
		else
			r = (int) (Math.random()*slopes.length/2 + slopes.length/2);
		slope = slopes[r];
		count = 0;
	}
	
	/**
	 * Instantiates a new alien missile. This is the second constructor for a specific slope.
	 * 
	 * @param a
	 *            the Alien a is the Owner of the missile
	 * @param slope
	 *            the slope is a slope that is presumed to be in the array slopes or within
	 *            {-3, -2, -1, 0, 0, 1, 2, 3}
	 */
	public AlienMissile(Alien a, int slope)
	{
		super(a.getRight() + GameField.BOARD_WIDTH/(4*a.getImage().getIconWidth())+1, a.getEdge()+ GameField.BOARD_HEIGHT/(4*a.getImage().getIconHeight()));
		this.slope = slope;
		count = 0;
		
	}
	
	
	/* (non-Javadoc)
	 * @see Missile#move()
	 */
	public void move() {
		if(count % Alien.DELAY == 0)
		{
			switch(slope)
			{
				case 0:
					toEdge++;
					toRightNext = toRight;
					toEdgeNext = toEdge+LENGTH;
					break;
				case -1:
					toEdge++;
					toRight++;
					toEdgeNext = (int)(toEdge + LENGTH * Math.sqrt(2));
					toRightNext = (int)(toRight + LENGTH * Math.sqrt(2));
					break;
				case 1:
					toEdge++;
					toRight--;
					toEdgeNext = (int)(toEdge + LENGTH * Math.sqrt(2));
					toRightNext = (int)(toRight - LENGTH * Math.sqrt(2));
					break;
				case -2:
					toEdge+= 2;
					toRight++;
					toEdgeNext = (int)(toEdge + LENGTH * Math.sqrt(5));
					toRightNext = (int)(toRight + LENGTH * Math.sqrt(5));
					break;
				case 2:
					toEdge+=2;
					toRight--;
					toEdgeNext = (int)(toEdge + LENGTH * Math.sqrt(5));
					toRightNext = (int)(toRight - LENGTH * Math.sqrt(5));
					break;
				default:
					break;
			}
		}
		count++;
		
	}
	
	/* (non-Javadoc)
	 * @see Missile#isVisible()
	 */
	public boolean isVisible() {
		return (toEdge < GameField.BOARD_HEIGHT && toEdge > 0) && (toRight < GameField.BOARD_WIDTH && toRight > 0);
	}

	

}
