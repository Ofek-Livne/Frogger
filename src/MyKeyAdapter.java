import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

public abstract class MyKeyAdapter extends KeyAdapter {
	
		protected JPanel panel;
		protected static HashMap<Integer, Image> frogImages;
		
		public MyKeyAdapter(JPanel panel) {
			super();
			this.panel = panel;
			this.panel.setFocusable(true);
			frogImages = new HashMap<Integer, Image>();
			for (int i = 1; i <= 2; i++) { //2 playes
				frogImages.put(KeyEvent.VK_UP * ((i == 1)? 1 : 10), new ImageIcon("images\\frogs\\FrogUp" + i + ".png").getImage());
				frogImages.put(KeyEvent.VK_DOWN * ((i == 1)? 1 : 10), new ImageIcon("images\\frogs\\FrogDown" + i + ".png").getImage());
				frogImages.put(KeyEvent.VK_LEFT * ((i == 1)? 1 : 10), new ImageIcon("images\\frogs\\FrogLeft" + i + ".png").getImage());
				frogImages.put(KeyEvent.VK_RIGHT * ((i == 1)? 1 : 10), new ImageIcon("images\\frogs\\FrogRight" + i + ".png").getImage());
			}
		}

		@Override
		public void keyPressed(KeyEvent e)
		{
			int code = e.getKeyCode();
			
			if (PauseThread.isPaused) {
				return;
			}
			switch(code) {
				case KeyEvent.VK_UP:
					keyUp();
					break;
				case KeyEvent.VK_DOWN:
					keyDown();
					break;
				case KeyEvent.VK_LEFT:
					keyLeft();
					break;
				case KeyEvent.VK_RIGHT:
					keyRight();
		            break;
			}
			panel.repaint();
		}
		
		public static Image getImage(int code) {
			if (frogImages.containsKey(code)) {
				return frogImages.get(code);
			}
			return null;
		}
		
		public static Image getImage(Direction direction, boolean isFirstFrog) {
			switch (direction) {
				case UP: {
					return getImage(KeyEvent.VK_UP * ((isFirstFrog)? 1 : 10));
				}
				case DOWN: {
					return getImage(KeyEvent.VK_DOWN * ((isFirstFrog)? 1 : 10));
				}
				case LEFT: {
					return getImage(KeyEvent.VK_LEFT * ((isFirstFrog)? 1 : 10));
				}
				case RIGHT: {
					return getImage(KeyEvent.VK_RIGHT * ((isFirstFrog)? 1 : 10));
				}
			}
			return null;
		}
		
		public abstract void keyUp();
		public abstract void keyDown();
		public abstract void keyLeft();
		public abstract void keyRight();
	}