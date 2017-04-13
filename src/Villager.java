

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class Villager extends Unit {
	/**Variables Required;
	 * Dialogues:
	 * Village MinMax: Boundaries, So Villagers don't walk out of village, 
	 * timer: bioclock used for CountDown,
	 * 		 CountDown for RandomWalk, 
	 * 		count used for CoolDown 
	 * 		Cooldown for Dialogue Display
	 * Dir[]: Directions to move in:
	 * Color: Color of dialogue bar
	 * talkTo: Boolean if the villager is talkedTo
	 * */
	
	/**Could potentially be better to split villagers into separate classes
	 * more friendly for adding extra future extension, but not done because 
	 * it would result in thin classes, as I didn't consider extending them*/
	private String[] dialogues;
	static final int VillageMinX=460, VillageMaxX=1010, VillageMinY=520, VillageMaxY=870;
	static final double milli = 0.001, VillagerSpeed=0.05;
	static final int countDown=3, cooldown=4;
	private double bioclock=0;
	private double count=0;
	private int dir[]={0,0};
	private Color bar = new Color(0.0f, 0.0f, 0.0f, 0.8f);   // Black, transp
	private boolean talkTo=false;
	
	/**Initialize Villagers and stores its lines
	 * Delegating initialization to parent classes
	 * @param x_pos
	 * @param y_pos
	 * @param path
	 * @param map
	 * @param name
	 * @param stats
	 * Lines are dialogues, stored in Villager.
	 * @param lines
	 * @throws SlickException
	 */
	public Villager(double x_pos, double y_pos, String path, SimpleMap map, String name, int[] stats, String[] lines)
			throws SlickException {
		super(x_pos, y_pos, path, map, name, stats);
		super.setSpeed(VillagerSpeed);
		dialogues= lines;
		for(int i=0; i<lines.length;i++){
			System.out.println(lines[i]);
		}
	}
	public String[] allDialogues(){return dialogues;}
	
	
	/**Update NPC
	 * @param delta: used as a clock to track time that has gone by
	 * Time to display dialogue, Time to change moving direction
	 * @throws SlickException
	 */
	public void updateNPC(int delta) throws SlickException{
		if(bioclock<=0){
			bioclock= countDown;
			wonderAround();
		}
		
		/**The duration to display the dialogue for*/
		if(count>0)
			count-=delta*milli;	
		if(count<=0){
			count=0;
			talkTo=false;
		}
		notInBound();
		bioclock-=milli*delta;
		super.update(dir[0], dir[1], delta);
	}
	
	/**if villager is talkedTo, 
	 * initiate count clock to determine when to stop displaying dialogue*/
	public void talkTo(){
		count=cooldown;
	}
	
	/**
	 * Draw Villager Method
	 * @param player: A reference to player
	 * @param g: Graphics to draw
	 * @param posX: Coordinates to draw the player
	 * @param posY
	 */
	public void drawVillager(Player player, Graphics g, int posX, int posY){
		super.draw(g,posX, posY);
		String line=null;
		/**When the villagers are in Idle, returns the last line*/
		if(super.isIdle()){
			line=idleTalk();
		}
		
		/**If count clock is initiated, draw dialogue*/
		if(count>0){
			
			if(get_name().contains("Elvira")){
				line= talkToElivira(player);				
			}
			if(get_name().contains("Prince")){
				line=talkToPrince(player);
			}
			if(get_name().contains("Garth")){
				line=talkToGarth(player);
			}
		}
		if(line!=null)
			super.drawBar(g, posX, posY-15,line, bar);
		
	}
	/**Returns a random direction like in Passive Monsters*/
	  public void wonderAround(){
			int[] dirX={0,1,-1}, dirY=dirX;
			Random dirRand = new Random();
			int xRand= dirRand.nextInt(3);
			int yRand= dirRand.nextInt(3);
			
			if(dir[0]!=0||dir[1]!=0){
				dir[0]=0;
				dir[1]=0;
			}
			else{
				dir[0]=dirX[xRand];
				dir[1]= dirY[yRand];
			}
		}
	 
	  /**If walking outside of village change direction*/
	public void notInBound(){
		if(this.getXPos()<=VillageMinX ||this.getXPos()>=VillageMaxX)
			dir[0]*=-1;
		if(this.getYPos()<=VillageMinY ||this.getYPos()>=VillageMaxY)
			dir[1]*=-1;
			
	}
	
	
	
	/**Talking to Elvira
	 * @param player: A reference to player, 
	 * villagers need to know certain stats about player
	 * @return: The dialogue incurred
	 */
	private String talkToElivira(Player player){
		if(player.getStat().getHP()!=player.getStat().getMaxHP()||talkTo){
			player.getStat().revive();
			talkTo=true;
			return dialogues[1];
		}	
		return dialogues[0];
	}
	
	
	/**Talking to Prince
	 * param and return are analogous to the previous method
	 * @param player
	 * @return
	 */
	private String talkToPrince(Player player){
		
		if(player.inventoryContains("Elixir of Life")||talkTo){
			talkTo=true;
			player.removeFromInvent("Elixir of Life");
			return dialogues[1];
		}
		return dialogues[0];
		
	}
	
	
	/**Talking to Prince
	 * param and return are analogous to the previous method
	 * @param player
	 * @return
	 */
	private String talkToGarth(Player player){
		System.out.println(player.inventoryContains("Amulet of Vitality"));
		if(!player.inventoryContains("Amulet of Vitality")){
			return dialogues[0]; 
		}
		if(!player.inventoryContains("Sword of Strength")){
			return dialogues[1]; 
		}
		if(!player.inventoryContains("Tome of Agility")){
			return dialogues[2]; 	
		}
		
		return dialogues[3];
		
	}
	/**The last dialogues is the line to be displayed when character is in idle*/
	public String idleTalk(){
		return dialogues[dialogues.length-1];
	}
	
}
