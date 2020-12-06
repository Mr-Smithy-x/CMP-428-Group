package groupproject.mapeditor.views;

import groupproject.gameengine.tile.TileSet;
import groupproject.mapeditor.MapEditorController;
import groupproject.mapeditor.components.MapEditorTileButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MapEditorTileSetView extends JPanel {
    private final transient MapEditorController editorController;
    private final JPanel tilesInSet;
    private MapEditorTileButton selectedTileButton;

    public MapEditorTileSetView(MapEditorController controller) {
        this.editorController = controller;
        tilesInSet = new JPanel();
        tilesInSet.setLayout(new GridBagLayout());
        tilesInSet.setBackground(Color.gray);
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.add(tilesInSet);
        this.setBackground(Color.gray);
    }

    public void initTileSetView(TileSet tileSet) {
        tilesInSet.removeAll();
        this.selectedTileButton = null;
        GridBagConstraints constraints = new GridBagConstraints();
        for (int row = 0; row < tileSet.getTileSetRows(); row++) {
            for (int col = 0; col < tileSet.getTileSetColumns(); col++) {
                int tileIndex = (row * tileSet.getTileSetColumns()) + col;
                MapEditorTileButton button = new MapEditorTileButton(tileSet.getTileImageList().get(tileIndex), row, col);
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        setTileSelected((MapEditorTileButton) e.getSource());
                    }
                });
                constraints.gridx = col;
                constraints.gridy = row;
                constraints.insets = new Insets(0, 0, 1, 1);
                tilesInSet.add(button, constraints);
            }
        }
        revalidate();
        repaint();
    }

    private void setTileSelected(MapEditorTileButton button) {
        if (this.selectedTileButton != null) {
            this.selectedTileButton.setBorder(UIManager.getBorder("Label.border"));
        }
        this.selectedTileButton = button;
        button.setBorder(BorderFactory.createLineBorder(Color.blue));
        this.editorController.setSelectedTile(button.getTileImage());
    }
}
