package groupproject.gameengine.contracts;

public interface Gravitation extends Movable {

    default void setVelocity(Number velocity_x, Number velocity_y) {
        setVelocityX(velocity_x);
        setVelocityY(velocity_y);
    }

    default void setAcceleration(Number accelerate_x, Number accelerate_y) {
        setAccelerationX(accelerate_x);
        setAccelerationY(accelerate_y);
    }

    default void setDrag(Number drag_x, Number drag_y) {
        setDragX(drag_x);
        setDragY(drag_y);
    }

    void setVelocityX(Number velocity_x);

    void setVelocityY(Number velocity_y);

    void setAccelerationX(Number acceleration_x);

    void setAccelerationY(Number acceleration_y);

    void setDragX(Number drag_x);

    void setDragY(Number drag_y);

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
