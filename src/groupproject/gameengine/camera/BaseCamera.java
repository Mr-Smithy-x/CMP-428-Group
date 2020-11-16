package groupproject.gameengine.camera;

import groupproject.gameengine.contracts.BoundingContract;
import groupproject.gameengine.models.BoundingBox;
import groupproject.gameengine.tile.TileMap;

public abstract class BaseCamera {

    protected double x, y;
    private double vx, vy;
    private double ay, av;
    protected double x_origin, y_origin;
    protected int scaling = 1;
    protected int gravity = 1;
    public static boolean DEBUG = true;


    public int getScaling() {
        return scaling;
    }

    public int getGravity() {
        return gravity;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public void setScaling(int scaling) {
        this.scaling = scaling;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void moveBy(double dx, double dy) {
        x += dx;
        y += dy;
    }


    public void setOrigin(BoundingContract e, TileMap map){
        float x = e.getX().floatValue();
        float y = e.getY().floatValue();

        //Commented out code would center the pixel onto the screen, but with this dynamic, it works much differently
        x_origin = x - (map.getWidth().floatValue() / 2);// + (e.getWidth().floatValue() / 2);
        y_origin = y - (map.getHeight().floatValue() / 2);// + (e.getHeight().floatValue() / 2);

        this.x = x_origin;
        this.y = y_origin;


        if(e instanceof BoundingBox){
            x_origin -= e.getWidth().doubleValue() / 2;
            y_origin -= e.getHeight().doubleValue() / 2;
        }
    }

    public void setOrigin(BoundingContract e, double screen_width, double screen_height) {
        float x = e.getX().floatValue();
        float y = e.getY().floatValue();

        //Commented out code would center the pixel onto the screen, but with this dynamic, it works much differently
        x_origin = x - (screen_width / 2);// + (e.getWidth().floatValue() / 2);
        y_origin = y - (screen_height / 2);// + (e.getHeight().floatValue() / 2);

        this.x = x_origin;
        this.y = y_origin;


        if(e instanceof BoundingBox){
            x_origin -= e.getWidth().doubleValue() / 2;
            y_origin -= e.getHeight().doubleValue() / 2;
        }

        // System.out.printf("(X: %s,Y: %s), ORIGIN: (x: %s, y: %s)", x, y, x_origin, y_origin);
    }

    public void setup(double x, double y) {
        this.x = x;
        this.y = y;
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

    public BaseCamera(double x_origin, double y_origin) {
        this.x_origin = x_origin;
        this.y_origin = y_origin;
    }

    public void move(double xamt, double yamt) {
        x_origin += xamt;
        y_origin += yamt;
    }

    public double getXOrigin() {
        return x_origin;
    }

    public void setXOrigin(float x_offset) {
        this.x_origin = x_offset;
    }

    public double getYOrigin() {
        return y_origin;
    }

    public void setYOrigin(float y_offset) {
        this.y_origin = y_offset;
    }


}