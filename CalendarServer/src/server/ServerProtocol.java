package server;

//

public class ServerProtocol {

	public String processInput(String rawInput) throws Exception{
		if(rawInput != null){
			System.out.println(rawInput);
			String[] input = rawInput.split(" ");
			
		}
		
		return "OK";
		
	}
	
}
