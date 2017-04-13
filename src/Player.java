

import java.util.ArrayList;

import org.newdawn.slick.SlickException;

public class Player extends Unit{
	/**ArrayList for inventory, more convenienty than an Array*/
	private ArrayList <Items> inventory= new ArrayList<>();
	/**Players Speed*/
	static final double PlayerSpeed=0.25;
	
	/**Initializing a Player delegated to ancestor classes
	 * @param x_pos: Positions
	 * @param y_pos
	 * @param path: Image path
	 * @param map: a reference of map
	 * @param name: name
	 * @param stats: Player's stats
	 * @throws SlickException
	 */
	public Player(double x_pos, double y_pos, String path, SimpleMap map, String name,int[] stats)
			throws SlickException {
		super(x_pos, y_pos, path, map, name, stats);
		super.setSpeed(PlayerSpeed);
		
	}
	
	/**Using toArray returning an array of inventory.
	 * @return Returns all items in the inventory of player
	 */
	public Items[]  getInventory(){
		Items[] invent= new Items[inventory.size()];
		inventory.toArray(invent);
		return invent;
	}
	
	/**Updates the Player, if dead respawn
	 * @param dir_x: Directions player should move in
	 * @param dir_y
	 * @param delta: Number of milli seconds lapsed, used to track time and calc velocity
	 * @throws SlickException
	 */
	public void playerUpdate(int dir_x,int dir_y,int delta) throws SlickException{
		super.update(dir_x, dir_y, delta);
		if(!super.isAlive()){
			super.respawn();
		}
	}
	
	/**Attack an array of Monsters
	 * @param creatures: An array of monsters to be attacked
	 */
	public void attack(Monsters[] creatures){
		int i;
		double dmg= super.getStat().calcDamage();
		if(dmg!=0){
			super.hasAttacked(true);
		}
		for(i=0;i<creatures.length;i++){
			creatures[i].damage(dmg);
		}
	}
	

	/**Checking if the players' inventory contains a certain item
	 * @param name: The name to check
	 * @return
	 */
	public boolean inventoryContains(String name){
		for(int i=0;i<inventory.size();i++){
			if(inventory.get(i).getName().contains(name)){
				return true;
			}
		}
		return false;
	}
	
	/**if inventory has this item, remove from ArrayList and call remove from Items
	 * @param name: name of item to be removed
	 */
	public void removeFromInvent(String name){
		for(int i=0;i<inventory.size();i++){
			if(inventory.get(i).getName().contains(name)){
				inventory.get(i).itemRemoved(this);
				inventory.remove(i);
			}
		}
	}
	
	/**Pick, add item to inventory, and call pickUp in Item
	 * @param the item picked up
	 */
	public void pickUp(Items item){
		if(item.itemAvailable()){
			item.pickedUp(true, super.getStat());
			inventory.add(item);
		}
	}

}
