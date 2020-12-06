package groupproject.gameengine.tile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TileSet {
    public static final String TILE_FOLDER = "assets/tiles/";
    private final ArrayList<BufferedImage> tileImageList = new ArrayList<>();
    private final int perTileHeight;
    private final int perTileWidth;
    private BufferedImage tileSetImage;

    public TileSet(int perTileWidth, int perTileHeight, String tileSetFile) {
        this.perTileHeight = perTileHeight;
        this.perTileWidth = perTileWidth;
        File tileSet = new File(TILE_FOLDER + tileSetFile);
        try {
            this.tileSetImage = ImageIO.read(tileSet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        initTileSet();
    }

    private void initTileSet() {
        int rows = tileSetImage.getHeight() / perTileHeight;
        int columns = tileSetImage.getWidth() / perTileWidth;
        for (int rowIndex = 0; rowIndex < rows; rowIndex++) {
            for (int columnIndex = 0; columnIndex < columns; columnIndex++) {
                BufferedImage newTileImage = tileSetImage.getSubimage(columnIndex * perTileWidth, rowIndex * perTileHeight,
                        perTileWidth, perTileHeight);
                tileImageList.add(newTileImage);
            }
        }
    }

    public List<BufferedImage> getTileImageList() {
        return tileImageList;
    }

    public int getTileSetRows() {
        return tileSetImage.getHeight() / perTileHeight;
    }

    public int getTileSetColumns() {
        return tileSetImage.getWidth() / perTileWidth;
    }
}
