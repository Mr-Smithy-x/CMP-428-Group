package groupproject.gameengine.contracts;

public interface CollisionDetection extends Movable {

    default void pushes(Movable contract) {
        double dx = getX().doubleValue() - contract.getX().doubleValue();
        double dy = getY().doubleValue() - contract.getY().doubleValue();
        double d = Math.sqrt(dx * dx + dy * dy);
        double ux = dx / d;
        double uy = dy / d;
        double ri = getRadius().doubleValue() + contract.getRadius().doubleValue();
        double p = ri - d;
        this.setWorld(
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

}
