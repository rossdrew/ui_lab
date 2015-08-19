package rox.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import rox.gui.event.MapEventGenerator;
import rox.gui.event.MapEventListener;
import rox.model.*;
import rox.resource.ResourceLoader;

/**
 * @author Ross
 * @version 0.06
 * 
 * 0.08 (25/01/2015)
 *  - Fixed issue where first draw of an image after load failed
 *  - Submitted to SVN!
 *  
 * 0.07 (27/02/2013
 *  - Moved mapClick fire to after release of click
 *  (- Can now draw around edges - MapEditor)
 * 
 * 0.06 (07/12/2012)
 *  - Separated implementation of map and tile to allow 
 *    injection of separate structures.
 *  
 * 0.05 (24/11/2012)
 *  - Added MapLocation and map placement validations
 *  - Added GridCoordinate and started coord change over
 *  - Added event listeners
 *  - Added currently selected tile
 *  
 * 0.04 (21/11/2012)
 *  - Added images and ResourceLoader
 *  - Added on/off for grid lines
 *  - added dragging to map
 *  
 * 0.03 (20/11/2012)
 *  - Added MapModel, TileModel and MapModelBuilder
 *  
 * 0.02 (19/11/2012)
 *  - draw so it centers on the size of the panel
 *  
 * 0.01
 *  - First version
 * 
 */
