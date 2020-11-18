package groupproject.gameengine.contracts;

public interface Gravitation extends Movable {

    default void setAcceleration(Number accelerateX, Number accelerateY) {
        setAccelerationX(accelerateX);
        setAccelerationY(accelerateY);
    }

    default void setDrag(Number dragX, Number dragY) {
        setDragX(dragX);
        setDragY(dragY);
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

    default void setVelocity(Number velocityX, Number velocityY) {
        setVelocityX(velocityX);
        setVelocityY(velocityY);
    }

    Number getVelocityX();

    void setVelocityX(Number velocityX);

    Number getAccelerationX();

    void setAccelerationX(Number accelerationX);

    Number getVelocityY();

    void setVelocityY(Number velocityY);

    Number getAccelerationY();

    void setAccelerationY(Number accelerationY);

    default boolean isDraggable() {
        return false;
    }

    Number getDragX();

    void setDragX(Number dragX);

    Number getDragY();

    void setDragY(Number dragY);
}
