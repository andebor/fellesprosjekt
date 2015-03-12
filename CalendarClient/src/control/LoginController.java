package control;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;



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
			Client.getAppointmentList();
		}
		else {
			feedbackLabel.setVisible(true);
		}
	}

}
