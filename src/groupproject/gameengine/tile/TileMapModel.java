package groupproject.gameengine.tile;

import java.io.Serializable;

public class TileMapModel implements Serializable {
    public static final String MAP_FOLDER = "assets/maps/";
    // Constant serialization UID so that maps can be deserialized despite running JVM version.
    private static final long serialVersionUID = 4112L;
    private final String tileSetFile;
    private final int perTileWidth;
    private final int perTileHeight;
    private final int mapColumns;
    private int mapRows;
    private int[][] mapLayout;
    private boolean[][] collisionMap;
    private int[][] objectMap;

    public TileMapModel(String file, int perTileWidth, int perTileHeight, int mapRows, int mapColumns) {
        this.tileSetFile = file;
        this.perTileWidth = perTileWidth;
        this.perTileHeight = perTileHeight;
        this.mapColumns = mapColumns;
        this.mapRows = mapRows;
        this.mapLayout = new int[mapRows][mapColumns];
        this.collisionMap = new boolean[mapRows][mapColumns];
        this.objectMap = new int[mapRows][mapColumns];

        for (int row = 0; row < mapRows; row++) {
            for (int col = 0; col < mapColumns; col++) {
                mapLayout[row][col] = -1;
                collisionMap[row][col] = false;
                objectMap[row][col] = -1;
            }
        }
    }

    public String getTileSetFile() {
        return tileSetFile;
    }

    public int getPerTileWidth() {
        return perTileWidth;
    }

    public int getPerTileHeight() {
        return perTileHeight;
    }

    public int getMapRows() {
        return mapRows;
    }

    public void setMapRows(int mapRows) {
        this.mapRows = mapRows;
    }

    public int getMapColumns() {
        return mapColumns;
    }

    public int[][] getMapLayout() {
        return mapLayout;
    }

    public void setMapLayout(int[][] mapLayout) {
        this.mapLayout = mapLayout;
    }

    public boolean[][] getCollisionMap() {
        return collisionMap;
    }

    public void setCollisionMap(boolean[][] collisionMap) {
        this.collisionMap = collisionMap;
    }

    public int[][] getObjectMap() {
        return objectMap;
    }

    public void setObjectMap(int[][] objectMap) {
        this.objectMap = objectMap;
    }
}
