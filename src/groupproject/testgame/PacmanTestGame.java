package groupproject.testgame;

import groupproject.gameengine.GameContainer;
import groupproject.containers.PacmanContainer;

import java.io.IOException;

public class PacmanTestGame {

    public static void main(String[] args) throws IOException {
        System.setProperty("sun.java2d.opengl", "true");
        //GameContainer game = ExampleGameContainer.frame(800, 800);
        GameContainer game = PacmanContainer.frame();
        game.start();
    }
}
