import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.sound.sampled.Clip;

/**
 * This is for the Galaga Project in APCS
 * The Class Ship is an overreaching ship class that
 * provides basic implementation for more specific ships
 */
public abstract class Ship extends GameSprite
{
	protected ArrayList<Missile> storage; //Stores the missiles to be painted
	private int lives; //How Many lives the ship has
	protected static int MAX_SHOTS = 2; //Maximum shots that can be fired
	private boolean canMove, 
	canFire, 
	isInvincible = false, 
	canThrottle = false,
	multipleShots = false;	
	public abstract void keyReleased(KeyEvent e);
	public abstract void keyPressed(KeyEvent e);
	public abstract void keyTyped(KeyEvent e);
	protected Clip shotPlayer; //Plays sound for shot
	public void fire() 
	{
		if(!isCanFire())
			return;
		if(storage.size() < MAX_SHOTS)
		{
			if(!multipleShots)
			{
				storage.add(new Missile((int)(GameField.BOARD_WIDTH*(double)this.toRight/100) + 
						image.getIconWidth()/2-4, GameField.BOARD_HEIGHT-this.image.getIconHeight()-35 + Missile.HEIGHT)); //Adds new missile
				GameField.setShotsFired(GameField.getShotsFired() + 1);
			}
			else
			{
				storage.add(new Missile((int)(GameField.BOARD_WIDTH*(double)this.toRight/100) + 
						image.getIconWidth()/2-4, GameField.BOARD_HEIGHT-this.image.getIconHeight() + Missile.HEIGHT));
				storage.add(new Missile((int)(GameField.BOARD_WIDTH*(double)this.toRight/100) + 
						image.getIconWidth()/2-4, GameField.BOARD_HEIGHT-this.image.getIconHeight()-35 + Missile.HEIGHT));
				storage.add(new Missile((int)(GameField.BOARD_WIDTH*(double)this.toRight/100) + 
						image.getIconWidth()/2-4, GameField.BOARD_HEIGHT-this.image.getIconHeight()+70 + Missile.HEIGHT));
				storage.add(new Missile((int)(GameField.BOARD_WIDTH*(double)this.toRight/100) + 
						image.getIconWidth()/2-35, GameField.BOARD_HEIGHT-this.image.getIconHeight()+35 + Missile.HEIGHT));
				storage.add(new Missile((int)(GameField.BOARD_WIDTH*(double)this.toRight/100) + 
						image.getIconWidth()/2+25, GameField.BOARD_HEIGHT-this.image.getIconHeight()+35 + Missile.HEIGHT));
				storage.add(new Missile((int)(GameField.BOARD_WIDTH*(double)this.toRight/100) + 
						image.getIconWidth()/2-20, GameField.BOARD_HEIGHT-this.image.getIconHeight()+35 + Missile.HEIGHT));
				storage.add(new Missile((int)(GameField.BOARD_WIDTH*(double)this.toRight/100) + 
						image.getIconWidth()/2+10, GameField.BOARD_HEIGHT-this.image.getIconHeight()+35 + Missile.HEIGHT));
				GameField.setShotsFired(GameField.getShotsFired() + 7);
			}
		}
		try {
			play();
		} catch (Exception e){}
		
	}
	
	public abstract void play();
	/**
	 * Removes the invisible Missiles that have reached the end of the screen.
	 * @return All Missiles that have flew off the screen have been gotten rid of
	 */
	public void removeInvisible()
	{
		for(int i = 0; i < storage.size(); i++)
		{
			if(!storage.get(i).isVisible())
			{
				storage.remove(i);
				i--;
			}
		}
	}
	
	/**
	 * Checks if is hit.
	 * 
	 * @param input
	 *            the input isn't null and does hass all of the aliens
	 * @return true, if is hit
	 */
	public boolean isHit(ArrayList<Alien> input)
	{
		int right = GameField.getFormattedRight(this.getRight());
		int edge = GameField.getFormattedEdge(this.getEdge());
		int nextRight;
		int nextEdge;
		
		for(Alien b: input)
		{
			for(AlienMissile a: b.list)
			{
				nextRight = GameField.getFormattedRight(a.toRightNext);
				nextEdge = GameField.getFormattedEdge(a.toEdgeNext);
				if(nextRight >= right && nextRight <= right + image.getIconWidth())
				{
					if(nextEdge >= edge &&  nextEdge <= edge + image.getIconHeight())
					{
						b.list.remove(a);
						return true;
					}
				}
			}
			
			nextRight = GameField.getFormattedRight(b);
			nextEdge = GameField.getFormattedEdge(b);
			
			if(nextRight >= right && nextRight <= right + image.getIconWidth() || 
					nextRight + b.image.getIconWidth() >= right && nextRight +
					b.image.getIconWidth() <= right + image.getIconWidth())
				if(nextEdge >= edge &&  nextEdge <= edge + image.getIconHeight() ||
					nextEdge + b.getImage().getIconHeight() >= edge &&  nextEdge + 
					b.image.getIconHeight() <= edge + image.getIconHeight())
				{
					input.remove(b);
					Alien.amountAttacking--;
					return true;
				}
		}
		return false;
	}
	//Getters and setters
	public boolean isCanFire() {
		return canFire;
	}
	public void setCanFire(boolean canFire) {
		this.canFire = canFire;
	}
	public boolean isCanMove() {
		return canMove;
	}
	public void setCanMove(boolean canMove) {
		this.canMove = canMove;
	}
	public int getLives() {
		return lives;
	}
	public void setLives(int lives) {
		this.lives = lives;
	}
	public boolean isInvincible() {
		return isInvincible;
	}
	public void setInvincible(boolean isInvincible) {
		this.isInvincible = isInvincible;
	}
	public boolean isCanThrottle() {
		return canThrottle;
	}
	public void setCanThrottle(boolean canThrottle) {
		this.canThrottle = canThrottle;
	}
 	public ArrayList<Missile> getStorage()
	{
		return storage;
	}
	public void setStorage(ArrayList<Missile> input)
	{
		storage = input;
	}
	public boolean isMultipleShots() {
		return multipleShots;
	}
	public void setMultipleShots(boolean multipleShots) {
		this.multipleShots = multipleShots;
	}



}
