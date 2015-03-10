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

import model.Appointment;

//


public class Client
{
	
	private Socket s;
	public static DataOutputStream outToServer;
	public static BufferedReader inFromServer;
	public static String username;
	
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
		return output;
	}
	
	public static boolean login(String username, String password) throws IOException{	
		
		

		String response = sendToServer("login " + username + " " + password);
		
		if (response.trim().equals("OK")) {
			System.out.println("ja");
			Client.username = username;
			return true;
		}
		else {
			System.out.println("nei");
			return false;
		}
	}
	
	public static String getAppointmentList() throws IOException {
	
		
		String response = sendToServer("getAppointmentList " + Client.username);
		return response;
		
	}
	
	public static boolean addAppointment(Appointment appointment) throws IOException {
		
		
		
		String response1 = sendToServer("addNewAppointment " + appointment.getDescription() + " " + appointment.getStart().toString() + " " + appointment.getFrom().toString() + " " 
				+ appointment.getPlace() + " " + appointment.getRoom() + " " + Client.username);
		
		// TODO add employees
		/**
		for (String employee : appointment.getUsers()){
			String response2 = sendToServer("addEmployeeToAppointment " +  )
		}
		*/
		
		return Boolean.valueOf(response1);
		
	}
	
	public static String deleteAppointment(String ID) throws IOException {
	
		
		String response = sendToServer("deleteAppointment " + ID + " " + username);
		return response;
		
	}
	
	public static boolean checkAppointmentOwnership(String ID) throws IOException {
		
		String response = sendToServer("checkAppointmentOwnership " + ID + " " + username);
		return Boolean.valueOf(response);
		
		
	}
	
	
	
   public static void main(String [] args) throws Exception {
	   
	   //Client client = new Client();
	   
	   //Client.login("lol", "lol");
	   
	   
   }
	   
   }