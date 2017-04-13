

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class GameObject {
	/**Every Game Objects has a name, Image, Coordinates and 
	 * Initial coordinates also a path to the image*/
	Image objectImg;
	double xPos, yPos,initXPos,initYPos;
	String path, name;
	
	
	/**Initializing GameObjects
	 * @param path: Path to image
	 * @param xPos: Coordinates of the gameobject
	 * @param yPos
	 * @param name: name of the game object
	 * @throws SlickException
	 */
	GameObject(String path, double xPos, double yPos, String name) throws SlickException{
		this.xPos=initXPos=xPos;
		this.yPos=initYPos=yPos;
		this.path=path;
		this.name=name;
		objectImg= new Image(path);
	}
	/**Draw Image
	 * @param x: position to draw the object
	 * @param y
	 */
	public void draw(float x, float y){
		objectImg.drawCentered(x, y);
	}
	/**Necessary Getters and Setters*/
	public double getXPos() { return xPos;}
	public double getYPos() { return yPos;}
	public double getinitXPos() { return initXPos;}
	public double getinitYPos() { return initYPos;}
	public void setXPos(double xPos) {this.xPos=xPos;}
	public void setYPos(double yPos) { this.yPos=yPos;}
	public String returnName(){ return name;}
}
