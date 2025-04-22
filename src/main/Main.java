package main;

import panelsFrames.MainMenuPanel;
import panelsFrames.ServerFrame;
import panelsFrames.SoloPanel;
import constants.Constants;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Main {
    private static JFrame nextFrame = null;

    public static void main(String[] args) {
        addFont();
        JFrame MainMenuFrame;
        MainMenuFrame = new JFrame("Frogger");
        MainMenuPanel mainMenuPanel = new MainMenuPanel();

        MainMenuFrame.setSize(Constants.MAIN_MENU_PANEL_SIZE);
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
                nextFrame.setSize(Constants.GAME_PANEL_SIZE);
                SoloPanel soloPanel = new SoloPanel(Constants.GAME_PANEL_SIZE);
                nextFrame.add(soloPanel);
                Image icon = Toolkit.getDefaultToolkit().getImage("images\\icons\\frog.png");
                nextFrame.setIconImage(icon);
                break;
            case 1:
                nextFrame = new ServerFrame(Constants.GAME_PANEL_SIZE);
                break;
        }
        nextFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        nextFrame.setVisible(true);
        nextFrame.setResizable(false);
        nextFrame.repaint();
    }

    public static void drawBottomRow(Graphics g) {
        for (int i = 0; i < Constants.WIDTH_GRID; i++) {
            g.setColor((i % 2 == 0) ? Color.GRAY : Color.DARK_GRAY);
            g.fillRect(i * Constants.TILE_SIZE, (Constants.HEIGHT_GRID - 1) * Constants.TILE_SIZE,
                    Constants.TILE_SIZE, Constants.TILE_SIZE);
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

