package groupproject.mapeditor.components;

import groupproject.gameengine.tile.TileSet;
import groupproject.mapeditor.MapEditorController;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class NewMapDialog extends JPanel {
    private final transient MapEditorController editorController;
    private String tileSetImageFile;

    public NewMapDialog(MapEditorController editorController) {
        this.editorController = editorController;
        NumberInputVerifier verifier = new NumberInputVerifier();

        JTextField tileWidth = new JTextField("");
        tileWidth.setPreferredSize(new Dimension(100, 25));
        tileWidth.setInputVerifier(verifier);
        JTextField tileHeight = new JTextField("");
        tileHeight.setPreferredSize(new Dimension(100, 25));
        tileHeight.setInputVerifier(verifier);
        JTextField mapRows = new JTextField("");
        mapRows.setPreferredSize(new Dimension(100, 25));
        mapRows.setInputVerifier(verifier);
        JTextField mapColumns = new JTextField("");
        mapColumns.setPreferredSize(new Dimension(100, 25));
        mapColumns.setInputVerifier(verifier);

        setupPanelLayout(tileWidth, tileHeight, mapRows, mapColumns);

        int result = JOptionPane.showConfirmDialog(null, this, "Create Map", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION)
            try {
                this.editorController.createTileMap(tileSetImageFile, Integer.parseInt(tileWidth.getText()),
                        Integer.parseInt(tileHeight.getText()), Integer.parseInt(mapRows.getText()), Integer.parseInt(mapColumns.getText()));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Parameters missing or invalid. Try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
    }

    private void setupPanelLayout(JTextField tileWidth, JTextField tileHeight, JTextField mapRows, JTextField mapColumns) {
        GridBagConstraints constraints = new GridBagConstraints();
        this.setLayout(new GridBagLayout());

        constraints.gridx = 0;
        constraints.gridy = 0;
        this.add(new JLabel("Width per tile: "), constraints);
        constraints.gridx = 1;
        constraints.gridy = 0;
        this.add(tileWidth, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        this.add(new JLabel("Height per tile: "), constraints);
        constraints.gridx = 1;
        constraints.gridy = 1;
        this.add(tileHeight, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        this.add(new JLabel("Rows in map: "), constraints);

        constraints.gridx = 1;
        constraints.gridy = 2;
        this.add(mapRows, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        this.add(new JLabel("Columns in map: "), constraints);

        constraints.gridx = 1;
        constraints.gridy = 3;
        this.add(mapColumns, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        this.add(new JLabel("Tile set image: "), constraints);

        constraints.gridx = 1;
        constraints.gridy = 4;
        JButton openFileButton = new JButton("Open");
        openFileButton.setPreferredSize(new Dimension(100, 25));
        this.add(openFileButton, constraints);

        openFileButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files (.jpg, .png, .gif, .bmp)",
                        "jpg", "png", "gif", "bmp");
                fileChooser.setFileFilter(filter);
                fileChooser.setCurrentDirectory(new File(TileSet.TILE_FOLDER));
                int returnVal = fileChooser.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    tileSetImageFile = fileChooser.getSelectedFile().getName();
                    openFileButton.setText(tileSetImageFile);
                }
            }
        });
        this.setMinimumSize(new Dimension(250, 250));
    }
}
