package groupproject.containers.zelda;

import groupproject.containers.zelda.helpers.GameTextDialog;
import groupproject.containers.zelda.hud.EnergyHud;
import groupproject.containers.zelda.hud.LifeHud;
import groupproject.containers.zelda.models.Dog;
import groupproject.containers.zelda.models.Link;
import groupproject.gameengine.GameContainer;
import groupproject.gameengine.camera.GlobalCamera;
import groupproject.gameengine.models.BoundingBox;
import groupproject.gameengine.sprite.Sprite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class ZeldaContainer extends GameContainer {

    Link link;
    Dog dog;
    BoundingBox healthBox;
    BoundingBox damageBox;

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
        if (pressedKey[KeyEvent.VK_D]) {
            link.damageHealth(1);
        } else if (pressedKey[KeyEvent.VK_F]) {
            link.incrementHealth(1);
        }
        if (!link.isDead()) {
            if (pressedKey[KeyEvent.VK_LEFT]) {
                link.setSpritePose(Sprite.Pose.LEFT);
                link.move();
            } else if (pressedKey[KeyEvent.VK_RIGHT]) {
                link.setSpritePose(Sprite.Pose.RIGHT);
                link.move();
            } else if (pressedKey[KeyEvent.VK_UP]) {
                link.setSpritePose(Sprite.Pose.UP);
                link.move();
            } else if (pressedKey[KeyEvent.VK_DOWN]) {
                link.setSpritePose(Sprite.Pose.DOWN);
                link.move();
            }

            if (link.hasEnergy()) {
                if (pressedKey[KeyEvent.VK_Z]) {
                    link.spin();
                    link.useEnergy(.5);
                } else if (pressedKey[KeyEvent.VK_SPACE]) {
                    link.attack();
                    link.useEnergy(.1);
                }
            }

            if (link.isOverlapping(dog)) {
                link.pushes(dog);
            }

            if (healthBox.isOverlapping(link)) {
                link.incrementHealth(.1);
                link.incrementEnergy(.05);
            }
            if (damageBox.isOverlapping(link)) {
                link.damageHealth(.2);
                link.useEnergy(.1);
            }
        }
        GlobalCamera.getInstance().setOrigin(link, getWidth(), getHeight());
    }

    @Override
    protected void onPaint(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());
        dog.render(g);
        link.render(g);
        g.setColor(Color.GREEN);
        healthBox.render(g);
        g.setColor(Color.RED);
        damageBox.render(g);
        LifeHud.getInstance().render(g);
        EnergyHud.getInstance().render(g);
        GlobalCamera.getInstance().render(g, getContainer());
        if (link.isDead()) {
            g.setColor(new Color(1, 1, 1, 0.4f));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setFont(caveatFont);
            g.setColor(new Color(255, 70, 70));
            drawTextCenteredOffset(g, GameTextDialog.PLAYER_DIED, 0, getHeight() / 4);
        }
    }

    @Override
    protected void onInitialize() throws IOException {
        link = new Link(getWidth() / 2, getHeight() / 2, 1000 / 16);
        dog = new Dog(getWidth() / 2 - 100, getHeight() / 2 - 50, 2);
        link.setVelocity(10);
        dog.setVelocity(10);
        GlobalCamera.getInstance().setOrigin(link, getWidth(), getHeight());
        link.setHealth(100);
        link.setEnergy(50);
        LifeHud.getInstance().setLife(link);
        EnergyHud.getInstance().setEnergy(link);
        healthBox = new BoundingBox((int) (getWidth() / 1.5), (int) (getHeight() / 1.5), 100, 100);
        damageBox = new BoundingBox((int) (getWidth() / 1.2), (int) (getHeight() / 1.2), 100, 100);
    }
}
