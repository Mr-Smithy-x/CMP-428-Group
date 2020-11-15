package groupproject.games;

import groupproject.base.GameContainer;
import groupproject.gameengine.camera.GlobalCamera;
import groupproject.gameengine.models.BoundingCircle;
import groupproject.gameengine.models.BoundingLine;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;

public class TestGameContainer extends GameContainer {


    BoundingCircle circle;
    BoundingLine line;

    @Override
    protected void onInitialize() {
        circle = new BoundingCircle(getWidth()/2, getHeight()/2, 50);
        line = new BoundingLine(getWidth(), getHeight() - 50,  0, getHeight() - 50);
        circle.setAcceleration(0, 0.9);
        circle.setVelocity(0, 0);
        circle.setDrag(0.3, 0.01);
        GlobalCamera.getInstance().setOrigin(circle, getWidth(), getHeight());
    }

    @Override
    protected void onPlay() {
        if(pressing[LT]){
            circle.moveLeft(10);
            GlobalCamera.getInstance().moveLeft(10);
        }else if(pressing[RT]){
            circle.moveRight(10);
            GlobalCamera.getInstance().moveRight(10);
        }
        if(circle.overlaps(line)){
            circle.bounceOffLine(line);
            circle.pushedBackBy(line);
        }

        circle.gravitate();
        //Assert that the camera follows the main user regardless of whether it was pushed by another object
        //GlobalCamera.getInstance().setOrigin(circle, getWidth(), getHeight());
    }

    @Override
    protected void onPaint(Graphics g) {
        g.setColor(Color.black);
        line.render(g);
        g.setColor(Color.red);
        circle.render(g);
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
