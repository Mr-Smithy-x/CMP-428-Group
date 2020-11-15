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
        this.setVelocity(
                this.getVelocityX().doubleValue() + this.getAccelerationX().doubleValue(),
                this.getVelocityY().doubleValue() + this.getAccelerationY().doubleValue()
        );
        if (this.isDraggable()) {
            this.setVelocity(
                    getVelocityX().doubleValue() * getDragX().doubleValue(),
                    getVelocityY().doubleValue() * getDragY().doubleValue()
            );
        }
        if (Math.abs(this.getVelocityX().doubleValue()) > 0.1) {
            this.setX(this.getX().doubleValue() + this.getVelocityX().doubleValue());
        }
        if (Math.abs(this.getVelocityY().doubleValue()) > 0.1) {
            this.setY(this.getY().doubleValue() + this.getVelocityY().doubleValue());
        }
    }


    default void bounceOff(Gravitation gravitational) {
        double dx = gravitational.getX().doubleValue() - getX().doubleValue();
        double dy = gravitational.getY().doubleValue() - getY().doubleValue();
        double mag = Math.sqrt(dx * dx + dy * dy);
        double ux = dx / mag; //in this case unit vector
        double uy = dy / mag;
        double tx = -uy; //tangent vector
        double ty = ux;
        double u = getVelocityX().doubleValue() * ux + getVelocityY().doubleValue() * uy;
        double t = getVelocityX().doubleValue() * tx + getVelocityY().doubleValue() * ty;
        double cu = gravitational.getVelocityX().doubleValue() * ux + gravitational.getVelocityY().doubleValue() * uy;
        double ct = gravitational.getVelocityX().doubleValue() * tx + gravitational.getVelocityY().doubleValue() * ty;
        setVelocity(.9 * (t * tx + cu * ux), .9 * (t * ty + cu * uy));
        gravitational.setVelocity(.9 * (ct * tx + u * ux), .9 * (ct * ty + u * uy));
    }

    default void bounceOffLine(BoundingContractLine line) {
        double d = line.distanceTo(getX().doubleValue(), getY().doubleValue()).doubleValue();
        double p = getRadius().doubleValue() - d;
        setWorld(
                getX().doubleValue() + 1.9 * (p * line.getNormalX().doubleValue()),
                getY().doubleValue() + 1.9 * (p * line.getNormalY().doubleValue())
        );
        double mag = 1.9 * (getVelocityX().doubleValue() * line.getNormalX().doubleValue() + getVelocityY().doubleValue() * line.getNormalY().doubleValue());
        double tx = mag * line.getNormalX().doubleValue();
        double ty = mag * line.getNormalY().doubleValue();
        setVelocity(getVelocityX().doubleValue() - tx, getVelocityY().doubleValue() - ty);
    }


}
