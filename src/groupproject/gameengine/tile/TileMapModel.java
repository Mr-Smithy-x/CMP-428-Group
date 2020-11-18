package groupproject.gameengine.tile;

import java.io.Serializable;

public class TileMapModel implements Serializable {
    public static final String MAP_FOLDER = "assets/maps/";
    // Constant serialization UID so that maps can be deserialized despite running JVM version.
    private static final long serialVersionUID = 4112L;
    private final String tileSetFile;
    private final int perTileWidth;
    private final int perTileHeight;
    private final int mapRows;
    private final int mapColumns;
    private final int[][] mapLayout;
    private final boolean[][] collisionMap;
    private final int[][] objectMap;

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

    public int getMapColumns() {
        return mapColumns;
    }

    public int[][] getMapLayout() {
        return mapLayout;
    }

    public boolean[][] getCollisionMap() {
        return collisionMap;
    }

    public int[][] getObjectMap() {
        return objectMap;
    }
}
