package groupproject.spritesheeteditor.algorithms

import javafx.embed.swing.SwingFXUtils
import javafx.scene.canvas.GraphicsContext
import java.awt.Color
import java.awt.image.BufferedImage
import kotlin.math.abs


object FloodFillAlgorithm {

    fun originalFlood(image: BufferedImage, graphics: GraphicsContext, x: Int, y: Int, color: Color, replacement: Color) {
        if (x >= 1 && y >= 1 && x < image.width && y < image.height) {
            val c2 = Color(image.getRGB(x, y))
            if (abs(c2.green - color.green) < 30 &&
                    abs(c2.red - color.red) < 30 &&
                    abs(c2.blue - color.blue) < 30
            ) {
                image.setRGB(x, y, replacement.rgb)
                graphics.drawImage(SwingFXUtils.toFXImage(image, null), 0.0, 0.0)
                originalFlood(image, graphics, x, y + 1, color, replacement)
                originalFlood(image, graphics, x + 1, y, color, replacement)
                originalFlood(image, graphics, x - 1, y, color, replacement)
                originalFlood(image, graphics, x, y - 1, color, replacement)
            }
        }
    }

    fun floodNonTranslucent(map: HashMap<Double, ArrayList<Double>>, image: BufferedImage, graphics: GraphicsContext, x: Double, y: Double, replacement: Color) {
        if (x >= 1 && y >= 1 && x < image.width && y < image.height) {
            val rgb = image.getRGB(x.toInt(), y.toInt())
            val c2 = Color(rgb, true)
            if (c2.alpha != 0 && (!map.containsKey(x) || (map.containsKey(x) && !map[x]!!.contains(y)))) {
                image.setRGB(x.toInt(), y.toInt(), replacement.rgb)
                if (!map.containsKey(x)) {
                    map[x] = ArrayList()
                }
                map[x]!!.add(y)
                if (!isTransparent(image, x, y + 1)) {
                    floodNonTranslucent(map, image, graphics, x, y + 1, replacement)
                }
                if (!isTransparent(image, x + 1, y)) {
                    floodNonTranslucent(map, image, graphics, x + 1, y, replacement)
                }
                if (!isTransparent(image, x - 1, y)) {
                    floodNonTranslucent(map, image, graphics, x - 1, y, replacement)
                }
                if (!isTransparent(image, x, y - 1)) {
                    floodNonTranslucent(map, image, graphics, x, y - 1, replacement)
                }
            }
        }
    }

    private fun isTransparent(image: BufferedImage, x: Double, y: Double): Boolean {
        return peekColor(image, x, y).alpha == 0
    }

    private fun peekColor(image: BufferedImage, x: Double, y: Double): Color {
        val rgb = image.getRGB(x.toInt(), y.toInt())
        return Color(rgb, true)
    }
}