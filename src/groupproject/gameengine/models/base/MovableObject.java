package groupproject.gameengine.models.base;

import groupproject.gameengine.contracts.Movable;

public abstract class MovableObject implements Movable {

    protected double x, y;
    protected double width, height;
    protected int worldAngle;


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
        return sin[worldAngle];
    }

    @Override
    public Number getCosAngle() {
        return cos[worldAngle];
    }

    @Override
    public int getWorldAngle() {
        return worldAngle;
    }

    @Override
    public void setWorldAngle(int worldAngle) {
        this.worldAngle = worldAngle;
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
