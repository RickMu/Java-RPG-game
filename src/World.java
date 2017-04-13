

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;

/* 433-294 Object Oriented Software Development
 * RPG Game Engine
 * Author: <Rick> <Ymu1>
 */

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;

/** Represents the entire game world.
 * (Designed to be instantiated just once for the whole game).
 */
public class World
{
	
	
	/*Constants*/
	//How many tiles are needed, How far is the very top left tile from the player's tile
	private static final int Xblocks= 13, Yblocks=11, X_blockoff=6, Y_blockoff=5,x_off=32, y_off=60;
	//Centre of screen
	private static final int Xscreen_centre= 400, Yscreen_centre=300;
	private static final int MAX_PATH= 10000;
	private AStarPathFinder pathFinder;
	//Map properties.
	private double destX, destY;
	/*Variables*/
	private Player player;
	private Aggressive[] attackers;
	private Passive[] nonattackers;
	private Villager[] NPCs;
	private Items[] equipments; 
	private int sx,sy;	
	//Place to draw the player, changes when camera freeze
	private int draw_x,draw_y;	
	//Where to render the map on screen
	private int render_x, render_y; 
	//The top left coordinate of the Camera;
	private Camera view;
	private Utility tools= new Utility();
	private SimpleMap map= new SimpleMap("assets/map.tmx","assets/");
	private Image panel;
	private Animation dragonAnimation= new Animation();
	//private AStarPath path;
	
    /** Create a new World object. 
     */
    public World()
    throws SlickException, FileNotFoundException
    {
    	
    	panel= new Image("assets/panel.png");
    	//Creates all Game Objects in this World: 
    	create_units();
    	//Initialize a pathFinder for later use
    	pathFinder = new AStarPathFinder(map, MAX_PATH, true);   	
    	//Initialize Camera to follow player.
    	view= new Camera(player);
    	
     }
   
    
    
    /**Reading in data of all GameObjects*/
    public void create_units() throws FileNotFoundException, SlickException{
		/**Local Variables
		 * to capture Dialogues: lines[][], 
		 * coordinates:coord [][], 
		 * names: names[]
		 * To capture returned data from tools, Object[] is used*/
    	String[][] lines;
		double[][] coord;
		String[]names;
		Object[] obj;
		
		/*Might be able to combine the two init_item and init_units*/
		/** Obtain data and initialized Animate Objects*/
		lines=tools.dialogues();
		obj=  tools.unitsData();	
		names=(String[]) obj[0];
		coord=(double[][]) obj[1];		
		init_units(names,coord,lines);
		
		/**Obtained data and Initialized Inanimate Objects*/
		Object[] itemObj =tools.readItems();
		names=(String[]) itemObj[0];
		coord=(double[][]) itemObj[1];	
		init_items(names,coord);
	}

    /**Initialize 
     * 
     * @param name: Name of the GameObject
     * @param coord: Coordinate of GameObject
     * @param lines: Dialogues used by Villagers only
     * @throws SlickException
     * @throws FileNotFoundException
     */
    public void init_units(String[] name, double[][]coord, String[][]lines) throws SlickException, FileNotFoundException{
    	//Each 
    	NPCs= new Villager[lines.length];
    	nonattackers= new Passive[30];
    	attackers= new Aggressive[name.length-34];
    	
       	int sentinel=0, i;
    	int stats[];
    	
    	for (i=0; i<name.length;i++){
    			//Find extra data required to initialize Units: Stats, Image
    			stats=tools.findStats(name[i]);
    			String path1=tools.imagePath(name[i]);
    			
    			
    			if(i==0){ player= new Player(coord[i][0],coord[i][1],path1,map,name[i],stats);}
    			
    			else if(i<4){
    				NPCs[i-1]= new Villager(coord[i][0],coord[i][1],path1,map,name[i],stats, lines[i-1]);
    				//System.out.println("This is name" + name[i] +" "+i);
    				//NPCs[i]= new Unit(coord[i][0],coord[i][1],path1,map,name[i],stats);
    			}
    			else {
    				if(!tools.isAggressive(name[i])){
    					nonattackers[i-4]=new Passive(coord[i][0],coord[i][1],path1,map,name[i],stats);
    					sentinel=i+1;
    				}
    				else 
    					attackers[i-sentinel]= new Aggressive(coord[i][0],coord[i][1],path1,map,name[i],stats);
    			}
    		}
    
    }
    	
