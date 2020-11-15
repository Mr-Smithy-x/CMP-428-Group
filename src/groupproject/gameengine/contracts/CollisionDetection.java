package groupproject.gameengine.contracts;

public interface CollisionDetection extends Gravitation {

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

    default boolean overlaps(Movable movable) {
        double dx = getX().doubleValue() - movable.getX().doubleValue();
        double dy = getY().doubleValue() - movable.getY().doubleValue();
        double d2 = dx * dx + dy * dy;
        double ri = getRadius().doubleValue() + movable.getRadius().doubleValue();
        return d2 <= ri * ri;
    }

    default double distanceBetween(Movable movable) {
        double dx = movable.getX().doubleValue() - getX().doubleValue();
        double dy = movable.getY().doubleValue() - getY().doubleValue();
        return Math.sqrt(dx * dx + dy + dy);
    }

    default boolean overlaps(BoundingContractLine line) {
        double distance = line.distanceTo(getX().doubleValue(), getY().doubleValue()).doubleValue();
        double v = getRadius().doubleValue();
        //System.out.printf("DISTANCE: %s, Radius: %s\n", distance, v);
        return distance * distance < v * v;
    }

    default void pushedBackBy(BoundingContractLine line) {
        double distance = line.distanceTo(getX().doubleValue(), getY().doubleValue()).doubleValue();
        double p = getRadius().doubleValue() - distance;
        setWorld(
                getX().doubleValue() + p * line.getNormalX().doubleValue(),
                getY().doubleValue() + p * line.getNormalY().doubleValue()
        );
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
        pushedBackBy(line); //fixes issue overlaping
    }


}
