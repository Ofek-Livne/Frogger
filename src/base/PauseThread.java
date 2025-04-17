package base;

import main.Main;

import java.awt.*;

public abstract class PauseThread extends Thread {
    public static int SLEEP_TIME = 10;
    public static boolean isPaused = false;
    protected static long pausedTime; // the point in time where pressed to pause

    public PauseThread() {
        pausedTime = -1;
    }

    @Override
    public void run() {
        boolean isAlive = true;
        while (isAlive) {
            while (isPaused) {
                try {
                    sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                }
            }
            isAlive = myRun();
            try {
                sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
            }
        }
    }

    public abstract boolean myRun();

    public static void checkForPauseManager(long sleepTime) {
        long timeElapsed = 0;
        while (timeElapsed < sleepTime) {
            while (isPaused) {
                try {
                    sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                }
            }
            try {
                sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
            }
            timeElapsed += SLEEP_TIME * 1.1;
        }
    }

    public static void togglePause() {
        if (isPaused) { //proceed
            isPaused = false;
        } else { //pause
            isPaused = true;
            pausedTime = System.currentTimeMillis();
        }
    }

    public static void drawPaused(Graphics g) {
        if (System.currentTimeMillis() - Math.pow(10, 9) >= pausedTime)
            pausedTime = -1;
        if (!isPaused)
            return;
        g.setColor(Color.RED);
        g.fillRect((Main.WIDTH_GRID / 2 - 3) * Main.TILE_SIZE, (int) ((Main.HEIGHT_GRID / 2 - .5) * Main.TILE_SIZE),
                6 * Main.TILE_SIZE, 2 * Main.TILE_SIZE);
        g.setColor(Color.YELLOW);
        g.setFont(new Font(Main.FONT_NAME, Font.PLAIN, 40));

        //display the text
        String str = "PAUSED";
        int stringLen = (int) g.getFontMetrics().getStringBounds(str, g).getWidth();
        int start = 6 * Main.TILE_SIZE / 2 - stringLen / 2;
        g.drawString(str, start + (Main.WIDTH_GRID / 2 - 3) * Main.TILE_SIZE,
                (int) ((Main.HEIGHT_GRID / 2 + 0.75) * Main.TILE_SIZE));
    }
}
