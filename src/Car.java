import javax.swing.*;
import java.awt.*;

public class Car extends MovingObject{
	private boolean isLeftToRight;
	private Image image;
	
	public Car(int y, int size, int speed, boolean isLeftToRight, GameManager gameManager, int carRow) {
		super(y, size, speed, gameManager);
		this.isLeftToRight = isLeftToRight;
		this.x = isLeftToRight ? -size : Main.WIDTH_GRID * Main.TILE_SIZE;
		this.image = new ImageIcon("images\\cars\\Car" + carRow + ".png").getImage();
	}
	
	@Override
	public boolean myRun() {
		x += speed * (isLeftToRight ? 1 : -1);
		if (isLeftToRight && x >= gameManager.getFrameSize().getX() ||
				!isLeftToRight && x < -size) {
			return false;
		}
		for (int i = 0; i < gameManager.getFrogs().length; i++) {
			if (IsColliding(gameManager.getFrogs()[i])) {
				gameManager.getFrogs()[i].frogDied();
			}
		}
		return true;
	}
	
	@Override
	public void draw(Graphics g) {
		g.drawImage(image, x, y, size, Main.TILE_SIZE, null);
	}
}
