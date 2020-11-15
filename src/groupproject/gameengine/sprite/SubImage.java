package groupproject.gameengine.sprite;

import groupproject.gameengine.helpers.MathHelper;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SubImage {

    private final int spritePositionStartX;
    private final int spritePositionStartY;
    private final int width;
    private final int height;
    private Image image;

    public SubImage(int x, int y, int width, int height) {
        this.spritePositionStartX = x;
        this.spritePositionStartY = y;
        this.width = width;
        this.height = height;
    }

    public Image getImage() {
        return image;
    }


    public void setImage(BufferedImage image) {
        this.setImage(image, 96);
    }

    public void setImage(BufferedImage image, float scale_aspect) {
        if (this.image == null) {
            BufferedImage subimage = image.getSubimage(spritePositionStartX, spritePositionStartY, width, height);
            float aspectRatio = MathHelper.getAspectRatio(width, height);
            float width = MathHelper.newWidth(scale_aspect, aspectRatio);
            //float height = MathHelper.newHeight(scale_aspect, aspectRatio);
            this.image = subimage.getScaledInstance((int) width, (int) scale_aspect, Image.SCALE_AREA_AVERAGING);
        }
    }

    public int getSpritePositionStartX() {
        return spritePositionStartX;
    }

    public int getSpritePositionStartY() {
        return spritePositionStartY;
    }

    public int getWidth() {
        return width;
    }


    public int getHeight() {
        return height;
    }
}