    /**Initializing Items
     * @param names: Item names
     * @param coord: Item coordinates
     * @throws SlickException
     */
   public void init_items(String[] names,double[][]coord) throws SlickException{
	   int i;
	   equipments= new Items[names.length];
	   for(i=0;i<names.length;i++){
		   String path= tools.itemPath(names[i]);
		   equipments[i]= new Items(coord[i][1],coord[i][2],(int) coord[i][0],path,names[i]);
	   }
   }
    
  
    
    
    /** Update the game state for a frame.
     * 
     */
   /**Updating all GameObjects and also view in World
    *@param dir_x The player's movement in the x axis (-1, 0 or 1).
    * @param dir_y The player's movement in the y axis (-1, 0 or 1).
    * @param delta Time passed since last frame (milliseconds).
    * @param mouse_x: xCoord for mouse click
    * @param mouse_y: yCoord for mouse click
    * @param keyA: if keyA is pressed
    * @param keyT: if keyT is pressed
    * @throws SlickException
    */
    public void update(int dir_x, int dir_y, int delta, double mouse_x, double mouse_y, boolean keyA, boolean keyT) 
    throws SlickException
    {
    	 view.update();		//Update Camera Pos
    	//PathFinding Algorithm
    	int i, dir[] = {0,0}; 
    	if((mouse_x!=0&&mouse_y!=0)){
    		destX=mouse_x+view.getMinX(); destY= mouse_y+view.getMinY();
    	}
    	if ((destX!=0||destY!=0)){
    			dir=findPath(destX,destY,player);
    			dir_x=dir[0];
    			dir_y=dir[1];
    			if (dir_x==0 &&dir_y==0)
    				destX=destY=0;
    	}
    	/**
    	 * This is weird the update, look
    	 */
	    player.playerUpdate(dir_x, dir_y, delta);
	    for(i=0; i<nonattackers.length;i++){
	       	nonattackers[i].passiveUpdate((int)player.get_x_pos(),(int)player.get_y_pos(),delta);
	    }	    
	    for(i=0; i<attackers.length;i++){
	    	attackers[i].monsterUpdate(0,0,delta);
	    }
	    for(i=0;i<NPCs.length;i++){
	    	NPCs[i].updateNPC(delta);
	    }
	    Monsters[] tmp, tmp1,tmp2;
	    Items item;
	   
	    if(keyA){
	    	item=itemInSight(equipments);
	    	tmp1=monstersInRange(50,0,nonattackers);
	    	tmp2=monstersInRange(50,0,attackers);
	    	// Join the two returned Monster Arrays.
	    	Monsters[] creatures= new Monsters[tmp1.length+tmp2.length];
	    	for(i=0;i<tmp1.length+tmp2.length;i++){
	    		if(i<tmp1.length)
	    		creatures[i]=tmp1[i];
	    		else
	    			creatures[i]=tmp2[i-tmp1.length];
	    	}

	    	player.attack(creatures);
	    	if(item!=null){
	    		player.pickUp(item);
	    	}
	     }
	    
	    Villager person=villagerInRange(50,NPCs);
	    if(keyT){
	    	if(person!=null)
	    		person.talkTo();
	    }
    	tmp=monstersInRange(150,30,attackers);
	    for (i=0;i<tmp.length;i++){
		    	dir=findPath(player.get_x_pos(),player.get_y_pos(),tmp[i]);
		    	tmp[i].monsterUpdate(dir[0], dir[1], delta);
	   
	    }
	    tmp= monstersInRange(50,0,attackers);
	    Aggressive[] creatures= new Aggressive[tmp.length];
	    for(i=0;i<tmp.length;i++){
	    	creatures[i]=(Aggressive)tmp[i];
	    	creatures[i].attack(player);
	    	
	    }
	    
	    variableRender(view.get_charac());	//Freezing camera condition satisfies
	    draw_character(view.get_charac());
	     //Tests the monsters inRange
    }
 
