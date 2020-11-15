package groupproject;

import groupproject.base.GameContainer;
import groupproject.games.TestGameContainer;

public class TestGame {



    public static void main(String[] args){
        System.setProperty("sun.java2d.opengl", "true");
        GameContainer game = TestGameContainer.frame(800, 800);
        game.start();
    }
}
