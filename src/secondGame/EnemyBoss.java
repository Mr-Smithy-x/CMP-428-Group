package secondGame;

import java.awt.*;
import java.util.Random;

public class EnemyBoss extends GameObject {
    private Handler handler;
    private int timer1 = 100;
    private int timer2 = 100;
    Random r = new Random();

    public EnemyBoss(int x, int y, ID id, Handler handler) {
        super(x, y, id);
        this.handler = handler;
        velX = 0;
        velY = 2;
    }

    @Override
    public void tick() {
        x+=velX;
        y+=velY;
        if(timer1 <=0 ) velY = 0;
        else timer1--;
        if(timer1 <= 0 ) timer2--;
        if(timer2 <= 0){
            if(velX == 0) velX = 2;
//            velX += 0.001f;
//            int spawn = r.nextInt(10);
//            if(spawn == 0)
//                handler.addObject(new Bullet((int)x+96,(int)y+96, ID.BasicEnemy,handler));
        }
        if(x<=0||x>=Game.WIDTH-80){
            velX *= -1;
        }
//        handler.addObject(new Trail(x,y,ID.Trail,Color.RED,64,16,0.001f,handler));
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect((int)x,(int)y,64,64);

    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int)x,(int)y,64,64);
    }
}