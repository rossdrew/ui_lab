package rox.model.file;

import java.io.*;

import rox.model.MapGrid;
import rox.model.MapTile;
import rox.model.ModifiableMap;
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

	private byte[] formatData(MapGrid mapGrid){
		int tileCount = mapGrid.getHeight() * mapGrid.getWidth();
		int tileIndex = 0;
		
		byte[] data = new byte[tileCount + 2];		
		
		data[tileIndex++] =  (byte)mapGrid.getHeight();
		data[tileIndex++] =  (byte)mapGrid.getWidth();
		
        for (int y = 0; y < mapGrid.getHeight(); y++){
        	for (int x = 0; x < mapGrid.getWidth(); x++){
        		data[tileIndex++] = (byte)mapGrid.getTile(x, y).getID();
        	}
        }
        
        return data;
	}
	
	public MapGrid load(){
		try (FileInputStream fileIStream = new FileInputStream(testFilename);
			final InputStream iStream = new BufferedInputStream(fileIStream)){

			byte[] header = new byte[2];
			int headerBytes = iStream.read(header);
			int height = (int)header[0];
			int width = (int)header[1];

			final byte[] data = new byte[height * width];
			System.out.println("Loading map [" + height + "x" + width + "] from '" + testFilename + "'");
			int dataBytes = iStream.read(data);

			//Build map grid
			final MapGrid mapGrid = new ModifiableMap(height, width);
			int tileIndex = 0;

			for (int y = 0; y < mapGrid.getHeight(); y++){
				for (int x = 0; x < mapGrid.getWidth(); x++){
					TileType tileType = (TestTileModel.getByID(data[tileIndex++]));
					MapTile tile = new TestTileModel(tileType);
					mapGrid.setTile(x, y, tile);
				}
			}

			return mapGrid;
		} catch (IOException e) {
            throw new RuntimeException(e);
        }
	}
	
	public void save(MapGrid mapGrid){
		try (FileOutputStream fileOStream = new FileOutputStream(testFilename); OutputStream oStream = new BufferedOutputStream(fileOStream)){
			System.out.println("Saving map [" + mapGrid.getHeight() + "x" + mapGrid.getWidth() + "] to '" + testFilename + "'");
			oStream.write( formatData(mapGrid));
			oStream.flush();
		} catch (IOException e) {
            throw new RuntimeException(e);
        }
	}
}
