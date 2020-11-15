package groupproject.gameengine.helpers;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SpriteHelper {


    public static Image test(BufferedImage spriteSheet, int x, int y) {
        Image desired = spriteSheet.getSubimage(x, y, 30 * 3, 30 * 3);
        return desired;

    }

    /*
    /**
     * This function gets the position of the tile, scaled off bounds of the screen or in screen
     * but in general somewhere on the map.
     *
     * @param tileSet
     * @param sprite
     * @return

    public static Tile getCurrentTile(TileMap tileSet, Movable sprite) {
        Point currentPoint = getCurrentPoint(tileSet, sprite);
        return tileSet.get(currentPoint);
    }

    public static Point getCurrentPoint(TileMap tileSet, Movable sprite) {
        int sprite_position_x = sprite.getX().intValue() / GlobalCamera.getInstance().getScaling();
        int sprite_position_y = sprite.getY().intValue() / GlobalCamera.getInstance().getScaling();
        int scaled_tile_width = tileSet.getTileWidth(); //Size of the tile, now scaled
        int scaled_tile_height = tileSet.getTileHeight();
        int scaled_tile_position_x = sprite_position_x - (sprite_position_x % scaled_tile_width);
        int scaled_tile_position_y = sprite_position_y - (sprite_position_y % scaled_tile_height);
        long point = Point.toLong(scaled_tile_position_x, scaled_tile_position_y);
        if (Camera.DEBUG) {
            System.out.printf("Sprite: %s -  WORLD (%s, %s). Scaled: %s x (%s, %s)\n",
                    sprite.getClass().getCanonicalName(),
                    sprite_position_x, sprite_position_y,
                    GlobalCamera.getInstance().getScaling(),
                    (sprite_position_x),
                    (sprite_position_y));
            System.out.printf("Real Pos: (%s, %s)\n\n", scaled_tile_position_x, scaled_tile_position_y);
        }
        return Point.fromLong(point);
    }
    */

}
