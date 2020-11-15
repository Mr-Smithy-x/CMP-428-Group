package groupproject.gameengine.models.base;

import groupproject.gameengine.contracts.Gravitation;

public abstract class GravitationObject extends MovableObject implements Gravitation {

    double velocity_x, velocity_y;
    double acceleration_x, acceleration_y;
    double drag_x, drag_y;


    @Override
    public void setVelocityX(Number velocity_x) {
        this.velocity_x = velocity_x.doubleValue();
    }

    @Override
    public void setVelocityY(Number velocity_y) {
        this.velocity_y = velocity_y.doubleValue();
    }

    @Override
    public void setAccelerationX(Number acceleration_x) {
        this.acceleration_x = acceleration_x.doubleValue();
    }

    @Override
    public void setAccelerationY(Number acceleration_y) {
        this.acceleration_y = acceleration_y.doubleValue();
    }

    @Override
    public void setDragX(Number drag_x) {
        this.drag_x = drag_x.doubleValue();
    }

    @Override
    public void setDragY(Number drag_y) {
        this.drag_y = drag_y.doubleValue();
    }

    @Override
    public Number getDragX() {
        return drag_x;
    }

    @Override
    public Number getDragY() {
        return drag_y;
    }

    @Override
    public Number getVelocityX() {
        return velocity_x;
    }

    @Override
    public Number getVelocityY() {
        return velocity_y;
    }

    @Override
    public Number getAccelerationX() {
        return acceleration_x;
    }

    @Override
    public Number getAccelerationY() {
        return acceleration_y;
    }
}
