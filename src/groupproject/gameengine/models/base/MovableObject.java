package groupproject.gameengine.models.base;

import groupproject.gameengine.contracts.Movable;

public abstract class MovableObject implements Movable {

    protected double x;
    protected double y;
    protected double width;
    protected double height;

    @Override
    public Number getX() {
        return x;
    }

    @Override
    public void setX(Number x) {
        this.x = x.doubleValue();
    }

    @Override
    public Number getY() {
        return y;
    }

    @Override
    public void setY(Number y) {
        this.y = y.doubleValue();
    }

    @Override
    public Number getWidth() {
        return width;
    }

    @Override
    public void setWidth(Number width) {
        this.width = width.intValue();
    }

    @Override
    public Number getHeight() {
        return height;
    }

    @Override
    public void setHeight(Number height) {
        this.height = height.intValue();
    }
}
