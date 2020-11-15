package groupproject.gameengine.models.base;

import groupproject.gameengine.contracts.Movable;

public abstract class MovableObject implements Movable {

    protected double x, y;
    protected double width, height;
    protected int world_angle;


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
    public Number getSinAngle() {
        return sin[world_angle];
    }

    @Override
    public Number getCosAngle() {
        return cos[world_angle];
    }

    @Override
    public int getWorldAngle() {
        return world_angle;
    }

    @Override
    public void setWorldAngle(int world_angle) {
        this.world_angle = world_angle;
    }

    @Override
    public void setX(Number x) {
        this.x = x.doubleValue();
    }

    @Override
    public void setY(Number y) {
        this.y = y.doubleValue();
    }

    @Override
    public void setWidth(Number width) {
        this.width = width.intValue();
    }

    @Override
    public void setHeight(Number height) {
        this.height = height.intValue();
    }
}
