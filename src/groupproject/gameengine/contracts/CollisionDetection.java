package groupproject.gameengine.contracts;

public interface CollisionDetection extends Gravitation {

    /**
     * Push objects, use this so that we can push objects to the side.
     *
     * @param contract - The object being pushed.
     */
    default void pushes(Movable contract) {
        double dx = getX().doubleValue() - contract.getX().doubleValue();
        double dy = getY().doubleValue() - contract.getY().doubleValue();
        double d = Math.sqrt(dx * dx + dy * dy);
        double ux = dx / d;
        double uy = dy / d;
        double ri = getWidth().doubleValue() / 2 + contract.getWidth().doubleValue() / 2;
        double p = ri - d;
        setWorld(
                getX().doubleValue() + ux * p / 2,
                getY().doubleValue() + uy * p / 2
        );
        double setPosX = contract.getX().doubleValue() - (ux * p / 2);
        double setPosY = contract.getY().doubleValue() - (uy * p / 2);
        contract.setWorld(setPosX, setPosY);
    }


    default boolean isOverlapping(BoundingContract box) {
        return ((box.getX().doubleValue() + box.getWidth().doubleValue() >= getX().doubleValue()) &&
                (getX().doubleValue() + getWidth().doubleValue() >= box.getX().doubleValue()) &&
                (box.getY().doubleValue() + box.getHeight().doubleValue() >= getY().doubleValue()) &&
                (getY().doubleValue() + getHeight().doubleValue() >= box.getY().doubleValue()));
    }


    default boolean willOverlap(BoundingContract r, int dx, int dy) {
        return !(getX().intValue() + dx > r.getDiagonalX().intValue() || getY().intValue() + dy > r.getDiagonalY().intValue() ||
                r.getX().intValue() > getDiagonalX().intValue() + dx || r.getY().intValue() > getDiagonalY().intValue() + dy);
    }


    /**
     * How far is the object from you?
     *
     * @param movable - The object being checked against.
     * @return The distance between objects.
     */
    default double distanceBetween(Movable movable) {
        double dx = movable.getX().doubleValue() - getX().doubleValue();
        double dy = movable.getY().doubleValue() - getY().doubleValue();
        return Math.sqrt(dx * dx + dy * dy);
    }
}
