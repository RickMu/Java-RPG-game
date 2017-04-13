

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
/**
 *  YOu can toArray and Arraylist <String[]> 
 *  to String[][] lines. Do ArrayList.toArray(lines);
 * @author Administrator
 *
 */
public class Utility {
	/**Static constants used to make returning an GameObject's image
	 *  easier and also reading in data easier*/
	static final String BANDIT="bandit", BAT="bat", NECROMANCER="necromancer",
	PEASANT= "peasant", PLAYER= "Hero", PRINCE= "prince", SHAMAN="shaman",
	SKELETON= "skeleton", DEATHBLADE= "blade", AMULET="amulet", ELIXIR="elixir", SWORD="sword",
	TOME ="tome";	
	static final String UnitsFile ="data/specific_data.txt",
						StatsFile= "data/general_data.txt",
						ItemsFile= "data/items.txt",
						DialogueFile= "data/dialogue.txt";
	static final String Units="assets/units/", PNG=".png", Item="assets/items/";


	/**
	 * Determine if the monster is aggressive or not
	 * @param name: Name of the monster
	 * @return: True if it is aggressive
	 */
	public boolean isAggressive(String name){
		if(name.toLowerCase().contains(BAT)){
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public String imagePath(String name){
		String types[]={BANDIT,BAT,DEATHBLADE,SKELETON,PRINCE};
		String image = null;
				
		if(name.contains("Player")){ image=PLAYER;}
		if (name.contains("Elvira")){ image=SHAMAN;}
		if (name.contains("Garth")){image=PEASANT;}
		if( name.contains("Draelic")){image=NECROMANCER;}
		for (int i=0; i<types.length;i++){
			if(name.toLowerCase().contains(types[i])){
				image= types[i];
				break;
			}
		}
		
		return Units+image+PNG;
	}
	public String itemPath(String name){
		String types[]={ AMULET, ELIXIR, SWORD,TOME};
		String image = null;
		for(int i=0; i<types.length;i++)
			if(name.toLowerCase().contains(types[i])){
				image= types[i];
				break;
			}
		
		
		return Item+image+PNG;
	}
	
	
	
	/**Read in dialgoues for Villagers
	 * @return Dialogues in a 2D array
	 * @throws FileNotFoundException
	 */
	public String[][] dialogues() throws FileNotFoundException{
		ArrayList <String[]> lines= new ArrayList<>();
		ArrayList <String> dialogues= new ArrayList<>();
		int i=0;
		File file= new File(DialogueFile);
		
		Scanner sc= new Scanner(file);
		while(sc.hasNextLine()){
			
			String c = sc.nextLine().trim();
			if(c.endsWith(":")&& !dialogues.isEmpty()){
				String[] diag= new String[dialogues.size()];
				dialogues.toArray(diag);
				lines.add(diag);
				dialogues.clear();
				
			}
			if(!c.endsWith(":")){
				dialogues.add(c);
			}
		}
		String [][] script= new String[lines.size()][];
		lines.toArray(script);
		return script;
	}
	
	/**Finds the stats for a Unit given its name
	 * @param name: Find according to name of the unit
	 * @return: Returns the stat in an array
	 * @throws FileNotFoundException
	 */
	public int[] findStats(String name) throws FileNotFoundException{
		int i;
		int stats[]=null;
		File file= new File(StatsFile);
		Scanner sc= new Scanner(file);
		while(sc.hasNextLine()){
			String[] c = sc.nextLine().split("(\\s+)(?<=\\D)(?=\\d)");
			stats= new int[c.length-1];

			if(name.contains(c[0])){
				for(i=0;i<stats.length;i++){
					stats[i]= Integer.parseInt(c[i+1]);	
				}
				break;
			}
		}
		
		return stats;
	
	}
	/**Items information are read and parsed, Name is stored in a String array, Attributes in an
	 * double Array.
	 *  Returned by assigning them to an object Array;*/
	
	public Object[] readItems() throws FileNotFoundException{
		int i;
		ArrayList<String> itemName= new ArrayList<>();
		ArrayList<double[]> attributes= new ArrayList<>();
		File file= new File(ItemsFile);
		Scanner sc= new Scanner(file);
		while(sc.hasNextLine()){
			String[] c = sc.nextLine().split("(\\s+)(?<=\\D)(?=\\d)");
			double tmp[]= new double[c.length-1];
			for(i=0;i<c.length;i++){
				if(i==0){
					itemName.add(c[i]);
				}
				else{
					tmp[i-1]=Double.parseDouble(c[i]);
				}
			}
			attributes.add(tmp);	
		}
				String[]name= new String[itemName.size()];
				name=itemName.toArray(name);
				double[][] attrb = new double[attributes.size()][];
				for(i=0;i<attributes.size();i++){
					attrb[i]= attributes.get(i);
				}
		return new Object[]{name,attrb};
	}
	/**Read in coordinates and name for all Units, name and coordinates are returned
	 * @return: Returns an object array containing int array and string array;
	 * @throws FileNotFoundException
	 */
	public Object[] unitsData() throws FileNotFoundException{
		ArrayList <String> names = new ArrayList<>();
		ArrayList <double[]> position = new ArrayList<>();
		File file= new File(UnitsFile);
		Scanner sc = new Scanner(file);
		while(sc.hasNextLine()){
			String[] c = sc.nextLine().split("(?<=\\D)(?=\\d)");
			
			double[] points=new double[2];
			for(int i = 0 ;i<c.length;i++){
				if(i==0){
					names.add(c[i].trim());
					//System.out.println("This is name"+ c[i]);
				}
				else{
					if(c[i].endsWith(",")){
					c[i]=c[i].substring(0,c[i].length()-1);}
					points[i-1]= Double.parseDouble(c[i]);
				}
				if(i==2){position.add(points);}
				}}
		String[] title= new String[names.size()];
		names.toArray(title);
		double[][] coord= new double[position.size()][];
		for(int i=0; i<position.size();i++)
			coord[i]= position.get(i);
		return new Object[]{title, coord};
	}
}
