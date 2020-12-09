package groupproject.mapeditor.components;

import groupproject.gameengine.tile.TileMapModel;
import groupproject.mapeditor.MapEditorController;
import groupproject.mapeditor.model.EditorMode;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

// Suppress deprecation warnings, since SonarLint seems to think the project is set to Java 10+.
@SuppressWarnings("java:S1874")
public class MapEditorMenuBar extends JMenuBar {
    private static final JCheckBoxMenuItem paintMode = new JCheckBoxMenuItem("Paint Mode");
    private static final JCheckBoxMenuItem collisionMode = new JCheckBoxMenuItem("Collision Mode");
    private static final JCheckBoxMenuItem objectMode = new JCheckBoxMenuItem("Object Mode");
    private final transient MapEditorController editorController;

    public MapEditorMenuBar(MapEditorController controller) {
        this.editorController = controller;
        setupFileMenu();
        setupEditorModeMenu();
        setupEditMenu();
        setupViewMenu();
    }

    public static void setEditorMenuStatus(EditorMode status) {
        switch (status) {
            case PAINT:
                paintMode.setSelected(true);
                collisionMode.setSelected(false);
                objectMode.setSelected(false);
                break;
            case OBJECT:
                paintMode.setSelected(false);
                collisionMode.setSelected(false);
                objectMode.setSelected(true);
                break;
            case COLLISION:
                paintMode.setSelected(false);
                collisionMode.setSelected(true);
                objectMode.setSelected(false);
                break;
            default:
                break;
        }
    }

    private void setupFileMenu() {
        JMenu fileMenu = new JMenu("File");
        JMenuItem newFile = new JMenuItem("Create Map..");
        JMenuItem openFile = new JMenuItem("Open Map..");
        JMenuItem saveFile = new JMenuItem("Save Map..");
        JMenuItem generateMapFromImage = new JMenuItem("Generate Map From Image...");
        fileMenu.add(newFile);
        fileMenu.add(openFile);
        fileMenu.add(saveFile);
        fileMenu.add(generateMapFromImage);
        this.add(fileMenu);

        KeyStroke newMapShortcut = KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        KeyStroke openFileShortcut = KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        KeyStroke saveFileShortcut = KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        newFile.setAccelerator(newMapShortcut);
        openFile.setAccelerator(openFileShortcut);
        saveFile.setAccelerator(saveFileShortcut);

        newFile.addActionListener((ActionEvent event) -> new NewMapDialog(editorController));
        generateMapFromImage.addActionListener((ActionEvent event) -> new GenerateMapDialog(editorController));

        openFile.addActionListener((ActionEvent event) -> {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("TileMap Files (.tilemap)", "tilemap");
            fileChooser.setFileFilter(filter);
            fileChooser.setCurrentDirectory(new File(TileMapModel.MAP_FOLDER));
            int returnVal = fileChooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String mapFile = fileChooser.getSelectedFile().getAbsolutePath();
                editorController.loadTileMap(mapFile);
            }
        });

