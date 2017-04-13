

import java.io.FileNotFoundException;

/* 433-294 Object Oriented Software Development
 * RPG Game Engine
 * Author: Matt Giuca <mgiuca>
 */

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/** Main class for the Role-Playing Game engine.
 * Handles initialisation, input and rendering.
 */
public class RPG extends BasicGame
{
    private World world;
    private int dir_x = 0, dir_y = 0;
    private int mouse_x, mouse_y;
    private boolean keyA, keyT;
    private static String img_player= "assets/units/player.png";
    /** Screen width, in pixels. */
    public static final int screenwidth = 800;
    /** Screen height, in pixels. */
    public static final int screenheight = 600;
    public static final int panelheight = 70;
    /** Create a new RPG object. */
    public RPG()
    {
        super("RPG Game Engine");
    }

    /** Initialise the game state.
     * @param gc The Slick game container object.
     */
    @Override
    public void init(GameContainer gc)
    throws SlickException
    {
    	try {
			world = new World();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /** Update the game state for a frame.
     * @param gc The Slick game container object.
     * @param delta Time passed since last frame (milliseconds).
     */
    @Override
    public void update(GameContainer gc, int delta)
    throws SlickException
    {
        // Get data about the current input (keyboard state).
        Input input = gc.getInput();

        // Update the player's movement direction based on keyboard presses.
        if (input.isKeyDown(Input.KEY_DOWN))
            dir_y = 1;
        if (input.isKeyDown(Input.KEY_UP))
            dir_y = -1;
        if (input.isKeyDown(Input.KEY_LEFT))
            dir_x = -1;
        if (input.isKeyDown(Input.KEY_RIGHT))
            dir_x = 1;
        if (input.isKeyPressed(Input.KEY_ESCAPE))
        	System.exit(0);
        if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
        	mouse_x= input.getMouseX();
        	mouse_y= input.getMouseY();
        	}
        if (input.isKeyPressed(Input.KEY_A) ||input.isKeyDown(Input.KEY_A) ){
        		keyA=true;
        	}
        if (input.isKeyPressed(Input.KEY_T) ){
    		keyT=true;
    	}
        	
       // System.out.println("MouseClick"+ mouse_x+ ' '+ mouse_y);
        // Let World.update decide what to do with this data.
        world.update(dir_x, dir_y, delta, mouse_x, mouse_y,keyA,keyT);
        dir_x =dir_y=0;	//So if no input I can get 0,0
        mouse_x=mouse_y=0;
        keyA=keyT=false;
    }

    /** Render the entire screen, so it reflects the current game state.
     * @param gc The Slick game container object.
     * @param g The Slick graphics object, used for drawing.
     */
    public void render(GameContainer gc, Graphics g)
    throws SlickException
    {
        // Let World.render handle the rendering.
        world.render(g);
    }

    /** Start-up method. Creates the game and runs it.
     * @param args Command-line arguments (ignored).
     */
    public static void main(String[] args)
    throws SlickException
    {
        AppGameContainer app = new AppGameContainer(new RPG());
        // setShowFPS(true), to show frames-per-second.
        app.setShowFPS(true);
        app.setDisplayMode(screenwidth, screenheight, false);
        app.start();
    }
}
