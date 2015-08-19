package rox.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import rox.model.MapGrid;
import rox.model.MapTile;

public class MapPreviewPanel extends JPanel 
{
	private int TILESIZE = 5;
	
	private MapGrid mapModel;
	
	public MapPreviewPanel(MapGrid mapModel)
	{
		this.mapModel = mapModel;
		
		setPreferredSize(new Dimension(TILESIZE * mapModel.getWidth(), 
				   					   TILESIZE * mapModel.getWidth()));
	}
	
	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		
		MapTile[][] mapTiles = mapModel.getTiles();

		for (int x = 0, startX = 0; x < mapModel.getWidth(); x++, startX += TILESIZE)
		{
			for (int y = 0, startY = 0; y < mapModel.getHeight(); y++, startY += TILESIZE)
			{
				g.setColor(mapTiles[x][y].getBaseColor());
				g.fillRect(startX, startY, TILESIZE, TILESIZE);
			}
		}
		
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, TILESIZE * mapModel.getWidth()-1, TILESIZE * mapModel.getWidth()-1);
	}
}
