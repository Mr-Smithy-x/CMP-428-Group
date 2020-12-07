package groupproject.mapeditor.views;

import groupproject.gameengine.tile.TileSet;
import groupproject.mapeditor.MapEditorController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class MapEditorTileSetView extends JPanel {
    private final transient MapEditorController editorController;
    private transient BufferedImage tileSetView;
    private transient int selectedTileRow;
    private transient int selectedTileCol;
    private transient int tileWidth;
    private transient int tileHeight;

    public MapEditorTileSetView(MapEditorController controller) {
        this.editorController = controller;
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.setBackground(Color.gray);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                setTileSelected(e.getX(), e.getY());
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (tileSetView != null)
            g.drawImage(tileSetView, 0, 0, null);
    }

    public void initTileSetView() {
        TileSet tileSet = editorController.getTileSet();
        tileWidth = editorController.getTileWidth();
        tileHeight = editorController.getTileHeight();
        int tileSetImageWidth = tileWidth * tileSet.getTileSetColumns();
        int tileSetImageHeight = tileHeight * tileSet.getTileSetRows();
        tileSetView = new BufferedImage(tileSetImageWidth, tileSetImageHeight, BufferedImage.TYPE_3BYTE_BGR);
        this.setPreferredSize(new Dimension(tileSetImageWidth, tileSetImageHeight));
        selectedTileRow = -1;
        selectedTileCol = -1;
        Graphics tileSetViewGraphics = tileSetView.createGraphics();

        for (int row = 0; row < tileSet.getTileSetRows(); row++) {
            for (int col = 0; col < tileSet.getTileSetColumns(); col++) {
                int tileIndex = (row * tileSet.getTileSetColumns()) + col;
                int x = col * tileWidth;
                int y = row * tileHeight;
                tileSetViewGraphics.drawImage(tileSet.getTileImageList().get(tileIndex), x, y, null);
            }
        }

        revalidate();
        repaint();
    }

    private void drawSelectedTileOutline(int newRow, int newCol) {
        Graphics tileSetViewGraphics = tileSetView.createGraphics();

        // Redraw white outline around old selected tile if there was one.
        if (selectedTileRow >= 0 && selectedTileCol >= 0) {
            int oldTileIndex = (selectedTileRow * editorController.getTileSet().getTileSetColumns()) + selectedTileCol;
            tileSetViewGraphics.drawImage(editorController.getTileSet().getTileImageList().get(oldTileIndex),
                    selectedTileCol * tileWidth, selectedTileRow * tileHeight, tileWidth, tileHeight, null);
        }

        // Set new selected tile area, and draw the selected outline.
        selectedTileRow = newRow;
        selectedTileCol = newCol;
        tileSetViewGraphics.setColor(Color.blue);
        tileSetViewGraphics.drawRect(selectedTileCol * tileWidth,selectedTileRow * tileHeight, tileWidth - 1,
                tileHeight - 1);
    }

    private void setTileSelected(int clickedX, int clickedY) {
        int row = clickedY / tileHeight;
        int col = clickedX / tileWidth;
        drawSelectedTileOutline(row, col);
        this.editorController.setSelectedTile(row, col);
        revalidate();
        repaint();
    }
}
