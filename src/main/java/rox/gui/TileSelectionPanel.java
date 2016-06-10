package rox.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import rox.gui.event.MapEventListener;
import rox.model.MapGrid;
import rox.model.MapTile;
import rox.resource.ResourceLoader;

/**
 * @author Ross
 *
 *  There are alot of shared methods and variables here with MapPanel, 
 * perhaps a common parent MapCanvas might be in order.
 * 
 *  -Why does the tile selection get squished?
 */
public class TileSelectionPanel extends JPanel
							    implements MouseListener, 
							    		   MouseWheelListener
{
	public enum TileLayout { VERTCIAL, 
							 HORIZONTAL 
						   };
	
    protected ResourceLoader resourceLoader;	
    protected MapGrid mapGrid;
    protected MapTile[] tileList;
    
    private ArrayList<MapEventListener> mapListeners = new ArrayList<MapEventListener>();
    
	protected int tileSize = 32;
	protected int optionsOffset = 10;
	
	private TileLayout tileLayout = TileLayout.VERTCIAL;
	private MapTile selectedTile;
	
	public TileSelectionPanel(ResourceLoader resourceLoader, MapGrid mapGrid)
	{
		this.resourceLoader = resourceLoader;
		this.mapGrid = mapGrid;
		this.tileList = mapGrid.getTileList();
		
		this.setPreferredSize( new Dimension(tileSize,tileSize));
    this.addMouseListener(this);
		
		selectedTile = tileList[0];
	}
	
	public MapTile getSelectedTile()
	{
		return selectedTile;
	}
	
	public TileLayout getTileLayout() 
	{
		return tileLayout;
	}

	public void setTileLayout(TileLayout layout) 
	{
		this.tileLayout = layout;
	}
	
	/* Method Area */
	
	private void drawTile(Graphics g, int x, int y, MapTile tile)
	{
		drawColoredTile(g, 0, 0, tile.getBaseColor());
        drawImageTile(g, 0, 0, tile.getID());
	}
	
	private void drawImageTile(Graphics g, int x, int y, int imageID)
	{
		BufferedImage img = resourceLoader.getImage(imageID);
		
		if (img != null)
		{
			g.drawImage(img, x, 
							 y, 
							 x + tileSize, 
							 y + tileSize, 
							 0, 0, 256, 236, null);
		}
	}
	
	private void drawImageTiles(Graphics g, int xLoc, int yLoc)
	{
		int x=xLoc, y=yLoc;
		for (int i=0; i<tileList.length; i++)
		{
			drawImageTile(g, x, y, tileList[i].getID());
			
			switch(tileLayout)
			{
				case VERTCIAL:
					y+=tileSize;
				break;
				
				case HORIZONTAL:
					x+=tileSize;
				break;
			}
		}
	}
	
	/**
	 * Draw specified color at the specified location
	 * 
	 * @param g Graphics object
	 * @param x X location of tile to draw
	 * @param y Y location of tile to draw
	 * @param color
	 */
	private void drawColoredTile(Graphics g, int x, int y, Color color)
	{
		g.setColor( color);
		g.fillRect( x, y, tileSize, tileSize);
	}
	
	/**
	 * Draw simple color tile representations
	 * 
	 * @param g Graphics to draw on
	 */
	private void drawColoredTiles(Graphics g, int locX, int locY)
	{		
		int x=locX, y=locY;
		for (int i=0; i<tileList.length; i++)
		{
			drawColoredTile(g, x, y, tileList[i].getBaseColor());
			
			switch(tileLayout)
			{
				case VERTCIAL:
					y+=tileSize;
				break;
				
				case HORIZONTAL:
					x+=tileSize;
				break;
			}
		}
	}
	
	public void addMapEventListener(MapEventListener listener)
	{
		mapListeners.add(listener);
	}
	
	public void removeMapEventListener(MapEventListener listener)
	{
		mapListeners.remove(listener);
	}
	
	private void fireMapClickListeners(int x, int y)
	{
		for (MapEventListener listener : mapListeners)
		{
			listener.mapClicked(x, y);
		}
	}
	
	private boolean tileNumberWithinRange(int tileNumber)
	{
	  return (tileNumber < tileList.length && tileNumber >= 0);
	}
	
	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		
        drawTile(g, 0, 0, selectedTile);
        
        drawColoredTiles(g, 0, (tileSize + optionsOffset));
        drawImageTiles(g, 0, (tileSize + optionsOffset));
	}
	
	@Override
	public void mouseClicked(MouseEvent event) 
	{
	  int location = (tileLayout == TileLayout.VERTCIAL ? event.getY() : event.getX());
		int clickedTile = ((location - optionsOffset) / tileSize ) - 1 ;
		
		if (tileNumberWithinRange(clickedTile))
		{
			selectedTile = tileList[clickedTile];
			repaint();
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {}
	
}
