package groupproject.containers.zelda;

import groupproject.containers.zelda.helpers.GameTextDialog;
import groupproject.containers.zelda.hud.EnergyHud;
import groupproject.containers.zelda.hud.LifeHud;
import groupproject.containers.zelda.models.Dog;
import groupproject.containers.zelda.models.MinishLink;
import groupproject.containers.zelda.models.Octorok;
import groupproject.containers.zelda.models.Gibdo;
import groupproject.containers.zelda.projectiles.EnergyBall;
import groupproject.containers.zelda.projectiles.MudBall;
import groupproject.containers.zelda.sound.GlobalSoundEffect;
import groupproject.containers.zelda.sound.GlobalSoundTrack;
import groupproject.gameengine.GameContainer;
import groupproject.gameengine.camera.GlobalCamera;
import groupproject.gameengine.models.BoundingBox;
import groupproject.gameengine.sprite.Sprite;
import groupproject.gameengine.tile.TileMap;
import groupproject.games.ZeldaTestGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class ZeldaContainer extends GameContainer {

    Octorok octorok;
    Dog dog;
    BoundingBox healthBox;
    BoundingBox damageBox;
    MinishLink minishLink;
    TileMap map;
    Gibdo gibdo;

    protected ZeldaContainer(JFrame container, Canvas canvas) {
        super(container, canvas);
    }

    public static GameContainer frame(int width, int height) {
        JFrame frame = make("Zelda Test Game", width, height);
        Canvas canvas = make(width, height);
        frame.add(canvas);
        frame.pack();
        return new ZeldaContainer(frame, canvas);
    }

    @Override
    protected void onPlay() {
        if(!octorok.isDead()) {
            if (octorok.distanceBetween(minishLink) > 100) {
                if (octorok.getX().intValue() > minishLink.getX().intValue()) {
                    octorok.setSpritePose(Sprite.Pose.LEFT);
                    octorok.move();
                }
                if (octorok.getY().intValue() > minishLink.getY().intValue()) {
                    octorok.setSpritePose(Sprite.Pose.UP);
                    octorok.move();
                }
                if (octorok.getX().intValue() < minishLink.getX().intValue()) {
                    octorok.setSpritePose(Sprite.Pose.RIGHT);
                    octorok.move();
                }
                if (octorok.getY().intValue() < minishLink.getY().intValue()) {
                    octorok.setSpritePose(Sprite.Pose.DOWN);
                    octorok.move();
                }
            } else {
                //Basic AI, we can improve this
                octorok.shoot();
                    octorok.isProjectileHitting(minishLink);

                if (octorok.isOverlapping(minishLink) && (octorok.getX().intValue() == minishLink.getX().intValue())){
                    minishLink.damageHealth(0.5);
                }
            }
        }
        if(!gibdo.isDead()){
            //gibdo.getX().intValue() <= 310 && gibdo.getY().intValue == 350
            //gibdo.getX().intValue() <= gibdo.getX().intValue()+10 && gibdo.getY().intValue() == gibdo.getY().intValue()
            if(gibdo.getX().intValue() <= gibdo.getX().intValue()+10 && gibdo.getY().intValue() == gibdo.getY().intValue()){
                gibdo.setSpritePose(Sprite.Pose.LEFT);
                gibdo.move();
            }
            //gibdo.getX().intValue() <= 140 && gibdo.getY().intValue() >= 350
            //gibdo.getX().intValue() <= gibdo.getX().intValue()-10 && gibdo.getY().intValue() == gibdo.getY().intValue()
            if(gibdo.getX().intValue() >= gibdo.getX().intValue()-10 && gibdo.getY().intValue() == gibdo.getY().intValue()){
                gibdo.setSpritePose(Sprite.Pose.DOWN);
                gibdo.move();
            }
            //gibdo.getX().intValue() >= 140 && gibdo.getY().intValue() == 450
            //gibdo.getX().intValue() >= gibdo.getX().intValue()-10 && gibdo.getY().intValue() == gibdo.getY().intValue()+100
            if(gibdo.getX().intValue() >= 140 && gibdo.getY().intValue() == 450){
                gibdo.setSpritePose(Sprite.Pose.RIGHT);
                gibdo.move();
            }
            //gibdo.getX().intValue() >= 310 && gibdo.getY().intValue() == 450
            //gibdo.getX().intValue() >= gibdo.getX().intValue()+10 && gibdo.getY().intValue() == gibdo.getY().intValue()+100
            if(gibdo.getX().intValue() == 310 && gibdo.getY().intValue() <= 450){
                gibdo.setSpritePose(Sprite.Pose.UP);
                gibdo.move();
            }
            if(gibdo.isOverlapping(minishLink) && (gibdo.getX().intValue() == minishLink.getX().intValue())){
                minishLink.damageHealth(1);
            }
        }
        if (pressedKey[KeyEvent.VK_D]) {
            minishLink.damageHealth(1);
        } else if (pressedKey[KeyEvent.VK_F]) {
            minishLink.incrementHealth(1);
        }
        if (!minishLink.isDead()) {
            if (pressedKey[KeyEvent.VK_LEFT]) {
                if (pressedKey[KeyEvent.VK_R]) minishLink.roll();
                else minishLink.setSpritePose(Sprite.Pose.LEFT);
                minishLink.move();
            } else if (pressedKey[KeyEvent.VK_RIGHT]) {
                if (pressedKey[KeyEvent.VK_R]) minishLink.roll();
                else minishLink.setSpritePose(Sprite.Pose.RIGHT);
                minishLink.move();
            } else if (pressedKey[KeyEvent.VK_UP]) {
                if (pressedKey[KeyEvent.VK_R]) minishLink.roll();
                else minishLink.setSpritePose(Sprite.Pose.UP);
                minishLink.move();
            } else if (pressedKey[KeyEvent.VK_DOWN]) {
                if (pressedKey[KeyEvent.VK_R]) minishLink.roll();
                else minishLink.setSpritePose(Sprite.Pose.DOWN);
                minishLink.move();
            }
            if (minishLink.hasEnergy()) {
                if (pressedKey[KeyEvent.VK_Z]) {
                    minishLink.spin();
                    minishLink.useEnergy(.5);
                } else if (pressedKey[KeyEvent.VK_SPACE]) {
                    minishLink.attack();
                    minishLink.useEnergy(.1);
                    octorok.damageHealth(1);
                    gibdo.damageHealth(1);
                } else if (pressedKey[KeyEvent.VK_T]) {
                    minishLink.shoot();
                    minishLink.isProjectileHitting(gibdo);
                    minishLink.isProjectileHitting(octorok);
                }
            }
            if (minishLink.isOverlapping(dog)) {
                minishLink.pushes(dog);
            }

            if (octorok.isOverlapping(dog)) {
                octorok.pushes(dog);
            }
            if (healthBox.isOverlapping(minishLink)) {
                minishLink.incrementHealth(.1);
                minishLink.incrementEnergy(.05);
                GlobalSoundTrack.getInstance().setTrack(GlobalSoundTrack.Track.PAUSE);
            } else if (damageBox.isOverlapping(minishLink)) {
                GlobalSoundTrack.getInstance().setTrack(GlobalSoundTrack.Track.COMBAT);
                minishLink.damageHealth(.2);
                minishLink.useEnergy(.1);
            } else {
                GlobalSoundTrack.getInstance().setTrack(GlobalSoundTrack.Track.NORMAL);
            }
        }
        GlobalSoundEffect.getInstance().play(minishLink);
        GlobalSoundTrack.getInstance().play();
        GlobalCamera.getInstance().setOrigin(minishLink.getBounds(), getWidth(), getHeight());
    }

    @Override
    protected void onPause() {
        super.onPause();
        GlobalSoundTrack.getInstance().setTrack(GlobalSoundTrack.Track.PAUSE);
        GlobalSoundTrack.getInstance().play();
    }

    @Override
    protected void onPaint(Graphics g) {
        g.setColor(new Color(70, 120, 70));
        g.fillRect(0, 0, getWidth(), getHeight());
        if (map != null) map.render(g);
        dog.render(g);
        octorok.render(g);
        gibdo.render(g);
        minishLink.render(g);
        g.setColor(Color.GREEN);
        healthBox.render(g);
        g.setColor(Color.RED);
        damageBox.render(g);
        LifeHud.getInstance().render(g);
        EnergyHud.getInstance().render(g);
        if (ZeldaTestGame.inDebuggingMode()) {
            GlobalCamera.getInstance().render(g, getContainer());
        }
        if (minishLink.isDead()) {
            g.setColor(new Color(1, 1, 1, 0.4f));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setFont(caveatFont);
            g.setColor(new Color(255, 70, 70));
            drawTextCenteredOffset(g, GameTextDialog.PLAYER_DIED, 0, getHeight() / 4);
        }
    }

    @Override
    protected void onInitialize() throws IOException {
        map = loadTileMap("forest_test.tilemap");
        map.initializeMap();
        octorok = new Octorok(getWidth() / 2, getHeight() / 2, 1000 / 16);
        dog = new Dog(getWidth() / 2 - 100, getHeight() / 2 - 50, 2);
        minishLink = new MinishLink(getWidth() / 2, getHeight() / 2, 1000 / 16);
        gibdo = new Gibdo(getWidth() / 2, getHeight() / 2 + 50, 1000 / 16);
        octorok.setVelocity(2);
        dog.setVelocity(10);
        gibdo.setVelocity(2);
        octorok.setProjectile(new MudBall());
        minishLink.setProjectile(new EnergyBall());
        minishLink.setVelocity(3);
        GlobalCamera.getInstance().setOrigin(minishLink.getBounds(), getWidth(), getHeight());
        octorok.setHealth(100);
        octorok.setEnergy(100);
        minishLink.setHealth(100);
        minishLink.setEnergy(50);
        LifeHud.getInstance().setLife(minishLink);
        EnergyHud.getInstance().setEnergy(minishLink);
        healthBox = new BoundingBox((int) (getWidth() / 1.5), (int) (getHeight() / 1.5), 100, 100);
        damageBox = new BoundingBox((int) (getWidth() / 1.2), (int) (getHeight() / 1.2), 100, 100);
    }

    /**
     * Will scale the image up or down to the current map
     *
     * @return
     */
    @Override
    public int getWidth() {
        return 600;
    }

    /**
     * Will scale the image up or down to the current map
     *
     * @return
     */
    @Override
    public int getHeight() {
        return 600;
    }

}
