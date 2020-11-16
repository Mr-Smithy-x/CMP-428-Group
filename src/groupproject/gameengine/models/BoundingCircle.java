package groupproject.gameengine.models;

import groupproject.gameengine.camera.GlobalCamera;
import groupproject.gameengine.contracts.CameraContract;
import groupproject.gameengine.contracts.CollisionDetection;
import groupproject.gameengine.contracts.Renderable;
import groupproject.gameengine.models.base.GravitationObject;

import java.awt.*;

public class BoundingCircle
        extends GravitationObject
        implements CollisionDetection, Renderable, CameraContract {

    double radius;

    @Override
    public boolean isDraggable() {
        return true;
    }

    public BoundingCircle(double x, double y, double radius){
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.width = radius * 2;
        this.height = radius * 2;
    }


    @Override
    public Number getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public void render(Graphics g) {
        int yRadius = (int) (getCameraOffsetX(GlobalCamera.getInstance()).doubleValue() - getRadius().doubleValue());
        int xRadius = (int) (getCameraOffsetY(GlobalCamera.getInstance()).doubleValue() - getRadius().doubleValue());
        g.drawOval(yRadius, xRadius, (int) (2.0 * radius), (int) (2.0 * radius));
        g.drawLine(getCameraOffsetX(GlobalCamera.getInstance()).intValue(),
                getCameraOffsetY(GlobalCamera.getInstance()).intValue(),
                (int) (getCameraOffsetX(GlobalCamera.getInstance()).doubleValue() + radius * getCosAngle().doubleValue()),
                (int) (getCameraOffsetY(GlobalCamera.getInstance()).doubleValue() + radius * getSinAngle().doubleValue())
        );
    }
}
