package groupproject.gameengine;

import java.awt.Graphics;

public class Rect {
	private int x;
	private int y;
	private int width;
	private int height;
	private int diagonalX;
	private int diagonalY;

	public Rect(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.diagonalX = x + width;
		this.diagonalY = y + height;
	}

	public void move(int dx, int dy) {
		x += dx;
		y += dy;
		diagonalX += dx;
		diagonalY += dy;
	}

	public boolean overlaps(Rect r, int dx, int dy) {
		return !(this.x + dx > r.diagonalX || this.y + dy > r.diagonalY ||
				r.x > this.diagonalX + dx || r.y > this.diagonalY + dy);
	}

	public void draw(Graphics g) {
		g.drawRect(x, y, width, height);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
