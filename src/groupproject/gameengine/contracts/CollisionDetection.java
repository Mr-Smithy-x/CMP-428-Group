package groupproject.gameengine.contracts;

public interface CollisionDetection extends Gravitation {

    /**
     * Push objects, use this so that we can push objects to the side.
     * @param contract
     */
    default void pushes(Movable contract) {
        double dx = getX().doubleValue() - contract.getX().doubleValue();
        double dy = getY().doubleValue() - contract.getY().doubleValue();
        double d = Math.sqrt(dx * dx + dy * dy);
        double ux = dx / d;
        double uy = dy / d;
        double ri = getRadius().doubleValue() + contract.getRadius().doubleValue();
        double p = ri - d;
        setWorld(
                getX().doubleValue() + ux * p / 2,
                getY().doubleValue() + uy * p / 2
        );
        double set_pos_x = contract.getX().doubleValue() - (ux * p / 2);
        double set_pos_y = contract.getY().doubleValue() - (uy * p / 2);
        contract.setWorld(set_pos_x, set_pos_y);
    }

    /**
     * Does the object overlaps?
     * @param movable
     * @return
     */
    default boolean overlaps(Movable movable) {
        double dx = getX().doubleValue() - movable.getX().doubleValue();
        double dy = getY().doubleValue() - movable.getY().doubleValue();
        double d2 = dx * dx + dy * dy;
        double ri = getRadius().doubleValue() + movable.getRadius().doubleValue();
        return d2 <= ri * ri;
    }

    /**
     * How far is the object from you?
     * @param movable
     * @return
     */
    default double distanceBetween(Movable movable) {
        double dx = movable.getX().doubleValue() - getX().doubleValue();
        double dy = movable.getY().doubleValue() - getY().doubleValue();
        return Math.sqrt(dx * dx + dy + dy);
    }

    /**
     * Maybe we migh t want o bound off a tile?
     * @param gravitational
     */
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



}
