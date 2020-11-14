package groupproject.gameengine.tile;

import groupproject.gameengine.Rect;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile {
    private int x;
    private int y;
    private Rect boundsRect;
    private final int width;
    private final int height;
    private boolean collisionEnabled = false;
    private boolean isObject = false;
    private final BufferedImage tileImage;

    public Tile(BufferedImage image) {
        this.tileImage = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    public void draw(Graphics g) {
        g.drawImage(tileImage, x, y, null);
    }

    public void drawBoundsRect(Graphics g) {
        if(collisionEnabled) g.setColor(Color.red);
        else g.setColor(Color.green);
        boundsRect.draw(g);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
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

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Rect getBoundsRect() {
        return boundsRect;
    }

    public void initBoundsRect() {
        this.boundsRect = new Rect(x, y, width, height);
    }
}
