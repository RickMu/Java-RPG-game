
import java.io.File;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.pathfinding.Mover;

public class Unit extends GameObject implements Mover {
	/**Necessary Variables*/
	static final int xOffSet=12, yOffSet=18;
	private double speed_x,speed_y; 	//Unit Velocity
	private double speed;				//Unit speed
	private int orient=1;			//Which way the image faces
	private Stats unitStat;			//Unit stats
	private SimpleMap map;			//A reference of map
	private boolean underAttack;
	private Color bar = new Color(0.8f, 0.0f, 0.0f, 0.8f);      // Red, transp
	
	
	/**Animation related variables, to test for conditions satisfying
	 * drawing animation instead of image*/
	private Animation idle=null, attack=null,move=null, magic=null;
	private boolean idleAnime=false, attackAnime=false;
	private double bioclock=3;
	static final int countDown=3;
	static final double milli=0.001;
	
	/**Necessary Getters and Setters*/
	
	double get_x_pos(){ return super.getXPos();}
	double get_y_pos(){ return super.getYPos();}
	/**For Animation Booleans, to determine when an Animation should be drawn*/
	String get_name(){return name;}
	public boolean isIdle(){return idleAnime;}
	public void hasAttacked(boolean attack) {attackAnime=attack;}
	
	/**Getting values of stats for, display, calculation etc purposes*/
	public int getRange(){ return unitStat.getRange();}
	public Stats getStat(){
		return unitStat;
	}
	
	
	 
	 /**Attacked
	  * @param dmg: damage done to the units health
	  */
	 public void damage(double dmg){
		 unitStat.deduceHP(dmg);
		 isAttacked(true);
	 }
	 
	 
	 /**To set whether the unit is Under Attack or not
	  * @param state
	  */
	 public void isAttacked(boolean state){
		 underAttack=state;
	 }
	 /**To check if the unit is under attack*/
	 public boolean getState(){
		 return underAttack;
	 }
	 
	/**Speed and set speed
	 * @speed: a speed for the unit*/
	 public double speed_x(){return speed_x;}
	 public double speed_y(){return speed_y;}
	 public void setSpeed(double speed){ this.speed=speed;}
	
	 
	 
	/**Obtains a copy of SimpleMap Object, map, so can delegate methods,
	 * int[] stats passes in raw data of the Units stat.
	 * this is used to initialize the units stat*/ 
	 
	 /**
	  * Initialization Delegated to GameObject
	  * @param x_pos
	  * @param y_pos
	  * @param path
	  * @param name
	  * 
	  * @param stats: Stats is used to initialize stats of the unit:
	  * @param map: Reference to map is kept
	  * @throws SlickException
	  */
	 public Unit(double x_pos,double y_pos,String path, SimpleMap map, String name, int[] stats)
			throws SlickException{
		super(path,x_pos,y_pos,name);
		this.map= map;
	
		unitStat=new Stats(stats);
		addAnimation(path);
	}
	
	/**Respawn the unit, in Unit if wish to add extension to respawn monsters as well*/
	public void respawn(){
		xPos=super.initXPos;
		yPos=super.initYPos;
		unitStat.respawn();
	}
	
	/**Orienting the Unit's Image and calls orientAnime, to orient Animation
	 * @param dir_x: dir_x determines the direction unit faces.
	 * @throws SlickException
	 */
	public void orient(int dir_x) 
			throws SlickException{
		if (orient!=dir_x && dir_x==-1*orient){
			objectImg=objectImg.getFlippedCopy(true,false);
    		orient=dir_x;
    		idle=orientAnime(idle);
    		move=orientAnime(move);
    		attack= orientAnime(attack);
    		magic= orientAnime(magic);
		}
	}
	
	/**
	 * Update Unit
	 * @param dir_x: Direction to move in
	 * @param dir_y: Direction to move in
	 * @param delta: GameFrame used to determine speed
	 * @throws SlickException
	 */
	 public void update(int dir_x, int dir_y, int delta)
			    throws SlickException
			    {
		 			if(unitStat.isAlive()){
		 			unit_move(dir_x, dir_y, delta);
		 			orient(dir_x);			//Orient image
		 			unitStat.updateStats(delta);
		 			if (dir_x!=0||dir_y!=0|| attackAnime){
		 				bioclock=countDown;
		 			}
		 			else
		 				bioclock-=delta*milli;
		 			}
			    }
	 
	 
	 public boolean isAlive(){
	 
		 return unitStat.isAlive();
	 }
	 
	 
	 /**Drawing health/dialogues bar
	  * @param g: Graphics to draw bars.
	  * @param xPos: xPosition on Screen of the unit
	  * @param yPos: yPosition on Screen of the unit
	  * @param text: The text to render
	  * @param BAR
	  */
	 public void drawBar(Graphics g, int xPos, int yPos, String text, Color BAR){
	    	
	    	Color VALUE = new Color(1.0f, 1.0f, 1.0f);          // White
	    	
	    	double health_percent;
	    	float posX,posY, bar_width, bar_height=20, text_x, text_y;
	    	bar_width=text.length()*10;
	    	
			posX=xPos-bar_width/2;posY=yPos-45;
			health_percent = unitStat.getHP()/unitStat.getMaxHP(); 
	
			bar_width = (int) (bar_width * health_percent);
	        
	        g.setColor(BAR);
		    g.fillRect( posX, posY, bar_width, bar_height);
		    text_x= posX+text.length()*0.2f;
	    	text_y=posY;
	    	g.setColor(VALUE);
	        g.drawString(text, text_x, text_y);
			
	    }
	
