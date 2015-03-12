package control;

import java.io.IOException;

import javafx.application.Platform;
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
	private void handleLogin() throws IOException {
		
		Client client = new Client();
		
		boolean correctLogin = Client.login(txtUsername.getText(), txtPassword.getText());
		
//		mainApp.initRootNav();
		if(correctLogin) {
			mainApp.loginSuccess();
			
			
			NotificationListener notificationListener = new NotificationListener();
			notificationListener.start();
			
			Client.getAppointmentList();
		}
		else {
			feedbackLabel.setVisible(true);
		}
//		mainApp.showAppointmentOverview();
	}

}
