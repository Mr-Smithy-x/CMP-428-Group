package groupproject.mapeditor;

import groupproject.gameengine.tile.TileMapModel;
import groupproject.gameengine.tile.TileSet;
import groupproject.mapeditor.components.MapEditorMenuBar;
import groupproject.mapeditor.model.EditorMode;
import groupproject.mapeditor.model.MapEditorModel;
import groupproject.mapeditor.views.MapEditorTileSetView;
import groupproject.mapeditor.views.MapEditorView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class MapEditorController {
    private final MapEditorModel model;
    private final MapEditorView mapEditorView;
    private final MapEditorMenuBar mapEditorMenuBar;
    private final MapEditorTileSetView mapEditorTileSetView;
    private BufferedImage selectedTile;
    private EditorMode editorMode;
    private int lastClickedRow;
    private int lastClickedCol;
    private String loadedFile;
    private boolean mapNeedsSaving;
    private boolean viewCollisionTileStatus = true;
    private double scaleFactor = 1;

    public MapEditorController() {
        this.model = new MapEditorModel();
        this.mapEditorView = new MapEditorView(this);
        this.mapEditorTileSetView = new MapEditorTileSetView(this);
        this.mapEditorMenuBar = new MapEditorMenuBar(this);
        this.mapNeedsSaving = false;
        setEditorMode(EditorMode.PAINT);
    }

    public void loadTileMap(String mapFile) {
        try (FileInputStream fis = new FileInputStream(mapFile); ObjectInputStream is = new ObjectInputStream(fis)) {
            TileMapModel mapModel = (TileMapModel) is.readObject();
            model.setMapModel(mapModel);
            initializeViews();
            loadedFile = mapFile;
            mapNeedsSaving = true;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeViews() {
        model.setTileSet(new TileSet(model.getMapModel().getPerTileWidth(), model.getMapModel().getPerTileHeight(),
                model.getMapModel().getTileSetFile()));
        mapEditorView.loadInitialMapView(model.getMapModel());
        mapEditorTileSetView.initTileSetView();
        setEditorMode(EditorMode.PAINT);
        this.selectedTile = null;
    }

    public void createTileMap(String file, int perTileWidth, int perTileHeight, int mapRows, int mapColumns) {
        model.setMapModel(new TileMapModel(file, perTileWidth, perTileHeight, mapRows, mapColumns));
        initializeViews();
        loadedFile = null;
        mapNeedsSaving = true;
    }

    public void generateTileMap(String tileSetImageFile, int tileWidth, int tileHeight) {
        try {
            BufferedImage imageForGenerate = ImageIO.read(new File(TileSet.TILE_FOLDER + tileSetImageFile));
            createTileMap(tileSetImageFile, tileWidth, tileHeight, imageForGenerate.getHeight() / tileHeight,
                    imageForGenerate.getWidth() / tileWidth);
            for (int row = 0; row < model.getMapModel().getMapRows(); row++) {
                for (int col = 0; col < model.getMapModel().getMapColumns(); col++) {
                    int nextTileIndex = (row * getTileSet().getTileSetColumns()) + col;
                    model.getMapModel().getMapLayout()[row][col] = nextTileIndex;
                }
            }
            mapEditorView.loadInitialMapView(model.getMapModel());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveTileMap(String mapFile) {
        if (!mapFile.endsWith(".tilemap")) {
            mapFile += ".tilemap";
        }
        try (FileOutputStream fos = new FileOutputStream(mapFile); ObjectOutputStream os = new ObjectOutputStream(fos)) {
            os.writeObject(model.getMapModel());
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadedFile = mapFile;
    }

    public void fillMapWithSelectedTile() {
        for (int row = 0; row < model.getMapModel().getMapRows(); row++) {
            for (int col = 0; col < model.getMapModel().getMapColumns(); col++) {
                switch (editorMode) {
                    case PAINT:
                        updateTileInMap(row, col, false);
                        break;
                    case COLLISION:
                    case OBJECT:
                        break;
                }
            }
        }
        mapEditorView.loadInitialMapView(model.getMapModel());
    }

    public void updateTileInMap(int row, int col, boolean setEmpty) {
        if (!setEmpty)
            model.getMapModel().getMapLayout()[row][col] = getTileSet().getTileImageList().indexOf(selectedTile);
        else model.getMapModel().getMapLayout()[row][col] = -1;
    }

    public void fillRowBasedOnEditorMode() {
        for (int col = 0; col < model.getMapModel().getMapColumns(); col++) {
            switch (editorMode) {
                case PAINT:
                    updateTileInMap(lastClickedRow, col, false);
                    break;
                case COLLISION:
                    updateCollisionTileInMap(lastClickedRow, col, !getCollisionStatusAt(lastClickedRow, col));
                    break;
                case OBJECT:
                    updateTileInObjectMap(lastClickedRow, col, false);
                    break;
            }
        }
        mapEditorView.loadInitialMapView(model.getMapModel());
    }

    public void updateCollisionTileInMap(int row, int col, boolean status) {
        model.getMapModel().getCollisionMap()[row][col] = status;
    }

    public void updateTileInObjectMap(int row, int col, boolean setEmpty) {
        if (!setEmpty)
            model.getMapModel().getObjectMap()[row][col] = getTileSet().getTileImageList().indexOf(selectedTile);
        else model.getMapModel().getObjectMap()[row][col] = -1;
    }

    public void fillColumnBasedOnEditorMode() {
        for (int row = 0; row < model.getMapModel().getMapRows(); row++) {
            switch (editorMode) {
                case PAINT:
                    updateTileInMap(row, lastClickedCol, false);
                    break;
                case COLLISION:
                    updateCollisionTileInMap(row, lastClickedCol, !getCollisionStatusAt(row, lastClickedCol));
                    break;
                case OBJECT:
                    updateTileInObjectMap(row, lastClickedCol, false);
                    break;
            }
        }
        mapEditorView.loadInitialMapView(model.getMapModel());
    }

    public void addRowToLayout(String location) {
        int[][] currentMapLayout = model.getMapModel().getMapLayout();
        boolean[][] currentCollisionMap = model.getMapModel().getCollisionMap();
        int[][] currentObjectMap = model.getMapModel().getObjectMap();
        int newRowCount = model.getMapModel().getMapRows() + 1;
        int colCount = model.getMapModel().getMapColumns();

        int[][] newMapLayout = new int[newRowCount][colCount];
        boolean[][] newCollisionMap = new boolean[newRowCount][colCount];
        int[][] newObjectMap = new int[newRowCount][colCount];

        int offset = location.equals("top") ? 1 : 0;
        for (int row = location.equals("top") ? 1 : 0; row < model.getMapModel().getMapRows() + offset; row++) {
            for (int col = 0; col < model.getMapModel().getMapColumns(); col++) {
                newMapLayout[row][col] = currentMapLayout[row - offset][col];
                newCollisionMap[row][col] = currentCollisionMap[row - offset][col];
                newObjectMap[row][col] = currentObjectMap[row - offset][col];
            }
        }

        model.getMapModel().setMapRows(newRowCount);
        model.getMapModel().setMapLayout(newMapLayout);
        model.getMapModel().setCollisionMap(newCollisionMap);
        model.getMapModel().setObjectMap(newObjectMap);
        mapEditorView.loadInitialMapView(model.getMapModel());
    }

    public MapEditorView getMapEditorView() {
        return mapEditorView;
    }

    public MapEditorMenuBar getMapEditorMenuBar() {
        return mapEditorMenuBar;
    }

    public MapEditorTileSetView getMapEditorTileSetView() {
        return mapEditorTileSetView;
    }

    public BufferedImage getSelectedTile() {
        return selectedTile;
    }

    public void setSelectedTile(int row, int col) {
        this.selectedTile = getTileSet().getTileImageList().get((row * getTileSet().getTileSetColumns()) + col);
    }

    public EditorMode getEditorMode() {
        return editorMode;
    }

    public void setEditorMode(EditorMode editorMode) {
        this.editorMode = editorMode;
        MapEditorMenuBar.setEditorMenuStatus(editorMode);
    }

    public void setLastClickedRow(int clickedY) {
        this.lastClickedRow = clickedY / model.getMapModel().getPerTileHeight();
    }

    public void setLastClickedCol(int clickedX) {
        this.lastClickedCol = clickedX / model.getMapModel().getPerTileWidth();
    }

    public String getLoadedFile() {
        return loadedFile;
    }

    public boolean mapNeedsSaving() {
        return mapNeedsSaving;
    }

    public int getTileWidth() {
        return model.getMapModel().getPerTileWidth();
    }

    public int getTileHeight() {
        return model.getMapModel().getPerTileHeight();
    }

    public double getScaleFactor() {
        return scaleFactor;
    }

    public void setScaleFactor(double scaleFactor) {
        this.scaleFactor = scaleFactor;
        mapEditorView.loadInitialMapView(model.getMapModel());
    }

    public boolean isViewCollisionTileStatus() {
        return viewCollisionTileStatus;
    }

    public void setViewCollisionTileStatus(boolean viewCollisionTileStatus) {
        this.viewCollisionTileStatus = viewCollisionTileStatus;
        mapEditorView.loadInitialMapView(model.getMapModel());
    }

    public int getTileAt(int row, int col) {
        return this.model.getMapModel().getMapLayout()[row][col];
    }

    public int getObjectTileAt(int row, int col) {
        return this.model.getMapModel().getObjectMap()[row][col];
    }

    public boolean getCollisionStatusAt(int row, int col) {
        return model.getMapModel().getCollisionMap()[row][col];
    }

    public TileSet getTileSet() {
        return model.getTileSet();
    }

    public BufferedImage getTileImage(int row, int col) {
        return getTileSet().getTileImageList().get(model.getMapModel().getMapLayout()[row][col]);
    }

    public BufferedImage getObjectTileImage(int row, int col) {
        return getTileSet().getTileImageList().get(model.getMapModel().getObjectMap()[row][col]);
    }

    public void changeSelectedTileInView(String direction) {
        switch (direction) {
            case "up":
                mapEditorTileSetView.changeSelectedRowAndColBy(-1, 0);
                break;
            case "down":
                mapEditorTileSetView.changeSelectedRowAndColBy(1, 0);
                break;
            case "left":
                mapEditorTileSetView.changeSelectedRowAndColBy(0, -1);
                break;
            case "right":
                mapEditorTileSetView.changeSelectedRowAndColBy(0, 1);
                break;
            default:
                break;
        }
    }
}
