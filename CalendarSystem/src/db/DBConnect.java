package db;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Stack;

public class DBConnect {
	//This class establishes a connection to the database, enabling the server to execute queries
	
	private Connection connection;
	private Statement statement;
	//private PreparedStatement pstatement;
	private ResultSet results;
	private String dbURL;
	private String username;
	private String password;
	
	public DBConnect() {
		//Connects to the db
		try {
			Class.forName("com.mysql.jdbc.Driver");
			this.dbURL = "jdbc:mysql://mysql.stud.ntnu.no:3306/mariusmb_kalenderDB";
			this.username = "mariusmb";
			this.password = "Qe7Uw6AE";
			connection = DriverManager.getConnection(this.dbURL, this.username, this.password);
			System.out.println("Success!");
		} catch (Exception e) {
			System.out.println("Connection failed:");
			e.printStackTrace();
		}
	}
	
	private void printEmployees() {
		//Prints all data in the db table ansatt.
		//Method added mainly for testing purposes.
		try {
			String query = "select * from ansatt";
			statement = connection.createStatement();
			results = statement.executeQuery(query);
			while (results.next()) {
				int empNo = results.getInt(1);
				String fName = results.getString(2);
				String lName = results.getString(3);
				String email = results.getString(4);
				String pword = results.getString(5);
				System.out.println("Ansattnummer: " + empNo + " Name: " + fName + " " + lName + " Username: " + email + " Password: " + pword);
				System.out.println();
			}
		} catch (Exception e) {
			System.out.println("Something went wrong, I have no idea what it could be but perhaps this is helpful:");
			e.printStackTrace();
		}
		
	}
	
	private void printData(String table) {
		//Prints all data from any table.
		//Method added mainly for testing purposes.
		try {
			String query = "select * from " + table;
			statement = connection.createStatement();
			results = statement.executeQuery(query);
			while (results.next()) {
				int i = 1;
				while (true) {
					try {
						System.out.print(results.getObject(i) + " ");
					} catch (SQLException e) {
						break;
					}
					i++;
				}
				System.out.println();
				System.out.println();
			}
		} catch (Exception e) {
			System.out.println("Something went wrong, I have no idea what it could be but perhaps this is helpful:");
			e.printStackTrace();
		}
		
	}
	
