package groupproject.gameengine.algorithms;


import groupproject.gameengine.algorithms.models.Network;
import groupproject.gameengine.algorithms.models.Node;

import java.util.*;

public abstract class AStar<T extends Network<N>, N extends Node<N>> extends PathFindingObservable<N> {

    protected T network;
    protected LinkedHashSet<N> path;

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
            this.path = new LinkedHashSet<>();
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

    public LinkedHashSet<N> getPath() {
        return path;
    }

    protected N getStartNode() {
        return start;
    }

    protected N getEndNode() {
        return end;
    }

    protected void setStartNode(N start) {
        this.start = start;
    }

    protected void setEndNode(N end) {
        this.end = end;
    }

}