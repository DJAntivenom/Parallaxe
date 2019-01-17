package ch.elste.parallaxe;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

/**
 * The starter class for the parallax program.
 * 
 * @author Dillon
 * 
 * @version 0.5
 *
 */
public class ParallaxStarter implements Runnable {
	public static volatile boolean rendering = true;

	private static final ParallaxStarter instance = new ParallaxStarter();

	/**
	 * The speed of the camera in pixels per second.
	 */
	private static double speed = 600d;

	private final JFrame frame;
	private final JPanel panel;

	private Road road;
	private Tree[] tree;
	private BufferedImage currFrame;

	private long frameStartTime, frameTime, timeSinceFrameChange, timeAtFrameChange;
	private static final long FIXED_FRAME_TIME_NANOS = (long) (1d / 60d * Math.pow(10, 9));

	/**
	 * @since 0.1
	 */
	private ParallaxStarter() {
		frame = new JFrame("Parallax");
		panel = new JPanel();

		panel.setPreferredSize(new Dimension(1600, 800));
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

		currFrame = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
	}

	/**
	 * @since 0.1
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(instance);
	}

	@Override
	public void run() {
		tree = new Tree[150];
		for (int i = 0; i < tree.length; i++) {
			tree[i] = new Tree(Math.random() * panel.getWidth(), Math.random() * 10, Math.random() * 950 + 50);
		}
		sortTrees();

		road = new Road(50, 250, 10);

		SwingWorker<Void, Void> renderThread = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				while (rendering) {
					if (timeSinceFrameChange > FIXED_FRAME_TIME_NANOS)
						update(FIXED_FRAME_TIME_NANOS);

					frameStartTime = System.nanoTime();

					render(currFrame.createGraphics());

					frameTime = System.nanoTime() - frameStartTime;

					if (timeSinceFrameChange > FIXED_FRAME_TIME_NANOS) {
						panel.getGraphics().drawImage(currFrame, 0, 0, panel);
						timeSinceFrameChange = 0;
						timeAtFrameChange = System.nanoTime();
					}

					timeSinceFrameChange = System.nanoTime() - timeAtFrameChange;
				}
				return null;
			}
		};

		renderThread.execute();
	}

	/**
	 * Update the physics.
	 * 
	 * @param elapsedTime
	 *                    the time since last physics update in nanoseconds
	 * 
	 * @since 0.4
	 */
	public void update(double elapsedTime) {
		for (int i = 0; i < tree.length; i++) {
			tree[i].translateX(-speed * elapsedTime / Math.pow(10, 9));
		}
	}

	/**
	 * The render method, to render a frame and save it to {@code g2d}.
	 * 
	 * @param g2d
	 *            the {@link Graphics2D} object to draw to
	 * 
	 * @since 0.1
	 */
	public void render(Graphics2D g2d) {
		g2d.clearRect(0, 0, currFrame.getWidth(), currFrame.getHeight());
		g2d.setBackground(Color.BLACK);

		for (int i = 0; i < tree.length; i++) {
			tree[i].render(g2d);
		}

		road.render(g2d);

		g2d.drawString(String.format("FPS: %4.1f", getFPS()), 5, 20);
	}

	/**
	 * Returns the time having been needed to render the last frame.
	 * 
	 * @return the last frame time in nanoseconds
	 * 
	 * @since 0.2
	 */
	public long getFrameTimeNanos() {
		return frameTime;
	}

	/**
	 * Returns the time having been needed to render the last frame.
	 * 
	 * @return the last frame time in seconds
	 * 
	 * @since 0.2
	 * 
	 * @see #getFrameTimeNanos()
	 */
	public double getFrameTime() {
		return frameTime / Math.pow(10, 9);
	}

	/**
	 * Returns the frames per second.
	 * 
	 * @return the frames per second
	 * 
	 * @since 0.2
	 */
	public double getFPS() {
		return 1d / getFrameTime();
	}

	/**
	 * Returns the current frame as a {@link BufferedImage}.
	 * 
	 * @return the current frame
	 * 
	 * @since 0.2
	 */
	public BufferedImage getCurrFrame() {
		return currFrame;
	}

	/**
	 * Returns the width of a frame.
	 * 
	 * @return the width of a frame
	 * 
	 * @since 0.5
	 */
	public static int getWidth() {
		return instance.currFrame.getWidth();
	}
	
	/**
	 * Returns the height of a frame.
	 * 
	 * @return the height of a frame
	 * 
	 * @since 0.5
	 */
	public static int getHeight() {
		return instance.currFrame.getHeight();
	}

	/**
	 * Returns an instance of this class.
	 * 
	 * @return an instance of this class
	 * 
	 * @since 0.2
	 */
	public static ParallaxStarter instance() {
		return instance;
	}

	/**
	 * Sort the trees from biggest z to smallest.
	 * 
	 * @since 0.3
	 */
	private void sortTrees() {
		for (int i = 1; i < tree.length; i++) {
			for (int j = i; j > 0 && tree[j - 1].getZ() < tree[j].getZ(); j--) {
				swap(j, j - 1);
			}
		}
	}

	/**
	 * Swap to object in the tree array.
	 * 
	 * @param i
	 *          the first index
	 * @param j
	 *          the second index
	 * 
	 * @since 0.3
	 */
	private void swap(int i, int j) {
		Tree temp = tree[i];
		tree[i] = tree[j];
		tree[j] = temp;
	}
}
