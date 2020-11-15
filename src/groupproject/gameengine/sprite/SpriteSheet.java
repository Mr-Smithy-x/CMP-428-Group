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
    protected int shift_right = 0;
    protected int shift_down;
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
    protected File file;
    protected int delay = 0;
    protected int duration = 10;
    protected int current_column = 0;
    protected int pose = 0;
    protected CollisionDetection circle;


    public SpriteSheet() throws IOException {
        initializeSheet();
        setupImages();
        circle = new BoundingCircle(0, 0, subImages[0][0].getWidth());
    }


    @Deprecated
    public void initEvenly(int rows, int columns, int width, int height) {
        for (int i_row = 0; i_row < rows; i_row++) {
            int roster_height = spriteSheet.getHeight();
            int real_height = height;
            for (int j_column = 0; j_column < columns; j_column++) {
                int roster_width = spriteSheet.getWidth();
                int real_width = width;
                //BufferedImage subimage = spriteSheet.getSubimage(j_column * width, i_row * height, width, height);
                //subimages[i_row][j_column] = new SubImage(subimage);
            }
        }
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
        if (current_column >= subImages[pose].length) {
            current_column = 0;
        }
        if (current_column < 0) {
            current_column = subImages[pose].length - 1;
        }
    }

    public void setShiftDown(int shift_down) {
        this.shift_down = shift_down;
    }

    public void setShiftRight(int shift_right) {
        this.shift_right = shift_right;
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
                (int) (getCameraOffsetX(GlobalCamera.getInstance()).doubleValue() - (image.getWidth(null) / 2) + shift_right),
                (int) (getCameraOffsetY(GlobalCamera.getInstance()).doubleValue() - (image.getHeight(null) / 2) + shift_down),
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
            current_column++;
            validate();
            delay = duration;
        }
        delay--;
    }

    protected void prevImageColumn() {
        if (delay == 0) {
            current_column--;
            validate();
            delay = duration;
        }
        delay--;
    }

    public Image getImage() {
        validate();
        SubImage[] subImage = subImages[pose];
        if (subImage.length <= current_column) {
            current_column = 0;
        }
        SubImage sub = subImage[current_column];
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
    public void setVelocity(Number velocity_x, Number velocity_y) {
        circle.setVelocity(velocity_x, velocity_y);
    }

    public void setAcceleration(Number accelerate_x, Number accelerate_y) {
        circle.setAcceleration(accelerate_x, accelerate_y);
    }

    public void setDrag(Number drag_x, Number drag_y) {
        circle.setDrag(drag_x, drag_y);
    }

    @Override
    public void setWorldAngle(int world_angle) {
        circle.setWorldAngle(world_angle);
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
    public void setVelocityX(Number velocity_x) {
        circle.setVelocityX(velocity_x);
    }

    @Override
    public void setVelocityY(Number velocity_y) {
        circle.setVelocityY(velocity_y);
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
    public void setAccelerationX(Number acceleration_x) {
        this.circle.setAccelerationX(acceleration_x);
    }

    @Override
    public void setAccelerationY(Number acceleration_y) {
        this.circle.setAccelerationY(acceleration_y);
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
    public void setDragX(Number drag_x) {
        circle.setDragX(drag_x);
    }

    @Override
    public void setDragY(Number drag_y) {
        circle.setDragY(drag_y);
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
