package groupproject.testgame;

import groupproject.gameengine.Rect;
import groupproject.gameengine.sprite.Sprite;

public class Ghost extends Sprite {

	public Ghost(int x, int y, String spritePrefix, int delay) {
		super(x, y, spritePrefix, delay);
		this.boundsRect = new Rect(this.x, this.y, 40, 40);
	}

	@Override
	protected void initAnimations() {
		/*
		 * Not needed currently.
		 */
	}
}
