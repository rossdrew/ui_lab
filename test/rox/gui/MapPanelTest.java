package rox.gui;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import rox.model.MapGrid;
import rox.model.TestTileModel.TileType;
import rox.model.build.TestMapModelBuilder;
import rox.resource.ResourceLoader;

public class MapPanelTest
{
  private MapPanel mapPanel;
  private ResourceLoader resourceLoader;
  
  @Before
  public void setUp() throws Exception
  {
    resourceLoader = new ResourceLoader();

    TestMapModelBuilder builder = new TestMapModelBuilder(52, 53);

    builder.fillBlock(TileType.GRASS, 1, 1, 9, 9);
    builder.fillBlock(TileType.FORREST, 2, 2, 8, 8);
    builder.fillBlock(TileType.MOUNTAIN, 3, 3, 7, 7);

    MapGrid model = builder.generateMapModel();
    model.setResourceLoader(resourceLoader);

    mapPanel = new MapPanel(model, resourceLoader);
  }
}
