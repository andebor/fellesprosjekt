package server;

import db.DBConnect;

//

public class ServerProtocol {

	public String processInput(String rawInput) throws Exception{
		
		DBConnect database = new DBConnect();
		
		if(rawInput != null){
			System.out.println(rawInput);
			String[] input = rawInput.split(" ");
			
			switch(input[0].toUpperCase()){
			case "LOGIN":
				
				String username = input[1];
				String password = database.getPassword(username);
				
				if(password.equals(input[2])) {
					return "OK";
				}
				else {
					return "NOK";
				}
			//
				
			case "GETAPPOINTMENTLIST":
				
				String response = database.getAppointments(input[1]);
				return response;
			
			case "ADDNEWAPOINTMENT":
				
				String description = input[1];
				String startTime = input[2];
				String endTime = input[3];
				String location = input[4];
				String meetingRoom = input[5];
				String owner = input[6];
				
				//String response = database.addNewAppointment(description, startTime, endTime, location, meetingRoom, owner);
				//return response;
				return "";
				
			case "DELETEAPPOINTMENT":
				
				if(database.getEmpno(input[2])==database.getAppointmentOwner(Integer.parseInt(input[1]))){
					boolean response1 = database.removeAppointment(Integer.parseInt(input[1]));
					return String.valueOf(response1);
				}
				
				else {
					return "user is not appointment owner";
				}
			}
				
		}
		
		return "OK";
		
	}
	
}
