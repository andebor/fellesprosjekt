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
			}
		}
		
		return "OK";
		
	}
	
}
