package groupproject.gameengine.camera;


public class GlobalCamera extends BaseCamera {

    private static GlobalCamera instance;


    public GlobalCamera() {
        super(0, 0);
    }

    public static GlobalCamera getInstance() {
        if (instance == null) {
            instance = new GlobalCamera();
        }
        return instance;
    }

}
