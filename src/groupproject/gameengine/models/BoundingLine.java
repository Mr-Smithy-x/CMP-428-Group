package groupproject.gameengine.models;

import groupproject.gameengine.camera.GlobalCamera;
import groupproject.gameengine.contracts.BoundingContractLine;
import groupproject.gameengine.contracts.CameraContract;
import groupproject.gameengine.contracts.Renderable;

import java.awt.*;

public class BoundingLine implements BoundingContractLine, CameraContract, Renderable {

    double pointX;
    double pointY;
    double pointX2;
    double pointY2;
    double normalX;
    double normalY;

    double computedNormal;


    public BoundingLine(double x1, double y1, double x2, double y2) {
        this.pointX = x1;
        this.pointY = y1;
        this.pointX2 = x2;
        this.pointY2 = y2;
        computedNormal = this.computeNormal().doubleValue();
    }

    @Override
    public Number getX2() {
        return pointX2;
    }

    @Override
    public Number getY2() {
        return pointY2;
    }

    @Override
    public Number getNormalX() {
        return normalX;
    }

    @Override
    public Number getNormalY() {
        return normalY;
    }

    @Override
    public void setNormalX(Number normalX) {
        System.out.println("Normal X:" + normalX);
        this.normalX = normalX.doubleValue();
    }

    @Override
    public void setNormalY(Number normalY) {
        System.out.println("Normal Y:" + normalY);
        this.normalY = normalY.doubleValue();
    }

    @Override
    public Number getX() {
        return pointX;
    }

    @Override
    public Number getY() {
        return pointY;
    }

    @Override
    public void setX(Number x) {
        this.pointX = x.doubleValue();
    }

    @Override
    public void setY(Number y) {
        this.pointY = y.doubleValue();
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
