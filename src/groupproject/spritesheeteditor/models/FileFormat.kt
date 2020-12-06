package groupproject.spritesheeteditor.models

import java.io.File
import java.io.FileInputStream
import java.io.ObjectInputStream
import java.io.Serializable
import java.util.*

data class FileFormat(
        override var image: String,
        var poses: LinkedList<AnimationRow>
) : SingleFileFormat(image), Serializable {

    data class AnimationRow(var pose: String, var set: AnimationSet) : Serializable {
        fun add(bounds: SpriteBounds) {
            set.add(bounds)
        }

        companion object {
            private const val serialVersionUID = 20201204202000L
        }
    }

    class AnimationSet : LinkedHashSet<SpriteBounds>() {

        companion object {
            private const val serialVersionUID = 20201204202000L
        }
    }

    data class SpriteBounds constructor(val x: Int, val y: Int, val w: Int, val h: Int) : Serializable {

        companion object {
            private const val serialVersionUID = 20201204202000L
        }
    }

    companion object {
        private const val serialVersionUID = 20201204202000L

        fun load(filename: String): FileFormat? {
            return load(File("./assets/poses/$filename"))
        }

        private fun load(file: File): FileFormat? {
            return ObjectInputStream(FileInputStream(file)).use {
                return@use when (val pose = it.readObject()) {
                    is FileFormat -> pose
                    else -> null
                }
            }
        }

    }


}