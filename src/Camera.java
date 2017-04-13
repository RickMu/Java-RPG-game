

/* SWEN20003 Object Oriented Software Development
 * RPG Game Engine
 * Author: <Your name> <Your login>
 */

import org.newdawn.slick.SlickException;

/** Represents the camera that controls our viewpoint.
 */
public class Camera
{

    /** The unit this camera is following */
    private Unit unitFollow;
    
    /** The width and height of the screen */
    /** Screen width, in pixels. */
    private final static int screenwidth=800;
    /** Screen height, in pixels. */
    private final static int screenheight=600;
    private static final int PixelMin=0, PixelMax=6912;
    
    /** The camera's centre position in the world, in x and y coordinates. */
    private double xPos;	
    private double yPos;
    /**Variables used for testing if the camera has hit a boundary*/
    private double minX, minY, maxY,maxX;
    
    /** Update the game camera to recentre it's viewpoint around the player 
     */
    public void update()
    	    throws SlickException
    	    {
    			tempMinMax(unitFollow.getXPos(),unitFollow.getYPos());
    			/**check if need to freeze camera*/
    			if (minX>=PixelMin && maxX<=PixelMax){
    	    			xPos= unitFollow.getXPos();
    			}
    			if (minY>=PixelMin && maxY<=PixelMax){
    				yPos= unitFollow.getYPos();
    			}
    	    }
   /**Getters, to get camera centre position/Min/Max and the unit it follows*/
    public double getxPos() {
        return xPos;
    }
    public double getyPos() {
        return yPos;
    }
    public Unit get_charac(){
    	return unitFollow;
    }

    public double getMinX(){
        return xPos-screenwidth/2;
    }
    
    /** Returns the maximum x value on screen 
     */
    public double getMaxX(){
    	return  xPos+screenwidth/2;
    }
    
    /** Returns the minimum y value on screen 
     */
    public double getMinY(){
    	return yPos-screenheight/2;
    }
    
    /** Returns the maximum y value on screen 
     */
    public double getMaxY(){
    	return yPos+screenheight/2;
    }

    /**Constructor*/
    public Camera(Unit player)
    {
    	unitFollow= player;	//Following player
    }
    
    
    /**
     * @param x_pos: xPosition with respect to the entire world
     * @param y_pos: yPosition 
     * @return: if this position is within the current view window.
     */
    public boolean is_inView(int x_pos, int y_pos){
    	if(x_pos>=getMinX() && x_pos<=getMaxX() && y_pos>=getMinY() &&y_pos<=getMaxY())
    		return true;
    	return false;
    }
    
    /**
     * A method to help this class, easily determine whether the window has moved to the 
     * edge of the map
     * @param x_base: centre coordinates of the camera
     * @param y_base
     */
    private void tempMinMax(double x_base, double y_base){
    	maxX= x_base+screenwidth/2;
    	minX= x_base-screenwidth/2;
    	maxY= y_base+screenheight/2;
    	minY= y_base-screenheight/2;
    }
   
    /** Tells the camera to follow a given unit. 
     * not used
     */
    public void followUnit(Unit unitFollow)
    throws SlickException
    {
    }
    
}