package groupproject.mapeditor.components;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

@SuppressWarnings("java:S110")
public class MapEditorTileButton extends JLabel {
    private transient BufferedImage tileImage;
    private transient BufferedImage objectImage;
    private final int mapRow;
    private final int mapCol;

    public MapEditorTileButton(BufferedImage tileImage, int row, int col) {
        this.tileImage = tileImage;
        this.mapRow = row;
        this.mapCol = col;
        this.drawIcon();
        if(tileImage != null) {
            this.setPreferredSize(new Dimension(this.tileImage.getWidth(), this.tileImage.getHeight()));
        }
    }

    public void drawIcon() {
        if(this.tileImage != null) {
            if(this.objectImage != null) {
                BufferedImage layeredImage = new BufferedImage(tileImage.getWidth(), tileImage.getHeight(), tileImage.getType());
                Graphics g = layeredImage.getGraphics();
                g.drawImage(this.tileImage, 0, 0, null);
                g.drawImage(this.objectImage, 0, 0, null);
                this.setIcon(new ImageIcon(layeredImage));
            } else this.setIcon(new ImageIcon(tileImage));
        }
    }

    public void setTileImage(BufferedImage tileImage) {
        this.tileImage = tileImage;
        this.drawIcon();
    }

    public BufferedImage getTileImage() {
        return tileImage;
    }

    public int getMapRow() {
        return mapRow;
    }

    public int getMapCol() {
        return mapCol;
    }

    public void setObjectImage(BufferedImage objectImage) {
        this.objectImage = objectImage;
        this.drawIcon();
    }

    public BufferedImage getObjectImage() {
        return objectImage;
    }
}
