package groupproject.gameengine.sprite;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import groupproject.gameengine.camera.GlobalCamera;
import groupproject.gameengine.contracts.CameraContract;
import groupproject.gameengine.contracts.CollisionDetection;
import groupproject.gameengine.contracts.Renderable;
import groupproject.gameengine.models.BoundingBox;

public abstract class Sprite implements Renderable, CameraContract, CollisionDetection {
    private static final String SPRITE_FOLDER = "assets/sprites/";
    protected Logger logger = Logger.getLogger("GameEngine", null);
    protected String currentDirection = "";
    protected HashMap<String, Animation> animDict = new HashMap<>();
    protected BoundingBox boundsRect;
    protected int velocity;

    public Sprite(int x, int y, String spritePrefix, int delay) {
        velocity = 1;
        loadBaseAnimations(spritePrefix, delay);
        initAnimations();
        this.boundsRect = new BoundingBox(x, y,
                getFirstAnimation().getCurrentFrame().getWidth(),
                getFirstAnimation().getCurrentFrame().getHeight());
    }

    // For initializing anymore animations besides 4 basic ones for the sprite.
    protected abstract void initAnimations();

    // Takes care of initializing animations for the 4 basic directions the sprite would face.
    // Can always override this to fit the needs of your sprite.
    protected void loadBaseAnimations(String prefix, int delay) {
        String[] directions = {"up", "down", "left", "right"};
        for (int i = 0; i < directions.length; i++) {
            Animation anim = new Animation(delay, String.join("_", prefix, directions[i]), getSpriteDirectory());
            animDict.put(directions[i], anim);
        }
    }

    // Draws the sprite's current image based on its current state.
    @Override
    public void render(Graphics g) {
        if (animDict.containsKey(currentDirection)) {
            BufferedImage currentFrame = animDict.get(currentDirection).getCurrentFrame();
            g.drawImage(currentFrame,
                    getCameraOffsetX(GlobalCamera.getInstance()).intValue() - currentFrame.getWidth() / 4,
                    getCameraOffsetY(GlobalCamera.getInstance()).intValue() - currentFrame.getHeight() / 4, null);
        } else {
            Animation firstAnim = getFirstAnimation();
            BufferedImage currentFrame = firstAnim.getCurrentFrame();
            g.drawImage(currentFrame,
                    getCameraOffsetX(GlobalCamera.getInstance()).intValue() - currentFrame.getWidth() / 4,
                    getCameraOffsetY(GlobalCamera.getInstance()).intValue() - currentFrame.getHeight() / 4,
                    null);
        }

        // For debug purposes, draw the bounding box of the sprite.
        g.setColor(Color.blue);
        boundsRect.render(g);
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

    public void move() {
        switch (currentDirection) {
            case "up":
                boundsRect.moveUp(velocity);
                break;
            case "down":
                boundsRect.moveDown(velocity);
                break;
            case "left":
                boundsRect.moveLeft(velocity);
                break;
            case "right":
                boundsRect.moveRight(velocity);
                break;
            default:
                break;
        }
    }

    public BoundingBox getBounds() {
        return boundsRect;
    }

    public String getSpriteDirectory() {
        return SPRITE_FOLDER + this.getClass().getSimpleName().toLowerCase();
    }

    public String getSpriteDirection() {
        return currentDirection;
    }

    public void setSpriteDirection(String currentDirection) {
        this.currentDirection = currentDirection;
    }

    public int getVelocity() {
        return velocity;
    }

    @Override
    public Number getX() {
        return boundsRect.getX();
    }

    @Override
    public Number getY() {
        return boundsRect.getY();
    }

    @Override
    public Number getWidth() {
        return boundsRect.getWidth();
    }

    @Override
    public Number getHeight() {
        return boundsRect.getHeight();
    }

    @Override
    public void setWidth(Number width) {
        boundsRect.setWidth(width);
    }

    @Override
    public void setHeight(Number height) {
        boundsRect.setHeight(height);
    }

    @Override
    public void gravitate() {
        boundsRect.gravitate();
    }

    @Override
    public void setX(Number x) {
        boundsRect.setX(x);
    }

    @Override
    public void setY(Number y) {
        boundsRect.setY(y);
    }

    @Override
    public void setVelocityX(Number velocityX) {
        boundsRect.setVelocityX(velocityX);
    }

    @Override
    public void setVelocityY(Number velocityY) {
        boundsRect.setVelocityY(velocityY);
    }

    @Override
    public void setAccelerationX(Number accelerationX) {
        boundsRect.setAccelerationX(accelerationX);
    }

    @Override
    public void setAccelerationY(Number accelerationY) {
        boundsRect.setAccelerationY(accelerationY);
    }

    @Override
    public void setDragX(Number dragX) {
        boundsRect.setDragX(dragX);
    }

    @Override
    public void setDragY(Number dragY) {
        boundsRect.setDragY(dragY);
    }

    @Override
    public Number getDragX() {
        return boundsRect.getDragX();
    }

    @Override
    public Number getDragY() {
        return boundsRect.getDragY();
    }

    @Override
    public Number getVelocityX() {
        return boundsRect.getVelocityX();
    }

    @Override
    public Number getVelocityY() {
        return boundsRect.getVelocityY();
    }

    @Override
    public Number getAccelerationX() {
        return boundsRect.getAccelerationX();
    }

    @Override
    public Number getAccelerationY() {
        return boundsRect.getAccelerationY();
    }


    @Override
    public void setWorldAngle(int worldAngle) {
        boundsRect.setWorldAngle(worldAngle);
    }

    @Override
    public int getWorldAngle() {
        return boundsRect.getWorldAngle();
    }

    @Override
    public Number getSinAngle() {
        return boundsRect.getSinAngle();
    }

    @Override
    public Number getCosAngle() {
        return boundsRect.getCosAngle();
    }
}
