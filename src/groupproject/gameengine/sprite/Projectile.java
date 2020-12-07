package groupproject.gameengine.sprite;

import groupproject.containers.zelda.sound.GlobalSoundEffect;
import groupproject.spritesheeteditor.models.PoseFileFormat;
import groupproject.spritesheeteditor.models.ProjectileFileFormat;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class Projectile extends AnimatedObject<Animation, ProjectileFileFormat> {

    private static final String PROJECTILE_FOLDER = "assets/projectiles/";
    private static final String PROJECTILE_SHEET_FOLDER = "assets/sheets/";

    protected Projectile(ProjectileFileFormat format, int x, int y, int scaled, int delay) throws IOException {
        super(format, x, y, scaled, delay);
    }

    @Override
    @SuppressWarnings("java:S1172")
    protected Animation setupImages(ProjectileFileFormat format, BufferedImage image, int delay) {
        Animation animation = Animation.with(delay);
        for (PoseFileFormat.Bounds bounds : format.getSet()) {
            animation.addFrame(image.getSubimage(bounds.getX(), bounds.getY(), bounds.getW(), bounds.getH()));
        }
        return animation;
    }


    @Override
    protected Animation setupImages(BufferedImage initializeSheet, int delay) {
        return Animation.with(delay);
    }


    public String getSpriteDirectory() {
        return SPRITE_FOLDER + this.getClass().getSimpleName().toLowerCase();
    }


    @Override
    protected Animation setupImages(String spritePrefix, int delay) {
        String filename = String.join("_", spritePrefix, "");
        String directory = getSpriteDirectory();
        if (Animation.isValidDirectory(filename, directory)) {
            return new Animation(delay, filename, directory);
        }
        return null;
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        GlobalSoundEffect.getInstance().play(GlobalSoundEffect.Clips.CANE);
    }

    // For initializing anymore animations besides 4 basic ones for the projectiles.

    @Override
    public Animation getCurrentAnimation() {
        return animDict;
    }

    protected BufferedImage initializeSheet(String spriteSheet) throws IOException {
        return ImageIO.read(new File(String.format("%s%s", getSheetDirectory(), spriteSheet)));
    }

    @Override
    public String getSheetDirectory() {
        return PROJECTILE_SHEET_FOLDER;
    }

    @Override
    public String getFileDirectory() {
        return PROJECTILE_FOLDER;
    }

}