	 /**Orients, the given animation to the orientation of this unit	
	  * @param anime: The animation to re-orient
	  * @return: returns the animation
	  */
	 public Animation orientAnime(Animation anime){
			int i;
			if(anime==null){
				return null;
			}
			Animation tmp= new Animation();
			for(i=0;i<anime.getFrameCount();i++){
				tmp.addFrame(anime.getImage(i).getFlippedCopy(true, false), anime.getDuration(i));
			}
			return tmp;
		}
	
	 
	 /**Moves the unit if it can be moved, calls map.isblocked to check if
	  * @param dir_x: direction
	  * @param dir_y
	  * @param delta: Gameframe milli seconds
	  */
	 public void unit_move(int dir_x, int dir_y, int delta){
		 	/**maintain the same speed walking diagonally*/
		 	speed_x=speed_y=velocity(dir_x,dir_y,delta);
			speed_x= speed_x*dir_x;
			speed_y=speed_y*dir_y;
			
			/**if the tile is blocked don't move*/
			if(map.is_blocked((int)(xPos+speed_x+xOffSet*dir_x), (int)(yPos))){
				speed_x=0;
			}
			if(map.is_blocked((int)(xPos), (int)(yPos+speed_y+yOffSet*dir_y))){
				speed_y=0;
			}
			
			move(speed_x,speed_y);
	 }
	 
	 /**Adds Animation if there are corresponding folders in given path
	  *   Adds: Idle, Attack, Move, magic if exists
	  * @param path: path where the files stored.
	  * @throws SlickException
	  */
	 public void addAnimation(String path) throws SlickException{
			int i,j;
			Image curImg;
			String directory;
			Animation tmp = null;
			int time=0;
			System.out.println("This is path Units "+path);
			directory=path.substring(0, path.length()-4);
			
			File [] decents, children;
			File ancestor, parent; 
			ancestor =new File(directory);
			
			/**Lists all files in the directory*/
			decents= ancestor.listFiles();	
			
			if(decents!=null){
			
			/**Runs through all those files and add the pngs if file exits*/
			for(i=0;i<decents.length;i++){
				parent= decents[i];
				children= parent.listFiles();
			
				if(parent.getPath().contains("idle")){
					idle=new Animation();
					tmp=idle;
					time =200;
				}
				else if(parent.getPath().toLowerCase().contains("attack")){
					attack=new Animation();
					tmp=attack;
					time =(int) (unitStat.getCoolDown()/(decents.length+2));
				
				}
				else if(parent.getPath().toLowerCase().contains("move")){
					move=new Animation();
					tmp=move;
					time =100;
				}
				else if(parent.getPath().toLowerCase().contains("magic")){
					magic=new Animation();
					tmp=magic;
					time =150;
				}
				else
					continue;
				
				/**Add the pngs to the animation*/
				for(j=0;j<children.length;j++){
					curImg= new Image(children[j].getPath());
					tmp.addFrame(curImg,time);
					}
				}
			}
		}
	
	 /**Draw all possible animation Draw A certain Animation instead of unit image. if condition satisfies
	  * 
	  * @param x: Positions to draw animation
	  * @param y
	  */
	 public void drawAnimation(float x, float y){
				if(this.isAlive()){
				if(idle!=null&&bioclock<=0&& !attackAnime){
						idle.draw(x-36, y-36);
						idleAnime=true;
						
				}
				else if (attack!=null&&attackAnime){
				
					attack.draw(x-36, y-36);
					int tmpX=-72;
					if(magic!=null){
						if(orient==1){
							tmpX=0;
						}
						magic.draw(x+tmpX+orient*magic.getFrame()*35,y-32);
					}
					if(attack.getFrame()==attack.getFrameCount()-1){					
						attackAnime=false;
						attack.restart();
						if(magic!=null)
							magic.restart();
					}
					
				}
				else if (move!=null && (this.speed_x()!=0||this.speed_y()!=0)){
					move.draw(x-36, y-36);
				}
				else{
					idleAnime=false;
					super.draw(x, y);
				}
			}
	 }
	
	/**Drawing method for unit
	 * 
	 * @param g: Graphics to draw bars
	 * @param x: Positions to draw
	 * @param y
	 */
	 public void draw(Graphics g, int x, int y){
		if(unitStat.isAlive())
			drawBar(g, x, y,this.name,bar);
		if(unitStat.isAlive()){
			drawAnimation( x, y);
		}
		
	}
	
	 /**Maintain the movement speed same diagonally and horizontal or vertically
	  * @param dir_x: Directions and Delta game Frame
	  * @param dir_y
	  * @param delta
	  * @return
	  */
	 public double velocity(int dir_x, int dir_y,int delta){
		if(dir_x!=0 && dir_y!=0){
			return (Math.sqrt(speed*speed/2)*delta);
		}
		return speed*delta;
	}
	
	/**Move the player
	 * @param x_in: x distance the Player supposed to move
	 * @param y_in	y distance the Player supposed to move
	 */
	public void move(double x_in, double y_in){
		xPos+=x_in;
		yPos+=y_in;

	}
	
}
