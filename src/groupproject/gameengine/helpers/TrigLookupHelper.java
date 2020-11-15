package groupproject.gameengine.helpers;

public class TrigLookupHelper {

    public static final double[] cos = genCos();
    public static final double[] sin = genSin();

    public static double[] genCos() {
        double[] cos = new double[360];
        for (int i = 0; i < cos.length; i++) {
            cos[i] = Math.cos(Math.PI / 180 * i);
        }
        return cos;
    }

    public static double[] genSin() {
        double[] sin = new double[360];
        for (int i = 0; i < sin.length; i++) {
            sin[i] = Math.sin(i * Math.PI / 180);
        }
        return sin;
    }

    public static float newWidth(float newHeight, float aspectRatio) {
        return newHeight * aspectRatio;
    }

    public static float newHeight(float newWidth, float aspectRatio) {
        return newWidth / aspectRatio;
    }

    public static float getAspectRatio(float oldWidth, float oldHeight) {
        return oldWidth / oldHeight;
    }
}