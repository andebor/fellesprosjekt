package control;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class RootNavController {
	
	// SET MAINAPP
	MainApp mainApp;
	
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
	
    // MAIN NAVIGATION
    
    @FXML
	private void gotoCalendar() {
    	System.out.println("Opening calendarView..");
		mainApp.showCalendar();
	}
	
	@FXML
	private void gotoAppointments() {
		System.out.println("Opening appointmentView..");
		mainApp.showAppointmentOverview();
	}
	
	@FXML
	private void gotoInvitations() {
		System.out.println("Opening InvitationsView..");
		mainApp.showInvitations();
	}
	
	@FXML
	private void gotoNotifications() {
		System.out.println("Opening NotificationsView..");
		mainApp.showNotifications();
	}
	
	@FXML
	private void gotoUserManagement() {
		System.out.println("Opening UserManagementView..");
		mainApp.showUserManagement();
	}
	
	// NOTIFICATIONS
		
	@FXML private Button btn_notifications;
	
	public void removeNotificationBold() {
		btn_notifications.setStyle("-fx-font-weight: normal;");
		btn_notifications.setText("Varsler");
	}
	
	public void setNotificationBold() {
		btn_notifications.setStyle("-fx-font-weight: bold;");
		btn_notifications.setText("Nye varsler!");
	}
}
