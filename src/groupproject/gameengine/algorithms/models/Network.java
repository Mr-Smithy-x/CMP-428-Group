package groupproject.gameengine.algorithms.models;

public interface Network<N extends Node> {

    Iterable<N> getNodes();

    boolean hasCrossDirection();

    N find(int col, int row);

    default void resetNetwork() {
        for (N node : getNodes()) {
            node.reset();
        }
    }

}