package au.gov.ga.conn4d.impl.writers;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import au.gov.ga.conn4d.Particle;
import au.gov.ga.conn4d.utils.TimeConvert;

import au.gov.ga.conn4d.output.TrajectoryWriter;


public class TrajectoryWriter_MSSQLServer implements TrajectoryWriter {

	private Connection conn;
	private Statement stmt;
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private String timeUnits = "Date";
	private String durationUnits = "Days";
	private boolean overwrite = true;
	private PreparedStatement ps1,ps2;
	private boolean negCoord;

	public TrajectoryWriter_MSSQLServer(String releaseName) {

		String sql;

		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager
					.getConnection("jdbc:sqlserver://servernameSQLEXPRESSinstance;database=yourdatabase;user=your username");

			if (overwrite) {
				if (tableExists(releaseName)) {
					stmt = conn.createStatement();
					stmt.executeUpdate("DROP TABLE " + releaseName);
					stmt.close();
					conn.commit();
				}
				stmt = conn.createStatement();
				sql = "CREATE TABLE " + releaseName + "(" +

				"IDX INTEGER NOT NULL AUTO_INCREMENT, "
						+ "SOURCE VARCHAR(25), " + "ID BIGINT NOT NULL, "
						+ "TIME_ TIMESTAMP, " + "DURATION, " + "DEPTH DOUBLE, "
						+ "LON DOUBLE, " + "LAT DOUBLE, " + "DISTANCE DOUBLE, "
						+ "STATUS VARCHAR(3), " + "DESTINATION VARCHAR(25)"
						+ "NODATA BOOLEAN)";

				stmt.close();
				conn.commit();
			}

			if (overwrite) {
				if (tableExists(releaseName + "_SET")) {
					stmt = conn.createStatement();
					stmt.executeUpdate("DROP TABLE " + releaseName + "_SET");
					stmt.close();
					conn.commit();
				}
				stmt = conn.createStatement();
				sql = "CREATE TABLE " + releaseName + "(" +

				"IDX INTEGER NOT NULL AUTO_INCREMENT, "
						+ "SOURCE VARCHAR(25), " + "ID BIGINT NOT NULL, "
						+ "TIME_ TIMESTAMP, " + "DURATION DOUBLE" + "DEPTH DOUBLE, "
						+ "LON DOUBLE, " + "LAT DOUBLE, " + "DISTANCE DOUBLE, "
						+ "DESTINATION VARCHAR(25)" + "NODATA BOOLEAN)";
				stmt.close();
				conn.commit();
			}

			//May need to add dbname.tablename
			
			sql = "INSERT INTO " + releaseName + "(" +
			
					"ID, " + 
					"TIME_, " +
					"DURATION, " +
					"DEPTH, " +
					"LON, " +
					"LAT, " +
					"DISTANCE, " +
					"STATUS, " +
					"DESTINATION, " +
					"NODATA) " +
					"VALUES(?,?,?,?,?,?,?,?,?,?)";
			
			ps1 = conn.prepareStatement(sql);
			
			sql = "INSERT INTO " + releaseName + "(" +
					
					"ID, " + 
					"TIME_, " +
					"DURATION, " +
					"DEPTH, " +
					"LON, " +
					"LAT, " +
					"DISTANCE, " +
					"DESTINATION, " +
					"NODATA) " +
					"VALUES(?,?,?,?,?,?,?,?,?)";
			
			ps2 = conn.prepareStatement(sql);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void apply(Particle p) {

		/*
		 * Write ID, Time (as an actual Date/Time stamp), Duration (Days),
		 * Depth, Longitude, Latitude, Distance /* traveled and Status (S =
		 * settled, L = Lost, M = Dead, I = in transit)
		 */

		try {
			if (p.recording()) {

				ps1.setLong(0, p.getID());
				ps1.setTimestamp(1, new Timestamp(p.getT()));
				ps1.setDouble(2, TimeConvert.convertFromMillis(durationUnits, p.getAge()));
				ps1.setDouble(3, p.getZ());
				
				if (p.getX() > 180) {
					ps1.setDouble(4, -(360d - p.getX()));
				} else {
					ps1.setDouble(4, p.getX());
				}
				
				ps1.setDouble(5, p.getY());
				ps1.setDouble(6, p.getDistance());
				
				if (p.canSettle() == true) {
					ps2.setLong(0, p.getID());
					ps2.setTimestamp(1, new Timestamp(p.getT()));
					ps2.setDouble(2, TimeConvert.convertFromMillis(durationUnits, p.getAge()));
					ps2.setDouble(3, p.getZ());
					
					if (p.getX() > 180) {
						ps2.setDouble(4, -(360d - p.getX()));
					} else {
						ps2.setDouble(4, p.getX());
					}
					
					ps2.setDouble(5, p.getY());
					ps2.setDouble(6, p.getDistance());
					ps2.setString(7, p.getDestination());
					ps1.setString(7, "S" + p.getDestination());
					ps2.setBoolean(8, p.getNodata());
					

				} else if (p.isLost() == true) {
					ps1.setString(7, "L");
					p.setRecording(false);
				} else if (p.isDead() == true) {
					ps1.setString(7, "M");
					p.setRecording(false);
				} else if (p.wasError() == true) {
					ps1.setString(7, "X");
					p.setRecording(false);
				} else {
					ps1.setString(7, "I");
				}

				ps1.setString(7, "I");	
				ps1.setBoolean(8, p.getNodata());

				ps1.execute();
				ps2.execute();

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		try {
			ps1.close();
			ps2.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private boolean tableExists(String name) throws SQLException {
		DatabaseMetaData md = conn.getMetaData();
		ResultSet rs = md.getTables(null, null, "%", null);
		while (rs.next()) {
			if (rs.getString(3).equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	public String getTimeUnits() {
		return timeUnits;
	}

	@Override
	public void setTimeUnits(String timeUnits) {
		this.timeUnits = timeUnits;
	}

	public String getDurationUnits() {
		return durationUnits;
	}

	@Override
	public void setDurationUnits(String durationUnits) {
		this.durationUnits = durationUnits;
	}
	@Override
	public void setNegCoord(boolean negCoord){
		this.negCoord = negCoord;
	}
}
