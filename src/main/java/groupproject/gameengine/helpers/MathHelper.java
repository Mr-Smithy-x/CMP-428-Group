package groupproject.gameengine.helpers;

public class MathHelper {

    private MathHelper() {

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
