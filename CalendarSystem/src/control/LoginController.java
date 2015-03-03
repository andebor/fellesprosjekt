package control;

import javafx.fxml.FXML;



public class LoginController {
	
	MainApp mainApp;
	
	 public void setMainApp(MainApp mainApp) {
	        this.mainApp = mainApp;
		 }
	
	@FXML
	private void handleLogin() {
		System.out.println("Her okja");
//		mainApp.initRootNav();
		mainApp.loginSuccess();
//		mainApp.showAppointmentOverview();
	}

}
