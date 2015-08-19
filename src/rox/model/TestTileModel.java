package rox.model;

import java.awt.Color;
import java.awt.image.BufferedImage;

import rox.util.DoubleKeyMap;

/**
 * @author Ross
 *
 */
public class TestTileModel implements MapTile
{
	/**
	 * Representation of a single map tile type
	 */
	public enum TileType
	{
		OCEAN(0, new Color(0, 0, 205), "resources/tiles/ocean.jpg", "Ocean"),
		SEA(1, new Color(0, 0, 255), "resources/tiles/sea.jpg", "Sea"),
		SAND(2, new Color(238, 238, 0), "resources/tiles/barrensbasedirtcc6.png", "Sand"),
		SWAMP(3, new Color(139, 69, 19), "resources/tiles/barrensbasedirtswk8.png", "Swamp"),
		GRASS(4, new Color(34, 139, 34), "resources/tiles/grass_e.png", "Grass"),
		FORREST(5, new Color(0, 100, 0), "resources/tiles/forest.jpg", "Forrest"),
		MOUNTAIN(6, new Color(139, 123, 139), "resources/tiles/dirt.jpg", "Mountains"),
		INVALID(50, new Color(222, 184, 135), null, "ERR!"),
		UNKNOWN(60, new Color(0, 0, 0), null, "Unknown");
	
		private int id;
		private Color baseColor;
		private String resourceLocation;
		private String description;
		
		TileType(int id, Color baseColor, String resource, String description)
		{
			this.id = id;
			this.baseColor = baseColor;
			this.resourceLocation = resource;
			this.description = description;
		}
		
		public int getID()
		{
			return id;
		}
		
		public Color getBaseColor()
		{
			return this.baseColor;
		}
		
		public void setResourceLocation(String location)
		{
			this.resourceLocation = location;
		}
		
		public String getResourceLocation()
		{
			return this.resourceLocation;
		}
		
		public TileType getBaseLevel()
		{
			return OCEAN;
		}
		
		public String toString()
		{
			return  " [" + getID() + ":" + description + "]";
		}
	}
	
	protected TileType tileType = TileType.INVALID;
	
	public TestTileModel()
	{
		this.tileType = TileType.OCEAN;
	}
	
	public TestTileModel(TileType type)
	{
		this.tileType = type;
	}
	
	public static TileType getByID(int i)
	{
		return TileType.values()[i];
	}
	
	public TileType getTileType()
	{
		return this.tileType;
	}

	public MapTile[] getTileList()
	{
		MapTile[] tileList = new TestTileModel[TileType.values().length];
		int i=0;
		
		for (TileType type : TileType.values())
		{
			tileList[i++] = new TestTileModel(type);
		}
		
		return tileList;
	}
	
	@Override
	public int getID() 
	{
		return tileType.getID();
	}

	@Override
	public BufferedImage getImage() 
	{
		return null;
	}

	@Override
	public boolean isTraversable() 
	{
		return true;
	}

	@Override
	public Color getBaseColor() 
	{
		return this.tileType.getBaseColor();
	}
	
	@Override
	public String toString()
	{
		return this.tileType.toString();
	}
}