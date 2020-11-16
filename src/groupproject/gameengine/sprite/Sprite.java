package groupproject.gameengine.sprite;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import groupproject.gameengine.camera.GlobalCamera;
import groupproject.gameengine.contracts.CameraContract;
import groupproject.gameengine.contracts.Movable;
import groupproject.gameengine.contracts.Renderable;
import groupproject.gameengine.models.BoundingBox;

public abstract class Sprite implements Renderable, CameraContract, Movable {
    private static final String SPRITE_FOLDER = "assets/sprites/";
    protected int x;
    protected int y;
    protected Logger logger = Logger.getLogger("GameEngine", null);
    protected String currentDirection = "";
    protected HashMap<String, Animation> animDict = new HashMap<>();
    protected BoundingBox boundsRect;
    protected int velocity;

    public Sprite(int x, int y, String spritePrefix, int delay) {
        this.x = x;
        this.y = y;
        velocity = 1;
        loadBaseAnimations(spritePrefix, delay);
        initAnimations();
        this.boundsRect = new BoundingBox(this.x, this.y,
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
            g.drawImage(animDict.get(currentDirection).getCurrentFrame(),
                    getCameraOffsetX(GlobalCamera.getInstance()).intValue(),
                    getCameraOffsetY(GlobalCamera.getInstance()).intValue(), null);
        } else {
            Animation firstAnim = getFirstAnimation();
            g.drawImage(firstAnim.getCurrentFrame(),
                    getCameraOffsetX(GlobalCamera.getInstance()).intValue(),
                    getCameraOffsetY(GlobalCamera.getInstance()).intValue(),
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
                y -= velocity;
                boundsRect.moveUp(velocity);
                break;
            case "down":
                y += velocity;
                boundsRect.moveDown(velocity);
                break;
            case "left":
                x -= velocity;
                boundsRect.moveLeft(velocity);
                break;
            case "right":
                x += velocity;
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
        return x;
    }

    @Override
    public Number getY() {
        return y;
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
    public void setX(Number x) {
        this.x = x.intValue();
    }

    @Override
    public void setY(Number y) {
        this.y = y.intValue();
    }

}
