package groupproject.containers.zelda.projectiles;

import groupproject.containers.zelda.sound.GlobalSoundEffect;
import groupproject.gameengine.sprite.DamageProjectile;
import groupproject.spritesheeteditor.models.ProjectileFileFormat;

import java.io.IOException;

public class MudBall extends DamageProjectile {

    public MudBall() throws IOException {
        this(1);
    }

    public MudBall(int delay) throws IOException {
        this(2, delay);
    }

    public MudBall(int scaled, int delay) throws IOException {
        super(ProjectileFileFormat.Companion.load("octorok"), 0, 0, scaled, delay);
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
        return 0.5;
    }
}
