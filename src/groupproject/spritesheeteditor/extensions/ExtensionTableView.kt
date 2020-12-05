package groupproject.spritesheeteditor.extensions

import groupproject.gameengine.sprite.Sprite
import groupproject.spritesheeteditor.helpers.SpriteCellValueFactory
import groupproject.spritesheeteditor.models.FileFormat
import groupproject.spritesheeteditor.views.SpriteCanvasSelectionView
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import java.awt.image.BufferedImage
import java.io.*
import java.util.*

fun TableView<FileFormat.AnimationRow>.init(
        spriteCanvasSelectionView: SpriteCanvasSelectionView
): List<TableColumn<FileFormat.AnimationRow, Any>> {
    return init(spriteCanvasSelectionView.image)
}

fun TableView<FileFormat.AnimationRow>.init(
        image: BufferedImage
): List<TableColumn<FileFormat.AnimationRow, Any>> {
    val map = ArrayList<TableColumn<FileFormat.AnimationRow, Any>>()
    for (i in 0..16) {
        if (i == 0) {
            val element = TableColumn<FileFormat.AnimationRow, Any>("Pose")
            element.cellValueFactory = PropertyValueFactory("pose")
            map.add(element)
        } else {
            val element = TableColumn<FileFormat.AnimationRow, Any>("F:$i")
            element.cellValueFactory = SpriteCellValueFactory(i - 1, image)
            map.add(element)
        }
    }
    items.clear()
    columns.clear()
    columns.addAll(map)
    return map
}

val TableView<FileFormat.AnimationRow>.poses: List<String>
    get() = items.map { it.pose }

fun TableView<FileFormat.AnimationRow>.loadSerialized(spriteCanvasSelectionView: SpriteCanvasSelectionView, file: File) {
    ObjectInputStream(FileInputStream(file)).use {
        val pose = it.readObject()
        when (pose) {
            is FileFormat -> load(spriteCanvasSelectionView, file, pose)
            else -> println("Deserialization failed")
        }
    }
}


fun TableView<FileFormat.AnimationRow>.load(spriteCanvasSelectionView: SpriteCanvasSelectionView, file: File, format: FileFormat) {
    val imagePath = "${file.parentFile.parent}/sheets/${format.image}"
    val imageFile = File(imagePath)
    if (imageFile.exists()) {
        spriteCanvasSelectionView.file = imageFile
        init(spriteCanvasSelectionView.image)
        items.removeAll()
        items.addAll(format.poses)
        val defaultPoses = Sprite.Pose.values().map { it.name }
        val addedPoses = items.map { it.pose }
        val posesToAdd = defaultPoses.minus(addedPoses)
        for (pose in posesToAdd) {
            addOption(pose)
        }
        refresh()
    } else {
        throw FileNotFoundException("$imagePath, Couldnt find relative to location")
    }
}


fun TableView<FileFormat.AnimationRow>.hasPose(pose: String): Boolean {
    return items.any { it.pose.equals(pose, true) }
}

fun TableView<FileFormat.AnimationRow>.find(pose: String): FileFormat.AnimationRow? {
    return items.find {
        it.pose.equals(
                pose,
                true
        )
    }
}

fun TableView<FileFormat.AnimationRow>.addOption(pose: String) {
    items.add(FileFormat.AnimationRow(pose, FileFormat.AnimationSet()))
    refresh()
}

fun TableView<FileFormat.AnimationRow>.removeLastInserted() {
    val item = selectionModel.selectedItem
    if (!item.set.isEmpty()) {
        item.set.remove(item.set.last())
        refresh()
    }
}

fun TableView<FileFormat.AnimationRow>.map(filename: String): FileFormat {
    val lists = LinkedList<FileFormat.AnimationRow>()
    lists.addAll(items)
    return FileFormat(filename, lists)
}

fun TableView<FileFormat.AnimationRow>.map(image: File): FileFormat {
    return map(image.name)
}

fun TableView<FileFormat.AnimationRow>.saveSerialized(file: File, imageFilename: File) {
    ObjectOutputStream(FileOutputStream(file)).use {
        it.writeObject(map(imageFilename))
    }
}


