package groupproject.containers.zelda.models;


import groupproject.gameengine.sprite.AttackSprite;
import groupproject.gameengine.sprite.DamageProjectile;
import groupproject.spritesheeteditor.models.PoseFileFormat;

import java.io.IOException;

public class Gibdo extends AttackSprite {

    public Gibdo(int positionX, int positionY, int duration) throws IOException {
        super(PoseFileFormat.Companion.load(Gibdo.class.getSimpleName().toLowerCase()), positionX, positionY, 2, duration);
    }


    @Override
    protected void onInitAnimations() {
        super.onInitAnimations();
        setHealth(100);
        setEnergy(50);
    }

    @Override
    public DamageProjectile getProjectile() {
        return null;
    }

    @Override
    public void setProjectile(DamageProjectile projectile) {

    }
}
