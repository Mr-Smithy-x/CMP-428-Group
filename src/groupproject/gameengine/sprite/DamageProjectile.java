package groupproject.gameengine.sprite;

import groupproject.containers.zelda.contracts.Damage;
import groupproject.spritesheeteditor.models.ProjectileFileFormat;

import java.io.IOException;

public abstract class DamageProjectile extends Projectile implements Damage {

    protected DamageProjectile(ProjectileFileFormat format, int x, int y, int scaled, int delay) throws IOException {
        super(format, x, y, scaled, delay);
    }

}
