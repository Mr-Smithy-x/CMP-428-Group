package groupproject.containers.zelda.projectiles;

import groupproject.containers.zelda.sound.GlobalSoundEffect;
import groupproject.gameengine.sprite.Projectile;
import groupproject.spritesheeteditor.models.ProjectileFileFormat;

import java.io.IOException;

public class MudBall extends Projectile {

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
    protected void initAnimations() {

    }

    @Override
    public String getSoundEffectFile() {
        return null;
    }

    @Override
    protected void playSound() {
        GlobalSoundEffect.getInstance().play(GlobalSoundEffect.Clips.CANE);
    }
}
