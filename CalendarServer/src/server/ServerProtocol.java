package server;


import java.util.Stack;

import db.DBConnect;

//

public class ServerProtocol {
	
	public DBConnect database;
	
	public ServerProtocol() {
		database = new DBConnect();
	}

	public String processInput(String rawInput) throws Exception{
		
	
		if(rawInput != null){
			System.out.println(rawInput);
			String[] input = rawInput.split("#%");
			
			switch(input[0].toUpperCase()){
			
			case "SYNCCHECK":
				
				return "CHECK";
			
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
				if(location.equals("null")){
					location = null;
				}
				if(meetingRoom.equals("null")){
					meetingRoom = null;
				}
				
				System.out.println("description: " + description);
				System.out.println("startTime: " + startTime);
				System.out.println("endTime: " + endTime);
				System.out.println("location: " + location);
				System.out.println("meetingRoom: " + meetingRoom);
				System.out.println("owner: " + owner);
				
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
				
			case "SETALARM":
				
				return String.valueOf(database.setAlarm(Integer.parseInt(input[2]), database.getEmpno(input[1]), Integer.parseInt(input[3])));
				
			case "HIDEAPPOINTMENT":
				
				
				return String.valueOf(database.hideAppointment(Integer.parseInt(input[1]), Integer.parseInt(input[2])));
				
				
			case "GETALARMS":
				
				return database.getAlarmsAsString(database.getEmpno(input[1]));
			
			case "GETEMPLOYEES":
				
				return database.getEmployees();
				
			case "REMOVEEMPLOYEE":
				
				String empNo1 = input[1];
				String appID1 = input[2];
				Boolean response8 = database.removeEmployeeFromAppointment(Integer.parseInt(appID1), Integer.parseInt(empNo1));
				return response8.toString();
				
			case "GETROOMS":
				
				return database.getAvailableRooms(input[4] + " " + input[1]+":00.0", input[4] + " " + input[2]+"00.0", Integer.parseInt(input[3]));
				
			case "UPDATEAPPOINTMENT":
				
				String ID = input[1];
				String descriptionn = input[2];
				String start = input[3];
				String end = input[4];
				String date = input[5];
				String place = input[6];
				String room = input[7];
				String editRoom = input[8];
				if(!descriptionn.equals("null")){
				database.updateAppointmentString(Integer.parseInt(ID), "formål", descriptionn);
				}
				if(!start.equals("null")){
				database.updateAppointmentString(Integer.parseInt(ID), "starttid", date + " " + start + ":00.0");
				}
				if(!end.equals("null")){
				database.updateAppointmentString(Integer.parseInt(ID), "sluttid", date + " " + end + ":00.0");
				}
				if(!place.equals("null")){
				database.updateAppointmentString(Integer.parseInt(ID), "sted", place);
				database.updateAppointmentInteger(Integer.parseInt(ID), "møteromNR", null);
				
				}
				if(editRoom.equals("YES")){
					database.updateAppointmentInteger(Integer.parseInt(ID), "møteromNR", Integer.parseInt(room));
					database.updateAppointmentString(Integer.parseInt(ID), "sted", null);

				}
				
				
			case "ADDEMPLOYEETOAPPOINTMENT":
				
				String empNo = input[1];
				String appID = input[2];
				Boolean response5 = database.addEmployeeToAppointment(Integer.parseInt(appID), Integer.parseInt(empNo));
				return response5.toString();
				
			case "SETNEWPASSWORD":
				String UserName = input[1];
				String newpassword = input[2];
				
				byte[] newSalt = Security.generateSalt();
				char[] newpwd = newpassword.toCharArray();
				byte[] hashedNewPwd = Security.hashPassword(newpwd, newSalt);
				String encodedNewPwd = Security.bytetoString(hashedNewPwd);
				
				String changeResponse = database.setNewPass(UserName, encodedNewPwd, newSalt);
				return changeResponse;
				
			case "HASNOTIFICATIONS":
				int empNo2 = database.getEmpno(input[1]);
				
				Boolean response6 = database.hasNewNotifications(empNo2);
				
				if(response6 == false) {
					return "ingenVarlser";
				}
				
				return response6.toString();
				
			case "FLAGALLNOTIFICATIONSASSEEN":
				int empNo3 = database.getEmpno(input[1]);
				
				Boolean response7 = database.flagAllNotificationsAsSeen(empNo3);
				
				return response7.toString();
				
			case "GETNEWNOTIFICATIONS":
				int empNo4 = database.getEmpno(input[1]);
				
				return database.getNewNotifications(empNo4);
				
	
			case "GETUSER":
				
				return database.getEmployeeName(Integer.parseInt(input[1]));
				
			case "ISINVITEDEMPLOYEE":
				int empNom = database.getEmpno(input[2]);
				return String.valueOf(database.isInvitedEmployee(Integer.parseInt(input[1]), empNom));
				
			case "CHANGESTATUS":
				int appointmentID = Integer.parseInt(input[1]);
				int empNom1 = database.getEmpno(input[2]);
				int selection = Integer.parseInt(input[3]);
				return String.valueOf(database.changeStatus(appointmentID, empNom1, selection));
				
			case "ADDNOTIFICATION":
				int empNo5 = Integer.parseInt(input[1]);
				String msg = input[2];
				
				Boolean response9 = database.addNotification(empNo5, msg);
				
				return response9.toString();
				
			case "DELETEUSER":
				String userToDelete = input[1];
				
				String deleteResponse = database.deleteUser(userToDelete);
				
				return deleteResponse;
			
			
			case "GETAPPOINTMENTEXCLUSIVE":
				
				return database.getAppointmentsExclusive(input[1]);
				
			case "GETUSERID":
				
				return String.valueOf(database.getEmpno(input[1]));
				
			case "FIRENOTIFICATION":
				
				return String.valueOf(database.fireNotification());
				
			case "GETGROUPS":
				
				return database.getGroups();
				
			case "GETGROUP":
				
				Stack<Integer> employers = database.getGroup(Integer.parseInt(input[1]));
				String responseEmp = "";
				for(Integer emp : employers){
					responseEmp+=database.getEmployeeName(emp) + " " + String.valueOf(emp) + "%&%";
				}
				responseEmp = responseEmp.substring(0, responseEmp.length()-3);
				return responseEmp;
	
			case "ADDMEMBER":
				
				Boolean addMemberResponse =  database.addMember(Integer.parseInt(input[1]), Integer.parseInt(input[2]));
				
				return addMemberResponse.toString();
				
			case "REMOVEMEMBER":
				
				Boolean removeMemberResponse = database.removeMember(Integer.parseInt(input[1]), Integer.parseInt(input[2]));
				
				return removeMemberResponse.toString();
			
			case "GETMYGROUPS":
				
				String myGroupsResponse = database.getMyGroups(input[1]);
				
				return myGroupsResponse;
				
			case "GETNAME":
				
				
				int empno6 = database.getEmpno(input[1]);
				
				String getNameResponse = database.getEmployeeName(empno6);
	
				
				return getNameResponse;
			
			case "NEWALARMS":
				
				String alarm = input[1];
				int empNo7 = Integer.parseInt(input[2]);
				return database.hasEarlierAlarms(alarm, empNo7) ? "TRUE" : "FALSE";
			
			case "GETEMPNO":
				String user = input[1];
				int empNo8 = database.getEmpno(user);
				return String.valueOf(empNo8);
			}
			
			
		} //Closing bracket for switch statement

		//database.close();
		return "OK";
		
	}
	
}
