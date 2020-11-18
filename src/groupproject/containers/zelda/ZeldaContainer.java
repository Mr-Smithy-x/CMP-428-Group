package groupproject.containers.zelda;

import groupproject.containers.zelda.models.Dog;
import groupproject.containers.zelda.models.Link;
import groupproject.gameengine.GameContainer;
import groupproject.gameengine.camera.GlobalCamera;
import groupproject.gameengine.sprite.Sprite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class ZeldaContainer extends GameContainer {

    Link link;
    Dog dog;

    @Override
    protected void onInitialize() throws IOException {
        link = new Link(getWidth() / 2, getHeight() / 2, 1000/16);
        dog = new Dog(getWidth() / 2 - 100, getHeight() / 2 - 50, 2);
        link.setVelocity(10);
        dog.setVelocity(10);
        GlobalCamera.getInstance().setOrigin(link, getWidth(), getHeight());
    }

    @Override
    protected void onPlay() {
        if (pressedKey[KeyEvent.VK_LEFT]) {
            link.setSpritePose(Sprite.Pose.LEFT);
            link.move();
        } else if (pressedKey[KeyEvent.VK_RIGHT]) {
            link.setSpritePose(Sprite.Pose.RIGHT);
            link.move();
        } else if (pressedKey[KeyEvent.VK_UP]) {
            link.setSpritePose(Sprite.Pose.UP);
            link.move();
        }else if (pressedKey[KeyEvent.VK_DOWN]) {
            link.setSpritePose(Sprite.Pose.DOWN);
            link.move();
        }else if(pressedKey[KeyEvent.VK_Z]){
            link.spin();
        }else if(pressedKey[KeyEvent.VK_SPACE]){
            link.attack();
        }
        if(link.isOverlapping(dog)){
            link.pushes(dog);
        }
        GlobalCamera.getInstance().setOrigin(link, getWidth(), getHeight());
    }

    @Override
    protected void onPaint(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());
        dog.render(g);
        link.render(g);
        GlobalCamera.getInstance().render(g, getContainer());
    }


    public static GameContainer frame(int width, int height) {
        JFrame frame = make("Zelda Test Game", width, height);
        Canvas canvas = make(width, height);
        frame.add(canvas);
        frame.pack();
        return new ZeldaContainer(frame, canvas);
    }

    protected ZeldaContainer(JFrame container, Canvas canvas) {
        super(container, canvas);
    }

}
