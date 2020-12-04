package groupproject.containers.zelda.contracts;

public interface Life {

    double getHealth();

    default double getMaxHealth() {
        return 100;
    }

    default boolean isDead() {
        return getHealth() <= 0;
    }

    void setHealth(double health);

    default void damageHealth(double damage) {
        if (getHealth() > 0) {
            setHealth(Math.max(getHealth() - damage, 0));
        }
    }

    default void incrementHealth(double damage) {
        if (getHealth() < getMaxHealth()) {
            setHealth(Math.min(getHealth() + damage, getMaxHealth()));
        }
    }

}
