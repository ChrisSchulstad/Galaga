/**
 * This is for the Galaga Project for APCS
 * The Class Alien. A standard subclass used for 
 * generating different types of aliens. Used as 
 * a shell for different types of Aliens
 */
public abstract class Alien extends GameSprite
{
	protected int health, //How many shots it takes
	points, //How many points each alien gets for killing it
	row, //Keeps the running row so that the alien can revert back to this
	column, //To keep a running column so that the alien can revert back to this
	count = 0; //As a mediator count to not go so fast
	protected volatile java.util.ArrayList<AlienMissile> list; //Like Storage for ship
	protected static javax.sound.sampled.Clip hitPlayer, //HitPlayers play the sound when the alien is hit
	flyPlayer; //Same as hit but a sound when it is flying
	protected static javax.sound.sampled.AudioInputStream hitStream,
	flyStream;
	protected static boolean isMovingRight; //Self Explanitory
	protected boolean isAttacking; 			//""""
	protected boolean isMoving = true;		//""""
	protected volatile static int amountAttacking = 0, //Running count of how many are attacking so not too many are attacking
			DELAY = 16,
			baseHealth = 0,
			basePoints = 100; //Delay is increased to slow the game down
	public static final int MAX_DELAY = 14, //SE (Self Explanitory)
			MUSIC_DELAY = 2500; //How many counts the music takes before the game starts
	public static final String hitPath = "mus/Galaga_Kill.wav", //SE
			flyPath = "mus/Flying_Enemy.wav"; //SE
	public static final int[][] BasicPosition = 
		{{18,15}, {26, 15}, {34,15}, 
		{42,15}, {50, 15}, {58, 15}, 
		{66, 15}, {74, 15},{82, 15}, 
		}; //The X,Y Coordinates of the Basic Alien formation
	public static final int[][] RedPosition = 
		{{15, 20}, {23, 20}, {31, 20}, 
		{39, 20}, {47, 20}, {55, 20}, 
		{63, 20}, {71, 20}, {79, 20},
		{87, 20}}; //Same but Red Formation
	public static final int[][] BossPosition = 
		{{15, 10}, {25, 10}, {35, 10}, 
		{45, 10}, {55, 10}, {65, 10}, 
		{75, 10}, {85, 10}}; //Same but boss Formation
	public abstract void attack(); //A very specific attack for each alien

	//Getters and Setters
	public int getHealth()
	{
		return health;
	}
	public void setHealth(int input)
	{
		health = input;
	}
	public int getPoints()
	{
		return points;
	}
	public void setPoints(int input)
	{
		points = input;
	}
	
	/**
	 * Returns the alien to the position
	 */
	public void returnToPosition() 
	{
		toRight = column;
		toEdge = row;
		
	}
	
	//subtracts one from health
	public void hit()
	{
		health--;
	}
	
	/**
	 * Fires an Alien Missile in the -2, 0, 2 slope
	 */
	public void fire() 
	{
		list.add(new AlienMissile(this, -2));
		list.add(new AlienMissile(this, 0));
		list.add(new AlienMissile(this, 2));	
	}
	
	/**
	 * Checks if is hit.
	 * 
	 * @param storage is not Null
	 *            the storage variable comes from the ship class.
	 *            The array scans all missiles to see if it would work
	 * @return true, if is hit
	 */
	public boolean isHit(java.util.ArrayList<Missile> storage) 
	{
		if(storage.size() == 0)
			return false;
		
		int right = GameField.getFormattedRight(this);
		int edge = GameField.getFormattedEdge(this);
		
		for(Missile a: storage)
		{
			if(a.getRight() >= right && a.getRight() <= right + image.getIconWidth())
				if(a.getEdge() >= edge &&  a.getEdge() <= edge + image.getIconHeight())
				{
					storage.remove(a);
					if(isAttacking)
						Alien.amountAttacking--;
					GameField.setShotsHit(GameField.getShotsHit() + 1);
	   	 			return true;
				}
		}
		return false;
	}

	/**
	 * Moves the Alien back and forth
	 */
	public void move()
	{
		if(isMoving && count > MUSIC_DELAY)
		{
			
			if(count % (DELAY*1.5) == 0) //Not so fast
			{
				if(isMovingRight)
				{
					toRight++;
					column++;
				}
				else
				{
					toRight--;
					column--;
				}
			
			if(toRight <= 3)
			{
				isMovingRight = true;
				toRight+= 2;
			}
			
			else if(toRight >= 97)
			{
				isMovingRight = false;
			}
			}
		}
		count++;
		
	}
}
