package geogebra.awt;

import geogebra.common.awt.Rectangle;
import geogebra.common.awt.Rectangle2D;


public class GeneralPath extends geogebra.common.awt.GeneralPath implements Shape{
	
	private java.awt.geom.GeneralPath impl = new java.awt.geom.GeneralPath();
	public GeneralPath(java.awt.geom.GeneralPath g){
		impl = g;
	}
	public GeneralPath() {
		impl = new java.awt.geom.GeneralPath();
	}
	@Override
	public synchronized void moveTo(float f, float g) {
		impl.moveTo(f,g);
		
	}
	
	@Override
    public synchronized void reset() {
		impl.reset();
    }
    
	@Override
    public synchronized void lineTo(float x, float y) {
    	impl.lineTo(x, y);
    }
	
	@Override
    public synchronized void closePath() {
    	impl.closePath();
    }

	public boolean intersects(int i, int j, int k, int l) {
		return impl.intersects(i,j,k,l);
	}

	public boolean contains(int x, int y) {
		return impl.contains(x,y);
	}

	public Rectangle getBounds() {
		return new geogebra.awt.Rectangle(impl.getBounds());
	}

	public Rectangle2D getBounds2D() {
		return new geogebra.awt.Rectangle2D(impl.getBounds2D());
	}

	public boolean contains(Rectangle rectangle) {
		return impl.contains(geogebra.awt.Rectangle.getAWTRectangle(rectangle));
	}

	public boolean contains(double xTry, double yTry) {
		return impl.contains(xTry, yTry);
	}
	public java.awt.Shape getAwtShape() {
		return impl;
	}

}
