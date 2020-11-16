package groupproject.gameengine;

import groupproject.gameengine.tile.TileMap;
import groupproject.gameengine.tile.TileMapModel;

import java.awt.event.*;
import java.io.*;
import java.util.logging.*;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class GamePanel extends JPanel implements KeyListener {
	
	private boolean isRunning = true;
	protected final transient Logger logger = Logger.getLogger("GameEngine", null);
	protected final boolean[] pressedKey = new boolean[255];

	public GamePanel() {
		setFocusable(true);
		addKeyListener(this);
	}

	protected void runGame() {
		initObjects();
		
		while (isRunning) {
			mainGameLogic();
			repaint();
			try {
				Thread.sleep(16); // should result in 60FPS.
			} catch (InterruptedException ex) {
				logger.log(Level.SEVERE, ex.getMessage());
				Thread.currentThread().interrupt();
			}
		}
	}

	public TileMap loadTileMap(String mapFile) {
		try (FileInputStream fis = new FileInputStream(TileMapModel.MAP_FOLDER + mapFile); ObjectInputStream is = new ObjectInputStream(fis)) {
			TileMapModel mapModel = (TileMapModel) is.readObject();
			return new TileMap(mapModel);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected abstract void mainGameLogic();

	protected abstract void initObjects();
	
	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		pressedKey[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		pressedKey[e.getKeyCode()] = false;
	}
}
