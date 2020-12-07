package groupproject.spritesheeteditor.models

import java.io.File
import java.io.FileInputStream
import java.io.ObjectInputStream
import java.io.Serializable

data class ProjectileFileFormat(
    override var image: String,
    var set: AnimationSet
) : FileFormat(image), Serializable {

    companion object {

        private const val serialVersionUID = 20201204202000L

        fun load(filename: String): ProjectileFileFormat? {
            var pathname = "./assets/projectiles/$filename"
            if(!pathname.endsWith(".projectile")){
                pathname += ".projectile"
            }
            return load(File(pathname))
        }

        private fun load(file: File): ProjectileFileFormat? {
            return ObjectInputStream(FileInputStream(file)).use {
                return@use when (val pose = it.readObject()) {
                    is ProjectileFileFormat -> pose
                    else -> null
                }
            }
        }
    }
}