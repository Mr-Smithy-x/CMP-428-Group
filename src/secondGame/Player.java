package secondGame;

import java.awt.*;
import java.util.Random;

public class Player extends GameObject{
    Random r = new Random();
    Handler handler;
    public Player(int x, int y, ID id, Handler handler) {
        super(x, y, id);
        this.handler = handler;
    }

    @Override
    public void tick() {
        x += velX;
        y += velY;
        x = Game.clamp(x,0,Game.WIDTH-48);
        y = Game.clamp(y,0,Game.HEIGHT-80);
        handler.addObject(new Trail(x,y,ID.Trail,Color.white,32,32,0.02f,handler));
        collision();
    }

    public void collision(){
        for(int i=0; i<handler.objects.size();i++){
            GameObject temObject = handler.objects.get(i);
            if(temObject.getId()==ID.BasicEnemy){
                if(getBounds().intersects(temObject.getBounds())){
                    HUD.HEALTH -= 2;
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g.setColor(Color.green);
        g2d.draw(getBounds());

        g.setColor(Color.WHITE);
        g.fillRect(x, y, 32, 32);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x,y, 32, 32);
    }
}
