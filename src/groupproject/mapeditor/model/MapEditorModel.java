package groupproject.mapeditor.model;

import groupproject.gameengine.tile.TileMapModel;
import groupproject.gameengine.tile.TileSet;

public class MapEditorModel {
    private TileMapModel mapModel;
    private TileSet tileSet;

    public TileMapModel getMapModel() {
        return mapModel;
    }

    public TileSet getTileSet() {
        return tileSet;
    }

    public void setMapModel(TileMapModel mapModel) {
        this.mapModel = mapModel;
    }

    public void setTileSet(TileSet tileSet) {
        this.tileSet = tileSet;
    }
}
