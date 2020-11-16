package groupproject.gameengine.sprite;


import groupproject.gameengine.camera.GlobalCamera;
import groupproject.gameengine.contracts.CameraContract;
import groupproject.gameengine.contracts.CollisionDetection;
import groupproject.gameengine.contracts.Renderable;
import groupproject.gameengine.models.BoundingCircle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class SpriteSheet
        implements CollisionDetection, Renderable, CameraContract {


    private static final String SPRITE_SHEET_FOLDER = "assets/sheets/";
    protected int shiftRight = 0;
    protected int shiftDown;
    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
    public static final int JUMP = 4;
    public static final int ATTACK_UP = 5;
    public static final int ATTACK_DOWN = 6;
    public static final int ATTACK_LEFT = 7;
    public static final int ATTACK_RIGHT = 8;
    public static final int SPIN_ATTACK = 9;

    protected boolean moving = false;
    protected BufferedImage spriteSheet;
    protected SubImage[][] subImages = new SubImage[16][];
    protected SubImage[] stillImages = new SubImage[16];
    protected int delay = 0;
    protected int duration = 10;
    protected int currentColumn = 0;
    protected int pose = 0;
    protected CollisionDetection circle;


    public SpriteSheet() throws IOException {
        initializeSheet();
        setupImages();
        circle = new BoundingCircle(0, 0, subImages[0][0].getWidth());
    }

    protected SubImage[] initAnimation(int column, int row, int width, int height, int size) {
        SubImage[] images = new SubImage[size];
        for (int i = 0; i < size; i++) {
            images[i] = new SubImage((column * width) + (i * width), row * height, width, height);
        }
        return images;
    }


    protected abstract void setupImages();

    protected void validate() {
        if (currentColumn >= subImages[pose].length) {
            currentColumn = 0;
        }
        if (currentColumn < 0) {
            currentColumn = subImages[pose].length - 1;
        }
    }

    public void setShiftDown(int shiftDown) {
        this.shiftDown = shiftDown;
    }

    public void setShiftRight(int shiftRight) {
        this.shiftRight = shiftRight;
    }

    @Override
    public void render(Graphics g) {
        Image image;
        if (moving) {
            image = getImage();
        } else {
            image = getStillImage();
        }
        g.drawImage(image,
                (int) (getCameraOffsetX(GlobalCamera.getInstance()).doubleValue() - (image.getWidth(null) / 2) + shiftRight),
                (int) (getCameraOffsetY(GlobalCamera.getInstance()).doubleValue() - (image.getHeight(null) / 2) + shiftDown),
                image.getWidth(null),
                image.getHeight(null),
                null
        );
        moving = false;
        if (GlobalCamera.DEBUG) {
            ((Renderable) circle).render(g);
        }
    }

    protected void nextImageColumn() {
        if (delay == 0) {
            currentColumn++;
            validate();
            delay = duration;
        }
        delay--;
    }

    protected void prevImageColumn() {
        if (delay == 0) {
            currentColumn--;
            validate();
            delay = duration;
        }
        delay--;
    }

    public Image getImage() {
        validate();
        SubImage[] subImage = subImages[pose];
        if (subImage.length <= currentColumn) {
            currentColumn = 0;
        }
        SubImage sub = subImage[currentColumn];
        sub.setImage(spriteSheet);
        return sub.getImage();
    }

    public Image getStillImage() {
        SubImage stillImage = stillImages[pose];
        stillImage.setImage(spriteSheet);
        return stillImage.getImage();//spriteSheet.getSubimage(stillImage.spritePositionStartX, stillImage.spritePositionStartY, stillImage.width, stillImage.height);
    }

    protected void initializeSheet() throws IOException {
        spriteSheet = ImageIO.read(new File(getSpriteSheetDirectory() + ".png"));
    }

    public String getSpriteSheetDirectory() {
        return SPRITE_SHEET_FOLDER + this.getClass().getSimpleName().toLowerCase();
    }

    public void setPose(int pose) {
        moving = false;
        this.pose = pose;

        switch (pose) {
            case LEFT:
                circle.setWorldAngle(180);
                break;
            case DOWN:
                circle.setWorldAngle(90);
                break;
            case RIGHT:
                circle.setWorldAngle(0);
                break;
            case UP:
                circle.setWorldAngle(270);
                break;
        }
        nextImageColumn();
    }

    public int getPose() {
        return pose;
    }

    @Override
    public void setVelocity(Number velocityX, Number velocityY) {
        circle.setVelocity(velocityX, velocityY);
    }

    public void setAcceleration(Number accelerateX, Number accelerateY) {
        circle.setAcceleration(accelerateX, accelerateY);
    }

    public void setDrag(Number dragX, Number dragY) {
        circle.setDrag(dragX, dragY);
    }

    @Override
    public void setWorldAngle(int worldAngle) {
        circle.setWorldAngle(worldAngle);
    }

    @Override
    public int getWorldAngle() {
        return circle.getWorldAngle();
    }

    @Override
    public Number getSinAngle() {
        return circle.getSinAngle();
    }

    @Override
    public Number getCosAngle() {
        return circle.getCosAngle();
    }

    @Override
    public void moveBy(Number dx, Number dy) {
        moving = true;
        circle.moveBy(dx, dy);
        if (dx.doubleValue() < dy.doubleValue()) {
            if (dy.doubleValue() > 0) {
                pose = DOWN;
            }
            if (dy.doubleValue() < 0) {
                pose = UP;
            }
            if (dx.doubleValue() < 0) {
                pose = LEFT;
            }
            if (dx.doubleValue() > 0) {
                pose = RIGHT;
            }
        } else if (dy.doubleValue() < dx.doubleValue()) {
            if (dy.doubleValue() > 0) {
                pose = DOWN;
            }
            if (dy.doubleValue() < 0) {
                pose = UP;
            }
            if (dx.doubleValue() < 0) {
                pose = LEFT;
            }
            if (dx.doubleValue() > 0) {
                pose = RIGHT;
            }
        }
        switch (pose) {
            case LEFT:
                circle.setWorldAngle(180);
                break;
            case DOWN:
                circle.setWorldAngle(90);
                break;
            case RIGHT:
                circle.setWorldAngle(0);
                break;
            case UP:
                circle.setWorldAngle(270);
                break;
        }
        nextImageColumn();
    }

    @Override
    public Number getX() {
        return circle.getX();
    }

    @Override
    public Number getY() {
        return circle.getY();
    }

    @Override
    public void setX(Number x) {
        circle.setX(x);
    }

    @Override
    public void setY(Number y) {
        circle.setY(y);
    }

    @Override
    public Number getWidth() {
        return getImage().getWidth(null);
    }

    @Override
    public Number getHeight() {
        return getImage().getHeight(null);
    }

    @Override
    public void setWidth(Number width) {
        //TODO: nothing
    }

    @Override
    public void setHeight(Number height) {
        //TODO: nothing
    }

    @Override
    public Number getRadius() {
        return circle.getRadius();
    }

    @Override
    public Number getVelocityX() {
        return circle.getVelocityX();
    }

    @Override
    public Number getVelocityY() {
        return circle.getVelocityY();
    }

    @Override
    public void setVelocityX(Number velocityX) {
        circle.setVelocityX(velocityX);
    }

    @Override
    public void setVelocityY(Number velocityY) {
        circle.setVelocityY(velocityY);
    }

    @Override
    public Number getAccelerationX() {
        return circle.getAccelerationX();
    }

    @Override
    public Number getAccelerationY() {
        return circle.getAccelerationY();
    }

    @Override
    public void setAccelerationX(Number accelerationX) {
        this.circle.setAccelerationX(accelerationX);
    }

    @Override
    public void setAccelerationY(Number accelerationY) {
        this.circle.setAccelerationY(accelerationY);
    }

    @Override
    public void gravitate() {
        circle.gravitate();
        nextImageColumn();
        moving = true;
    }

    @Override
    public void setWorld(Number x, Number y) {
        circle.setWorld(x, y);
    }

    @Override
    public void setDragX(Number dragX) {
        circle.setDragX(dragX);
    }

    @Override
    public void setDragY(Number dragY) {
        circle.setDragY(dragY);
    }

    @Override
    public Number getDragX() {
        return circle.getDragX();
    }

    @Override
    public Number getDragY() {
        return circle.getDragY();
    }


}
