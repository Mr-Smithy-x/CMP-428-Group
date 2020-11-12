package groupproject.gameengine.tile;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TileMap {
    private final TileMapModel mapModel;
    private final TileSet tileSet;
    private final int mapWidth;
    private final int mapHeight;
    private final Tile[][] mainLayerTiles;
    private final Tile[][] objectLayerTiles;

    public TileMap(TileMapModel mapModel) {
        this.tileSet = new TileSet(mapModel.getPerTileWidth(), mapModel.getPerTileHeight(), mapModel.getTileSetFile());
        this.mapWidth = mapModel.getPerTileWidth() * mapModel.getMapColumns();
        this.mapHeight = mapModel.getPerTileHeight() * mapModel.getMapRows();
        this.mapModel = mapModel;
        this.mainLayerTiles = new Tile[mapModel.getMapRows()][mapModel.getMapColumns()];
        this.objectLayerTiles = new Tile[mapModel.getMapRows()][mapModel.getMapColumns()];
    }

    public void initializeMap() {
        for (int row = 0; row < mapModel.getMapRows(); row++) {
            for (int col = 0; col < mapModel.getMapColumns(); col++) {
                BufferedImage tileImage = tileSet.getTileImageList().get(mapModel.getMapLayout()[row][col]);
                Tile currentTile = new Tile(tileImage);
                currentTile.setCollisionEnabled(mapModel.getCollisionMap()[row][col]);
                if(mapModel.getObjectMap()[row][col] != -1) {
                    Tile objectTile = new Tile(tileSet.getTileImageList().get(mapModel.getObjectMap()[row][col]));
                    objectTile.setX(mapModel.getPerTileWidth()*col);
                    objectTile.setY(mapModel.getPerTileHeight()*row);
                    objectLayerTiles[row][col] = objectTile;
                }
                currentTile.setX(mapModel.getPerTileWidth()*col);
                currentTile.setY(mapModel.getPerTileHeight()*row);
                currentTile.initBoundsRect();
                mainLayerTiles[row][col] = currentTile;
            }
        }
    }

    public void drawMap(Graphics g) {
        BufferedImage map = new BufferedImage(mapWidth, mapHeight, BufferedImage.TYPE_INT_RGB);
        Graphics gMap = map.getGraphics();
        for (int row = 0; row < mapModel.getMapRows(); row++) {
            for (int col = 0; col < mapModel.getMapColumns(); col++) {
                Tile currentTile = mainLayerTiles[row][col];
                currentTile.draw(gMap);
                if(objectLayerTiles[row][col] != null) objectLayerTiles[row][col].draw(gMap);
            }
        }
        g.drawImage(map, 0, 0, null);
    }

    public static BufferedImage resizeBufferedImage(BufferedImage image, int width, int height) {
        Image tempImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = resizedImage.createGraphics();
        g.drawImage(tempImage, 0, 0, null);
        g.dispose();
        return resizedImage;
    }

    public Tile[][] getMainLayerTiles() {
        return mainLayerTiles;
    }
}
