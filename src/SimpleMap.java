

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

class SimpleMap implements TileBasedMap{
	/** mapProperty is needed for AStarPath finding, built in path finding algorithm.
	 * The Static Variables are so the character cannot walk out of the map*/
	private int[][] mapProperty;
	private TiledMap map;
	static final int xRestrict=6, yRestrict=18;
	static final int WALKABLE =0;
	
	/**Initialize game Map and its properties*/	
	SimpleMap(String path1, String path2) throws SlickException{
		map= new TiledMap(path1, path2);
		mapProperty = new int[getHeightInTiles()][getWidthInTiles()];
		initTileBasedMap();				
	}
	
	/**Restricts the domain on Map that an Unit is allow to walk on
	 * @param tx: coordinates in pixels
	 * @param ty
	 * @return: true if tile is blocked Otherwise false;
	 */
	public boolean is_blocked(double tx, double ty){
		int tile_x, tile_y;
		
		if((int)tx<xRestrict || (int)tx>(getMapPix()-xRestrict))
			return true;
		if((int)ty<yRestrict || (int)ty>(getMapPix()-yRestrict))
			return true;
		
		
		tile_x=(int)tx/getTileSize();
		tile_y=(int)ty/getTileSize();
		//Returns True if this tile is blocked 
		return (mapProperty[tile_x][tile_y]!=WALKABLE);	
	}
	
	/**Gets the id of the Tile
	 * @param sx: Coordinate in tiles, not pixels
	 * @param sy
	 * @return
	 */
	 public int getTileId(int sx, int sy){
			return map.getTileId(sx,sy, 0);
		}
	
	 /** Tile's Property: blocked or not
	  * @param sx: Coordinates in Tiles
	  * @param sy
	  * @return: 1 for blocked, 0 for not
	  */
	public int tileProperty(int sx, int sy){
			int terrian;
			String s;
			terrian = getTileId(sx,sy);
			s= map.getTileProperty(terrian, "block", "0");
			return Integer.parseInt(s);
	}
		
	/**Render Map Method
	 * @param render_x: positions to start rendering
	 * @param render_y
	 * @param sx: TileId to render 
	 * @param sy
	 * @param x_block: how many times to render
	 * @param y_block
	 */
	public void render(int render_x, int render_y, int sx, int sy, int x_block, int y_block){
		map.render(render_x, render_y, sx, sy, x_block,y_block);
	}
	
	/**Returns the Map dimension in Pix, as the Map is square
	 * @return
	 */
	public int getMapPix(){
		return getTileSize()*getHeightInTiles();
	}
	/**
	 * @return: Return tile size;
	 */
	public int getTileSize(){
		return map.getTileHeight();
	}
	
	/**Records information about the map in a table, used for AStarPathFinding
	 * and also because it is convenient, for checking if the tile is blocked*/
	public void initTileBasedMap(){
		for (int i=0;i<getHeightInTiles();i++){
			for(int j = 0; j<getWidthInTiles(); j++){
				mapProperty[i][j]= tileProperty(i,j);
			}
		}
	}
	@Override
	/**Necessary for AStartPath Finding, Methods required for TileBasedMap
	 * @ param int x:  Tile position in tiles
	 * 			int y:
	 */
	public boolean blocked(PathFindingContext ctx, int x, int y) {
		return mapProperty[x][y]!=0;
	}
	
	
	@Override
	/**For Path Finding, the getCost function of each move. Method required for TileBasedMap */
	public float getCost(PathFindingContext ctx, int x, int y) {
		// TODO Auto-generated method stub
		return 1.0f;
	}
	
	@Override
	/**
	 * Get the number of Tiles in Map Vertically*/
	public int getHeightInTiles() {		
		return map.getHeight();
	}
	/**
	 * Get the number of Tiles in Map Horizontally */
	@Override
	public int getWidthInTiles() {
		return map.getWidth();
	}

	@Override
	public void pathFinderVisited(int arg0, int arg1) {
	}
	
}
