package groupproject.gameengine.algorithms;


import groupproject.gameengine.algorithms.models.Node;

import java.util.LinkedHashSet;

public interface PathFindingObserver<N extends Node<N>> {
    void update(PathFindingObservable<N> observer, LinkedHashSet<N> path);
}
