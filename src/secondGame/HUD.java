package secondGame;

import java.awt.*;

public class HUD {
    public static int HEALTH = 100;
    private int greenValue = 255;
    private int score = 0;
    private int level = 1;

    public void tick(){
        HEALTH = Game.clamp(HEALTH, 0,100);
        greenValue = Game.clamp(greenValue, 0, 255);
        greenValue = HEALTH*2;
        score++;
    }
    public void render(Graphics g){
        g.setColor(Color.gray);
        g.fillRect(15,15,200,32);
        g.setColor(new Color(75,(int)greenValue,0));
        g.fillRect(15,15,(int)HEALTH*2,32);
        g.setColor(Color.white);
        g.drawRect(15,15,200,32);

        g.drawString("Score: "+score, 10,68);
        g.drawString("Level: "+level, 10, 88);
    }

    public static int getHEALTH() {
        return HEALTH;
    }

    public int getGreenValue() {
        return greenValue;
    }

    public int getScore() {
        return score;
    }

    public int getLevel() {
        return level;
    }

    public static void setHEALTH(int HEALTH) {
        HUD.HEALTH = HEALTH;
    }

    public void setGreenValue(int greenValue) {
        this.greenValue = greenValue;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
