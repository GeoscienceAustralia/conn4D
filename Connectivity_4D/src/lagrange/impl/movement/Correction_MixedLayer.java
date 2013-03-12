package lagrange.impl.movement;


import java.io.IOException;

import lagrange.impl.readers.Reader_NetCDF_3D;
import lagrange.utils.TimeConvert;
import lagrange.utils.Triangular;
import lagrange.Particle;

public class Correction_MixedLayer{
	
	private Reader_NetCDF_3D n3;
	
	public Correction_MixedLayer(String mld){
		try {
			n3 = new Reader_NetCDF_3D(mld,"mld","Time","Latitude","Longitude");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	// Check what we want the maximum bandwidth of travel to be.
	// scale using the mld.
	
	public void apply(Particle p){
		double a = 0;
		double b = n3.getValue(TimeConvert.millisToHYCOM(p.getT()), p.getX(), p.getY());
		double c = p.getZ();
		c=c>0?0:c;
		Triangular tri = new Triangular(a,b,c);
		p.setZ(tri.nextDouble());
	}	
}
