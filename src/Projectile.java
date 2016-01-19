
/**
 * The Interface Projectile is a common apparatis 
 * for all missile objects in this game.
 */
public interface Projectile
{
	
	/**
	 * Moves the Missile in whatever its 
	 * specifized direction is
	 */
	public abstract void move();
	
	/**
	 * Checks if is visible.
	 * 
	 * @return true, if is visible
	 */
	public abstract boolean isVisible();
}
