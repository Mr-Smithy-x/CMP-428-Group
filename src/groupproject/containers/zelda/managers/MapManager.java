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
                new Rectangle(384, 1120, 80, 16), //Bottom Of Hyrule Castle - TO CASTLE GARDEN
                new Rectangle(416, 0, 32, 48), //Top Of Hyrule Castle - TO MAIN HALL
        };
        public static Rectangle[] CASTLE_MAIN_HALL = new Rectangle[]{
                new Rectangle(160, 64, 16, 16), //Top Of left Main Hall - TO BASEMENT
                new Rectangle(800, 64, 16, 16), //Top Of right Main Hall - TO BASEMENT
                new Rectangle(480, 1040 - 16, 16, 16), //Bottom Main Hall - TO Entrance Hall
                new Rectangle(480, 0, 16, 16), //Bottom Main Hall - To Throne Room
        };
        public static Rectangle[] HYRULE_GARDEN = new Rectangle[]{
                new Rectangle(992, 0, 80, 48), //top of garden
                new Rectangle(960, 960, 80, 48) //bottom of garden
        };

        public static Rectangle[] FOREST = new Rectangle[]{
                new Rectangle(736, 0, 176, 16), //top of garden
        };

        public static Rectangle[] CASTLE_BASEMENT = new Rectangle[]{
                new Rectangle(192, 32, 16, 16), // top left of castle_basement
                new Rectangle(896, 32, 16, 16), // top right of castle_basement
                new Rectangle(544, 896, 16, 16) // mid of castle_basement
        };
        public static Rectangle[] CASTLE_BASEMENT_HALL = new Rectangle[]{
                new Rectangle(256, 16, 16, 32), // top  of basement_hall
                new Rectangle(256, 416, 16, 16), // bottom  of basement_hall
        };
        public static Rectangle[] CASTLE_BASEMENT_ENTRANCE = new Rectangle[]{
                new Rectangle(256, 0, 16, 16), // top  of basement_entrance
                new Rectangle(256, 304, 16, 16), // bottom  of basement_entrance
        };

        public static Rectangle[] CASTLE_THRONE_ROOM = new Rectangle[]{
                new Rectangle(256, 688, 16, 16), // bottom  of throne
                new Rectangle(528, 288, 16, 16), // right  of throne
        };

        public static Rectangle[] CASTLE_BEDROOM = new Rectangle[]{
                new Rectangle(0, 256, 16, 16), // left  of room
        };

        public static LinkedHashSet<TransitionTile> getTiles(TransitionTile.Map map) {
            LinkedHashSet<TransitionTile> set = new LinkedHashSet<>();
            Rectangle rectangle;
            switch (map) { //16x14
                case CASTLE_THRONE_ROOM:
                    rectangle = CASTLE_THRONE_ROOM[0];//bottom
                    set.add(TransitionTile.create(rectangle.x, rectangle.y, rectangle.width, rectangle.height, new Point(CASTLE_MAIN_HALL[3].x, CASTLE_MAIN_HALL[3].y + 32), TransitionTile.Map.CASTLE_MAIN_HALL));
                    rectangle = CASTLE_THRONE_ROOM[1];//right
                    set.add(TransitionTile.create(rectangle.x, rectangle.y, rectangle.width, rectangle.height, new Point(CASTLE_BEDROOM[0].x + 32, CASTLE_BEDROOM[0].y), TransitionTile.Map.CASTLE_BEDROOM));

                    break;
                case CASTLE_BEDROOM:
                    rectangle = CASTLE_BEDROOM[0];//bottom
                    set.add(TransitionTile.create(rectangle.x, rectangle.y, rectangle.width, rectangle.height, new Point(CASTLE_THRONE_ROOM[1].x-32, CASTLE_THRONE_ROOM[1].y), TransitionTile.Map.CASTLE_THRONE_ROOM));
                    break;
                case CASTLE_BOSS_ROOM:
                    break;
                case CASTLE_BASEMENT_ENTRANCE:
                    rectangle = CASTLE_BASEMENT_ENTRANCE[0];//top
                    set.add(TransitionTile.create(rectangle.x, rectangle.y, rectangle.width, rectangle.height, new Point(CASTLE_BASEMENT_HALL[1].x, CASTLE_BASEMENT_HALL[1].y - 32), TransitionTile.Map.CASTLE_BASEMENT_HALL));
                    rectangle = CASTLE_BASEMENT_ENTRANCE[1];//borrom
                    set.add(TransitionTile.create(rectangle.x, rectangle.y, rectangle.width, rectangle.height, new Point(248+8, 16), TransitionTile.Map.CASTLE_BASEMENT));
                    break;
                case CASTLE_BASEMENT_HALL:
                    rectangle = CASTLE_BASEMENT_HALL[0];//top
                    set.add(TransitionTile.create(rectangle.x, rectangle.y, rectangle.width, rectangle.height, new Point(552, 592), TransitionTile.Map.CASTLE_BOSS_ROOM));
                    rectangle = CASTLE_BASEMENT_HALL[1];//bottom
                    set.add(TransitionTile.create(rectangle.x, rectangle.y, rectangle.width, rectangle.height, new Point(256+8, 16), TransitionTile.Map.CASTLE_BASEMENT_ENTRANCE));
                    break;
                case CASTLE_BASEMENT:
                    rectangle = CASTLE_BASEMENT[0];//left
                    set.add(TransitionTile.create(rectangle.x, rectangle.y, rectangle.width, rectangle.height, new Point(160+8, 96), TransitionTile.Map.CASTLE_MAIN_HALL));
                    rectangle = CASTLE_BASEMENT[1];//right
                    set.add(TransitionTile.create(rectangle.x, rectangle.y, rectangle.width, rectangle.height, new Point(800 + 8, 96), TransitionTile.Map.CASTLE_MAIN_HALL));
                    rectangle = CASTLE_BASEMENT[2];//mid
                    set.add(TransitionTile.create(rectangle.x, rectangle.y, rectangle.width, rectangle.height, new Point(256+8, 240), TransitionTile.Map.CASTLE_BASEMENT_ENTRANCE));
                    break;
                case CASTLE_HYRULE:
                    rectangle = HYRULE_CASTLE[0];//to gar
                    set.add(TransitionTile.create(rectangle.x, rectangle.y, rectangle.width, rectangle.height, new Point(1004, 100), TransitionTile.Map.CASTLE_GARDEN));
                    rectangle = HYRULE_CASTLE[1];
                    set.add(TransitionTile.create(rectangle.x, rectangle.y, rectangle.width, rectangle.height, new Point(488, 1040 - 32), TransitionTile.Map.CASTLE_MAIN_HALL));
                    break;
                case CASTLE_MAIN_HALL:
                    rectangle = CASTLE_MAIN_HALL[0];//left
                    set.add(TransitionTile.create(rectangle.x, rectangle.y, rectangle.width, rectangle.height, new Point(196, 64), TransitionTile.Map.CASTLE_BASEMENT));
                    rectangle = CASTLE_MAIN_HALL[1];//right
                    set.add(TransitionTile.create(rectangle.x, rectangle.y, rectangle.width, rectangle.height, new Point(896, 64), TransitionTile.Map.CASTLE_BASEMENT));
                    rectangle = CASTLE_MAIN_HALL[2];//bottom
                    set.add(TransitionTile.create(rectangle.x, rectangle.y, rectangle.width, rectangle.height, new Point(424, 64), TransitionTile.Map.CASTLE_HYRULE));
                    rectangle = CASTLE_MAIN_HALL[3];//top
                    set.add(TransitionTile.create(rectangle.x, rectangle.y, rectangle.width, rectangle.height, new Point(272, 688-16), TransitionTile.Map.CASTLE_THRONE_ROOM));
                    break;
                case NONE:
                    break;
                case CASTLE_GARDEN:
                    rectangle = HYRULE_GARDEN[0];
                    set.add(TransitionTile.create(rectangle.x, rectangle.y, rectangle.width, rectangle.height, new Point(424, 1104), TransitionTile.Map.CASTLE_HYRULE));
                    rectangle = HYRULE_GARDEN[1];
                    set.add(TransitionTile.create(rectangle.x, rectangle.y, rectangle.width, rectangle.height, new Point(824, 16), TransitionTile.Map.FOREST));
                    break;
                case FOREST:
                    rectangle = FOREST[0];
                    set.add(TransitionTile.create(rectangle.x, rectangle.y, rectangle.width, rectangle.height, new Point(HYRULE_GARDEN[1].x + HYRULE_GARDEN[1].width/2, HYRULE_GARDEN[1].y-32), TransitionTile.Map.CASTLE_GARDEN));
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

    public LinkedHashSet<TransitionTile> getTransitionTiles() {
        return transitionTiles.get(current);
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
