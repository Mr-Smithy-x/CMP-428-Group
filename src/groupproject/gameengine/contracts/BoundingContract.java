package groupproject.gameengine.contracts;

public interface BoundingContract {

    Number getX();
    Number getY();

    default Number getX2() {
        return getX().doubleValue() + getWidth().doubleValue();
    }

    default Number getY2() {
        return getY().doubleValue() + getHeight().doubleValue();
    }

    default Number getDiagonalX() {
        return getX2().intValue() - 1;
    }

    default Number getDiagonalY() {
        return getY2().intValue() - 1;
    }

    Number getWidth();
    Number getHeight();

    void setWidth(Number width);
    void setHeight(Number height);
    void setX(Number x);
    void setY(Number y);

    default void setWorld(Number x, Number y){
        setX(x);
        setY(y);
    }

}
