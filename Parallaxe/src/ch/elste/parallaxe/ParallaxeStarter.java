package ch.elste.parallaxe;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class ParallaxeStarter implements Runnable{
	public static volatile boolean rendering = false;
	
	private static final ParallaxeStarter instance = new ParallaxeStarter();
	
	private final JFrame frame;
	private final JPanel panel;
	private final Tree tree;
	private BufferedImage currFrame;
	
	public ParallaxeStarter() {
		frame = new JFrame("Parallaxe");
		panel = new JPanel();
		
		panel.setPreferredSize(new Dimension(800, 800));
		frame.setContentPane(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				rendering = false;
				super.windowClosing(e);
				System.exit(0);
			}
		});
		
		tree = new Tree(200, 200, 200);
		currFrame = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(instance);
	}
	
	@Override
	public void run() {
		SwingWorker<Void, Void> renderThread = new SwingWorker<Void, Void>() {
			
			@Override
			protected Void doInBackground() throws Exception {
				while(rendering) {
					render();
				}
				return null;
			}
		};
		
		renderThread.execute();
	}
	
	public void render() {
		tree.render(currFrame.createGraphics());
		
		panel.getGraphics().drawImage(currFrame, 0, 0, panel);
	}

}
