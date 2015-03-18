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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import dateUtils.DateHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;

//


public class Client
{
	
	private Socket s;
	public static DataOutputStream outToServer;
	public static BufferedReader inFromServer;
	public static String username;
	private Stack<String> alarms;
	
	public Client() throws IOException {
		init();
	}
	
	public void init() throws UnknownHostException, IOException {
		
		String host = "localhost";
		int port = 6067;
		
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
			return true;
		}
		else {
			System.out.println("Class:Client - Wrong login");
			return false;
		}
	}
	
	public static String getAppointmentList() throws IOException {
		System.out.println("see or not");
		return getAppointmentList(Client.username);
	}
	
	public static String getAppointmentList(String user) throws IOException {
		System.out.println("only this please");
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
		
		return sendToServer("GETEMPLOYEES");
		
	}
	
	public static String getGroups() throws IOException{
		
		return sendToServer("GETGROUPS");
		
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
	
	public static String getAlarms() throws IOException {
		String alarms = sendToServer("GETALARMS" + "#%" + username); 
		return alarms;
	}
	
	public void initAlarms(String seperator) throws IOException {
		this.alarms = new Stack<String>();
		String alarms = getAlarms();
		String [] alarmArray = alarms.split(seperator);
		List<String> alarmList = Arrays.asList(alarmArray);
		this.alarms.addAll(alarmList);
		Thread t = Thread.currentThread();
	}
	
	public void alarmListener(Stack<String> alarms) {
		//int mariusmb = getEmpno("mariusmb");
		//Stack<String> alarms = getAlarms(mariusmb);
		LocalDateTime nextAlarm;
		LocalDateTime now;
		long interval = 60000; //One minute interval between polls
		while (!this.alarms.isEmpty()) {
			nextAlarm = DateHelper.getDateTime(alarms.pop());
			int second = 0;
			while (true) {
				now = LocalDateTime.now();
				if (nextAlarm.isBefore(now)) {
					System.out.println("HEI OG HOPP, PÅ TIDE Å STÅ OPP!");
					//TODO: Legge til dialogboks her
					break;
				}else {
					//System.out.println("Ikke ennå ..." + second++);
					try {
						Thread.sleep(interval);;
					} catch (InterruptedException e) {
						e.printStackTrace();
						break;
					}
				}
			}
		}
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
	
	public static void main(String [] args) throws Exception {
		
		//Client client = new Client();
		
		//Client.login("lol", "lol");
		
	}
}
