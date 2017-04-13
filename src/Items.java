

import org.newdawn.slick.SlickException;

public class Items extends GameObject {

	/**Could potentially be better if items are split in to multiple classes for future extension
	 * But for this assignment only they would be really thin classes*/
	
	/**Effect: Stats Enhance
	 * onMap: is it picked.
	 */
	private double effect;
	private boolean onMap=true;
	
	/**Initialize items, store effect, delegate to GameObject
	 * @effect: Stats Enhancement*/
	Items(double x, double y, int effect, String path, String name) throws SlickException{
		super(path,x,y,name);
		this.effect=effect;
	}
	
	/**Getters and Setters
	*@Returns Whether the item is still on Map*/
	public boolean itemAvailable(){
		return onMap;
	}
	
	/**if item is Picked up, onMap no longer true,
	 * @param action: if the player picked it up.
	 * @param stat: Passes the stat the item enhances.
	 */
	public void pickedUp(boolean action, Stats stat){
		if(onMap==true){
			enhance(stat);
		}
		onMap=!action;
	}
	/**Draws the item, delegated*/
	public void itemDraw(float x,float y){
		if (onMap){
			super.draw(x, y);
		}
	}
	/**Set onMap=true again, and its coordinate where player had dropped it
	 * @param player: item is removed it get dropped
	 */
	public void itemRemoved(Player player){
		onMap=true;
		super.setXPos(player.getXPos());
		super.setYPos(player.getYPos());
	}
	
	/**What stats to enhance
	 * @param stat: Stats to be enhanced
	 */
	public void enhance(Stats stat){
		
		if(name.contains("Amulet of Vitality")){
			stat.setMaxHP(effect);
		}
		if(name.contains("Sword of Strength")){
			stat.setDmg(effect);
		}
		if(name.contains("Tome of Agility")){
			stat.setCoolDown(effect);
		}
	}
	/**Delegate to GameObject, get Name'
	 * @return Returns name of the item*/
	public String getName(){
		return super.returnName();
	}
}
