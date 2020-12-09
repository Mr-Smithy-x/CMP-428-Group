package groupproject.containers.zelda.managers;

import groupproject.containers.zelda.algorithms.ATileStarAlgorithm;
import groupproject.containers.zelda.sound.GlobalSoundEffect;
import groupproject.containers.zelda.sound.GlobalSoundTrack;
import groupproject.gameengine.GameContainer;
import groupproject.gameengine.algorithms.AStar;
import groupproject.gameengine.camera.GlobalCamera;
import groupproject.gameengine.sprite.AttackSprite;
import groupproject.gameengine.sprite.Sprite;
import groupproject.gameengine.tile.Tile;
import groupproject.gameengine.tile.TileMap;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.PriorityQueue;

public class ZeldaWorldManager extends BaseWorldManager {

    ATileStarAlgorithm aStar;
    PriorityQueue<Tile> path;

    public ZeldaWorldManager(GameContainer container) {
        super(container);
    }

    /**
     * Handle the input events here
     *
     * @param keys
     */
    public void manual(boolean[] keys) {
        if (keys[KeyEvent.VK_D]) {
            getPlayer().damageHealth(1);
        } else if (keys[KeyEvent.VK_F]) {
            getPlayer().incrementHealth(1);
        }
        if (!isPlayerDead()) {
            if (keys[KeyEvent.VK_LEFT]) {
                if (keys[KeyEvent.VK_R]) getPlayer().roll();
                else getPlayer().setSpritePose(Sprite.Pose.LEFT);
                getPlayer().move();
            } else if (keys[KeyEvent.VK_RIGHT]) {
                if (keys[KeyEvent.VK_R]) getPlayer().roll();
                else getPlayer().setSpritePose(Sprite.Pose.RIGHT);
                getPlayer().move();
            } else if (keys[KeyEvent.VK_UP]) {
                if (keys[KeyEvent.VK_R]) getPlayer().roll();
                else getPlayer().setSpritePose(Sprite.Pose.UP);
                getPlayer().move();
            } else if (keys[KeyEvent.VK_DOWN]) {
                if (keys[KeyEvent.VK_R]) getPlayer().roll();
                else getPlayer().setSpritePose(Sprite.Pose.DOWN);
                getPlayer().move();
            }
            if (getPlayer().hasEnergy()) {
                if (keys[KeyEvent.VK_Z]) {
                    getPlayer().spinAttack(getEnemies());
                    getPlayer().useEnergy(.5);
                } else if (keys[KeyEvent.VK_SPACE]) {
                    getPlayer().attack(getEnemies());
                    getPlayer().useEnergy(.1);
                } else if (keys[KeyEvent.VK_T]) {
                    getPlayer().shoot();
                }
            }
        } else {
            GlobalSoundTrack.getInstance().setTrack(GlobalSoundTrack.Track.PAUSE);
        }
    }

    public void calculatePath(AttackSprite enemy) {
        aStar.solvePath(enemy, getPlayer());
    }

    /**
     * Enemy AI work and others
     */
    public void automate() {

        getEnemies().stream().filter(enemy -> !enemy.isDead()).forEach(enemy -> {
            getPlayer().isProjectileHitting(enemy);
            enemy.isProjectileHitting(getPlayer());
            if (enemy.distanceBetween(getPlayer()) > 600) {
                /*if (enemy.getX().intValue() > getPlayer().getX().intValue()) {
                    enemy.setSpritePose(Sprite.Pose.LEFT);
                    enemy.move();
                }
                if (enemy.getY().intValue() > getPlayer().getY().intValue()) {
                    enemy.setSpritePose(Sprite.Pose.UP);
                    enemy.move();
                }
                if (enemy.getX().intValue() < getPlayer().getX().intValue()) {
                    enemy.setSpritePose(Sprite.Pose.RIGHT);
                    enemy.move();
                }
                if (enemy.getY().intValue() < getPlayer().getY().intValue()) {
                    enemy.setSpritePose(Sprite.Pose.DOWN);
                    enemy.move();
                }*/
                if (enemy.distanceBetween(getPlayer()) < 100) {
                    //Basic AI, we can improve this
                    enemy.shoot();
                } else if (enemy.isPathNullOrEmpty()) {
                    calculatePath(enemy);
                }else{
                    enemy.move();
                }
            } else if(!enemy.isPathNullOrEmpty()) {
                enemy.move();
            }
        });
    }

    @Override
    protected void renderGlobalSounds() {
        if (isPlayerDead() || getContainer().isPaused()) {
            GlobalSoundTrack.getInstance().setTrack(GlobalSoundTrack.Track.PAUSE);
        } else if (getPlayer().isNear(getEnemies())) {
            GlobalSoundTrack.getInstance().setTrack(GlobalSoundTrack.Track.COMBAT);
        } else {
            GlobalSoundTrack.getInstance().setTrack(GlobalSoundTrack.Track.NORMAL);
        }
        GlobalSoundEffect.getInstance().play(getPlayer());
        GlobalSoundTrack.getInstance().play();
    }

    @Override
    protected void renderGame(Graphics g) {
        super.renderGame(g);
        if (path != null) {
            for (Tile tile : path) {
                int width = tile.getWidth().intValue();
                int height = tile.getHeight().intValue();
                g.setColor(new Color(255, 0, 0, 80));
                g.fillRect((int) ((tile.getPoint().x * width) - GlobalCamera.getInstance().getX()), (int) ((tile.getPoint().y * height) - GlobalCamera.getInstance().getY()), width, height);
            }
        }
        //The game already handles most of the rendering, if there any more rendering that needs to be done
        //do it here
    }

    @Override
    public void setTileMap(TileMap map) {
        super.setTileMap(map);
        aStar = new ATileStarAlgorithm(map);
    }


    public AStar<TileMap, Tile> getAStar() {
        return aStar;
    }


}
