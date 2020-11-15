package groupproject.games;

import groupproject.base.GameContainer;
import groupproject.gameengine.camera.GlobalCamera;
import groupproject.gameengine.models.BoundingCircle;
import groupproject.gameengine.models.BoundingLine;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.io.IOException;

public class TestGameContainer extends GameContainer {


    BoundingCircle circle;
    BoundingCircle box;
    BoundingLine line;
    Link link;
    Dog dog;

    @Override
    protected void onInitialize() throws IOException {
        circle = new BoundingCircle(getWidth() / 2, getHeight() / 2, 100);
        link = new Link(getWidth() / 2, getHeight() / 2, 2);
        dog = new Dog(getWidth() / 2, getHeight() / 2 - 50, 2);

        box = new BoundingCircle(getWidth() / 2, getHeight() / 2 - 300, 100);
        line = new BoundingLine(getWidth(), getHeight() - 50, 0, getHeight() - 50);
        circle.setAcceleration(0, 0.9);
        circle.setVelocity(0, 0);
        circle.setDrag(0.99, 0.99);
        dog.setAcceleration(0, 0.9);
        dog.setVelocity(0, 0);
        dog.setDrag(0.99, 0.99);

        box.setAcceleration(0, 0.9);
        box.setVelocity(0, 0);
        box.setDrag(0.99, 0.99);
        GlobalCamera.getInstance().setOrigin(link, getWidth(), getHeight());
    }

    @Override
    protected void onPlay() {
        circle.gravitate();
        box.gravitate();
        dog.gravitate();

        if (pressing[LT]) {
            link.moveLeft(10);
            dog.moveLeft(10);
            GlobalCamera.getInstance().moveLeft(10);
        } else if (pressing[RT]) {

            dog.moveRight(10);
            link.moveRight(10);
            GlobalCamera.getInstance().moveRight(10);
        } else if (pressing[UP]) {
            link.moveUp(10);
            dog.moveUp(10);
            GlobalCamera.getInstance().moveUp(10);
        }else if (pressing[DN]) {
            link.moveDown(10);
            dog.moveDown(10);
            GlobalCamera.getInstance().moveDown(10);
        }

        if (link.overlaps(line)) {
            link.bounceOffLine(line);
        }
        if (dog.overlaps(line)) {
            dog.bounceOffLine(line);
        }

        if (circle.overlaps(line)) {
            circle.bounceOffLine(line);
        }
        if (box.overlaps(line)) {
            box.bounceOffLine(line);
        }

        if(link.overlaps(dog)){
            link.pushes(dog);
        }
        if (circle.overlaps(box)) {
            circle.bounceOff(box);
            circle.pushes(box);
        }
        if (box.overlaps(link)) {
            box.bounceOff(link);
            box.pushes(link);
        }

        if (circle.overlaps(link)) {
            circle.bounceOff(link);
            circle.pushes(link);
        }

        //Assert that the camera follows the main user regardless of whether it was pushed by another object
        GlobalCamera.getInstance().setOrigin(link, getWidth(), getHeight());
    }

    @Override
    protected void onPaint(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.white);
        line.render(g);
        g.setColor(Color.red);
        circle.render(g);
        box.render(g);
        dog.render(g);
        link.render(g);
    }


    public static GameContainer frame(int width, int height) {
        JFrame frame = make("Test Game", width, height);
        Canvas canvas = make(width, height);
        frame.add(canvas);
        frame.pack();
        return new TestGameContainer(frame, canvas);
    }

    public static GameContainer applet(Applet applet) {
        return new TestGameContainer(applet);
    }

    protected TestGameContainer(JFrame container, Canvas canvas) {
        super(container, canvas);
    }

    protected TestGameContainer(Applet container) {
        super(container);
    }

}
