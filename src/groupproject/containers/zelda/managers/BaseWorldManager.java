package groupproject.containers.zelda.managers;

import groupproject.containers.zelda.hud.EnergyHud;
import groupproject.containers.zelda.hud.LifeHud;
import groupproject.gameengine.GameContainer;
import groupproject.gameengine.camera.GlobalCamera;
import groupproject.gameengine.contracts.Renderable;
import groupproject.gameengine.sprite.AttackSprite;
import groupproject.gameengine.tile.TileMap;

import java.awt.*;
import java.util.ArrayList;

public abstract class BaseWorldManager implements Renderable {
    private AttackSprite player;
    private ArrayList<AttackSprite> enemies;
    private TileMap map;
    private GameContainer container;

    public GameContainer getContainer() {
        return container;
    }

    public AttackSprite getPlayer() {
        return player;
    }

    public ArrayList<AttackSprite> getEnemies() {
        return enemies;
    }

    public TileMap getMap() {
        return map;
    }

    public BaseWorldManager(GameContainer container) {
        this.container = container;
        enemies = new ArrayList<>();
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

    public void setTileMap(TileMap map) {
        this.map = map;
        this.map.initializeMap();
    }

    public void setPlayer(AttackSprite player) {
        if (player.getVelocity() == 0) {
            player.setVelocity(5);
        }
        this.player = player;
        LifeHud.getInstance().setLife(player);
        EnergyHud.getInstance().setEnergy(player);
    }

    public void setEnemies(ArrayList<AttackSprite> enemies) {
        this.enemies = enemies;
    }

    @Override
    public void render(Graphics g) {
        map.render(g);
        enemies.stream().filter(e -> !e.isDead()).forEach(e -> e.render(g));
        renderGame(g);
        player.render(g);
        LifeHud.getInstance().render(g);
        EnergyHud.getInstance().render(g);
        renderGlobalSounds();
    }

    /**
     * In the event you want to render more
     * @param g
     */
    protected void renderGame(Graphics g){

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