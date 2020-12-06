package groupproject.gameengine.sprite;

import groupproject.containers.zelda.sound.GlobalSoundEffect;
import groupproject.spritesheeteditor.models.FileFormat;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;

public abstract class Sprite extends AnimatedObject<EnumMap<Sprite.Pose, Animation>, FileFormat> {

    private static final String SPRITE_FOLDER = "assets/sprites/";
    private static final String SPRITE_SHEET_FOLDER = "assets/sheets/";

    protected Pose currentPose = Pose.RIGHT;

    protected Sprite(int x, int y, String spritePrefix, int delay) {
        super(x, y, spritePrefix, delay);
    }

    protected Sprite(String spriteSheet, int x, int y, int scaled, int delay) throws IOException {
        super(spriteSheet, x, y, scaled, delay);
    }

    protected Sprite(FileFormat format, int x, int y, int scaled, int delay) throws IOException {
        super(format, x, y, scaled, delay);
    }

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

    // For initializing anymore animations besides 4 basic ones for the sprite.
    protected abstract void initAnimations();

    public String getSpriteDirectory() {
        return SPRITE_FOLDER + this.getClass().getSimpleName().toLowerCase();
    }

    @SuppressWarnings("java:S1172")
    public EnumMap<Pose, Animation> setupImages(FileFormat format, BufferedImage image, int delay) {
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


    public Animation getCurrentAnimation() {
        boolean hasKey = animDict.containsKey(currentPose);
        if (hasKey) {
            return animDict.get(currentPose);
        } else {
            Collection<Animation> values = animDict.values();
            return values.toArray(new Animation[0])[0];
        }
    }


    @Override
    public String getSheetDirectory() {
        return SPRITE_SHEET_FOLDER;
    }

    @Override
    public String getFileDirectory() {
        return SPRITE_SHEET_FOLDER;
    }

    @Override
    public String getSoundEffectFile() {
        return currentPose.soundFileName;
    }

    // Draws the sprite's current image based on its current state.
    @Override
    public void render(Graphics g) {
        super.render(g);
        GlobalSoundEffect.getInstance().play(this);
        moving = false;
    }


    public void move() {
        this.direction = currentPose.direction;
        super.move();
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

    public enum Pose {
        UP(Direction.UP), DOWN(Direction.DOWN), LEFT(Direction.LEFT), RIGHT(Direction.RIGHT),
        ATTACK_UP("attack.wav", Direction.UP), ATTACK_DOWN("attack.wav", Direction.DOWN),
        ATTACK_LEFT("attack.wav", Direction.LEFT), ATTACK_RIGHT("attack.wav", Direction.RIGHT),
        SPIN_ATTACK("spin_attack.wav", Direction.NONE), ALL, JUMP, DEAD("dead.wav", Direction.NONE),
        ROLL_LEFT(Direction.LEFT), ROLL_RIGHT(Direction.RIGHT), ROLL_UP(Direction.UP), ROLL_DOWN(Direction.DOWN),
        ATTACK_UP_01("cane.wav", Direction.UP), ATTACK_DOWN_01("cane.wav",Direction.DOWN),
        ATTACK_LEFT_01("cane.wav", Direction.LEFT), ATTACK_RIGHT_01("cane.wav", Direction.RIGHT);

        private final String soundFileName;
        private final Direction direction;

        Pose() {
            this(Direction.NONE);
        }

        Pose(Direction direction){
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
