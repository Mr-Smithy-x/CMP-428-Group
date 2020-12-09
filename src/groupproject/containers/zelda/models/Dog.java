package groupproject.containers.zelda.models;

import groupproject.containers.zelda.contracts.MiniHealthBar;
import groupproject.gameengine.camera.GlobalCamera;
import groupproject.gameengine.sprite.AttackSprite;
import groupproject.gameengine.sprite.DamageProjectile;
import groupproject.gameengine.sprite.Sprite;
import groupproject.spritesheeteditor.models.PoseFileFormat;

import java.awt.*;
import java.io.IOException;

public class Dog extends AttackSprite implements MiniHealthBar {

    public Dog(int positionX, int positionY, int duration) throws IOException {
        super(PoseFileFormat.Companion.load("dog.pose"), positionX, positionY, 2, duration);
    }


    @Override
    public void render(Graphics g) {
        renderHud(g, GlobalCamera.getInstance());
        super.render(g);
    }

    @Override
    protected void onInitAnimations() {
        super.onInitAnimations();
        setHealth(100);
        setEnergy(50);
    }


    @Override
    public void setProjectile(DamageProjectile projectile) {

    }

    @Override
    public DamageProjectile getProjectile() {
        return null;
    }

}
