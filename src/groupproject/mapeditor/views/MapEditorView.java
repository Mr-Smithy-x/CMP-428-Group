package groupproject.mapeditor.views;

import groupproject.gameengine.tile.TileMapModel;
import groupproject.mapeditor.MapEditorController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class MapEditorView extends JPanel {
    private final transient MapEditorController editorController;
    private transient BufferedImage mapView;

    public MapEditorView(MapEditorController controller) {
        this.editorController = controller;
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.setBackground(Color.gray);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = (int) (e.getX() / editorController.getScaleFactor());
                int y = (int) (e.getY() / editorController.getScaleFactor());
                if (isValidClick(x, y)) {
                    handleClick(e);
                    editorController.setLastClickedRow(y);
                    editorController.setLastClickedCol(x);
                }
            }
        });

        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = (int) (e.getX() / editorController.getScaleFactor());
                int y = (int) (e.getY() / editorController.getScaleFactor());
                if (isValidClick(x, y)) handleClick(e);
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (mapView != null)
            g.drawImage(mapView, 0, 0, (int) (mapView.getWidth() * editorController.getScaleFactor()),
                    (int) (mapView.getHeight() * editorController.getScaleFactor()), null);
    }

    @SuppressWarnings("java:S1215")
    public void loadInitialMapView(TileMapModel mapModel) {
        int mapWidth = mapModel.getPerTileWidth() * mapModel.getMapColumns();
        int mapHeight = mapModel.getPerTileHeight() * mapModel.getMapRows();
        this.setPreferredSize(new Dimension(mapWidth, mapHeight));
        mapView = new BufferedImage(mapWidth, mapHeight, BufferedImage.TYPE_INT_BGR);
        System.gc(); // if working with big maps, get the old references out of memory ASAP.

        Graphics mapViewGraphics = mapView.createGraphics();

        for (int row = 0; row < mapModel.getMapRows(); row++) {
            for (int col = 0; col < mapModel.getMapColumns(); col++) {
                if (mapModel.getMapLayout()[row][col] != -1) {
                    mapViewGraphics.drawImage(editorController.getTileImage(row, col),
                            col * mapModel.getPerTileWidth(), row * mapModel.getPerTileHeight(), null);

                    if (mapModel.getObjectMap()[row][col] != -1)
                        mapViewGraphics.drawImage(editorController.getObjectTileImage(row, col),
                                col * mapModel.getPerTileWidth(), row * mapModel.getPerTileHeight(), null);
                }

                drawCollisionBorder(mapModel.getCollisionMap()[row][col], mapViewGraphics, row, col,
                        mapModel.getPerTileWidth(), mapModel.getPerTileWidth());
            }
        }

        renderChanges(mapViewGraphics);
    }

    private void renderChanges(Graphics mapViewGraphics) {
        mapViewGraphics.dispose();
        revalidate();
        repaint();
    }

    private void drawCollisionBorder(boolean collisionEnabled, Graphics mapViewGraphics, int row, int col, int tileWidth, int tileHeight) {
        if (editorController.isViewCollisionTileStatus() && collisionEnabled) {
            mapViewGraphics.setColor(Color.red);
            mapViewGraphics.drawRect(col * tileWidth, row * tileHeight, tileWidth - 1, tileHeight - 1);
        }
    }

    private boolean isValidClick(int x, int y) {
        return (x < mapView.getWidth() && y < mapView.getHeight()) &&
                (x > 0 && y > 0);
    }

    private void handleClick(MouseEvent e) {
        int x = (int) (e.getX() / editorController.getScaleFactor());
        int y = (int) (e.getY() / editorController.getScaleFactor());
        if (SwingUtilities.isLeftMouseButton(e))
            editorTileLeftClicked(x, y);
        else if (SwingUtilities.isRightMouseButton(e))
            editorTileRightClicked(x, y);
    }

    private void editorTileLeftClicked(int clickedX, int clickedY) {
        Graphics mapViewGraphics = mapView.createGraphics();
        int tileWidth = editorController.getTileWidth();
        int tileHeight = editorController.getTileHeight();
        int row = clickedY / tileHeight;
        int col = clickedX / tileWidth;
        switch (editorController.getEditorMode()) {
            case PAINT:
                if (editorController.getSelectedTile() != null) {
                    mapViewGraphics.drawImage(editorController.getSelectedTile(), col * tileWidth,
                            row * tileHeight, null);
                    int possibleObjectTileID = editorController.getObjectTileAt(row, col);
                    if (possibleObjectTileID > 0)
                        mapViewGraphics.drawImage(editorController.getObjectTileImage(row, col), col * tileWidth,
                                row * tileHeight, null);
                    drawCollisionBorder(editorController.getCollisionStatusAt(row, col), mapViewGraphics, row, col,
                            tileWidth, tileHeight);
                    editorController.updateTileInMap(row, col, false);
                }
                break;
            case COLLISION:
                editorController.updateCollisionTileInMap(row, col, true);
                drawCollisionBorder(true, mapViewGraphics, row, col, tileWidth, tileHeight);
                break;
            case OBJECT:
                if (editorController.getSelectedTile() != null && editorController.getTileAt(row, col) > 0) {
                    mapViewGraphics.drawImage(editorController.getSelectedTile(), col * tileWidth, row * tileHeight, null);
                    drawCollisionBorder(editorController.getCollisionStatusAt(row, col), mapViewGraphics, row, col,
                            tileWidth, tileHeight);
                    editorController.updateTileInObjectMap(row, col, false);
                }
                break;
        }
        renderChanges(mapViewGraphics);
    }

    private void editorTileRightClicked(int clickedX, int clickedY) {
        Graphics mapViewGraphics = mapView.createGraphics();
        int tileWidth = editorController.getTileWidth();
        int tileHeight = editorController.getTileHeight();
        int row = clickedY / tileHeight;
        int col = clickedX / tileWidth;
        switch (editorController.getEditorMode()) {
            case PAINT:
                mapViewGraphics.setColor(Color.black);
                mapViewGraphics.fillRect(col * tileWidth, row * tileHeight, tileWidth, tileHeight);
                editorController.updateTileInMap(row, col, true);
                editorController.updateTileInObjectMap(row, col, true);
                editorController.updateCollisionTileInMap(row, col, false);
                break;
            case OBJECT:
                if (editorController.getObjectTileAt(row, col) > 0) {
                    mapViewGraphics.drawImage(editorController.getTileImage(row, col), col * tileWidth, row * tileHeight, null);
                    drawCollisionBorder(editorController.getCollisionStatusAt(row, col), mapViewGraphics, row, col,
                            tileWidth, tileHeight);
                    editorController.updateTileInObjectMap(row, col, true);
                }
                break;
            case COLLISION:
                if (editorController.getCollisionStatusAt(row, col)) {
                    editorController.updateCollisionTileInMap(row, col, false);
                    mapViewGraphics.drawImage(editorController.getTileImage(row, col), col * tileWidth, row * tileHeight, null);
                }
                break;
        }
        renderChanges(mapViewGraphics);
    }
}
