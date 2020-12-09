package groupproject.spritesheeteditor.models

import java.io.File
import java.io.FileInputStream
import java.io.ObjectInputStream
import java.io.Serializable
import java.util.*

data class PoseFileFormat(
        override var image: String,
        var poses: LinkedList<AnimationRow>
) : FileFormat(image), Serializable {


    companion object {
        private const val serialVersionUID = 20201204202000L

        fun load(filename: String): PoseFileFormat? {
            var pathname = "./assets/poses/$filename"
            if (!pathname.endsWith(".pose")) {
                pathname += ".pose"
            }
            return load(File(pathname))
        }

        private fun load(file: File): PoseFileFormat? {
            return ObjectInputStream(FileInputStream(file)).use {
                return@use when (val pose = it.readObject()) {
                    is PoseFileFormat -> pose
                    else -> null
                }
            }
        }

    }


}