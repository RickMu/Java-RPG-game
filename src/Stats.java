

import java.util.Random;

public class Stats {
	/**Attributes of a unit
	 * HP: MaxHP, CurrentHP
	 * CoolDown: Cooldown, timer to time this cooldown
	 * Range: The units attacking Range, meant for extension, but ultimately not implemented due to time
	 * Alive: if it is not dead
	 * Dmg: baseDmg and MaxDmg, sets up a dmg range each attack can deal */
	private double MaxHP, curHP;
	private int cooldown, range;
	private int timer=0,  baseDmg, maxDmg;
	private boolean alive=true;
	
	/**Initialize stats
	 * @param stats: An array of raw data, to be processed
	 */
	Stats(int[] stats){
		MaxHP=stats[0];
		curHP=MaxHP;
		baseDmg=stats[1];
		maxDmg=baseDmg+3;
		cooldown= stats[2];
		range=stats[3];
	}
	
	/**Update Stats, for attack cooldown*/
	public void updateStats(int delta){
		if(timer>0){
			timer-= delta;	
		}
		else
			timer=0;
	}
	/**Getters and Setters*/
	public double getMaxHP(){return MaxHP;}
	public double getCoolDown(){ return cooldown;}
	public double getHP(){ return curHP;}
	/**getRange built if want to add in Range Attack creatures*/
	public int getRange(){return range;}
	public double getTimer(){return timer;}
	public boolean isAlive(){
		return alive;
	}
	
	/**These setters are mostly needed for item pick ups
	 * @param reduce what to reduce cooldown by
	 */
	public void setCoolDown(double reduce){ cooldown-=reduce;}
	
	/**
	 *	Set MaxHp
	 * @param Max: int to set maxHP to
	 */
	public void setMaxHP(double Max){ 
		if(curHP==MaxHP){
			MaxHP+=Max;
			curHP=MaxHP;
		}
		else
			MaxHP+=Max;
		}
	
	/**Set base attack
	 * @param base: int to set attack to
	 */
	public void setDmg(double base){ baseDmg+=base; maxDmg=baseDmg+3;}
	public int[] getDmg(){ return new int[]{baseDmg, maxDmg};}
	
	
	/**If die respawn, talk to Elvira revive*/
	public void respawn(){
		alive=true;
		revive();
	}
	public void revive(){
		curHP=MaxHP;
	}
	
	/**Called when need to deduct this units HP
	 * @param damage
	 */
	public void deduceHP(double damage){
		if(alive){
		curHP-=damage;
		//System.out.println("curHP "+curHP);
		if(curHP<=0){
				alive=false;
				curHP=0;
			}
		}
	}

	/**Calculate damage
	 * @return Returns a dmg between the units max and lower dmg for this unit
	 */
	public double calcDamage(){
		double dmg=0;
		if(timer<=0&&alive){
			Random dmgRand = new Random();
			dmg=baseDmg+dmgRand.nextInt(4);
			timer=cooldown;
		}
		return dmg;
	}
	

	

}
