package groupproject.containers.zelda.contracts;

public interface Energy extends Life {

    double getEnergy();

    void setEnergy(double energy);

    default double getMaxEnergy() {
        return 50;
    }

    default void useEnergy(double usage, boolean affectHealth) {
        if (usage >= 50 * .25 && affectHealth) {
            damageHealth(usage * .25);
        }
        if (getEnergy() > 0) {
            setEnergy(Math.max(getEnergy() - usage, 0));
        }
    }

    default void useEnergy(double usage) {
        useEnergy(usage, false);
    }

    default void incrementEnergy(double additional) {
        incrementEnergy(additional, false);
    }

    default void incrementEnergy(double additional, boolean affectHealth) {
        if (affectHealth) incrementHealth(.05);
        if (getEnergy() < getMaxEnergy()) {
            setEnergy(Math.min(getEnergy() + additional, getMaxEnergy()));
        }
    }

    default boolean hasEnergy() {
        return getEnergy() > 0;
    }
}
