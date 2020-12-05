package states;

import java.awt.image.BufferedImage;

public class AssetsMenu {
    public static BufferedImage[] btn_start;

    public static void init(){
        btn_start = new BufferedImage[2];
        btn_start[0] = ImageLoader.loadImage("/textures/startButton_1.png");
        btn_start[1] = ImageLoader.loadImage("/textures/startButton_2.png");
    }
}
