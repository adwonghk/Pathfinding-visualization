import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class VisualPanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
	private static final long serialVersionUID = 1L;

	private int frameWidth, frameHeight;
	private final int  cellSize;
	
	private Node[][] map;
	
	// for generating the maze
	private Random rand;
	
	private boolean drawing = false;
	
	//private Timer timer;
	//int x, y;
	
	public VisualPanel(int width, int height) {
		setFocusable(true);
		requestFocusInWindow();
		
		frameWidth = width;
		frameHeight = height;
		cellSize = 40; // find common factor of the frame width and frame height
		
		map = new Node[frameWidth / cellSize][frameHeight / cellSize];
		
		rand = new Random();
		
		for (int x = 0; x < frameWidth / cellSize; x++) {
			for (int y = 0; y < frameHeight / cellSize; y++) {
				map[x][y] = new Node(x, y, cellSize);
			}
		}
		
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		
		//x = 0;
		//y = 0;
		
		/*timer = new Timer(100, e -> {
			delay(x++, y++);
			repaint();
		});*/
	}
	
	private void delay(int x, int y) {
		if (x >= map.length) return;
		if (y >= map[0].length) return;
		map[x][y].setBlocked(true);
	}
	
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				map[i][j].draw(g);
			}
		}
	}
	
	private void generateMaze(Node[][] map) {
		
	}
	
	private Node recursvieBacktracker(Node[][] map) {
		return null;
	}
	
	private void primsAlgorithm(Node[][] map) {
		ArrayList<Node> wallList = new ArrayList<Node>();
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[0].length; y++) {
				map[x][y].setBlocked(true);
			}
		}
		int randX = rand.nextInt(map.length);
		int randY = rand.nextInt(map[0].length);
		map[randX][randY].setBlocked(false);
	}
	
	private void buildInnerWall(Node[][] map) {
		buildOuterWall(map);
		for (int x = 2; x < map.length; x +=2) {
			for (int y = 1; y < map[0].length; y +=2) {
				map[x][y].setBlocked(true);
				try {
					Thread.sleep(1000);
					System.err.println("drawing");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		for (int x = 1; x < map.length; x +=2) {
			for (int y = 2; y < map[0].length; y +=2) {
				map[x][y].setBlocked(true);
			}
		}
	}
	
	private void buildOuterWall(Node[][] map) {
		for (int x = 0; x < frameWidth / cellSize; x++) {
			map[x][0].setBlocked(true);
			map[x][(frameHeight / cellSize) - 1].setBlocked(true);
		}
		for (int y = 0; y < frameHeight / cellSize; y++) {
			map[0][y].setBlocked(true);
			map[(frameWidth / cellSize) - 1][y].setBlocked(true);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			drawing = true;
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			drawing = false;
		}
		
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				if (map[i][j].isContain(e.getPoint())) {
					map[i][j].setBlocked(drawing);
					break;
				}
			}
		}
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		drawing = false;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e) || SwingUtilities.isRightMouseButton(e)) {
			for (int i = 0; i < map.length; i++) {
				for (int j = 0; j < map[0].length; j++) {
					if (map[i][j].isContain(e.getPoint())) {
						map[i][j].setBlocked(drawing);
						break;
					}
				}
			}
		}
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}


	@Override
	public void keyPressed(KeyEvent e) {
	}


	@Override
	public void keyReleased(KeyEvent e) {
	}


	@Override
	public void keyTyped(KeyEvent e) {
		//System.out.println("building wall");
		//buildInnerWall(map);

	}

}
