import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public class Node {

	private int size;
	private int x, y;
	private Color color;
	private boolean isVisited = false;
	private boolean isBlocked = false;
	
	public Node(int x, int y, int size) {
		this.x = x;
		this.y = y;
		this.size = size;
		color = Color.WHITE;
	}
	
	public int getSize() {
		return size;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public boolean isVisited() {
		return isVisited;
	}
	
	public void setVisited(boolean isVisited) {
		this.isVisited = isVisited;
	}
	
	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
		color = isBlocked ? Color.BLACK : Color.WHITE;
	}
	
	public boolean isBlocked() {
		return isBlocked;
	}

	public Color getColor() {
		return color;
	}
	
	public boolean isContain(Point pt) {
		Rectangle rect = new Rectangle(x * size, y * size, size, size);
		if (rect.contains(pt)) return true;
		return false;
	}
	
	/***
	 * @param dir 
	 * @param map
	 * @return the adjacent cell if exists otherwise return null
	 */
	public Node findAdjNode(int dir, Node[][] map, int frameWidth, int frameHeight) {
		// dealing with boundary cases
		if (y == 0 && dir >= 0 && dir <= 2) {
			return null;
		} else if (y == (frameHeight / size) && dir >= 3 && dir <= 5) {
			return null;
		} else if (x == 0 && (dir == 0 || dir == 6 || dir == 7)) {
			return null;
		} else if (x == (frameWidth / size) && dir >= 2 && dir <= 4) {
			return null;
		}
		
		
		
		return null;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawRect(x * size, y * size, size, size);
		g.setColor(color);
		if (isBlocked) {
			g.fillRect(x * size, y * size, size, size);
		}
	}
	
}
