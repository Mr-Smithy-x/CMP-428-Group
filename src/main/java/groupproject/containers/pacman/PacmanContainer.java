package groupproject.containers.pacman;

import groupproject.containers.pacman.models.Ghost;
import groupproject.containers.pacman.models.Pacman;
import groupproject.gameengine.GameContainer;
import groupproject.gameengine.camera.GlobalCamera;
import groupproject.gameengine.sprite.Sprite;
import groupproject.gameengine.tile.Tile;
import groupproject.gameengine.tile.TileMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class PacmanContainer extends GameContainer {

    private static final int WINDOW_HEIGHT = 768;
    private static final int WINDOW_WIDTH = 672;
    private final Pacman player = new Pacman(3, 11);
    private final Ghost redGhost = new Ghost(80, 11, "red", 100);
    private final List<TileMap> maps = new ArrayList<>();

    protected PacmanContainer(JFrame container, Canvas canvas) {
        super(container, canvas);
    }

    public static GameContainer frame() {
        JFrame frame = make("Pacman Test Game", WINDOW_WIDTH, WINDOW_HEIGHT);
        Canvas canvas = make(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.add(canvas);
        frame.pack();
        return new PacmanContainer(frame, canvas);
    }

    @Override
    protected void onPlay() {
        Sprite.Pose pose = player.getSpritePose();
        boolean allowMove = false;

        if (pressedKey[KeyEvent.VK_W] && !isSpriteCollidingWithMap(player, Sprite.Pose.UP)) {
            pose = Sprite.Pose.UP;
            allowMove = true;
        }

        if (pressedKey[KeyEvent.VK_S] && !isSpriteCollidingWithMap(player, Sprite.Pose.DOWN)) {
            pose = Sprite.Pose.DOWN;
            allowMove = true;
        }

        if (pressedKey[KeyEvent.VK_A] && !isSpriteCollidingWithMap(player, Sprite.Pose.LEFT)) {
            pose = Sprite.Pose.LEFT;
            allowMove = true;
        }

        if (pressedKey[KeyEvent.VK_D] && !isSpriteCollidingWithMap(player, Sprite.Pose.RIGHT)) {
            pose = Sprite.Pose.RIGHT;
            allowMove = true;
        }

        if (allowMove || !isSpriteCollidingWithMap(player, pose)) {
            player.setSpritePose(pose);
            player.move();
            //Its important to note that when using the camera with tilemap call this method
            //do not use getWidth or getHeight of the component window when using it with the tilemap
            //player.getBounds is accurate in terms of centering the square
            GlobalCamera.getInstance().setOrigin(player.getBounds(), maps.get(0));
        }
    }

    // Basic collision detection based on the 8 map tiles surrounding a sprite.
    private boolean isSpriteCollidingWithMap(Sprite sprite, Sprite.Pose pose) {
        boolean isColliding = false;
        int velocity = sprite.getVelocity();

        switch (pose) {
            case RIGHT:
                for (Tile tile : maps.get(0).getSurroundingTiles(
                        sprite.getBounds().getX().intValue(),
                        sprite.getBounds().getY().intValue(),
                        Sprite.Pose.RIGHT))
                    if (tile.isCollisionEnabled() && sprite.getBounds().willOverlap(tile.getBoundsRect(), velocity, 0))
                        isColliding = true;
                break;
            case LEFT:
                for (Tile tile : maps.get(0).getSurroundingTiles(
                        sprite.getBounds().getX().intValue(),
                        sprite.getBounds().getY().intValue(),
                        Sprite.Pose.LEFT))
                    if (tile.isCollisionEnabled() && sprite.getBounds().willOverlap(tile.getBoundsRect(), -velocity, 0))
                        isColliding = true;
                break;
            case UP:
                for (Tile tile : maps.get(0).getSurroundingTiles(
                        sprite.getBounds().getX().intValue(),
                        sprite.getBounds().getY().intValue(),
                        Sprite.Pose.UP))
                    if (tile.isCollisionEnabled() && sprite.getBounds().willOverlap(tile.getBoundsRect(), 0, -velocity))
                        isColliding = true;
                break;
            case DOWN:
                for (Tile tile : maps.get(0).getSurroundingTiles(
                        sprite.getBounds().getX().intValue(),
                        sprite.getBounds().getY().intValue(),
                        Sprite.Pose.DOWN))
                    if (tile.isCollisionEnabled() && sprite.getBounds().willOverlap(tile.getBoundsRect(), 0, velocity))
                        isColliding = true;
                break;
            default:
                break;
        }

        return isColliding;
    }

    @Override
    protected void onPaint(Graphics g) {
        if (!maps.isEmpty()) {
            maps.get(0).render(g);
            for (Tile tile : maps.get(0).getSurroundingTiles(
                    player.getBounds().getX().intValue(),
                    player.getBounds().getY().intValue(), Sprite.Pose.ALL))
                tile.drawBoundsRect(g);
        }
        player.render(g);
        redGhost.render(g);
        GlobalCamera.getInstance().render(g, getContainer());
    }

    @Override
    protected void onInitialize() {
        try {
            maps.add(loadTileMap("pacman.tilemap"));
            maps.get(0).initializeMap();
            GlobalCamera.getInstance().setOrigin(player.getBounds(), maps.get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPausePaint(Graphics g) {
        g.setColor(new Color(1f, 1f, 1f, 0.3f));
        g.fillRect(0, 0, getWidth(), getHeight());
        String paused = "Paused";
        int width = g.getFontMetrics().stringWidth(paused) / 2;
        g.setColor(new Color(0f, 0f, 0f, 0.7f));
        g.drawString(paused, getWidth() / 2 - width, getHeight() / 2 + g.getFontMetrics().getHeight() / 2);
    }

    /**
     * Will scale the image up or down to the current map
     *
     * @return
     */
    @Override
    public int getWidth() {
        return maps.get(0).getWidth().intValue();
    }

    /**
     * Will scale the image up or down to the current map
     *
     * @return
     */
    @Override
    public int getHeight() {
        return maps.get(0).getHeight().intValue();
    }
}
