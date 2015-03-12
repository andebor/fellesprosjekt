package control;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import javafx.fxml.FXML;

public class UserManagementController {
	
	MainApp mainApp;
	
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
    @FXML
    private void testAddUser() throws IOException, GeneralSecurityException {
//		addUser("andebor", "Anders", "Borud", "andebor");
//		addUser("vigleikl", "Vigleik", "Lund", "vigleikl");
//		addUser("mariusmb", "Marius", "Bang", "mariusmb");
//		addUser("alfredb", "Alfred", "Birketvedt", "alfredb");
//		addUser("lars", "Lars", "Larsen", "lars");
//    	changeUserPass("admin", "admin");
//    	changeUserPass("vigleikl", "vigleikl");
//    	changeUserPass("mariusmb", "mariusmb");
//    	changeUserPass("alfredb", "alfredb");
//    	changeUserPass("andebor", "andebor");
    }
    
    private void addUser(String username, String firstName, String lastName,String password) throws IOException {
    	String response = Client.addUser(username, firstName, lastName, password);
    	System.out.println(response);
    }  
    
    private void changeUserPass(String username, String password) throws IOException {
    	String response = Client.changeUserPass(username, password);
    	System.out.println(response);
    }

}
