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
		try {
			this.connection.close();
		} catch (SQLException e) {
			System.out.println("Connection could not be closed:");
			System.out.println(e);
		}
	}
	
	public static void main(String[] args) {
		DBConnect dbc = new DBConnect();
		dbc.printData("ansatt");
		dbc.close();
	}

}
