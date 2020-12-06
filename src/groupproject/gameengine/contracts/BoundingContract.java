package groupproject.gameengine.contracts;

public interface BoundingContract {

    default Number getDiagonalX() {
        return getX2().intValue() - 1;
    }

    default Number getX2() {
        return getX().doubleValue() + getWidth().doubleValue();
    }

    Number getX();

    void setX(Number x);

    Number getWidth();

    void setWidth(Number width);

    default Number getDiagonalY() {
        return getY2().intValue() - 1;
    }

    default Number getY2() {
        return getY().doubleValue() + getHeight().doubleValue();
    }

    Number getY();

    void setY(Number y);

    Number getHeight();

    void setHeight(Number height);

    default void setWorld(Number x, Number y) {
        setX(x);
        setY(y);
    }
}