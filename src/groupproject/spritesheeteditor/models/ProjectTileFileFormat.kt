package groupproject.spritesheeteditor.models

import java.io.File
import java.io.FileInputStream
import java.io.ObjectInputStream
import java.io.Serializable

data class ProjectTileFileFormat(
    override var image: String,
    var set: FileFormat.AnimationSet
) : SingleFileFormat(image), Serializable {

    companion object {

        private const val serialVersionUID = 20201204202000L

        fun load(filename: String): ProjectTileFileFormat? {
            return load(File("./assets/projectiles/$filename"))
        }

        private fun load(file: File): ProjectTileFileFormat? {
            return ObjectInputStream(FileInputStream(file)).use {
                return@use when (val pose = it.readObject()) {
                    is ProjectTileFileFormat -> pose
                    else -> null
                }
            }
        }
    }
}