package au.gov.ga.conn4d.test.impl.movement;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;
import au.gov.ga.conn4d.Particle;

import au.gov.ga.conn4d.impl.movement.Diffusion_Simple_3D;

public class Diffusion_Simple_3DTest {

	Diffusion_Simple_3D ds3 = new Diffusion_Simple_3D();
	int n = 10000;
	
	@Test
	public void test() {
		//double[] x = new double[n];
		//double[] y = new double[n];
		//double[] z = new double[n];
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("C:/Temp/disp.csv"));
			
			for (int i = 0; i < n; i++){
				Particle p = new Particle();
				
				for(int j = 0; j < 12; j++){
					ds3.apply(p);
				}
				
				//x[i] = p.getX();
				//y[i] = p.getY();
				//z[i] = p.getZ();
				//bw.write(x[i] + "," + y[i] + "," + z[i] + "\n");
				bw.write(p.getX() + "," + p.getY() + "," + p.getZ() + "\n");
			}
			
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public static void main(String[] args){
		Diffusion_Simple_3DTest d3t = new Diffusion_Simple_3DTest();
		d3t.test();
		System.out.println("Complete");
	}
}
