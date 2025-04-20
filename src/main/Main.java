package main;

import panelsFrames.MainMenuPanel;
import panelsFrames.ServerFrame;
import panelsFrames.SoloPanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Main {
    public static final int WIDTH_GRID = 14;
    public static final int HEIGHT_GRID = 13;
    public static final int TILE_SIZE = 60;
    //static final int BORDER_WIDTH = 16, BORDER_HEIGHT = 39; //class
    static final int BORDER_WIDTH = 14, BORDER_HEIGHT = 37; //home
    public static final String FONT_NAME = "ZX Spectrum 7 Bold";
    static final Dimension gamePanelSize = new Dimension(WIDTH_GRID * TILE_SIZE + BORDER_WIDTH,
                                                        HEIGHT_GRID * TILE_SIZE + BORDER_HEIGHT),
                       mainMenuPanelSize = new Dimension(840 + BORDER_WIDTH,
                                                        840 + BORDER_HEIGHT);
    public static final Color PINK = new Color(253, 85, 118);
    public static final Color TAN = new Color(248, 235, 200);
    public static final Color MAIN_FROG_GREEN = new Color(108, 244, 76);
    public static final Color FROG_GREEN = new Color(0, 255, 33);
    public static final Color FROG_CYAN = new Color(28, 178, 131);
    private static JFrame nextFrame = null;

    public static void main(String[] args) {
        addFont();
        JFrame MainMenuFrame;
        MainMenuFrame = new JFrame("Frogger");
        MainMenuPanel mainMenuPanel = new MainMenuPanel();

        MainMenuFrame.setSize(mainMenuPanelSize);
        MainMenuFrame.add(mainMenuPanel);
        MainMenuFrame.setVisible(true);
        MainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MainMenuFrame.setResizable(false);
        Image icon = Toolkit.getDefaultToolkit().getImage("images\\icons\\main_icon.png");
        MainMenuFrame.setIconImage(icon);

        mainMenuPanel.getSoloButton().addActionListener(e -> buttonsOnClick(0, MainMenuFrame));
        mainMenuPanel.getMultiplayerButton().addActionListener(e -> {
            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() throws Exception {
                    buttonsOnClick(1, MainMenuFrame);
                    return null;
                }
            };
            worker.execute();
        });
    }

    public static void buttonsOnClick(int response, JFrame mainMenuFrame) {
        mainMenuFrame.setVisible(false);

        switch (response) {
            case 0:
                nextFrame = new JFrame("Frogger - Solo");
                nextFrame.setSize(gamePanelSize);
                SoloPanel soloPanel = new SoloPanel(gamePanelSize);
                nextFrame.add(soloPanel);
                Image icon = Toolkit.getDefaultToolkit().getImage("images\\icons\\frog.png");
                nextFrame.setIconImage(icon);
                break;
            case 1:
                nextFrame = new ServerFrame(gamePanelSize);
                break;
        }
        nextFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        nextFrame.setVisible(true);
        nextFrame.setResizable(false);
        nextFrame.repaint();
    }

    public static void drawBottomRow(Graphics g) {
        for (int i = 0; i < WIDTH_GRID; i++) {
            g.setColor((i % 2 == 0) ? Color.GRAY : Color.DARK_GRAY);
            g.fillRect(i * TILE_SIZE, (HEIGHT_GRID - 1) * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
    }

    public static void addFont() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("ZxSpectrum7Bold-1GpEB.ttf")));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }
}

