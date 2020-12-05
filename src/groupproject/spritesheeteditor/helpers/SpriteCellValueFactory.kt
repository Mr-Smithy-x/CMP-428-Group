package groupproject.spritesheeteditor.helpers

import groupproject.spritesheeteditor.models.FileFormat
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.beans.value.ObservableValue
import javafx.embed.swing.SwingFXUtils
import javafx.scene.control.TableColumn
import javafx.scene.image.ImageView
import javafx.util.Callback
import java.awt.image.BufferedImage

class SpriteCellValueFactory(val index: Int, val image: BufferedImage) :
        Callback<TableColumn.CellDataFeatures<FileFormat.AnimationRow, Any>, ObservableValue<Any>> {

    override fun call(data: TableColumn.CellDataFeatures<FileFormat.AnimationRow, Any>): ObservableValue<Any> {
        if (index < data.value.set.size) {
            val value = data.value.set.toTypedArray()[index]
            val image = image.getSubimage(value.x, value.y, value.w, value.h)
            val view = ImageView(SwingFXUtils.toFXImage(image, null))
            data.tableColumn.maxWidth = (value.w + value.w / 2).toDouble()
            data.tableColumn.minWidth = (value.w + value.w / 2).toDouble()
            data.tableColumn.prefWidth = (value.w + value.w / 2).toDouble()
            data.tableView.refresh()
            return ReadOnlyObjectWrapper(view)
        }
        return ReadOnlyObjectWrapper(null)
    }
}