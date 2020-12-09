package groupproject.containers.zelda.algorithms;

import groupproject.gameengine.algorithms.AStar;
import groupproject.gameengine.algorithms.PathFindingObservable;
import groupproject.gameengine.algorithms.PathFindingObserver;
import groupproject.gameengine.contracts.BoundingContract;
import groupproject.gameengine.sprite.Sprite;
import groupproject.gameengine.tile.Tile;
import groupproject.gameengine.tile.TileMap;

import java.util.LinkedList;

//witty ;)
public class ATileStarAlgorithm extends AStar<TileMap, Tile> implements PathFindingObserver<Tile> {

    private Sprite sprite;

    public ATileStarAlgorithm(TileMap network) {
        super(network);
        addObserver(this);
    }

    public Tile getTileAtPosition(BoundingContract position) {
        double x = position.getX().doubleValue();
        double y = position.getY().doubleValue();
        return getNetwork().getTileAtPoint(x, y);
    }

    /**
     * We always want to store the staring sprite to give it its path
     * @param start
     */
    protected void setStartNode(Sprite start) {
        super.setStartNode(getTileAtPosition(start));
        this.sprite = start;
    }

    protected void setEndNode(Sprite end) {
        super.setEndNode(getTileAtPosition(end));
    }

    @Override
    public void update(PathFindingObservable<Tile> observer, LinkedList<Tile> path) {
        System.out.println(path);
        if (sprite != null) {
            sprite.setPath(path);
        }
        sprite = null;
    }

    public void solvePath(Sprite start, Sprite end) {
        reset();
        setStartNode(start);
        setEndNode(end);
        solve();
        getNetwork().resetNetwork();
    }
}
