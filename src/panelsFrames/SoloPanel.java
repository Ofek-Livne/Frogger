package panelsFrames;

import utils.MyKeyAdapter;
import base.PauseThread;
import general.GameManager;
import main.Main;
import general.Frog;
import general.Timer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Arrays;

public class SoloPanel extends JPanel implements Runnable {
	
	private Timer timerInstance;
	private GameManager gameManager;
	Frog frog;
	Thread thread;
	
	public SoloPanel(Dimension frameSize) {
		super();
		setSize(frameSize.width, frameSize.height);
		gameManager = new GameManager(this.getSize(), 1);
		frog = gameManager.getFrogs()[0];
		addKeyListener(new SoloKeyAdapter(frog, this));
		timerInstance = Timer.getInstance(frog);
		timerInstance.start();
		thread = new Thread(this);
		thread.start();
	}
	
	public GameManager getGameManager() {
		return gameManager;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Main.drawBottomRow(g);
		timerInstance.draw(g);
		frog.draw(g);
		gameManager.drawPanel(g);
		
		PauseThread.drawPaused(g);
	}
	
	@Override
	public void run() {
		while (true) {
			try {Thread.sleep(1);} catch (InterruptedException e) {}
			repaint();
			if (frog.getExtraLives() < 0) {
				showDialog("Game Over!");
			}
			if (frog.areAllLilypadsFull()) {
				Timer.checkForNewHighscore();
				showDialog("Well Played! your time: " + 
				(int)((System.currentTimeMillis() - Timer.getGameStartTime()) / 1000) + " seconds!");
			}
		}
	}
	
	public void showDialog(String message) {
		frog.setAbleToMove(false);
		String[] options =  {"Play Again!", "Exit"};
		int response = JOptionPane.showOptionDialog(null, message, 
				message, JOptionPane.DEFAULT_OPTION, 
				JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		if (response != 0) //The only options are: (1, -1) = exit, and 0 = replay
			System.exit(0);
		startNewGame();
	}
	
	public void startNewGame() {
		frog.setExtraLives(Frog.STARTING_EXTRA_LIVES);
		frog.jumpToStart();
		frog.setAbleToMove(true);
		Timer.resetGameStartingTime();
		Timer.setHighscoreLocal(Timer.getHighscoreFromFile());
		Arrays.fill(frog.getLilypadsFree(), true);
	}

	public class SoloKeyAdapter extends MyKeyAdapter {
		
		private Frog frog;
		
		public SoloKeyAdapter(Frog frog, JPanel panel) {
			super(panel);
			this.frog = frog;
		}
		
		@Override
		public void keyPressed(KeyEvent e)
		{
			if (!frog.getAbleToMove()) return;
			int code = e.getKeyCode();
			if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_P) {
				PauseThread.togglePause();
				return;
			}
			super.keyPressed(e);
			if (getImage(code) != null && !PauseThread.isPaused)
				frog.setImage(getImage(code));
		}

		@Override
		public void keyUp() {
			frog.addY(-Frog.SIZE);
		}

		@Override
		public void keyDown() {
			frog.addY(Frog.SIZE);			
		}

		@Override
		public void keyLeft() {
			frog.addX(-Frog.SIZE);			
		}

		@Override
		public void keyRight() {
			frog.addX(Frog.SIZE);
		}
	}
}
