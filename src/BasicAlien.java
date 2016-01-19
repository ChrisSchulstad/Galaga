import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * This is for the Galaga Project in APCS
 * The Class BasicAlien. The alien looks like a bee 
 * and its attack is very simple, just a move forward.
 *  It is in the middle row of the attack formation
 */
public class BasicAlien extends Alien
{
	
	/**
	 * Instantiates a new basic alien, saves the column 
	 * and row as a backup in case need to be reset
	 * as well as loads the image, initializes points,
	 * creates a new list of Alien Missiles and set health
	 * 
	 * @param toEdge
	 *            the to edge is a percentage from 
	 *            left to right, of 100, how up or
	 *            down the alien is the y coordinate.
	 * @param toRight
	 *            the to right is the same as toEdge but
	 *            it is the x coordinate.
	 */
	public BasicAlien(int toEdge, int toRight)
	{
		this.toEdge = toEdge;
		this.toRight = toRight;
		column = toRight;
		row = toEdge;
		BufferedImage img = null;
		isMovingRight = true;
		try{
			img = ImageIO.read(getClass().getResource("BasicAlien.png"));
		}
		catch(Exception e){e.printStackTrace();}
		points = basePoints;
		image = new ImageIcon(img);
		list = new ArrayList<AlienMissile>();
		health = baseHealth+1;
	}
	
	/* (non-Javadoc)
	 * @see Alien#attack()
	 */
	public void attack() {
		if(isAttacking && count > MUSIC_DELAY)
		{
			
			if(count % DELAY == 0)
				toEdge++;
			
			if(toEdge == row + 1)
			{
				Alien.amountAttacking++;
				toEdge++;
			}
			if(GameField.getFormattedEdge(this) >= GameField.BOARD_HEIGHT*.6 && 
					GameField.getFormattedEdge(this) <= GameField.BOARD_HEIGHT * .61)
				fire();
			else if(GameField.getFormattedEdge(toEdge) >= GameField.BOARD_HEIGHT)
				toEdge = 0;
			else if(toEdge == row)
			{
				isAttacking = false;
				if(count % DELAY == 0)
					Alien.amountAttacking--;
			}
			
		}
			
		
	}

}
