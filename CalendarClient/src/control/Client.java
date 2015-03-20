package control;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import dateUtils.DateHelper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Appointment;

//


public class Client
{
	
	private Socket s;
	public static DataOutputStream outToServer;
	public static BufferedReader inFromServer;
	public static String username;
	public static String fullName;
	public static String userID;
	private static String empNo;
	//private Stack<String> alarms;
	public static final int MYSQLDATETIMELENGTH = 19;
	private static String nextAlarmString;
	
	//TODO: Slettede avtaler vil fortsatt få alarm.
	public static Thread alarmListener = new Thread() {
		public void run() {
			setPriority(MIN_PRIORITY);
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e1) {
				System.out.println("InterruptedException in alarmListener thread.");
			}
			//System.out.println("Alarm listener thread running ...");
			Stack<String> alarms = getAlarms("\n");
			LocalDateTime nextAlarm;
			LocalDateTime now;
			String desc;
			String dateTime;
			//int minute = Integer.MAX_VALUE; //TODO: 15 er placeholder. Finn den faktiske verdien.
			long minute;
			while (true) {
				while (alarms.size() < 3) {
					Client.nextAlarmString = null;
					try {
						//System.out.println("Sleeping while waiting for more alarms ...");
						Thread.sleep(Long.MAX_VALUE);
					} catch (InterruptedException e) {
						//System.out.println("Interrupted! More alarms, I hope?");
					}
					alarms = getAlarms("\n");
				}
				Client.nextAlarmString = alarms.pop();
				desc = alarms.pop();
				dateTime = alarms.pop().substring(0, DateHelper.MYSQLDATETIMELENGTH);
				nextAlarm = DateHelper.getDateTime(Client.nextAlarmString);
				minute = nextAlarm.until(DateHelper.getDateTime(dateTime), ChronoUnit.MINUTES);
				now = LocalDateTime.now();
				long diff = now.until(nextAlarm, ChronoUnit.MINUTES);
				System.out.println("Time until next alarm is " + diff + " minutes ...");
				//String[] args = {"IT WORKS!"};
				//DialogApp.main(args);
				try {
					//System.out.println("Sleeping for " + diff + " minutes ...");
					long milliseconds = diff * 60000;
					Thread.sleep(milliseconds);
					String msg = "Avtalen \"" + desc + "\" begynner om " + minute + " minutter.";
					//String[] args = {msg};
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							Client.showAlert(msg);
						}
					});
				} catch (InterruptedException e) {
					//System.out.println("New alarm(s), resetting alarmstack");
					alarms = getAlarms("\n");
				}
					
				}
			}
		};
	
	public static Thread pollThread = new Thread() {
		public void run() {
			setPriority(Thread.MIN_PRIORITY);
			//System.out.println("Poll thread running ...");
			int interval = 60113; //How often the server should be polled for new alarms
			while (true) {
				try {
					try {
						Thread.sleep(interval);
					} catch (InterruptedException e) {
						System.out.println("pollThread: InterruptedException");
					}
					//System.out.println("Polling ...");
					if (hasNewAlarms()) {
						//System.out.println("New alarm(s), attempting to interrupt alarm listener thread ...");
						Client.alarmListener.interrupt();
					}else {
						//System.out.println("No new alarms.");
					}
				} catch (IOException e) {
					System.out.println("pollThread: Something went wrong.");
					break;
				}
			}
		}
	};
	
	public static void showAlert(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		//alert.initOwner(null);
		alert.setTitle("Varsel");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	public Client() throws IOException {
		init();
	}
	
	public void init() throws UnknownHostException, IOException {
		
		String host = "localhost";
		int port = 6066;
		
		try {
			s = new Socket(host, port);
			
			outToServer = new DataOutputStream(s.getOutputStream());
			inFromServer = new BufferedReader(new InputStreamReader(s.getInputStream()));
			
			
			System.out.println("Connected to serverhost " + host + " with port " + port);
			
			
			
			
			
			//s.close(); //Denne linjen fikser alt.
		} 
		catch(Exception ConnectException){
			System.out.println("Could not connect to server");
		}
		
		
	}
	
	
	public static String sendToServer(String message) throws IOException {
		String modifiedSentence;
		try {
			outToServer.writeBytes(message + "\r\n");
		}
		catch (SocketException e) {
			
		}
		String output = "";
		String tempString = inFromServer.readLine();
		while(tempString.length() > 0) {
			output += modifiedSentence = tempString + "\r\n";
			tempString = inFromServer.readLine();
		}
		return output.trim();
	}
	

	public static boolean login(String username, String password) throws IOException{	
		String response = sendToServer("login" + "#%" + username + "#%" + password);

		if (response.trim().equals("OK")) {
			System.out.println("Class:Client - Successful login!");
			Client.username = username;
			Client.empNo = Client.getEmpNo(username);
			try {
			Client.alarmListener.start();
			Client.pollThread.start();
			}
			catch(IllegalThreadStateException e){
				//Logg ut fiks. hindrer at alarmlistener starter opp mens den allerede går. 
			}
			return true;
		}
		else {
			System.out.println("Class:Client - Wrong login");
			return false;
		}
	}
	
	public static String getAppointmentList() throws IOException {
		return getAppointmentList(Client.username);
	}
	
	public static String getAppointmentList(String user) throws IOException {
		SyncTest();
		Boolean isSynced = true;
		String response = sendToServer("GETAPPOINTMENTLIST" + "#%" + user);
		while(isSynced){
			if(response.equals("CHECK")){
				response = sendToServer("GETAPPOINTMENTLIST" + "#%" + user);
			}
			else {
				isSynced = false;
			}
		}
		return response;

		
	}
	
	public static boolean addAppointment(Appointment appointment) throws IOException {
		
		
		String room = "";
		if(appointment.getRoom() != null){
			room = appointment.getRoom().split(" ")[0];
		}
		else {
			room = null;
		}
		
		
		String response1 = sendToServer("addNewAppointment" + "#%" + appointment.getDescription() + "#%" + appointment.getStart().toString() + "#%" + appointment.getFrom().toString() + "#%" + appointment.getDate().toString() + "#%" 
				+ appointment.getPlace() + "#%" + room + "#%" + Client.username);
		System.out.println("ASKLDLJASDLJKSALKJ: " + Client.username);
		// TODO add employees
		
		for (String employee : appointment.getUsers()){
			
			String[] emp = employee.split(" ");
			String response2 = sendToServer("ADDEMPLOYEETOAPPOINTMENT" + "#%" + emp[emp.length-1] + "#%" + response1);
		}
		


		
		
		return true; //response1 returns appointmentID now
		
	}
	
	public static String setAlarm(String alarmTimer, String description) throws IOException {
		String response = sendToServer("SETALARM" + "#%" + username + "#%" + description + "#%" + alarmTimer);
		return response;
	}
	
	public static String deleteAppointment(String ID) throws IOException {
	
		
		String response = sendToServer("deleteAppointment" + "#%" + ID + "#%" + username);
		return response;
		
	}
	
	public static String addUser(String username, String firstName, String lastName,String password) throws IOException {
		String response = sendToServer("ADDNEWUSER" + "#%" + username + "#%" + firstName + "#%" + lastName + "#%" + password);
		return response;
	}

	public static String checkAppointmentOwnership(String ID) throws IOException {
		
		String response = sendToServer("checkAppointmentOwnership" + "#%" + ID + "#%" + username);
		return response.substring(0, 4);
	}
	
	public static String getEmployees() throws IOException{
		
		SyncTest();
		Boolean isSynced = true;
		String response = sendToServer("GETEMPLOYEES");
		while(isSynced){
			if(response.equals("CHECK")){
				response = sendToServer("GETEMPLOYEES");
			}
			else {
				isSynced = false;
			}
		}
		return response;
		
		
	}
	
	public static String getGroups() throws IOException{
		
		
		SyncTest();
		Boolean isSynced = true;
		String response = sendToServer("GETGROUPS");
		while(isSynced){
			if(response.equals("CHECK")){
				response = sendToServer("GETGROUPS");
			}
			else {
				isSynced = false;
			}
		}
		return response;
		
		
	}
	
	public static String getGroup(String groupID) throws IOException{
		
		
		String response = sendToServer("GETGROUP" + "#%" + groupID);
		return response;
		
	}
	
	public static String hasNotifications() throws IOException {
	
		return sendToServer("hasNotifications" + "#%" + Client.username);


	}
	
	public static String changeStatus(String appointmentID, String status) throws IOException {
		return sendToServer("CHANGESTATUS" + "#%" + appointmentID + "#%" + Client.username + "#%" + status);
	}
	
	public static String flagAllNotificationsAsSeen() throws IOException {
		String response = sendToServer("flagAllNotificationsAsSeen" + "#%" + Client.username);
		return response;

	}
	
	public static String getNewNotifications() throws IOException {
		SyncTest();
		Boolean isSynced = true;
		String response = sendToServer("getNewNotifications" + "#%" + Client.username);
		while(isSynced){
			if(response.equals("CHECK")){
				response = sendToServer("getNewNotifications" + "#%" + Client.username);
			}
			else {
				isSynced = false;
			}
		}
		System.out.println("TEST2 " + response);
		return response;

	}
	
	public static String addNotification(String msg, String username) throws IOException {
		
		return sendToServer("addNotification" + "#%" + username+ "#%" + msg);
	}
	

	public static String getRooms(String start, String end, String cap, String date) throws IOException{
		
		String response = sendToServer("GETROOMS" + "#%" + start + "#%" + end + "#%" + cap + "#%" + date);
		return response;

	}
	
	public static String isInvitedEmployee(String appointmentID) throws IOException{
		

		return sendToServer("ISINVITEDEMPLOYEE" + "#%" + appointmentID + "#%" + Client.username);
		
	}
	
	public static boolean editAppointment(Appointment appointment, Appointment oldAppointment, List<String> removedEmployees) throws IOException{
		String room = "";
		String editRoom = "YES";
		if(!(appointment.getRoom().equals("null"))){
			room = appointment.getRoom().split(" ")[0];
		}
		else {
			room = "null";
		}
		String description = "null";
		String start = "null";
		String end = "null";
		String editDate = "YES";
		String place = "null";
		String date;
		
		
		if(!appointment.getDescription().equals(oldAppointment.getDescription())){
			description = appointment.getDescription();
		}
		if(!appointment.getFrom().toString().equals(oldAppointment.getFrom().toString())){
			end = appointment.getFrom().toString();
		}
		if(!appointment.getStart().toString().equals(oldAppointment.getStart().toString())){
			start = appointment.getStart().toString();
		}
		if(!appointment.getDate().toString().equals(oldAppointment.getDate().toString())){
			date = appointment.getDate().toString();
			start = appointment.getStart().toString();
			end = appointment.getFrom().toString();
		}
		else{
			date = oldAppointment.getDate().toString();
		}
		try {
		if(!appointment.getPlace().equals(oldAppointment.getPlace())){
			place = appointment.getPlace();
				}
		}
		catch(NullPointerException e){
		if(!(appointment.getPlace() == (oldAppointment.getPlace()))){
			place = appointment.getPlace();
			}
		}
		try {
		if(room.equals(oldAppointment.getRoom())){
			editRoom = "NO";
			}
		}
		catch(NullPointerException e){
		if(room == oldAppointment.getRoom()){
			editRoom = "NO";
		}
		}
		
		String response1 = sendToServer("UPDATEAPPOINTMENT" + "#%" + appointment.getID() + "#%" + description + "#%" + start + "#%" + end + "#%" + date + "#%" 
				+ place + "#%" + room + "#%" + editRoom);

		List<String> removeEmpComparingList = FXCollections.observableArrayList();
		
		for (String employee : appointment.getUsers()){
			
			String[] emp = employee.split(" ");
			String response2 = sendToServer("ADDEMPLOYEETOAPPOINTMENT" + "#%" + emp[emp.length-1] + "#%" + appointment.getID());
			removeEmpComparingList.add(emp[emp.length-1]);
		}
		//Remove employee
		for (String removeEmp : removedEmployees) {
			String[] emp = removeEmp.split(" ");
			int i = 0;
			for(String compareEmp : removeEmpComparingList) {
				if(compareEmp.equals(emp[0])){
					i++;
				}
			}
			if(i == 0){
				sendToServer("REMOVEEMPLOYEE" + "#%" + emp[0] + "#%" + appointment.getID());
			}
			
		}
		
//		//Set alarm
//		if(!(appointment.getAlarm()==0)){
//		sendToServer("SETALARM" + "#%" + username + "#%" + appointment.getID() + "#%" + appointment.getAlarm());
//		}

		
		sendToServer("FIRENOTIFICATION");
		
		
		return true; //response1 returns appointmentID now
		
		
	}
	
	public static boolean SyncTest() throws IOException {
		
		while(!sendToServer("SYNCCHECK").equals("CHECK")){
			try {
			    Thread.sleep(200);                 //1000 milliseconds is one second.
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
		}
		return true;
		
	}
	
	public static String hideAppointment(Appointment appointment) throws IOException {
		
		//Check user status
		String clientID = sendToServer("GETUSERID" + "#%" + Client.username);
		ObservableList<String> usersList = (ObservableList<String>) appointment.getUsers();
		for(String users : usersList){
			
			String[] user = users.split(" ");
			if(user[0].equals(clientID) && !((user[user.length-1].equals("Venter")) || (user[user.length-1].equals("Deltar")))){				
				String response = sendToServer("HIDEAPPOINTMENT" + "#%" + appointment.getID() + "#%" + clientID);
				return response;
			}
			
		}
		System.out.println("HIDEAPPOINTMENT FALSE");
		return "false";
		
	}
	
	public static String getAppointmentExclusive() throws IOException{
		SyncTest();
		Boolean isSynced = true;
		String response = sendToServer("GETAPPOINTMENTEXCLUSIVE" + "#%" + Client.username);
		while(isSynced){
			if(response.equals("CHECK")){
				response = sendToServer("GETAPPOINTMENTEXCLUSIVE" + "#%" + Client.username);
			}
			else {
				isSynced = false;
			}
		}
		return response;

	}
	
	public static String changeUserPass(String username, String password) throws IOException {
		String response = sendToServer("SETNEWPASSWORD" + "#%" + username + "#%" + password);
		return response;
	}
	
	public static String getUser(String id) throws IOException {
	   String response = sendToServer("GETUSER" + "#%" + id);
	   return response;
	}
	
	public static String deleteUser(String username) throws IOException {
		String response = sendToServer("DELETEUSER" + "#%" + username);
		return response;
	}
	
	public static String getAlarmString(String username) throws IOException {
		String alarms = sendToServer("GETALARMS" + "#%" + username); 
		return alarms;
	}
	
	private static Stack<String> getAlarms(String seperator) {
		Stack<String> alarmStack = new Stack<String>();
		String alarms = null;
		try {
			alarms = getAlarmString(Client.username);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String [] alarmArray = alarms.split(seperator);
		for (String alarm : alarmArray) {
			int n = alarm.length();
			int m = seperator.length();
			if (n > m) {
				alarmStack.push(alarm.substring(0, n-m));
			}
		}
		String lastAlarm = alarmStack.pop() + "0";
		alarmStack.push(lastAlarm);
		return alarmStack;
	}
	
	public static String getUserID(String username) throws IOException {
		String response = sendToServer("GETUSERID" + "#%" + username);
		return response;
	}
	
	public static String addMember(String groupID, String empNo) throws IOException {
		String response = sendToServer("ADDMEMBER" + "#%" + groupID + "#%" + empNo);
		return response;
	}
	
	public static String removeMember(String groupID, String empNo) throws IOException {
		String response = sendToServer("REMOVEMEMBER" + "#%" + groupID + "#%" + empNo);
		return response;
	}
	
	public static String getMyGroups(String username) throws IOException {
		String response = sendToServer("GETMYGROUPS" + "#%" + username);
		return response;
	}
	
	public static String getName(String username) throws IOException {
		String response = sendToServer("getName" + "#%" + username);
		return response;
	}

	public static String getEmpNo(String username) throws IOException {
		String response = sendToServer("GETEMPNO" + "#%" + username);
		return response;
	}
	
	private static boolean hasNewAlarms() throws IOException {
		if (nextAlarmString == null) {
			nextAlarmString = DateHelper.MAXMYSQLDATETIME;
		}
		String response = sendToServer("NEWALARMS" + "#%" + nextAlarmString + "#%" + empNo);
		if (response.equals("TRUE")) {
			return true;
		}else if (response.equals("FALSE")) {
			return false;
		}else {
			throw new IOException("Could not parse the response: " + response);
		}
	}

	public static String getAttendingStatus(int appointmentID) throws IOException {
		String response = sendToServer("GETATTENDINGSTATUS" + "#%" + appointmentID);
		return response;
	}
	
	public static void main(String [] args) throws Exception {
		
		Client c = new Client();
		c.username = "mariusmb";
		
		//String alarms = c.getAlarmString(username);
		//c.showAlert("IJWDIJAIODHJUNGFRKLSE");
		//String empNo = c.getEmpNo(c.username);
		Client.login(username, username);
		//System.out.println(c.hasNewAlarms());
		//System.out.println(c.empNo);
		//System.out.println(alarms);
		//c.initAlarms("\n");
		//c.alarmListener();
//		System.out.println(c.alarms);
//		while (!c.alarms.isEmpty()) {
//			String alarm = c.alarms.pop();
//			String content = c.alarms.pop();
//			System.out.println("Content: " + content + "\n");
//			System.out.println("Alarm: " + alarm + "\n\n");
//		}
		//String [] args2 = {"test"};
		//DialogApp.main(args2);
	}
}
