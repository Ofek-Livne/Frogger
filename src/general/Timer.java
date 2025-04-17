package general;

import base.MovingObject;
import base.PauseThread;
import main.Main;

import java.awt.*;
import java.io.*;

public class Timer extends Thread {
	static final int TIME = 30000;
	private static int highscore;
	private static long startTime, gameStartTime, pausedTime;
	private static float barPercent, pausedPercent;
	private static Frog frog;
	private static Timer timerSingelton;
	
	private Timer(Frog frog) {
		Timer.startTime = Timer.gameStartTime = System.currentTimeMillis();
		Timer.frog = frog;
		Timer.highscore = getHighscoreFromFile();
		Timer.pausedTime = -1;
		Timer.pausedPercent = -1;
	}
	
	public static Timer getInstance(Frog frog) {
		return (timerSingelton == null)? new Timer(frog) : timerSingelton;
	}
	
	public static long getStartTime() {
		return startTime;
	}
	
	public static long getGameStartTime() {
		return gameStartTime;
	}
	
	public static void resetStartingTime() {
		startTime = System.currentTimeMillis();
		pausedTime = -1;
		pausedPercent = -1;
	}
	
	public static void resetGameStartingTime() {
		gameStartTime = System.currentTimeMillis();
		pausedTime = -1;
		pausedPercent = -1;
	}
	
	public static void setHighscoreLocal(int highscore) {
		Timer.highscore = highscore;
	}
	
	public void draw(Graphics g) {
		final int GUI_HEIGHT = Main.TILE_SIZE / 3;
		final int BAR_OFF_THE_MIDDLE = 2;
		barPercent =  (TIME - (System.currentTimeMillis() - startTime)) / (float) TIME;
		if (PauseThread.isPaused && pausedTime == -1)
			pausedPercent = barPercent;
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.BOLD, 10)); //display time and high score
		displayString(g, "time: " + (System.currentTimeMillis() - gameStartTime) / 1000, Main.TILE_SIZE * Main.HEIGHT_GRID - GUI_HEIGHT);
		displayString(g, "HS: " + highscore, Main.TILE_SIZE * Main.HEIGHT_GRID - GUI_HEIGHT + 10);
		
		if (frog.getAbleToMove()){ // draw time bar if alive
			g.drawRect(BAR_OFF_THE_MIDDLE * Main.TILE_SIZE, Main.TILE_SIZE * Main.HEIGHT_GRID - GUI_HEIGHT,
					(Main.WIDTH_GRID - 2 * BAR_OFF_THE_MIDDLE) * Main.TILE_SIZE - 1, GUI_HEIGHT);
			g.setColor(Color.RED);
			barPercent = (PauseThread.isPaused ? pausedPercent : barPercent);
			g.fillRect(BAR_OFF_THE_MIDDLE * Main.TILE_SIZE, Main.TILE_SIZE * Main.HEIGHT_GRID - GUI_HEIGHT,
					(int) ((Main.WIDTH_GRID - 2 * BAR_OFF_THE_MIDDLE) * Main.TILE_SIZE * barPercent), GUI_HEIGHT);
		}
				
	}
	
	public void displayString(Graphics g, String str, int y) {
		int stringLen = (int) g.getFontMetrics().getStringBounds(str, g).getWidth();
		int start = Main.TILE_SIZE/2 - stringLen/2;
		g.drawString(str, start + Main.TILE_SIZE * (Main.WIDTH_GRID - 2), y);
	}
	
	@Override
	public void run() {
		while (true) {
			if (PauseThread.isPaused && pausedTime == -1) {
				pausedTime = System.currentTimeMillis();
			} else if (!PauseThread.isPaused && pausedTime != -1) {
				startTime += System.currentTimeMillis() - pausedTime;
				pausedTime = -1;
			}
			if (startTime + TIME < System.currentTimeMillis() && frog.getAbleToMove() && pausedTime == -1)
				frog.frogDied();
			
			try {Thread.sleep(MovingObject.SLEEP_TIME);} catch (InterruptedException e) {}
		}
	}
	
	public static void checkForNewHighscore() {
		int totalTime = (int)((System.currentTimeMillis() - gameStartTime) / 1000);
		if (totalTime < highscore)
		{
			highscore = totalTime;
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter("highscore.txt"));
				bw.write("" + totalTime);
				bw.close();
			} catch (IOException e) {e.printStackTrace();}
		}
	}
	
	public static int getHighscoreFromFile(){
		int res = 999;
		try {
			BufferedReader br = new BufferedReader(new FileReader("highscore.txt"));
			res = Integer.parseInt(br.readLine());
			br.close();
		} catch (IOException e) {e.printStackTrace();}
		return res;
	}
}
