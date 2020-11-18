package groupproject.gameengine.sprite;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;

public class Animation {

    private Logger logger = Logger.getLogger("GameEngine", null);
    private List<Image> frames = new ArrayList<>();
    private Timer timer;
    private int delay = 500;
    private int currentFrame = 0;

    public Animation(int delay, String prefix, String directory) {
        this(delay);
        loadFrames(prefix, directory);
    }

    private Animation(int delay) {
        this.delay = delay;
        timer = new Timer();
        AnimateTask task = new AnimateTask();
        timer.scheduleAtFixedRate(task, 0, this.delay);
    }

    public static Animation with(int delay) {
        return new Animation(delay);
    }

    public void loadFrames(String prefix, String folder) {
        File directory = new File(folder);
        File[] frameFiles = directory.listFiles(file -> file.getName().startsWith(prefix));
        if (frameFiles.length == 0) {
            logger.log(Level.INFO, "No images file found for {0}.", prefix);
        }
        for (File f : frameFiles) {
            this.addFrame(f);
        }
    }

    public void addFrame(File frame) {
        try {
            this.addFrame(ImageIO.read(frame));
            logger.log(Level.INFO, "Loaded image file {0}.", frame.getName());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading image for animation frame.");
        }
    }

    public void addFrame(BufferedImage image) {
        frames.add(image);
    }

    public Image getCurrentFrame() {
        try {
            return frames.get(currentFrame);
        } catch (IndexOutOfBoundsException e) {
            logger.log(Level.SEVERE, "No image files loaded for the current animation!");
            return null;
        }
    }

    public void setFirstFrame(Image image) {
        frames.add(0, image);
    }

    public void scale(int scale) {
        frames = frames.stream()
                .map(frames -> frames.getScaledInstance(frames.getWidth(null) * scale, frames.getHeight(null) * scale, Image.SCALE_FAST))
                .collect(Collectors.toList());
    }

    public Image getFirstFrame() {
        if (!frames.isEmpty()) {
            return frames.get(0);
        }
        return null;
    }

    private class AnimateTask extends TimerTask {
        @Override
        public void run() {
            if (!frames.isEmpty()) {
                if (currentFrame < frames.size() - 1) {
                    currentFrame += 1;
                } else {
                    currentFrame = 0;
                }
            }
        }
    }
}
