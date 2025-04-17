import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

public class DataToClients implements Serializable {
	
	private static final long serialVersionUID = 1L;

	static final short NUM_OF_ROADS = 5;
	static final short ROAD_BUFFER_COLUMNS = 6;
	static final short EMPTY = -420;
	
	private short[][] carsPositions;
	private short[][][] waterPositionsAndSizes; //x = x of object, y = size in pixels
	transient private Turtle[] turtlesDiving; //transient = not sent
	private short[][] turtlesDivingData = {{EMPTY, EMPTY}, {EMPTY, EMPTY}}; //[i][0] = x of turtle, [i][1] = diving state
	private boolean[][] lilypadsFree;
	private short[][] frogsLocation;
	private byte[] frogsDirections;
	private boolean[] positionChanged;
	private boolean[] frogsDead;
	private Boolean winner = null; //null = no winner yet, true/false = is first frog the winner
	transient long timeWon = -1;
		
	public DataToClients() {
		carsPositions = new short[NUM_OF_ROADS][ROAD_BUFFER_COLUMNS];
		for (int i = 0; i < NUM_OF_ROADS; i++) {
			for (int j = 0; j < ROAD_BUFFER_COLUMNS; j++) {
				carsPositions[i][j] = EMPTY;
			}
		}
		waterPositionsAndSizes = new short[NUM_OF_ROADS][ROAD_BUFFER_COLUMNS][2];
		for (int i = 0; i < NUM_OF_ROADS; i++) {
			for (int j = 0; j < ROAD_BUFFER_COLUMNS; j++) {
				waterPositionsAndSizes[i][j][0] = EMPTY;
				waterPositionsAndSizes[i][j][1] = 0;
			}
		}
		turtlesDiving = new Turtle[2];
		lilypadsFree = new boolean[2][5];
		frogsLocation = new short[2][2];
		frogsDead = new boolean[2];
		positionChanged = new boolean[2];
		
		frogsDirections = new byte[2];
		frogsDirections[0] = Direction.directionToByte(Direction.UP);
		frogsDirections[1] = Direction.directionToByte(Direction.UP);
	}
	
	public short[][] getCarsPositions() {
		return carsPositions;
	}

	public short[][][] getWaterPositionsAndSizes() {
		return waterPositionsAndSizes;
	}

	public Turtle[] getTurtlesDiving() {
		return turtlesDiving;
	}
	
	public short[][] getTurtlesDivingData() {
		return turtlesDivingData;
	}

	public boolean[][] getLilypadsFree() {
		return lilypadsFree;
	}
	
	public short[][] getFrogLocation() {
		return frogsLocation;
	}
	
	public Direction[] getFrogsDirections() {
		Direction[] ret = { Direction.byteToDirection(frogsDirections[0]),
							Direction.byteToDirection(frogsDirections[1])};
		return ret;
	}
	
	public boolean[] getPositionChanged() {
		return positionChanged;
	}
	
	public void setPositionChanged(boolean positionChanged, boolean isFirstPlayer) {
		this.positionChanged[(isFirstPlayer ? 0 : 1)] = positionChanged;
	}
	
	public boolean[] getFrogsDead() {
		return frogsDead;
	}
	
	public Boolean getWinner() {
		return winner;
	}
	
	public void checkForWinner() {
		for (int i = 0, j; i < lilypadsFree.length; i++) {
			boolean res = true;
			for (j = 0; j < lilypadsFree[0].length; j++) {
				res &= !lilypadsFree[i][j];
			}
			if (res) { //did not break, thus all the values are true
				if (timeWon == -1) {
					timeWon = System.currentTimeMillis();
				} else if (timeWon + 300 < System.currentTimeMillis()) {					
					winner = (i == 0);
				}
			}
		}
	}
	 
	public void fillCarsPositions(CarManager[] roadManager) {
		int i = 0, j;
		LinkedList<MovingObject> carsList;
		for (; i < NUM_OF_ROADS; i++) {
			carsList = roadManager[i].getList();
			j = 0;
			for (; j < carsList.size(); j++) {
				try {
					carsPositions[i][j] 
							= (short)carsList.get(j).getX();
				} catch (IndexOutOfBoundsException e) {System.out.println("cars list out of bounds");
				} catch (NullPointerException e) {System.out.println("used 'get' on null in car list");}
			}
			for (; j < ROAD_BUFFER_COLUMNS && carsPositions[i][j] != EMPTY; j++) {
				carsPositions[i][j] = EMPTY;
			}
		}
	}
	
