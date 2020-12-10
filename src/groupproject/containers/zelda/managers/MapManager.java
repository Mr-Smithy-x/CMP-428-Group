package groupproject.containers.zelda.managers;

import groupproject.containers.zelda.algorithms.ATileStarAlgorithm;
import groupproject.containers.zelda.models.TransitionTile;
import groupproject.gameengine.sprite.AttackSprite;
import groupproject.gameengine.sprite.Sprite;
import groupproject.gameengine.tile.Tile;
import groupproject.gameengine.tile.TileMap;
import groupproject.gameengine.tile.TileMapModel;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Optional;

public class MapManager {

    private static class Points {

        public static Rectangle[] HYRULE_CASTLE = new Rectangle[]{
                new Rectangle(384, 1120, 80, 16), //Bottom Of Hyrule Castle
                //BOTTOM: x: (384.0 - 464.0),(y:1136.0);
                //LEFT_ENTRANCE: (128-144), 416.0
                //RIGHT:  (704, 720) - 416.0
                //TOP: (416-432) - 32
        };

        public static Rectangle[] HYRULE_GARDEN = new Rectangle[]{
                new Rectangle(992, 0, 80, 16) //top of garden
                //BOTTOM: x: (384.0 - 464.0),(y:1136.0);
                //LEFT_ENTRANCE: (128-144), 416.0
                //RIGHT:  (704, 720) - 416.0
                //TOP: (416-432) - 32
        };

        public static LinkedHashSet<TransitionTile> getTiles(TransitionTile.Map map) {
            LinkedHashSet<TransitionTile> set = new LinkedHashSet<>();
            Rectangle rectangle;
            switch (map) {
                case CASTLE_HYRULE:
                    rectangle = HYRULE_CASTLE[0];
                    set.add(TransitionTile.create(rectangle.x, rectangle.y, rectangle.width, rectangle.height, new Point(1004, 100), TransitionTile.Map.CASTLE_GARDEN));
                    break;
                case CASTLE_GARDEN:
                    rectangle = HYRULE_GARDEN[0];
                    set.add(TransitionTile.create(rectangle.x, rectangle.y, rectangle.width, rectangle.height, new Point(424, 1104), TransitionTile.Map.CASTLE_HYRULE));
                    break;
            }
            return set;
        }


        private Points() {
        }
    }

    private static final MapManager instance = new MapManager();
    private ATileStarAlgorithm aStar;
    private TransitionTile.Map current;
    private HashMap<TransitionTile.Map, LinkedHashSet<TransitionTile>> transitionTiles = new HashMap<>();
    private final TransitionTile deadTile = TransitionTile.create(0, 0, 0, 0, new Point(0, 0), TransitionTile.Map.NONE);

    private MapManager() {
    }


    public static MapManager getInstance() {
        return instance;
    }


    public TransitionTile checkTransitionTile(Sprite sprite) {
        LinkedHashSet<TransitionTile> transitionTiles = this.transitionTiles.get(current);
        Optional<TransitionTile> first = transitionTiles.stream().filter(sprite::isOverlapping).findFirst();
        return first.orElse(deadTile);
    }

    public void transition(Sprite sprite, TransitionTile tile) {
        sprite.setWorld(tile.getSpawn().x, tile.getSpawn().y);
        loadTileMap(tile.getMap().getFile());
    }

    public void loadTileMap(String mapFile) {
        TransitionTile.Map map = TransitionTile.Map.parse(mapFile);
        if (transitionTiles.containsKey(map)) {
            Optional<TransitionTile.Map> first = transitionTiles.keySet().stream().filter(p -> p.getFile().equals(map.getFile())).findFirst();
            first.ifPresent(value -> current = value);
        } else {
            try (FileInputStream fis = new FileInputStream(TileMapModel.MAP_FOLDER + mapFile + ".tilemap"); ObjectInputStream is = new ObjectInputStream(fis)) {
                TileMapModel mapModel = (TileMapModel) is.readObject();
                TileMap currentMap = new TileMap(mapModel);
                currentMap.initializeMap();
                if (aStar == null) {
                    aStar = new ATileStarAlgorithm(currentMap);
                } else {
                    aStar.setNetwork(currentMap);
                }
                TileMap currentMapOverlay = loadOverlayMap(TileMapModel.MAP_FOLDER + mapFile + "_overlay.tilemap");
                map.setMap(currentMap);
                if (currentMapOverlay != null) {
                    map.setMapOverlay(currentMapOverlay);
                }
                LinkedHashSet<TransitionTile> tiles = Points.getTiles(map);
                transitionTiles.put(map, tiles);
                current = map;
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private TileMap loadOverlayMap(String overlayMapFile) {
        if (new File(overlayMapFile).exists()) {
            try (FileInputStream fis = new FileInputStream(overlayMapFile); ObjectInputStream is = new ObjectInputStream(fis)) {
                TileMapModel mapModel = (TileMapModel) is.readObject();
                TileMap currentMapOverlay = new TileMap(mapModel);
                currentMapOverlay.initializeMap();
                return currentMapOverlay;
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public TileMap getCurrentMap() {
        return current.getMap();
    }

    public TileMap getCurrentMapOverlay() {
        return current.getOverlayMap();
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
