package rox.model;

import rox.model.TestTileModel.TileType;
import rox.model.build.TestMapModelBuilder;
import rox.resource.ResourceLoader;

/**
 * @author Ross
 *
 */
public class ModifiableMap implements MapGrid
{
	protected MapTile[][] tileMap;
	
	public ModifiableMap(MapTile[][] tileMap)
	{
		this.tileMap = tileMap;
	}
	
	public ModifiableMap(int length, int height){
		TestMapModelBuilder mapModelBuilder = new TestMapModelBuilder(length, height);
		
		this.tileMap = mapModelBuilder.getTileMap();
	}
	
	public MapTile getTile(int x, int y)
	{
		return tileMap[x][y];
	}
	
	public int getWidth()
	{
		return tileMap.length;
	}
	
	public int getHeight()
	{
		return tileMap[0].length;
	}
	
	@Override
	public MapTile[][] getTiles()
	{
		return tileMap.clone();
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		for (TileType type : TestTileModel.TileType.values()){
			/*DEBUG*///System.out.println("Adding resource informaiton for " + type.getID());
			resourceLoader.addResource(type.getID(), type.getResourceLocation());
		}
	}

	@Override
	public MapTile[] getTileList(){
		return tileMap[0][0].getTileList();
	}

	@Override
	public void setTile(int x, int y, MapTile tile) throws IndexOutOfBoundsException{
		if ( (y < 0 || y > tileMap[0].length) || (x < 0 || x > tileMap.length)){
			throw new IndexOutOfBoundsException("Trying to set tile (" + x + "," + y + ") outwith map bounds.");
		}

		tileMap[x][y] = tile;
	}
	
	@Override
	public boolean setTiles(int x1, int y1, int x2, int y2, MapTile tile){
	  for (int x = x1; x <= x2; x++){
	    for (int y = y1; y <= y2; y++){
			try{
				setTile(x,y, tile);
			}catch (IndexOutOfBoundsException e){
				return false;
			}
	    }
	  }
	  
	  return true;
	}
}
