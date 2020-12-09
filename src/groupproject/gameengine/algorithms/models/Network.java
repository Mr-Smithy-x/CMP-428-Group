package groupproject.gameengine.algorithms.models;

import java.awt.*;

public interface Network<N extends Node> {

    Iterable<N> getNodes();

    boolean hasCrossDirection();

    N find(int col, int row);

}