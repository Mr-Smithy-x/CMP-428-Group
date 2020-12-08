package groupproject.gameengine.contracts;

import groupproject.gameengine.camera.BaseCamera;

public interface CameraContract extends BoundingContract {

    default boolean isInsideCamera(BaseCamera camera) {
        if(getX().doubleValue() < camera.getX() + camera.getWidth() && getX().doubleValue() > camera.getX()) {
            return getY().doubleValue() < camera.getY() + camera.getWidth() && getY().doubleValue() > camera.getY();
        }
        return false;
    }

    default boolean isOutsideCamera(BaseCamera camera){
        return !isInsideCamera(camera);
    }

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
