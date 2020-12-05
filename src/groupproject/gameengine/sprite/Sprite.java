package groupproject.gameengine.sprite;

import groupproject.containers.zelda.sound.GlobalSoundEffect;
import groupproject.gameengine.camera.GlobalCamera;
import groupproject.gameengine.contracts.CameraContract;
import groupproject.gameengine.contracts.CollisionDetection;
import groupproject.gameengine.contracts.Renderable;
import groupproject.gameengine.models.BoundingBox;
import groupproject.games.ZeldaTestGame;
import groupproject.spritesheeteditor.models.FileFormat;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Sprite implements Renderable, CameraContract, CollisionDetection {

    private static final String SPRITE_FOLDER = "assets/sprites/";
    private static final String SPRITE_SHEET_FOLDER = "assets/sheets/";
    protected Logger logger = Logger.getLogger("GameEngine", null);
    protected Pose currentPose = Pose.RIGHT;
    protected EnumMap<Pose, Animation> animDict = new EnumMap<>(Pose.class);
    protected CollisionDetection bounds;
    protected int velocity;
    protected int scaled = 1;
    protected boolean moving = false;

    protected Sprite(int x, int y, String spritePrefix, int delay) {
        velocity = 1;
        loadBaseAnimations(spritePrefix, delay);
        initAnimations();
        setupBox(x, y);
    }

    protected Sprite(String spriteSheet, int x, int y, int scaled, int delay) throws IOException {
        this.scaled = scaled;
        animDict.putAll(setupImages(initializeSheet(spriteSheet), delay));
        initAnimations();
        setupBox(x, y);
    }

    protected Sprite(FileFormat format, int x, int y, int scaled, int delay) throws IOException {
        this.scaled = scaled;
        animDict.putAll(setupImages(format, initializeSheet(format.getImage()), delay));
        initAnimations();
        setupBox(x, y);
    }

    public boolean isActive() {
        return moving;
    }

    // Takes care of initializing animations for the 4 basic directions the sprite would face.
    // Can always override this to fit the needs of your sprite.
    protected void loadBaseAnimations(String prefix, int delay) {
        String[] directions = Arrays.stream(Pose.values()).map(d -> d.name().toLowerCase()).toArray(String[]::new);
        for (String direction : directions) {
            Animation anim = new Animation(delay, String.join("_", prefix, direction), getSpriteDirectory());
            animDict.put(Pose.parse(direction), anim);
        }
    }

    // For initializing anymore animations besides 4 basic ones for the sprite.
    protected abstract void initAnimations();

    private void setupBox(int x, int y) {
        Image currentFrame = getFirstAnimation().getCurrentFrame();
        this.bounds = new BoundingBox(x, y, currentFrame.getWidth(null) / scaled, currentFrame.getHeight(null) / scaled);
    }

    public String getSpriteDirectory() {
        return SPRITE_FOLDER + this.getClass().getSimpleName().toLowerCase();
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

    @SuppressWarnings("java:S1172")
    private EnumMap<Pose, Animation> setupImages(FileFormat format, BufferedImage image, int delay) {
        EnumMap<Pose, Animation> map = new EnumMap<>(Pose.class);
        for (FileFormat.AnimationRow row : format.getPoses()) {
            Animation animation = Animation.with(delay);
            for (FileFormat.SpriteBounds spriteBounds : row.getSet()) {
                animation.addFrame(image.getSubimage(spriteBounds.getX(), spriteBounds.getY(), spriteBounds.getW(), spriteBounds.getH()));
            }
            map.put(Pose.parse(row.getPose()), animation);
        }
        return map;
    }

    @SuppressWarnings("java:S1172")
    protected EnumMap<Pose, Animation> setupImages(BufferedImage image, int delay) {
        return new EnumMap<>(Pose.class);
    }

    protected BufferedImage initializeSheet(String spriteSheet) throws IOException {
        return ImageIO.read(new File(String.format("%s%s", getSpriteSheetDirectory(), spriteSheet)));
    }

    public String getSpriteSheetDirectory() {
        return SPRITE_SHEET_FOLDER;
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

    public Animation getCurrentAnimation() {
        return animDict.containsKey(currentPose) ? animDict.get(currentPose) : getFirstAnimation();
    }

    // Draws the sprite's current image based on its current state.
    @Override
    public void render(Graphics g) {
        Image currentFrame;
        if (animDict.containsKey(currentPose)) {
            if (moving) {
                currentFrame = animDict.get(currentPose).getCurrentFrame();
            } else {
                currentFrame = animDict.get(currentPose).getFirstFrame();
            }
        } else {
            Animation firstAnim = getFirstAnimation();
            currentFrame = firstAnim.getCurrentFrame();
        }
        g.drawImage(currentFrame,
                getCameraOffsetX(GlobalCamera.getInstance()).intValue() - currentFrame.getWidth(null) / 4,
                getCameraOffsetY(GlobalCamera.getInstance()).intValue() - currentFrame.getHeight(null) / 4, null);
        if (ZeldaTestGame.inDebuggingMode()) {
            drawActualImageBounds(currentFrame, g);
            drawBounds(g);
        }
        GlobalSoundEffect.getInstance().play(this);
        moving = false;
    }

    public void drawActualImageBounds(Image currentFrame, Graphics g) {
        // For debug purposes, draw the bounding box of the sprite.
        g.setColor(Color.RED);
        g.drawRect(getCameraOffsetX(GlobalCamera.getInstance()).intValue() - currentFrame.getWidth(null) / 4,
                getCameraOffsetY(GlobalCamera.getInstance()).intValue() - currentFrame.getHeight(null) / 4,
                currentFrame.getWidth(null)
                , currentFrame.getHeight(null));
    }

    public void drawBounds(Graphics g) {
        // For debug purposes, draw the bounding box of the sprite.
        g.setColor(Color.blue);
        ((Renderable) bounds).render(g);
    }

    public void move() {
        switch (currentPose) {
            case ROLL_UP:
            case UP:
                moveUp(velocity);
                break;
            case ROLL_DOWN:
            case DOWN:
                moveDown(velocity);
                break;
            case ROLL_LEFT:
            case LEFT:
                moveLeft(velocity);
                break;
            case ROLL_RIGHT:
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

    public Pose getSpritePose() {
        return currentPose;
    }

    public void setSpritePose(Pose currentPose) {
        this.currentPose = currentPose;
    }

    public String getPoseSoundEffect() {
        return String.format("%s/%s", this.getClass().getSimpleName().toLowerCase(), currentPose.getSoundFileName());
    }

    public int getVelocity() {
        return velocity;
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

    public enum Pose {
        UP, DOWN, LEFT, RIGHT,
        ATTACK_UP("attack.wav"), ATTACK_DOWN("attack.wav"),
        ATTACK_LEFT("attack.wav"), ATTACK_RIGHT("attack.wav"),
        SPIN_ATTACK("spin_attack.wav"), ALL, JUMP, DEAD("dead.wav"),
        ROLL_LEFT, ROLL_RIGHT, ROLL_UP, ROLL_DOWN,
        ATTACK_UP_01("cane.wav"), ATTACK_DOWN_01("cane.wav"),
        ATTACK_LEFT_01("cane.wav"), ATTACK_RIGHT_01("cane.wav");

        private final String soundFileName;

        Pose() {
            this.soundFileName = null;
        }

        Pose(String soundFileName) {
            this.soundFileName = soundFileName;
        }

        public static Pose parse(String pose) {
            return Pose.valueOf(pose.toUpperCase());
        }

        public boolean hasSoundFile() {
            return soundFileName != null;
        }

        public String getSoundFileName() {
            return soundFileName;
        }
    }
}
