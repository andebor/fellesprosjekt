package control;

import javafx.fxml.FXML;



public class LoginController {
	
	MainApp mainApp;
	
	 public void setMainApp(MainApp mainApp) {
	        this.mainApp = mainApp;
		 }
	
	@FXML
	private void handleLogin() {
		mainApp.showAppointmentOverview();
	}

}
