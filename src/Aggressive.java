
import org.newdawn.slick.SlickException;

public class Aggressive extends Monsters{
	
	/**Initialization Delegated
	 * @param x_pos: coordinates
	 * @param y_pos
	 * @param path: 
	 * @param map : 
	 * @param name
	 * @param stats
	 * @throws SlickException
	 */
	Aggressive(double x_pos, double y_pos, String path, SimpleMap map, String name, int[] stats)
			throws SlickException {
		super(x_pos, y_pos, path, map, name, stats);
	}
	
	
	/**Attack Player, calls setAttack to so draw draws animation instead.
	 * @param player: Player the monsters attack
	 */
	public void attack(Player player){
		double dmg= 0.01;//super.getStat().calcDamage();
		//System.out.println("dmg"+dmg);
			player.getStat().deduceHP(dmg);
			if(dmg!=0){
			super.setAttack(true);
			}
		}
}
