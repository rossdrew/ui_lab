package rox.model;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * @author Ross
 *
 */
public interface MapTile 
{
	public int getID();
	public BufferedImage getImage();
	public boolean isTraversable();
	public Color getBaseColor();
	public MapTile[] getTileList();
}
