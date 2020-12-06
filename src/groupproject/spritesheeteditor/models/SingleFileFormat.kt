package groupproject.spritesheeteditor.models

import java.io.Serializable

open class SingleFileFormat(
    open var image: String
) : Serializable {

    companion object {
        private const val serialVersionUID = 20201204202000L

    }

}