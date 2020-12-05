package states;

import groupproject.gameengine.GameContainer;

import java.awt.*;

public abstract class State {

    private static State currentState = null;
    public static void setState(State state){
        currentState = state;
    }

    public static State getState(){
        return currentState;
    }

    protected GameContainer game;
    public State(GameContainer game){
        this.game = game;
    }

    public abstract void tick();

    public abstract void render(Graphics g);
}
