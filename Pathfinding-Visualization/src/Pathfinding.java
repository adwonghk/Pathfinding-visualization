import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class Pathfinding {
	
	public Pathfinding() {
		JFrame frame = new JFrame();
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Pathfinding Visualization - by Adrian Wong");
		frame.setUndecorated(true);
		setFullscreen(frame);
		frame.setLayout(null);
		frame.setContentPane(new VisualPanel(frame.getWidth(), frame.getHeight()));
		frame.requestFocus();
		frame.setVisible(true);
	}
	
	private void setFullscreen(JFrame frame) {
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		Rectangle bounds = gc.getBounds();
		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
		
		Rectangle effectiveScreenArea = new Rectangle();

		effectiveScreenArea.x = bounds.x + screenInsets.left;
		effectiveScreenArea.y = bounds.y + screenInsets.top;
		effectiveScreenArea.height = bounds.height - screenInsets.top - screenInsets.bottom;        
		effectiveScreenArea.width = bounds.width - screenInsets.left - screenInsets.right;
		
		frame.setSize(effectiveScreenArea.width, effectiveScreenArea.height);
	}
	
	public static void main(String[] args) {
		new Pathfinding();
	}

}

