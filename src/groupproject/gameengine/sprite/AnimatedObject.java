package groupproject.gameengine.sprite;

import groupproject.gameengine.camera.GlobalCamera;
import groupproject.gameengine.contracts.CameraContract;
import groupproject.gameengine.contracts.CollisionDetection;
import groupproject.gameengine.contracts.Renderable;
import groupproject.gameengine.models.BoundingBox;
import groupproject.spritesheeteditor.models.FileFormat;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AnimatedObject<T, F extends FileFormat> implements Renderable, CameraContract, CollisionDetection {

    protected static final String SPRITE_SHEET_FOLDER = "assets/sheets/";
    protected static final String SPRITE_FOLDER = "assets/sprites/";

    protected Logger logger = Logger.getLogger("GameEngine", null);
    protected T animDict;
    protected Direction direction = Direction.NONE;
    protected CollisionDetection bounds;
    protected int velocity;
    protected double scaled = 1;
    protected boolean moving = false;

    protected AnimatedObject(F format, int x, int y, double scaled, int delay) throws IOException {
        this.scaled = scaled;
        animDict = setupImages(format, initializeSheet(format.getImage()), delay);
        onInitAnimations();
        setupBox(x, y);
    }

    protected AnimatedObject(String spriteSheet, int x, int y, double scaled, int delay) throws IOException {
        this.scaled = scaled;
        animDict = setupImages(initializeSheet(spriteSheet), delay);
        onInitAnimations();
        setupBox(x, y);
    }

    protected AnimatedObject(int x, int y, String spritePrefix, int delay) {
        velocity = 1;
        animDict = setupImages(spritePrefix, delay);
        onInitAnimations();
        setupBox(x, y);
    }


    // For initializing anymore animations besides 4 basic ones for the projectiles.

    protected abstract void onInitAnimations();

    public abstract Animation getCurrentAnimation();

    public abstract Animation getSafeAnimation();

    public String getSheetDirectory() {
        return SPRITE_SHEET_FOLDER;
    }

    public String getSpriteDirectory() {
        return SPRITE_FOLDER + this.getClass().getSimpleName().toLowerCase();
    }

    public abstract String getSoundEffectFile();

    private void setupBox(int x, int y) {
        Image currentFrame = getFirstAnimation().getCurrentFrame();
        this.bounds = new BoundingBox(x, y, (int) (currentFrame.getWidth(null) / scaled), (int) (currentFrame.getHeight(null) / scaled));
    }

    protected Animation getFirstAnimation() {
        Animation currentAnimation = getCurrentAnimation();
        if (currentAnimation != null) {
            return currentAnimation;
        } else {
            logger.log(Level.SEVERE, "No animations created for {0}!", this.getClass().getSimpleName());
            throw new NoSuchElementException();
        }
    }

    @SuppressWarnings("java:S1172")
    protected abstract T setupImages(F format, BufferedImage image, int delay);

    protected abstract T setupImages(BufferedImage initializeSheet, int delay);

    protected abstract T setupImages(String spritePrefix, int delay);


    protected BufferedImage initializeSheet(String spriteSheet) throws IOException {
        return ImageIO.read(new File(String.format("%s%s", getSheetDirectory(), spriteSheet)));
    }

    protected BufferedImage pluck(BufferedImage image, int column, int row, int width, int height) {
        return image.getSubimage((column * width), row * height, width, height);
    }

    protected Animation parseAnimation(BufferedImage image, int column, int row, int width, int height, int count, int delay) {
        Animation images = Animation.with(delay);
        for (int i = 0; i < count; i++) {
            int x = (column * width) + (i * width);
            int y = row * height;
            images.addFrame(image.getSubimage(x, y, width, height));
        }
        return images;
    }

    // Draws the sprite's current image based on its current state.
    @Override
    public void render(Graphics g) {
        if(canRender(GlobalCamera.getInstance())) {
            Image currentFrame;
            if (moving) {
                currentFrame = getCurrentAnimation().getCurrentFrame();
            } else {
                currentFrame = getCurrentAnimation().getFirstFrame();
            }
            if (currentFrame == null) {
                Animation safeAnimation = getSafeAnimation();
                if (moving) {
                    currentFrame = safeAnimation.getCurrentFrame();
                } else {
                    currentFrame = safeAnimation.getFirstFrame();
                }
            }
            g.drawImage(currentFrame,
                    getDrawImageXPosition(currentFrame),
                    getDrawImageYPosition(currentFrame),
                    (int) (currentFrame.getWidth(null) * scaled),
                    (int) (currentFrame.getHeight(null) * scaled), null);
            if (inDebuggingMode()) {
                drawActualImageBounds(currentFrame, g);
                drawBounds(g);
            }
        }
        moving = false;
    }

    /**
     * Need this to override
     *
     * @param currentFrame
     * @return
     */
    protected int getDrawImageXPosition(Image currentFrame) {
        return getCameraOffsetX(GlobalCamera.getInstance()).intValue() - currentFrame.getWidth(null) / 4;
    }

    /**
     * To Override
     *
     * @param currentFrame
     * @return
     */
    protected int getDrawImageYPosition(Image currentFrame) {
        return getCameraOffsetY(GlobalCamera.getInstance()).intValue() - currentFrame.getHeight(null) / 4;
    }

    public void drawActualImageBounds(Image currentFrame, Graphics g) {
        // For debug purposes, draw the bounding box of the sprite.
        g.setColor(Color.RED);
        g.drawRect(getDrawImageXPosition(currentFrame),
                getDrawImageYPosition(currentFrame),
                (int)(currentFrame.getWidth(null) * scaled),
                (int)(currentFrame.getHeight(null) * scaled));
    }

    public void drawBounds(Graphics g) {
        // For debug purposes, draw the bounding box of the sprite.
        g.setColor(Color.blue);
        ((Renderable) bounds).render(g);
    }

    public void move() {
        switch (direction) {
            case UP:
                moveUp(velocity * GlobalCamera.getInstance().getScaling());
                break;
            case DOWN:
                moveDown(velocity * GlobalCamera.getInstance().getScaling());
                break;
            case LEFT:
                moveLeft(velocity * GlobalCamera.getInstance().getScaling());
                break;
            case RIGHT:
                moveRight(velocity * GlobalCamera.getInstance().getScaling());
                break;
            default:
                break;
        }
    }

    public CollisionDetection getBounds() {
        return bounds;
    }

    public boolean isActive() {
        return moving;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getVelocity() {
        return (int) (velocity * GlobalCamera.getInstance().getScaling());
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    @Override
    public Number getX() {
        return bounds.getX();
    }

    @Override
    public void setX(Number x) {
        bounds.setX(x);
        if (x.intValue() > 0) {
            moving = true;
        }
    }

    @Override
    public Number getY() {
        return bounds.getY();
    }

    @Override
    public void setY(Number y) {
        bounds.setY(y);
        if (y.intValue() > 0) {
            moving = true;
        }
    }

    @Override
    public Number getWidth() {
        return bounds.getWidth();
    }

    @Override
    public void setWidth(Number width) {
        bounds.setWidth(width);
    }

    public Image getCurrentFrame() {
        return getCurrentAnimation().getCurrentFrame();
    }

    @Override
    public Number getHeight() {
        return bounds.getHeight();
    }

    @Override
    public void setHeight(Number height) {
        bounds.setHeight(height);
    }

    @Override
    public void gravitate() {
        bounds.gravitate();
    }

    @Override
    public Number getDragX() {
        return bounds.getDragX();
    }

    @Override
    public void setDragX(Number dragX) {
        bounds.setDragX(dragX);
    }

    @Override
    public Number getDragY() {
        return bounds.getDragY();
    }

    @Override
    public void setDragY(Number dragY) {
        bounds.setDragY(dragY);
    }

    @Override
    public Number getVelocityX() {
        return bounds.getVelocityX();
    }

    @Override
    public void setVelocityX(Number velocityX) {
        bounds.setVelocityX(velocityX);
    }

    @Override
    public Number getVelocityY() {
        return bounds.getVelocityY();
    }

    @Override
    public void setVelocityY(Number velocityY) {
        bounds.setVelocityY(velocityY);
    }

    @Override
    public Number getAccelerationX() {
        return bounds.getAccelerationX();
    }

    @Override
    public void setAccelerationX(Number accelerationX) {
        bounds.setAccelerationX(accelerationX);
    }

    @Override
    public Number getAccelerationY() {
        return bounds.getAccelerationY();
    }

    @Override
    public void setAccelerationY(Number accelerationY) {
        bounds.setAccelerationY(accelerationY);
    }

    public enum Direction {
        NONE, UP, DOWN, LEFT, RIGHT
    }

}
