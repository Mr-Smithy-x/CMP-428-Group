package secondGame;

import java.awt.*;

public class SmartEnemy extends GameObject {
    private Handler handler;
    private GameObject player;

    public SmartEnemy(int x, int y, ID id, Handler handler) {
        super(x, y, id);
        this.handler = handler;

        for(int i=0; i<handler.objects.size(); i++){
            if(handler.objects.get(i).getId()==ID.Player){
                player = handler.objects.get(i);
            }
        }

    }

    @Override
    public void tick() {
        x+=velX;
        y+=velY;

        float diffX = x - player.getX() - 8;
        float diffY = y - player.getY() - 8;
        float distance = (float) Math.sqrt
                (
                        (x - player.getX())*(x - player.getX()) +
                        (y - player.getY())*(y - player.getY())
                );

        velX = (int) ((-1.0/distance)*diffX);
        velY = (int) ((-1.0/distance)*diffY);

        if(x<=0||x>=Game.WIDTH-32) velX*=-1;
        if(y<=0||y>=Game.HEIGHT-32) velY*=-1;
        handler.addObject(new Trail((int)x,(int)y,ID.Trail,Color.GREEN,16,16,0.01f,handler));
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect((int)x,(int)y,16,16);

    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int)x,(int)y,16,16);
    }
}