package au.gov.ga.conn4d.impl.writers;

import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import au.gov.ga.conn4d.Particle;
import au.gov.ga.conn4d.utils.TimeConvert;

import au.gov.ga.conn4d.output.TrajectoryWriter;


public class TrajectoryWriter_Oracle implements TrajectoryWriter {

	private Connection conn;
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String conString = "jdbc:oracle:thin:@sun-db-dev:1521:oradev";
	private Statement stmt;
	private String durationUnits = "Days";
	private String timeUnits = "Date";
	private boolean overwrite = true;
	private PreparedStatement ps1, ps2;
	@SuppressWarnings("unused")
	private boolean negCoord;

	public TrajectoryWriter_Oracle(String releaseName) {

		// Using a JPanel as the message for the JOptionPane
		JPanel userPanel = new JPanel();
		userPanel.setLayout(new GridLayout(2, 2));

		// Labels for the textfield components
		JLabel usernameLbl = new JLabel("Username:");
		JLabel passwordLbl = new JLabel("Password:");

		JTextField username = new JTextField();
		JPasswordField passwordFld = new JPasswordField();

		// Add the components to the JPanel
		userPanel.add(usernameLbl);
		userPanel.add(username);
		userPanel.add(passwordLbl);
		userPanel.add(passwordFld);

		// As the JOptionPane accepts an object as the message
		// it allows us to use any component we like - in this case
		// a JPanel containing the dialog components we want
		JOptionPane.showConfirmDialog(null, userPanel, "Login to Oracle Database:",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		
		String sql;

		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(conString, username.getText(),
					String.valueOf(passwordFld.getPassword()));

			if (overwrite) {
				if (tableExists(releaseName)) {
					stmt = conn.createStatement();
					stmt.executeUpdate("DROP TABLE " + releaseName);
					stmt.close();
					conn.commit();
				}

				stmt = conn.createStatement();
				sql = "CREATE TABLE " + releaseName + "(" +

				"SOURCE VARCHAR(25), " + "ID NUMBER(19) NOT NULL, "
						+ "TIME_ TIMESTAMP, " + "DURATION NUMBER, "
						+ "DEPTH NUMBER, " + "LON NUMBER, " + "LAT NUMBER, "
						+ "DISTANCE NUMBER, " + "STATUS VARCHAR(3), "
						+ "DESTINATION VARCHAR(25), " + "NODATA NUMBER(1))";

				stmt.execute(sql);
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

				"SOURCE VARCHAR(25), " + "ID NUMBER(19) NOT NULL, "
						+ "TIME_ TIMESTAMP, " + "DURATION NUMBER, "
						+ "DEPTH NUMBER, " + "LON NUMBER, " + "LAT NUMBER, "
						+ "DISTANCE NUMBER, " + "STATUS VARCHAR(3), "
						+ "DESTINATION VARCHAR(25), " + "NODATA NUMBER(1))";

				stmt.close();
				conn.commit();
			}

			// May need to add dbname.tablename

			sql = "INSERT INTO " + releaseName + "(" +

			"ID, " + "TIME_, " + "DURATION, " + "DEPTH, " + "LON, " + "LAT, "
					+ "DISTANCE, " + "STATUS, " + "DESTINATION, " + "NODATA) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?)";

			ps1 = conn.prepareStatement(sql);

			sql = "INSERT INTO " + releaseName + "(" +

			"ID, " + "TIME_, " + "DURATION, " + "DEPTH, " + "LON, " + "LAT, "
					+ "DISTANCE, " + "DESTINATION, " + "NODATA) "
					+ "VALUES(?,?,?,?,?,?,?,?,?)";

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
				ps1.setDouble(2, TimeConvert.convertFromMillis(durationUnits,
						p.getAge()));
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
					ps2.setDouble(
							2,
							TimeConvert.convertFromMillis(durationUnits,
									p.getAge()));
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
					ps2.setInt(8, p.getNodata() ? 1 : 0);

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
				ps1.setInt(8, p.getNodata() ? 1 : 0);

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
	
	//@Override
	@Override
	public void setNegCoord(boolean negCoord){
		this.negCoord = negCoord;
	}
}
