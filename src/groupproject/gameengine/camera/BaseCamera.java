package groupproject.gameengine.camera;

import groupproject.gameengine.GamePanel;
import groupproject.gameengine.contracts.BoundingContract;
import groupproject.gameengine.models.BoundingBox;
import groupproject.gameengine.tile.TileMap;

import java.awt.*;

public abstract class BaseCamera  {

    protected double x, y;
    protected double vx, vy;
    protected double ay, av;
    protected double x_origin, y_origin;
    protected int scaling = 1;
    protected int gravity = 1;
    public static boolean DEBUG = true;

    //region Setters
    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public void setScaling(int scaling) {
        this.scaling = scaling;
    }

    public void setOrigin(BoundingContract e, TileMap map) {
        double x = e.getX().doubleValue();
        double y = e.getY().doubleValue();

        //Commented out code would center the pixel onto the screen, but with this dynamic, it works much differently
        this.x_origin = x - (map.getWidth().doubleValue() / 2);// + (e.getWidth().doubleValue() / 2);
        this.y_origin = y - (map.getHeight().doubleValue() / 2);// + (e.getHeight().doubleValue() / 2);

        this.x = x_origin;
        this.y = y_origin;


        if (e instanceof BoundingBox) {
            x_origin -= e.getWidth().doubleValue() / 2;
            y_origin -= e.getHeight().doubleValue() / 2;
        }
    }

    public void setOrigin(BoundingContract e, double screen_width, double screen_height) {
        double x = e.getX().doubleValue();
        double y = e.getY().doubleValue();

        //Commented out code would center the pixel onto the screen, but with this dynamic, it works much differently
        x_origin = x - (screen_width / 2);// + (e.getWidth().doubleValue() / 2);
        y_origin = y - (screen_height / 2);// + (e.getHeight().doubleValue() / 2);

        this.x = x_origin;
        this.y = y_origin;


        if (e instanceof BoundingBox) {
            x_origin -= e.getWidth().doubleValue() / 2;
            y_origin -= e.getHeight().doubleValue() / 2;
        }
    }

    public void setOffsetFromOrigin(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setXOrigin(double x_offset) {
        this.x_origin = x_offset;
    }

    public void setYOrigin(double y_offset) {
        this.y_origin = y_offset;
    }
    //endregion

    //region Getters
    public double getXOrigin() {
        return x_origin;
    }

    public double getYOrigin() {
        return y_origin;
    }

    public int getScaling() {
        return scaling;
    }

    public int getGravity() {
        return gravity;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    //endregion


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
        x_origin += xamt;
        y_origin += yamt;
    }

    public BaseCamera(double x_origin, double y_origin) {
        this.x_origin = x_origin;
        this.y_origin = y_origin;
    }


    public void render(Graphics g,  Image frame) {
        g.setColor(Color.pink);
        int x = (frame.getWidth(null) / 2);
        int y = (frame.getHeight(null) / 2);
        g.drawLine(x - 5, y, x + 5, y);
        g.drawLine(x, y - 5, x, y + 5);
    }

    public void render(Graphics g,  Component frame) {
        g.setColor(Color.pink);
        int x = (frame.getWidth() / 2);
        int y = (frame.getHeight() / 2);
        g.drawLine(x - 5, y, x + 5, y);
        g.drawLine(x, y - 5, x, y + 5);
    }

}