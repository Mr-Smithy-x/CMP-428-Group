package groupproject.gameengine.algorithms;


import groupproject.gameengine.algorithms.models.Node;

import java.util.LinkedList;

public interface PathFindingObserver<N extends Node<N>> {
    void update(PathFindingObservable<N> observer, LinkedList<N> path);
}
