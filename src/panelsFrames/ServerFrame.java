package panelsFrames;

import movingObjectManagers.TurtleManager;
import general.GameManager;
import constants.Constants;
import utils.DataToClients;
import utils.Direction;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerFrame extends JFrame {
	
	public static final int PORT = 8911;
	static final int PLAYERS = 2;
	
	private ServerSocket server;
	
	private Socket socket1;
	private InputStream inputStream1;
	private ObjectInputStream objectinputStream1;
	private OutputStream outputStream1;
	private ObjectOutputStream objectOutputStream1;
	
	private Socket socket2;
	private InputStream inputStream2;
	private ObjectInputStream objectinputStream2;
	private OutputStream outputStream2;
	private ObjectOutputStream objectOutputStream2;
	
	private DataToClients data;
	
	private GameManager gameManager;
	private Direction[] frogsDirections;
	private Direction[] directionsFrogsWantToMove;
	
	private JLabel label;
	
	public ServerFrame(Dimension gamePanelSize){
		super("Frogger - Server Window");
		Image icon = Toolkit.getDefaultToolkit().getImage("images\\icons\\server_icon.png"); 
		setIconImage(icon);
		getContentPane().setBackground(Constants.TAN);
		setVisible(true);
		setSize(400, 200);
		label = new JLabel("no players joined");
		label.setBounds(200, 0, 200, 50);
		label.setFont(new Font(Constants.FONT_NAME, Font.PLAIN, 25));
		add(label);
		gameManager = new GameManager(gamePanelSize, PLAYERS);
		frogsDirections = new Direction[2];
		frogsDirections[0] = Direction.UP;
		frogsDirections[1] = Direction.UP;
		directionsFrogsWantToMove = new Direction[2];
		data = new DataToClients();
		((TurtleManager)gameManager.getWaterManager().get(1)).setDataToClient(data);
		((TurtleManager)gameManager.getWaterManager().get(4)).setDataToClient(data);
		try {serverConnection();} 
		catch (IOException e) {e.printStackTrace();}
	}
	
	public void serverConnection() throws IOException {
		server = new ServerSocket(PORT);
		
		socket1 = server.accept();
		outputStream1 = socket1.getOutputStream();
		objectOutputStream1 = new ObjectOutputStream(outputStream1);
		objectOutputStream1.writeObject(true);
		inputStream1 = socket1.getInputStream();
		objectinputStream1 = new ObjectInputStream(inputStream1);
		label.setText("p1 joined");
		
		socket2 = server.accept();
		outputStream2 = socket2.getOutputStream();
		objectOutputStream2 = new ObjectOutputStream(outputStream2);
		objectOutputStream2.writeObject(false);
		inputStream2 = socket2.getInputStream(); 
		objectinputStream2 = new ObjectInputStream(inputStream2);
		label.setText("p1 & p2 joined");
		boolean p1Ready = false, p2Ready = false;
		while (!p1Ready || !p2Ready) {
			if (!p1Ready) {
				try {
					Object obj = objectinputStream1.readObject();
					if (obj instanceof Boolean) {p1Ready = true;}
				} catch (ClassNotFoundException | IOException e) {e.printStackTrace();}
			}
			if (!p2Ready) {
				try {
					Object obj = objectinputStream2.readObject();
					if (obj instanceof Boolean) {p2Ready = true;}
				} catch (ClassNotFoundException | IOException e) {e.printStackTrace();}
			}
		}
		String s = "start";
		objectOutputStream1.writeObject(s);
		objectOutputStream2.writeObject(s);

		boolean[][] lilypadsTwoPlayers = new boolean[PLAYERS][gameManager.getFrogs()[0].getLilypadsFree().length];
		Thread handleClient1;
		handleClient1 = new Thread(new Runnable(){
			@Override
			public void run() {
				
				while (true) {
					boolean[] check = {true, true};
					for (int i = 0; i < PLAYERS; i++) {
						for (int j = 0; j < lilypadsTwoPlayers[i].length; j++) {
							lilypadsTwoPlayers[i][j] = gameManager.getFrogs()[i].getLilypadsFree()[j];
							check[i] &= !lilypadsTwoPlayers[i][j];
						}
					}
					if (check[0]) {
						System.out.println("p1 won");
					}
					if (check[1]) {
						System.out.println("p2 won");
					}
					
					data.fillData(gameManager.getRoadManager(), gameManager.getWaterManager(), lilypadsTwoPlayers,
							gameManager.getFrogs(), frogsDirections);
					try {
						objectOutputStream1.writeObject(data);
						objectOutputStream1.reset();
						directionsFrogsWantToMove[0] = (Direction)objectinputStream1.readObject();
						if (directionsFrogsWantToMove[0] != null) {
							frogsDirections[0] = directionsFrogsWantToMove[0];
							handleDirection(directionsFrogsWantToMove[0], true);
						} else {
							data.setPositionChanged(false, true);
						}
					} catch (IOException e) {
						System.out.println("p1 disconnected");
						if (data.getWinner() != null) 
								System.exit(0);
					} catch (ClassNotFoundException e1) {e1.printStackTrace();}
					if (!gameManager.getFrogs()[0].getAbleToMove()) {
						frogsDirections[0] = Direction.UP;
						try {Thread.sleep(5);} catch (InterruptedException e) {e.printStackTrace();}
					}
				}
			}
		});
		handleClient1.start();
		
		Thread handleClient2 = new Thread(new Runnable(){
			@Override
			public void run() {
				while (true) {
					data.fillData(gameManager.getRoadManager(), gameManager.getWaterManager(), lilypadsTwoPlayers,
							gameManager.getFrogs(), frogsDirections);
					try {
						objectOutputStream2.writeObject(data);
						objectOutputStream2.reset();
						directionsFrogsWantToMove[1] = (Direction)objectinputStream2.readObject();
						if (directionsFrogsWantToMove[1] != null) {
							frogsDirections[1] = directionsFrogsWantToMove[1];
							handleDirection(directionsFrogsWantToMove[1], false);
						} else { data.setPositionChanged(false, false); }
					} catch (IOException e) {
						System.out.println("p2 disconnected");
						if (data.getWinner() != null)
							System.exit(0);
					} catch (ClassNotFoundException e1) {e1.printStackTrace();}
					if (!gameManager.getFrogs()[1].getAbleToMove()) {
						frogsDirections[1] = Direction.UP;
					}
					try {Thread.sleep(5);} catch (InterruptedException e) {e.printStackTrace();} //
				}
			}
		});
		handleClient2.start();
		/*
		Thread handleClients = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					handleClient1.run();
					handleClient2.run();
					//try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
				}
			}
		});
		handleClients.setPriority(4);
		handleClients.start();
		*/
		label.setText("game started");
	}
	
	public void handleDirection(Direction direction, boolean isFirstFrog) {
		if (!gameManager.getFrogs()[(isFirstFrog ? 0 : 1)].getAbleToMove()) {
			data.setPositionChanged(true, isFirstFrog);
			return;
		}
		switch (direction) {
			case Direction.UP: {
				gameManager.getFrogs()[(isFirstFrog ? 0 : 1)].addY(-Constants.TILE_SIZE);
				break;
			}
			case Direction.DOWN: {
				gameManager.getFrogs()[(isFirstFrog ? 0 : 1)].addY(Constants.TILE_SIZE);
				break;
			}
			case Direction.LEFT: {
				gameManager.getFrogs()[(isFirstFrog ? 0 : 1)].addX(-Constants.TILE_SIZE);
				break;
			}
			case Direction.RIGHT: {
				gameManager.getFrogs()[(isFirstFrog ? 0 : 1)].addX(Constants.TILE_SIZE);
				break;
			}
		}
		data.setPositionChanged(true, isFirstFrog);
	}
}