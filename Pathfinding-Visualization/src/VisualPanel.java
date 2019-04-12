import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Random;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class VisualPanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
	private static final long serialVersionUID = 1L;

	public static int frameWidth, frameHeight;
	private int xOffset, yOffset;
	private int  cellSize;
	
	private Node[][] map;
	
	// for generating the maze
	private Random rand;
	
	private boolean drawing = false;
	
	private Thread thread;
	
	private boolean recursive = false;
	
	// GUI
	private JButton btnGenMaze;
	
	public VisualPanel(int width, int height) {
		setFocusable(true);
		requestFocusInWindow();
		setPreferredSize(new Dimension(frameWidth, frameHeight));
		setBackground(Color.BLACK);
		
		thread = new Thread(() -> {
			if (recursive) {
				Stack<Node> stack = new Stack<Node>();
				map[0][0].setVisited(true);
				stack.push(map[0][0]);
				recursvieBacktracker(map, stack);
				
				for (int i = 0; i < map.length; i++) {
					for (int j = 0; j < map[0].length; j++) {
						if (!map[i][j].isVisited()) map[i][j].setBlocked(true);
						try {
							Thread.sleep(5);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						repaint();
					}
				}
			}
			recursive = false;
		});
		
		frameWidth = width;
		frameHeight = height;
		
		cellSize = 20;
		
		for (int i = 20; i < 40; i++) {
			if (frameWidth / i % 2 != 0 && frameHeight / i % 2 != 0) {
				cellSize = i;
				break;
			}
		}
		
		xOffset = (frameWidth - cellSize * (frameWidth / cellSize)) / 2;
		yOffset = (frameHeight - cellSize * (frameHeight / cellSize)) / 2;
		
		System.out.println("Frame width and height: " + frameWidth + ", " + frameHeight);
		System.out.println("Cell size: " + cellSize);
		System.out.println("Constructing a " + frameWidth / cellSize + " by " + frameHeight / cellSize + " map...");
		System.out.println("xOffset, yOffset: " + xOffset + ", " + yOffset);
		
		map = new Node[frameWidth / cellSize][frameHeight / cellSize];
		
		rand = new Random();
		
		for (int x = 0; x < frameWidth / cellSize; x++) {
			for (int y = 0; y < frameHeight / cellSize; y++) {
				map[x][y] = new Node(x, y, cellSize);
				//map[x][y].setPaintIndex(true);
				//num_nodes++;
			}
		}
		
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);

		btnGenMaze = new JButton("Generate Maze");
		btnGenMaze.addActionListener(e -> {
			if (!recursive) {
				recursive = true;
				
				System.out.println("Starting recursvie backtracker...");
				if (thread != null && !thread.isAlive()) {
					thread.start();
				}
			}
		});
		//btnGenMaze.setBounds(10, frameHeight - 50, 300, 30);
		add(btnGenMaze);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.WHITE);
		g.fillRect(xOffset, yOffset, frameWidth - xOffset * 2, frameHeight - yOffset * 2);
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				map[i][j].draw(g, xOffset, yOffset);
			}
		}
	}
	
	// Choose initial node
	// Can visit neighbor?
	// - if can
	//  1. pick random neighbor that can be visit
	//  2. put neighbor on stack
	//  3. mark path to neighbor
	// Mark visited and pop
	private void recursvieBacktracker(Node[][] map, Stack<Node> stack) {		
		System.out.println("Current node: " + stack.peek());
		stack.peek().setCurrentNode(true);
		
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				if (map[i][j].isPath(map)) map[i][j].setVisited(true);
			}
		}
		
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		repaint();
		
		while (stack.peek().canVisitAdjNode(map)) {
			Node randNeighbor = null;
			do {
				randNeighbor = stack.peek().findAdjNode(Direction.valueOf(rand.nextInt(4)), map);
				// check if the randNeighbor have been already visited
				if (randNeighbor != null) {
					if (randNeighbor.isVisited()) randNeighbor = null;
				}
			} while (randNeighbor == null);
			
			System.out.println("Neighbor node: " + randNeighbor.getX() + " " + randNeighbor.getY());
			
			stack.peek().setCurrentNode(false);
			
			randNeighbor.setVisited(true);
			stack.peek().linking(randNeighbor);
			stack.push(randNeighbor);
			
			recursvieBacktracker(map, stack);
			stack.pop().setCurrentNode(false);
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
			if (recursive) {
				for (int i = 0; i < map.length; i++) {
					for (int j = 0; j < map[0].length; j++) {
						if (map[i][j].isContain(e.getPoint())) {
							System.out.println("Node info: " + map[i][j]);
							break;
						}
					}
				}
			}
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
	}

}