        saveFile.addActionListener((ActionEvent event) -> {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("TileMap Files (.tilemap)", "tilemap");
            fileChooser.setFileFilter(filter);
            if (editorController.mapNeedsSaving()) {
                if (editorController.getLoadedFile() != null)
                    fileChooser.setSelectedFile(new File(editorController.getLoadedFile()));
                else fileChooser.setCurrentDirectory(new File(TileMapModel.MAP_FOLDER));
                int returnVal = fileChooser.showSaveDialog(this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    String mapFile = fileChooser.getSelectedFile().getAbsolutePath();
                    editorController.saveTileMap(mapFile);
                }
            }
        });
    }

    private void setupEditorModeMenu() {
        JMenu editorMode = new JMenu("Editor Mode");
        editorMode.add(paintMode);
        editorMode.add(collisionMode);
        editorMode.add(objectMode);
        this.add(editorMode);

        KeyStroke paintShortcut = KeyStroke.getKeyStroke(KeyEvent.VK_P, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        paintMode.setAccelerator(paintShortcut);
        KeyStroke collisionShortcut = KeyStroke.getKeyStroke(KeyEvent.VK_L, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        collisionMode.setAccelerator(collisionShortcut);
        KeyStroke objectShortcut = KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        objectMode.setAccelerator(objectShortcut);
        paintMode.addActionListener((ActionEvent event) -> editorController.setEditorMode(EditorMode.PAINT));
        collisionMode.addActionListener((ActionEvent event) -> editorController.setEditorMode(EditorMode.COLLISION));
        objectMode.addActionListener((ActionEvent event) -> editorController.setEditorMode(EditorMode.OBJECT));
    }

    private void setupEditMenu() {
        JMenu editMenu = new JMenu("Edit");
        JMenuItem fillOption = new JMenuItem("Paint All Tiles");
        KeyStroke fillShortcut = KeyStroke.getKeyStroke(KeyEvent.VK_A, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        fillOption.setAccelerator(fillShortcut);

        JMenuItem fillRow = new JMenuItem("Fill Current Row");
        KeyStroke fillRowShortcut = KeyStroke.getKeyStroke(KeyEvent.VK_R, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        fillRow.setAccelerator(fillRowShortcut);

        JMenuItem fillCol = new JMenuItem("Fill Current Column");
        KeyStroke fillColShortcut = KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        fillCol.setAccelerator(fillColShortcut);

        JMenuItem selectedMoveUp = new JMenuItem("Selected Tile - Move up");
        KeyStroke selectedMoveUpKey = KeyStroke.getKeyStroke(KeyEvent.VK_W, 0);
        selectedMoveUp.setAccelerator(selectedMoveUpKey);

        JMenuItem selectedMoveDown = new JMenuItem("Selected Tile - Move down");
        KeyStroke selectedMoveDownKey = KeyStroke.getKeyStroke(KeyEvent.VK_S, 0);
        selectedMoveDown.setAccelerator(selectedMoveDownKey);

        JMenuItem selectedMoveLeft = new JMenuItem("Selected Tile - Move left");
        KeyStroke selectedMoveLeftKey = KeyStroke.getKeyStroke(KeyEvent.VK_A, 0);
        selectedMoveLeft.setAccelerator(selectedMoveLeftKey);

        JMenuItem selectedMoveRight = new JMenuItem("Selected Tile - Move right");
        KeyStroke selectedMoveRightKey = KeyStroke.getKeyStroke(KeyEvent.VK_D, 0);
        selectedMoveRight.setAccelerator(selectedMoveRightKey);

        JMenuItem addRowToTop = new JMenuItem("Add Row to Top");
        JMenuItem addRowToBottom = new JMenuItem("Add Row to Bottom");

        fillOption.addActionListener((ActionEvent event) -> editorController.fillMapWithSelectedTile());
        fillRow.addActionListener((ActionEvent event) -> editorController.fillRowBasedOnEditorMode());
        fillCol.addActionListener((ActionEvent event) -> editorController.fillColumnBasedOnEditorMode());
        addRowToTop.addActionListener((ActionEvent event) -> editorController.addRowToLayout("top"));
        addRowToBottom.addActionListener((ActionEvent event) -> editorController.addRowToLayout("bottom"));
        selectedMoveUp.addActionListener((ActionEvent event) -> editorController.changeSelectedTileInView("up"));
        selectedMoveDown.addActionListener((ActionEvent event) -> editorController.changeSelectedTileInView("down"));
        selectedMoveLeft.addActionListener((ActionEvent event) -> editorController.changeSelectedTileInView("left"));
        selectedMoveRight.addActionListener((ActionEvent event) -> editorController.changeSelectedTileInView("right"));

        editMenu.add(fillOption);
        editMenu.add(fillRow);
        editMenu.add(fillCol);
        editMenu.add(addRowToTop);
        editMenu.add(addRowToBottom);
        editMenu.add(selectedMoveUp);
        editMenu.add(selectedMoveDown);
        editMenu.add(selectedMoveLeft);
        editMenu.add(selectedMoveRight);
        this.add(editMenu);
    }

    private void setupViewMenu() {
        JMenu viewMenu = new JMenu("View");
        JMenuItem zoomIn = new JMenuItem("Zoom In");
        JMenuItem zoomOut = new JMenuItem("Zoom Out");
        JMenuItem viewCollisionTiles = new JMenuItem("Show Collision Tiles");
        JMenuItem hideCollisionTiles = new JMenuItem("Hide Collision Tiles");

        zoomIn.addActionListener((ActionEvent event) -> editorController.setScaleFactor(editorController.getScaleFactor() + 0.25));
        zoomOut.addActionListener((ActionEvent event) -> editorController.setScaleFactor(editorController.getScaleFactor() - 0.25));
        viewCollisionTiles.addActionListener((ActionEvent event) -> editorController.setViewCollisionTileStatus(true));
        hideCollisionTiles.addActionListener((ActionEvent event) -> editorController.setViewCollisionTileStatus(false));

        viewMenu.add(zoomIn);
        viewMenu.add(zoomOut);
        viewMenu.add(viewCollisionTiles);
        viewMenu.add(hideCollisionTiles);
        this.add(viewMenu);
    }
}