    /**Finds all Monsters in a Given Range
     * 
     * 
     * 
     * @param upper: Maximum radius
     * @param lower: Minimum Radius
     * @param creatures: The array of all monsters to search through
     * @return: An array of monsters in Range
     */
	public Monsters[] monstersInRange(int upper, int lower, Monsters[] creatures){
		/**ArrayList is used for its flexibility*/
		ArrayList <Monsters> inRange= new ArrayList<>();
		double radius;
		/**Checks all the monsters*/
		for(int i=0; i<creatures.length;i++){
			if (view.is_inView((int)creatures[i].get_x_pos(),(int)creatures[i].get_y_pos())){	
				radius=distBetween(player,creatures[i]);
				if (radius>lower&&radius<upper){
					inRange.add(creatures[i]);
				}	
			}
		}
		/**Turn the ArrayList into an Array to return*/
		Monsters[] monsters= new Monsters[inRange.size()];
		inRange.toArray(monsters);
		return monsters;
	}
	
	/**Finds all Villagers in a Given Range
	 * 
	 * @param upper:Maximum radius
	 * @param NPCs: All villagers
	 * @return a Villager in Range
	 */
	public  Villager villagerInRange (int upper, Villager [] NPCs){
		double  radius;
		for(int i=0; i<NPCs.length;i++){
			if (view.is_inView((int)NPCs[i].getXPos(),(int)NPCs[i].getYPos())){	
				radius=distBetween(player,NPCs[i]);
				if (radius<upper){
					return NPCs[i];
				}	
			}
		}
		return null;
	}
	
	/**Find all items
	 * 
	 * Did not combine the two methods itemInSight, villagerInRange, because 
	 * otherwise need a GameObject method and would need Downcasting when using it. 
	 * 
	 * @param items: The array of all items
	 * @return: Items in range:
	 */
	public Items itemInSight(Items[] items){
		
		double  dx,dy, radius;
		Items nothing=null;
		for(int i=0; i<items.length;i++){
			if (view.is_inView((int)items[i].getXPos(),(int)items[i].getYPos())){	
			radius=distBetween(player,items[i]);
			if (radius<30){
				return items[i];
				}
		
			}
		}
		return nothing;
	}
	
	/**Finds the distance between two units
	 * @param unit1:  
	 * @param unit2:
	 * @return the distance
	 */
	public double distBetween(GameObject unit1, GameObject unit2){
		double dx= unit1.getXPos()-unit2.getXPos();
		double dy= unit1.getYPos()-unit2.getYPos();
		double radius = Math.sqrt(dx*dx+dy*dy);
		return radius;
	}
	
	/**Employs AStarPathFinder, to find path, 
	 * 
	 * @param tx:Xcoord to go 
	 * @param ty:Ycoord to go
	 * @param user: the Unit that uses this method. 
	 * @return: returns the direction to go
	 */
	public int [] findPath(double tx, double ty, Unit user){
		Path path = null;
		/**Destination tiles*/
		int px= (int)(tx/map.getTileSize());
		int py=(int)(ty/map.getTileSize());
		/**User tiles*/
		int cx= (int)(user.get_x_pos())/map.getTileSize();
		int cy=(int)(user.get_y_pos())/map.getTileSize();
		int steps=0, dirX=0,dirY=0;
		
		/**if player reached destination tile, or blocked, don't use path finder*/
		if(!map.blocked(null, px, py)&&!(cx==px && cy==py)){
			path = pathFinder.findPath(null, cx, cy, px, py);
		}
		
		/**Needed to find the specific position that the mouse clicked on, not just the tile*/
		if(!map.is_blocked(tx, ty)&&Math.abs(tx-user.get_x_pos())>5)
			dirX=(int)((tx-user.get_x_pos())/Math.abs(tx-user.get_x_pos()));
		if(!map.is_blocked(tx, ty)&&Math.abs(ty-user.get_y_pos())>5)
			dirY=(int)((ty-user.get_y_pos())/Math.abs(ty-user.get_y_pos()));
		
		/**Catch what pathFinder returns, if pathFinder is initialized*/
		if(path!=null){
			if (px!=cx ||py!=cy){
				dirX=(int) (path.getX(steps+1)-cx);
				dirY=(int) (path.getY(steps+1)-cy);
				}
		}
		return new int []{dirX,dirY};
	}
	
    
    /**Rendering Map and View*/
    public void variableRender(Unit unitFollow){
    		sx=calcTile(view.getxPos());
    		render_x= -modCalcTile(view.getxPos())-x_off;
    		sy=calcTile(view.getyPos());
    		render_y=-modCalcTile(view.getyPos())-y_off;
    }

