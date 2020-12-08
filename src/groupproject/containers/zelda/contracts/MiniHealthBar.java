package groupproject.containers.zelda.contracts;

import groupproject.gameengine.camera.BaseCamera;
import groupproject.gameengine.contracts.CameraContract;

import java.awt.*;

public interface MiniHealthBar extends Life, CameraContract {

    default void renderHud(Graphics graphics, BaseCamera camera) {
        if (!isDead()) {
            int x = getCameraOffsetX(camera).intValue() + getWidth().intValue();
            //int y = getCameraOffsetY(camera).intValue() + getHeight().intValue();


            int alpha = Math.min(Math.max(0, x), Math.min(Math.max(x, 255), 255));

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
