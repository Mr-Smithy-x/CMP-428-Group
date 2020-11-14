package groupproject.gameengine.sprite;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import groupproject.gameengine.Rect;

public abstract class Sprite {
	private static final String SPRITE_FOLDER = "assets/sprites/";
	protected int x;
	protected int y;
	protected Logger logger = Logger.getLogger("GameEngine", null);
	protected String currentDirection = "";
	protected HashMap<String, Animation> animDict = new HashMap<>();
	protected Rect boundsRect;
	protected int velocity;

	public Sprite(int x, int y, String spritePrefix, int delay) {
		this.x = x;
		this.y = y;
		velocity = 1;
		loadBaseAnimations(spritePrefix, delay);
		initAnimations();
		this.boundsRect = new Rect(this.x, this.y,
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
	public void draw(Graphics g) {
		if (animDict.containsKey(currentDirection)) {
			g.drawImage(animDict.get(currentDirection).getCurrentFrame(), x, y, null);
		} else {
			Animation firstAnim = getFirstAnimation();
			g.drawImage(firstAnim.getCurrentFrame(), x, y, null);
		}

		// For debug purposes, draw the bounding box of the sprite.
		g.setColor(Color.blue);
		boundsRect.draw(g);
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
		switch(currentDirection) {
		case "up":
			y -= velocity;
			boundsRect.move(0, -velocity);
			break;
		case "down":
			y += velocity;
			boundsRect.move(0, +velocity);
			break;
		case "left":
			x -= velocity;
			boundsRect.move(-velocity, 0);
			break;
		case "right":
			x += velocity;
			boundsRect.move(velocity, 0);
			break;
		default:
			break;
		}
	}

	public Rect getBounds() {
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

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
