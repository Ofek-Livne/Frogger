import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class ClientPanel extends JPanel implements Runnable {
	
	private Socket socket;
	private InputStream inputStream;
	private ObjectInputStream objectInputStream;
	private OutputStream outputStream;
	private ObjectOutputStream objectOutputStream;
	private Thread thread;
	
	private DataToClients data;
	private short[][] carsPositions;
	private short[][][] waterPositionsAndSizes; //x = x of object, y = size in pixels
	private short[][] turtlesDivingData;
	private boolean[][] lilypadsFreeTwoPlayes;
	
	private short[][] frogsLocation;
	private boolean isFirstFrog;
	private Image[] displayedFrogImages;
	private Direction[] frogsDirectionsForImages;
	private Direction whereIWantToMove;
	private Boolean winner;
	
	private Image[] carsImages, logImages, turtleImages, lilypadImages;
	private Image deadFrogImage;
	
	public ClientPanel() throws IOException { //TODO the server is sending who is dead, process the data
		super();
		data = null;
		frogsDirectionsForImages = new Direction[2];
		frogsDirectionsForImages[0] = Direction.UP;
		frogsDirectionsForImages[1] = Direction.UP;

		addKeyListener(new ClientKeyAdapter(this));
		thread = new Thread(this);
		thread.setPriority(4);
		initImages();
		connectToServer(ServerFrame.PORT);
	}
	
	public void connectToServer(int port) throws IOException {
		socket = new Socket("localhost", port);
		socket.setTcpNoDelay(true);
		inputStream = socket.getInputStream();
		objectInputStream = new ObjectInputStream(inputStream);
		outputStream = socket.getOutputStream();
		objectOutputStream = new ObjectOutputStream(outputStream);
	}
	
	@Override
	public void run() {
		displayedFrogImages = new Image[2];
		displayedFrogImages[0] = new ImageIcon("images\\frogs\\FrogUp" + (isFirstFrog ? 1 : 2) + ".png").getImage(); //maybe need a change
		displayedFrogImages[1] = new ImageIcon("images\\frogs\\FrogUp" + (!isFirstFrog ? 1 : 2) + ".png").getImage();

		while (true) {
			try {Thread.sleep(5);} catch (InterruptedException e) {}
			try {
				//long start = System.nanoTime(); //TIME************************************************************************************
				data = (DataToClients)objectInputStream.readObject();
				//System.out.print(System.nanoTime() - start + " "); //TIME*****************************************************************
				this.carsPositions = data.getCarsPositions();
				this.waterPositionsAndSizes = data.getWaterPositionsAndSizes();
				this.turtlesDivingData = data.getTurtlesDivingData();
				this.lilypadsFreeTwoPlayes = data.getLilypadsFree();
				this.frogsLocation = data.getFrogLocation();
				this.frogsDirectionsForImages = data.getFrogsDirections();
				updateFrogsImages(frogsDirectionsForImages);
				this.winner = data.getWinner();
				if (winner != null) {
					JOptionPane.showMessageDialog(this, "P" + (winner ? 1 : 2) + " won!");
					System.exit(0);
				}
				if (data.getPositionChanged()[(isFirstFrog ? 0 : 1)]) {
					whereIWantToMove = null;
				}
				objectOutputStream.writeObject(whereIWantToMove);
				repaint();
				//System.out.println(System.nanoTime() - start); //TIME*********************************************************************
			} catch (ClassNotFoundException e) {e.printStackTrace();
			} catch (ArrayIndexOutOfBoundsException e) { System.out.println("array index one frame error");
			} catch (IOException e) {
				try {socket.close();} catch (IOException e1) {e1.printStackTrace();}
				try {Thread.sleep(3000);} catch (InterruptedException e1) {e1.printStackTrace();}
				System.exit(0);
			}
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		if (data == null) return;
		
		for (int i = 0; i < Main. WIDTH_GRID; i++) {
			g.setColor((i % 2 == 1)?Color.GRAY:Color.DARK_GRAY);
			g.fillRect(i * Main.TILE_SIZE, 6 * Main.TILE_SIZE, Main.TILE_SIZE, Main.TILE_SIZE);
		}
		g.setColor(Color.BLACK);
		g.fillRect(0, (Main.WIDTH_GRID - 7) * Main.TILE_SIZE, Main.WIDTH_GRID * Main.TILE_SIZE, 5 * Main.TILE_SIZE);
		for (int i = 0; i < DataToClients.NUM_OF_ROADS; i++) {
			int j = 0;
			while (carsPositions[i][j] != DataToClients.EMPTY) {
				g.drawImage(carsImages[i], carsPositions[i][j], (Main.HEIGHT_GRID - 2 - i) * Main.TILE_SIZE,
						Main.TILE_SIZE *(1 + i / (DataToClients.NUM_OF_ROADS - 1)), Main.TILE_SIZE, null);
				j++;
			}
		}
		g.setColor(Color.BLUE);
		g.fillRect(0, 0, Main.WIDTH_GRID * Main.TILE_SIZE, 6 * Main.TILE_SIZE);
		int y;
		for (int i = 0; i < DataToClients.NUM_OF_ROADS; i++) {
			y = (i + 1) * Main.TILE_SIZE;
			int j = 0;
			while (waterPositionsAndSizes[i][j][0] != DataToClients.EMPTY) {
				short[] p = waterPositionsAndSizes[i][j];
				//p[1] = size
				if (i == 1 || i == 4) { //turtle
					for (int k = 0; k < p[1]; k += Main.TILE_SIZE + 10) {
						g.drawImage(turtleImages[0], p[0] + k, y, Main.TILE_SIZE, Main.TILE_SIZE, null);
					}
				}
				else { //log
					g.drawImage(logImages[2], p[0], y, Main.TILE_SIZE, Main.TILE_SIZE, null);
					g.drawImage(logImages[1], p[0] + Main.TILE_SIZE, y, p[1] - 2 * Main.TILE_SIZE, Main.TILE_SIZE, null);
					g.drawImage(logImages[0], p[0] + p[1] - Main.TILE_SIZE, y, Main.TILE_SIZE, Main.TILE_SIZE, null);
				}
				j++;
			}
		}
		g.setColor(Color.BLUE);
		for (int i = 0; i < turtlesDivingData.length; i++) {
			if (turtlesDivingData[i][0] != DataToClients.EMPTY) {
				y = (3 * i + 2) * Main.TILE_SIZE;
				for (int j = 0; j < Main.TILE_SIZE * 2; j += Main.TILE_SIZE + 10) {
					g.fillRect(turtlesDivingData[i][0] + j, y, Main.TILE_SIZE, Main.TILE_SIZE);
					g.drawImage(turtleImages[turtlesDivingData[i][1]], turtlesDivingData[i][0] + j, y,
							Main.TILE_SIZE, Main.TILE_SIZE, null);
				}
			}
		}
		//drawLilypads:
		Image lilypadImage;
		for (int i = 0; i < lilypadsFreeTwoPlayes[0].length; i++) {
			lilypadImage = lilypadImages[1 + (lilypadsFreeTwoPlayes[0][i]? 0 : 1) + (lilypadsFreeTwoPlayes[1][i]? 0 : 2)];
			g.drawImage(lilypadImage, (1 + i * 3) * Main.TILE_SIZE, 0, Main.TILE_SIZE, Main.TILE_SIZE, null);
		}
		
		Main.drawBottomRow(g);
		int myFrogIndex = isFirstFrog ? 0 : 1;
		int otherFrogIndex = !isFirstFrog ? 0 : 1;
		g.drawImage(displayedFrogImages[myFrogIndex], frogsLocation[myFrogIndex][0],
				frogsLocation[myFrogIndex][1], Main.TILE_SIZE, Main.TILE_SIZE, null);
		g.drawImage(displayedFrogImages[otherFrogIndex], frogsLocation[otherFrogIndex][0],
				frogsLocation[otherFrogIndex][1], Main.TILE_SIZE, Main.TILE_SIZE, null);
	}
	
	public void initImages() {
		carsImages = new Image[5];
		for (int i = 0; i < carsImages.length; i++) {
			carsImages[i] = new ImageIcon("images\\cars\\Car" + i + ".png").getImage();
		}
		logImages = new Image[3];
		logImages[0] = new ImageIcon("images\\logs\\LogStart.png").getImage();
		logImages[1] = new ImageIcon("images\\logs\\LogMiddle.png").getImage();
		logImages[2] = new ImageIcon("images\\logs\\LogEnd.png").getImage();
		turtleImages = new Image[4];
		for (int i = 0; i < turtleImages.length; i++) { //4 because last image is empty (underwater)
			turtleImages[i] = new ImageIcon("images\\turtles\\Turtle" + i + ".png").getImage();
		}
		lilypadImages = new Image[5];
		lilypadImages[0] = new ImageIcon("images\\lilypads\\LilypadFull.png").getImage();
		lilypadImages[1] = new ImageIcon("images\\lilypads\\LilypadEmpty.png").getImage();
		lilypadImages[2] = new ImageIcon("images\\lilypads\\LilypadP1.png").getImage();
		lilypadImages[3] = new ImageIcon("images\\lilypads\\LilypadP2.png").getImage();
		lilypadImages[4] = new ImageIcon("images\\lilypads\\LilypadBoth.png").getImage();
		deadFrogImage = new ImageIcon("images\\frogs\\Dead.png").getImage();
	}
	
	public void updateFrogsImages(Direction[] directions) {
		this.displayedFrogImages[0] = data.getFrogsDead()[0] ? deadFrogImage : MyKeyAdapter.getImage(directions[0], true);
		this.displayedFrogImages[1] = data.getFrogsDead()[1] ? deadFrogImage : MyKeyAdapter.getImage(directions[1], false);
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		JFrame frame = new JFrame("ERROR - Client did not get the isFirstFrog");
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(Main.WIDTH_GRID * Main.TILE_SIZE + Main.BORDER_WIDTH, Main.HEIGHT_GRID * Main.TILE_SIZE + Main.BORDER_HEIGHT);
		frame.setLocation(dim.width/2 - frame.getSize().width/2, dim.height/2 - frame.getSize().height/2);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		
		ClientPanel clientPanel = new ClientPanel();
		Object obj = clientPanel.objectInputStream.readObject();
		if (obj instanceof Boolean) {
			clientPanel.isFirstFrog = ((Boolean)obj).booleanValue();
			frame.setTitle("Frogger - P" + (1 + (clientPanel.isFirstFrog ? 0 : 1)));
		}
		
		Image icon = Toolkit.getDefaultToolkit().getImage("images\\frogs\\FrogUp" + (clientPanel.isFirstFrog ? 1 : 2 ) + ".png");
		frame.setIconImage(icon);
		
		ReadyPanel readyPanel = new ReadyPanel(clientPanel.isFirstFrog);
		frame.setVisible(true);
		frame.add(readyPanel);
		frame.repaint();
		while (!readyPanel.isReady()) {
			try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
		}
		clientPanel.objectOutputStream.writeObject(true);
		
		obj = clientPanel.objectInputStream.readObject();
		if (obj instanceof String) {
			if (((String)obj).equals("start")) {
				frame.getContentPane().removeAll();
				frame.add(clientPanel);
				frame.setVisible(true);
				frame.repaint();
				clientPanel.thread.start();
				clientPanel.requestFocus();
			}
		}
	}
	
	public class ClientKeyAdapter extends MyKeyAdapter {

		public ClientKeyAdapter(JPanel panel) {
			super(panel);
		}
		
		public void putInArray(Direction[] directions, boolean isFirstFrog, Direction value) {
			directions[(isFirstFrog ? 0 : 1 )] = value;
		}

		@Override
		public void keyUp() {
			putInArray(frogsDirectionsForImages, isFirstFrog, Direction.UP);
			whereIWantToMove = Direction.UP;
		}

		@Override
		public void keyDown() {
			putInArray(frogsDirectionsForImages, isFirstFrog, Direction.DOWN);	
			whereIWantToMove = Direction.DOWN;
		}

		@Override
		public void keyLeft() {
			putInArray(frogsDirectionsForImages, isFirstFrog, Direction.LEFT);	
			whereIWantToMove = Direction.LEFT;
		}

		@Override
		public void keyRight() {
			putInArray(frogsDirectionsForImages, isFirstFrog, Direction.RIGHT);
			whereIWantToMove = Direction.RIGHT;
		}
	}
}
