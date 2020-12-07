package secondGame;

import java.awt.*;
import java.util.Random;

public class Bullet extends GameObject {
    private Handler handler;
    Random r = new Random();

    public Bullet(int x, int y, ID id, Handler handler) {
        super(x, y, id);
        this.handler = handler;

        velX = (r.nextInt()-5);
        velY = 5;
    }

    @Override
    public void tick() {
        x+=velX;
        y+=velY;

        if(y>=Game.HEIGHT) handler.removeObject(this);
        handler.addObject(new Trail((int)x,(int)y,ID.Trail,Color.WHITE,16,16,0.01f,handler));
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect((int)x,(int)y,16,16);

    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int)x,(int)y,16,16);
    }
}