package groupproject.spritesheeteditor.controllers

import groupproject.spritesheeteditor.extensions.*
import groupproject.spritesheeteditor.models.FileFormat
import groupproject.spritesheeteditor.views.SpriteCanvasSelectionView
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.canvas.Canvas
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.stage.FileChooser
import java.io.File
import kotlin.system.exitProcess

class MainViewController : EventHandler<ActionEvent>, SpriteCanvasSelectionView.SpriteCanvasMouseCallback {

    //region FXML Injectable Fields
    @FXML
    lateinit var newMenuItem: MenuItem

    @FXML
    lateinit var openMenuItem: MenuItem

    @FXML
    lateinit var saveMenuItem: MenuItem

    @FXML
    lateinit var closeMenuItem: MenuItem

    @FXML
    lateinit var aboutMenuItem: MenuItem

    @FXML
    lateinit var spriteCanvas: Canvas

    @FXML
    lateinit var addPoseBtn: Button

    @FXML
    lateinit var addImageBtn: Button

    @FXML
    lateinit var tableView: TableView<FileFormat.AnimationRow>

    @FXML
    lateinit var selectPoseCombobox: ComboBox<String>

    @FXML
    lateinit var poseTextField: TextField
    //endregion

    //region Non Injectables
    private val spriteCanvasSelectionView: SpriteCanvasSelectionView by lazy {
        SpriteCanvasSelectionView(spriteCanvas)
    }
    var imageExtensionFilter = FileChooser.ExtensionFilter("Image", "*.png", "*.gif")
    var poseExtensionFilter = FileChooser.ExtensionFilter("Pose", "*.pose")
    private var fileChooser: FileChooser = FileChooser()
    //endregion

    @FXML
    fun initialize() {
        newMenuItem.onAction = this
        openMenuItem.onAction = this
        saveMenuItem.onAction = this
        closeMenuItem.onAction = this
        aboutMenuItem.onAction = this
        addPoseBtn.onAction = this
        addImageBtn.onAction = this
        spriteCanvasSelectionView.callback = this
        tableView.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            newValue?.run {
                selectPoseCombobox.selectionModel.select(this.pose)
            }
        }
        tableView.setOnKeyReleased {
            if (it.code == KeyCode.BACK_SPACE && tableView.selectionModel.selectedIndex > -1) {
                tableView.removeLastInserted()
            }
        }
    }

    private fun alert(
        title: String,
        headerText: String,
        contentText: String,
        type: Alert.AlertType = Alert.AlertType.INFORMATION
    ) = Alert(type).also {
        it.title = title
        it.headerText = headerText
        it.contentText = contentText
    }

    override fun handle(event: ActionEvent?) {
        when (event?.source) {
            aboutMenuItem -> {
                alert(
                    "About",
                    "Dev: Mr-Smithy-x@github.com",
                    "Parsing & rearranging sheets is an nuisance"
                ).showAndWait()
            }
            newMenuItem -> {
                spriteCanvasSelectionView.clear()
                fileChooser.initialDirectory = File("./assets/sheets")
                fileChooser.extensionFilters.setAll(imageExtensionFilter)
                val file = fileChooser.showOpenDialog(null)
                if (file != null && file.exists()) {
                    spriteCanvasSelectionView.file = file
                    tableView.init(spriteCanvasSelectionView)
                    tableView.items.clear()
                }
            }
            openMenuItem -> {
                fileChooser.initialDirectory = File("./assets/poses")
                fileChooser.extensionFilters.setAll(poseExtensionFilter)
                val file = fileChooser.showOpenDialog(null)
                if (file != null && file.exists()) {
                    tableView.loadSerialized(spriteCanvasSelectionView, file)
                }
            }
            saveMenuItem -> {
                if (spriteCanvasSelectionView.isInitialized) {
                    val imageFile = spriteCanvasSelectionView.file!!
                    fileChooser.initialDirectory = File("./assets/poses")
                    fileChooser.extensionFilters.setAll(poseExtensionFilter)
                    fileChooser.initialFileName = imageFile.nameWithoutExtension
                    val file = fileChooser.showSaveDialog(null)
                    if (file != null) {
                        tableView.saveSerialized(file, imageFile)
                    }
                } else alert(
                    "Hmmm",
                    "We ran into a issue",
                    "There's nothing to save",
                    Alert.AlertType.INFORMATION
                ).showAndWait()
            }
            closeMenuItem -> {
                Platform.exit()
                exitProcess(0)
            }
            addPoseBtn -> {
                if (spriteCanvasSelectionView.isInitialized) {
                    val text = poseTextField.text.trim().toUpperCase()
                    poseTextField.clear()
                    if (!tableView.hasPose(text)) {
                        selectPoseCombobox.items.add(text)
                        selectPoseCombobox.selectionModel.select(text)
                        tableView.add(text)
                    } else alert(
                        "Error",
                        "We ran into a conflict",
                        "You already have '$text' listed already",
                        Alert.AlertType.ERROR
                    ).showAndWait()
                } else {
                    alert(
                        "Error",
                        "We ran into an issue",
                        "App needs to be initialized",
                        Alert.AlertType.ERROR
                    ).showAndWait()
                }
            }
            addImageBtn -> {
                if (spriteCanvasSelectionView.isInitialized) {
                    spriteCanvasSelectionView.getSelectedImage()?.run {
                        tableView.find(selectPoseCombobox.selectionModel.selectedItem)?.add(this)
                        tableView.refresh()
                    }
                } else {
                    alert(
                        "Error",
                        "We ran into an issue",
                        "App needs to be initialized",
                        Alert.AlertType.ERROR
                    ).showAndWait()
                }
            }
        }
    }

    override fun onDoubleClicked(bounds: FileFormat.SpriteBounds) {
        tableView.find(selectPoseCombobox.selectionModel.selectedItem)?.add(bounds)
        tableView.refresh()
    }

}