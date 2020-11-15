package groupproject.gameengine.models;

import groupproject.gameengine.camera.GlobalCamera;
import groupproject.gameengine.contracts.*;
import groupproject.gameengine.models.base.GravitationObject;

import java.awt.*;

public class BoundingBox extends GravitationObject implements CollisionDetection, Renderable, CameraContract {


    public BoundingBox(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean contains(int mx, int my) {
        return (mx > x) && (mx < x + width) &&
                (my > y) && (my < y + height);
    }

    @Override
    public boolean overlaps(Movable box) {
        boolean collides = false;
        if (box instanceof BoundingBox) {
            collides = (box.getX().doubleValue() + box.getWidth().doubleValue() >= x) &&
                    (x + width >= box.getX().doubleValue()) &&
                    (box.getY().doubleValue() + box.getHeight().doubleValue() >= y) &&
                    (y + height >= box.getY().doubleValue());
        } else {
            //circle collision detection
        }
        return collides;
    }

    @Override
    public Number getRadius() {
        return width / 2; //haha rect radius go brrrr
    }

    //in the case of overlapping, this is different from a circle since circles draw from the center
    @Override
    public boolean overlaps(BoundingContractLine line) {
        double d = line.distanceTo(x, y).doubleValue();
        return d < width;
    }

    @Override
    public void render(Graphics g) {

        //what about if we wanted to center it? so we move the x and y position of the object
        g.drawRect(getCameraOffsetX(GlobalCamera.getInstance()).intValue() - getWidth().intValue() / 2,
                 getCameraOffsetY(GlobalCamera.getInstance()).intValue() - getHeight().intValue() / 2,
                getWidth().intValue(), getHeight().intValue());
    }

}
