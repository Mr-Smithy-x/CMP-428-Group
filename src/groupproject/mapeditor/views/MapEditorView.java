package groupproject.mapeditor.views;

import groupproject.gameengine.tile.TileMapModel;
import groupproject.mapeditor.MapEditorController;
import groupproject.mapeditor.components.MapEditorTileButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;

public class MapEditorView extends JPanel {
    private final transient MapEditorController editorController;
    private final JPanel tileButtonContainer;

    public MapEditorView(MapEditorController controller) {
        this.editorController = controller;
        tileButtonContainer = new JPanel();
        tileButtonContainer.setLayout(new GridBagLayout());
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.add(tileButtonContainer);
        this.setBackground(Color.gray);
    }

    public void loadInitialMapView(List<BufferedImage> tileArray, TileMapModel mapModel) {
        tileButtonContainer.removeAll();
        GridBagConstraints constraints = new GridBagConstraints();
        for (int row = 0; row < mapModel.getMapRows(); row++) {
            for (int col = 0; col < mapModel.getMapColumns(); col++) {
                BufferedImage tileImage = null;
                BufferedImage objectTileImage = null;
                if (mapModel.getObjectMap()[row][col] != -1)
                    objectTileImage = tileArray.get(mapModel.getObjectMap()[row][col]);
                if (mapModel.getMapLayout()[row][col] != -1)
                    tileImage = tileArray.get(mapModel.getMapLayout()[row][col]);

                MapEditorTileButton button = new MapEditorTileButton(tileImage, row, col);
                button.setObjectImage(objectTileImage);
                if (tileImage == null)
                    createEmptyTileButton(tileArray.get(0).getWidth(), tileArray.get(0).getHeight(), button);
                if (mapModel.getCollisionMap()[row][col]) button.setBorder(BorderFactory.createLineBorder(Color.red));
                else button.setBorder(UIManager.getBorder("Label.border"));

                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (SwingUtilities.isLeftMouseButton(e))
                            editorTileLeftClicked((MapEditorTileButton) e.getSource());
                        else if (SwingUtilities.isRightMouseButton(e))
                            editorTileRightClicked((MapEditorTileButton) e.getSource());
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        MapEditorTileButton button = (MapEditorTileButton) e.getSource();
                        editorController.setCurrentHoveredRow(button.getMapRow());
                        editorController.setCurrentHoveredColumn(button.getMapCol());
                    }
                });

                constraints.gridx = col;
                constraints.gridy = row;
                constraints.insets = new Insets(0, 0, 1, 1);
                tileButtonContainer.add(button, constraints);
            }
        }
        revalidate();
        repaint();
    }

    private void createEmptyTileButton(int iconWidth, int iconHeight, MapEditorTileButton button) {
        BufferedImage image = new BufferedImage(iconWidth, iconHeight, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.createGraphics();
        g.setColor(Color.lightGray);
        g.fillRect(0, 0, iconWidth, iconHeight);
        Dimension tileSize = new Dimension(iconWidth, iconHeight);
        button.setPreferredSize(tileSize);
        button.setIcon(new ImageIcon(image));
    }

    private void editorTileLeftClicked(MapEditorTileButton button) {
        switch (editorController.getEditorMode()) {
        case PAINT:
            if (editorController.getSelectedTile() != null) {
                button.setTileImage(editorController.getSelectedTile());
                editorController.updateTileInMap(button.getMapRow(), button.getMapCol(), false);
            }
            break;
        case COLLISION:
            boolean currentVal = editorController.updateCollisionTileInMap(button.getMapRow(), button.getMapCol());
            if (currentVal) button.setBorder(BorderFactory.createLineBorder(Color.red));
            else button.setBorder(UIManager.getBorder("Label.border"));
            break;
        case OBJECT:
            if (editorController.getSelectedTile() != null && button.getTileImage() != null) {
                button.setObjectImage(editorController.getSelectedTile());
                editorController.updateTileInObjectMap(button.getMapRow(), button.getMapCol(), false);
            }
            break;
        }
    }

    private void editorTileRightClicked(MapEditorTileButton button) {
        switch (editorController.getEditorMode()) {
        case PAINT:
            if (button.getTileImage() != null) {
                int iconWidth = button.getTileImage().getWidth();
                int iconHeight = button.getTileImage().getHeight();
                button.setTileImage(null);
                button.setObjectImage(null);
                createEmptyTileButton(iconWidth, iconHeight, button);
                editorController.updateTileInMap(button.getMapRow(), button.getMapCol(), true);
                editorController.updateTileInObjectMap(button.getMapRow(), button.getMapCol(), true);
            }
            break;
        case OBJECT:
            if (button.getObjectImage() != null) {
                button.setObjectImage(null);
                editorController.updateTileInObjectMap(button.getMapRow(), button.getMapCol(), true);
            }
            break;
        case COLLISION:
            break;
        }
    }
}
