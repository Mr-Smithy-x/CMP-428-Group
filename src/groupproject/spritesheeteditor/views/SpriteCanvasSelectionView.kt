package groupproject.spritesheeteditor.views

import groupproject.spritesheeteditor.algorithms.FloodFillAlgorithm
import groupproject.spritesheeteditor.models.FileFormat
import javafx.embed.swing.SwingFXUtils
import javafx.scene.canvas.Canvas
import javafx.scene.input.MouseEvent
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class SpriteCanvasSelectionView : BaseCanvasView {

    enum class SelectionMode(var dimension: Int = 0) {
        FINE,
        EVEN(48);
    }

    interface SpriteCanvasMouseCallback {
        fun onDoubleClicked(bounds: FileFormat.Bounds)
    }

    constructor(canvas: Canvas) : super(canvas)

    constructor(canvas: Canvas, file: File) : super(canvas, file) {
        redraw()
    }

    var mode: SelectionMode = SelectionMode.EVEN

    lateinit var image: BufferedImage
        private set

    lateinit var callback: SpriteCanvasMouseCallback

    override fun redraw(): BufferedImage {
        image = super.redraw()
        return image
    }

    private var selectedBounds: FileFormat.Bounds? = null

    override fun handle(event: MouseEvent?) {
        super.handle(event)

        when (event?.eventType) {
            MouseEvent.MOUSE_CLICKED, MouseEvent.MOUSE_DRAGGED -> {
                drawImage(event!!)
                if (event.clickCount >= 2 && this::callback.isInitialized && this.selectedBounds != null) {
                    callback.onDoubleClicked(selectedBounds!!)
                } else {
                    println(selectedBounds)
                }
            }
        }
    }

    private fun drawImage(e: MouseEvent) {
        drawImage(e.x, e.y)
    }

    private fun drawImage(x: Double, y: Double) {
        val map = HashMap<Double, ArrayList<Double>>()
        canvas.graphicsContext2D.clearRect(0.0, 0.0, canvas.width, canvas.height)
        val i = ImageIO.read(file)
        when (mode) {
            SelectionMode.EVEN -> {
                val realX = x.toInt() - (x.toInt() % mode.dimension)
                val realY = y.toInt() - (y.toInt() % mode.dimension)
                selectedBounds = FileFormat.Bounds(realX, realY, mode.dimension, mode.dimension)
            }
            else -> {
                val rgb = i.getRGB(x.toInt(), y.toInt())
                val c = Color(rgb, true)
                if (c.alpha != 0) {
                    FloodFillAlgorithm.floodNonTranslucent(map, i, canvas.graphicsContext2D, x, y, Color.RED)
                    canvas.graphicsContext2D.drawImage(SwingFXUtils.toFXImage(i, null), 0.0, 0.0)
                    val lowX = map.keys.minOrNull()
                    val highX = map.keys.maxOrNull()?.plus(1)
                    val lowY = map.values.flatten().minOrNull()
                    val highY = map.values.flatten().maxOrNull()?.plus(1)
                    if (lowX != null && highX != null && lowY != null && highY != null) {
                        val width = highX - lowX
                        val height = highY - lowY
                        selectedBounds =
                                FileFormat.Bounds(lowX.toInt(), lowY.toInt(), width.toInt(), height.toInt())
                    }
                } else {
                    selectedBounds = null
                }
            }
        }
        canvas.graphicsContext2D.drawImage(SwingFXUtils.toFXImage(i, null), 0.0, 0.0)
        if (selectedBounds != null) {
            canvas.graphicsContext2D.strokeRect(
                    selectedBounds!!.x.toDouble(),
                    selectedBounds!!.y.toDouble(),
                    selectedBounds!!.w.toDouble(),
                    selectedBounds!!.h.toDouble()
            )
        }
    }


    fun getSelectedImage(): FileFormat.Bounds? {
        return selectedBounds
    }

}