    /**Draw Character? Need Change*/
    public void draw_character(Unit unitFollow){
    	draw_x= Xscreen_centre;
    	if (unitFollow.get_x_pos()!=view.getxPos())
    		draw_x= xposOnScreen(unitFollow);
    	draw_y=Yscreen_centre;
    	if (unitFollow.get_y_pos()!=view.getyPos())
    		draw_y= yposOnScreen(unitFollow);  		
    }
    
    /**Draw all GameObjects*/
    public void unit_draw(Graphics g){
    	int i;
    	player.draw(g, draw_x, draw_y);
    	Items items;
    	
    	
    	/**Monsters have the same drawing method as Unit*/
    	Unit tmp;
    	for (i=0; i<attackers.length;i++){
    		tmp= attackers[i];
    		if (view.is_inView((int)tmp.getXPos(),(int)tmp.getYPos()))	
			tmp.draw(g, xposOnScreen(tmp), yposOnScreen(tmp));
    	
    	}
    	for (i=0; i<nonattackers.length;i++){
    		tmp= nonattackers[i];
    		if (view.is_inView((int)tmp.get_x_pos(),(int)tmp.get_y_pos())){	
    		tmp.draw(g,xposOnScreen(tmp), yposOnScreen(tmp));
			
    		}
    	
    	}
    	/**Items have a separate drawing methods, as it is needed to determine 
    	 * if they are picked up*/
    	for(i=0; i<equipments.length;i++){
    		items= equipments[i];
    		if (view.is_inView((int)items.getXPos(),(int)items.getYPos())){	
    			items.itemDraw(xposOnScreen(items),yposOnScreen(items));
    		}
    	}
    	
    	Villager person;
    	/**Villager needs to have dialogues drawn, and the dialogues depend on 
    	 * player's certain status*/
    	for(i=0; i<NPCs.length;i++){
    		person= NPCs[i];
    		if (view.is_inView((int)person.getXPos(),(int)person.getYPos())){	
    			person.drawVillager(player,g, xposOnScreen(person),yposOnScreen(person));
    		}
    	}
    	
    }
 
    /**Methods for deriving useful meta data*/
    public int calcTile(double pos){
		return (int)(pos/map.getTileSize());
	}
	public int modCalcTile(double pos){
		return (int)(pos%map.getTileSize());
	}
	public int xposOnScreen(GameObject user){
		return (int)(user.getXPos()-view.getMinX());
	}
	public int yposOnScreen(GameObject user){
		return (int)(user.getYPos()-view.getMinY());
	}
    
