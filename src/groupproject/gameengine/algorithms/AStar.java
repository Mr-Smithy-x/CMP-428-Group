package groupproject.gameengine.algorithms;


import groupproject.gameengine.algorithms.models.Network;
import groupproject.gameengine.algorithms.models.Node;

import java.util.*;

public class AStar<T extends Network<N>, N extends Node<N>> extends Observable {


    private T network;
    private PriorityQueue<N> path;

    private N start;
    private N end;

    private Set<N> openList;
    private Set<N> closedList;

    public AStar(T network) {
        this.network = network;
    }

    public void solve() {
        if (path != null && !path.isEmpty()) {
            this.path.clear();
        } else {
            this.path = new PriorityQueue<>();
        }

        if (start == null || end == null) {
            return;
        }

        if (start.equals(end)) {
            return;
        }

        this.openList = new LinkedHashSet<>();
        this.closedList = new LinkedHashSet<>();

        this.openList.add(start);

        while (!openList.isEmpty()) {
            N current = getLowestFunction();

            if (current.equals(end)) {
                retrace(current);
                break;
            }

            openList.remove(current);
            closedList.add(current);
            for (N n : current.getNeighbours()) {
                if (closedList.contains(n) || !n.isValid()) {
                    continue;
                }
                double tempScore = current.getCost() + current.distanceTo(n);
                if (openList.contains(n)) {
                    if (tempScore < n.getCost()) {
                        n.setCost(tempScore);
                        n.setParent(current);
                    }
                } else {
                    n.setCost(tempScore);
                    openList.add(n);
                    n.setParent(current);
                }
                n.setHeuristic(n.discover(end));
                n.setFunction(n.getCost() + n.getHeuristic());
            }
        }
        updateUI();
    }

    public void reset() {
        this.start = null;
        this.end = null;
        this.path = null;
        this.openList = null;
        this.closedList = null;
    }

    private void retrace(N current) {
        N temp = current;
        this.path.add(current);
        while (temp.getParent() != null) {
            this.path.add(temp.getParent());
            temp = temp.getParent();
        }
        this.path.add(start);
    }

    private N getLowestFunction() {
        N lowest = openList.stream().findFirst().orElse(null);
        for (N n : openList) {
            if (n.getFunction() < lowest.getFunction()) {
                lowest = n;
            }
        }
        return lowest;
    }

    public void updateUI() {
        setChanged();
        notifyObservers(getPath());
        clearChanged();
    }

    public T getNetwork() {
        return network;
    }

    public PriorityQueue<N> getPath() {
        return path;
    }

    public N getStartNode() {
        return start;
    }

    public N getEndNode() {
        return end;
    }

    public void setStartNode(N start) {
        this.start = start;
    }

    public void setEndNode(N end) {
        this.end = end;
    }

}