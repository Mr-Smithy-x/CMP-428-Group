package groupproject.spritesheeteditor.extensions

import groupproject.gameengine.sprite.Sprite
import groupproject.spritesheeteditor.helpers.AnimatedCellValueFactory
import groupproject.spritesheeteditor.models.FileFormat
import groupproject.spritesheeteditor.models.PoseFileFormat
import groupproject.spritesheeteditor.models.ProjectileFileFormat
import groupproject.spritesheeteditor.views.SpriteCanvasSelectionView
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.stage.FileChooser
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
            element.cellValueFactory = AnimatedCellValueFactory(i - 1, image)
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

fun TableView<FileFormat.AnimationRow>.loadSerialized(
        spriteCanvasSelectionView: SpriteCanvasSelectionView,
        file: File
) {
    ObjectInputStream(FileInputStream(file)).use {
        val pose = it.readObject()
        when (pose) {
            is PoseFileFormat -> load(spriteCanvasSelectionView, file, pose)
            is ProjectileFileFormat -> load(spriteCanvasSelectionView, file, pose)
            is FileFormat -> {
                println(pose.image)
            }
            else -> println("Deserialization failed")
        }
    }

}

fun TableView<FileFormat.AnimationRow>.load(
        spriteCanvasSelectionView: SpriteCanvasSelectionView,
        file: File,
        format: PoseFileFormat
) {
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

fun TableView<FileFormat.AnimationRow>.load(
        spriteCanvasSelectionView: SpriteCanvasSelectionView,
        file: File,
        format: ProjectileFileFormat
) {
    val imagePath = "${file.parentFile.parent}/sheets/${format.image}"
    val imageFile = File(imagePath)
    if (imageFile.exists()) {
        spriteCanvasSelectionView.file = imageFile
        init(spriteCanvasSelectionView.image)
        items.removeAll()
        items.add(FileFormat.AnimationRow(mode.name, format.set))
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

fun TableView<FileFormat.AnimationRow>.map(filename: String): PoseFileFormat {
    val lists = LinkedList<FileFormat.AnimationRow>()
    lists.addAll(items)
    return PoseFileFormat(filename, lists)
}

fun TableView<FileFormat.AnimationRow>.mapSelected(filename: String): ProjectileFileFormat {
    val row = selectionModel.selectedItem ?: items.first()
    return ProjectileFileFormat(filename, row.set)
}

fun TableView<FileFormat.AnimationRow>.map(image: File): PoseFileFormat {
    return map(image.name)
}

fun TableView<FileFormat.AnimationRow>.mapSelected(image: File): ProjectileFileFormat {
    return mapSelected(image.name)
}

class ExtensionTableView {
    enum class EDITOR(val file: File, val extensionFilter: FileChooser.ExtensionFilter) {
        PROJECTILE(File("./assets/projectiles").also {
            if (!it.exists()) {
                it.mkdirs()
            }
        }, FileChooser.ExtensionFilter("Projectile", "*.projectile")),
        POSE(File("./assets/poses").also {
            if (!it.exists()) {
                it.mkdirs()
            }
        }, FileChooser.ExtensionFilter("Pose", "*.pose"));
    }

    companion object {
        var editor: EDITOR = EDITOR.POSE
    }
}

var TableView<FileFormat.AnimationRow>.mode: ExtensionTableView.EDITOR
    get() = ExtensionTableView.editor
    set(value) {
        ExtensionTableView.editor = value
    }

fun TableView<FileFormat.AnimationRow>.saveSerialized(file: File, imageFilename: File) {
    val pathname = "./assets/sheets/${imageFilename.name}"
    val imageToCopy = File(pathname)
    if (!imageToCopy.exists()) {
        imageFilename.copyTo(imageToCopy, false)
    }
    when (mode) {
        ExtensionTableView.EDITOR.POSE -> ObjectOutputStream(FileOutputStream(file)).use {
            it.writeObject(map(imageFilename))
        }
        ExtensionTableView.EDITOR.PROJECTILE -> ObjectOutputStream(FileOutputStream(file)).use {
            it.writeObject(mapSelected(imageFilename))
        }

    }
}


