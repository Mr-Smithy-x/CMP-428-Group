package groupproject.base;



import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public abstract class GameContainer implements Runnable, KeyListener, MouseListener, MouseMotionListener {

    Font timesRoman = new Font("TimesNewRoman", Font.PLAIN, 32);
    Thread t;
    protected boolean[] pressing = new boolean[1024];
    boolean playing = true;

    /**
     * Mouse Variables
     */
    protected int mx = 0, my = 0;
    private Image off_screen_image;
    private Component container;
    protected Graphics off_g;


    protected GameContainer(JFrame container, Canvas canvas) {
        this.container = canvas;
        off_screen_image = canvas.createImage(canvas.getWidth(), canvas.getHeight());
        off_g = off_screen_image.getGraphics();
        off_g.setFont(timesRoman);
        container.requestFocus();
        container.addKeyListener(this);
        container.addMouseListener(this);
        container.addMouseMotionListener(this);
    }

    protected GameContainer(Applet container) {
        this.container = container;
        off_screen_image = container.createImage(container.getWidth(), container.getHeight());
        off_g = off_screen_image.getGraphics();
        off_g.setFont(timesRoman);
        container.requestFocus();
        container.addKeyListener(this);
        container.addMouseListener(this);
        container.addMouseMotionListener(this);
    }


    protected abstract void onPlay();
    protected abstract void onPaint(Graphics g);
    protected abstract void onInitialize() throws IOException;


    public void start() throws IOException {
        onInitialize();
        t = new Thread(this);
        t.start();
    }

    protected void onStart() {

    }

    protected void onPause() {

    }

    protected void onStop() {

    }


    protected void onPausePaint(Graphics g){
        g.setColor(new Color(0f,0f,0f,0.3f));
        g.fillRect(0, 0, getWidth(), getHeight());
        String paused = "Paused";
        int width = g.getFontMetrics().stringWidth(paused) / 2;
        g.drawString(paused, getWidth()/2 - width, getHeight()/2);
    }



    public void onRepaint() {
        update(container.getGraphics());
    }



    public int getWidth() {
        return container.getWidth();
    }

    public int getHeight() {
        return container.getWidth();
    }

    public Component getContainer() {
        return container;
    }

    protected void update(Graphics g) {
        off_g.clearRect(0, 0, container.getWidth(), container.getHeight());
        if(playing) {
            onPaint(off_g);
        }else{
            onPaint(off_g);
            onPausePaint(off_g);
        }
        g.drawImage(off_screen_image, 0, 0, null);
    }


    @Override
    public void run() {
        onStart();
        while (true) {
            if(playing) {
                onPlay();
            }else{
                onPause();
            }
            onRepaint();
            try {
                t.sleep(16);
            } catch (InterruptedException x) {
                onStop();
            }
        }
    }


    public void mouseMoved(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        pressing[e.getKeyCode()] = true;
    }

    public void keyReleased(KeyEvent e) {
        pressing[e.getKeyCode()] = false;
        if(e.getKeyCode() == _P){
            playing = !playing;
        }
    }

    public final void keyTyped(KeyEvent e) {
    }


    public static final int UP = KeyEvent.VK_UP;
    public static final int DN = KeyEvent.VK_DOWN;
    public static final int LT = KeyEvent.VK_LEFT;
    public static final int RT = KeyEvent.VK_RIGHT;

    public static final int _A = KeyEvent.VK_A;
    public static final int _B = KeyEvent.VK_B;
    public static final int _C = KeyEvent.VK_C;
    public static final int _D = KeyEvent.VK_D;
    public static final int _E = KeyEvent.VK_E;
    public static final int _F = KeyEvent.VK_F;
    public static final int _G = KeyEvent.VK_G;
    public static final int _H = KeyEvent.VK_H;
    public static final int _I = KeyEvent.VK_I;
    public static final int _J = KeyEvent.VK_J;
    public static final int _K = KeyEvent.VK_K;
    public static final int _L = KeyEvent.VK_L;
    public static final int _M = KeyEvent.VK_M;
    public static final int _N = KeyEvent.VK_N;
    public static final int _O = KeyEvent.VK_O;
    public static final int _P = KeyEvent.VK_P;
    public static final int _Q = KeyEvent.VK_Q;
    public static final int _R = KeyEvent.VK_R;
    public static final int _S = KeyEvent.VK_S;
    public static final int _T = KeyEvent.VK_T;
    public static final int _U = KeyEvent.VK_U;
    public static final int _V = KeyEvent.VK_V;
    public static final int _W = KeyEvent.VK_W;
    public static final int _X = KeyEvent.VK_X;
    public static final int _Y = KeyEvent.VK_Y;
    public static final int _Z = KeyEvent.VK_Z;

    public static final int _1 = KeyEvent.VK_1;
    public static final int _2 = KeyEvent.VK_2;
    public static final int _3 = KeyEvent.VK_3;
    public static final int _4 = KeyEvent.VK_4;
    public static final int _5 = KeyEvent.VK_5;
    public static final int _6 = KeyEvent.VK_6;
    public static final int _7 = KeyEvent.VK_7;
    public static final int _8 = KeyEvent.VK_8;
    public static final int _9 = KeyEvent.VK_9;

    public static final int CTRL = KeyEvent.VK_CONTROL;
    public static final int SHFT = KeyEvent.VK_SHIFT;
    public static final int ALT = KeyEvent.VK_ALT;

    public static final int SPACE = KeyEvent.VK_SPACE;

    public static final int COMMA = KeyEvent.VK_COMMA;
    public static final int PERIOD = KeyEvent.VK_PERIOD;
    public static final int SLASH = KeyEvent.VK_SLASH;
    public static final int SEMICOLON = KeyEvent.VK_SEMICOLON;
    public static final int COLON = KeyEvent.VK_COLON;
    public static final int QUOTE = KeyEvent.VK_QUOTE;


    protected static JFrame make(String title, int width, int height){
        JFrame frame = new JFrame("Test Game");
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        return frame;
    }

    protected static Canvas make(int width, int height){
        Canvas canvas = new Canvas();
        canvas.setFocusable(true);
        canvas.setFocusTraversalKeysEnabled(true);
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setMaximumSize(new Dimension(width, height));
        canvas.setMinimumSize(new Dimension(width, height));
        canvas.setFocusable(false);
        return canvas;
    }

}
