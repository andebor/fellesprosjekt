package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
	
	public void printData(String table) {
		//Prints all data in the db table ansatt. Originally intended to print all data from any table, which is why it takes the "table" argument.
		//Method added mainly for testing purposes.
		try {
			String query = "select * from " + table;
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
	
	private String getPassword(String user) {
		//This method takes a username ("something@firmax.no") and returns that user's password. Returns null if user does not exist.
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
	
	private boolean addNewAppointment(String description, String startTime, String endTime, String location, Integer meetingRoom, int owner) {
		//Adds new appointment to the DB.
		//description, location and meetingRoom may be null.
		//owner should be employee number of whomever created the appointment.
		//start time and end time must be formatted strings. Correct format is "CCYY-MM-12 HH:MM:SS".
		String query = "INSERT INTO avtale (formål, starttid, sluttid, ";
		if (location != null) {
			query += "sted, ";
		}
		if (meetingRoom != null) {
			query += "møteromNR, ";
		}
		query += "ansvarlig) VALUES ('";
		if (description != null) {
			query += description + "', '";
		}else {
			query += "Ny avtale', '";
		}
		query += startTime + "', '" + endTime + "', ";
		if (location != null) {
			query += "'" + location + "', ";
		}
		if (meetingRoom != null) {
			query += "'" + meetingRoom + "', ";
		}
		query += owner + ");";
		System.out.println(query);
		try {
			statement = connection.createStatement();
			statement.executeUpdate(query);
		}catch (SQLException e) {
			System.out.println("Unable to create appointment.");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
		DBConnect dbc = new DBConnect();
		dbc.printData("ansatt");
		String domain = "@firmax.no";
		String password = dbc.getPassword("gordonfreeman" + domain);
		//System.out.println(password);
		dbc.addNewAppointment("Planlegge fest hos Mange", "2012-06-15 09:15:00", "2012-06-15 16:00:00", null, 203, 623901);
		dbc.close();
	}

}
