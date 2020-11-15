package groupproject.gameengine.camera;

import groupproject.gameengine.contracts.Movable;
import groupproject.gameengine.models.BoundingBox;

public abstract class BaseCamera {

    protected float x, y;
    private float vx, vy;
    private float ay, av;
    protected float x_origin, y_origin;
    protected int scaling = 1;
    protected int gravity = 1;


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

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void moveBy(double dx, double dy) {
        x += dx;
        y += dy;
    }


    public void setOrigin(Movable e, float screen_width, float screen_height) {
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

    public void setup(float x, float y) {
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

    public BaseCamera(float x_origin, float y_origin) {
        this.x_origin = x_origin;
        this.y_origin = y_origin;
    }

    public void move(float xamt, float yamt) {
        x_origin += xamt;
        y_origin += yamt;
    }

    public float getXOrigin() {
        return x_origin;
    }

    public void setXOrigin(float x_offset) {
        this.x_origin = x_offset;
    }

    public float getYOrigin() {
        return y_origin;
    }

    public void setYOrigin(float y_offset) {
        this.y_origin = y_offset;
    }


}