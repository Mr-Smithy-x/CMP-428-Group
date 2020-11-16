package groupproject.gameengine.contracts;

public interface Gravitation extends Movable {

    default void setVelocity(Number velocityX, Number velocityY) {
        setVelocityX(velocityX);
        setVelocityY(velocityY);
    }

    default void setAcceleration(Number accelerateX, Number accelerateY) {
        setAccelerationX(accelerateX);
        setAccelerationY(accelerateY);
    }

    default void setDrag(Number dragX, Number dragY) {
        setDragX(dragX);
        setDragY(dragY);
    }

    void setVelocityX(Number velocityX);

    void setVelocityY(Number velocityY);

    void setAccelerationX(Number accelerationX);

    void setAccelerationY(Number accelerationY);

    void setDragX(Number dragX);

    void setDragY(Number dragY);

    Number getDragX();

    Number getDragY();

    Number getVelocityX();

    Number getVelocityY();

    Number getAccelerationX();

    Number getAccelerationY();

    default boolean isDraggable() {
        return false;
    }

    default void gravitate() {
        setVelocity(
                getVelocityX().doubleValue() + getAccelerationX().doubleValue(),
                getVelocityY().doubleValue() + getAccelerationY().doubleValue()
        );
        if (isDraggable()) {
            setVelocity(
                    getVelocityX().doubleValue() * getDragX().doubleValue(),
                    getVelocityY().doubleValue() * getDragY().doubleValue()
            );
        }
        if (Math.abs(getVelocityX().doubleValue()) > 0.1) {
            setX(getX().doubleValue() + getVelocityX().doubleValue());
        }
        if (Math.abs(getVelocityY().doubleValue()) > 0.1) {
            setY(getY().doubleValue() + getVelocityY().doubleValue());
        }
    }


}
