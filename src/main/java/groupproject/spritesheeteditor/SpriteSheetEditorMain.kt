package groupproject.spritesheeteditor

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

class SpriteSheetEditorMain : Application() {

    override fun start(primaryStage: Stage?) {
        val fileString = "fxml/mainwindow.fxml"
        val resource = javaClass.getResource(fileString)
        val root = FXMLLoader.load<Parent>(resource)
        primaryStage!!.title = "SpriteSheet Editor"
        primaryStage.scene = Scene(root)
        primaryStage.show()
    }

}