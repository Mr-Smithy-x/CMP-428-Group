package groupproject.mapeditor.views;

import groupproject.gameengine.tile.TileMapModel;
import groupproject.mapeditor.MapEditorController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;

public class MapEditorView extends JPanel {
    private final transient MapEditorController editorController;
    private transient BufferedImage mapView;
    private transient BufferedImage objectMapView;

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
                    editorController.setCurrentHoveredRow(y);
                    editorController.setCurrentHoveredColumn(x);
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
        if (objectMapView != null)
            g.drawImage(objectMapView, 0, 0, (int) (objectMapView.getWidth() * editorController.getScaleFactor()),
                    (int) (objectMapView.getHeight() * editorController.getScaleFactor()), null);
    }

    @SuppressWarnings("java:S1215")
    public void loadInitialMapView(List<BufferedImage> tileArray, TileMapModel mapModel) {
        int mapWidth = mapModel.getPerTileWidth() * mapModel.getMapColumns();
        int mapHeight = mapModel.getPerTileHeight() * mapModel.getMapRows();
        this.setPreferredSize(new Dimension(mapWidth, mapHeight));
        mapView = new BufferedImage(mapWidth, mapHeight, BufferedImage.TYPE_INT_BGR);
        objectMapView = new BufferedImage(mapWidth, mapHeight, BufferedImage.TYPE_INT_ARGB_PRE);
        System.gc(); // if working with big maps, get the old references out of memory ASAP.

        Graphics mapViewGraphics = mapView.createGraphics();
        Graphics objectViewGraphics = objectMapView.createGraphics();

        for (int row = 0; row < mapModel.getMapRows(); row++) {
            for (int col = 0; col < mapModel.getMapColumns(); col++) {
                if (mapModel.getMapLayout()[row][col] != -1) {
                    if (mapModel.getObjectMap()[row][col] != -1)
                        objectViewGraphics.drawImage(tileArray.get(mapModel.getObjectMap()[row][col]),
                                col * mapModel.getPerTileWidth(), row * mapModel.getPerTileHeight(), null);

                    mapViewGraphics.drawImage(tileArray.get(mapModel.getMapLayout()[row][col]),
                            col * mapModel.getPerTileWidth(), row * mapModel.getPerTileHeight(), null);
                }

                drawCollisionBorder(mapModel.getCollisionMap()[row][col], mapViewGraphics, row, col,
                        mapModel.getPerTileWidth(), mapModel.getPerTileWidth());
            }
        }

        renderChanges(mapViewGraphics, objectViewGraphics);
    }

    private void renderChanges(Graphics mapViewGraphics, Graphics objectViewGraphics) {
        mapViewGraphics.dispose();
        objectViewGraphics.dispose();
        revalidate();
        repaint();
    }

    private void drawCollisionBorder(boolean collisionEnabled, Graphics mapViewGraphics, int row, int col, int tileWidth, int tileHeight) {
        if (editorController.isViewCollisionTileStatus()) {
            if (collisionEnabled) mapViewGraphics.setColor(Color.red);
            else mapViewGraphics.setColor(Color.white);
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
        Graphics objectViewGraphics = objectMapView.createGraphics();
        int tileWidth = editorController.getTileWidth();
        int tileHeight = editorController.getTileHeight();
        int row = clickedY / tileHeight;
        int col = clickedX / tileWidth;
        switch (editorController.getEditorMode()) {
            case PAINT:
                if (editorController.getSelectedTile() != null) {
                    mapViewGraphics.drawImage(editorController.getSelectedTile(), col * tileWidth,
                            row * tileHeight, null);
                    editorController.updateTileInMap(row, col, false);
                }
                break;
            case COLLISION:
                boolean currentVal = editorController.updateCollisionTileInMap(row, col);
                if (currentVal)
                    drawCollisionBorder(true, mapViewGraphics, row, col,
                            tileWidth, tileHeight);
                break;
            case OBJECT:
                if (editorController.getSelectedTile() != null && editorController.getTileAtPoint(row, col) > 0) {
                    objectViewGraphics.drawImage(editorController.getSelectedTile(), col * tileWidth, row * tileHeight, null);
                    editorController.updateTileInObjectMap(row, col, false);
                }
                break;
        }
        renderChanges(mapViewGraphics, objectViewGraphics);
    }

    private void editorTileRightClicked(int clickedX, int clickedY) {
        Graphics mapViewGraphics = mapView.createGraphics();
        Graphics2D objectViewGraphics = objectMapView.createGraphics();
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
                break;
            case OBJECT:
                if (editorController.getObjectTileAtPoint(row, col) > 0) {
                    objectViewGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
                    objectViewGraphics.setColor(new Color(0, 0, 0, 0));
                    objectViewGraphics.fillRect(col * tileWidth, row * tileHeight, tileWidth, tileHeight);
                    editorController.updateTileInObjectMap(row, col, true);
                }
                break;
            case COLLISION:
                boolean currentVal = editorController.updateCollisionTileInMap(row, col);
                if (!currentVal)
                    drawCollisionBorder(false, mapViewGraphics, row, col,
                            tileWidth, tileHeight);
                break;
        }
        renderChanges(mapViewGraphics, objectViewGraphics);
    }
}
