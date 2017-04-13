

import java.util.Random;

import org.newdawn.slick.SlickException;

public class Passive extends Monsters {
	final static double milli = 0.001;
	final static double countDown = 3, fleeCount=5;
	private boolean attacked;
	private int dir[]={0,0};
	double bioclock=0;
	
	/**Initialization Delegated*/
	Passive(double x_pos, double y_pos, String path, SimpleMap map, String name, int[] stats) throws SlickException {
		super(x_pos, y_pos, path, map, name,stats);
	}
	
	/**Update the passive, Switch direction when it has flown in that direction for 3 secs
	 * @param coordX: coordinates of the unit that attacked it.
	 * @param coordY
	 * @param delta: Delta used in super
	 * @throws SlickException
	 */
	public void passiveUpdate(int coordX, int coordY, int delta) throws SlickException{
		//Problem here.
			if(bioclock<=0){
				bioclock= countDown;
				wonderAround();
				attacked=false;
			}
			if(super.getState()){
				super.isAttacked(false);
				attacked=true;
				bioclock=fleeCount;
			}
			if (attacked){
				flee(coordX,coordY);
			}
			super.update(dir[0], dir[1], delta);
			
			bioclock-=delta*milli;
		}
	/**Returns a random combination of directions*/
	  public void wonderAround(){
			int[] dirX={0,1,-1}, dirY=dirX;
			Random dirRand = new Random();
			int xRand= dirRand.nextInt(3);
			int yRand= dirRand.nextInt(3);
			dir[0]=dirX[xRand];
			dir[1]= dirY[yRand];
		}
	  
	/**Flee from the given coordinate: Unit's coordinate
	 * @param coordX: Coordinate of the unit attacked it
	 * @param coordY
	 */
	public void flee(int coordX, int coordY){
		/**If current direction gets closer to the player, 
		 * call wonderAround to set new directions */
		while((coordX-super.getXPos())/Math.abs(coordX-super.getXPos())==dir[0] 
				&&((coordY-super.getYPos())/Math.abs(coordY-super.getYPos())==dir[1])){
			wonderAround();
			}
		/**If it is not moving in any direction reset speed again*/
		if(super.speed_x()==0||super.speed_y()==0){
			wonderAround();
		}
		
	}
	

}
