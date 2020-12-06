package groupproject.gameengine.tile;

import groupproject.gameengine.camera.GlobalCamera;
import groupproject.gameengine.contracts.CameraContract;
import groupproject.gameengine.contracts.Renderable;
import groupproject.gameengine.models.BoundingBox;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile implements Renderable, CameraContract {
    private final int width;
    private final int height;
    private final BufferedImage tileImage;
    private int x;
    private int y;
    private BoundingBox boundsRect;
    private boolean collisionEnabled = false;
    private boolean isObject = false;

    public Tile(BufferedImage image) {
        this.tileImage = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(tileImage,
                getCameraOffsetX(GlobalCamera.getInstance()).intValue(),
                getCameraOffsetY(GlobalCamera.getInstance()).intValue(),
                null);
    }

    public void drawBoundsRect(Graphics g) {
        if (collisionEnabled) g.setColor(Color.red);
        else g.setColor(Color.green);
        boundsRect.render(g);
    }

    @Override
    public Number getX() {
        return x;
    }

    @Override
    public void setX(Number x) {
        this.x = x.intValue();
    }

    @Override
    public Number getY() {
        return y;
    }

    @Override
    public void setY(Number y) {
        this.y = y.intValue();
    }

    @Override
    public Number getWidth() {
        return width;
    }

    @Override
    public void setWidth(Number width) {
        /*
          Not needed.
         */
    }

    @Override
    public Number getHeight() {
        return height;
    }

    @Override
    public void setHeight(Number height) {
        /*
          Not needed.
         */
    }

    public boolean isCollisionEnabled() {
        return collisionEnabled;
    }

    public void setCollisionEnabled(boolean collisionEnabled) {
        this.collisionEnabled = collisionEnabled;
    }

    public boolean isObject() {
        return isObject;
    }

    public void setObject(boolean object) {
        isObject = object;
    }

    public BufferedImage getTileImage() {
        return tileImage;
    }

    public BoundingBox getBoundsRect() {
        return boundsRect;
    }

    public void initBoundsRect() {
        this.boundsRect = new BoundingBox(x, y, width, height);
    }
}