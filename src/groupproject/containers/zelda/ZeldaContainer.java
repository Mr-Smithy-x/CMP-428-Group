package groupproject.containers.zelda;

import groupproject.containers.zelda.helpers.GameTextDialog;
import groupproject.containers.zelda.helpers.BaseWorldManager;
import groupproject.containers.zelda.helpers.ZeldaWorldManager;
import groupproject.containers.zelda.models.Dog;
import groupproject.containers.zelda.models.MinishLink;
import groupproject.containers.zelda.models.Octorok;
import groupproject.containers.zelda.sound.GlobalSoundTrack;
import groupproject.gameengine.GameContainer;
import groupproject.gameengine.camera.GlobalCamera;
import groupproject.gameengine.models.BoundingBox;
import groupproject.games.ZeldaTestGame;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ZeldaContainer extends GameContainer {

    ZeldaWorldManager world = new ZeldaWorldManager(this);
    BoundingBox healthBox;
    BoundingBox damageBox;

    protected ZeldaContainer(JFrame container, Canvas canvas) {
        super(container, canvas);
    }

    public static GameContainer frame(int width, int height) {
        JFrame frame = make("Zelda Test Game", width, height);
        Canvas canvas = make(width, height);
        frame.add(canvas);
        frame.pack();
        return new ZeldaContainer(frame, canvas);
    }

    @Override
    protected void onPlay() {
        world.automate();
        world.adjust();
        world.manual(pressedKey);
    }

    @Override
    protected void onPause() {
        super.onPause();
        GlobalSoundTrack.getInstance().setTrack(GlobalSoundTrack.Track.PAUSE);
        GlobalSoundTrack.getInstance().play();
    }

    @Override
    protected void onPaint(Graphics g) {
        world.render(g);
        g.setColor(Color.GREEN);
        healthBox.render(g);
        g.setColor(Color.RED);
        damageBox.render(g);
        if (ZeldaTestGame.inDebuggingMode()) {
            GlobalCamera.getInstance().render(g, getContainer());
        }
        if (world.isPlayerDead()) {
            g.setColor(new Color(1, 1, 1, 0.4f));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setFont(caveatFont);
            g.setColor(new Color(255, 70, 70));
            drawTextCenteredOffset(g, GameTextDialog.PLAYER_DIED, 0, getHeight() / 4);
        }
    }

    @Override
    protected void onInitialize() throws IOException {
        world.setTileMap(loadTileMap("forest_test.tilemap"));
        world.setPlayer(new MinishLink(getWidth() / 2, getHeight() / 2, 1000 / 16));
        world.addEnemy(new Octorok(getWidth() / 2, getHeight() / 2, 1000 / 16));
        world.addEnemy(new Dog(getWidth() / 2 - 100, getHeight() / 2 - 50, 2));
        GlobalCamera.getInstance().setOrigin(world.getPlayer(), getWidth(), getHeight());
        healthBox = new BoundingBox((int) (getWidth() / 1.5), (int) (getHeight() / 1.5), 100, 100);
        damageBox = new BoundingBox((int) (getWidth() / 1.2), (int) (getHeight() / 1.2), 100, 100);
    }

    /**
     * Will scale the image up or down to the current map
     *
     * @return
     */
    @Override
    public int getWidth() {
        return 600;
    }

    /**
     * Will scale the image up or down to the current map
     *
     * @return
     */
    @Override
    public int getHeight() {
        return 600;
    }

}
