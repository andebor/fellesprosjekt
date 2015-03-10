package server;

import db.DBConnect;

//

public class ServerProtocol {

	public String processInput(String rawInput) throws Exception{
		
		DBConnect database = new DBConnect();
		
		if(rawInput != null){
			System.out.println(rawInput);
			String[] input = rawInput.split("#%");
			
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
			
			case "ADDNEWAPPOINTMENT":
				String description = input[1];
				String startTime = input[4] + " " + input[2] + ":00";
				String endTime = input[4] + " " + input[3] + ":00";
				String location = input[5];
				String meetingRoom = input[6];
				String owner = String.valueOf(database.getEmpno(input[7]));
				
				System.out.println("description: " + description);
				System.out.println("startTime: " + startTime);
				System.out.println("endTime: " + endTime);
				System.out.println("location: " + location);
				System.out.println("meetingRoom: " + meetingRoom);
				System.out.println("owner: " + owner);
				
				System.out.println("database.addNewAppointment(" + description + ", " + startTime + ", " + endTime + ", " + location + ", " + meetingRoom + ", " + owner + ")");
				Boolean response2 = database.addNewAppointment(description, startTime, endTime, location, null, owner);

				return String.valueOf(response2);
				
			case "DELETEAPPOINTMENT":
				
				if(database.getEmpno(input[2])==database.getAppointmentOwner(Integer.parseInt(input[1]))){
					boolean response1 = database.removeAppointment(Integer.parseInt(input[1]));
					return String.valueOf(response1);
				}
				
				else {
					return "user is not appointment owner";
				}
				
			case "ADDNEWUSER":
				String userName = input[1];
				String firstName = input[2];
				String lastName = input[3];
				String passWord = input[4];
				
				String response3 = database.addUser(userName, firstName, lastName, passWord);
				return response3;

			case "CHECKAPPOINTMENTOWNERSHIP":
				//
				if(database.getEmpno(input[2])==database.getAppointmentOwner(Integer.parseInt(input[1]))){
					return "true";
				}
				
				else {
					return "false";
				}
				
			case "EDITAPPOINTMENT":
				
	
				}
		}
		
		return "OK";
		
	}
	
}
