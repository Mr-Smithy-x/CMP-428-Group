package groupproject.gameengine.sprite;

import javax.imageio.ImageIO;
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

public class Animation {

    private final Logger logger = Logger.getLogger("GameEngine", null);
    private final Timer timer;
    private List<Image> frames = new ArrayList<>();
    private int currentFrame = 0;
    private boolean paused = false;

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isPaused() {
        return paused;
    }

    public Animation(int delay, String prefix, String directory) {
        this(delay);
        loadFrames(prefix, directory);
    }

    private Animation(int delay) {
        timer = new Timer();
        AnimateTask task = new AnimateTask();
        timer.scheduleAtFixedRate(task, 0, delay);
    }

    public static Animation with(int delay) {
        return new Animation(delay);
    }

    public static boolean isValidDirectory(String prefix, String folder) {
        File directory = new File(folder);
        File[] frameFiles = directory.listFiles(file -> file.getName().startsWith(prefix));
        return frameFiles != null && frameFiles.length > 0;
    }

    public void loadFrames(String prefix, String folder) {
        if (isValidDirectory(prefix, folder)) {
            File directory = new File(folder);
            File[] frameFiles = directory.listFiles(file -> file.getName().startsWith(prefix));
            for (File f : frameFiles) {
                this.addFrame(f);
            }
        } else {
            logger.log(Level.INFO, "No images file found for {0}.", prefix);
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

    public int getCurrentFrameIndex() {
        return currentFrame;
    }

    public void scale(int scale) {
        frames = frames.stream()
                .map(scaledFrames -> scaledFrames.getScaledInstance(scaledFrames.getWidth(null) * scale, scaledFrames.getHeight(null) * scale, Image.SCALE_FAST))
                .collect(Collectors.toList());
    }

    public boolean isLastFrame() {
        return currentFrame == frames.size() - 1;
    }

    public boolean isFirstFrame() {
        return currentFrame == 0;
    }

    public Image getFirstFrame() {
        if (!frames.isEmpty()) {
            return frames.get(0);
        }
        return null;
    }

    public void setFirstFrame(Image image) {
        frames.add(0, image);
    }

    public void holdLastFrame() {
        if (currentFrame == frames.size() - 1) {
            currentFrame -= 1;
        }
    }

    private class AnimateTask extends TimerTask {
        @Override
        public void run() {
            if (!frames.isEmpty()) {
                if(!paused) {
                    if (currentFrame < frames.size() - 1) {
                        currentFrame += 1;
                    } else {
                        currentFrame = 0;
                    }
                }
            }
        }
    }
}
