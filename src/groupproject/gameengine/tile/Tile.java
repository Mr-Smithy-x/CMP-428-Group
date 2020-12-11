package groupproject.gameengine.tile;

import groupproject.gameengine.algorithms.models.Network;
import groupproject.gameengine.algorithms.models.Node;
import groupproject.gameengine.camera.GlobalCamera;
import groupproject.gameengine.contracts.CameraContract;
import groupproject.gameengine.contracts.Renderable;
import groupproject.gameengine.models.BoundingBox;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

public class Tile extends Node<Tile> implements Comparable<Node<Tile>>, Renderable, CameraContract {
    private final int width;
    private final int height;
    private final BufferedImage tileImage;
    private final int tileID;
    private int x;
    private int y;
    private BoundingBox boundsRect;
    private boolean collisionEnabled = false;
    private boolean collisionOverride = false;
    protected transient Point point = new Point(0, 0); //set default point

    public Tile(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        tileID = 0;
        tileImage = null;
    }

    public Tile(BufferedImage image, int tileID) {
        this.tileImage = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.tileID = tileID;
    }

    @Override
    public void render(Graphics g) {
        if(canRender(GlobalCamera.getInstance())) {
            g.drawImage(tileImage,
                    getCameraOffsetX(GlobalCamera.getInstance()).intValue(),
                    getCameraOffsetY(GlobalCamera.getInstance()).intValue(),
                    null);
            if (System.getProperty("DEBUG").equals("true") && boundsRect != null) drawBoundsRect(g);
        }
    }

    public void drawBoundsRect(Graphics g) {
        if (collisionEnabled) {
            g.setColor(Color.red);
            boundsRect.render(g);
        }
    }

    @Override
    public Number getX() {
        return x;
    }

    @Override
    public void setX(Number x) {
        this.x = x.intValue();
    }

    @Override
    public Number getY() {
        return y;
    }

    @Override
    public void setY(Number y) {
        this.y = y.intValue();
    }

    @Override
    public Number getWidth() {
        return width;
    }

    @Override
    public void setWidth(Number width) {
        /*
          Not needed.
         */
    }

    @Override
    public Number getHeight() {
        return height;
    }

    @Override
    public void setHeight(Number height) {
        /*
          Not needed.
         */
    }

    public boolean isCollisionEnabled() {
        return collisionEnabled;
    }

    public void setCollisionEnabled(boolean collisionEnabled) {
        this.collisionEnabled = collisionEnabled;
    }

    public boolean isCollisionOverride() {
        return collisionOverride;
    }

    public void setCollisionOverride(boolean collisionOverride) {
        this.collisionOverride = collisionOverride;
    }

    public BoundingBox getBoundsRect() {
        return boundsRect;
    }

    public void initBoundsRect() {
        this.boundsRect = new BoundingBox(x, y, width, height);
    }

    public int getTileID() {
        return tileID;
    }


    @Override
    public void calculateNearestNodes(Network network) {
        TileMap grid = (TileMap) network;

        Set<Tile> nodes = new HashSet<>();

        int minX = 0;
        int minY = 0;
        int maxX = width + point.x;
        int maxY = height + point.y;

        int x = point.x;
        int y = point.y;

        if (x > minX) {
            Tile e = grid.find(x+1, y);
            if (e != null && e.isValid()) {
                nodes.add(e); //west
            }
        }

        if (x < maxX) {
            Tile e = grid.find(x + 1, y);
            if (e != null && e.isValid()) {
                nodes.add(e); //east
            }
        }

        if (y > minY) {
            Tile e = grid.find(x, y - 1);
            if (e != null && e.isValid()) {
                nodes.add(e); //north
            }
        }

        if (y < maxY) {
            Tile e = grid.find(x, y + 1);
            if (e != null && e.isValid()) {
                nodes.add(e); //south
            }
        }

        if (network.hasCrossDirection()) {
            if (x > minX && y > minY) {
                Tile e = grid.find(x - 1, y - 1);
                if (e != null && e.isValid()) {
                    nodes.add(e); //northwest
                }
            }

            if (x < maxX && y < maxY) {
                Tile e = grid.find(x + 1, y + 1);
                if (e != null && e.isValid()) {
                    nodes.add(e); //southeast
                }
            }

            if (x < maxX && y > minY) {
                Tile e = grid.find(x + 1, y - 1);
                if (e != null && e.isValid()) {
                    nodes.add(e); //northeast
                }
            }

            if (x > minY && y < maxY) {
                Tile e = grid.find(x - 1, y + 1);
                if (e != null && e.isValid()) {
                    nodes.add(e); //southwest
                }
            }
        }
        this.setNeighbours(nodes);
    }


    @Override
    public double discover(Tile dest) {
        return distanceTo(dest);
    }

    @Override
    public double distanceTo(Tile dest) {
        int px = dest.point.x - point.x;
        int py = dest.point.y - point.y;
        return Math.sqrt(px * px + py * py);
    }

    @Override
    public int compareTo(Node<Tile> o) {
        double b1Priority = this.getFunction();
        double b2Priority = o.getFunction();
        return Double.compare(b1Priority, b2Priority);
    }

    public Point getPoint() {
        return point;
    }

    public void setMapPoint(Point point) {
        this.point = point;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tile tile = (Tile) o;
        if (point.x != tile.getPoint().x) return false;
        return point.y == tile.getPoint().y;
    }

    @Override
    public int hashCode() {
        int result = point.x;
        result = 31 * result + point.y;
        return result;
    }

    @Override
    public String toString() {
        if(getPoint() == null){
            return "Tile{}";
        }
        return String.format("Tile{(%s, %s)}", getPoint().x * width, getPoint().y * height);
    }

    @Override
    public boolean isValid() {
        return !isCollisionEnabled();
    }
}
