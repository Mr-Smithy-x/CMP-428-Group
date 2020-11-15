package groupproject.gameengine.contracts;

import groupproject.gameengine.camera.BaseCamera;

public interface CameraContract extends BoundingContract{

    default Number getCameraOffsetX(BaseCamera camera) {
        return getX().doubleValue() - camera.getX();
    }

    default Number getCameraOffsetY(BaseCamera camera) {
        return getY().doubleValue() - camera.getY();
    }

    default Number getCameraOffsetX2(BaseCamera camera) {
        return getX2().doubleValue() - camera.getX();
    }

    default Number getCameraOffsetY2(BaseCamera camera) {
        return getY2().doubleValue() - camera.getY();
    }

}
