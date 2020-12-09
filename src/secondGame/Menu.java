package secondGame;

import groupproject.containers.zelda.ZeldaContainer;
import groupproject.gameengine.GameContainer;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Random;

public class Menu extends MouseAdapter {
    private ZeldaContainer zeldaGame;
    private Game game;
    private Handler handler;
    Random r = new Random();

    public Menu(Game game, Handler handler){
        this.game = game;
        this.handler = handler;
    }

    public Menu(ZeldaContainer zeldaGame, Handler handler){
        this.zeldaGame = zeldaGame;
        this.handler = handler;
    }
    public void mousePressed(MouseEvent e){
        int mx = e.getX();
        int my = e.getY();

        if (game.gameState == Game.STATE.Menu){
            // Game1
            if (mouseOver(mx, my,300,150,300,80)){
                game.gameState = Game.STATE.ZeldaGame;
                GameContainer zeldaGame = ZeldaContainer.frame(800, 800);
                try {
                    zeldaGame.start();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
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

        // Back Button in Help
        if (game.gameState == Game.STATE.Help){
            if (mouseOver(mx,my,300,450,300,80 )){
                game.gameState = Game.STATE.Menu;
                return;
            }
        }
        // Back Button in Game2
        if (game.gameState == Game.STATE.Game){
            if (mouseOver(mx,my,300,450,300,80 )){
                game.gameState = Game.STATE.Menu;
                Game newGame = new Game();
                HUD hud = new HUD();

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
