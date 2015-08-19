package rox.resource;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import rox.model.MapGrid;
import rox.model.TestTileModel;

/**
 * @author Ross
 *
 */
public class ResourceLoader 
{
	private HashMap<Object, String> resourceLocations = new HashMap<Object, String>();
	private HashMap<Object, BufferedImage> loadedImages = new HashMap<Object, BufferedImage>();
	
	public ResourceLoader()
	{
		
	}
	
	public void addResource(Object id, String location)
	{
	  /*DEBUG*///System.out.println("Adding resource " + id + " = '" + location + "'");
		resourceLocations.put( id, location);
	}
	
	private BufferedImage loadImage(String imgLocation)
	{
		return loadImage( new File(imgLocation));
	}
	
	private BufferedImage loadImage(File imgFile)
	{
		StringBuffer statusMessage = new StringBuffer("Loading '" + imgFile.getAbsolutePath() + "'");
		
		BufferedImage img = null;
		
		try 
		{
		    img = ImageIO.read(imgFile);
		    statusMessage.append("\t[OK]");
		} catch (IOException e) {
			statusMessage.append("\t[FAIL] : "+e.getMessage());
			//e.printStackTrace();
		}

		System.out.println(statusMessage.toString());
		
		return img;
	}
	
	public BufferedImage getImage(Object associatedObject)
	{
		if (loadedImages.get(associatedObject) != null) 
		{
			return loadedImages.get(associatedObject);
		} else 
		{
			if (resourceLocations.get(associatedObject) != null) 
			{
				BufferedImage image = loadImage(resourceLocations.get(associatedObject));
				loadedImages.put(associatedObject, image);

				return image;
			} 
			else 
			{
				/* DEBUG */Throwable t = new Throwable();
				/* DEBUG */StackTraceElement[] elements = t.getStackTrace();

				/* DEBUG */System.out.println("ResourceLoader: No image resource specified for " + associatedObject);
				/* DEBUG */System.out.println("\tRequsted from " + elements[1].getClassName());

				return null;
			}
		}
	}
}