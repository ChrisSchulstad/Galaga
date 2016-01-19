import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
/**
 * This is for the Galaga Project in APCS
 * The Class RedAlien. an alien who circles his attack pattern
 */
public class RedAlien extends Alien {
	
	private double angle;
	private int toSpot; //See Blue Alien if asking
	private static final int circleDiameter = 30;
	
	/**
	 * Instantiates a new red alien.
	 * 
	 * @param toEdge
	 *            the to edge same as the other
	 * @param toRight
	 *            the to right same as the others
	 */
	public RedAlien(int toEdge, int toRight)
	{
		this.toEdge = toEdge;
		this.toRight = toRight;
		row = toEdge;
		column = toRight;
		angle = 0;
		BufferedImage img = null;
		isMovingRight = true;
		
		try{
			img = ImageIO.read(getClass().getResource("RedAlien.png"));
		}
		catch(IOException e){
			e.printStackTrace();
		}
		points = basePoints + 100;
		image = new ImageIcon(img);
		list = new ArrayList<AlienMissile>();
		health = baseHealth+ 1;
		toSpot = 0;
		
	}
	
	/* (non-Javadoc)
	 * @see Alien#attack()
	 */
	public void attack()
	{
		if(isAttacking && count > MUSIC_DELAY)
		{
			double radTemp;
			switch(toSpot)
			{
				case 0:
					break;
				case 1:
					if(count % (DELAY*1.5) == 0)
						toEdge++;
					if(GameField.getFormattedEdge(this) >= GameField.BOARD_HEIGHT*.6 && 
							GameField.getFormattedEdge(this) <= GameField.BOARD_HEIGHT * .61)
						fire();
					if(GameField.getFormattedEdge(toEdge) >= GameField.BOARD_HEIGHT)
						toSpot++;
					break;
				case 2:
					if(count % DELAY == 0)
						angle +=2;
					if(isLeft())
						radTemp = Math.toRadians(angle);
					else
						radTemp = Math.toRadians(180-angle);
					toRight = column + circleDiameter/2+ (int)(Math.cos(radTemp)*circleDiameter);
					toEdge = 95 - (int)(Math.sin(radTemp)*circleDiameter/2);
					if(angle >= 180)
					{
						angle = 0;
						toEdge = 0;
						toSpot++;
					}
					break;
				case 3:
					if(count%DELAY == 0)
						toEdge++;
					if(toEdge == row)
					{
						if(count % DELAY == 0)
							Alien.amountAttacking--;
						isAttacking = false;
						isMoving = true;
						toSpot = 0;
						toRight = column;
					}
					break;
				default:
					break;	
			}
		}
	}
	
	/**
	 * Start attack. @see BlueAlien.startAttack()
	 */
	public void startAttack()
	{
		if(toSpot == 0)
		{
			toSpot++;
			Alien.amountAttacking++;
			isMoving = false;
			isAttacking = true;
		}
	}
	
	/**
	 * Checks if is left.
	 * 
	 * @return true, if is left
	 */
	private boolean isLeft()
	{
		return column < 50;
	}
}
