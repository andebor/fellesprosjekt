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
				System.out.println("CLASS:ServerProtocol - Trying login..");
				String username = input[1];
				byte[] salt = database.getSalt(username);
				char[] typedPwd = input[2].toCharArray();
				
				if (salt != null) {
					byte[] hashedPwd = Security.hashPassword(typedPwd, salt);
					byte[] dbPwd = Security.stringtoByte(database.getPassword(username));
					
					if (Security.matches(hashedPwd, dbPwd)) {
						return "OK";
					}
				} else {
					return "NOK";
				}
						
				
//				String password = database.getPassword(username);
//				
//				if(password.equals(input[2])) {
//					return "OK";
//				}
//				else {
//					return "NOK";
//				}
			
				
			case "GETAPPOINTMENTLIST":
				
				String response = database.getAppointments(input[1]);
				System.out.println("HEEER: " + response);
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
				Boolean response2 = database.addNewAppointment(description, startTime, endTime, location, meetingRoom, owner);
				int response3 = database.getLatestAddition("avtale", "avtaleID");
				return String.valueOf(response3);
				
			case "DELETEAPPOINTMENT":
				
				if(database.getEmpno(input[2])==database.getAppointmentOwner(Integer.parseInt(input[1]))){
					System.out.println("appointment ID: " + input[1]);
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
				
				byte[] newsalt = Security.generateSalt();
				char[] pwd = passWord.toCharArray();
				byte[] hashedPwd = Security.hashPassword(pwd, newsalt);
				String encodedPwd = Security.bytetoString(hashedPwd);
				
				String addResponse = database.addUser(userName, firstName, lastName, encodedPwd, newsalt);
				return addResponse;

			case "CHECKAPPOINTMENTOWNERSHIP":
				//
				if(database.getEmpno(input[2])==database.getAppointmentOwner(Integer.parseInt(input[1]))){
					return "true";
				}
				
				else {
					return "false";
				}
				
			case "EDITAPPOINTMENT":
				
				
			case "GETEMPLOYEES":
				
				return database.getEmployees();
				
			case "GETROOMS":
				
				return database.getMeetingRooms("#/#", "/@/");
				
			case "UPDATEAPPOINTMENT":
				
				String ID = input[1];
				String descriptionn = input[2];
				String start = input[3];
				String end = input[4];
				String date = input[5];
				String place = input[6];
				String room = input[7];
				database.updateAppointmentString(Integer.parseInt(ID), "formål", descriptionn);
				database.updateAppointmentString(Integer.parseInt(ID), "starttid", date + " " + start + ":00.0");
				database.updateAppointmentString(Integer.parseInt(ID), "sluttid", date + " " + end + ":00.0");
				database.updateAppointmentString(Integer.parseInt(ID), "sted", place);

				
				if(room == null || !place.equals("null")){
					
					database.updateAppointmentInteger(Integer.parseInt(ID), "møteromNR", null);
					}
				else {
					database.updateAppointmentInteger(Integer.parseInt(ID), "møteromNR", Integer.parseInt(room));
				}
				
			case "ADDEMPLOYEETOAPPOINTMENT":
				
				String empNo = input[1];
				String appID = input[2];
				Boolean response5 = database.addEmployeeToAppointment(Integer.parseInt(appID), Integer.parseInt(empNo));
				return response5.toString();
				
				
			case "HASNOTIFICATIONS":
				
				return "lol";
				
			}
			
		} //Closing bracket for switch statement

		
		return "OK";
		
	}
}
