package groupproject.gameengine.models;

import groupproject.gameengine.camera.GlobalCamera;
import groupproject.gameengine.contracts.BoundingContractLine;
import groupproject.gameengine.contracts.CameraContract;
import groupproject.gameengine.contracts.Renderable;

import java.awt.*;

public class BoundingLine implements BoundingContractLine, CameraContract, Renderable {

    double point_x;
    double point_y;
    double point_x2;
    double point_y2;
    double normal_x;
    double normal_y;

    double computedNormal;


    public BoundingLine(double x1, double y1, double x2, double y2) {
        this.point_x = x1;
        this.point_y = y1;
        this.point_x2 = x2;
        this.point_y2 = y2;
        computedNormal = this.computeNormal().doubleValue();
    }

    @Override
    public Number getX2() {
        return point_x2;
    }

    @Override
    public Number getY2() {
        return point_y2;
    }

    @Override
    public Number getNormalX() {
        return normal_x;
    }

    @Override
    public Number getNormalY() {
        return normal_y;
    }

    @Override
    public void setNormalX(Number normal_x) {
        this.normal_x = normal_x.doubleValue();
    }

    @Override
    public void setNormalY(Number normal_y) {
        this.normal_y = normal_y.doubleValue();
    }

    @Override
    public Number getX() {
        return point_x;
    }

    @Override
    public Number getY() {
        return point_y;
    }

    @Override
    public void setX(Number x) {
        this.point_x = x.doubleValue();
    }

    @Override
    public void setY(Number y) {
        this.point_y = y.doubleValue();
    }

    @Override
    public void render(Graphics g) {
        g.drawLine(
                getCameraOffsetX(GlobalCamera.getInstance()).intValue(),
                getCameraOffsetY(GlobalCamera.getInstance()).intValue(),
                getCameraOffsetX2(GlobalCamera.getInstance()).intValue(),
                getCameraOffsetY2(GlobalCamera.getInstance()).intValue()
        );
    }
}
