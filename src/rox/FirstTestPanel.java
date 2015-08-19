package rox;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import rox.gui.MapPanel;
import rox.gui.MapPreviewPanel;
import rox.model.MapGrid;
import rox.model.TestTileModel.TileType;
import rox.model.build.TestMapModelBuilder;
import rox.model.file.MapGridFile;
import rox.resource.ResourceLoader;

/**
 * @author Ross Drew
 * 
 * Example class and panel for testing MapPanel
 *
 */
public class FirstTestPanel extends JFrame 
{
	private ResourceLoader resourceLoader = new ResourceLoader();
	private JToolBar toolBar;
	private JCheckBox showGrid;
	
	private MapGrid mapGrid;
	
	private MapPanel mapPanel;
	
	public FirstTestPanel()
	{
		super("First Test Panel");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 500);
		setVisible(true);
		
		setLayout(new BorderLayout());
		
		createMapPanel();
		createPreviewPanel();
		createToolbar();
	}

	private void createMapPanel() 
	{
//		mapPanel = new MapPanel( generateTestMap(), resourceLoader);
		mapPanel = new MapPanel( createMapFromFile(), resourceLoader);
		add(mapPanel, BorderLayout.CENTER);
	}

	private void createToolbar() 
	{
		toolBar = new JToolBar();
		showGrid = new JCheckBox("Show grid");
		showGrid.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mapPanel.setShowGrid(showGrid.isSelected());
			}
		});
		toolBar.add( showGrid);
		
		add( toolBar, BorderLayout.NORTH);
	}
	
	private void createPreviewPanel()
	{
		MapPreviewPanel previewPanel = new MapPreviewPanel(mapGrid);
		
		add(previewPanel, BorderLayout.EAST);
	}
	
	private MapGrid createMapFromFile()
	{
		MapGridFile mapFile = new MapGridFile("mapGridFileX.mgd");
		mapGrid = mapFile.load();
		mapGrid.setResourceLoader(resourceLoader);
		
		return mapGrid;
	}
	
	private MapGrid generateTestMap()
	{
		TestMapModelBuilder builder = new TestMapModelBuilder(52, 53);
		
		builder.fillBlock(TileType.GRASS, 1, 1, 9, 9);
		builder.fillBlock(TileType.FORREST, 2, 2, 8, 8);
		builder.fillBlock(TileType.MOUNTAIN, 3, 3, 7, 7);
		
		MapGrid model = builder.generateMapModel();
		model.setResourceLoader(resourceLoader);
		
		return model;
	}
	
	public static void main(String[] args) 
	{
		FirstTestPanel testPanel = new FirstTestPanel();	
	}
}
