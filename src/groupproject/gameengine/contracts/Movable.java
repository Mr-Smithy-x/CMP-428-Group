package groupproject.gameengine.contracts;

public interface Movable extends BoundingContract {
    default void moveBy(Number dx, Number dy) {
        int y = this.getY().intValue() + dy.intValue();
        int x = this.getX().intValue() + dx.intValue();
        x = x - (x % 2); // make sure its even, it does justice a*
        y = y - (y % 2);
        this.setWorld(x, y);
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
