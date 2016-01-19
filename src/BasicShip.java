import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;


/**
 * This is for the Galaga Project in APCS
 * The Class BasicShip.
 * Comes from the Hierarchy to maintain a systematic movement
 */
public class BasicShip extends Ship
{
	private Set<Integer> pressed = new HashSet<Integer>(); //Keeps a list of the pressed keys to iterate over them
	
	/**
	 * Instantiates a new basic ship. 
	 * Instigates toRight to be the Middle, 
	 * Saves the Image, and Creates a new
	 * arraylist for missiles
	 */
	public BasicShip()
	{
		toRight = 50; //An integer between 0 and 100 that is a percent of how far over the ship is
		BufferedImage img = null;
		
		try {
			//img = ImageIO.read(new File(getClass().getClassLoader().getResource("/pic/GalagaShip.png")));
			img = ImageIO.read(getClass().getResource("GalagaShip.png"));
		} catch (Exception e) {
			e.printStackTrace();
		} //This all just gets the image, but Java needs you to catch an error even if the file is in the source folder
		image = new ImageIcon(img); //Actually saves it
		this.setEdge((int) ((GameField.BOARD_HEIGHT-(double)getImage().getIconHeight())*100.0/GameField.BOARD_HEIGHT));
		storage = new ArrayList<Missile>(2);
		setLives(2);
		AudioInputStream stream = null;
		try {
			stream = AudioSystem.getAudioInputStream(getClass().getResource("Fire_Sound_Effect.wav"));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		try {
			shotPlayer = AudioSystem.getClip();
			shotPlayer.open(stream);
		} catch (Exception e){e.printStackTrace();}
		
	}

	
	/* 
	 * Adds a new missile to the arraylist to be painted in the gamefield paint
	 */
	
	/* (non-Javadoc)
	 * @see Ship#play()
	 */
	public void play(){
		shotPlayer.loop(0);
	}
	

    /**
	 * Key released.
	 * 
	 * @param e given by Java
	 * It gets rid of the pressed Key queue
	 *          
	 */
    public void keyReleased(KeyEvent e) 
    {
    	pressed.remove(e.getKeyCode());
    }
    
    public void keyPressed(KeyEvent e)
    {
    	pressed.add(e.getKeyCode());
    	
    	if(pressed.contains(KeyEvent.VK_W))
    	{
    		GameField.setStartingUp(false);
    		GameField.playIntro();
    	}
    	
    	if(pressed.contains(KeyEvent.VK_R))
    	{
    		GameField.setReset(true);
    	}
    	
    	if(isCanThrottle() && pressed.contains(KeyEvent.VK_UP))
    	{
    		Alien.DELAY++;
    	}
    	if(isCanThrottle() && pressed.contains(KeyEvent.VK_DOWN))
    	{
    		if(Alien.DELAY >=2)
    			Alien.DELAY--;
    	}
    	
    	
    	if(isCanMove())
    	{
    	if(pressed.contains(KeyEvent.VK_SHIFT))
    	{
    		if(pressed.contains(KeyEvent.VK_RIGHT))
    		{
    			if(toRight <= 90)
    				toRight+=16;
    		}
    		if(pressed.contains(KeyEvent.VK_LEFT))
    		{
    			if(toRight >= 4)
    				toRight -= 16;
    		}
    	}
    	
    	for(int in: pressed)
    	{
    		switch(in)
    		{
    		case KeyEvent.VK_RIGHT:
    			if(toRight <= 90)
    				toRight+=4;
    			break;
    		case KeyEvent.VK_LEFT:
    			if(toRight >= 4)
    				toRight -= 4;
    			break;
    		case KeyEvent.VK_SPACE:
    			fire();
    			break;
    		case KeyEvent.VK_ENTER:
					SwingUtilities.invokeLater(new Runnable(){
						public void run()
						{
							GameField.setPause(!GameField.isPause());
						}
					}); 
    			break;
    		case KeyEvent.VK_M:
    			toggleMax();
    			break;
    		case KeyEvent.VK_I:
    			toggleInvincible();
    			break;
    		case KeyEvent.VK_T:
    			toggleThrottle();
    			break;
    		case KeyEvent.VK_C:
    			toggleRainbow();
    			break;
    		case KeyEvent.VK_N:
    			toggleShots();
    			break;
    		default:
    			break;
    		}
    		
    	}
    	}
    	
    }


	private void toggleShots() {
		setMultipleShots(!isMultipleShots());
		
	}


	/**
	 * Toggle rainbow.
	 */
	private void toggleRainbow() {
		Missile.setColorful(!Missile.isColorful());
	}


	/**
	 * Toggle throttle.
	 */
	private void toggleThrottle() {
		setCanThrottle(!isCanThrottle());
		
	}


	/**
	 * Toggle invincible.
	 */
	private void toggleInvincible() {
		setInvincible(!isInvincible());
		
	}


	/**
	 * Toggle max.
	 */
	private void toggleMax() {
		if(MAX_SHOTS == 2)
			MAX_SHOTS = 500;
		else
			MAX_SHOTS = 2;
		
	}


	public void keyTyped(KeyEvent e) {//No Implementation Needed}
	

}
}
