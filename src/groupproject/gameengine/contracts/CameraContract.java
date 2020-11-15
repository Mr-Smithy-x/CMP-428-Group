package groupproject.gameengine.contracts;

import groupproject.gameengine.camera.BaseCamera;

public interface CameraContract extends BoundingContract {

    default Number getCameraOffsetX(BaseCamera camera) {
        if (camera == null) {
            return getX();
        }
        return getX().doubleValue() - camera.getX();
    }

    default Number getCameraOffsetY(BaseCamera camera) {
        if (camera == null) {
            return getY();
        }
        return getY().doubleValue() - camera.getY();
    }

    default Number getCameraOffsetX2(BaseCamera camera) {
        if (camera == null) {
            return getX2();
        }
        return getX2().doubleValue() - camera.getX();
    }

    default Number getCameraOffsetY2(BaseCamera camera) {
        if (camera == null) {
            return getY();
        }
        return getY2().doubleValue() - camera.getY();
    }

}