public class MapPanel extends JPanel implements MouseListener,
												MouseWheelListener, 
												MouseMotionListener, 
												MapEventGenerator
{
	private ResourceLoader resourceLoader;
	private MapGrid mapModel;
	
	private ArrayList<MapEventListener> mapListeners = new ArrayList<MapEventListener>();
	
  protected Color OFFBROWN = new Color(200, 160, 130);
  protected int ZOOM_RATE = 5;
  protected int ZOOM_MAX = 80;
  protected int ZOOM_MIN = 1;
	
  protected GridCoordinate boardCenter = new GridCoordinate(0,0);
  protected GridCoordinate boardOffset = new GridCoordinate(0,0);
  protected GridCoordinate selectedTile = null;
  protected GridCoordinate lastSelectedTile = null; 
  protected int tileDisplaySize = 32;
  protected boolean showGrid;

  boolean dragging = false;
  private GridCoordinate boardClick = null;
	private GridCoordinate mapDownClick = null;
    
	public MapPanel(MapGrid mapModel, ResourceLoader resourceLoader)
	{
		this.resourceLoader = resourceLoader;
		this.mapModel = mapModel;
		
		this.addMouseListener(this);
		this.addMouseWheelListener(this);
		this.addMouseMotionListener(this);
	}
	
	/**
	 * Set whether grid lines are shown
	 * 
	 * @param showGrid true if grid lines are to be shown, otherwise false
	 */
	public void setShowGrid(boolean showGrid)
	{
		this.showGrid = showGrid;
		repaint();
	}
	
	public boolean isGridVisible()
	{
		return this.showGrid;
	}
	
	public MapGrid getMapModel() {
		return mapModel;
	}

	public Color getTileColor(int x, int y)
	{
		return mapModel.getTile(x, y).getBaseColor();
	}
	
	public GridCoordinate getSelectedTile()
	{
		return selectedTile;
	}
	
	public GridCoordinate getLastSelectedTile()
	{
		return lastSelectedTile;
	}
	
	/**
	 *  Re-calculate variables related to drawing locations 
	 */
	public void updateGraphicsVariables()
	{
		int panelHorizontalCentre = this.getWidth() / 2;
		int panelVerticalCentre = this.getHeight() / 2;
		
		int halfBoardHSize = (mapModel.getWidth() * tileDisplaySize) / 2;
		int halfBoardVSize = (mapModel.getHeight() * tileDisplaySize) / 2;
		
		boardCenter.setX(panelHorizontalCentre - halfBoardHSize);
		boardCenter.setY(panelVerticalCentre - halfBoardVSize);
	}

	private void zoomMap(int zoomValue)
	{
		int zoomMagnitude = zoomValue;
		int newSize = ( tileDisplaySize -( zoomMagnitude * ZOOM_RATE));
		
		if ( newSize > ZOOM_MIN && newSize < ZOOM_MAX)
		{
			tileDisplaySize=newSize;
		}
		
		repaint();
	}
	
	private int verticalScreenLocationOf(int y)
	{
		return ( getVerticalStartPoint() + ( y * tileDisplaySize));
	}
	
	private int horizontalScreenLocationOf(int x)
	{
		return ( getHorizontalStartPoint() + ( x * tileDisplaySize));
	}
	
	private int getHorizontalStartPoint()
	{
		return boardCenter.getX() + boardOffset.getX();
	}
	
	private int getVerticalStartPoint()
	{
		return boardCenter.getY() + boardOffset.getY();
	}
	
	public boolean isValidHorizontalScreenLocaiton(int x)
	{
		boolean validTile = false;
		int horizontalEnd = x + (tileDisplaySize * mapModel.getWidth());
		
		if ( (tileDisplaySize * mapModel.getWidth()) > this.getWidth())
		{
			validTile = (x <= 0) && (horizontalEnd >= this.getWidth());
		}
		else
		{
			validTile = (x >= 0) && (horizontalEnd <= this.getWidth());
		}
		
		return validTile;
	}
	
	public boolean isValidVerticalScreenLocait(int y)
	{
		boolean validTile = false;
		int verticalEnd = y + (tileDisplaySize * mapModel.getHeight());
		
		if ( (tileDisplaySize * mapModel.getHeight()) >= this.getHeight())
		{
			validTile = (y <= 0) && (verticalEnd >= this.getHeight());
		}
		else
		{
			validTile = (y >= 0) && (verticalEnd <= this.getHeight());
		}
		
		return validTile;
	}

	private void drawImageTiles(Graphics g)
	{
    for (int y = 0; y < mapModel.getHeight(); y++)
    {
      for (int x = 0; x < mapModel.getWidth(); x++)
      {
        drawImageTile(g, y, x);
      }
    }
	}

	private void drawImageTile(Graphics g, int y, int x) 
	{
		MapTile tile = mapModel.getTile(x, y);
		BufferedImage img = resourceLoader.getImage(tile.getID());
		
		if (img != null)
		{
			g.drawImage(img, horizontalScreenLocationOf(x), 
							 verticalScreenLocationOf(y), 
							 horizontalScreenLocationOf(x) + tileDisplaySize, 
							 verticalScreenLocationOf(y) + tileDisplaySize, 
							 0, 0, 256, 256, null);
			
			//XXX
			if ((x-1)>0)
			{
				if (mapModel.getTile(x-1, y).getID() == TestTileModel.TileType.GRASS.getID() 
			     && tile.getID() < TestTileModel.TileType.GRASS.getID())
				{
					System.out.println("Drawing at " + x + ", " + y + " over " + tile);
					
					BufferedImage img2 = resourceLoader.getImage(mapModel.getTile(x-1, y).getID());
					
					g.drawImage(img2, horizontalScreenLocationOf(x), 
							 		 verticalScreenLocationOf(y), 
							 		 horizontalScreenLocationOf(x) + tileDisplaySize, 
							 		 verticalScreenLocationOf(y) + tileDisplaySize, 
							 		 256, 0, 512, 256, null);
				}
			}
			//XXX	
		}
		else
		{
			System.out.println("No image cached for tile (" + tile.toString() + ") at [" + x + "," + y + "]");
		}
	}
	
	private void drawColoredTiles(Graphics g)
	{
		for (int y = 0; y < mapModel.getHeight(); y++) 
		{
			for (int x = 0; x < mapModel.getWidth(); x++) 
			{
				drawColoredTile(g, x, y);
			}
		}
	}
	
	private void drawColoredTile(Graphics g, int x, int y)
	{
		drawColoredTile( g, x, y, getTileColor( x, y));
	}
	
	private void drawColoredTile(Graphics g, int x, int y, Color color)
	{
		g.setColor( color);
		g.fillRect( horizontalScreenLocationOf(x), verticalScreenLocationOf(y), tileDisplaySize, tileDisplaySize);
	}
	
	private void drawGrid(Graphics g)
	{
    int boardEndPointX = horizontalScreenLocationOf(mapModel.getWidth());
    int boardEndPointY = verticalScreenLocationOf(mapModel.getHeight());

    //XXX needs to be tested with rectangular maps
    int tileStartX = 0, tileStartY = 0;
    int longestEdge = Math.max(mapModel.getWidth(), mapModel.getHeight());

    g.setColor(OFFBROWN);

    for (int lineNumber = 0; lineNumber <= longestEdge; lineNumber++)
    {
      tileStartY = getVerticalStartPoint() + (lineNumber * tileDisplaySize);
      g.drawLine(getHorizontalStartPoint(), tileStartY, boardEndPointX, tileStartY);

      tileStartX = getHorizontalStartPoint() + (lineNumber * tileDisplaySize);
      g.drawLine(tileStartX, getVerticalStartPoint(), tileStartX, boardEndPointY);
    }
	}
	
	private void drawSelectionBox(Graphics g)
	{
		if (selectedTile != null)
		{
			int x = selectedTile.getX() * tileDisplaySize;
			int y = selectedTile.getY() * tileDisplaySize;
			
			g.setColor( new Color( 255, 255, 255));
			g.drawRect( getHorizontalStartPoint() + x, getVerticalStartPoint() + y, tileDisplaySize, tileDisplaySize);
			
			g.setColor( new Color( 80, 200, 255));
			g.drawRect( getHorizontalStartPoint() + x + 1, getVerticalStartPoint() + y + 1, tileDisplaySize - 2, tileDisplaySize - 2);
		}
	}
	
	private void fireMapClickListeners(int x, int y)
	{
		for (MapEventListener listener : mapListeners)
		{
			listener.mapClicked(x, y);
		}
	}

	@Override
	public void addMapEventListener(MapEventListener listener)
	{
		mapListeners.add(listener);
	}
	
	@Override
	public void removeMapEventListener(MapEventListener listener)
	{
		mapListeners.remove(listener);
	}
	
	@Override
	public void paint(Graphics g)
	{		
		super.paint(g);
		
        updateGraphicsVariables();
        
        drawColoredTiles(g);
        drawImageTiles(g);
        
        if (isGridVisible())
        {	
        	drawGrid(g);
        }
        
        drawSelectionBox(g);
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}
	
	@Override
	public void mousePressed(MouseEvent event) 
	{
		if (boardClick == null)
		{
			boardClick = new GridCoordinate(event.getX(), event.getY());
		}
		else
		{
			boardClick.setX(event.getX());
			boardClick.setY(event.getY());
		}
		
		int tileX = (boardClick.getX() - getHorizontalStartPoint()) / tileDisplaySize;
		int tileY = (boardClick.getY() - getVerticalStartPoint()) / tileDisplaySize;
		
		mapDownClick = new GridCoordinate(tileX, tileY);

		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) 
	{	
		if (mapDownClick!=null && !dragging)
		{
			lastSelectedTile = selectedTile;
			selectedTile = mapDownClick;
			fireMapClickListeners(mapDownClick.getX(), mapDownClick.getY());
		}
		
		dragging=false;
		repaint();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent mwEvent) 
	{
		zoomMap( mwEvent.getWheelRotation());
	}
	
	@Override
	public void mouseDragged(MouseEvent event) 
	{
		dragging = true; 
		
		int deltaX = (event.getX() - boardClick.getX());
		int deltaY = (event.getY() - boardClick.getY());

		boolean repaintRequired=false;
		
		if (isValidHorizontalScreenLocaiton(getHorizontalStartPoint() + deltaX))
		{
			boardOffset.setX( boardOffset.getX() + deltaX);
			boardClick.setX(event.getX());
			repaintRequired=true;
		}
		
		if (isValidVerticalScreenLocait(getVerticalStartPoint() + deltaY))
		{
			boardOffset.setY( boardOffset.getY() + deltaY);
			boardClick.setY(event.getY());
			repaintRequired=true;
		}
		
		if (repaintRequired)
		{
			repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {}
}
