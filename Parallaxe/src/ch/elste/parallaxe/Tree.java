package ch.elste.parallaxe;

import java.awt.Graphics2D;
import java.awt.Polygon;

public class Tree {
	private int x, y, z;
	private Polygon[] polys;

	public Tree(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		
		polys = new Polygon[4];
		polys[0] = new Polygon();
		polys[0].addPoint(getX(0), 200);
		polys[0].addPoint(getX(50), 200);
		polys[0].addPoint(getX(50), 150);
		polys[0].addPoint(getX(0), 150);
		for(int i = 1; i < polys.length; i++) {
			polys[i] = new Polygon();
			polys[i].addPoint(getX(0), 50*i);
			polys[i].addPoint(getX(50), 50*i);
			polys[i].addPoint(getX(25), 50*(i - 1));
		}
	}

	public void render(Graphics2D g2d) {
		for(Polygon p : polys) {
			g2d.fillPolygon(p);
		}
	}
	
	private int getX(int offset) {
		return x+offset;
	}
}
