package lagrange.impl.writers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import lagrange.Particle;
import lagrange.output.TrajectoryWriter;
import lagrange.utils.TimeConvert;

/**
 * Writes pertinent trajectory data to an output file in ASCII text format.
 * 
 * @author Johnathan Kool
 * 
 */

public class TrajectoryWriter_Dbase implements TrajectoryWriter {

	
	private String durationUnits = "Days";
	private String timeUnits = "Date";
	private String driver = "";
	private Connection conn;
	private PreparedStatement tps,sps;
	private boolean negCoord;

	/**
	 * Constructor that uses a String to generate the output file.
	 * 
	 * @param outputFile -
	 *            The path and name of the output file
	 */

	public TrajectoryWriter_Dbase(String connection) {

			try {
				Class.forName(driver);
				conn = DriverManager.getConnection(connection);
				Statement s = conn.createStatement();
				
				s.execute("CREATE TABLE IF NOT EXISTS trajectories (ID BIGINTEGER, DATETIMESTAMP TIME_, DOUBLE DEPTH, DOUBLE DURATION, FLOAT LON, FLOAT LAT, FLOAT DIST, VARCHAR(5) STATUS, BOOLEAN NODATA");
				s.execute("CREATE TABLE IF NOT EXISTS settlement (ID BIGINTEGER, DATETIMESTAMP TIME_, DOUBLE DEPTH, DOUBLE DURATION, FLOAT LON, FLOAT LAT, FLOAT DIST, VARCHAR(50) LOCATION");
				
				s.close();
				
				tps = conn.prepareStatement("INSERT INTO trajectories (ID, TIME_,");
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	/**
	 * Actually writes the data parameters to the output file.
	 * 
	 * @param p -
	 *            The particle whose information will be persisted.
	 */

	@Override
	public synchronized void apply(Particle p) {
		
		try {
			tps.setLong(0, p.getID());
			tps.setTimestamp(1, new Timestamp(p.getT()));
			tps.setDouble(2,TimeConvert.convertFromMillis(durationUnits, p.getAge()));
			tps.setDouble(3,p.getZ());
			double val;
			if (p.getX() > 180) {
				val = (-(360d - p.getX()));
			} else {
				val = (p.getX());
			}
			tps.setDouble(4,val);
			tps.setDouble(5,p.getY());
			tps.setDouble(6,p.getDistance());
			String status;
			if (p.canSettle() == true) {
				writeSPS(p);
				status = "S";
				
			} else if (p.isLost() == true) {
				status = "L";
			} else if (p.isDead() == true) {
				status = "M";
			} else {
				status = "I";
			}
			tps.setString(7, status);
			tps.setBoolean(8, p.wasNoData());
			tps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void flush(){}
	
	/**
	 * Closes and cleans up the output file
	 */
	
	@Override
	public void close() {

		try {
			tps.close();
			//sps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void writeSPS(Particle p) throws SQLException{
		sps.setLong(0, p.getID());
		sps.setTimestamp(1, new Timestamp(p.getT()));
		sps.setDouble(2,TimeConvert.convertFromMillis(durationUnits, p.getAge()));
		sps.setDouble(3,p.getZ());
		double val;
		if (p.getX() > 180) {
			val = (-(360d - p.getX()));
		} else {
			val = (p.getX());
		}
		sps.setDouble(4,val);
		sps.setDouble(5,p.getY());
		sps.setDouble(6,p.getDistance());
		sps.setString(7, p.getDestination());
		sps.executeUpdate();
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getDurationUnits() {
		return durationUnits;
	}

	@Override
	public void setDurationUnits(String durationUnits) {
		this.durationUnits = durationUnits;
	}

	public String getTimeUnits() {
		return timeUnits;
	}

	@Override
	public void setTimeUnits(String timeUnits) {
		this.timeUnits = timeUnits;
	}
	
	@Override
	public void setNegCoord(boolean negCoord){
		this.negCoord = negCoord;
	}

}