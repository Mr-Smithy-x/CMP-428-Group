package groupproject.gameengine.hud;

import groupproject.gameengine.contracts.Renderable;

import java.awt.*;

public abstract class BaseHud implements Renderable {

    private Graphics hud;

    @Override
    public void render(Graphics graphics) {
        if (hud == null) {
            hud = graphics.create();
        }
        onRenderHud(hud, graphics);
    }

    /**
     * Parameter Hud is where you draw your actual graphics for the hud
     * This is intended for being able to utilize the clip functionality without
     * having to affect the main graphics for the game. using clip on parent
     * prevents parts of the screen from being render, so only clip onto the hud
     * otherwise, you can actually utilize the render function and keep this function empty
     *
     * @param hud    Hud in which you're able to draw on
     * @param parent the parent graphics in the event you need to draw onto the actual graphic
     */
    protected abstract void onRenderHud(Graphics hud, Graphics parent);

}
