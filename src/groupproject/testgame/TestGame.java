package groupproject.testgame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JFrame;

import groupproject.gameengine.GamePanel;
import groupproject.gameengine.Rect;
import groupproject.gameengine.tile.Tile;
import groupproject.gameengine.tile.TileMap;

@SuppressWarnings({"serial", "java:S110"})
public class TestGame extends GamePanel {
	private static final int WINDOW_HEIGHT = 768;
	private static final int WINDOW_WIDTH = 1024;
	private transient Pacman player = new Pacman(0, 0, "pac", 65);
	private transient Ghost redGhost = new Ghost(400, 500, "redghost", 100);
	private transient List<Rect> rectObjects = new ArrayList<>(); // keep all the rectangles in a neat list for iteration purposes
	private final transient List<TileMap> maps = new ArrayList<>();
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(maps.size() != 0) maps.get(0).drawMap(g);
		player.draw(g);
	}

	@Override
	protected void initObjects() {
		try {
			maps.add(loadTileMap("test.tilemap"));
			maps.get(0).initializeMap();
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (Tile[] row : maps.get(0).getMainLayerTiles()) {
			for (Tile tile : row) {
				if(tile.isCollisionEnabled()) rectObjects.add(tile.getBoundsRect());
			}
		}
	}
	
	@Override
	protected void mainGameLogic() {
		playerMovement();
		if (redGhost.getX() >= 400 && redGhost.getY() > 50) {
			redGhost.move(0, -2);
		}
	}
	
	private void playerMovement() {
		int dx = 0;
		int dy = 0;
		
		if (pressedKey[KeyEvent.VK_W]) {
			dy = -1;
			player.setSpriteAnim("up");
		}
		
		if (pressedKey[KeyEvent.VK_S]) {
			dy = 1;
			player.setSpriteAnim("down");
		}
		
		if (pressedKey[KeyEvent.VK_A]) { 
			dx = -1;
			dy = 0;
			player.setSpriteAnim("left");
		}
		
		if (pressedKey[KeyEvent.VK_D]) {
			dx = 1;
			dy = 0;
			player.setSpriteAnim("right");
		}
		dx *= 2;
		dy *= 2;
		checkPlayerCollision(dx, dy);
	}
	
	private void checkPlayerCollision(int dx, int dy) {
		for (Rect rectObject : rectObjects) {
			if (player.getBounds().overlaps(rectObject, dx, dy)) {
				dx = 0;
				dy = 0;
				logger.log(Level.INFO, "Player collision detected!");
			}
		}
		player.move(dx, dy);
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
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		game.runGame();
	}
}
