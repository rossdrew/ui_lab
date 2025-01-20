package rox.model;

import rox.resource.ResourceLoader;

/**
 * @author Ross
 *
 */
public interface MapGrid 
{
  //TODO map name & add to file
	public MapTile getTile(int x, int y);
	public void setTile(int x, int y, MapTile tile) throws IndexOutOfBoundsException;
	public boolean setTiles(int x1, int y1, int x2, int y2, MapTile tile);
	public MapTile[][] getTiles();
	public int getWidth();
	public int getHeight();
	public void setResourceLoader(ResourceLoader resourceLoader); //This needs to deal with tiles so it can be tested
	public MapTile[] getTileList();
}
