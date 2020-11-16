package groupproject.containers.pacman.models;

import groupproject.gameengine.models.BoundingBox;
import groupproject.gameengine.sprite.Sprite;

public class Pacman extends Sprite {
	public Pacman(int x, int y) {
		super(x, y, "pac", 65);
		this.boundsRect = new BoundingBox(x+5, y+5, 8, 8);
	}

	@Override
	protected void initAnimations() {
		/*
		 * Don't need anything here yet.
		 */
	}
}
