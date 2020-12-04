package groupproject.gameengine.contracts;

public interface Movable extends BoundingContract {
    default void moveBy(Number dx, Number dy) {
        this.setWorld(this.getX().doubleValue() + dx.doubleValue(),
                this.getY().doubleValue() + dy.doubleValue());
    }

    default void moveLeft(Number speed) {
        moveBy(-speed.doubleValue(), 0);
    }

    default void moveRight(Number speed) {
        moveBy(speed.doubleValue(), 0);
    }

    default void moveDown(Number speed) {
        moveBy(0, speed.doubleValue());
    }

    default void moveUp(Number speed) {
        moveBy(0, -speed.doubleValue());
    }
}
