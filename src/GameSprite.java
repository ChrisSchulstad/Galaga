import javax.swing.ImageIcon;


/**
 * The Class GameSprite. This is the generic class for anything 
 * that is going to appear on the screen
 */
public abstract class GameSprite 
{
	protected int toRight, toEdge; //x,y Coordinates
	protected ImageIcon image; //Image if need be
	
	//Getters and Setters
	//As a generic Class there is nothing else much to do...
	public int getRight()
	{return toRight;}
	public void setRight(int input)
	{toRight = input;}
	
	public ImageIcon getImage()
	{return image;}
	public void setImage(ImageIcon a)
	{image = a;}

	public int getEdge()
	{
		return toEdge;
	}
	public void setEdge(int set)
	{
		toEdge = set;
	}
	
	
}
