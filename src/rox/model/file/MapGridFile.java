/**
 * 
 */
package rox.model.file;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import rox.model.MapGrid;
import rox.model.MapTile;
import rox.model.TestMapModel;
import rox.model.TestTileModel;
import rox.model.TestTileModel.TileType;

/**
 * @author Ross Drew
 *
 */
public class MapGridFile 
{
	private String testFilename = "mapGridFileX.mgd";

	public MapGridFile(String fileLocation)
	{
		testFilename = fileLocation;
	}

	private byte[] formatData(MapGrid mapGrid)
	{
		int tileCount = mapGrid.getHeight() * mapGrid.getWidth();
		int tileIndex = 0;
		
		byte[] data = new byte[tileCount + 2];		
		
		data[tileIndex++] =  (byte)mapGrid.getHeight();
		data[tileIndex++] =  (byte)mapGrid.getWidth();
		
        for (int y = 0; y < mapGrid.getHeight(); y++)
        {
        	for (int x = 0; x < mapGrid.getWidth(); x++)
        	{
        		data[tileIndex++] = (byte)mapGrid.getTile(x, y).getID();
        	}
        }
        
        return data;
	}
	
	public MapGrid load()
	{
		byte[] data;
		MapGrid mapGrid=null;
		
		try
		{
			InputStream input = null;
			
			try
			{
				input = new BufferedInputStream(new FileInputStream(testFilename));
				
			//Read header
				byte[] header = new byte[2];
				input.read(header);
				int height = (int)header[0];
				int width = (int)header[1];
				
			//Read data
				data = new byte[height * width];
				System.out.println("Loading map [" + height + "x" + width + "] from '"+testFilename+"'");
				input.read(data);
				
			//Build map grid
				mapGrid = new TestMapModel(height, width);
				int tileIndex = 0;
				
        for (int y = 0; y < mapGrid.getHeight(); y++)
        {
          for (int x = 0; x < mapGrid.getWidth(); x++)
          {
            TileType tileType = (TestTileModel.getByID(data[tileIndex++]));
            MapTile tile = new TestTileModel(tileType);
            mapGrid.setTile(x, y, tile);
          }
        }
			}
			finally
			{
				input.close();
			}
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File not found: "+e.getMessage());
		}
		catch (IOException e)
		{
			System.out.println("IO Exception: "+e.getMessage());
		}
		
		return mapGrid;
	}
	
	public void save(MapGrid mapGrid)
	{
		try
		{
			OutputStream output = null;
			
			try
			{
				output = new BufferedOutputStream(new FileOutputStream(testFilename));
				System.out.println("Saving map [" + mapGrid.getHeight() + "x" + mapGrid.getWidth() + "] to '" + testFilename + "'");
				output.write( formatData(mapGrid));
				output.flush();
			}
			finally
			{
				output.close();
			}
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File not found: "+e.getMessage());
		}
		catch (IOException e)
		{
			System.out.println("IO Exception: "+e.getMessage());
		}
	}
	
	public static void main(String[] args) 
	{


	}

}
