package groupproject.spritesheeteditor.models

import java.io.Serializable
import java.util.*

open class FileFormat(
        open var image: String
) : Serializable {

    data class AnimationRow(var pose: String, var set: AnimationSet) : Serializable {
        fun add(bounds: Bounds) {
            set.add(bounds)
        }

        companion object {
            private const val serialVersionUID = 20201204202000L
        }
    }

    class AnimationSet : LinkedHashSet<Bounds>() {

        companion object {
            private const val serialVersionUID = 20201204202000L
        }
    }

    data class Bounds constructor(val x: Int, val y: Int, val w: Int, val h: Int) : Serializable {

        companion object {
            private const val serialVersionUID = 20201204202000L
        }
    }

    companion object {
        private const val serialVersionUID = 20201204202000L

    }

}