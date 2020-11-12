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
	protected String spriteCurrentAnim;
	protected HashMap<String, Animation> animDict = new HashMap<>();
	protected Rect boundsRect;
	
	public Sprite(int x, int y, String spritePrefix, int delay) {
		this.x = x;
		this.y = y;
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
			Animation anim = new Animation(delay, String.join("_", prefix, directions[i]), getSpriteDir());
			animDict.put(directions[i], anim);
		}
	}
	
	// Draws the sprite's current image based on its current state.
	public void draw(Graphics g) {
		if (animDict.containsKey(spriteCurrentAnim)) {
			g.drawImage(animDict.get(spriteCurrentAnim).getCurrentFrame(), x, y, null);
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
	
	public void move(int dx, int dy) {
		x += dx;
		y += dy;
		boundsRect.move(dx, dy);
	}
	
	public Rect getBounds() {
		return boundsRect;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public String getSpriteDir() {
		return SPRITE_FOLDER + this.getClass().getSimpleName().toLowerCase();
	}

	public void setSpriteAnim(String animIndex) {
		this.spriteCurrentAnim = animIndex;
	}
}
