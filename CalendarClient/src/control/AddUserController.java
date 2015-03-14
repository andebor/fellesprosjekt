package control;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddUserController {
	
	@FXML
    Label missingfields_label;
    
    @FXML
    TextField username_field, firstname_field, lastname_field, pwd_field;
    
	MainApp mainApp;
	Stage dialogStage;
	
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
    public void setDialogStage(Stage dialogStage) {
    	this.dialogStage = dialogStage;
    }
        
    @FXML
    private void handleConfirmAdd() throws IOException {
    	if (valiidUserInput()) {
    		UserManagementController.addUser(username_field.getText(), firstname_field.getText(), lastname_field.getText(), pwd_field.getText());
    		dialogStage.close();
    	}else{
    		pwd_field.setVisible(true);
    		System.out.println("Not valid input.");
    	}
    }
    
    @FXML
    private void handleCancelAdd() {
    	System.out.println("Closing dialog..");
    	dialogStage.close();			
    }
    
    private boolean valiidUserInput() {
    	if (username_field.getText() == null || username_field.getText().length() == 0) {
    		return false;
    	}
    	if (firstname_field.getText() == null || firstname_field.getText().length() == 0) {
    		return false;
    	}
    	if (lastname_field.getText() == null || lastname_field.getText().length() == 0) {
    		return false;
    	}
    	if (pwd_field.getText() == null || pwd_field.getText().length() == 0) {
    		return false;
    	}else{
    		return true;
    	}
    }

}
