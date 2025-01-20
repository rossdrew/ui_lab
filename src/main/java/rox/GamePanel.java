package rox;

import rox.gui.MapPanel;
import rox.gui.MapPreviewPanel;
import rox.model.MapGrid;
import rox.model.file.MapGridFile;
import rox.resource.ResourceLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePanel extends JFrame {
    private final ResourceLoader resourceLoader = new ResourceLoader();
    private JCheckBox showGrid;

    private MapGrid mapGrid;

    private MapPanel mapPanel;

    public GamePanel(){
        super("Discovery: The New World");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setVisible(true);

        setLayout(new BorderLayout());

        createMapPanel();
        createPreviewPanel();
        createToolbar();
    }

    private void createMapPanel(){
        mapPanel = new MapPanel(createMapFromFile(), resourceLoader);
        add(mapPanel, BorderLayout.CENTER);
    }

    private void createToolbar(){
        final JToolBar toolBar = new JToolBar();
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

    private void createPreviewPanel(){
        MapPreviewPanel previewPanel = new MapPreviewPanel(mapGrid);

        add(previewPanel, BorderLayout.EAST);
    }

    private MapGrid createMapFromFile(){
        final MapGridFile mapFile = new MapGridFile("mapGridFileX.mgd");
        mapGrid = mapFile.load();
        mapGrid.setResourceLoader(resourceLoader);

        return mapGrid;
    }

    public static void main(String[] args)
    {
        GamePanel gamePanel = new GamePanel();
    }
}
