package groupproject.containers.zelda.managers;

import groupproject.gameengine.GameContainer;

import java.awt.*;
import java.awt.event.MouseEvent;

public class MenuScreenManager {

    private GameContainer container;
    private State state = State.Pause;

    public enum State {
        Pause,
        Help
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public MenuScreenManager(GameContainer container) {
        this.container = container;
    }

    public void onEvent(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();


        int midX = container.getComponentMidX();
        int midY = container.getComponentMidY();
        int paddingY = (midX / 4);
        // draw rectangles
        int rectWidth = 200;
        int rectY = midY / 4;
        int centerRectPos = midX - rectWidth / 2;
        int height = 60;
        // Play
        int padding = paddingY;
        int y1 = (rectY + (paddingY * 3) - padding);
        int y2 = (rectY + (paddingY * 4) - padding);
        int y3 = (rectY + (paddingY * 5) - padding);
        if (mouseOver(mx, my, centerRectPos, y1, rectWidth, height)) {
            if (state == State.Pause) {
                container.setPlaying();
            }else{
                state = State.Pause;
            }
        } else if (mouseOver(mx, my, centerRectPos, y2, rectWidth, height)) {
            if(state == State.Pause) {
                state = State.Help;
            }
        } else if (mouseOver(mx, my, centerRectPos, y3, rectWidth, height)) {
            if(state == State.Pause) {
                System.exit(0);
                System.out.println("OK");
            }
        }
    }


    public boolean mouseOver(int mx, int my, int x, int y, int width, int height) {
        if (mx > x && mx < x + width) {
            if (my > y && my < y + height) {
                return true;
            } else return false;
        } else return false;
    }

    public void render(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, container.getWidth(), container.getHeight());
        if (state == State.Pause) {
            int paddingY = (container.getMidY() / 4);
            g.setColor(Color.WHITE);
            container.drawTextCenteredOffset(g, "Menu", 0, container.getMidY() - paddingY);
            // Game 1
            container.drawTextCenteredOffset(g, "Continue", 0, container.getMidY() - (paddingY) * 3);
            // string help
            container.drawTextCenteredOffset(g, "Help", 0, container.getMidY() - (paddingY) * 4);
            // string quit
            container.drawTextCenteredOffset(g, "Quit", 0, container.getMidY() - (paddingY) * 5);

            // draw rectangles
            int rectWidth = 200;
            int rectY = container.getMidY() / 4;
            int centerRectPos = container.getMidX() - rectWidth / 2;
            int height = 60;
            g.drawRect(centerRectPos, (rectY += paddingY * 2) - paddingY / 2 +
                    g.getFontMetrics().getHeight() / 4, rectWidth, height);
            g.drawRect(centerRectPos, (rectY += paddingY) - paddingY / 2 +
                    g.getFontMetrics().getHeight() / 4, rectWidth, height);
            g.drawRect(centerRectPos, (rectY += paddingY) - paddingY / 2 +
                    g.getFontMetrics().getHeight() / 4, rectWidth, height);
        } else if (state == State.Help) {
            int paddingY = (container.getMidY() / 4);
            g.setColor(Color.WHITE);
            container.drawTextCenteredOffset(g, "Help", 0, container.getMidY() - paddingY);
            // Game 1
            container.drawTextCenteredOffset(g, "Back", 0, container.getMidY() - (paddingY) * 3);

            // draw rectangles
            int rectWidth = 200;
            int rectY = container.getMidY() / 4;
            int centerRectPos = container.getMidX() - rectWidth / 2;
            int height = 60;
            g.drawRect(centerRectPos, (rectY += paddingY * 2) - paddingY / 2 +
                    g.getFontMetrics().getHeight() / 4, rectWidth, height);

        }
    }
}
