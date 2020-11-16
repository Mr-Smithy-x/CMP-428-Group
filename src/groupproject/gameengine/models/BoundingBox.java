package groupproject.gameengine.models;

import groupproject.gameengine.camera.GlobalCamera;
import groupproject.gameengine.contracts.*;
import groupproject.gameengine.models.base.GravitationObject;

import java.awt.*;

public class BoundingBox extends GravitationObject implements CollisionDetection, Renderable, CameraContract {


    public BoundingBox(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean isDraggable() {
        return true;
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
        return width; //haha rect radius go brrrr
    }

    @Override
    public void pushes(Movable contract) {

        if (contract instanceof BoundingBox) {
            double dx = getX().doubleValue() - contract.getX().doubleValue();
            double dy = getY().doubleValue() - contract.getY().doubleValue();
            double d = Math.sqrt(dx * dx + dy * dy);
            double ux = dx / d;
            double uy = dy / d;
            double ri = getRadius().doubleValue() / 2 + contract.getRadius().doubleValue() / 2;
            double p = ri - d;
            setWorld(
                    getX().doubleValue() + ux * p / 2,
                    getY().doubleValue() + uy * p / 2
            );
            double positionX = contract.getX().doubleValue() - (ux * p / 2);
            double positionY = contract.getY().doubleValue() - (uy * p / 2);
            contract.setWorld(positionX, positionY);
        }
    }

    @Override
    public void render(Graphics g) {
        //what about if we wanted to center it? so we move the x and y position of the object
        g.drawRect(getCameraOffsetX(GlobalCamera.getInstance()).intValue(),
                getCameraOffsetY(GlobalCamera.getInstance()).intValue(),
                getWidth().intValue(), getHeight().intValue());
    }

    public Number getDiagonalX() {
        return x + width - 1;
    }

    public Number getDiagonalY() {
        return y + height - 1;
    }


    public boolean overlaps(BoundingBox r, int dx, int dy) {
        return !(this.x + dx > r.getDiagonalX().intValue() || this.y + dy > r.getDiagonalY().intValue() ||
                r.x > this.getDiagonalX().intValue() + dx || r.y > this.getDiagonalY().intValue() + dy);
    }


}