	public void close() {
		//Closes all resources.
		try {
			if (connection != null) {
				connection.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (results != null) {
				results.close();
			}
		} catch (SQLException e) {
			System.out.println("One or more resources could not be closed:");
			System.out.println(e);
		}
	}
	
	/**
	 * Retrieves a user's password from the DB.
	 * @param user Username on the format "something@firmax.no"
	 * @return Password of the user, null if user does not exist.
	 */
	private String getPassword(String user) {
		String query = "SELECT * FROM ansatt WHERE brukernavn = '" + user + "'";
		String result = null;
		try {
			statement = connection.createStatement();
			results = statement.executeQuery(query);
			results.next(); //Gets the next and only result of this query
			result = results.getString("passord");
		} catch (SQLException e) {
			System.out.println("Unable to obtain password.");
			//e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Adds new appointment to the DB.
	 * 
	 * @param description If null the default is "Ny avtale".
	 * @param startTime Must be formatted string. Accepted format is "CCYY-MM-DD HH:MM:SS".
	 * @param endTime Must be formatted string. Accepted format is "CCYY-MM-DD HH:MM:SS".
	 * @param location Can be null.
	 * @param meetingRoom Can be null.
	 * @param owner ansattNR of whomever created the appointment.
	 * @return True if addition was successful, false otherwise.
	 */
	private boolean addNewAppointment(String description, String startTime, String endTime, String location, Integer meetingRoom, int owner) {
		String query = "INSERT INTO avtale (formål, starttid, sluttid, ";
		if (location != null) {
			query += "sted, ";
		}
		if (meetingRoom != null) {
			query += "møteromNR, ";
		}
		query += "ansvarlig) VALUES (\"";
		if (description != null) {
			query += description + "\", \"";
		}else {
			query += "Ny avtale\", \"";
		}
		query += startTime + "\", \"" + endTime + "\", ";
		if (location != null) {
			query += "\"" + location + "\", ";
		}
		if (meetingRoom != null) {
			query += "\"" + meetingRoom + "\", ";
		}
		query += owner + ");";
		System.out.println(query);
		try {
			statement = connection.createStatement();
			statement.executeUpdate(query);
			//Appointment should be in the DB by now. The final step is adding the owner of the appointment to the appointment.
			return addEmployeeToAppointment(getLatestAddition("avtale", "avtaleID"), owner);
		}catch (SQLException e) {
			System.out.println("Unable to create appointment.");
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean addEmployeeToAppointment(int appointmenID, int empNo) {
		String query = "INSERT INTO avtaleAnsatt (avtaleID, ansattNR) VALUES (" + appointmenID + ", " + empNo + ")";
		try {
			statement = connection.createStatement();
			statement.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
//	private boolean isExistingAppointment1(int appointmentID) {
//		//Deprecated, use isExistingAppointment
//		//Returns true if record of the appointment exists in the DB.
//		boolean exists = false;
//		String query = "SELECT COUNT(*) FROM avtale WHERE avtaleID = " + appointmentID;
//		try {
//			statement = connection.createStatement();
//			results = statement.executeQuery(query); //Results is a 1x1 table containing the number of appointments corresponding to the appointmentID. Should be 0 or 1.
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		try {
//			results.next();
//			exists = results.getInt(1) == 1;
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return exists;
//	}
	
	/**
	 * 
	 * @param appointmentID
	 * @return True if record of the appointment exists in the DB, false otherwise.
	 */
	private boolean isExistingAppointment(int appointmentID) {
		String query = "SELECT * FROM avtale WHERE avtaleID = " + appointmentID;
		try {
			statement = connection.createStatement();
			results = statement.executeQuery(query);
			return results.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Removes appointment from the DB, if it exists.
	 * CAUTION: Irreversible deletion
	 * @param appointmentID
	 * @return Returns true if the appointment was successfully removed, false otherwise.
	 */
	private boolean removeAppointment(int appointmentID) {
		//
		if (!isExistingAppointment(appointmentID)) {
			System.out.println("There is no record of an appointment with the ID " + appointmentID + " in the database.");
			return false;
		}
		String query = "DELETE FROM avtale WHERE avtaleID = " + appointmentID;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Finds the latest addition to a table.
	 * @param table Name of table.
	 * @param primaryKey Name of primary key, should have auto_increment.
	 * @return Returns the ID of the latest addition to any table in the DB with auto_increment.
	 */
	private int getLatestAddition(String table, String primaryKey) {	
		String query = "SELECT * FROM " + table + " WHERE " + primaryKey + " = (SELECT MAX(" + primaryKey + ") FROM " + table + ")";
		System.out.println(query);
		try {
			statement = connection.createStatement();
			results = statement.executeQuery(query);
			results.next();
			return results.getInt(primaryKey);
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * Removes the latest addition to a table.
	 * @param table Name of table.
	 * @param primaryKey Name of primary key, should have auto_increment.
	 * @return True if removal is successful, false otherwise.
	 */
	private boolean removeLatest(String table, String primaryKey) {
		int index = getLatestAddition(table, primaryKey);
		return removeAppointment(index);
	}
	
	private Stack<Integer> getGroup (int groupID) {
		String query = "SELECT ansatt.ansattNR FROM ansatt JOIN gruppeAnsatt ON ansatt.ansattNR = gruppeAnsatt.ansattNR JOIN gruppe ON gruppe.gruppeID = gruppeAnsatt.gruppeID WHERE gruppe.gruppeID = " + groupID;
		Stack<Integer> group = new Stack<Integer>();
		try {
			statement = connection.createStatement();
			results = statement.executeQuery(query);
			while (results.next()) {
				group.push(results.getInt(1));
			}
			return group;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args) {
		DBConnect dbc = new DBConnect();
		dbc.printData("avtale");
		String domain = "@firmax.no";
		String table1 = "avtale";
		String table2 = "avtaleAnsatt";
		String primaryKey = "avtaleID";
		//dbc.addNewAppointment("Something", "2012-06-15 19:15:00", "2012-06-15 23:00:00", null, 203, 623900);
		//dbc.addEmployeeToAppointment(1, 623904);
		//dbc.printData(table2);
		System.out.println(dbc.getGroup(1));
		dbc.close();
	}

}
