package movingObjectManagers;

import base.MovingObjectManager;
import base.PauseThread;
import constants.Constants;
import general.GameManager;
import movingObjects.Car;

import java.util.Random;

public class CarManager extends MovingObjectManager {
	static final int MIN_DISTANCE_BETWEEN_CARS = 5;
	private int carRow;
	private boolean isLeftToRight;
	
	public CarManager(int y, int size, int speed, GameManager gameManager, boolean isLeftToRight, int carRow) {
		super(y, size, speed, gameManager);
		this.isLeftToRight = isLeftToRight;
		this.carRow = carRow;
	}
	
	@Override
	public void run() {
		Random rnd = new Random();
		int distance;
		while (true) {
			distance = rnd.nextInt(Constants.WIDTH_GRID - MIN_DISTANCE_BETWEEN_CARS)
					+ MIN_DISTANCE_BETWEEN_CARS + size / Constants.TILE_SIZE;
			while(!list.isEmpty() && !list.getFirst().isAlive()) {
				list.removeFirst();
			}
			list.addLast(new Car(y, size, speed, isLeftToRight, gameManager, carRow));
			list.getLast().start();
			PauseThread.checkForPauseManager(distance * Constants.TILE_SIZE * PauseThread.SLEEP_TIME / speed);
		}
	}
}
