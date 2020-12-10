package groupproject.gameengine.algorithms.models;

import java.util.Set;

public abstract class Node<N extends Node> {

    private N parent;
    private Set<N> neighbours;
    private double cost, heuristic, function;
    private boolean valid;

    //region Getters
    public double getHeuristic() {
        return heuristic;
    }

    public double getFunction() {
        return function;
    }

    public double getCost() {
        return cost;
    }

    public Set<N> getNeighbours() {
        return neighbours;
    }

    public N getParent() {
        return parent;
    }
    //endregion

    //region Abstracts

    public abstract void calculateNearestNodes(Network network);

    public abstract double distanceTo(N dest);

    public abstract double discover(N dest);

    //endregion

    //region Setters

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setHeuristic(double heuristic) {
        this.heuristic = heuristic;
    }

    public void setFunction(double function) {
        this.function = function;
    }

    public void setNeighbours(Set<N> neighbours) {
        this.neighbours = neighbours;
    }

    public void setParent(N parent) {
        this.parent = parent;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    //endregion

    public boolean isValid() {
        return valid;
    }

    public void reset(){
        this.cost = 0;
        this.function = 0;
        this.parent = null;
        this.heuristic = 0;
    }
}