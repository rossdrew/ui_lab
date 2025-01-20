package rox.model.build;

import rox.model.MapGrid;
import rox.model.MapTile;
import rox.model.ModifiableMap;
import rox.model.TestTileModel;
import rox.model.TestTileModel.TileType;

/**
 * @author Ross
 *
 *   Creates a sea level map and allows manipulation then generates an 
 *  output of {@link ModifiableMap MapModel} for building custom maps.
 */
public class TestMapModelBuilder 
{
	protected MapTile[][] tileMap;
	
	public TestMapModelBuilder(MapGrid model)
	{
		tileMap = new TestTileModel[model.getWidth()][model.getHeight()];
		
		for (int x = 0; x < model.getWidth(); x++)
		{
			for (int y = 0; y < model.getHeight(); y++)
			{
				tileMap[x][y] = model.getTile(x, y);
			}
		}
	}
	
	public TestMapModelBuilder(int length, int height)
	{
		tileMap = new TestTileModel[length][height];
		
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < length; x++)
			{
				//Create an ocean
				tileMap[x][y] = new TestTileModel();
			}
		}
	}
	
	public MapTile[][] getTileMap()
	{
		return this.tileMap;
	}
	
	public MapGrid generateMapModel()
	{
		return new ModifiableMap(getTileMap());
	}
	
	/**
	 * Fill specified space with specified TileType
	 * 
	 * @param tileType type of tile to set block to
	 * @param x1 starting horizontal location
	 * @param y1 starting vertical location
	 * @param x2 end horizontal location
	 * @param y2 end vertical location
	 */
	public void fillBlock(TileType tileType, int startX, int startY, int endX, int endY) 
	{
		for (int x = startX; x < endX; x++)
		{
			for (int y = startY; y < endY; y++)
			{
				fillTile(tileType, x, y);
			}
		}
	}
	
	/**
	 * Set specified tile to specified type
	 * 
	 * @param tileType type of tile to set tile to
	 * @param x horizontal location of tile
	 * @param y vertical location of tile
	 */
	public void fillTile(TileType tileType, int x, int y)
	{
		tileMap[x][y] = new TestTileModel(tileType);
	}
}
