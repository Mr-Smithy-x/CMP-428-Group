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
    public Number getRadius() {
        return width; //haha rect radius go brrrr
    }

    @Override
    public void render(Graphics g) {
        //what about if we wanted to center it? so we move the x and y position of the object
        g.drawRect(getCameraOffsetX(GlobalCamera.getInstance()).intValue(),
                getCameraOffsetY(GlobalCamera.getInstance()).intValue(),
                getWidth().intValue(), getHeight().intValue());
    }


}
