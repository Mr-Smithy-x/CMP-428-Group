package groupproject.containers.zelda.contracts;

import groupproject.gameengine.camera.BaseCamera;
import groupproject.gameengine.contracts.CameraContract;

import java.awt.*;

public interface MiniHealthBar extends Life, CameraContract {

    default void renderHud(Graphics graphics, BaseCamera camera) {
        if (!isDead()) {
            graphics.setColor(new Color(150, 70, 70));
            int maxWidth = (int) getMaxHealth() / 2;
            int width = (int) getHealth() / 2;
            graphics.fillRect(
                    getCameraOffsetX(camera).intValue() - width / 8,
                    getCameraOffsetY(camera).intValue() - getHeight().intValue() / 2,
                    maxWidth,
                    5);
            graphics.setColor(new Color(0, 150, 70));
            graphics.fillRect(
                    getCameraOffsetX(camera).intValue() - width / 8,
                    getCameraOffsetY(camera).intValue() - getHeight().intValue() / 2,
                    width,
                    5);
            graphics.setColor(Color.BLACK);
            graphics.drawRect(
                    getCameraOffsetX(camera).intValue() - width/8,
                    getCameraOffsetY(camera).intValue() - getHeight().intValue() / 2,
                    maxWidth,
                    5);
        }
    }
}
