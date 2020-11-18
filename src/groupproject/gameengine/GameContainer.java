package groupproject.gameengine;


import groupproject.gameengine.tile.TileMap;
import groupproject.gameengine.tile.TileMapModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class GameContainer implements Runnable, KeyListener, MouseListener, MouseMotionListener {

    protected final transient Logger logger = Logger.getLogger("GameEngine", null);
    private final Font mono = new Font(Font.MONOSPACED, Font.BOLD, 16);
    private final Component container;
    protected boolean[] pressedKey = new boolean[255];
    protected boolean playing = true;
    /**
     * Mouse Variables
     */
    protected int mx = 0, my = 0;
    protected Graphics off_g;
    private boolean isRunning = true;
    private Thread t;
    private Image off_screen_image;


    protected GameContainer(JFrame container, Canvas canvas) {
        this.container = canvas;
        container.requestFocus();
        container.addKeyListener(this);
        container.addMouseListener(this);
        container.addMouseMotionListener(this);
    }

    protected static JFrame make(String title, int width, int height) {
        JFrame frame = new JFrame("Test Game");
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        return frame;
    }

    protected static Canvas make(int width, int height) {
        Canvas canvas = new Canvas();
        canvas.setFocusable(true);
        canvas.setFocusTraversalKeysEnabled(true);
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setMaximumSize(new Dimension(width, height));
        canvas.setMinimumSize(new Dimension(width, height));
        canvas.setFocusable(false);
        return canvas;
    }

    public void start() throws IOException {
        onInitialize();
        onCreateImages();
        t = new Thread(this);
        t.start();
    }

    /*
     * Every game requires objects of some sort, implement that logic in this method.
     */
    protected abstract void onInitialize() throws IOException;

    protected void onCreateImages() {
        off_screen_image = container.createImage(getWidth(), getHeight());
        off_g = off_screen_image.getGraphics();
        off_g.setFont(mono);
    }

    /**
     * Default Scaled Width
     *
     * @return
     */
    public int getWidth() {
        return container.getWidth();
    }

    /**
     * Default Scaled Height
     *
     * @return
     */
    public int getHeight() {
        return container.getWidth();
    }

    public Component getContainer() {
        return container;
    }

    @Override
    public void run() {
        onStart();
        while (isRunning) {
            if (playing) {
                onPlay();
            } else {
                onPause();
            }
            onRepaint();
            try {
                Thread.sleep(16); // should result in 60FPS.
            } catch (InterruptedException x) {
                logger.log(Level.SEVERE, x.getMessage());
                break;
            }
        }
        onStop();
    }

    protected void onStart() {

    }

    /*
     * Main logic for the game, up to you to implement this in your game of course.
     */
    protected abstract void onPlay();

    protected void onPause() {

    }

    private void onRepaint() {
        update(container.getGraphics());
    }

    protected void onStop() {

    }

    protected void update(Graphics g) {
        off_g.clearRect(0, 0, container.getWidth(), container.getHeight());
        if (playing) {
            onPaint(off_g);
        } else {
            onPaint(off_g);
            onPausePaint(off_g);
        }
        g.drawImage(off_screen_image, 0, 0, container.getWidth(), container.getHeight(), null);
        g.dispose();
    }

    protected abstract void onPaint(Graphics g);

    /**
     * Paint the paused screen here!
     *
     * @param g
     */
    protected void onPausePaint(Graphics g) {
        g.setColor(new Color(0f, 0f, 0f, 0.3f));
        g.fillRect(0, 0, getWidth(), getHeight());
        String paused = "Paused";
        int width = g.getFontMetrics().stringWidth(paused) / 2;
        g.drawString(paused, getWidth() / 2 - width, getHeight() / 2 + g.getFontMetrics().getHeight() / 2);
    }

    public void stop() {
        isRunning = false;
        if (t != null) {
            t.interrupt();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
        /*
         * Will be implemented later if needed.
         */
    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKey[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKey[e.getKeyCode()] = false;
        if (e.getKeyCode() == KeyEvent.VK_P) {
            playing = !playing;
        }
    }

    public TileMap loadTileMap(String mapFile) {
        try (FileInputStream fis = new FileInputStream(TileMapModel.MAP_FOLDER + mapFile); ObjectInputStream is = new ObjectInputStream(fis)) {
            TileMapModel mapModel = (TileMapModel) is.readObject();
            return new TileMap(mapModel);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
