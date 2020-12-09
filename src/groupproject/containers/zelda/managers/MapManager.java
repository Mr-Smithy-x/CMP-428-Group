package groupproject.containers.zelda.managers;

import groupproject.containers.zelda.algorithms.ATileStarAlgorithm;
import groupproject.gameengine.sprite.Sprite;
import groupproject.gameengine.tile.Tile;
import groupproject.gameengine.tile.TileMap;
import groupproject.gameengine.tile.TileMapModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
public class MapManager {

    private static final MapManager instance = new MapManager();
    private TileMap currentMap;
    private TileMap currentMapOverlay;
    private ATileStarAlgorithm aStar;

    private MapManager() {
    }

    public static MapManager getInstance() {
        return instance;
    }

    public void loadTileMap(String mapFile) {
        try (FileInputStream fis = new FileInputStream(TileMapModel.MAP_FOLDER + mapFile + ".tilemap"); ObjectInputStream is = new ObjectInputStream(fis)) {
            TileMapModel mapModel = (TileMapModel) is.readObject();
            currentMap = new TileMap(mapModel);
            currentMap.initializeMap();
            aStar = new ATileStarAlgorithm(currentMap);
            loadOverlayMap(TileMapModel.MAP_FOLDER + mapFile + "_overlay.tilemap");
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    private void loadOverlayMap(String overlayMapFile) {
        if (new File(overlayMapFile).exists()) {
            try (FileInputStream fis = new FileInputStream(overlayMapFile); ObjectInputStream is = new ObjectInputStream(fis)) {
                TileMapModel mapModel = (TileMapModel) is.readObject();
                currentMapOverlay = new TileMap(mapModel);
                currentMapOverlay.initializeMap();
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public TileMap getCurrentMap() {
        return currentMap;
    }

    public TileMap getCurrentMapOverlay() {
        return currentMapOverlay;
    }

    // Basic collision detection based on the 8 map tiles surrounding a sprite.
    public boolean isSpriteCollidingWithMap(Sprite sprite, Sprite.Pose pose) {
        boolean isColliding = false;
        int velocity = sprite.getVelocity();

        switch (pose) {
            case RIGHT:
                for (Tile tile : getCurrentMap().getSurroundingTiles(sprite.getBounds().getX().intValue(),
                        sprite.getBounds().getY().intValue(), Sprite.Pose.RIGHT))
                    if (tile.isCollisionEnabled() && sprite.getBounds().willOverlap(tile.getBoundsRect(), velocity, 0))
                        isColliding = true;
                break;
            case LEFT:
                for (Tile tile : getCurrentMap().getSurroundingTiles(sprite.getBounds().getX().intValue(),
                        sprite.getBounds().getY().intValue(), Sprite.Pose.LEFT))
                    if (tile.isCollisionEnabled() && sprite.getBounds().willOverlap(tile.getBoundsRect(), -velocity, 0))
                        isColliding = true;
                break;
            case UP:
                for (Tile tile : getCurrentMap().getSurroundingTiles(sprite.getBounds().getX().intValue(),
                        sprite.getBounds().getY().intValue(), Sprite.Pose.UP))
                    if (tile.isCollisionEnabled() && sprite.getBounds().willOverlap(tile.getBoundsRect(), 0, -velocity))
                        isColliding = true;
                break;
            case DOWN:
                for (Tile tile : getCurrentMap().getSurroundingTiles(sprite.getBounds().getX().intValue(),
                        sprite.getBounds().getY().intValue(), Sprite.Pose.DOWN))
                    if (tile.isCollisionEnabled() && sprite.getBounds().willOverlap(tile.getBoundsRect(), 0, velocity))
                        isColliding = true;
                break;
            default:
                break;
        }

        return isColliding;
    }

    public ATileStarAlgorithm getAStar() {
        return aStar;
    }
}
