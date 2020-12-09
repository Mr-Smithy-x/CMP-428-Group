package groupproject.containers.zelda.managers;

import groupproject.containers.zelda.hud.EnergyHud;
import groupproject.containers.zelda.hud.LifeHud;
import groupproject.gameengine.GameContainer;
import groupproject.gameengine.camera.GlobalCamera;
import groupproject.gameengine.contracts.Renderable;
import groupproject.gameengine.sprite.AttackSprite;

import java.awt.*;
import java.util.ArrayList;

public abstract class BaseWorldManager implements Renderable {
    protected MapManager mapManager = MapManager.getInstance();
    private AttackSprite player;
    private ArrayList<AttackSprite> enemies;
    private GameContainer container;

    protected BaseWorldManager(GameContainer container) {
        this.container = container;
        enemies = new ArrayList<>();
    }

    public GameContainer getContainer() {
        return container;
    }

    public AttackSprite getPlayer() {
        return player;
    }

    public void setPlayer(AttackSprite player) {
        if (player.getVelocity() == 0) {
            player.setVelocity(5);
        }
        this.player = player;
        LifeHud.getInstance().setLife(player);
        EnergyHud.getInstance().setEnergy(player);
    }

    public ArrayList<AttackSprite> getEnemies() {
        return enemies;
    }

    public void setEnemies(ArrayList<AttackSprite> enemies) {
        this.enemies = enemies;
    }

    /**
     * Handle the input events here
     *
     * @param keys
     */
    public abstract void manual(boolean[] keys);

    public void adjust() {
        GlobalCamera.getInstance().setOrigin(player, container.getWidth(), container.getHeight());
    }

    /**
     * Enemy AI work and others
     */
    public abstract void automate();

    protected abstract void renderGlobalSounds();

    @Override
    public void render(Graphics g) {
        mapManager.getCurrentMap().render(g);
        enemies.stream().filter(e -> !e.isDead()).forEach(e -> e.render(g));
        renderGame(g);
        player.render(g);
        LifeHud.getInstance().render(g);
        EnergyHud.getInstance().render(g);
        renderGlobalSounds();
    }

    /**
     * In the event you want to render more
     *
     * @param g
     */
    protected void renderGame(Graphics g) {

    }

    public void addEnemy(AttackSprite enemy) {
        if (enemy.getVelocity() == 0) {
            enemy.setVelocity(3);
        }
        enemies.add(enemy);

    }

    public boolean isPlayerDead() {
        return player.isDead();
    }
}
