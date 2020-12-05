package secondGame;

import com.sun.org.apache.xpath.internal.axes.WalkingIteratorSorted;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable{
    private String title;
    private static final int WIDTH = 640, HEIGHT=WIDTH/12*9;
    private Thread thread;
    private boolean running = false;

    public Game(){
        new Display(WIDTH, HEIGHT,"Second Game", this);
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
        // call it to show on screen
        bs.show();
        g.dispose();
    }
}
