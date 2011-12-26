package geogebra.common.awt;

import geogebra.common.awt.Rectangle;

public interface Shape {

	boolean intersects(int i, int j, int k, int l);

	boolean contains(int x, int y);

	Rectangle getBounds();

	Rectangle2D getBounds2D();

	boolean contains(geogebra.common.awt.Rectangle rectangle);
	
	boolean contains(double xTry, double yTry);

}
