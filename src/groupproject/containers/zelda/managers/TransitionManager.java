package groupproject.containers.zelda.managers;

import groupproject.containers.zelda.models.TransitionTile;
import groupproject.containers.zelda.sound.GlobalSoundEffect;
import groupproject.containers.zelda.sound.GlobalSoundTrack;
import groupproject.gameengine.camera.GlobalCamera;
import groupproject.gameengine.contracts.Renderable;
import groupproject.gameengine.hud.BaseHud;
import groupproject.gameengine.sprite.Sprite;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Timer;
import java.util.TimerTask;

public class TransitionManager extends BaseHud implements Renderable {

    private Sprite sprite;
    private TransitionTile tile;

    public void setFor(Sprite sprite, TransitionTile tile) {
        setPlaying(false);
        this.sprite = sprite;
        this.tile = tile;
    }

    interface OnTransition {
        void onTransition(Sprite sprite, TransitionTile tile);
    }

    private final Rectangle base;
    private Ellipse2D circle;
    private static TransitionManager instance;
    private boolean playing = true;
    private int max_bounds = 0;
    private int min_bounds = 0;
    private Timer transitionTimer;
    private TimerTask transitionTask;
    private OnTransition transition;


    public void setTransition(OnTransition transition) {
        this.transition = transition;
    }

    public OnTransition getTransition() {
        return transition;
    }

    private TransitionManager() {
        this.base = new Rectangle(0, 0, (int) GlobalCamera.getInstance().getWidth(), (int) GlobalCamera.getInstance().getHeight());
        this.circle = new Ellipse2D() {
            private Rectangle rect = new Rectangle(0, 0, 0, 0);

            @Override
            public double getX() {
                return rect.x;
            }

            @Override
            public double getY() {
                return rect.y;
            }

            @Override
            public double getWidth() {
                return rect.width;
            }

            @Override
            public double getHeight() {
                return rect.height;
            }

            @Override
            public boolean isEmpty() {
                return rect.width <= 0 && rect.height <= 0;
            }

            @Override
            public void setFrame(double x, double y, double w, double h) {
                this.rect.setFrame(x, y, w, h);
            }

            @Override
            public Rectangle2D getBounds2D() {
                return rect;
            }
        };
        this.max_bounds = (int) (Math.sqrt(base.width * base.width + base.height * base.height));
        transitionTimer = new Timer();
        transitionTask = new TransitionTask();
        transitionTimer.scheduleAtFixedRate(transitionTask, 0, 3);
    }


    public static TransitionManager getInstance() {
        if (instance == null) {
            instance = new TransitionManager();
        }
        return instance;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
        if (!playing) {
            set((int) circle.getWidth() - 1);
            GlobalSoundEffect.getInstance().play(GlobalSoundEffect.Clips.WARPROOMCHANGE);
            GlobalSoundTrack.getInstance().pause();
        } else {
            set((int) circle.getWidth() + 1);
            GlobalSoundTrack.getInstance().resume();
        }
    }

    public boolean isPlaying() {
        return playing;
    }

    public boolean canTrigger() {
        return isFinishedTransitioning() && !isPlaying();
    }

    public void trigger() {
        if (canTrigger()) {
            if (sprite != null && tile != null) {
                transition.onTransition(sprite, tile);
                sprite = null;
                tile = null;
            }
            TransitionManager.getInstance().setPlaying(true);
        } else {
            TransitionManager.getInstance().setPlaying(false);
        }
    }

    public boolean isFinishedTransitioning() {
        return ((int) circle.getWidth()) == max_bounds || ((int) circle.getWidth()) == min_bounds;
    }


    private Area calculateRectOutside(Shape r) {
        Area outside = new Area(new Rectangle2D.Double(0, 0, base.getWidth(), base.getHeight()));
        outside.subtract(new Area(r));
        return outside;
    }

    @Override
    protected void onRenderHud(Graphics hud, Graphics parent) {
        hud.setColor(Color.BLACK);
        hud.fillRect(base.x, base.y, base.width, base.height);
        hud.setClip(calculateRectOutside(circle));
    }

    public void set(int size) {
        circle.setFrame(GlobalCamera.getInstance().getWidth() / 2 - size / 2, GlobalCamera.getInstance().getHeight() / 2 - size / 2, size, size);
    }


    public boolean shouldTrigger() {
        return ((int)circle.getWidth()) <= min_bounds;
    }

    class TransitionTask extends TimerTask {

        @Override
        public void run() {
            double size;
            if (playing) {
                size = Math.min(Math.min(circle.getWidth() + 2, max_bounds), max_bounds);
             } else {
                size = Math.max(Math.max(circle.getWidth() - 2, min_bounds), min_bounds);
            }
            set((int) size);
        }
    }
}
