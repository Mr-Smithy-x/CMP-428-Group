package groupproject.containers.zelda.projectiles;

import groupproject.containers.zelda.sound.GlobalSoundEffect;
import groupproject.gameengine.sprite.DamageProjectile;
import groupproject.gameengine.sprite.Projectile;
import groupproject.spritesheeteditor.models.ProjectileFileFormat;

import java.io.IOException;

public class EnergyBall extends DamageProjectile {

    public EnergyBall() throws IOException {
        this(1);
    }

    public EnergyBall(int delay) throws IOException {
        this(2, delay);
    }

    public EnergyBall(int scaled, int delay) throws IOException {
        super(ProjectileFileFormat.Companion.load("link_final_spritesheet"), 0, 0, scaled, delay);
        this.setVelocity(30);
    }

    @Override
    protected void onInitAnimations() {

    }

    @Override
    public String getSoundEffectFile() {
        return null;
    }

    @Override
    protected void playSound() {
        GlobalSoundEffect.getInstance().play(GlobalSoundEffect.Clips.CANE);
    }

    @Override
    public double getDamagePoints() {
        return 5;
    }
}
