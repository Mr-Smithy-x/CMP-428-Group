package groupproject.containers.zelda.contracts;

import groupproject.gameengine.contracts.CollisionDetection;
import groupproject.gameengine.sprite.AttackSprite;
import groupproject.gameengine.sprite.Projectile;

public interface Damage {

    double getDamagePoints();

    default void damage(Projectile projectile, AttackSprite detection) {
        if (shouldDamage(projectile, detection)) {
            detection.damageHealth(getDamagePoints());
        }
    }

    default boolean shouldDamage(Projectile projectile, CollisionDetection detection) {
        if (projectile == null || detection == null) return false;
        return projectile.isOverlapping(detection) && projectile.getVelocity() > 0;
    }

}
