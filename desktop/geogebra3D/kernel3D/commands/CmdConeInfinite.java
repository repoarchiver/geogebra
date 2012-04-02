package geogebra3D.kernel3D.commands;

import geogebra.common.kernel.Kernel;
import geogebra.common.kernel.arithmetic.Command;
import geogebra.common.kernel.arithmetic.NumberValue;
import geogebra.common.kernel.geos.GeoElement;
import geogebra.common.kernel.kernelND.GeoPointND;
import geogebra.common.main.MyError;

public class CmdConeInfinite extends CmdCone {
	
	
	
	
	public CmdConeInfinite(Kernel kernel) {
		super(kernel);
	}

	
	protected GeoElement[] conePointPointRadius(Command c, GeoPointND p1, GeoPointND p2, NumberValue r){
		return new GeoElement[] {kernelA.getManager3D().Cone(
				c.getLabel(),p1,p2,r)};
	}
	
	protected MyError argErr(GeoElement geo){
		return argErr(app,"ConeInfinite",geo);
	}
	
	protected MyError argNumErr(int n){
		return argNumErr(app,"ConeInfinite",n);
	}
	
}
