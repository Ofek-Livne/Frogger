package panelsFrames;

import main.Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ReadyPanel extends JPanel {
	GridBagConstraints gbc;
	JLabel backgroundLabel, title, youAre, youAreImage, instructions;
	JButton readyButton;
	private boolean isReady; //deafult value is false
	
	public ReadyPanel(boolean isFirstFrog) {
		BufferedImage background = null, frog_image = null;
		try {
			background = ImageIO.read(new File("images\\backgrounds\\bgReady.png"));
			frog_image = ImageIO.read(new File("images\\frogs\\FrogUp" + (isFirstFrog ? 1 : 2) + ".png"));
		} catch (IOException e1) {e1.printStackTrace();}
		
		gbc = new GridBagConstraints();
		backgroundLabel = new JLabel(new ImageIcon(background));
		backgroundLabel.setLayout(new GridBagLayout());
		add(backgroundLabel);

		// space
		gbc.gridy = 0;
		backgroundLabel.add(Box.createVerticalStrut(20), gbc);
		
		// title	
		title = new JLabel("FROGGER");
		title.setFont(new Font(Main.FONT_NAME, Font.PLAIN, 120));
		gbc.gridy = 1;
		backgroundLabel.add(title, gbc);
		
		// space
		gbc.gridy = 2;
		backgroundLabel.add(Box.createVerticalStrut(100), gbc);
		
		// ready button
		readyButton = new JButton("ready?");
		readyButton.setPreferredSize(new Dimension(400, 200));
		readyButton.setFont(new Font(Main.FONT_NAME, Font.PLAIN, 60));
		readyButton.setBackground(Main.PINK);
		readyButton.setBorderPainted(false);
		readyButton.setFocusPainted(false);
		readyButton.setContentAreaFilled(false);
		readyButton.setOpaque(true);
		readyButton.addMouseListener(new MouseAdapter() {
			@Override
            public void mouseEntered(MouseEvent evt) {
				if (!isReady)
					if (isFirstFrog)
						readyButton.setForeground(Main.FROG_GREEN);
					else
						readyButton.setForeground(Main.FROG_CYAN);
            }
			@Override
            public void mouseExited(MouseEvent evt) {
				readyButton.setForeground(Color.BLACK);
            }
			
			@Override
			public void mouseClicked(MouseEvent evt) {
				isReady = true;
				readyButton.setForeground(Color.BLACK);
				readyButton.setText("ready!");
				if (isFirstFrog)
					readyButton.setBackground(Main.FROG_GREEN);
				else
					readyButton.setBackground(Main.FROG_CYAN);
			}
        });
		gbc.gridy = 3;
		backgroundLabel.add(readyButton, gbc);
		
		// space
		gbc.gridy = 4;
		backgroundLabel.add(Box.createVerticalStrut(60), gbc);
		
		// you are line
		youAre = new JLabel("You are:");
		youAre.setFont(new Font(Main.FONT_NAME, Font.PLAIN, 40));
		Image biggerYouAreImage = new ImageIcon(frog_image).getImage();
		biggerYouAreImage = biggerYouAreImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		youAreImage = new JLabel(new ImageIcon(biggerYouAreImage));
		gbc.gridy = 5;
		backgroundLabel.add(youAre, gbc);
		backgroundLabel.add(youAreImage, gbc);
		
		// space
		gbc.gridy = 6;
		backgroundLabel.add(Box.createVerticalStrut(20), gbc);
		
		// instructions
		instructions = new JLabel("<html>first to all 5 lilipads wins!"
				+ "<br>don't get hit by the cars<br>"
				+ "surf on the logs and turtles</html>");
		instructions.setFont(new Font(Main.FONT_NAME, Font.PLAIN, 30));
		gbc.gridy = 7;
		backgroundLabel.add(instructions, gbc);
		
	}

	public boolean isReady() {
		return isReady;
	}	
}
