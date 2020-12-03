package groupproject.containers.zelda.hud;

import groupproject.containers.zelda.contracts.Energy;
import groupproject.gameengine.hud.BaseHud;

import java.awt.*;

public class EnergyHud extends BaseHud {

    private Energy energy;
    private final Rectangle base;


    private EnergyHud() {
        this.base = new Rectangle(10, 40, 10, 20);
    }

    private static EnergyHud instance = null;

    public static EnergyHud getInstance() {
        if (instance == null) {
            instance = new EnergyHud();
        }
        return instance;
    }

    public void setEnergy(Energy energy) {
        this.energy = energy;
        this.base.width = (int) energy.getMaxEnergy();
    }

    @Override
    protected void onRenderHud(Graphics hud, Graphics parent) {
        hud.setColor(new Color(70, 150, 70));
        hud.fillRect(base.x, base.y, (int) energy.getEnergy() * 2, 20);
        hud.setColor(Color.BLACK);
        hud.drawRect(base.x, base.y, (int) energy.getEnergy() * 2, 20);
        parent.setColor(Color.WHITE);
        String energyPercent = String.format("%.2f%%", energy.getEnergy());
        int stringWidth = hud.getFontMetrics().stringWidth(energyPercent);
        parent.drawString(energyPercent, (int) (energy.getEnergy() * 2 + ((double) stringWidth) / 2), 40 + hud.getFontMetrics().getHeight() / 2);
    }

}
