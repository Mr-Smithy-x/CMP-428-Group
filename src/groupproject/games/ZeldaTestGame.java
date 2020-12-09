package groupproject.games;

import groupproject.containers.zelda.ZeldaContainer;
import groupproject.gameengine.GameContainer;

import java.io.IOException;

public class ZeldaTestGame {

    public static void main(String[] args) throws IOException {
        System.setProperty("sun.java2d.opengl", "true");
        System.setProperty("DEBUG", "true");
        GameContainer game = ZeldaContainer.frame(800, 800);
        game.start();
    }

    public static boolean inDebuggingMode() {
        return System.getProperty("DEBUG", "false").equals("true");
    }

}
