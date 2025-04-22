package general;

import base.MovingObject;
import base.MovingObjectManager;
import constants.Constants;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class Frog extends Thread {
	static final Point STARTING_POSITION = new Point
			(Constants.WIDTH_GRID / 2 * Constants.TILE_SIZE, (Constants.HEIGHT_GRID - 1) * Constants.TILE_SIZE);
	public static final int STARTING_EXTRA_LIVES = 3;
    public static final int SIZE = Constants.TILE_SIZE;
	private int x, y, extraLives;
	private Image image;
	private final Image imageUp;
	private boolean isAbleToMove;
	
	private boolean[] lilypadsFree;
	private boolean isFirstFrog;
	private Point frameSize;
	private GameManager gameManager;
	
	public Frog(int size, boolean isFirstFrog, Point frameSize, GameManager gameManager) {
		this.x = STARTING_POSITION.x - (isFirstFrog ? size : 0);
		this.y = STARTING_POSITION.y;
		this.image = new ImageIcon("images\\frogs\\FrogUp" + (isFirstFrog? 1 : 2) + ".png").getImage();
		this.imageUp = image;
		this.extraLives = STARTING_EXTRA_LIVES;
		this.isAbleToMove = true;
		
		lilypadsFree = new boolean[5];
		Arrays.fill(lilypadsFree, true);
		this.isFirstFrog = isFirstFrog;
		this.frameSize = new Point(frameSize);
		this.gameManager = gameManager;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		if (x < 0)
			x = 0;
		else if (x + SIZE > frameSize.getX())
			x = (int)frameSize.getX() - SIZE;
		else this.x = x;
	}
	public void addX(int x) {
		setX(this.x + x);
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		if (y < 0)
			y = 0;
		else if (y + SIZE > frameSize.getY())
			y = (int)frameSize.getY() - SIZE;
		else this.y = y;
	}
	
	public void addY(int y) {
		setY(this.y + y);
	}
	
	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
	
	public boolean getAbleToMove() {
		return isAbleToMove;
	}

	public void setAbleToMove(boolean ableToMove) {
		this.isAbleToMove = ableToMove;
	}
	
	public int getExtraLives() {
		return extraLives;
	}

	public void setExtraLives(int extraLives) {
		this.extraLives = extraLives;
	}
	
	public boolean[] getLilypadsFree() {
		return lilypadsFree;
	}
	
	public void updateLilypadFree(int index) {
		lilypadsFree[index] = false;
	}
	
	public boolean areAllLilypadsFull() {
		int i = 0;
		boolean res = true;
		while (i < lilypadsFree.length && res) {
			res &= !lilypadsFree[i++];
		}
		return res;
	}
	
	public void draw(Graphics g) {
		final int EXTRA_LIVES_SIZE = SIZE / 3;
		g.drawImage(image, x, y, SIZE, SIZE, null); // draw the frog
		if (x == 0 && y == Constants.TILE_SIZE * (Constants.HEIGHT_GRID - 1)) return; // draw the extra lives if not on their tile
		for (int i = 0; i < extraLives; i++) {
			g.drawImage(imageUp, i * EXTRA_LIVES_SIZE,
					Constants.TILE_SIZE * Constants.HEIGHT_GRID - EXTRA_LIVES_SIZE,
					EXTRA_LIVES_SIZE, EXTRA_LIVES_SIZE, null);
		}
	}
	
	@Override
	public void run() {
		int isOnLilyPadResult = -1;
		while (true) {
			isOnLilyPadResult = isOnLilyPad();
			if (isOnLilyPadResult != -1) {
				updateLilypadFree(isOnLilyPadResult);
				jumpToStart();
			}
			if (isDrowning() || y == 0 && isOnLilyPadResult == -1) {
				frogDied();
			}
			try {Thread.sleep(MovingObject.SLEEP_TIME);} catch (InterruptedException e) {}
		}
	}
	
	public boolean isDrowning() {
		if (y < Constants.TILE_SIZE || y >= 6 * Constants.TILE_SIZE)
			return false;
		for (MovingObjectManager movingObjectManager : gameManager.getWaterManager()) {
			for (MovingObject movingObject : movingObjectManager.getList()) {
				if (movingObject.IsColliding(this)) {
					return false;
				}
			}
		}
		return true;
	}
	
	//if not on a lily pad returns -1, else return the lily pad index
	public int isOnLilyPad() {
		if (y > 0)
			return -1;
		for (int i = 0; i < getLilypadsFree().length; i++) {	
			if (getLilypadsFree()[i] && x + SIZE / 2 > (1 + i * 3) * Constants.TILE_SIZE
					&& x + SIZE / 3 < (2 + i * 3) * Constants.TILE_SIZE) {
				return i;				
			}
		}
		return -1;
	}
	
	public void jumpToStart() {
		this.x = STARTING_POSITION.x - (isFirstFrog ? SIZE : 0);
		this.y = STARTING_POSITION.y;
		this.image = imageUp;
		this.isAbleToMove = true;
		Timer.resetStartingTime();
	}
	
	public void frogDied() {
		--extraLives;
		this.image = new ImageIcon("images\\frogs\\Dead.png").getImage();
		this.isAbleToMove = false;
		try {Thread.sleep(1500);} catch (InterruptedException e) {e.printStackTrace();}
		jumpToStart();	
	}
}
