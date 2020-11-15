package groupproject.gameengine.contracts;

import groupproject.gameengine.helpers.TrigLookupHelper;

public interface Movable extends BoundingContract {
    double[] cos = TrigLookupHelper.cos;
    double[] sin = TrigLookupHelper.sin;

    //region Getters
    Number getSinAngle();
    Number getCosAngle();
    int getWorldAngle();
    //endregion

    //region Setters
    void setWorldAngle(int world_angle);

    //endregion

    //region Defaults


    default void moveBy(Number dx, Number dy) {
        this.setWorld(this.getX().doubleValue() + dx.doubleValue(), this.getY().doubleValue() + dy.doubleValue());
    }

    default void moveForwardBy(Number dA) {
        double dx = (dA.doubleValue() * getCosAngle().doubleValue());
        double dy = (dA.doubleValue() * getSinAngle().doubleValue());
        moveBy(dx, dy);
    }

    default void moveBackwardBy(Number dA) {
        moveForwardBy(-dA.doubleValue());
    }

    default void turnLeft(Number dA) {
        rotateBy(-dA.doubleValue());
    }

    default void turnRight(Number dA) {
        rotateBy(dA.doubleValue());
    }

    default void rotateBy(Number dA) {
        setWorldAngle(getWorldAngle() + dA.intValue());
        int world_angle = getWorldAngle();
        if (world_angle > 359) world_angle -= 360;
        if (world_angle < 0) world_angle += 360;
        if (world_angle > 360) {
            return;
        } else if (world_angle < 0) {
            return;
        }
        setWorldAngle(world_angle);
    }


    default Number getRadius() {
        return getWidth().doubleValue() / 2;
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
    //endregion
}
