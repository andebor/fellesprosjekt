package db;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Stack;
import java.util.TimeZone;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import dateUtils.DateHelper;

public class DBConnect {
	//This class establishes a connection to the database, enabling the server to execute queries
	
	private Connection connection;
	private Statement statement;
	//private PreparedStatement pstatement;
	//private ResultSet results;
	private final String URL = "jdbc:mysql://mysql.stud.ntnu.no:3306/mariusmb_kalenderDB";
	private final String USERNAME = "mariusmb";
	private final String PASSWORD = "password";
	private StringBuilder notification;
	private int updatedAppointmentID;
	private int changer; //Keeps track of the person who has changed something so that he does not receive notifications about it himself.
	public Thread thread;
	private Thread thread1;
	private Thread thread2;
	
	public DBConnect() {
		//Connects to the db
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			System.out.println("Successfully connected to the database ...");
		} catch (Exception e) {
			System.out.println("Connection failed:");
			e.printStackTrace();
		}
	}
	
	public void printEmployees() {
		//Prints all data in the db table ansatt.
		//Method added mainly for testing purposes.
		ResultSet results;
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
	
	public void printData(String table) {
		//Prints all data from any table.
		//Method added mainly for testing purposes.
		ResultSet results;
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
	
	/**
	 * Closes all database resources.
	 */
	public void close() {
		//Closes all resources.
		try {
			if (connection != null) {
				connection.close();
			}
			if (statement != null) {
				statement.close();
			}
//			if (results != null) {
//				results.close();
//			}
			System.out.println("Connection closed successfully.");
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
	public String getPassword(String user) {
		String query = "SELECT * FROM ansatt WHERE brukernavn = '" + user + "'";
		String result = null;
		ResultSet results;
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
	public boolean addNewAppointment(String description, String startTime, String endTime, String location, String meetingRoom, String owner) {
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
			query += "\"" + Integer.parseInt(meetingRoom) + "\", ";
		}
		query += Integer.parseInt(owner) + ");";
		try {
			statement = connection.createStatement();
			statement.executeUpdate(query);
			//Appointment should be in the DB by now. The final step is adding the owner of the appointment to the appointment.
			return addEmployeeToAppointment(getLatestAddition("avtale", "avtaleID"), Integer.parseInt(owner));
		}catch (SQLException e) {
			System.out.println("Unable to create appointment.");
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Adds employee to an appointment in the DB and sends notification to the employee.
	 * @param appointmentID "avtaleID" in the DB.
	 * @param empNo "ansattNR" in the DB.
	 * @return True if addition was successful, false otherwise.
	 */
	public boolean addEmployeeToAppointment(int appointmentID, int empNo) {
		if (isInvitedEmployee(appointmentID, empNo)) {
			System.out.println("Employee " + empNo + " has already been invited to the appointment.");
			return false;
		}
		if (!isExistingAppointment(appointmentID)) {
			System.out.println("No such appointment. Check appointmentID and try again.");
			return false;
		}
		String query = "INSERT INTO avtaleAnsatt (avtaleID, ansattNR) VALUES (" + appointmentID + ", " + empNo + ")";
		try {
			statement = connection.createStatement();
			statement.executeUpdate(query);
			//Employee should now be invited to the appointment. The remaining code sends a notification to the invited employee.
			if (empNo == getAppointmentOwner(appointmentID)) { //To prevent the creator from receiving a notification.
				changeStatus(appointmentID, empNo, 2);
				return true;
			}
			int ownerNo = getAppointmentOwner(appointmentID);
			String ownerName = getEmployeeName(ownerNo);
			String desc = getDescription(appointmentID);
			String dateString;
			String [] appointment = getAppointment(appointmentID).split("\n"); //All fields in getAppointment in array
			String dateTime = appointment[2]; //Start:CCYY-MM-DD HH:MM:SS
			dateTime = dateTime.substring("Start:".length()); //CCYY-MM-DD HH:MM:SS
			dateString = DateHelper.getNorwegianFormat(dateTime);
			String notification = ownerName + " har invitert deg til avtalen \"" + desc + "\" " + dateString;
			if (addNotification(empNo, notification)) {
				return true;
			}else {
				System.out.println("Failed to add notification.");
				return false;
			}
		} catch (SQLException e) {
			System.out.println("Could not add employee to appointment.");
			e.printStackTrace();
			return false;
		}
	}

	public String getAppointment(int appointmentID) {
		String query = "SELECT * FROM avtale WHERE avtaleID = " + appointmentID;
		ResultSet results;
		StringBuilder appointment = new StringBuilder();
		String seperator = "\n";
		try {
			statement = connection.createStatement();
			results = statement.executeQuery(query);
			if (!results.next()) {
				System.out.println("No such appointment!");
				return null;
			}
			appointment.append("ID:" + results.getInt(1) + seperator);
			appointment.append("Description:" + results.getString(2) + seperator);
			appointment.append("Start:" + results.getString(3) + seperator);
			appointment.append("End:" + results.getString(4) + seperator);
			appointment.append("Location:" + results.getString(5) + seperator);
			appointment.append("Room:" + results.getString(6) + seperator);
			appointment.append("Creator:" + results.getString(7) + seperator);
			return appointment.toString();
		} catch (SQLException e) {
			System.out.println("getAppointment: Unexpected error!");
			e.printStackTrace();
			return null;
		}	
	}
	
	public String getEmployeeName(int empNo) {
		String table = "ansatt";
		int primaryKeyValue = empNo;
		String primaryKeyName = "ansattNR";
		String field = "fornavn";
		String fName = (String) getValue(table, primaryKeyValue, primaryKeyName, field);
		field = "etternavn";
		String lName = (String) getValue(table, primaryKeyValue, primaryKeyName, field);
		return fName + " " + lName;
	}
	
	public String getDescription(int appointmentID) {
		String table = "avtale";
		int primaryKeyValue = appointmentID;
		String primaryKeyName = "avtaleID";
		String field = "formål";
		return (String) getValue(table, primaryKeyValue, primaryKeyName, field);
	}
	
	/**
	 * Checks to see if an employee is invited to an appointment
	 * @param appointmentID "avtaleID" in the DB.
	 * @param empNo "ansattNR" in the DB.
	 * @return True if employee has been invited to an appointment, false otherwise.
	 */
	public boolean isInvitedEmployee(int appointmentID, int empNo) {
		String query = "SELECT * FROM ansatt JOIN avtaleAnsatt ON avtaleAnsatt.ansattNR = ansatt.ansattNR JOIN avtale ON avtale.avtaleID = avtaleAnsatt.avtaleID WHERE avtale.avtaleID = " + appointmentID + " AND ansatt.ansattNR = " + empNo;
		ResultSet results;
		try {
			statement = connection.createStatement();
			results = statement.executeQuery(query);
			return results.next();
		}catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Checks to see if a group is invited to an appointment
	 * @param appointmentID "avtaleID" in the DB.
	 * @param groupID "ansattNR" in the DB.
	 * @return True if group has been invited to an appointment, false otherwise.
	 */
	public boolean isInvitedGroup(int appointmentID, int groupID) {
		String query = "SELECT * FROM gruppe JOIN avtaleGruppe ON avtaleGruppe.gruppeID = gruppe.gruppeID JOIN avtale ON avtale.avtaleID = avtaleGruppe.avtaleID WHERE avtale.avtaleID = " + appointmentID + " AND gruppe.gruppeID = " + groupID;
		ResultSet results;
		try {
			statement = connection.createStatement();
			results = statement.executeQuery(query);
			return results.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 
	 * @param appointmentID
	 * @return True if record of the appointment exists in the DB, false otherwise.
	 */
	public boolean isExistingAppointment(int appointmentID) {
		String query = "SELECT * FROM avtale WHERE avtaleID = " + appointmentID;
		ResultSet results;
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
	public boolean removeAppointment(int appointmentID) {
		if (!isExistingAppointment(appointmentID)) {
			System.out.println("There is no record of an appointment with the ID " + appointmentID + " in the database.");
			return false;
		}
		
		Stack<String> employees = getInvitedEmployees(appointmentID);
		String appointmentDescription = getDescription(appointmentID);
		
		String query = "DELETE FROM avtale WHERE avtaleID = " + appointmentID;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(query);
			while (!employees.isEmpty()) {
				String employeeNumber = employees.pop().split(" ")[0];
				addNotification(Integer.parseInt(employeeNumber), "Møte: " + appointmentDescription + " er avlyst/slettet");
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Updates an appointment with a new String value. Field should be validated before using this method!
	 * @param appointmentID "avtaleID" in the DB;
	 * @param field Name of the field in the DB. Use values "formål", "starttid", "sluttid", "sted"
	 * @param newValue
	 * @return
	 */
	public boolean updateAppointmentString(int appointmentID, String field, String newValue) {
		this.updatedAppointmentID = appointmentID;
		String table = "avtale";
		int primaryKeyValue = appointmentID;
		String primaryKeyName = "avtaleID";
		String desc = (String) getValue(table, primaryKeyValue, primaryKeyName, "formål");
		String query;
		if (this.notification == null) {
			this.notification = new StringBuilder();
			notification.append("Avtalen \"" + desc + "\" har blitt endret. \n");
		}
		this.changer = getAppointmentOwner(appointmentID);
		Object oldValue = getValue(table, primaryKeyValue, primaryKeyName, field);
		if (newValue == null) {
			query = "UPDATE avtale SET " + field + " = NULL WHERE avtaleID = " + appointmentID;
		}else {
			query = "UPDATE avtale SET " + field + " = \"" + newValue + "\" WHERE avtaleID = " + appointmentID;
		}
		try {
			statement = connection.createStatement();
			statement.executeUpdate(query);
			//Appointment should be updated at this point, lines below should send notifications to all invited employees.
			switch (field) {
			case "formål": notification.append("Formål har blitt endret til: \"" + newValue + "\"\n"); break;
			case "starttid": notification.append("Ny starttid: " + DateHelper.getNorwegianFormat(newValue) + "\n"); break;
			case "sluttid": notification.append("Ny sluttid: " + DateHelper.getNorwegianFormat(newValue) + "\n"); break;
			case "sted": notification.append("Nytt sted: " + newValue + "\n"); break;
			default: System.out.println("updateAppointmentString: Invalid value for parameter field."); return false;
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Updates an appointment with a new Integer value. Field should be validated before using this method!
	 * @param appointmentID "avtaleID" in the DB;
	 * @param field Name of the field in the DB. Use values "møteromNR", "ansvarlig"
	 * @param newValue
	 * @return
	 */
	public boolean updateAppointmentInteger(int appointmentID, String field, Integer newValue) {
		String table = "avtale";
		int primaryKeyValue = appointmentID;
		String primaryKeyName = "avtaleID";
		String desc = (String) getValue(table, primaryKeyValue, primaryKeyName, "formål");
		String query;
		String owner;
		if (this.notification == null) {
			this.notification = new StringBuilder();
			notification.append("Avtalen \"" + desc + "\" har blitt endret. \n");
		}
		// int oldValue = (int) getValue(table, primaryKeyValue, primaryKeyName, field);
		if (newValue == null) {
			query = "UPDATE avtale SET " + field + " = NULL WHERE avtaleID = " + appointmentID;
		}else {
			query = "UPDATE avtale SET " + field + " = " + newValue + " WHERE avtaleID = " + appointmentID;
		}
		try {
			statement = connection.createStatement();
			statement.executeUpdate(query);
			switch (field) {
			case "møteromNR": notification.append("Nytt møterom: " + newValue + "\n"); break;
			case "ansvarlig": notification.append("Ny ansvarlig: " + newValue + "\n"); break; //Not sure if this is useful
			default: System.out.println("updateAppointmentInteger: Invalid value for field."); return false;
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Returns the value of a field in the database, should work on any table where the primary key is a MySQL INTEGER.
	 * @param table Name of the SQL table
	 * @param primaryKeyValue Value of the primary key, must be int
	 * @param primaryKeyName Table field name of the primary key
	 * @param field Field name of the desired value
	 * @return Value of the field as an Object. Casting might be neccessary.
	 */
	public Object getValue(String table, int primaryKeyValue, String primaryKeyName, String field) {
		String query = "SELECT " + field + " FROM " + table + " WHERE " + primaryKeyName + " = " + primaryKeyValue;
		ResultSet results;
		try {
			statement = connection.createStatement();
			results = statement.executeQuery(query);
			if (!results.next()) {
				System.out.println("No value found.");
				return null;
			}
			Object value = results.getObject(1); //Gets the actual value
			if (results.next()) { //Checks if there are more than one value in the result set
				System.out.println("Multiple values found, returning the first value.");	
			}
			return value;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Something went wrong retrieving the value, check your input.");
			return null;
		}
	}
	
	/**
	 * Finds the latest addition to a table.
	 * @param table Name of table.
	 * @param primaryKey Name of primary key, should have auto_increment.
	 * @return Returns the ID of the latest addition to any table in the DB with auto_increment.
	 */
	public int getLatestAddition(String table, String primaryKey) {	
		String query = "SELECT * FROM " + table + " WHERE " + primaryKey + " = (SELECT MAX(" + primaryKey + ") FROM " + table + ")";
		ResultSet results;
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
	
	/**
	 * Retrieves employee number of all employees in a group from the DB.
	 * @param groupID "gruppeID" in the DB.
	 * @return A stack containing all employee numbers.
	 */
	public Stack<Integer> getGroup (int groupID) {
		String query = "SELECT ansatt.ansattNR FROM ansatt JOIN gruppeAnsatt ON ansatt.ansattNR = gruppeAnsatt.ansattNR JOIN gruppe ON gruppe.gruppeID = gruppeAnsatt.gruppeID WHERE gruppe.gruppeID = " + groupID;
		Stack<Integer> group = new Stack<Integer>();
		ResultSet results;
		try {
			statement = connection.createStatement();
			results = statement.executeQuery(query);
			while (results.next()) {
				group.push(results.getInt(1));
			}
			return group;
		} catch (SQLException e) {
			System.out.println("getGroup: Unexpected SQL error");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Adds every member of a group to an appointment in the DB.
	 * @param appointmentID "avtaleID" in the DB.
	 * @param groupID "gruppeID" in the DB.
	 * @return True if all additions were successful. False otherwise.
	 */
	public boolean addGroupToAppointment (int appointmentID, int groupID) {
		Stack<Integer> group = getGroup(groupID);
		if (isInvitedGroup(appointmentID, groupID)) {
			System.out.println("Group number " + groupID + " has already been added to this appointment.");
			return false;
		}
		int groupSize = group.size();
		int notAdded = 0;
		while (!group.isEmpty()) {
			//This while loop adds employees to the appointment one by one.
			if (!addEmployeeToAppointment(appointmentID, group.pop())) {
				//System.out.println("An error occured while trying to add employees from the group to the appointment.");
				notAdded++;
			};
		}
		System.out.println((groupSize - notAdded) + " out of " + groupSize + " employees successfully added.");
		String query = "INSERT INTO avtaleGruppe (avtaleID, gruppeID) VALUES (" + appointmentID + ", " + groupID + ")";
		//This query adds the group itself to the appointment.
		try {
			statement = connection.createStatement();
			statement.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			System.out.println("Attempt to add link between group and appointment in the database failed.");
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Removes employee from appointment in the DB.
	 * @param appointmentID "avtaleID" in the DB.
	 * @param empNo "ansattNR" in the DB.
	 * @return True if employee was successfully found and removed, false otherwise.
	 */
	public boolean removeEmployeeFromAppointment(int appointmentID, int empNo) {
		if (!isInvitedEmployee(appointmentID, empNo)) {
			System.out.println("Employee " + empNo + " has not been invited to the appointment and can therefore not be removed.");
			return false;
		}
		String query = "DELETE FROM avtaleAnsatt WHERE avtaleID = " + appointmentID + " AND ansattNR = " + empNo;
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
	 * Removes entire group from appointment in the DB.
	 * @param appointmentID "avtaleID" in the DB.
	 * @param groupID "gruppeID" in the DB.
	 * @return True if group was successfully found and removed, false otherwise.
	 */
	public boolean removeGroupFromAppointment(int appointmentID, int groupID) {
		if (!isInvitedGroup(appointmentID, groupID)) {
			System.out.println("Group " + groupID + " has not been invited to the appointment and can therefore not removed.");
			return false;
		}
		String query = "DELETE FROM avtaleGruppe WHERE avtaleID = " + appointmentID + " AND gruppeID = " + groupID;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		Stack<Integer> group = getGroup(groupID);
		int groupSize = group.size();
		int notRemoved = 0;
		while (!group.isEmpty()) {
			//This loop individually removes each group member
			//CAUTION! FIX LATER: This might remove employees from appointments even though they were individually and not as part of a group.
			if (!removeEmployeeFromAppointment(appointmentID, group.pop())) {
				notRemoved++;
			}
		}
		System.out.println((groupSize-notRemoved) + " out of " + groupSize + " group members successfully removed from the appointment");
		return true;
	}
	
	/**
	 * This method flags the appointment as hidden in the database.
	 * @param appointmentID "avtaleID" in the DB.
	 * @param empNo "ansattNR" in the DB.
	 * @return True if query was successful, false otherwise.
	 */
	public boolean hideAppointment(int appointmentID, int empNo) {
		String query = "UPDATE avtaleAnsatt SET synlig = 0 WHERE avtaleID = " + appointmentID + " AND ansattNR = " + empNo;
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
	 * Updates an employee's attendance status.
	 * @param appointmentID "avtaleID" in the DB.
	 * @param empNo "ansattNR" in the DB.
	 * @param selection Selects a valid status. 1 = "Venter", 2 = "Deltar", 3 = "Avkreftet", 4 = "Avbud". Other values are ignored. Note that "Venter" is the default value in the DB if nothing has been set.
	 * @return True if status was changed successfully, false otherwise.
	 */
	public boolean changeStatus(int appointmentID, int empNo, int selection) {
		if (this.notification == null) {
			notification = new StringBuilder();
		}else {
			System.out.println("changeStatus: Notification waiting to be fired.");
			return false;
		}
		this.updatedAppointmentID = appointmentID;
		this.changer = empNo;
		String desc = getDescription(appointmentID);
		String status;
		int empOwner = getAppointmentOwner(appointmentID);
		switch (selection) {
		case 1: status = "Venter";
		break;
		case 2: status = "Deltar";
		notification.append(getEmployeeName(empNo) + " har akseptert invitasjonen til avtalen \"" + desc + "\"");
		break;
		case 3: status = "Avkreftet";
		notification.append(getEmployeeName(empNo) + " har avslått invitasjonen til avtalen \"" + desc + "\"");
		break;
		case 4: status = "Avbud";
		notification.append(getEmployeeName(empNo) + " har meldt avbud for avtalen \"" + desc + "\"");
		break;
		default: System.out.println("Invalid status selection.");
		return false;
		}
		String query = "UPDATE avtaleAnsatt SET status = '" + status + "' WHERE avtaleID = " + appointmentID + " AND ansattNR = " + empNo;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(query);
			addNotification(empOwner, notification.toString());
			notification = null;
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Adds a notification to the "varsel" table in the DB.
	 * @param empNo "ansattNR" in the DB. Recipient of the notification.
	 * @param message String that is meant to show up when user clicks to see new notifications. 
	 * @return True if query to database is successful, false otherwise.
	 */
	public boolean addNotification(int empNo, String message) {
		String mysqlDateTime = DateHelper.getMySQLDateTime();
		String query = "INSERT INTO varsel (ansattNR, tekst, opprettet) VALUES (" + empNo + ", '" + message + "', '" + mysqlDateTime + "')";
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
	 * Flags all notifications as seen when user clicks to see new notifications.
	 * @param empNo "ansattNR" in the DB.
	 * @return True if query to the database is successful, false otherwise.
	 */
	public boolean flagAllNotificationsAsSeen(int empNo) {
		String query = "UPDATE varsel SET sett = 1 WHERE ansattNR = " + empNo;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean fireNotification() {
		if (notification == null) {
			System.out.println();
		}
		Stack<String> employees = getInvitedEmployees(this.updatedAppointmentID);
		String employee;
		int empNo;
		String notification = this.notification.toString();
		while (!employees.isEmpty()) {
			employee = employees.pop();
			empNo = Integer.parseInt(employee.substring(0, 6));
			if (empNo == this.changer) {
				continue;
			}
			if (!addNotification(empNo, notification)) {
				System.out.println("Attempt to add notification for " + getEmployeeName(empNo) + " failed.");
			}
		}
		this.notification = null;
		this.updatedAppointmentID = -1;
		return true;
	}
	
	/**
	 * Retrieves all appointments a user has been invited to.
	 * @param user "brukernavn" of the format "someone@firmax.no"
	 * @return A stack containing each appointment. Each appoinment is in turn a stack of strings representing the data associated with the appointment.
	 */
	public Stack<Stack<String>> getAppointmentsAsStack (String user) {
		String query = "SELECT formål, starttid, sluttid, sted, møteromNR, ansvarlig FROM ansatt JOIN avtaleAnsatt ON ansatt.ansattNR = avtaleAnsatt.ansattNR JOIN avtale ON avtaleAnsatt.avtaleID = avtale.avtaleID WHERE ansatt.brukernavn = '" + user + "'";
		System.out.println(query);
		Stack<Stack<String>> table = new Stack<Stack<String>>();
		Stack<String> tableRow;
		ResultSet results;
		try {
			statement = connection.createStatement();
			results = statement.executeQuery(query);
			while (results.next()) {
				tableRow = new Stack<String>();
				for (int i = 1; i <= 6; i++) {
					tableRow.push(results.getString(i));
				}
				table.push(tableRow);
			}
			return table;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Retrieves all appointments a user has been invited to as a String.
	 * @param user "brukernavn" of the format "someone@firmax.no"
	 * @return A String containing all the information related to an appointment. Each appointment is seperated with the seperator "$%".
	 */
	public String getAppointments (String user) {
		String query = "SELECT formål, starttid, sluttid, sted, møteromNR, fornavn, etternavn, avtaleID FROM (SELECT formål, starttid, sluttid, sted, møteromNR, ansvarlig, avtale.avtaleID FROM ansatt JOIN avtaleAnsatt ON ansatt.ansattNR = avtaleAnsatt.ansattNR JOIN avtale ON avtaleAnsatt.avtaleID = avtale.avtaleID WHERE ansatt.brukernavn = '" + user + "' AND avtaleAnsatt.synlig = 1) AS T1 JOIN ansatt ON ansvarlig = ansattNR";
		StringBuilder appointments = new StringBuilder();
		String fieldSeperator = "%$";
		String memberSeperator = "@/@";
		String appointmentSeperator = "$%";
		Stack<String> invitees;
		ResultSet results;
		try {
			statement = connection.createStatement();
			results = statement.executeQuery(query);
			while (results.next()) { //Gets the next appointment
				for (int i = 1; i <= 8; i++) { //Adds all relevant info about appointment
					appointments.append(results.getString(i) + fieldSeperator);
				}
				invitees = getInvitedEmployees(results.getInt(8));
				while (!invitees.isEmpty()) { //Adds information about invited employees (employee number, name and status)
					appointments.append(invitees.pop() + memberSeperator);					
				}
				appointments.delete(appointments.length()-memberSeperator.length(), appointments.length());
				appointments.append(appointmentSeperator);
			}
			if (appointments.toString().isEmpty()) {
				System.out.println("User " + user + " has no appointments!");
				return null;
			}
			appointments.delete(appointments.length()-2, appointments.length());
			return appointments.toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Retrieves all appointments a user has been invited to as a String, not including the ones where the user is the creator.
	 * @param user "brukernavn" of the format "someone@firmax.no"
	 * @return A String containing all the information related to an appointment. Each appointment is seperated with the seperator "$%".
	 */
	public String getAppointmentsExclusive (String user) {
		int empNo = getEmpno(user);
		String query = "SELECT formål, starttid, sluttid, sted, møteromNR, fornavn, etternavn, avtaleID FROM (SELECT formål, starttid, sluttid, sted, møteromNR, ansvarlig, avtale.avtaleID FROM ansatt JOIN avtaleAnsatt ON ansatt.ansattNR = avtaleAnsatt.ansattNR JOIN avtale ON avtaleAnsatt.avtaleID = avtale.avtaleID AND avtale.ansvarlig != " + empNo + " WHERE ansatt.ansattNR = " + empNo + " AND avtaleAnsatt.synlig = 1) AS T1 JOIN ansatt ON ansvarlig = ansattNR";
		//System.out.println(query);
		StringBuilder appointments = new StringBuilder();
		String fieldSeperator = "%$";
		String memberSeperator = "@/@";
		String appointmentSeperator = "$%";
		Stack<String> invitees;
		ResultSet results2;
		try {
			statement = connection.createStatement();
			results2 = statement.executeQuery(query);
			while (results2.next()) { //Gets the next appointment
				for (int i = 1; i <= 8; i++) { //Adds all relevant info about appointment
					appointments.append(results2.getString(i) + fieldSeperator);
				}
				invitees = getInvitedEmployees(results2.getInt(8));
				while (!invitees.isEmpty()) { //Adds information about invited employees (employee number, name and status)
					appointments.append(invitees.pop() + memberSeperator);					
				}
				appointments.delete(appointments.length()-3, appointments.length());
				appointments.append(appointmentSeperator);
			}
			if (appointments.toString().isEmpty()) {
				System.out.println("User " + user + " has no appointments!");
				return null;
			}
			appointments.delete(appointments.length()-2, appointments.length());
			return appointments.toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String getUsername(int empNo) {
		String query = "SELECT brukernavn FROM ansatt WHERE ansattNR = " + empNo;
		ResultSet results;
		try {
			statement = connection.createStatement();
			results = statement.executeQuery(query);
			if (!results.next()) {
				System.out.println("No user found!");
				return null;
			}
			return results.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public int getEmpno(String username) {
		String query = "SELECT ansattNR FROM ansatt WHERE brukernavn = '" + username + "'";
		ResultSet results;
		try {
			statement = connection.createStatement();
			results = statement.executeQuery(query);
			if (!results.next()) {
				System.out.println("No such user!");
				return -1;
			}
			return results.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public int getAppointmentOwner(int appointmentID) {
		String table = "avtale";
		int primaryKeyValue = appointmentID;
		String primaryKeyName = "avtaleID";
		String field = "ansvarlig";
		Object owner = getValue(table, primaryKeyValue, primaryKeyName, field);
		if (owner == null) {
			System.out.println("No owner found.");
			if (!isExistingAppointment(appointmentID)) {
				System.out.println("No such appointment.");
			}
			return -1;
		}
		return (int) owner;
	}
	
	public Stack<String> getInvitedEmployees(int appointmentID) {
		String query = "SELECT ansatt.ansattNR, fornavn, etternavn, status FROM ansatt JOIN avtaleAnsatt ON avtaleAnsatt.ansattNR = ansatt.ansattNR JOIN avtale ON avtale.avtaleID = avtaleAnsatt.avtaleID WHERE avtale.avtaleID = " + appointmentID;
		Stack<String> employees = new Stack<String>();
		String seperator = " ";
		ResultSet results;
		try {
			statement = connection.createStatement();
			results = statement.executeQuery(query);
			if (!results.next()) {
				System.out.println("No employees invited to this appointment.");
				return null;
			}
			do {
				String empNo = results.getString(1);
				String fName = results.getString(2);
				String lName = results.getString(3);
				String status = results.getString(4);
				String employee = empNo + seperator + fName + seperator + lName + seperator + status;
				employees.push(employee);
			} while (results.next());
		} catch (SQLException e) {
			System.out.println("Failed to retrieve employees from database.");
			e.printStackTrace();
			return null;
		}
		return employees;
	}
	
	public boolean isExistingUser(int empNo) {
		return getUsername(empNo) != null;
	}

	public String addUser(String username, String firstName, String lastName,String password, byte[] salt) throws SQLException {
		PreparedStatement  ps = connection.prepareStatement("INSERT INTO ansatt (fornavn, etternavn, brukernavn, passord, salt) VALUES (?,?,?,?,?)");
		if (username == null || firstName == null || lastName == null || password == null || salt == null) {
			return "Missing arguments";
		} else {
			try {
				ps.setString(1, firstName);
				ps.setString(2, lastName);
				ps.setString(3, username);
				ps.setString(4, password);
				ps.setBytes(5, salt);
				ps.executeUpdate();
				int groupID = getGroupID("Alle ansatte");
				int empNo = getEmpno(username);
				if (!addMember(groupID, empNo)) {
					return "User was created but could not be added to the group \"Alle ansatte\"";
				}
				return "User was created successfully.";
				
			} catch (SQLException e) {
				System.out.println("SQL error: Unable to create user.");
				e.printStackTrace();
				return "Unable to create user.";
			}
		}
	}
	
	public int getGroupID(String groupName) {
		String query = "SELECT gruppeID FROM gruppe WHERE navn = '" + groupName + "'";
		ResultSet results;
		try {
			statement = connection.createStatement();
			results = statement.executeQuery(query);
			if (!results.next()) {
				System.out.println("No such group!");
				return -1;
			}
			return results.getInt(1);
		} catch (SQLException e) {
			System.out.println("getGroupID: Unexpected SQL Error!");
			e.printStackTrace();
			return -1;
		}
		
	}
	
	public byte[] getSalt(String username) {
		String query = "SELECT salt FROM ansatt WHERE brukernavn = '" + username + "';";
		ResultSet results;
		if (username == null) {
			System.out.println("Missing argument: Need username to return salt.");
			return null;
		} else {
			try {
				statement = connection.createStatement();
				results = statement.executeQuery(query);
				if (!results.next()) {
					System.out.println("No match!");
					return null;
				}	
				return results.getBytes("salt");
			} catch (SQLException e) {
				System.out.println("SQL error when trying to get salt from db.");
				e.printStackTrace();
				return null;
			}
		}
	}
	
	/**
	 * Returns all employees in the database as a string.
	 * @return A string containing all employees.
	 */
	public String getEmployees() {
		String fieldSeperator = "&/&";
		String employeeSeperator = "@/@";
		StringBuilder employees = new StringBuilder();
		String query = "SELECT ansattNR, fornavn, etternavn, brukernavn FROM ansatt";
		ResultSet results;
		try {
			statement = connection.createStatement();
			results = statement.executeQuery(query);
			while (results.next()) {
				for (int i = 1; i <= 4; i++) { //Adds full name and employee number
					employees.append(results.getString(i));
					employees.append(fieldSeperator); //Indicates new field
				}
				employees.delete(employees.length()-fieldSeperator.length(), employees.length()); //Removes the last seperator
				employees.append(employeeSeperator); //Indicates new employee
			}
			employees.delete(employees.length()-employeeSeperator.length(), employees.length()); //Removes the last seperator
			return employees.toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Gets all meeting rooms in the database.
	 * @return All meeting rooms as a String.
	 */
	public String getMeetingRooms() {
		String query = "SELECT * FROM møterom";
		ResultSet results;
		String roomNo;
		StringBuilder rooms = new StringBuilder();
		String name;
		String cap;
		String fieldSeperator = " ";
		String roomSeperator = "\n";
		try {
			statement = connection.createStatement();
			results = statement.executeQuery(query);
			while (results.next()) { //Retrieves next meeting room
				roomNo = results.getString(1);
				name = results.getString(2);
				cap = results.getString(3);
				rooms.append("No:" + roomNo + fieldSeperator + "Navn:" + name + fieldSeperator + "Capacity:" + cap + roomSeperator);
			}
			rooms.delete(rooms.length()-roomSeperator.length(), rooms.length()); //Removes the last seperator
			return rooms.toString();
		} catch (SQLException e) {
			System.out.println("Unable to retrieve meeting rooms from database.");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Same as getMeetingRooms() without parameters, but this method allows any seperator to be used
	 * @param fieldSeperator Desired field seperator
	 * @param roomSeperator Desired room seperator
	 * @return All meeting rooms as a string
	 */
	public String getMeetingRooms(String fieldSeperator, String roomSeperator) {
		String query = "SELECT * FROM møterom";
		ResultSet results;
		String roomNo;
		StringBuilder rooms = new StringBuilder();
		String name;
		String cap;
		try {
			statement = connection.createStatement();
			results = statement.executeQuery(query);
			while (results.next()) { //Retrieves next meeting room
				roomNo = results.getString(1);
				name = results.getString(2);
				cap = results.getString(3);
				rooms.append("" + roomNo + fieldSeperator  + name + fieldSeperator + cap + roomSeperator);
			}
			rooms.delete(rooms.length()-roomSeperator.length(), rooms.length()); //Removes the last seperator
			return rooms.toString();
		} catch (SQLException e) {
			System.out.println("Unable to retrieve meeting rooms from database.");
			e.printStackTrace();
			return null;
		}
	}
	
	
	public String getAppointments(String intervalStart, String intervalEnd) {
		String query = "SELECT * FROM avtale WHERE (starttid >= '" + intervalStart + "' AND starttid < '" + intervalEnd + "') OR (sluttid > '" + intervalStart + "' AND sluttid <= '" + intervalEnd + "') OR (starttid < '" + intervalStart + "' AND sluttid > '" + intervalEnd + "')";
		System.out.println(query);
		ResultSet results;
		StringBuilder appointments = new StringBuilder();
		String fieldSeperator = " ";
		String appointmentSeperator = "\n";
		String id;
		String desc;
		String startTime;
		String endTime;
		String location;
		String meetingRoom;
		String owner;
		try {
			statement = connection.createStatement();
			results = statement.executeQuery(query);
			while (results.next()) {
				id = results.getString(1);
				desc = results.getString(2);
				startTime = results.getString(3);
				endTime = results.getString(4);
				location = results.getString(5);
				meetingRoom = results.getString(6);
				owner = results.getString(7);
				appointments.append("ID:" + id + fieldSeperator + "Description:" + desc + fieldSeperator + "Start:" + startTime + fieldSeperator + "End:" + endTime + fieldSeperator + "Location:" + location + fieldSeperator + "Room:" + meetingRoom + fieldSeperator + "Creator:" + owner + appointmentSeperator);
			}
			appointments.delete(appointments.length()-appointmentSeperator.length(), appointments.length()); //Removes final seperator
			return appointments.toString();
		} catch (SQLException e) {
			System.out.println("Unable to retrieve appointments!");
			e.printStackTrace();
			return null;
		}
	}
	
	public String getNotifications(int empNo) {
		String query = "SELECT varselID, tekst, CAST(sett AS UNSIGNED) AS sett, opprettet FROM varsel WHERE ansattNR = " + empNo;
		StringBuilder notifications = new StringBuilder();
		String fieldSeperator = "\n";
		String notificationSeperator = "\n\n";
		String id;
		String message;
		boolean isSeen;
		String seen;
		String created;
		ResultSet results;
		try {
			statement = connection.createStatement();
			results = statement.executeQuery(query);
			if (!results.next()) {
				System.out.println("User " + empNo + " has no notifications!");
				return null;
			}
			do {
				id = results.getString(1);
				message = results.getString(2);
				isSeen = results.getBoolean(3);
				created = results.getString(4);
				if (isSeen) {
					seen = "Yes";
				}else {
					seen = "No";
				}
				notifications.append("ID:" + id + fieldSeperator + "Created:" + created + fieldSeperator + "Content:" + message + fieldSeperator + "Seen:" + seen + notificationSeperator);;
			} while (results.next());
			notifications.delete(notifications.length()-notificationSeperator.length(), notifications.length());
			return notifications.toString();
		} catch (SQLException e) {
			System.out.println("An error occured trying to retrieve notifications.");
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	public String getNewNotifications(int empNo) {
		String query = "SELECT varselID, tekst, CAST(sett AS UNSIGNED) AS sett, opprettet FROM varsel WHERE ansattNR = " + empNo + " AND sett = " + 0;
		StringBuilder notifications = new StringBuilder();
		String fieldSeperator = "\n";
		String notificationSeperator = "¤%¤";
		String id;
		String message;
		String created;
		ResultSet results;
		try {
			statement = connection.createStatement();
			results = statement.executeQuery(query);
			if (!results.next()) {
				System.out.println("User " + empNo + " has no new notifications!");
				return null;
			}
			do {
				id = results.getString(1);
				message = results.getString(2);
				created = results.getString(4);
				notifications.append("ID:" + id + fieldSeperator + "Created:" + created + fieldSeperator + "Content:" + message + notificationSeperator);;
			} while (results.next());
			notifications.delete(notifications.length()-notificationSeperator.length(), notifications.length());
			return notifications.toString();
		} catch (SQLException e) {
			System.out.println("An error occured trying to retrieve notifications.");
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean hasNewNotifications(int empNo) {
		return getNewNotifications(empNo) != null;
	}
	
	public String setNewPass(String username, String password, byte[] salt) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("UPDATE ansatt SET passord = ?, salt = ? WHERE brukernavn = ?");
		if (username == null || password == null || salt == null) {
			return "Missing arguments to set new user password.";
		} else {
			try {
				ps.setString(1, password);
				ps.setBytes(2, salt);
				ps.setString(3, username);
				ps.executeUpdate();
				return "User password was updated successfully.";
				
			} catch (Exception e) {
				System.out.println("SQL Error: Unable to update password for user: " + username);
				e.printStackTrace();
				return "Unable to update password";
			}
		}
	}

	public String getReservedRooms(String intervalStart, String intervalEnd) {
		String query = "SELECT møterom.møteromNR, navn, kapasitet FROM avtale JOIN møterom ON møterom.møteromNR = avtale.møteromNR WHERE (starttid >= '" + intervalStart + "' AND starttid < '" + intervalEnd + "') OR (sluttid > '" + intervalStart + "' AND sluttid <= '" + intervalEnd + "') OR (starttid < '" + intervalStart + "' AND sluttid > '" + intervalEnd + "') GROUP BY møterom.møteromNR";
		System.out.println(query);
		ResultSet results;
		StringBuilder rooms = new StringBuilder();
		String fieldSeperator = " ";
		String roomSeperator = "\n";
		String roomNo;
		String name;
		String cap;
		try {
			statement = connection.createStatement();
			results = statement.executeQuery(query);
			while (results.next()) {
				roomNo = results.getString(1);
				name = results.getString(2);
				cap = results.getString(3);
				rooms.append("No:" + roomNo + fieldSeperator + "Name:" + name + fieldSeperator + "Capacity:" + cap + roomSeperator);
			}
			rooms.delete(rooms.length()-roomSeperator.length(), rooms.length()); //Removes final seperator
			return rooms.toString();
		} catch (SQLException e) {
			System.out.println("Unable to retrieve reserved meeting rooms!");
			e.printStackTrace();
			return null;
		}
	}
	
	public String getAvailableRooms(String intervalStart, String intervalEnd) {
		String query = "SELECT møteromNR, navn, kapasitet FROM møterom WHERE møterom.møteromNR NOT IN(SELECT møterom.møteromNR FROM avtale JOIN møterom ON møterom.møteromNR = avtale.møteromNR WHERE (starttid >= '" + intervalStart + "' AND starttid < '" + intervalEnd + "') OR (sluttid > '" + intervalStart + "' AND sluttid <= '" + intervalEnd + "') OR (starttid < '" + intervalStart + "' AND sluttid > '" + intervalEnd + "') GROUP BY møterom.møteromNR)";
		System.out.println(query);
		ResultSet results;
		StringBuilder rooms = new StringBuilder();
		String fieldSeperator = " ";
		String roomSeperator = "\n";
		String roomNo;
		String name;
		String cap;
		try {
			statement = connection.createStatement();
			results = statement.executeQuery(query);
			if (!results.next()) {
				System.out.println("No available rooms found in the specified interval!");
				return null;
			}
			do {
				roomNo = results.getString(1);
				name = results.getString(2);
				cap = results.getString(3);
				rooms.append("No:" + roomNo + fieldSeperator + "Name:" + name + fieldSeperator + "Capacity:" + cap + roomSeperator);
			} while (results.next());
			rooms.delete(rooms.length()-roomSeperator.length(), rooms.length()); //Removes final seperator
			return rooms.toString();
		} catch (SQLException e) {
			System.out.println("Unable to retrieve reserved meeting rooms!");
			e.printStackTrace();
			return null;
		}
	}
	
	public String getAvailableRooms(String intervalStart, String intervalEnd, int minCapacity) {
		String query = "SELECT møteromNR, navn, kapasitet FROM møterom WHERE møterom.møteromNR NOT IN(SELECT møterom.møteromNR FROM avtale JOIN møterom ON møterom.møteromNR = avtale.møteromNR WHERE (starttid >= '" + intervalStart + "' AND starttid < '" + intervalEnd + "') OR (sluttid > '" + intervalStart + "' AND sluttid <= '" + intervalEnd + "') OR (starttid < '" + intervalStart + "' AND sluttid > '" + intervalEnd + "') GROUP BY møterom.møteromNR) AND kapasitet >= " + minCapacity;
		ResultSet results;
		StringBuilder rooms = new StringBuilder();
		String fieldSeperator = "#/#";
		String roomSeperator = "/@/";
		String roomNo;
		String name;
		String cap;
		try {
			statement = connection.createStatement();
			results = statement.executeQuery(query);
			if (!results.next()) {
				System.out.println("No available rooms!");
				return null;
			}
			do {
				roomNo = results.getString(1);
				name = results.getString(2);
				cap = results.getString(3);
				rooms.append("" + roomNo + fieldSeperator + name + fieldSeperator + cap + roomSeperator);
			} while (results.next());
			rooms.delete(rooms.length()-roomSeperator.length(), rooms.length()); //Removes final seperator
			return rooms.toString();
		} catch (SQLException e) {
			System.out.println("Unable to retrieve available meeting rooms!");
			e.printStackTrace();
			return null;
		}
	}
	
	public String deleteUser(String username) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("DELETE FROM ansatt WHERE brukernavn = ?");
		if (username == null) {
			return "No username argument.";
		}else {
			try {
				ps.setString(1, username);
				ps.executeUpdate();
				return "User was deleted successfully!";
			} catch (Exception e) {
				System.out.println("SQL Error: Unable to delete user: " + username);
				e.printStackTrace();
				return "Unable to delete user.";
			}
		}
	}

	public String getGroups() {
		String query = "SELECT gruppeID, navn, leder FROM gruppe";
		ResultSet results;
		StringBuilder groups = new StringBuilder();
		String fieldSeperator = "%&%";
		String groupSeperator = "\n";
		int id;
		String name;
		int leader;
		try {
			statement = connection.createStatement();
			results = statement.executeQuery(query);
			while (results.next()) {
				id = results.getInt(1);
				name = results.getString(2);
				leader = results.getInt(3);
				groups.append("ID:" + id + fieldSeperator + "Name:" + name + fieldSeperator + "Leader:" + leader + groupSeperator);
			}
			groups.delete(groups.length()-groupSeperator.length(), groups.length());
			return groups.toString();
		} catch (SQLException e) {
			System.out.println("getGroups(): Something went wrong trying to retrieve the groups from the database.");
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean setAlarm(int appointmentID, int empNo, long offset) {
		String startTime = getValue("avtale", appointmentID,"avtaleID", "starttid").toString().substring(0, 19);
		String alarmTime = DateHelper.getMySQLDateTimePast(startTime, offset);
		String query = "UPDATE avtaleAnsatt SET alarm = '" + alarmTime + "' WHERE avtaleID = " + appointmentID + " AND ansattNR = " + empNo;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			System.out.println("setAlarm: Something went wrong!");
			e.printStackTrace();
			return false;
		}
	}
	
	public String getAlarmsAsString(int empNo) {
		Stack<String> alarms = getFutureAlarms(empNo);
		String seperator = "\n";
		StringBuilder alarmString = new StringBuilder();
		while (!alarms.isEmpty()) {
			alarmString.append(alarms.pop() + seperator);
		}
		alarmString.delete(alarmString.length()-seperator.length(), alarmString.length());
		return alarmString.toString();
	}
	
	public Stack<String> getAlarms(int empNo) {
		String query = "SELECT alarm, avtaleID FROM avtaleAnsatt WHERE alarm IS NOT NULL AND ansattNR = " + empNo + " ORDER BY alarm";
		ResultSet results;
		String alarm;
		Stack<String> alarms = new Stack<String>();
		String content; //Name of the appointment
		int appointmentID;
		try {
			statement = connection.createStatement();
			results = statement.executeQuery(query);
			if (!results.next()) {
				System.out.println("User has no alarms!");
				return null;
			}
			do {
				alarm = results.getString(1).substring(0, 19);
				appointmentID = results.getInt(2);
				
				content = getValue("avtale", appointmentID, "avtaleID", "formål").toString();
				alarms.push(alarm);
				alarms.push(content);
			} while(results.next());
			return alarms;
		} catch (SQLException e) {
			System.out.println("getAlarms: Something went wrong!");
			e.printStackTrace();
			return null;
		}
	}
	
	public Stack<String> getFutureAlarms(int empNo) {
		String now = DateHelper.getMySQLDateTime();
		String query = "SELECT alarm, avtaleID FROM avtaleAnsatt WHERE alarm IS NOT NULL AND ansattNR = " + empNo + " AND alarm > '" + now + "' ORDER BY alarm";
		ResultSet results;
		String alarm;
		Stack<String> alarms = new Stack<String>();
		String content; //Name of the appointment
		String date; //Datetime of appointment
		int appointmentID;
		try {
			statement = connection.createStatement();
			results = statement.executeQuery(query);
			if (!results.next()) {
				System.out.println("User has no alarms!");
				return null;
			}
			do {
				alarm = results.getString(1).substring(0, 19);
				appointmentID = results.getInt(2);
				
				content = getValue("avtale", appointmentID, "avtaleID", "formål").toString();
				date = getValue("avtale", appointmentID, "avtaleID", "starttid").toString();
				alarms.push(alarm);
				alarms.push(content);
				alarms.push(date);
			} while(results.next());
			return alarms;
		} catch (SQLException e) {
			System.out.println("getAlarms: Something went wrong!");
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean addMember(int groupID, int empNo) {
		//int empNo = getEmpno(user);
		String query = "INSERT INTO gruppeAnsatt (gruppeID, ansattNR) VALUES (" + groupID + ", " + empNo + ")";
		try {
			statement = connection.createStatement();
			statement.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			System.out.println("addMember: Unexpected SQL error!");
			if (isMember(groupID, empNo)) {
				System.out.println("addMember: The user is already a member of this group.");
			}
			//e.printStackTrace();
			return false;
		}
	}
	
	public boolean isMember(int groupID, int empNo) {
		//int empNo = getEmpno(user);
		String query = "SELECT * FROM gruppeAnsatt WHERE gruppeID = " + groupID + " AND ansattNR = " + empNo;
		ResultSet results;
		try {
			statement = connection.createStatement();
			results = statement.executeQuery(query);
			return results.next();
		} catch (SQLException e) {
			System.out.println("isMember: Unexpected SQL error!");
			//e.printStackTrace();
			return true;
		}
	}
	
	public boolean removeMember(int groupID, int empNo) {
		//int empNo = getEmpno(user);
		String query = "DELETE FROM gruppeAnsatt WHERE gruppeID = " + groupID + " AND ansattNR = " + empNo;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			System.out.println("removeMember: Unexpected SQL error!");
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean addGroup(String name, int empNo) {
		//int empNo = getEmpno(creator);
		String query = "INSERT INTO gruppe (navn, leder) VALUES ('" + name + "', " + empNo + ")";
		try {
			statement = connection.createStatement();
			statement.executeUpdate(query);
			String table = "gruppe";
			String primaryKey = "gruppeID";
			int groupID = getLatestAddition(table, primaryKey);
			return addMember(groupID, empNo); //Adds the creator of the group to the group
		} catch (SQLException e) {
			System.out.println("addGroup: Unexpected SQL error!");
			//e.printStackTrace();
			return false;
		}
	}
	
	public boolean removeGroup(int groupID) {
		String query = "DELETE FROM gruppe WHERE gruppeID = " + groupID;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			System.out.println("removeGroup: Unexpected SQL Error!");
			e.printStackTrace();
			return false;
		}
	}
	
	public Stack<String> getAllUsers() {
		String query = "SELECT brukernavn FROM ansatt";
		Stack<String> users = new Stack<String>();
		ResultSet results;
		try {
			statement = connection.createStatement();
			results = statement.executeQuery(query);
			while (results.next()) {
				users.push(results.getString(1));
			}
			return users;
		} catch (SQLException e) {
			System.out.println("getAllUsers: Unexpected SQL error!");
			e.printStackTrace();
			return null;
		}
	}
	
    public String getMyGroups(String username) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT gruppe.gruppeID, navn, leder FROM gruppe, gruppeAnsatt, ansatt where gruppe.gruppeID=gruppeAnsatt.gruppeID AND ansatt.ansattNR=gruppeAnsatt.ansattNR AND ansatt.brukernavn=?");
        ResultSet results;
        StringBuilder groups = new StringBuilder();
        String fieldSeperator = "%&%";
        String groupSeperator = "\n";
        int id;
        String name;
        int leader;
        try {
            ps.setString(1, username);
            results = ps.executeQuery();
            while (results.next()) {
                id = results.getInt(1);
                name = results.getString(2);
                leader = results.getInt(3);
                groups.append("ID:" + id + fieldSeperator + "Name:" + name + fieldSeperator + "Leader:" + leader + groupSeperator);
            }
            groups.delete(groups.length()-groupSeperator.length(), groups.length());
            return groups.toString();
        } catch (SQLException e) {
            System.out.println("getGroups(): Something went wrong trying to retrieve the groups from the database.");
            e.printStackTrace();
            return null;
        }
    }
    

    public void testThread() {
    	this.thread1 = new Thread() {
			public void run() {
				    	System.out.println("Entering sleep, do not disturb!");
				    	int sleepTime = 0;
				    	try {
				    		while (sleepTime < 5) {
				    			if (Thread.interrupted()) {
				    				System.out.println("WHAT DID I JUST SAY????");
				    			}
				    			Thread.sleep(1000);
				    			System.out.println("Zzzz ...");
				    			sleepTime++;
				    		}
				    		System.out.println("Ah, that's much better!");
				    	} catch (InterruptedException e) {
				    		System.out.println("WHAT DID I JUST SAY????");
				    	}
			}
		};
		this.thread1.start();
    }
    
    public void interrupt() {
    	this.thread2 = new Thread() {
			public void run() {
				try {
					Thread.sleep(3000);
					System.out.println("HEEY! Whatcha doin'?");
					DBConnect.this.thread1.interrupt();

				} catch (InterruptedException e) {
					System.out.println("Backfire!");
				}
			}
		};
		this.thread2.start();
    }
    
    public void alarmTest() {
    	String description = "ALARMTEST";
    	String startTime = DateHelper.getMySQLDateTimeFuture(DateHelper.getMySQLDateTime(), 10000); //Ny avtale 16 min inn i fremtiden
    	String endTime = DateHelper.getMySQLDateTimeFuture(DateHelper.getMySQLDateTime(), 360); //Random tid etter start
    	String location = "STED";
    	String meetingRoom = null;
    	String owner = "623917";
    	addNewAppointment(description, startTime, endTime, location, meetingRoom, owner);
    	String table = "avtale";
    	String primaryKey = "avtaleID";
    	int appointmentID = getLatestAddition(table, primaryKey);
    	String username = "mariusmb";
    	int empNo = getEmpno(username);
    	long offset = 15; //Alarm 15 minutter før avtalen begynner.
    	setAlarm(appointmentID, empNo, offset);
    }
    
    public boolean hasEarlierAlarms(String alarm, int empNo) {
    	String now = DateHelper.getMySQLDateTime();
    	String query = "SELECT * FROM avtaleAnsatt WHERE alarm > '" + now + "' AND alarm < '" + alarm + "' AND ansattNR = " + empNo;
    	ResultSet results;
    	try {
			statement = connection.createStatement();
			results = statement.executeQuery(query);
			return results.next();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.out.println("hasEarlierAlarms: Unexpected SQL Error!");
		}
    	return true;
    }	
    	
    public String getAttendingStatus(int appointmentID) throws SQLException {
    	int invited = getInvitedEmployees(appointmentID).size();
    	PreparedStatement ps = connection.prepareStatement("select count(*) from avtaleAnsatt, avtale where avtale.avtaleID=avtaleAnsatt.avtaleID and avtale.avtaleID=? and avtaleAnsatt.status='Deltar' group by status");
    	ResultSet results;
    	StringBuilder counters = new StringBuilder();
    	int attendees = 0;
    	try {
			ps.setInt(1, appointmentID);
			results = ps.executeQuery();
			while (results.next()) {
				attendees = results.getInt(1);
				}
			//Format: attendees/Total amount of invited guests ex. 4/10
			counters.append(attendees + "/" + invited);
			return counters.toString();
		} catch (SQLException e) {
            e.printStackTrace();
            return null;
		}
    }
	
	public static void main(String[] args) throws SQLException {
		DBConnect dbc = new DBConnect();
		String domain = "@firmax.no";
		String table1 = "avtale";
		String table2 = "avtaleAnsatt";
		String PKName = "avtaleID";
		String field = "formål";
		String user1 = "hermoine" + domain;
		String user2 = "morganfreeman" + domain;
		int empNo1 = 623903;
		int empNo2 = 623901;
		int empNoX = 2356612;
		String desc = "Sutte kjepp og LEV VEL";
		String startTime = "2015-05-09 22:00:00";
		String endTime = "2015-05-09 23:00:00";
		String location = null;
		String meetingRoom = "203";
		String owner = "623917";
		int admin = dbc.getEmpno("admin");
		int andebor = dbc.getEmpno("andebor");
		int alfredb = dbc.getEmpno("alfredb");
		int vikleikl = dbc.getEmpno("vigleikl");
		int mariusmb = dbc.getEmpno("mariusmb");
		String PASSWORD = "password";
		//dbc.updateAppointmentString(52, field, "Slikke peis");
		//dbc.updateAppointmentString(76, "sluttid", endTime);
		//dbc.updateAppointmentInteger(73, "møteromNR", 201);
		//dbc.changeStatus(53, mariusmb, 3);
		//dbc.fireNotification();
		//dbc.addEmployeeToAppointment(73, andebor);
		//System.out.println(dbc.getNewNotifications(623923));
		//System.out.println(dbc.getValue(table1, 72, "avtaleID", "starttid"));
		//System.out.println(dbc.getAppointmentsExclusive("mariusmb"));
		//System.out.println(dbc.setAlarm(50, mariusmb, 60));
		//System.out.println(dbc.getAlarms(mariusmb));
		//String oneMinuteAhead = DateHelper.getMySQLDateTimeFuture(DateHelper.getMySQLDateTime(), 5);
		//Stack<String> alarms = new Stack();
		//alarms.push(oneMinuteAhead);
		//System.out.println(oneMinuteAhead);
		//dbc.alarmListener(alarms);
//		System.out.println(dbc.addGroup("Ballefrans", "mariusmb"));
		//Stack<String> users = dbc.getAllUsers();
		//String user;
//		while (!users.isEmpty()) {
//			user = users.pop();
//			dbc.addMember(6, user);
//		}
		//System.out.println(dbc.getGroupID("Alle ansatte"));
		//System.out.println(dbc.getGroup(dbc.getGroupID("Alle ansatte")));
		//System.out.println(dbc.addNewAppointment("Eksempelnavn", "2015-03-19 13:00:00", "2015-03-15 15:00:00", "Eksempelsted", null, "623917"));
		//dbc.testThread();
		//dbc.interrupt();
//		try {
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		dbc.alarmTest();
		//dbc.setAlarm(90, mariusmb, 15);
		//System.out.println(dbc.getFutureAlarms(mariusmb));
		//String alarm = "2015-03-20 16:00:00";
		//System.out.println(dbc.hasEarlierAlarms(alarm, mariusmb));
		//System.out.println(mariusmb);
		dbc.close();
	}

}