	/**Rendering Player Panel*/
	 public void renderPanel(Graphics g)
	    {
	        // Panel colours
	        Color LABEL = new Color(0.9f, 0.9f, 0.4f);          // Gold
	        Color VALUE = new Color(1.0f, 1.0f, 1.0f);          // White
	        Color BAR_BG = new Color(0.0f, 0.0f, 0.0f, 0.8f);   // Black, transp
	        Color BAR = new Color(0.8f, 0.0f, 0.0f, 0.8f);      // Red, transp

	        // Variables for layout
	        String text;                // Text to display
	        int text_x, text_y;         // Coordinates to draw text
	        int bar_x, bar_y;           // Coordinates to draw rectangles
	        int bar_width, bar_height;  // Size of rectangle to draw
	        int hp_bar_width;           // Size of red (HP) rectangle
	        int inv_x, inv_y;           // Coordinates to draw inventory item
	        int []dmgRange;

	        double health_percent;       // Player's health, as a percentage

	        // Panel background image
	        panel.draw(0, RPG.screenheight - RPG.panelheight);

	        // Display the player's health
	        text_x = 15;
	        text_y = RPG.screenheight - RPG.panelheight + 25;
	        
	        g.setColor(LABEL);
	        g.drawString("Health:", text_x, text_y);
	        text = Integer.toString((int)player.getStat().getHP())+"/"+Integer.toString((int)player.getStat().getMaxHP());                                 // TODO: HP / Max-HP

	        bar_x = 90;
	        bar_y = RPG.screenheight - RPG.panelheight + 20;
	        bar_width = 90;
	        bar_height = 30;
	        health_percent = (player.getStat().getHP()/player.getStat().getMaxHP());                         // TODO: HP / Max-HP
	        hp_bar_width = (int) (bar_width * health_percent);
	        text_x = bar_x + (bar_width - g.getFont().getWidth(text)) / 2;
	        g.setColor(BAR_BG);
	        g.fillRect(bar_x, bar_y, bar_width, bar_height);
	        g.setColor(BAR);
	        g.fillRect(bar_x, bar_y, hp_bar_width, bar_height);
	        g.setColor(VALUE);
	        g.drawString(text, text_x, text_y);

	        // Display the player's damage and cooldown
	        text_x = 200;
	        g.setColor(LABEL);
	        g.drawString("Damage:", text_x, text_y);
	        text_x += 65;
	        dmgRange= player.getStat().getDmg();
	        text = Integer.toString(dmgRange[0])+"-"+Integer.toString(dmgRange[1]);                                    // TODO: Damage
	        g.setColor(VALUE);
	        g.drawString(text, text_x, text_y);
	        text_x += 55;
	        g.setColor(LABEL);
	        g.drawString("Rate:", text_x, text_y);
	        text_x += 55;
	        text = Integer.toString((int)player.getStat().getTimer()/100);                                    // TODO: Cooldown
	        g.setColor(VALUE);
	        g.drawString(text, text_x, text_y);

	        // Display the player's inventory
	        g.setColor(LABEL);
	        g.drawString("Items:", 420, text_y);
	        bar_x = 490;
	        bar_y = RPG.screenheight - RPG.panelheight + 10;
	        bar_width = 288;
	        bar_height = bar_height + 20;
	        g.setColor(BAR_BG);
	        g.fillRect(bar_x, bar_y, bar_width, bar_height);

	        inv_x = 520;
	        inv_y = RPG.screenheight - RPG.panelheight
	            + ((RPG.panelheight) / 2);
	        Items[] invent;
	        invent= player.getInventory();
	        //System.out.println(invent.length);
	        for(int i=0;i<invent.length;i++)              // TODO
	        {
	        	invent[i].draw(inv_x, inv_y);
	            inv_x += 72;
	        }
	    }

	 /**Rendering Updates to Screen*/
    public void render(Graphics g)
    throws SlickException
    {
    	
    	Color BOUNDARY = new Color(0.9f, 0.9f, 0.4f);          // Gold
    	
    	
    	
    	
    	/**Drawing Maps*/
    	map.render(render_x, render_y,
    		sx-X_blockoff, sy-Y_blockoff, Xblocks,Yblocks);
    	
    	/**Draw All GameObjects*/
    	unit_draw(g);
    	/**Draw Player Panel*/
    	renderPanel(g);
    	
    	
    	/**Scaled to draw Mini Map*/
    	g.scale(0.15f, 0.15f);
    	map.render(render_x, render_y,
        		sx-X_blockoff, sy-Y_blockoff, Xblocks+5,Yblocks+5);
    	unit_draw(g);
    	g.resetTransform();
    	
    	/**Drawing borders for the mini map*/
    	g.setColor(BOUNDARY);
        g.fillRect(0f, 153f, 188f, 8f);
        g.fillRect(180f, 0f, 8f, 153f);
    	
     }


}
