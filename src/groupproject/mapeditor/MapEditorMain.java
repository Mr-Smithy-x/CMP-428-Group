package groupproject.mapeditor;

import groupproject.gameengine.tile.TileMapModel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class MapEditorMain {
    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "true");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            // Initialize main controller for the map editor.
            MapEditorController editorController = new MapEditorController();

            // Initialize all of the components for the editor.
            JFrame frame = new JFrame("Tile Map Editor");
            JScrollPane mapEditPane = new JScrollPane(editorController.getMapEditorView(),
                    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            JScrollPane tileSetPane = new JScrollPane(editorController.getMapEditorTileSetView(),
                    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            mapEditPane.getVerticalScrollBar().setUnitIncrement(16);
            tileSetPane.getVerticalScrollBar().setUnitIncrement(16);
            mapEditPane.getHorizontalScrollBar().setUnitIncrement(16);
            tileSetPane.getHorizontalScrollBar().setUnitIncrement(16);

            // Setup sizing for each component in the main window, and the split pane view.
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            mapEditPane.getViewport().setPreferredSize(new Dimension((int) (screenSize.getWidth() * 0.6),
                    (int) (screenSize.getHeight() * 0.75)));
            tileSetPane.getViewport().setPreferredSize(new Dimension((int) (mapEditPane.getViewport().getPreferredSize().getWidth() * 0.4),
                    (int) (mapEditPane.getViewport().getPreferredSize().getHeight() * 0.5)));
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mapEditPane, tileSetPane);
            splitPane.setResizeWeight(1.0);

            // Finally, put it all together into the JFrame.
            frame.add(splitPane);
            frame.setJMenuBar(editorController.getMapEditorMenuBar());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    JFileChooser fileChooser = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("TileMap Files (.tilemap)", "tilemap");
                    fileChooser.setFileFilter(filter);
                    if(editorController.mapNeedsSaving()) {
                        if (editorController.getLoadedFile() != null)
                            fileChooser.setSelectedFile(new File(editorController.getLoadedFile()));
                        else fileChooser.setCurrentDirectory(new File(TileMapModel.MAP_FOLDER));
                        int returnVal = fileChooser.showSaveDialog(null);
                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            String mapFile = fileChooser.getSelectedFile().getAbsolutePath();
                            editorController.saveTileMap(mapFile);
                            System.exit(0);
                        }
                    } else System.exit(0);
                }
            });
            frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        });
    }
}
