package lagrange.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;
import com.vividsolutions.jts.geom.LineString;

public class Trace {

	BufferedWriter br;
	long id = 0;

	public Trace(String s) {
		try {
			br = new BufferedWriter(new FileWriter(s));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void write(String s) {
		try {
			br.write(s);
			br.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void write(LineString ls){
		Coordinate p0 = ls.getCoordinateN(0);
		Coordinate p1 = ls.getCoordinateN(1);
		try {
			br.write(id+"\n");
			br.write(p0.x+"\t"+p0.y+"\t"+p0.z+"\n");
			br.write(p1.x+"\t"+p1.y+"\t"+p1.z+"\n");
			br.write("END"+"\n");
			br.flush();
			id++;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void write(LineSegment ls){
		Coordinate p0 = ls.p0;
		Coordinate p1 = ls.p1;
		try {
			br.write(id+"\n");
			br.write(p0.x+"\t"+p0.y+"\t"+p0.z+"\n");
			br.write(p1.x+"\t"+p1.y+"\t"+p1.z+"\n");
			br.write("END"+"\n");
			br.flush();
			id++;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close() {
		if (br != null) {
			try {
				br.flush();
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
