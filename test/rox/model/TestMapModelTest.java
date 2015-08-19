package rox.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import rox.resource.ResourceLoader;

public class TestMapModelTest 
{
	private MapGrid mapModel;
	
	@Before
	public void setUp() throws Exception 
	{
		mapModel = new TestMapModel(22, 33);
	}

	@Test
	public void testGetTile() 
	{
		assertNotNull(mapModel.getTile(1, 1));
	}

	@Test
	public void testGetWidth() 
	{
		assertEquals(22, mapModel.getWidth());
	}

	@Test
	public void testGetHeight() 
	{
		assertEquals(33, mapModel.getHeight());
	}

	@Test
	public void testGetTiles() 
	{
		assertNotNull(mapModel.getTileList());
	}

	@Test
	public void testSetResourceLoader() 
	{
		ResourceLoader resourceLoader = new ResourceLoader();
		mapModel.setResourceLoader(resourceLoader);
	}

	@Test
	public void testGetTileList() {}

	@Test
	public void testSetTile() {}

}
