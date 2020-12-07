package secondGame;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Random;

public class Game extends Canvas implements Runnable{
    private String title;
    public static final int WIDTH = 900, HEIGHT=WIDTH/12*9;
    private Thread thread;
    private boolean running = false;
    private Handler handler;
    private Random r;
    private HUD hud;
    private Spawn spawn;
    private Menu menu;

    public enum STATE {
        Menu,
        Help,
        Game,
        ZeldaGame;
    }

    public STATE gameState = STATE.Menu;

    public Game(){
        handler = new Handler();
        menu = new Menu(this, handler);
        this.addKeyListener(new KeyInput(handler));
        this.addMouseListener(menu);

        new Display(WIDTH, HEIGHT,"Second Game", this);

        r = new Random();
        hud = new HUD();
        spawn = new Spawn(handler, hud);

        if(gameState == STATE.Game){
            handler.addObject(new Player(WIDTH/2-32,HEIGHT/2-32, ID.Player, handler));
            handler.addObject(new BasicEnemy(r.nextInt(WIDTH),r.nextInt(HEIGHT), ID.BasicEnemy, handler));
        }
    }

    @Override
    public void run() {
        double fps = 60.0;  //tickes per second
        double timePerTick = 1000000000 / fps; // nanoseconds per tick
        double delta = 0;
        long lastTime = System.nanoTime();
		long timer = 0;
		int ticks = 0;

        while(running) {
            long now = System.nanoTime();
            delta += (now - lastTime)/timePerTick; //0.00024++  ticks
			timer += now - lastTime;
            lastTime = now;

            if (delta >= 1) { // if delta reached 1 tick
                tick();
                render();
				ticks++;
                delta--;
            }

			if (timer >= 1000000000) {
				System.out.println("Ticks and Frames: " + ticks);
				ticks = 0;
				timer = 0;
			}
        }

        stop();
    }

    public synchronized void start(){
        if(running)
            return;
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop(){
        if (!running)
            return;
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void tick(){
        handler.tick();
        if(gameState == STATE.Game){
            hud.tick();
            spawn.tick();
        }else if (gameState == STATE.Menu){
            menu.tick();
        }else if (gameState == STATE.Help){
            menu.tick();
        }


    }

    public void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if(bs==null) {
            this.createBufferStrategy(3);
            return;
        }
        // call method to draw, it's a paint brush
        Graphics g = bs.getDrawGraphics();

        g.clearRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        handler.render(g);

        if(gameState == STATE.Game){
            hud.render(g);
        }else if (gameState == STATE.Menu){
            menu.render(g);
        }else if (gameState == STATE.Help){
            menu.render(g);
        }


        // call it to show on screen
        bs.show();
        g.dispose();
    }

    public static int clamp(int var, int min,int max){
        if(var>=max)
            return var = max;
        else if(var<=min)
            return var = min;
        else
            return var;
    }
}
