package groupproject.gameengine.camera;

import groupproject.gameengine.contracts.BoundingContract;
import groupproject.gameengine.models.BoundingBox;
import groupproject.gameengine.tile.TileMap;

import java.awt.*;

public abstract class BaseCamera {

    public static boolean DEBUG = true;
    protected double x, y;
    protected double vx, vy;
    protected double ay, av;
    protected double xOrigin, yOrigin;
    protected int scaling = 1;
    protected int gravity = 1;

    public BaseCamera(double xOrigin, double yOrigin) {
        this.xOrigin = xOrigin;
        this.yOrigin = yOrigin;
    }

    public void setOrigin(BoundingContract e, TileMap map) {
        double x = e.getX().doubleValue();
        double y = e.getY().doubleValue();

        //Commented out code would center the pixel onto the screen, but with this dynamic, it works much differently
        this.xOrigin = x - (map.getWidth().doubleValue() / 2);// + (e.getWidth().doubleValue() / 2);
        this.yOrigin = y - (map.getHeight().doubleValue() / 2);// + (e.getHeight().doubleValue() / 2);

        this.x = xOrigin;
        this.y = yOrigin;


        if (e instanceof BoundingBox) {
            xOrigin -= e.getWidth().doubleValue() / 2;
            yOrigin -= e.getHeight().doubleValue() / 2;
        }
    }

    public void setOrigin(BoundingContract e, double screen_width, double screen_height) {
        double x = e.getX().doubleValue();
        double y = e.getY().doubleValue();

        //Commented out code would center the pixel onto the screen, but with this dynamic, it works much differently
        xOrigin = x - (screen_width / 2);// + (e.getWidth().doubleValue() / 2);
        yOrigin = y - (screen_height / 2);// + (e.getHeight().doubleValue() / 2);

        this.x = xOrigin;
        this.y = yOrigin;


        if (e instanceof BoundingBox) {
            xOrigin -= e.getWidth().doubleValue() / 2;
            yOrigin -= e.getHeight().doubleValue() / 2;
        }
    }

    public void setOffsetFromOrigin(double x, double y) {
        this.x = x;
        this.y = y;
    }

    //region Getters
    public double getXOrigin() {
        return xOrigin;
    }

    public void setXOrigin(double x_offset) {
        this.xOrigin = x_offset;
    }

    public double getYOrigin() {
        return yOrigin;
    }
    //endregion

    public void setYOrigin(double y_offset) {
        this.yOrigin = y_offset;
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

    //region Setters
    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public double getX() {
        return x;
    }
    //endregion

    public double getY() {
        return y;
    }

    //region Helpers
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
    //endregion

    public void moveOrigin(double xamt, double yamt) {
        xOrigin += xamt;
        yOrigin += yamt;
    }

    public void render(Graphics g, Component frame) {
        render(g, frame.getWidth(), frame.getHeight());
    }

    public void render(Graphics g, int width, int height) {
        g.setColor(Color.pink);
        int x = (width / 2);
        int y = (height / 2);
        g.drawLine(x - 5, y, x + 5, y);
        g.drawLine(x, y - 5, x, y + 5);
    }
}