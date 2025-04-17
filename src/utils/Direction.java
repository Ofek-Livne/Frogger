package utils;

public enum Direction {
	UP,
	DOWN,
	LEFT,
	RIGHT;
	
	public static byte directionToByte(Direction direction) {
		if (direction != null)
			return (byte)direction.ordinal();
		return -1;
	}
	
	public static Direction byteToDirection(byte b) {
		if (b != -1)
			return values()[b];
		return null;
	}
}


