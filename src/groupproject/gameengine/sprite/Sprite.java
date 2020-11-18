package groupproject.gameengine.sprite;

import groupproject.gameengine.camera.GlobalCamera;
import groupproject.gameengine.contracts.CameraContract;
import groupproject.gameengine.contracts.CollisionDetection;
import groupproject.gameengine.contracts.Renderable;
import groupproject.gameengine.models.BoundingBox;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Sprite implements Renderable, CameraContract, CollisionDetection {

    private static final String SPRITE_FOLDER = "assets/sprites/";
    private static final String SPRITE_SHEET_FOLDER = "assets/sheets/";
    protected Logger logger = Logger.getLogger("GameEngine", null);
    protected Direction currentDirection = Direction.RIGHT;
    protected HashMap<Direction, Animation> animDict = new HashMap<>();
    protected BoundingBox boundingBox;
    protected int velocity;
    protected int scaled = 1;
    protected boolean moving = false;

    public Sprite(int x, int y, String spritePrefix, int delay) {
        velocity = 1;
        loadBaseAnimations(spritePrefix, delay);
        initAnimations();
        setupBox(x, y);
    }

    public Sprite(String spritesheet, int x, int y, int scaled, int delay) throws IOException {
        this.scaled = scaled;
        animDict.putAll(setupImages(initializeSheet(spritesheet), delay));
        initAnimations();
        setupBox(x, y);
    }

    private void setupBox(int x, int y) {
        Image currentFrame = getFirstAnimation().getCurrentFrame();
        this.boundingBox = new BoundingBox(x, y, currentFrame.getWidth(null) / scaled, currentFrame.getHeight(null) / scaled);
    }

    protected BufferedImage pluck(BufferedImage image, int column, int row, int width, int height) {
        return image.getSubimage((column * width), row * height, width, height);
    }

    protected Animation getAnimation(BufferedImage image, int column, int row, int width, int height, int count, int delay) {
        Animation images = Animation.with(delay);
        for (int i = 0; i < count; i++) {
            int x = (column * width) + (i * width);
            int y = row * height;
            images.addFrame(image.getSubimage(x, y, width, height));
        }
        return images;
    }

    protected BufferedImage initializeSheet(String spriteSheet) throws IOException {
        return ImageIO.read(new File(String.format("%s%s", getSpriteSheetDirectory(), spriteSheet)));
    }

    protected HashMap<Direction, Animation> setupImages(BufferedImage image, int delay) {
        return new HashMap<>();
    }

    public String getSpriteSheetDirectory() {
        return SPRITE_SHEET_FOLDER;
    }

    // For initializing anymore animations besides 4 basic ones for the sprite.
    protected abstract void initAnimations();

    // Takes care of initializing animations for the 4 basic directions the sprite would face.
    // Can always override this to fit the needs of your sprite.
    protected void loadBaseAnimations(String prefix, int delay) {
        String[] directions = Arrays.stream(Direction.values()).map(d -> d.name().toLowerCase()).toArray(String[]::new);
        for (String direction : directions) {
            Animation anim = new Animation(delay, String.join("_", prefix, direction), getSpriteDirectory());
            animDict.put(Direction.parse(direction), anim);
        }
    }

    // Draws the sprite's current image based on its current state.
    @Override
    public void render(Graphics g) {
        if (animDict.containsKey(currentDirection)) {
            Image currentFrame;
            if (moving) {
                currentFrame = animDict.get(currentDirection).getCurrentFrame();
            } else {
                currentFrame = animDict.get(currentDirection).getFirstFrame();
            }
            g.drawImage(currentFrame,
                    getCameraOffsetX(GlobalCamera.getInstance()).intValue() - currentFrame.getWidth(null) / 4,
                    getCameraOffsetY(GlobalCamera.getInstance()).intValue() - currentFrame.getHeight(null) / 4, null);

            moving = false;
        } else {
            Animation firstAnim = getFirstAnimation();
            Image currentFrame = firstAnim.getCurrentFrame();
            g.drawImage(currentFrame,
                    getCameraOffsetX(GlobalCamera.getInstance()).intValue() - currentFrame.getWidth(null) / 4,
                    getCameraOffsetY(GlobalCamera.getInstance()).intValue() - currentFrame.getHeight(null) / 4,
                    null);
        }

        // For debug purposes, draw the bounding box of the sprite.
        g.setColor(Color.blue);
        boundingBox.render(g);
    }

    private Animation getFirstAnimation() {
        Optional<Animation> firstAnim = animDict.values().stream().findFirst();
        if (firstAnim.isPresent()) {
            return firstAnim.get();
        } else {
            logger.log(Level.SEVERE, "No animations created for {0}!", this.getClass().getSimpleName());
            throw new NoSuchElementException();
        }
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public void move() {
        switch (currentDirection) {
            case UP:
                moveUp(velocity);
                break;
            case DOWN:
                moveDown(velocity);
                break;
            case LEFT:
                moveLeft(velocity);
                break;
            case RIGHT:
                moveRight(velocity);
                break;
            default:
                break;
        }
    }

    public BoundingBox getBounds() {
        return boundingBox;
    }

    public String getSpriteDirectory() {
        return SPRITE_FOLDER + this.getClass().getSimpleName().toLowerCase();
    }

    public Direction getSpriteDirection() {
        return currentDirection;
    }

    public void setSpriteDirection(Direction currentDirection) {
        this.currentDirection = currentDirection;
    }

    public int getVelocity() {
        return velocity;
    }


    //region Override
    @Override
    public Number getX() {
        return boundingBox.getX();
    }

    @Override
    public Number getY() {
        return boundingBox.getY();
    }

    @Override
    public Number getWidth() {
        return boundingBox.getWidth();
    }

    @Override
    public Number getHeight() {
        return boundingBox.getHeight();
    }

    @Override
    public void setWidth(Number width) {
        boundingBox.setWidth(width);
    }

    @Override
    public void setHeight(Number height) {
        boundingBox.setHeight(height);
    }

    @Override
    public void gravitate() {
        boundingBox.gravitate();
    }

    @Override
    public void setX(Number x) {
        boundingBox.setX(x);
        if (x.intValue() > 0) {
            moving = true;
        }
    }

    @Override
    public void setY(Number y) {
        boundingBox.setY(y);
        if (y.intValue() > 0) {
            moving = true;
        }
    }

    @Override
    public void setVelocityX(Number velocityX) {
        boundingBox.setVelocityX(velocityX);
    }

    @Override
    public void setVelocityY(Number velocityY) {
        boundingBox.setVelocityY(velocityY);
    }

    @Override
    public void setAccelerationX(Number accelerationX) {
        boundingBox.setAccelerationX(accelerationX);
    }

    @Override
    public void setAccelerationY(Number accelerationY) {
        boundingBox.setAccelerationY(accelerationY);
    }

    @Override
    public void setDragX(Number dragX) {
        boundingBox.setDragX(dragX);
    }

    @Override
    public void setDragY(Number dragY) {
        boundingBox.setDragY(dragY);
    }

    @Override
    public Number getDragX() {
        return boundingBox.getDragX();
    }

    @Override
    public Number getDragY() {
        return boundingBox.getDragY();
    }

    @Override
    public Number getVelocityX() {
        return boundingBox.getVelocityX();
    }

    @Override
    public Number getVelocityY() {
        return boundingBox.getVelocityY();
    }

    @Override
    public Number getAccelerationX() {
        return boundingBox.getAccelerationX();
    }

    @Override
    public Number getAccelerationY() {
        return boundingBox.getAccelerationY();
    }


    @Override
    public void setWorldAngle(int worldAngle) {
        boundingBox.setWorldAngle(worldAngle);
    }

    @Override
    public int getWorldAngle() {
        return boundingBox.getWorldAngle();
    }

    @Override
    public Number getSinAngle() {
        return boundingBox.getSinAngle();
    }

    @Override
    public Number getCosAngle() {
        return boundingBox.getCosAngle();
    }

    //endregion

    public enum Direction {
        UP, DOWN, LEFT, RIGHT;

        public static Direction parse(String direction) {
            return Direction.valueOf(direction.toUpperCase());
        }
    }
}
