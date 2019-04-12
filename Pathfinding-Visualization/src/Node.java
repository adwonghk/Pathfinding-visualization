import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public class Node {
	
	private int frameWidth = VisualPanel.frameWidth;
	private int frameHeight = VisualPanel.frameHeight;

	private int size;
	private int x, y;
	private Color color;
	private boolean isVisited = false;
	private boolean isBlocked = false;
	
	private boolean isPaintIndex = false; // somehow painting index is very slow
	
	// the linkedNode contains all the node that are linked to this node
	private Node[] linkedNode = new Node[4];
	private int num_linkedNode = 0;
	
	private boolean isCurrentNode = false;
	
	public Node(int x, int y, int size) {
		this.x = x;
		this.y = y;
		this.size = size;
		color = Color.WHITE;
	}
	
	public void setFrameSize(int frameWidth, int frameHeight) {
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
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
	
	public void setCurrentNode(boolean isCurrentNode) {
		this.isCurrentNode = isCurrentNode;
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
	
	public void paintIndex(boolean paintIndex) {
		this.isPaintIndex = paintIndex;
	}
	
	// searching for the adjacent node in a certain direction
	public Node findAdjNode(Direction dir, Node[][] map) {
		// dealing with boundary cases
		int downMost = frameHeight / size - 1;
		int leftMost = frameWidth / size - 1;
		if (dir == Direction.UP && (y == 0 || y == 1)) {
			return null;
		} else if (dir == Direction.DOWN && (y == downMost || y == downMost - 1)) {
			return null;
		} else if (dir == Direction.LEFT && (x == 0 || x == 1)) {
			return null;
		} else if (dir == Direction.RIGHT && (x == leftMost || x == leftMost - 1)) {
			return null;
		}
		
		if (dir == Direction.UP) {
			return map[x][y - 2];
		} else if (dir == Direction.DOWN) {
			return map[x][y + 2];
		} else if (dir == Direction.LEFT) {
			return map[x - 2][y];
		} else if (dir == Direction.RIGHT) {
			return map[x + 2][y];
		}
		
		return null;
	}
	
	public boolean canVisitAdjNode(Node[][] map) {
		for (int i = 0; i < 4; i++) {
			Node node = findAdjNode(Direction.valueOf(i), map);
			// if the node doesnt exist, check for next one
			if (node == null) continue;
			if (!node.isVisited()) return true;
		}
		return false;
	}
	
	// if all adj nodes have been visited, return true
	public boolean checkAdjNodeVisited(Node[][] map) {
		for (int i = 0; i < 4; i++) { 
			Node node = findAdjNode(Direction.valueOf(i), map);
			if (node == null) continue;
			if (!node.isVisited()) return false;
		}
		
		return true;
	}
	
	public void linking(Node node) {
		linkedNode[num_linkedNode++] = node;
		node.linkedNode[node.num_linkedNode++] = this;
	}
	
	public void draw(Graphics g, int xOffset, int yOffset) {
		g.setColor(Color.LIGHT_GRAY);
		g.drawRect(x * size + xOffset, y * size + yOffset, size, size);
		
		if (isPaintIndex) {
			g.setColor(isBlocked ? Color.WHITE : Color.BLACK);
			g.drawString(x + ", " + y, x * size + xOffset, y * size + 10 + yOffset);
		}
		
		if (isVisited) {
			g.setColor(new Color(127, 189, 210));
			g.fillRect(x * size + 1 + xOffset, y * size + 1 + yOffset, size - 2, size - 2);
		}
		
		if (isBlocked) {
			g.setColor(color);
			g.fillRect(x * size + 1 + xOffset, y * size + 1 + yOffset, size - 2, size - 2);
		}
		
		if (isCurrentNode) {
			g.setColor(Color.YELLOW);
			g.fillRect(x * size + 1 + xOffset, y * size + 1 + yOffset, size - 2, size - 2);
		}
	}

	public boolean isLinkedWith(Node node) {
		for (int i = 0; i < linkedNode.length; i++) {
			if (linkedNode[i] == null) continue;
			if (linkedNode[i] == node) return true;
		}
		return false;
	}
	
	public boolean validPoint(int x, int y) {
		int downMost = frameHeight / size - 1;
		int leftMost = frameWidth / size - 1;
		if (x < 0 || x > leftMost) return false;
		if (y < 0 || y > downMost) return false;
		return true;
	}
	
	// call this after the maze generation
	public boolean isPath(Node[][] map) {
		// check between above and below
		if (validPoint(x, y - 1) && validPoint(x, y + 1)) {
			if (map[x][y - 1].isLinkedWith(map[x][y + 1])) {
				return true;
			}
		}
		
		// check between left and right
		if (validPoint(x - 1, y) && validPoint(x + 1, y)) {
			if (map[x - 1][y].isLinkedWith(map[x + 1][y])) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + "), isBlocked: " + isBlocked + ", isVisited: " + isVisited;
	}
}
