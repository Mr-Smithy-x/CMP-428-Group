package groupproject.containers.zelda.hud;

import groupproject.containers.zelda.contracts.Life;
import groupproject.gameengine.hud.BaseHud;

import java.awt.*;

public class LifeHud extends BaseHud {

    private static LifeHud instance = null;
    private final Polygon diamond;
    private final int[] xDerivedPoints;
    private final int[] yDerivedPoints;
    private Life life;

    private LifeHud() {
        Polygon polygon = new Polygon();
        polygon.xpoints = new int[]{10, 20, 30, 20};
        polygon.ypoints = new int[]{20, 10, 20, 30};
        polygon.npoints = polygon.xpoints.length;
        xDerivedPoints = new int[polygon.npoints];
        yDerivedPoints = new int[polygon.npoints];
        this.diamond = polygon;
    }

    public static LifeHud getInstance() {
        if (instance == null) {
            instance = new LifeHud();
        }
        return instance;
    }

    public void setLife(Life life) {
        this.life = life;
    }

    @Override
    protected void onRenderHud(Graphics hud, Graphics parent) {
        for (int index = 0; index < life.getMaxHealth() / 10; index++) {
            for (int point = 0; point < diamond.npoints; point++) {
                xDerivedPoints[point] = (index * 10 * 2) + (diamond.xpoints[point]);
                yDerivedPoints[point] = (diamond.ypoints[point]);
            }
            hud.setColor(new Color(255, 70, 70));
            hud.fillPolygon(xDerivedPoints, yDerivedPoints, diamond.npoints);
            hud.setColor(Color.BLACK);
            hud.drawPolygon(xDerivedPoints, yDerivedPoints, diamond.npoints);
        }
        double health = life.getHealth();
        int width = (int) (health * 2);
        hud.setClip(10, 0, width, 30);
        parent.setColor(Color.WHITE);
        String healthPercent = String.format("%.2f%%", health);
        int stringWidth = hud.getFontMetrics().stringWidth(healthPercent);
        parent.drawString(healthPercent, width + stringWidth / 2, 30 / 2 + hud.getFontMetrics().getHeight() / 2);
    }

}
