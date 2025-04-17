package base;

import general.GameManager;
import main.Main;
import general.Frog;
import java.awt.*;

public abstract class MovingObject extends PauseThread {
	protected int x, y, size, speed;
	protected GameManager gameManager;
	
	public MovingObject(int y, int size, int speed, GameManager gameManager) {
		this.y = y;
		this.size = size;
		this.speed = speed;
		this.gameManager = gameManager;
	}
	
	public int getX() {
		return x;
	}
	
	public int getSize() {
		return size;
	}
		
	public abstract void draw(Graphics g);
	
	public boolean IsColliding(Frog frog) {
		if (!frog.getAbleToMove()) return false;
		return !(frog.getX() + Main.TILE_SIZE <= this.x ||
				frog.getX() >= this.x + size ||
				frog.getY() + Main.TILE_SIZE <= this.y ||
				frog.getY() >= this.y + Main.TILE_SIZE);
	}
}
