import java.util.Random;

public class TurtleManager extends MovingObjectManager {
	static final int MIN_DISTANCE_BETWEEN_TURTLES = 5, DIVE_FREQUENCY = 5000;
	private long startTime;
	private DataToClients dataToClients; //only for multiplayer
	
	public TurtleManager(int y, int size, int speed, GameManager gameManager) {
		super(y, size, speed, gameManager);
		this.startTime = System.currentTimeMillis();
		this.dataToClients = null;
	}

	public void run() {
		Random rnd = new Random();
		int distance;
		Turtle turtleDiving;
		while (true) {
			distance = rnd.nextInt(Main.WIDTH_GRID - MIN_DISTANCE_BETWEEN_TURTLES) + MIN_DISTANCE_BETWEEN_TURTLES + size / Main.TILE_SIZE;
			while(!list.isEmpty() && !list.getFirst().isAlive()) {
				list.removeFirst();
			}
			list.addLast(new Turtle(y, size, speed, gameManager));
			list.getLast().start();
			if (startTime + DIVE_FREQUENCY < System.currentTimeMillis()) {
				turtleDiving =((Turtle)list.get(rnd.nextInt(list.size())));
				turtleDiving.dive();
				if (dataToClients != null) {
					dataToClients.setTurtleDiving(turtleDiving, y / (3 * Main.TILE_SIZE)); //y2 -> 0, y5-> 1
				}
				startTime += DIVE_FREQUENCY;
			}
			PauseThread.checkForPauseManager(distance * Main.TILE_SIZE * MovingObject.SLEEP_TIME / speed);
		}
	}
	
	public void setDataToClient(DataToClients dataToClients) {
		this.dataToClients = dataToClients;
	}
}
