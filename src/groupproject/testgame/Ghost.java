package groupproject.testgame;

import groupproject.gameengine.Rect;
import groupproject.gameengine.sprite.Sprite;

public class Ghost extends Sprite {

	public Ghost(int x, int y, String spritePrefix, int delay) {
		super(x, y, spritePrefix, delay);
		this.boundsRect = new Rect(x+4, y+5, 8, 8);
	}

	@Override
	protected void initAnimations() {
		/*
		 * Not needed currently.
		 */
	}
}
