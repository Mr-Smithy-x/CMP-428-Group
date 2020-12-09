package groupproject.containers.zelda.models;

import groupproject.containers.zelda.projectiles.EnergyBall;
import groupproject.gameengine.sprite.AttackSprite;
import groupproject.gameengine.sprite.DamageProjectile;
import groupproject.spritesheeteditor.models.PoseFileFormat;

import java.io.IOException;

public class MinishLink extends AttackSprite {

    DamageProjectile projectile;

    public MinishLink(int positionX, int positionY, int duration) throws IOException {
        super(PoseFileFormat.Companion.load("link_final_spritesheet"), positionX, positionY, 2, duration);
        setProjectile(new EnergyBall());
        setWidth(32);
        setHeight(32);
    }

    @Override
    protected void onInitAnimations() {
        super.onInitAnimations();
        setHealth(100);
        setEnergy(50);
    }

    @Override
    public void setProjectile(DamageProjectile projectile) {
        this.projectile = projectile;
    }

    @Override
    public DamageProjectile getProjectile() {
        return projectile;
    }
}
