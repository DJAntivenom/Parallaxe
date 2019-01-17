package ch.elste.parallaxe;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

/**
 * A tree with x, y and z coordinates.
 * 
 * @author Dillon
 *
 * @version 0.3
 */
public class Tree {
	public static final double LEAVE_WIDTH = 800;
	public static final double LEAVE_HEIGHT = 200;
	public static final double TRUNK_WIDTH = 240;
	public static final double TRUNK_HEIGHT = 200;

	private double x, y, z;
	private Leaves[] leaves;
	private Rectangle trunk;

	/**
	 * Creates a new tree object with given values.
	 * 
	 * @param x
	 *            the x-coordinate
	 * @param z
	 *            the y-coordinate between 0 and 1000
	 * 
	 * @throws IllegalArgumentException
	 * 
	 * @since 0.1
	 */
	public Tree(double x, double y, double z) {
		if (z < 0 || z > 1000)
			throw new IllegalArgumentException("z must be between 0 and 1000");

		this.x = x;
		this.z = z;
		this.y = ParallaxStarter.instance().getCurrFrame().getHeight() / 2d - y;

		leaves = new Leaves[3];

		trunk = new Rectangle((int) Math.round(getX((getLeaveWidth() - getTrunkWidth()) / 2)),
				(int) Math.round(getY(leaves.length * getLeaveHeight())), (int) Math.round(getTrunkWidth()),
				(int) Math.round(getTrunkHeight()));

		leaves[0] = new Leaves(getX(), getY(), getLeaveWidth(), getLeaveHeight(), .5);
		for (int i = 1; i < leaves.length; i++) {
			leaves[i] = new Leaves(getX(), getY(getLeaveHeight() * i), getLeaveWidth(), getLeaveHeight(), .2);
			leaves[i].setPos(getX(), getY(getLeaveHeight() * i));
		}
	}

	/**
	 * Render this tree.
	 * 
	 * @param g2d
	 *            the {@link Graphics2D} object to draw to
	 * 
	 * @since 0.1
	 */
	public void render(Graphics2D g2d) {
		g2d.setColor(new Color(112, 43, 26));
		g2d.fill(trunk);

		for (Leaves l : leaves) {
			l.render(g2d);
		}

		g2d.setColor(Color.WHITE);
	}

	/**
	 * @since 0.2
	 */
	private void updatePolys() {
		if (x < -getLeaveWidth())
			x = ParallaxStarter.instance().getCurrFrame().getWidth();

		trunk = new Rectangle((int) Math.round(getX((getLeaveWidth() - getTrunkWidth()) / 2)),
				(int) Math.round(getY(leaves.length * getLeaveHeight())), (int) Math.round(getTrunkWidth()),
				(int) Math.round(getTrunkHeight()));

		for (int i = 0; i < leaves.length; i++) {
			leaves[i].setPos(getX(), getY(getLeaveHeight() * i));
		}
	}

	/**
	 * Returns the {@link #x x-coordinate}.
	 * 
	 * @return the x-coordinate
	 * 
	 * @since 0.2
	 */
	public double getX() {
		return x;
	}

	/**
	 * Returns the {@link #x x-coordinate} plus the {@code offset}.
	 * 
	 * @param offset
	 *            the offset to be added to x
	 * 
	 * @return the new coordinate
	 * 
	 * @since 0.1
	 */
	private double getX(double offset) {
		return x + offset;
	}

	/**
	 * Sets the {@link #x x-coordinate}.
	 * 
	 * @param x
	 *            the coordinate
	 * 
	 * @since 0.2
	 */
	public void setX(double x) {
		this.x = x;
		updatePolys();
	}

	/**
	 * Translates this tree by {@code x}.
	 * 
	 * @param x
	 *            the value to translate by
	 * 
	 * @since 0.3
	 */
	public void translateX(double x) {
		this.x += x / getSpeedFactor(z);
		updatePolys();
	}

	/**
	 * Returns the {@link #y y-coordinate of this tree}.
	 * 
	 * @return the y-coordinate
	 * 
	 * @since 0.2
	 */
	public double getY() {
		return y;
	}

	/**
	 * Returns the {@link #y y-coordinate} plus the {@code offset}.
	 * 
	 * @param offset
	 *            the offset to be added to y
	 * 
	 * @return the new coordinate
	 * 
	 * @since 0.1
	 */
	private double getY(double offset) {
		return y + offset;
	}

	/**
	 * Sets the {@link #y y-coordinate}.
	 * 
	 * @param y
	 *            the coordinate
	 * 
	 * @since 0.2
	 */
	public void setY(double y) {
		this.y = y;
		updatePolys();
	}

	/**
	 * Translates this tree by {@code y}.
	 * 
	 * @param y
	 *            the value to translate by
	 * 
	 * @since 0.3
	 */
	public void translateY(double y) {
		this.y += y / getSpeedFactor(z);
		updatePolys();
	}

	/**
	 * Returns the z-coordinate.
	 * 
	 * @return the z-coordinate
	 * 
	 * @since 0.3
	 */
	public double getZ() {
		return z;
	}

