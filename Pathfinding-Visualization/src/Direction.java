
public enum Direction {
	UP,
	DOWN,
	LEFT,
	RIGHT;

	public static Direction valueOf(int i) {
		switch (i) {
		case 0:
			return UP;
		case 1:
			return DOWN;
		case 2:
			return LEFT;
		case 3:
			return RIGHT;
		}
		return null;
	}
}
