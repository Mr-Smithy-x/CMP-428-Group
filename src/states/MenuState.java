package states;

import groupproject.gameengine.GameContainer;

import java.awt.*;

public class MenuState extends State{

    public MenuState(GameContainer game) {
        super(game);
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.yellow);
        g.fillRect(0,0, game.getWidth(), game.getHeight());
    }
}