	public void fillWaterPositionsAndSizes(ArrayList<MovingObjectManager> waterManager) {
		int i = 0, j;
		LinkedList<MovingObject> waterObjectsList;
		for (; i < NUM_OF_ROADS; i++) {
			waterObjectsList = waterManager.get(i).getList();
			j = 0;
			for (; j < waterObjectsList.size(); j++) {
				try {
					waterPositionsAndSizes[i][j][0] = (short)waterObjectsList.get(j).getX();
					waterPositionsAndSizes[i][j][1] = (short)waterObjectsList.get(j).getSize();
				} catch (IndexOutOfBoundsException e) {System.out.println("water list out of bounds");
				} catch (NullPointerException e) {System.out.println("used 'get' on null in water list");}
			}
			for (; j < ROAD_BUFFER_COLUMNS && waterPositionsAndSizes[i][j][0] != EMPTY; j++) {
				waterPositionsAndSizes[i][j][0] = EMPTY;
				waterPositionsAndSizes[i][j][1] = 0;
			}
		}
	}
		
	public void setTurtleDiving(Turtle turtle, int index) {
		this.turtlesDiving[index] = turtle;
	}
	
	public void turtleDoneDiving(int index) {
		this.turtlesDiving[index] = null;
	}
	
	public void fillTurtlesDivingData() {
		for (int i = 0; i < turtlesDiving.length; i++) { //two turtle lines
			if (turtlesDiving[i] != null) {
				if (turtlesDiving[i].getDivingState() == 0) {
					turtlesDiving[i] = null;
					turtlesDivingData[i][0] = EMPTY;
				} else if (turtlesDivingData[i] == null){				
					turtlesDivingData[i][0] = (short)turtlesDiving[i].getX();
					turtlesDivingData[i][1] = (short)turtlesDiving[i].getDivingState();
				} else {
					turtlesDivingData[i][0] = (short)turtlesDiving[i].getX();
					turtlesDivingData[i][1] = (short)turtlesDiving[i].getDivingState();
				}
			}
		}
	}
	
	public void fillLilypadsFree(boolean[][] lilypadsFree) {
		for (int i = 0; i < lilypadsFree.length; i++) {
			for (int j = 0; j < lilypadsFree[i].length; j++) {
				this.lilypadsFree[i][j] = lilypadsFree[i][j];
			}
		}
		
	}
	
	public void fillFrogLocation(Frog[] frogs) {
		this.frogsLocation[0][0] = (short)frogs[0].getX();
		this.frogsLocation[0][1] = (short)frogs[0].getY();
		this.frogsLocation[1][0] = (short)frogs[1].getX();
		this.frogsLocation[1][1] = (short)frogs[1].getY();
	}
	
	public void fillFrogsDirections(Direction[] frogsDirections) {
		this.frogsDirections[0] = Direction.directionToByte(frogsDirections[0]);
		this.frogsDirections[1] = Direction.directionToByte(frogsDirections[1]);
	}
	
	public void fillFrogsDead(Frog[] frogs) { //myb change in future if not working correctly
		frogsDead[0] = !frogs[0].getAbleToMove();
		frogsDead[1] = !frogs[1].getAbleToMove();
	}
	
	public void fillData(CarManager[] roadManager, ArrayList<MovingObjectManager> waterManager, boolean[][] lilypadsFree,
			Frog[] frogs, Direction[] frogsDirections) {
		fillCarsPositions(roadManager);
		fillWaterPositionsAndSizes(waterManager);
		fillTurtlesDivingData();
		fillLilypadsFree(lilypadsFree);
		fillFrogLocation(frogs);
		fillFrogsDirections(frogsDirections);
		fillFrogsDead(frogs);
		checkForWinner();
	}

	@Override
	public String toString() {
		String str = "";
		for (int i = 0; i < NUM_OF_ROADS; i++) {
			int j = 0;
			while (j < ROAD_BUFFER_COLUMNS && carsPositions[i][j] != EMPTY) {
				str +="" + carsPositions[i][j++] + " -> ";
			}
			str += "\n";
		}
		str += "\n";
		for (int i = 0; i < NUM_OF_ROADS; i++) {
			int j = 0;
			while (j < ROAD_BUFFER_COLUMNS && waterPositionsAndSizes[i][j][0] != EMPTY) {
				str +="[" + waterPositionsAndSizes[i][j++][0] + ", " + waterPositionsAndSizes[i][j++][0] + "] -> ";
			}
			str += "\n";
		}
		str += "\n";
		for (int i = 0; i < lilypadsFree.length; i++) {
			for (int j = 0; j < lilypadsFree[i].length; j++)
				str +="" + lilypadsFree[i][j] + " -> ";
			str += "\n";
		}
		str += "\n";
		return str;
	}
}
