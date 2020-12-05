package groupproject.gameengine.camera;

import groupproject.gameengine.contracts.BoundingContract;
import groupproject.gameengine.models.BoundingBox;
import groupproject.gameengine.tile.TileMap;

import java.awt.*;

public abstract class BaseCamera {

    public static final boolean DEBUG = true;
    protected double x;
    protected double y;
    protected double vx;
    protected double vy;
    protected double ay;
    protected double av;
    protected double xOrigin;
    protected double yOrigin;
    protected int scaling = 1;
    protected int gravity = 1;

    protected BaseCamera(double xOrigin, double yOrigin) {
        this.xOrigin = xOrigin;
        this.yOrigin = yOrigin;
    }

    public void setOrigin(BoundingContract e, TileMap map) {
        // Commented out code would center the pixel onto the screen, but with this dynamic, it works much differently
        this.xOrigin = e.getX().doubleValue() - (map.getWidth().doubleValue() / 2);
        this.yOrigin = e.getY().doubleValue() - (map.getHeight().doubleValue() / 2);

        this.x = xOrigin;
        this.y = yOrigin;

        if (e instanceof BoundingBox) {
            xOrigin -= e.getWidth().doubleValue() / 2;
            yOrigin -= e.getHeight().doubleValue() / 2;
        }
    }

    // Bounds onto the screen,
    public void setOrigin(BoundingContract e, double screenWidth, double screenHeight) {
        xOrigin = e.getX().doubleValue() - (screenWidth / 2) - e.getWidth().doubleValue() / 2;
        yOrigin = e.getY().doubleValue() - (screenHeight / 2) - e.getHeight().doubleValue() / 2;
        this.x = xOrigin + e.getWidth().doubleValue();
        this.y = yOrigin + e.getHeight().doubleValue();
    }

    public void setOffsetFromOrigin(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getXOrigin() {
        return xOrigin;
    }

    public void setXOrigin(double xOffset) {
        this.xOrigin = xOffset;
    }

    public double getYOrigin() {
        return yOrigin;
    }

    public void setYOrigin(double yOffset) {
        this.yOrigin = yOffset;
    }

    public int getScaling() {
        return scaling;
    }

    public void setScaling(int scaling) {
        this.scaling = scaling;
    }

    public int getGravity() {
        return gravity;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void moveBy(double dx, double dy) {
        x += dx;
        y += dy;
    }

    public void moveUp(double dist) {
        y -= dist;
    }

    public void moveDown(double dist) {
        y += dist;
    }

    public void moveLeft(double dist) {
        x -= dist;
    }

    public void moveRight(double dist) {
        x += dist;
    }

    public void moveOrigin(double xamt, double yamt) {
        xOrigin += xamt;
        yOrigin += yamt;
    }

    public void render(Graphics g, Component frame) {
        render(g, frame.getWidth(), frame.getHeight());
    }

    public void render(Graphics g, int width, int height) {
        g.setColor(Color.pink);
        int newX = (width / 2);
        int newY = (height / 2);
        g.drawLine(newX - 5, newY, newX + 5, newY);
        g.drawLine(newX, newY - 5, newX, newY + 5);
    }
}
