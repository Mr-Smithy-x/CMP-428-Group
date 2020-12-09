package groupproject.containers.zelda.models;

import groupproject.containers.zelda.contracts.MiniHealthBar;
import groupproject.containers.zelda.projectiles.MudBall;
import groupproject.gameengine.camera.GlobalCamera;
import groupproject.gameengine.sprite.AttackSprite;
import groupproject.gameengine.sprite.DamageProjectile;
import groupproject.spritesheeteditor.models.PoseFileFormat;

import java.awt.*;
import java.io.IOException;

public class Octorok extends AttackSprite implements MiniHealthBar  {

    private DamageProjectile projectile;

    public Octorok(int positionX, int positionY, int duration) throws IOException {
        super(PoseFileFormat.Companion.load(Octorok.class.getSimpleName().toLowerCase()), positionX, positionY, 2, duration);
        setProjectile(new MudBall());
        setWidth(16);
        setHeight(16);
    }


    @Override
    protected void onInitAnimations() {
        super.onInitAnimations();
        setHealth(100);
        setEnergy(50);
    }

    @Override
    public void render(Graphics g) {
        renderHud(g, GlobalCamera.getInstance());
        super.render(g);
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
