package au.gov.ga.conn4d.utils;

import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class TextDir2DB {

	private Connection conn;
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String conString = "jdbc:oracle:thin:@sun-db-dev:1521:oradev";
	private Statement stmt;
	private DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
	private String durationUnits = "Days";
	private String timeUnits = "Date";
	private boolean overwrite = true;
	private PreparedStatement ps1, ps2;
	private ArrayList<String> depthvals = new ArrayList<String>();

	// private boolean negCoord;

	public TextDir2DB() {

		depthvals.add("0-10"); // 1
		depthvals.add("10-20"); // 2
		depthvals.add("20-30"); // 3
		depthvals.add("30-50"); // 4
		depthvals.add("50-75"); // 5
		depthvals.add("75-100"); // 6
		depthvals.add("100-125"); // 7
		depthvals.add("125-150"); // 8
		depthvals.add("150-200"); // 9
		depthvals.add("200-250"); // 10
		depthvals.add("250-300"); // 11
		depthvals.add("300-400"); // 12
		depthvals.add("400-500"); // 13
		depthvals.add("500-600"); // 14
		depthvals.add("600-700"); // 15
		depthvals.add("700-800"); // 16
		depthvals.add("800-900"); // 17
		depthvals.add("900-1000"); // 18
		depthvals.add("1000-1100"); // 19
		depthvals.add("1100-1200"); // 20
		depthvals.add("1200-1300"); // 21
		depthvals.add("1400-1500"); // 22
		depthvals.add("1500-1750"); // 23
		depthvals.add("1750-2000"); // 24
		depthvals.add("2000-2500"); // 25
		depthvals.add("2500-3000"); // 26
		depthvals.add("3000-3500"); // 27
		depthvals.add("3500-4000"); // 28
		depthvals.add("4000-4500"); // 29
		depthvals.add("4500-5000"); // 30
		depthvals.add("5000-5500"); // 31
	}

	public void makeTable(String tableName) {

		String username = null;
		char[] password = null;
		
		Console console = System.console();

		if (console!=null) {
			username = console.readLine("Enter USERNAME: ");
			password = console.readPassword("Enter PASSWORD: ");
		} else {
			// Using a JPanel as the message for the JOptionPane
			JPanel userPanel = new JPanel();
			userPanel.setLayout(new GridLayout(2, 2));

			// Labels for the textfield components
			JLabel usernameLbl = new JLabel("Username:");
			JLabel passwordLbl = new JLabel("Password:");

			JTextField usernameJ = new JTextField();
			JPasswordField passwordFld = new JPasswordField();

			// Add the components to the JPanel
			userPanel.add(usernameLbl);
			userPanel.add(usernameJ);
			userPanel.add(passwordLbl);
			userPanel.add(passwordFld);

			// As the JOptionPane accepts an object as the message
			// it allows us to use any component we like - in this case
			// a JPanel containing the dialog components we want
			
			JOptionPane.showConfirmDialog(null, userPanel,
					"Login to Oracle Database:", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.PLAIN_MESSAGE);

			username = usernameJ.getText();
			password = passwordFld.getPassword();
		}
		String sql;

		try {
			Class.forName(driver);

			// conn = DriverManager.getConnection(conString, "u83869",
			// "<password here>");

			conn = DriverManager.getConnection(conString, username,
					String.valueOf(password));

			Arrays.fill(password, ' ');

			if (overwrite) {
				if (tableExists(tableName)) {
					stmt = conn.createStatement();
					stmt.executeUpdate("DROP TABLE " + tableName);
					stmt.close();
					conn.commit();
				}

				stmt = conn.createStatement();
				sql = "CREATE TABLE " + tableName + "(" +

				"SOURCE VARCHAR(25), " + "RELDEPTH NUMBER, "
						+ "ID NUMBER(19) NOT NULL, " + "TIME_ TIMESTAMP, "
						+ "DURATION NUMBER, " + "DEPTH NUMBER, "
						+ "LON NUMBER, " + "LAT NUMBER, " + "DISTANCE NUMBER, "
						+ "STATUS VARCHAR(3), " + "DESTINATION VARCHAR(25), "
						+ "NODATA NUMBER(1), " + "RELEASE VARCHAR(25))";

				stmt.execute(sql);
				stmt.close();
				conn.commit();
				System.out.println("Trajectory table setup complete.");
			}

			if (overwrite) {
				if (tableExists(tableName + "_SET")) {
					stmt = conn.createStatement();
					stmt.executeUpdate("DROP TABLE " + tableName + "_SET");
					stmt.close();
					conn.commit();
				}
				stmt = conn.createStatement();
				sql = "CREATE TABLE " + tableName + "(" +

				"SOURCE VARCHAR(25), " + "ID NUMBER(19) NOT NULL, "
						+ "TIME_ TIMESTAMP, " + "DURATION NUMBER, "
						+ "DEPTH NUMBER, " + "LON NUMBER, " + "LAT NUMBER, "
						+ "DISTANCE NUMBER, " + "STATUS VARCHAR(3), "
						+ "DESTINATION VARCHAR(25), " + "NODATA NUMBER(1))";

				stmt.close();
				conn.commit();
				System.out.println("Settlement table setup complete.");
			}

			// May need to add dbname.tablename

			sql = "INSERT INTO " + tableName + "(" +

			"SOURCE, " + "RELDEPTH, " + "ID, " + "TIME_, " + "DURATION, "
					+ "DEPTH, " + "LON, " + "LAT, " + "DISTANCE, " + "STATUS, "
					+ "DESTINATION, " + "NODATA, " + "RELEASE) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

			ps1 = conn.prepareStatement(sql);

			sql = "INSERT INTO " + tableName + "(" +

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

	public void writeTRJTree(String root) {
		File dir = new File(root);
		if (!dir.isDirectory()) {
			System.out.println(root + " is not a directory.  Skipping.");
			return;
		}
		File[] files = dir.listFiles();

		for (File f : files) {
			String fname = f.getName();
			String rng = fname.substring(fname.indexOf("_D") + 2);
			int val = depthvals.indexOf(rng) + 1;
			writeTRJDir(f.getPath(), val);
		}
	}

	public void writeTRJDir(String dirname, int depth) {
		File dir = new File(dirname);
		if (!dir.isDirectory()) {
			System.out.println(dirname + " is not a directory.  Skipping.");
			return;
		}
		File[] files = dir.listFiles();

		for (File f : files) {
			for (File f2 : f.listFiles(new FileExtensionFilter(".txt"))) {
				writeTRJ(f2.getPath(), 0, f.getName());
			}
			System.out.println("    Release " + f.getName() + " complete.");
		}
		System.out.println(">>> Directory " + dir + " complete. >>>");
	}

	public void writeTRJ(String filename, int reldepth, String release) {

		File file = new File(filename);
		BufferedReader br = null;
		String ln = null;

		try {
			br = new BufferedReader(new FileReader(file));
			br.readLine();
			ln = br.readLine();
			while (ln != null) {
				StringTokenizer stk = new StringTokenizer(ln);
				if (stk.countTokens() != 10) {
					System.out
							.println("WARNING: Number of fields must equal 10 (contains "
									+ stk.countTokens()
									+ ").  Skipping "
									+ filename + "...");
					ps1.clearBatch();
					return;
				}
				ps1.setString(
						1,
						file.getName().substring(0,
								file.getName().lastIndexOf("."))); // Source
				ps1.setInt(2, reldepth);
				ps1.setInt(3, Integer.parseInt(stk.nextToken())); // Index
				Timestamp ts = null;
				try {
					String s = stk.nextToken() + " " + stk.nextToken();
					if (s.contains(":")) {
						ts = new Timestamp(df1.parse(s).getTime());
					} else {
						ts = new Timestamp(df2.parse(s).getTime());
					}
				} catch (ParseException e) {
					System.out
							.println("WARNING: Date could not be parsed from "
									+ ln + ".  Continuing");
					ln = br.readLine();
					continue;
				}
				ps1.setTimestamp(4, ts); // Time
				ps1.setDouble(5, Double.parseDouble(stk.nextToken())); // Duration
				ps1.setDouble(6, Double.parseDouble(stk.nextToken())); // Depth
				ps1.setDouble(7, Double.parseDouble(stk.nextToken())); // Longitude
				ps1.setDouble(8, Double.parseDouble(stk.nextToken())); // Latitude
				ps1.setDouble(9, Double.parseDouble(stk.nextToken())); // Distance
				String status = stk.nextToken();
				if (!status.startsWith("\"S")) {
					ps1.setString(10, status); // Status
					ps1.setString(11, "");
				} else {
					ps1.setString(10, "S");
					ps1.setString(11, status.substring(2, status.length() - 1));
				}
				ps1.setBoolean(12, Boolean.parseBoolean(stk.nextToken())); // NoData
				ps1.setString(13, release);
				// ps1.execute();
				ln = br.readLine();
				ps1.addBatch();
			}
			ps1.executeBatch();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException sq) {
			sq.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
		}
		System.out.println(filename + " processing complete");
	}

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

	public void setTimeUnits(String timeUnits) {
		this.timeUnits = timeUnits;
	}

	public String getDurationUnits() {
		return durationUnits;
	}

	public void setDurationUnits(String durationUnits) {
		this.durationUnits = durationUnits;
	}

	// public void setNegCoord(boolean negCoord) {
	// this.negCoord = negCoord;
	// }

	public static void main(String[] args) {
		System.out.println("Starting...");
		TextDir2DB td = new TextDir2DB();
		td.makeTable("T3_D5");
		// writeTRJTree("E:/Temp/Output_test");
		td.writeTRJDir("E:/Temp/Output_test/Pilot_D50-75/2009-03-02", 5);
		System.out.println("Complete");
	}
}
