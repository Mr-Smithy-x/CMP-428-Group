package groupproject.containers.zelda;

import groupproject.containers.zelda.managers.MenuScreenManager;
import groupproject.containers.zelda.helpers.GameTextDialog;
import groupproject.containers.zelda.managers.MapManager;
import groupproject.containers.zelda.managers.TransitionManager;
import groupproject.containers.zelda.managers.ZeldaWorldManager;
import groupproject.containers.zelda.models.MinishLink;
import groupproject.containers.zelda.models.Octorok;
import groupproject.containers.zelda.sound.GlobalSoundTrack;
import groupproject.gameengine.GameContainer;
import groupproject.gameengine.camera.GlobalCamera;
import groupproject.gameengine.models.BoundingBox;
import groupproject.gameengine.sprite.AttackSprite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class ZeldaContainer extends GameContainer {


    /**
     * @see ZeldaWorldManager - This contains the gameplay of the actual world
     * In this class, handle minor things like pause menu, select menu etc
     * The actual game code will be in the World manager and its really simple :)
     */
    ZeldaWorldManager world = new ZeldaWorldManager(this);
    BoundingBox healthBox;
    BoundingBox damageBox;
    MenuScreenManager game;

    protected ZeldaContainer(JFrame container, JPanel panel) {
        super(container, panel);
        game = new MenuScreenManager(this);
    }

    public static GameContainer frame(int width, int height) {
        JFrame frame = make("Zelda Test Game", width, height);
        JPanel panel = make(width, height);
        frame.add(panel);
        frame.pack();
        return new ZeldaContainer(frame, panel);
    }

    @Override
    protected void onPlay() {
        world.adjust();
        if(TransitionManager.getInstance().shouldTrigger()){
            TransitionManager.getInstance().trigger();
        }
        if(TransitionManager.getInstance().isPlaying() && TransitionManager.getInstance().isFinishedTransitioning()) {
            world.automate();
            world.manual(pressedKey);
        }
        //You can remove these below
        if (world.getPlayer().isOverlapping(healthBox)) {
            world.getPlayer().incrementHealth(.5);
            world.getPlayer().incrementEnergy(.5, true);
        }
        if (world.getPlayer().isOverlapping(damageBox)) {
            world.getPlayer().damageHealth(.5);
            world.getPlayer().useEnergy(.25);
        }
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
        if (inDebuggingMode()) {
            GlobalCamera.getInstance().render(g, getContainer());
        }
        if (world.isPlayerDead()) {
            g.setColor(new Color(1, 1, 1, 0.4f));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setFont(caveatFont);
            g.setColor(new Color(255, 70, 70));
            drawTextCenteredOffset(g, GameTextDialog.PLAYER_DIED, 0, getHeight() / 4);
        }
        TransitionManager.getInstance().render(g);
    }

    @Override
    protected void onInitialize() throws IOException {
        MapManager.getInstance().loadTileMap("hyrule_castle/castle_garden");
        world.setPlayer(new MinishLink(360, 300, 1000 / 24));
        world.getPlayer().setVelocity(4);
        world.addEnemy(new Octorok(getWidth() / 2, 300, 1000 / 16));
        world.addEnemy(new Octorok(getWidth() / 2 - 100, 300, 2));
        healthBox = new BoundingBox((int) (getWidth() / 1.5), (int) (getHeight() / 1.5), 100, 100);
        damageBox = new BoundingBox((int) (getWidth() / 1.2), (int) (getHeight() / 1.2), 100, 100);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        super.keyReleased(e);
        if (e.getKeyCode() == KeyEvent.VK_J) {
            world.calculatePath(world.getEnemies().get(1));
        }
        if (e.getKeyCode() == KeyEvent.VK_P) {
            if (!playing) {
                game.setState(MenuScreenManager.State.Pause);
            }
        }
    }


    @Override
    protected void onPausePaint(Graphics g) {
        game.render(g);
    }

    /**
     * Will scale the image up or down to the current map
     *
     * @return
     */
    @Override
    public int getWidth() {
        return (int) (450 * GlobalCamera.getInstance().getScaling());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        if (!playing) {
            game.onEvent(e);
        } else {
            AttackSprite player = world.getPlayer();
            boolean playing = TransitionManager.getInstance().isPlaying();
            System.out.println(playing);
            TransitionManager.getInstance().setPlaying(!playing);
            System.out.println(TransitionManager.getInstance().isPlaying());
        }
    }

    /**
     * Will scale the image up or down to the current map
     *
     * @return
     */
    @Override
    public int getHeight() {
        return (int) (450 * GlobalCamera.getInstance().getScaling());
    }

}