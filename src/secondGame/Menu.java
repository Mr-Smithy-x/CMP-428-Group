package secondGame;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class Menu extends MouseAdapter {
    private Game game;
    private Handler handler;
    Random r = new Random();

    public Menu(Game game, Handler handler){
        this.game = game;
        this.handler = handler;
    }

    public void mousePressed(MouseEvent e){
        int mx = e.getX();
        int my = e.getY();

        if (game.gameState == Game.STATE.Menu){
            // Game1
            if (mouseOver(mx, my,300,150,300,80)){


            }
            // Game2
            if (mouseOver(mx, my,300,250,300,80)){
                game.gameState = Game.STATE.Game;
                handler.addObject(new Player(Game.WIDTH/2-32,Game.HEIGHT/2-32, ID.Player, handler));
                handler.addObject(new BasicEnemy(r.nextInt(Game.WIDTH),r.nextInt(Game.HEIGHT), ID.BasicEnemy, handler));
            }
            // Help Button
            if (mouseOver(mx, my, 300,350,300,80)){
                game.gameState = Game.STATE.Help;
            }
            // Quit Button
            if (mouseOver(mx, my,300,450,300,80)){
                System.exit(1);
            }
        }

        // Back Button
        if (game.gameState == Game.STATE.Help){
            if (mouseOver(mx,my,300,450,300,80 )){
                game.gameState = Game.STATE.Menu;
                return;
            }
        }
    }

    public void mouseReleased(MouseEvent e){

    }

    public boolean mouseOver(int mx, int my, int x, int y, int width, int height){
        if(mx > x && mx < x + width){
            if(my > y && my < y + height){
                return true;
            }else return false;
        }else return false;
    }

    public void tick(){

    }

    public void render(Graphics g) {
        if (game.gameState == Game.STATE.Menu){
            Font font = new Font("arial", 1, 50);
            // string menu
            g.setFont(font);
            g.setColor(Color.WHITE);
            g.drawString("MENU",375,100);
            // Game 1
            g.setFont(font);
            g.setColor(Color.WHITE);
            g.drawString("Game 1",365,210);
            // Game 2
            g.setFont(font);
            g.setColor(Color.WHITE);
            g.drawString("Game 2",365,310);
            // string help
            g.setFont(font);
            g.setColor(Color.WHITE);
            g.drawString("Help",395,410);
            // string quit
            g.setFont(font);
            g.setColor(Color.WHITE);
            g.drawString("Quit",395,510);

            // draw rectangles
            g.setColor(Color.WHITE);
            g.drawRect(300,150,300,80);
            g.setColor(Color.WHITE);
            g.drawRect(300,250,300,80);
            g.setColor(Color.WHITE);
            g.drawRect(300,350,300,80);
            g.setColor(Color.WHITE);
            g.drawRect(300,450,300,80);
        } else if (game.gameState == Game.STATE.Help){
            Font font1 = new Font("arial", 1, 50);
            Font font2 = new Font("arial", 1, 50);

            g.setFont(font1);
            g.setColor(Color.WHITE);
            g.drawString("HELP",375,100);

            g.setFont(font1);
            g.setColor(Color.WHITE);
            g.drawString("Back",395,510);

            g.setColor(Color.WHITE);
            g.drawRect(300,450,300,80);
        }
    }
}
