package control;

import java.io.IOException;
/**
 * 
 * Class for Adding admin account to empty user database.
 *
 */
public class AdminInstall {
	
	public static void main(String[] args) throws IOException {
		Client client = new Client();
		
		Client.addUser("admin", "Admin", "Adminsen", "admin");
	}

}
