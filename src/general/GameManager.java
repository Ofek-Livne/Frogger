package general;

import base.MovingObjectManager;
import constants.Constants;
import movingObjectManagers.CarManager;
import movingObjectManagers.LogManager;
import movingObjectManagers.TurtleManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameManager {
	static final int NUM_OF_ROADS = 5;
	
	private CarManager[] roadManager;
	private ArrayList<MovingObjectManager> waterManager;
	private Image[] lilypadImages;
	private Frog[] frogs;
	private Point frameSize;
	
			
	public GameManager(Dimension d, int players) {
		this.frameSize = new Point((int)d.getWidth(), (int)d.getHeight());
		frogs = new Frog[players];
		for (int i = 0; i < players; i++) {
			frogs[i] = new Frog(Constants.TILE_SIZE, i == 0, frameSize, this);
			frogs[i].start();
		}
		lilypadImages = new Image[5];
		lilypadImages[0] = new ImageIcon("images\\lilypads\\LilypadFull.png").getImage();
		lilypadImages[1] = new ImageIcon("images\\lilypads\\LilypadEmpty.png").getImage();
		lilypadImages[2] = new ImageIcon("images\\lilypads\\LilypadP1.png").getImage();
		lilypadImages[3] = new ImageIcon("images\\lilypads\\LilypadP2.png").getImage();
		lilypadImages[4] = new ImageIcon("images\\lilypads\\LilypadBoth.png").getImage();

		roadManager = new CarManager[NUM_OF_ROADS];
		Map<Integer, Integer> carsSpeed = new HashMap<>();
		carsSpeed.put(0, 4); carsSpeed.put(1, 3); carsSpeed.put(2, 2); carsSpeed.put(3, 4); carsSpeed.put(4, 2);
		for (int i = 0; i < NUM_OF_ROADS; i++) {
			roadManager[i] = new CarManager((Constants.HEIGHT_GRID - 2 - i) * Constants.TILE_SIZE,
					Constants.TILE_SIZE *(1 + i / (NUM_OF_ROADS - 1)), carsSpeed.get(i), this , i % 2 == 1,  i);
			roadManager[i].start();
		}
		
		waterManager = new ArrayList<>();
		waterManager.add(new LogManager(Constants.TILE_SIZE, 3, this));
		waterManager.add(new TurtleManager(2 * Constants.TILE_SIZE, 2 * Constants.TILE_SIZE, 3, this));
		waterManager.add(new LogManager(3 * Constants.TILE_SIZE, 4, this));
		waterManager.add(new LogManager(4 * Constants.TILE_SIZE, 2, this));
		waterManager.add(new TurtleManager(5 * Constants.TILE_SIZE, 2 * Constants.TILE_SIZE, 2, this));
		for (MovingObjectManager waterLaneManager : waterManager) {
			waterLaneManager.start();
		}
	}
	
	public CarManager[] getRoadManager() {
		return roadManager;
	}
	
	public ArrayList<MovingObjectManager> getWaterManager() {
		return waterManager;
	}
	
	public Point getFrameSize() {
		return frameSize;
	}
	
	public Frog[] getFrogs() {
		return frogs;
	}
	
	public Image[] getLilypadImages() {
		return lilypadImages;
	}
	
	public void drawPanel(Graphics g)
	{
		for (int i = 0; i < Constants.WIDTH_GRID; i++) {
			g.setColor((i % 2 == 1)?Color.GRAY:Color.DARK_GRAY);
			g.fillRect(i * Constants.TILE_SIZE, 6 * Constants.TILE_SIZE, Constants.TILE_SIZE, Constants.TILE_SIZE);
		}
		g.setColor(Color.BLACK);
		g.fillRect(0, (Constants.WIDTH_GRID - 7) * Constants.TILE_SIZE,
				Constants.WIDTH_GRID * Constants.TILE_SIZE, 5 * Constants.TILE_SIZE);
		for (int i = 0; i < NUM_OF_ROADS; i++) {
			roadManager[i].draw(g);
		}
		g.setColor(Color.BLUE);
		g.fillRect(0, 0, Constants.WIDTH_GRID * Constants.TILE_SIZE, 6 * Constants.TILE_SIZE);
		for (MovingObjectManager movingObjectManager : waterManager) {
			movingObjectManager.draw(g);
		}
		
		for (int i = 0; i < frogs[0].getLilypadsFree().length; i++) {
			g.drawImage(lilypadImages[frogs[0].getLilypadsFree()[i] ? 1 : 0], (1 + i * 3) * Constants.TILE_SIZE,
					0, Constants.TILE_SIZE, Constants.TILE_SIZE, null);
		}
		for (int i = 0; i < frogs.length; i++) {
			frogs[0].draw(g);
		}
	}
}
