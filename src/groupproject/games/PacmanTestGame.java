package groupproject.games;

import groupproject.gameengine.GameContainer;
import groupproject.containers.pacman.PacmanContainer;

import java.io.IOException;

public class PacmanTestGame {

    public static void main(String[] args) throws IOException {
        System.setProperty("sun.java2d.opengl", "true");
        GameContainer game = PacmanContainer.frame();
        game.start();
    }
}
