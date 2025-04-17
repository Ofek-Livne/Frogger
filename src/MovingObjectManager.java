import java.awt.*;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;

abstract class MovingObjectManager extends Thread{
	protected LinkedList<MovingObject> list;
	protected int y, size, speed;
	protected GameManager gameManager;
	
	public MovingObjectManager(int y, int size, int speed, GameManager gameManager) {
		this.list = new LinkedList<>();
		this.y = y;
		this.size = size;
		this.speed = speed;
		this.gameManager = gameManager;
	}
	
	public LinkedList<MovingObject> getList() {
		return list;
	}
	
	@Override
	public abstract void run();
	
	public void draw(Graphics g) {
		try {
			for (MovingObject mo: list) {
				mo.draw(g);
			}
		} catch (ConcurrentModificationException e) {
		 // if it is already out of the list, it must be off-screen (doesn't need a draw anyway)
		}
	}
}