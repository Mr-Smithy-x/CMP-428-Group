package groupproject.containers.zelda.models;

import groupproject.gameengine.tile.Tile;
import groupproject.gameengine.tile.TileMap;

import java.awt.*;
import java.util.Arrays;

public class TransitionTile extends Tile {

    public enum Map {
        NONE(""),
        CASTLE_GARDEN("hyrule_castle/castle_garden"),
        CASTLE_HYRULE("hyrule_castle/hyrule_castle_entrance"),
        CASTLE_MAIN_HALL("hyrule_castle/main_hall"),
        CASTLE_THRONE_ROOM("hyrule_castle/throne_room"),
        CASTLE_BEDROOM("hyrule_castle/castle_bedroom"),
        CASTLE_BASEMENT("hyrule_castle/basement"),
        CASTLE_BASEMENT_ENTRANCE("basement_crypt/entrance"),
        CASTLE_BASEMENT_HALL("basement_crypt/hall"),
        CASTLE_BOSS_ROOM("basement_crypt/boss_room"),
        FOREST("forest_wip");

        private String file;
        private TileMap map;
        private TileMap overlayMap;

        Map(String file) {
            this.file = file;
        }

        public static Map parse(String tile) {
            boolean contains = Arrays.stream(Map.values()).anyMatch(m -> m.file.equals(tile));
            if (contains) {
                return Arrays.stream(Map.values()).filter(p -> p.file.equals(tile)).findFirst().get();
            }
            return NONE;
        }

        public String getFile() {
            return file;
        }

        public TileMap getMap() {
            return map;
        }

        public TileMap getOverlayMap() {
            return overlayMap;
        }

        public void setMap(TileMap map) {
            this.map = map;
        }

        public void setMapOverlay(TileMap currentMap) {
            this.overlayMap = currentMap;
        }
    }

    private Map map;

    public static TransitionTile create(int worldX, int worldY, int tileWidth, int tileHeight, Point spawn, Map map) {
        TransitionTile transitionTile = new TransitionTile(worldX, worldY, tileWidth, tileHeight);
        transitionTile.point = spawn;
        transitionTile.map = map;
        return transitionTile;
    }

    private TransitionTile(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public Map getMap() {
        return map;
    }

    public Point getSpawn() {
        return point;
    }

}
