package control;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;



public class LoginController {
	
	MainApp mainApp;
	
	@FXML
	TextField txtUsername;
	
	@FXML
	TextField txtPassword;
	
	@FXML
	Label feedbackLabel;
	
	 public void setMainApp(MainApp mainApp) {
	        this.mainApp = mainApp;
		 }
	
	@FXML
	private void handleLogin() throws IOException, GeneralSecurityException {
		
		Client client = new Client();
		
		boolean correctLogin = Client.login(txtUsername.getText(), txtPassword.getText());
		
		if(correctLogin) {
			mainApp.loginSuccess();
			
			
			//NotificationListener notificationListener = new NotificationListener();
			//notificationListener.start();
			
			Client.getAppointmentList();
		}
		else {
			feedbackLabel.setVisible(true);
		}
	}
	
	@FXML
	public void handleEnterPressed(KeyEvent event) throws IOException, GeneralSecurityException {
	    if (event.getCode() == KeyCode.ENTER) {
	        handleLogin(); 
	    }
	}

}
