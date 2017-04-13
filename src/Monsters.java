

import org.newdawn.slick.SlickException;

public class Monsters extends Unit{
	static final double monsterSpeed=0.2;
	
	/**Initializing Monsters
	 * Delegated to parent: all explanations found in parent constructor
	 * @param x_pos
	 * @param y_pos
	 * @param path
	 * @param map
	 * @param name
	 * @param stats
	 * @throws SlickException
	 */
	Monsters(double x_pos,double y_pos,String path, SimpleMap map, String name, int[] stats)
			throws SlickException{
		super(x_pos,y_pos,path,map,name, stats);
		super.setSpeed(monsterSpeed);
		}	
	/**Used to incur the animation related to the unit when attacking
	 * @param attacked: if the monster has attacked true.
	 */
	public void setAttack(boolean attacked){super.hasAttacked(attacked);}

	/**Update the Monster
	 * Delegated to parent: explanations foudnn there
	 * @param dirX
	 * @param dirY
	 * @param delta
	 * @throws SlickException
	 */
	public void monsterUpdate(int dirX, int dirY, int delta) throws SlickException{
			super.update(dirX, dirY, delta);
	}
}
