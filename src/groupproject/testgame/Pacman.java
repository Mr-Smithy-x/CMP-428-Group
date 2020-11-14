package groupproject.testgame;

import groupproject.gameengine.Rect;
import groupproject.gameengine.sprite.Sprite;

public class Pacman extends Sprite {
	public Pacman(int x, int y) {
		super(x, y, "pac", 65);
		this.boundsRect = new Rect(x+5, y+5, 8, 8);
	}

	@Override
	protected void initAnimations() {
		/*
		 * Don't need anything here yet.
		 */
	}
}
