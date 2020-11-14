package groupproject.testgame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;

import groupproject.gameengine.GamePanel;
import groupproject.gameengine.sprite.Sprite;
import groupproject.gameengine.tile.Tile;
import groupproject.gameengine.tile.TileMap;

@SuppressWarnings({"serial", "java:S110"})
public class TestGame extends GamePanel {
	private static final int WINDOW_HEIGHT = 768;
	private static final int WINDOW_WIDTH = 672;
	private final transient Pacman player = new Pacman(3, 11);
	private final transient Ghost redGhost = new Ghost(80, 11, "red", 100);
	private final transient List<TileMap> maps = new ArrayList<>();

	@Override
	// Paints all the components onto a base image, then that image is upscaled 3x (very roughly for now.).
	public void paintComponent(Graphics g) {
		BufferedImage frame = new BufferedImage(224, 256, BufferedImage.TYPE_INT_RGB);
		Graphics base = frame.createGraphics();
		super.paintComponent(base);
		if (!maps.isEmpty()) {
			maps.get(0).drawMap(base);
			for (Tile tile : maps.get(0).getSurroundingTiles(player.getBounds().getX(), player.getBounds().getY(), "all"))
				tile.drawBoundsRect(base);
		}
		player.draw(base);
		redGhost.draw((base));
		base.dispose();
		g.drawImage(frame, 0, 0, 672, 768, null);
	}

	@Override
	protected void initObjects() {
		try {
			maps.add(loadTileMap("pacman.tilemap"));
			maps.get(0).initializeMap();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void mainGameLogic() {
		playerMovement();
	}

	private void playerMovement() {
		String direction = player.getSpriteDirection();
		boolean allowMove = false;

		if (pressedKey[KeyEvent.VK_W] && !isSpriteCollidingWithMap(player, "up")) {
			direction = "up";
			allowMove = true;
		}

		if (pressedKey[KeyEvent.VK_S] && !isSpriteCollidingWithMap(player, "down")) {
			direction = "down";
			allowMove = true;
		}

		if (pressedKey[KeyEvent.VK_A] && !isSpriteCollidingWithMap(player, "left")) {
			direction = "left";
			allowMove = true;
		}

		if (pressedKey[KeyEvent.VK_D] && !isSpriteCollidingWithMap(player, "right")) {
			direction = "right";
			allowMove = true;
		}

		if(allowMove || !isSpriteCollidingWithMap(player, direction)) {
			player.setSpriteDirection(direction);
			player.move();
		}
	}

	// Basic collision detection based on the 8 map tiles surrounding a sprite.
	private boolean isSpriteCollidingWithMap(Sprite sprite, String direction) {
		boolean isColliding = false;
		int velocity = sprite.getVelocity();

		switch(direction) {
		case "right":
			for (Tile tile : maps.get(0).getSurroundingTiles(sprite.getBounds().getX(), sprite.getBounds().getY(), "right"))
				if(tile.isCollisionEnabled() && sprite.getBounds().overlaps(tile.getBoundsRect(), velocity, 0))
					isColliding = true;
			break;
		case "left":
			for (Tile tile : maps.get(0).getSurroundingTiles(sprite.getBounds().getX(), sprite.getBounds().getY(), "left"))
				if(tile.isCollisionEnabled() && sprite.getBounds().overlaps(tile.getBoundsRect(), -velocity, 0))
					isColliding = true;
			break;
		case "up":
			for (Tile tile : maps.get(0).getSurroundingTiles(sprite.getBounds().getX(), sprite.getBounds().getY(), "up"))
				if(tile.isCollisionEnabled() && sprite.getBounds().overlaps(tile.getBoundsRect(), 0, -velocity))
					isColliding = true;
			break;
		case "down":
			for (Tile tile : maps.get(0).getSurroundingTiles(sprite.getBounds().getX(), sprite.getBounds().getY(), "down"))
				if(tile.isCollisionEnabled() && sprite.getBounds().overlaps(tile.getBoundsRect(), 0, velocity))
					isColliding = true;
			break;
		default:
			break;
		}

		return isColliding;
	}

	public static void main(String[] args) {
		System.setProperty("sun.java2d.opengl", "true");
		JFrame frame = new JFrame("Test Game");
		TestGame game = new TestGame();
		game.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		game.setBackground(Color.black);
		frame.add(game);
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		game.runGame();
	}
}
