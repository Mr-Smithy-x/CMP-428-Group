package groupproject.containers.zelda.managers;

import groupproject.gameengine.tile.TileMap;
import groupproject.gameengine.tile.TileMapModel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class MapManager {

    private static final MapManager instance = new MapManager();
    private TileMap currentMap;

    private MapManager() { }

    public static MapManager getInstance() {
     return instance;
    }

    public void loadTileMap(String mapFile) {
        try (FileInputStream fis = new FileInputStream(TileMapModel.MAP_FOLDER + mapFile); ObjectInputStream is = new ObjectInputStream(fis)) {
            TileMapModel mapModel = (TileMapModel) is.readObject();
            currentMap = new TileMap(mapModel);
            currentMap.initializeMap();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    public TileMap getCurrentMap() {
        return currentMap;
    }
}
