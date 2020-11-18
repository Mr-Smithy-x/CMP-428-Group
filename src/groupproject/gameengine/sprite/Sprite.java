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
    protected CollisionDetection bounds;
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
        this.bounds = new BoundingBox(x, y, currentFrame.getWidth(null) / scaled, currentFrame.getHeight(null) / scaled);
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
                System.out.println(currentDirection);
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
    }

    public void drawBounds(Graphics g){
        // For debug purposes, draw the bounding box of the sprite.
        g.setColor(Color.blue);
        ((Renderable) bounds).render(g);
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

    public CollisionDetection getBounds() {
        return bounds;
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
        return bounds.getX();
    }

    @Override
    public Number getY() {
        return bounds.getY();
    }

    @Override
    public Number getWidth() {
        return bounds.getWidth();
    }

    @Override
    public Number getHeight() {
        return bounds.getHeight();
    }

    @Override
    public void setWidth(Number width) {
        bounds.setWidth(width);
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
    public void setX(Number x) {
        bounds.setX(x);
        if (x.intValue() > 0) {
            moving = true;
        }
    }

    @Override
    public void setY(Number y) {
        bounds.setY(y);
        if (y.intValue() > 0) {
            moving = true;
        }
    }

    @Override
    public void setVelocityX(Number velocityX) {
        bounds.setVelocityX(velocityX);
    }

    @Override
    public void setVelocityY(Number velocityY) {
        bounds.setVelocityY(velocityY);
    }

    @Override
    public void setAccelerationX(Number accelerationX) {
        bounds.setAccelerationX(accelerationX);
    }

    @Override
    public void setAccelerationY(Number accelerationY) {
        bounds.setAccelerationY(accelerationY);
    }

    @Override
    public void setDragX(Number dragX) {
        bounds.setDragX(dragX);
    }

    @Override
    public void setDragY(Number dragY) {
        bounds.setDragY(dragY);
    }

    @Override
    public Number getDragX() {
        return bounds.getDragX();
    }

    @Override
    public Number getDragY() {
        return bounds.getDragY();
    }

    @Override
    public Number getVelocityX() {
        return bounds.getVelocityX();
    }

    @Override
    public Number getVelocityY() {
        return bounds.getVelocityY();
    }

    @Override
    public Number getAccelerationX() {
        return bounds.getAccelerationX();
    }

    @Override
    public Number getAccelerationY() {
        return bounds.getAccelerationY();
    }


    @Override
    public void setWorldAngle(int worldAngle) {
        bounds.setWorldAngle(worldAngle);
    }

    @Override
    public int getWorldAngle() {
        return bounds.getWorldAngle();
    }

    @Override
    public Number getSinAngle() {
        return bounds.getSinAngle();
    }

    @Override
    public Number getCosAngle() {
        return bounds.getCosAngle();
    }

    //endregion

    public enum Direction {
        UP, DOWN, LEFT, RIGHT, ALL, JUMP, ATTACK_UP, ATTACK_DOWN, ATTACK_LEFT, ATTACK_RIGHT, SPIN_ATTACK;

        public static Direction parse(String direction) {
            return Direction.valueOf(direction.toUpperCase());
        }
    }
}
