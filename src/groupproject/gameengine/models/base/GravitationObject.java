package groupproject.gameengine.models.base;

import groupproject.gameengine.contracts.Gravitation;

public abstract class GravitationObject extends MovableObject implements Gravitation {

    double velocityX;
    double velocityY;
    double accelerationX;
    double accelerationY;
    double dragX;
    double dragY;

    @Override
    public Number getDragX() {
        return dragX;
    }

    @Override
    public void setDragX(Number dragX) {
        this.dragX = dragX.doubleValue();
    }

    @Override
    public Number getDragY() {
        return dragY;
    }

    @Override
    public void setDragY(Number dragY) {
        this.dragY = dragY.doubleValue();
    }

    @Override
    public Number getVelocityX() {
        return velocityX;
    }

    @Override
    public void setVelocityX(Number velocityX) {
        this.velocityX = velocityX.doubleValue();
    }

    @Override
    public Number getVelocityY() {
        return velocityY;
    }

    @Override
    public void setVelocityY(Number velocityY) {
        this.velocityY = velocityY.doubleValue();
    }

    @Override
    public Number getAccelerationX() {
        return accelerationX;
    }

    @Override
    public void setAccelerationX(Number accelerationX) {
        this.accelerationX = accelerationX.doubleValue();
    }

    @Override
    public Number getAccelerationY() {
        return accelerationY;
    }

    @Override
    public void setAccelerationY(Number accelerationY) {
        this.accelerationY = accelerationY.doubleValue();
    }
}
