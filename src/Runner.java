import javax.swing.JFrame;

/**
 * The Class Runner. It just effects the outlying look of the game
 */

@SuppressWarnings("serial")
public class Runner extends JFrame {
	
	public static final int BOARD_WIDTH = 480, BOARD_HEIGHT = 740;
	/**
	 * Instantiates a new runner. This is the class that defaults everything in the menu scenarios For Example.
	 */
	public Runner()
	{
		add(new GameField()); //Add a new Gamefield
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(BOARD_WIDTH, BOARD_HEIGHT); //Used static constants
        setLocationRelativeTo(null);
        setTitle("Galaga");
        setResizable(false);
        setVisible(true);
       
	}
	
	public static void main(String[] args) 
	{
		new Runner();	
	}
	

}
