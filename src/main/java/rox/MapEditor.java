package rox;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import rox.gui.ConsolePanel;
import rox.gui.MapPanel;
import rox.gui.MapPreviewPanel;
import rox.gui.TileSelectionPanel;
import rox.gui.event.MapEventListener;
import rox.model.GridCoordinate;
import rox.model.MapGrid;
import rox.model.TestMapModel;
import rox.model.build.TestMapModelBuilder;
import rox.model.file.MapGridFile;
import rox.resource.ResourceLoader;

/**
 * @author Ross
 *
 */
public class MapEditor extends JFrame implements MapEventListener, 
												                         ActionListener
{
  /*

   this (BorderLayout)
     mainPanel (BorderLayout)
                | toolBar     | 
          prev  | map         | Tile 
                | consolePane |
     consoleWindow

   */
	private static String ACTION_SAVE = "Save Map",
						            ACTION_LOAD = "Load Map",
						            ACTION_SHOW_GRID = "Show Grid",
						            ACTION_RESIZE_MAP = "Resize Map";
	
	private ResourceLoader resourceLoader = new ResourceLoader();
	
	private JMenuBar menuBar;
	private JCheckBoxMenuItem showGridMenuOption;
	
	private JPanel mainPanel;
	
	private JToolBar toolBar;
	private JCheckBox showGrid;
	private ConsolePanel consoleWindow; 
	
	private MapGrid mapModel;
	private MapPanel mapPanel;
	private MapPreviewPanel previewPanel;
	private TileSelectionPanel tileSelect;
	
	private JLabel shift;
	
	private boolean shiftPressed = false;
	
	public MapEditor()
	{
		super("Map Editor");
		
		initView();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 500);
		setVisible(true);
	}

  private void initView()
  {
    setLayout(new BorderLayout());
    
    mainPanel = new JPanel(new BorderLayout());
    
    createDropDownMenu();
	createConsoleWindow();
	createToolbar();
	regenerateMapPanel(null);	
    setupKeyListener();
    
    createStatusBar();
    
    add(mainPanel, BorderLayout.CENTER);
  }
  
  private void createStatusBar()
  {
	  JPanel statusBar = new JPanel();
	  statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
	  
	  shift = new JLabel("SHIFT");
	  shift.setVisible(shiftPressed);
	  statusBar.add(shift);
	  statusBar.add(new JLabel(" "));
	  
	  add(statusBar, BorderLayout.SOUTH);
  }
  
  private void setShiftPressed(boolean shiftIsPressed)
  {
    shiftPressed = shiftIsPressed;
    shift.setVisible(shiftPressed);
  }

  private void setupKeyListener()
  {
    KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher()
    {
      @Override
      public boolean dispatchKeyEvent(KeyEvent e)
      {
    	switch (e.getKeyCode())
    	{
    		case KeyEvent.VK_SHIFT:
    			setShiftPressed(e.getID() == KeyEvent.KEY_PRESSED);
    		    break;
    	}
    	  
        return false;
      }
    });
  }

  private void createToolbar()
  {
    toolBar = new JToolBar();
		showGrid = new JCheckBox("Show grid");
		showGrid.addActionListener(this);
		showGrid.setActionCommand(ACTION_SHOW_GRID);
		toolBar.add( showGrid);
		mainPanel.add(toolBar, BorderLayout.NORTH);
  }

  private void createConsoleWindow()
  {
		consoleWindow = new ConsolePanel();
		consoleWindow.setPreferredSize(new Dimension(1, 150));
		consoleWindow.setEditable(false);
		//System.setOut(consoleWindow.getStream());  //Direct standard out to console
		JScrollPane consolePane = new JScrollPane(consoleWindow);
		mainPanel.add(consolePane, BorderLayout.SOUTH);
  }

	private void createDropDownMenu() 
	{
		menuBar = new JMenuBar();
		
		menuBar.add(createSaveMenu());
		menuBar.add(createEditMenu());
		menuBar.add(createViewMenu());
		menuBar.add(createHelpMenu());
		
		this.setJMenuBar(menuBar);
	}

  private JMenu createHelpMenu()
  {
		JMenuItem infoMenuOption = new JMenuItem("Info");

		JMenu helpMenu = new JMenu("Help");
		helpMenu.add(infoMenuOption);
		return helpMenu;
  }

  private JMenu createViewMenu()
  {
		showGridMenuOption = new JCheckBoxMenuItem(ACTION_SHOW_GRID);
		showGridMenuOption.setActionCommand(ACTION_SHOW_GRID);
		showGridMenuOption.addActionListener(this);

		JMenu viewMenu = new JMenu("View");
		viewMenu.add(showGridMenuOption);
		return viewMenu;
  }

  private JMenu createEditMenu()
  {
		JMenuItem changeDimensionsOption = new JMenuItem(ACTION_RESIZE_MAP);
		changeDimensionsOption.setActionCommand(ACTION_RESIZE_MAP);
		changeDimensionsOption.addActionListener(this);

		JMenu editMenu = new JMenu("Edit");
		editMenu.add(changeDimensionsOption);

		return editMenu;
  }

  private JMenu createSaveMenu()
  {
		JMenuItem saveMenuOption = new JMenuItem(ACTION_SAVE);
		saveMenuOption.addActionListener(this);
		saveMenuOption.setActionCommand(ACTION_SAVE);

		JMenuItem loadMenuOption = new JMenuItem(ACTION_LOAD);
		loadMenuOption.addActionListener(this);
		loadMenuOption.setActionCommand(ACTION_LOAD);

		JMenu fileMenu = new JMenu("File");
		fileMenu.add(saveMenuOption);
		fileMenu.add(loadMenuOption);
		return fileMenu;
  }
	
	private MapGrid generateTestMap(int boardWidth, int boardHeight)
	{
		TestMapModelBuilder builder = new TestMapModelBuilder(boardWidth, boardHeight);
		mapModel = builder.generateMapModel();

		mapModel.setResourceLoader(resourceLoader);

		return mapModel;
	}
	
	/**
	 *  Will generate an empty map when given <i>null</i> but 
	 * generate map defined by <i>model</i> otherwise.
	 * 
	 * @param model
	 */
	private void regenerateMapPanel(MapGrid model)
	{
		if (mapPanel != null) 
		{
			mapPanel.removeMapEventListener(this);
			mainPanel.remove(mapPanel);
		}

		mapModel = (model == null ? generateTestMap(100, 100) : model);
		mapPanel = new MapPanel(mapModel, resourceLoader);
		mapPanel.setShowGrid(showGrid.isSelected());
		mapPanel.addMapEventListener(this);
		mainPanel.add(mapPanel, BorderLayout.CENTER);

		regenerateMapPreviewPanel();
		regenerateTileSelectionPanel();

		validate();
	}
	
  private void regenerateTileSelectionPanel()
  {
    if (tileSelect != null)
    {
      mainPanel.remove(tileSelect);
    }
    
    tileSelect = new TileSelectionPanel(resourceLoader, mapModel);
    mainPanel.add(tileSelect, BorderLayout.EAST);
  }

  private void regenerateMapPreviewPanel()
  {
    if (previewPanel != null)
    {
      mainPanel.remove(previewPanel);
    }
    
    previewPanel = new MapPreviewPanel(mapModel);
    mainPanel.add(previewPanel, BorderLayout.WEST);
  }
	
	public static void main(String[] args) 
	{
		MapEditor editorPanel = new MapEditor();
	}
	
	private String promptForFile()
	{
		// XXX Default file is probably not a good idea
		String file = "mapGridFileDefault.mgd";

		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Rox Map Grid Files (*.mgd)", 
																	 "mgd");
		chooser.setFileFilter(filter);

		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) 
		{
			println("Selected file : " + chooser.getSelectedFile().getAbsolutePath());

			file = chooser.getSelectedFile().getAbsolutePath();
		}

		return file;
	}
	
	private void resizeMap() 
	{
		JTextField newWidth = new JTextField();
		JTextField newHeight = new JTextField();

		final JComponent[] inputs = new JComponent[] { new JLabel("Width"),
													   newWidth, 
													   new JLabel("Height"), 
													   newHeight, 
													 };

		JOptionPane.showMessageDialog(null, 
									  inputs, 
									  "New Board Dimensions",
									  JOptionPane.QUESTION_MESSAGE);

		println("You entered " + newWidth.getText() + ", " + newHeight.getText());
		
		try 
		{
			Integer specifiedWidth = Integer.parseInt(newWidth.getText());
			Integer specifiedHeight = Integer.parseInt(newHeight.getText());

			if (specifiedWidth > 0 && specifiedHeight > 0) 
			{
				MapGrid mapGrid = new TestMapModel(specifiedHeight, specifiedWidth);
				regenerateMapPanel(mapGrid);
			} 
			else 
			{
			  println("Invalid sizes, both must be greater than 0");
			}
		} catch (NumberFormatException ex) {
		  println("Error parsing " + newWidth + "x" + newHeight + ": " + ex.getMessage());
		}
	}
	
	private void println(String outputString)
	{
	  consoleWindow.insert(outputString + "\n", 0);
	}
	
	@Override
	public void mapClicked(int x, int y) 
	{
		println("Selected (x:" + x + ", y:" + y + ")  -> " + tileSelect.getSelectedTile());
		
		if (mapModel == null)
		{
			mapModel = mapPanel.getMapModel();
		}
		
		mapModel.setTile(x, y, tileSelect.getSelectedTile());
		
		if (shiftPressed)
		{
			GridCoordinate now = mapPanel.getSelectedTile();
			GridCoordinate then = mapPanel.getLastSelectedTile();
			
			int deltaX = then.getX() - now.getX(); 
			int deltaY = then.getY() - now.getY();
			
			boolean xDirection = (deltaX < 0);
			boolean yDirection = (deltaY < 0);
			
			deltaX = Math.abs(deltaX);
			deltaY = Math.abs(deltaY);
			
			//color all tiles between
			for (int yi=0; yi<=deltaY; yi++)
			{
				for (int xi=0; xi<=deltaX; xi++)
				{
				  int xTile = (then.getX() + (xDirection ? xi : -xi));
				  int yTile = (then.getY() + (yDirection ? yi : -yi));
				  
				  println("Setting " + xTile + "," + yTile);
				  
				  //TODO use MapModel.setTiles() instead of all this
				  mapModel.setTile(xTile, yTile, tileSelect.getSelectedTile());
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
	  //XXX Move to Java 7 switch, this is ugly
		if (e.getActionCommand().equals(ACTION_SHOW_GRID))
		{
			boolean newVal = (e.getSource() == showGrid ? showGrid.isSelected() : showGridMenuOption.isSelected());
			mapPanel.setShowGrid(newVal);
			showGrid.setSelected(newVal);
			showGridMenuOption.setSelected(newVal);
		}
		else if (e.getActionCommand().equals(ACTION_SAVE))
		{
		  String userSelectedFile = promptForFile();
		  
			MapGridFile mapFile = new MapGridFile(userSelectedFile);
			mapFile.save(mapModel);
		}
		else if (e.getActionCommand().equals(ACTION_LOAD))
		{
		  String userSelectedFile = promptForFile();
		  
			MapGridFile mapFile = new MapGridFile(userSelectedFile);
			MapGrid mapGrid = mapFile.load();
			
			regenerateMapPanel(mapGrid);
		}
		else if (e.getActionCommand().equals(ACTION_RESIZE_MAP)){
		  resizeMap();

		}
	}
}

