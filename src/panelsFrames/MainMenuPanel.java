package panelsFrames;

import constants.Constants;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainMenuPanel extends JPanel {
    GridBagConstraints gbc;
    JLabel backgroundLabel;
    JButton soloButton, multiplayerButton;

    public MainMenuPanel() {
        BufferedImage background = null;
        try {
            background = ImageIO.read(new File("images\\backgrounds\\bg840.png"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        gbc = new GridBagConstraints();
        backgroundLabel = new JLabel(new ImageIcon(background));
        backgroundLabel.setLayout(new GridBagLayout());

        add(backgroundLabel);
        gbc.gridy = 1;

        //space left
        gbc.gridx = 0;
        backgroundLabel.add(Box.createHorizontalStrut(20), gbc);

        //solo button
        gbc.gridx = 1;
        soloButton = new JButton("SOLO");
        setButtonAppearance(soloButton);
        backgroundLabel.add(soloButton, gbc);

        //space between the buttons
        gbc.gridx = 2;
        backgroundLabel.add(Box.createHorizontalStrut(40), gbc);

        //multiplayer button
        gbc.gridx = 3;
        multiplayerButton = new JButton("1v1");
        setButtonAppearance(multiplayerButton);
        backgroundLabel.add(multiplayerButton, gbc);

        //space right
        gbc.gridx = 4;
        backgroundLabel.add(Box.createHorizontalStrut(40), gbc);

        //space from below
        gbc.gridy = 2;
        backgroundLabel.add(Box.createVerticalStrut(130), gbc);
    }

    public JButton getSoloButton() {
        return soloButton;
    }

    public JButton getMultiplayerButton() {
        return multiplayerButton;
    }

    public void setButtonAppearance(JButton button) {

        Border emptyBorder = BorderFactory.createEmptyBorder();

        button.setPreferredSize(new Dimension(180, 100));
        button.setFont(new Font(Constants.FONT_NAME, Font.PLAIN, 45));
        button.setBackground(Constants.TAN);

        button.setBorder(emptyBorder);
        button.setFocusPainted(false);

        button.setContentAreaFilled(false);
        button.setOpaque(true);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                button.setForeground(Constants.PINK);
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                button.setForeground(Color.BLACK);
                button.setBackground(Constants.TAN);
            }

            @Override
            public void mousePressed(MouseEvent evt) {
                button.setBackground(Constants.MAIN_FROG_GREEN);
            }
        });
    }
}
