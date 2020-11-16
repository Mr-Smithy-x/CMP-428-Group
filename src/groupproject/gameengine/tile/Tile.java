package groupproject.gameengine.tile;

import groupproject.gameengine.camera.GlobalCamera;
import groupproject.gameengine.contracts.CameraContract;
import groupproject.gameengine.contracts.Renderable;
import groupproject.gameengine.models.BoundingBox;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile implements Renderable, CameraContract {
    private int x;
    private int y;
    private BoundingBox boundsRect;
    private int width;
    private int height;
    private boolean collisionEnabled = false;
    private boolean isObject = false;
    private final BufferedImage tileImage;

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
        if(collisionEnabled) g.setColor(Color.red);
        else g.setColor(Color.green);
        boundsRect.render(g);
    }

    @Override
    public Number getX() {
        return x;
    }

    @Override
    public Number getY() {
        return y;
    }

    @Override
    public Number getWidth() {
        return width;
    }

    @Override
    public Number getHeight() {
        return height;
    }

    @Override
    public void setWidth(Number width) {
        this.width = width.intValue();
    }

    @Override
    public void setHeight(Number height) {
        this.height = height.intValue();
    }

    @Override
    public void setX(Number x) {
        this.x = x.intValue();
    }

    @Override
    public void setY(Number y) {
        this.y = y.intValue();
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
