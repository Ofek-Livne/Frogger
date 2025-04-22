package movingObjectManagers;

import base.MovingObject;
import base.MovingObjectManager;
import base.PauseThread;
import constants.Constants;
import general.GameManager;
import movingObjects.Log;

import java.util.Random;


public class LogManager extends MovingObjectManager {
	static final int MIN_DISTANCE_BETWEEN_LOGS = 5, MIN_LOG_SIZE = 3, MAX_LOG_SIZE = 6;
	
	public LogManager(int y, int speed, GameManager gameManager) {
		super(y, 1, speed, gameManager);
	}
	
	@Override
	public void run() {
		Random rnd = new Random();
		int distance;
		while (true) {
			size = (rnd.nextInt(1 + MAX_LOG_SIZE - MIN_LOG_SIZE) + MIN_LOG_SIZE) * Constants.TILE_SIZE;
			distance = rnd.nextInt(Constants.WIDTH_GRID - MIN_DISTANCE_BETWEEN_LOGS) + MIN_DISTANCE_BETWEEN_LOGS + size / Constants.TILE_SIZE;
			while(!list.isEmpty() && !list.getFirst().isAlive()) {
				list.removeFirst();
			}
			list.addLast(new Log(y, size, speed, gameManager));
			list.getLast().start();
			PauseThread.checkForPauseManager(distance * Constants.TILE_SIZE * MovingObject.SLEEP_TIME / speed);
		}
	}
}
