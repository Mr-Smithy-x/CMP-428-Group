package groupproject.gameengine.sprite;

import groupproject.containers.zelda.sound.GlobalSoundEffect;
import groupproject.gameengine.camera.GlobalCamera;
import groupproject.spritesheeteditor.models.FileFormat;
import groupproject.spritesheeteditor.models.PoseFileFormat;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public abstract class Sprite extends AnimatedObject<EnumMap<Sprite.Pose, Animation>, PoseFileFormat> {

    private static final String POSE_FOLDER = "./assets/poses/";

    protected Pose currentPose = Pose.RIGHT;

    protected Sprite(int x, int y, String spritePrefix, int delay) {
        super(x, y, spritePrefix, delay);
        check();
    }

    protected Sprite(PoseFileFormat format, int x, int y, int scaled, int delay) throws IOException {
        super(format, x, y, scaled, delay);
        check();
    }

    protected Sprite(String spriteSheet, int x, int y, int scaled, int delay) throws IOException {
        super(spriteSheet, x, y, scaled, delay);
        check();
    }

    /**
     * For initializing anymore animations besides 4 basic ones for the sprite.
     */
    protected abstract void onInitAnimations();

    /**
     * validating that you have the basic animations, up down left right
     */
    private void check()  {
        List<Pose> poses = new ArrayList<>(Arrays.asList(Pose.UP, Pose.DOWN, Pose.LEFT, Pose.RIGHT));
        poses.removeIf(p -> animDict.keySet().contains(p));
        if(!poses.isEmpty()) {
            String format = String.format("The following poses are missing: %s", poses.stream().map(pose -> pose.name()).collect(Collectors.joining(",")));
            logger.log(Level.SEVERE, format);
            return;
        }
        logger.log(Level.FINE, "All basic poses were initialized!");
    }

    public Animation getCurrentAnimation() {
        boolean hasKey = animDict.containsKey(currentPose);
        if (hasKey) {
            return animDict.get(currentPose);
        } else {
            Collection<Animation> values = animDict.values();
            return values.toArray(new Animation[0])[0];
        }
    }

    // Draws the sprite's current image based on its current state.
    public Pose getSpritePose() {
        return currentPose;
    }

    public void setSpritePose(Pose currentPose) {
        this.currentPose = currentPose;
        this.direction = currentPose.direction;
    }

    //This is for fetching sound effects in the future for specific class by their classname
    //ie. ./assets/sounds/effects/(classname)/filename.wav
    public String getPoseSoundEffect() {
        return String.format("%s/%s", this.getClass().getSimpleName().toLowerCase(), currentPose.getSoundFileName());
    }

    //region Override Methods

    // Takes care of initializing animations for the 4 basic directions the sprite would face.
    // Can always override this to fit the needs of your sprite.
    @Override
    protected EnumMap<Pose, Animation> setupImages(String spritePrefix, int delay) {
        EnumMap<Pose, Animation> map = new EnumMap<>(Pose.class);
        String[] directions = Arrays.stream(Sprite.Pose.values()).map(d -> d.name().toLowerCase()).toArray(String[]::new);
        for (String direction : directions) {
            String filename = String.join("_", spritePrefix, direction);
            String directory = getSpriteDirectory();
            if (Animation.isValidDirectory(filename, directory)) {
                Animation anim = new Animation(delay, filename, directory);
                map.put(Sprite.Pose.parse(direction), anim);
            }
        }
        return map;
    }

    @Override
    protected EnumMap<Pose, Animation> setupImages(PoseFileFormat format, BufferedImage image, int delay) {
        EnumMap<Pose, Animation> map = new EnumMap<>(Pose.class);
        for (FileFormat.AnimationRow row : format.getPoses()) {
            Animation animation = Animation.with(delay);
            for (FileFormat.Bounds bounds : row.getSet()) {
                animation.addFrame(image.getSubimage(bounds.getX(), bounds.getY(), bounds.getW(), bounds.getH()));
            }
            map.put(Pose.parse(row.getPose()), animation);
        }
        return map;
    }

    @Override
    protected EnumMap<Pose, Animation> setupImages(BufferedImage image, int delay) {
        return new EnumMap<>(Pose.class);
    }

    @Override
    public Animation getSafeAnimation() {
        return animDict.get(Pose.parse(currentPose.direction.name()));
    }

    @Override
    public String getSoundEffectFile() {
        return currentPose.soundFileName;
    }

    @Override
    public void render(Graphics g) {
        GlobalSoundEffect.getInstance().play(this);
        super.render(g);
    }

    @Override
    public Direction getDirection() {
        return currentPose.direction;
    }

    @Override
    public void move() {
        this.direction = currentPose.direction;
        super.move();
    }
    //endregion

    public enum Pose {
        UP(Direction.UP), DOWN(Direction.DOWN), LEFT(Direction.LEFT), RIGHT(Direction.RIGHT),
        ATTACK_UP("attack.wav", Direction.UP), ATTACK_DOWN("attack.wav", Direction.DOWN),
        ATTACK_LEFT("attack.wav", Direction.LEFT), ATTACK_RIGHT("attack.wav", Direction.RIGHT),
        SPIN_ATTACK("spin_attack.wav", Direction.NONE), ALL, JUMP, DEAD("dead.wav", Direction.NONE),
        ROLL_LEFT(Direction.LEFT), ROLL_RIGHT(Direction.RIGHT), ROLL_UP(Direction.UP), ROLL_DOWN(Direction.DOWN),
        ATTACK_UP_01(Direction.UP), ATTACK_DOWN_01(Direction.DOWN),
        ATTACK_LEFT_01(Direction.LEFT), ATTACK_RIGHT_01(Direction.RIGHT);

        private final String soundFileName;
        private final Direction direction;

        Pose() {
            this(Direction.NONE);
        }

        Pose(Direction direction) {
            this.soundFileName = null;
            this.direction = direction;
        }

        Pose(String soundFileName, Direction direction) {
            this.soundFileName = soundFileName;
            this.direction = direction;
        }

        public Direction getDirection() {
            return direction;
        }

        public static Pose parse(String pose) {
            if (pose.equals("NONE")) {
                return DOWN;
            }
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
