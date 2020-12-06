package groupproject.spritesheeteditor.views

import javafx.embed.swing.SwingFXUtils
import javafx.event.EventHandler
import javafx.scene.canvas.Canvas
import javafx.scene.image.WritableImage
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO


open class BaseCanvasView : EventHandler<MouseEvent?> {

    constructor(canvas: Canvas) {
        this.canvas = canvas
        this.pressing = BooleanArray(1024)
        this.canvas.onDragDetected = this
        this.canvas.onMouseClicked = this
        this.canvas.onMouseDragEntered = this
        this.canvas.onMouseDragReleased = this
        this.canvas.onMouseDragged = this
        this.canvas.onMouseDragEntered = this
        this.canvas.onMouseDragExited = this
        this.canvas.onMouseEntered = this
        this.canvas.onMouseExited = this
        this.canvas.setOnKeyPressed { e: KeyEvent -> keyPressed(e) }
        this.canvas.setOnKeyReleased { e: KeyEvent -> keyReleased(e) }
        this.canvas.isFocusTraversable = true
    }

    constructor(canvas: Canvas, file: File) : this(canvas) {
        this.file = file
    }


    protected var canvas: Canvas
    protected var pressing: BooleanArray
    private var last_y = 0.0
    private var last_x = 0.0


    var file: File? = null
        set(value) {
            field = value
            if (value != null) {
                redraw()
            }
        }

    val isInitialized
        get() = file != null

    protected open fun redraw(): BufferedImage {
        canvas.graphicsContext2D.clearRect(0.0, 0.0, canvas.width, canvas.height)
        val image = ImageIO.read(file)
        canvas.width = image.width.toDouble()
        canvas.height = image.height.toDouble()
        draw(image)
        return image
    }

    fun draw(toFXImage: WritableImage?) {
        canvas.graphicsContext2D.drawImage(toFXImage, last_x, last_y)
    }

    fun draw(
            image: BufferedImage,
            x: Double = 0.0,
            y: Double = 0.0,
            w: Double = image.width.toDouble(),
            h: Double = image.height.toDouble()
    ) {
        canvas.graphicsContext2D.drawImage(SwingFXUtils.toFXImage(image, null), x, y, w, h)
    }

    fun clear() {
        file = null
        canvas.graphicsContext2D.clearRect(0.0, 0.0, canvas.width, canvas.height)
    }

    override fun handle(event: MouseEvent?) = Unit


    open fun keyPressed(e: KeyEvent) {
        System.out.printf("IS SPACE PRESSED", e.code.ordinal == keySpace)
        pressing[e.code.ordinal] = true
    }

    open fun keyReleased(e: KeyEvent) {
        pressing[e.code.ordinal] = false
    }

    companion object {
        val keyUp = KeyCode.UP.ordinal
        val keyDn = KeyCode.DOWN.ordinal
        val keyLt = KeyCode.LEFT.ordinal
        val keyRt = KeyCode.RIGHT.ordinal
        val keyA = KeyCode.A.ordinal
        val keyB = KeyCode.B.ordinal
        val keyC = KeyCode.C.ordinal
        val keyD = KeyCode.D.ordinal
        val keyE = KeyCode.E.ordinal
        val keyF = KeyCode.F.ordinal
        val keyG = KeyCode.G.ordinal
        val keyH = KeyCode.H.ordinal
        val keyI = KeyCode.I.ordinal
        val keyJ = KeyCode.J.ordinal
        val keyK = KeyCode.K.ordinal
        val keyL = KeyCode.L.ordinal
        val keyM = KeyCode.M.ordinal
        val keyN = KeyCode.N.ordinal
        val keyO = KeyCode.O.ordinal
        val keyP = KeyCode.P.ordinal
        val keyQ = KeyCode.Q.ordinal
        val keyR = KeyCode.R.ordinal
        val keyS = KeyCode.S.ordinal
        val keyT = KeyCode.T.ordinal
        val keyU = KeyCode.U.ordinal
        val keyV = KeyCode.V.ordinal
        val keyW = KeyCode.W.ordinal
        val keyX = KeyCode.X.ordinal
        val keyY = KeyCode.Y.ordinal
        val keyZ = KeyCode.Z.ordinal
        val num1 = KeyCode.DIGIT1.ordinal
        val num2 = KeyCode.DIGIT2.ordinal
        val num3 = KeyCode.DIGIT3.ordinal
        val num4 = KeyCode.DIGIT4.ordinal
        val num5 = KeyCode.DIGIT5.ordinal
        val num6 = KeyCode.DIGIT6.ordinal
        val num7 = KeyCode.DIGIT7.ordinal
        val num8 = KeyCode.DIGIT8.ordinal
        val num9 = KeyCode.DIGIT9.ordinal
        val keyCtrl = KeyCode.CONTROL.ordinal
        val keyShft = KeyCode.SHIFT.ordinal
        val keyAlt = KeyCode.ALT.ordinal
        val keySpace = KeyCode.SPACE.ordinal
        val keyComma = KeyCode.COMMA.ordinal
        val keyPeriod = KeyCode.PERIOD.ordinal
        val keySlash = KeyCode.SLASH.ordinal
        val keySemiColon = KeyCode.SEMICOLON.ordinal
        val keyColon = KeyCode.COLON.ordinal
        val keyQuote = KeyCode.QUOTE.ordinal
    }

}