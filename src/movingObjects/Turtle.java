package movingObjects;

import base.MovingObject;
import constants.Constants;
import general.GameManager;
import base.PauseThread;
import general.Frog;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class Turtle extends MovingObject implements Serializable{
	static final int STATE_TIME = 1000;
	private long diveStartTime, lastPausedTime;
	private Image[] images;
	private int state;
	
	public Turtle(int y, int size, int speed, GameManager gameManager) {
		super(y, size, speed, gameManager);
		this.x = Constants.WIDTH_GRID * Constants.TILE_SIZE;
		this.images = new Image[3];
		this.images[0] = new ImageIcon("images\\turtles\\Turtle0.png").getImage();
		this.images[1] = new ImageIcon("images\\turtles\\Turtle1.png").getImage();
		this.images[2] = new ImageIcon("images\\turtles\\Turtle2.png").getImage();
		this.state = 0;
		this.diveStartTime = -1;
		this.lastPausedTime = -1;
	}
	
	@Override
	public boolean myRun() {
		x -= speed;
		if (x < -size - 50) {
			return false;
		}
		for (int i = 0; i < gameManager.getFrogs().length; i++) {
			if (IsColliding(gameManager.getFrogs()[i])) {
				gameManager.getFrogs()[i].addX(-speed);
			}
		}
		if (state > 0) {
			if (diveStartTime == -1)
				diveStartTime = System.currentTimeMillis();
			if (isAlive() && PauseThread.pausedTime != -1.0 && lastPausedTime != PauseThread.pausedTime) {				
				diveStartTime += System.currentTimeMillis() - PauseThread.pausedTime;
				lastPausedTime = PauseThread.pausedTime;
			}
			state = 1 + (int) (System.currentTimeMillis() - diveStartTime) / STATE_TIME;
		}
		if (state >= 4) {
			state = 0;
			diveStartTime = -1;
		}
		return true;
	}
	
	@Override
	public boolean IsColliding(Frog frog) {
		return super.IsColliding(frog) && (state <= 2);
	}
	
	@Override
	public void draw(Graphics g) {
		if (state > 2) return;
		for (int i = 0; i < size; i += Constants.TILE_SIZE + 10) {
			g.drawImage(images[state], x + i, y, Constants.TILE_SIZE, Constants.TILE_SIZE, null);
		}
	}
	
	public void dive() {
		state = 1;
	}
	
	public int getDivingState() {
		return this.state;
	}
}
