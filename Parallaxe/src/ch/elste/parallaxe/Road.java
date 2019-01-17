package ch.elste.parallaxe;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * 
 * @author Dillon Elste
 * 
 * @version 1.0
 *
 */
public class Road {
	private double x, y, z;
	private double height;
	private int stripeNumber;
	private Color temp;

	public Road(double z, double height, int stripeNumber) {
		this.x = 0;
		this.y = ParallaxStarter.getHeight() - getYFactor(z);
		this.z = z;
		this.height = height;
		this.stripeNumber = stripeNumber;
	}

	public void render(Graphics2D g2d) {
		temp = g2d.getColor();
		g2d.setColor(Color.GRAY);
		g2d.fillRect((int) Math.round(x), (int) Math.round(y), ParallaxStarter.getWidth(),
				(int) Math.round(height * getHeightFactor(z)));
		g2d.setColor(temp);
	}

	/**
	 * Calculates the factor for the height.
	 * 
	 * @param x
	 *          the value for the function
	 * 
	 * @return
	 *         <li>if x = 0: 1</li>
	 *         <li>if x = 1000: 0</li>
	 * 
	 * @since 1.0
	 */
	private double getHeightFactor(double x) {
		return -0.001 * x + 1;
	}

	/**
	 * Calculates the factor for y.
	 * 
	 * @param x
	 *          the value for the function
	 * 
	 * @return
	 *         <li>if x = 0, 0</li>
	 *         <li>if x = 1000, frameHeight / 2</li>
	 * 
	 * @since 1.0
	 */
	private double getYFactor(double x) {
		return ParallaxStarter.getHeight() / 2000d * x;
	}
}
