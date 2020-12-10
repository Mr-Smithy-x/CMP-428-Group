package groupproject.containers.zelda.contracts;

import groupproject.gameengine.camera.BaseCamera;
import groupproject.gameengine.camera.GlobalCamera;
import groupproject.gameengine.contracts.CameraContract;

import java.awt.*;

public interface MiniHealthBar extends Life, CameraContract {

    Polygon triangle = new Polygon(new int[]{0, 10, 20}, new int[]{0, 10, 0}, 3);

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
            int xDraw = getCameraOffsetX(camera).intValue() - width / 3;
            int yDraw = getCameraOffsetY(camera).intValue() - getHeight().intValue() * 2;
            graphics.fillRect(
                    xDraw,
                    yDraw,
                    maxWidth,
                    5);
            graphics.setColor(green);
            graphics.fillRect(
                    xDraw,
                    yDraw,
                    width,
                    5);
            graphics.setColor(black);
            graphics.drawRect(
                    xDraw,
                    yDraw,
                    maxWidth,
                    5);
            if (isOutsideCamera(camera)) {
                int[] xPoints = new int[3];
                int[] yPoints = new int[3];
                for (int i = 0; i < xPoints.length; i++) {
                    xPoints[i] = (int) Math.max(0, camera.getWidth() / 2 - 20);
                    yPoints[i] = (int) Math.max(0, camera.getHeight() / 2 - 20);
                }
                graphics.fillPolygon(xPoints, yPoints, 3);
            }
        }
    }
}
