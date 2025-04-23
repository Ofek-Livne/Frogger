package movingObjects;

import base.MovingObject;
import constants.Constants;
import general.GameManager;
import general.Frog;

import javax.swing.*;
import java.awt.*;

public class Log extends MovingObject {
	private Image[] images;
	
	public Log(int y, int size, int speed, GameManager gameManager) {
		super(y, size, speed, gameManager);
		this.x = -size;
		this.images = new Image[3];
		this.images[0] = new ImageIcon("images\\logs\\LogStart.png").getImage();
		this.images[1] = new ImageIcon("images\\logs\\LogMiddle.png").getImage();
		this.images[2] = new ImageIcon("images\\logs\\LogEnd.png").getImage();
	}
	
	@Override
	public boolean myRun() {
		x += speed;
		if (x > gameManager.getFrameSize().getX()) {
			return false;
		}
		for (int i = 0; i < gameManager.getFrogs().length; i++) {
			if (IsColliding(gameManager.getFrogs()[i])) {
				gameManager.getFrogs()[i].addX(speed);
			}
		}
		return true;
	}
	
	@Override
	public boolean IsColliding(Frog frog) {
		return !(frog.getX() >= this.x + size - Constants.TILE_SIZE / 2) && super.IsColliding(frog);
	}
	
	@Override
	public void draw(Graphics g)
	{
		g.drawImage(images[2], x, y, Constants.TILE_SIZE, Constants.TILE_SIZE, null);
		g.drawImage(images[1], x + Constants.TILE_SIZE, y, size  - 2 * Constants.TILE_SIZE, Constants.TILE_SIZE, null);
		g.drawImage(images[0], x + size - Constants.TILE_SIZE, y, Constants.TILE_SIZE, Constants.TILE_SIZE, null);
	}
}
