package groupproject.containers.zelda.algorithms;

import groupproject.gameengine.algorithms.AStar;
import groupproject.gameengine.tile.Tile;
import groupproject.gameengine.tile.TileMap;

//witty ;)
public class ATileStarAlgorithm extends AStar<TileMap, Tile> {
    public ATileStarAlgorithm(TileMap network) {
        super(network);
    }
}
