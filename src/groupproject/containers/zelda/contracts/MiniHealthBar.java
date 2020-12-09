package groupproject.containers.zelda.contracts;

import groupproject.gameengine.camera.BaseCamera;
import groupproject.gameengine.contracts.CameraContract;

import java.awt.*;

public interface MiniHealthBar extends Life, CameraContract {

    default void renderHud(Graphics graphics, BaseCamera camera) {
        if (!isDead()) {
            int x = getCameraOffsetX(camera).intValue();
            int y = getCameraOffsetY(camera).intValue();
            if (x > 255) {
                x = 255 - (Math.max(-255, 255 - x) * -1);
            }
            if (y > 255) {
                y = 255 - (Math.max(-255, 255 - y) * -1);
            }
            x = Math.max(0, x);
            y = Math.max(0, y);
            int alpha = Math.min(x, y);
            Color red = new Color(150, 70, 70, alpha);
            Color green = new Color(0, 150, 70, alpha);
            Color black = new Color(0, 0, 0, alpha);
            graphics.setColor(red);
            int maxWidth = (int) getMaxHealth() / 2;
            int width = (int) getHealth() / 2;
            graphics.fillRect(
                    getCameraOffsetX(camera).intValue() - width / 8,
                    getCameraOffsetY(camera).intValue() - getHeight().intValue() / 2,
                    maxWidth,
                    5);
            graphics.setColor(green);
            graphics.fillRect(
                    getCameraOffsetX(camera).intValue() - width / 8,
                    getCameraOffsetY(camera).intValue() - getHeight().intValue() / 2,
                    width,
                    5);
            graphics.setColor(black);
            graphics.drawRect(
                    getCameraOffsetX(camera).intValue() - width / 8,
                    getCameraOffsetY(camera).intValue() - getHeight().intValue() / 2,
                    maxWidth,
                    5);
        }
    }
}
