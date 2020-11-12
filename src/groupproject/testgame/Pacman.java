package groupproject.testgame;

import groupproject.gameengine.Rect;
import groupproject.gameengine.sprite.Sprite;

public class Pacman extends Sprite {
	public Pacman(int x, int y, String spritePrefix, int delay) {
		super(x, y, spritePrefix, delay);
		this.boundsRect = new Rect(this.x, this.y, 45, 40);
	}
	
	@Override
	protected void initAnimations() {
		/*
		 * Don't need anything here yet.
		 */
	}
}
