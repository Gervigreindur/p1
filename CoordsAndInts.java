package prog1;

public class CoordsAndInts {

	public static int coordinatesToInt(int x, int y, Pair size) {
		return (y - 1) * size.getX() + (x - 1);
	}
	
	public static Pair intToCoord(Integer number, Pair size) {
		return new Pair((number % size.getX()) + 1, (number / size.getX()) + 1);
	}
}
