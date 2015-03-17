package control;

import javafx.fxml.FXML;

public class GroupManagementController {
	
	MainApp mainApp;
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	
	@FXML
	private void handleAddUser() {
		System.out.println("Opening dialog to select user..");
	}
	
	@FXML
	private void handleRemoveUser() {
		System.out.println("Removing selected user from selected group");
	}

}