	/**
	 * Returns the height of this tree object.
	 * 
	 * @return the height of this tree object
	 * 
	 * @since 0.3
	 */
	public double getTreeHeight() {
		return getLeaveHeight() * 3 + getTrunkHeight();
	}

	/**
	 * Returns the leave height with respect to {@code z}.
	 * 
	 * @return the leave height with respect to {@code z}.
	 * 
	 * @since 0.3
	 */
	private double getLeaveHeight() {
		return LEAVE_HEIGHT / getHeightFactor(z);
	}

	/**
	 * Returns the trunk height with respect to {@code z}.
	 * 
	 * @return the trunk height with respect to {@code z}.
	 * 
	 * @since 0.3
	 */
	private double getTrunkHeight() {
		return TRUNK_HEIGHT / getHeightFactor(z);
	}

	/**
	 * Returns the leave width with respect to {@code z}.
	 * 
	 * @return the leave width with respect to {@code z}.
	 * 
	 * @since 0.3
	 */
	private double getLeaveWidth() {
		return LEAVE_WIDTH / getHeightFactor(z);
	}

	/**
	 * Returns the trunk width with respect to {@code z}.
	 * 
	 * @return the trunk width with respect to {@code z}.
	 * 
	 * @since 0.3
	 */
	private double getTrunkWidth() {
		return TRUNK_WIDTH / getHeightFactor(z);
	}

	/**
	 * Return the factor for the calculation of height and width.
	 * 
	 * @param x
	 *            the x-value to calculate the width with.
	 * 
	 * @return
	 *         <li>{@code 1} for {@code x = 0}</li>
	 *         <li>{@code 100} for {@code x = 1000}</li>
	 * 
	 * @since 0.3
	 */
	private double getHeightFactor(double x) {
		return 0.099 * x + 1d;
	}

	/**
	 * Return the factor for the speed calculation
	 * 
	 * @param x
	 *            the x-value
	 * 
	 * @return
	 *         <li>{@code 1} for {@code x = 0}</li>
	 *         <li>{@code 100} for {@code x = 1000}</li>
	 * 
	 * @since 0.3
	 */
	private double getSpeedFactor(double x) {
		return 0.01 * x + 1d;
	}

	/**
	 * A class to hold information about the leaves of the tree.
	 * 
	 * @author Dillon
	 * 
	 * @since 0.3
	 * @version 1.1
	 *
	 */
	private static class Leaves {
		private Polygon p;
		private Color color, temp;
		private Point2D.Double pos, size;
		private double topWidthPercent;

		/**
		 * Cretes new leaves with given values.
		 * 
		 * @param x
		 *            x-coordinate
		 * @param y
		 *            y-coordinate
		 * @param width
		 *            the bottom width of the leaves
		 * @param height
		 *            the height of the leaves
		 * @param topWidthPercent
		 *            the top width in percent of {@code width} between 0 and 1
		 * 
		 * @since 1.0
		 */
		public Leaves(double x, double y, double width, double height, double topWdithPercent) {
			this(Color.GREEN, x, y, width, height, topWdithPercent);
		}

		/**
		 * Cretes new leaves with given values.
		 * 
		 * @param color
		 *            the color of the leaves
		 * @param x
		 *            x-coordinate
		 * @param y
		 *            y-coordinate
		 * @param width
		 *            the bottom width of the leaves
		 * @param height
		 *            the height of the leaves
		 * @param topWidthPercent
		 *            the top width in percent of {@code width} between 0 and 1
		 * 
		 * @since 1.0
		 */
		public Leaves(Color color, double x, double y, double width, double height, double topWidthPercent) {
			p = new Polygon();

			this.color = color;

			size = new Point2D.Double(width, height);

			pos = new Point2D.Double(x, y);

			this.topWidthPercent = topWidthPercent;

			updatePoly();
		}

		/**
		 * Draws these leaves to {@code g2d}.
		 * 
		 * @param g2d
		 *            the {@link Graphics2D} object to draw to
		 * 
		 * @since 1.0
		 */
		public void render(Graphics2D g2d) {
			temp = g2d.getColor();
			g2d.setColor(color);

			g2d.fillPolygon(p);

			g2d.setColor(temp);
		}

		/**
		 * Updates the polygon.
		 * 
		 * @since 1.0
		 */
		public void updatePoly() {
			p.reset();
			p.addPoint((int) Math.round(pos.x), (int) Math.round(pos.y + size.y));
			p.addPoint((int) Math.round(pos.x + size.x), (int) Math.round(pos.y + size.y));
			p.addPoint((int) Math.round(pos.x + size.x * (1 - topWidthPercent)), (int) Math.round(pos.y));
			p.addPoint((int) Math.round(pos.x + size.x * (0 + topWidthPercent)), (int) Math.round(pos.y));
		}

		/**
		 * Sets the x and y coordinates of this leaves object.
		 * 
		 * @param x
		 *            the x-coordinate
		 * @param y
		 *            the y-coordinate
		 * 
		 * @since 1.1
		 */
		public void setPos(double x, double y) {
			pos.setLocation(x, y);
			updatePoly();
		}
	}
}
