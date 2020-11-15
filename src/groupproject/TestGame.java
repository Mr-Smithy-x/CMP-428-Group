package groupproject;

import groupproject.base.GameContainer;
import groupproject.games.TestGameContainer;

import java.io.IOException;

public class TestGame {



    public static void main(String[] args) throws IOException {
        System.setProperty("sun.java2d.opengl", "true");
        GameContainer game = TestGameContainer.frame(800, 800);
        game.start();
    }
}
