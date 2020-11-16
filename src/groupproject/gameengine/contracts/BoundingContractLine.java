package groupproject.gameengine.contracts;

public interface BoundingContractLine extends BoundingContract {


    default Number distanceTo(Number x, Number y) {
        //double vx = point_x - x.doubleValue(); // |v| <vx, vy>
        //double vy = point_y - y.doubleValue();
        double vx = x.doubleValue() - getX().doubleValue();
        double vy = y.doubleValue() - getY().doubleValue();
        //return -d;
        //x.doubleValue() * normal_x + y.intValue() * normal_y - c;
        return getNormalX().doubleValue() * vx + getNormalY().doubleValue() * vy;
    }


    default Number computeNormal() {
        double vx = getX2().doubleValue() - getX().doubleValue(); //<vx, vy> ie. vector v
        double vy = getY2().doubleValue() - getY().doubleValue();
        double mag_v = Math.sqrt(vx * vx + vy * vy); //magnitude (length?) |v|
        double ux = vx / mag_v; // unit vector <ux, uy> ie. u
        double uy = vy / mag_v; // direction to
        setNormal(-uy, ux);
        return getX().doubleValue() * getNormalX().doubleValue() + getY().doubleValue() * getNormalY().doubleValue();
    }


    default void setNormal(Number normalX, Number normalY){
        setNormalX(normalX);
        setNormalY(normalY);
    }


    @Override
    Number getX2();
    @Override
    Number getY2();

    Number getNormalX();
    Number getNormalY();


    @Override
    default Number getWidth() {
        return getX2().doubleValue() - getX().doubleValue();
    }

    @Override
    default Number getHeight() {
        return getY2().doubleValue() - getY().doubleValue();
    }



    @Override
    default void setWidth(Number width) {

    }

    @Override
    default void setHeight(Number height){

    }

    void setNormalX(Number normalX);
    void setNormalY(Number normalY);
}