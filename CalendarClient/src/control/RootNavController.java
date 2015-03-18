package control;

import java.awt.MouseInfo;
import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
//
import javafx.scene.shape.SVGPath;

public class RootNavController {
	
	
	// SET MAINAPP
	MainApp mainApp;
	/*
	public RootNavController() {
	    Platform.runLater(new Runnable() {
	        @Override
	        public void run() {
	        	MainApp.rootController.setNotificationBold();
	        	MainApp.rootController.removeNotificationBold();
	        }
	    });
	}
	*/
	
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
	private void gotoAppointments() throws IOException {
		//Client.addNotification("test test test");
		System.out.println("Opening appointmentView..");
		mainApp.showAppointmentOverview();
	}
	
	@FXML
	private void gotoInvitations() {
		System.out.println("Opening InvitationsView..");
		mainApp.showInvitations();
	}
	
	@FXML
	private void gotoNotifications() throws IOException {
		System.out.println("Opening NotificationsView..");
		mainApp.showNotifications();
	}
	
	@FXML
	private void gotoUserManagement() {
		System.out.println("Opening UserManagementView..");
		mainApp.showUserManagement();
	}
	
	@FXML
	private void handleLogOut() {
		System.out.println("Logging out..");
		mainApp.logOut();
	}
	
	@FXML
	private void gotoGroupManagement() {
		System.out.println("Opening GroupOverview..");
		mainApp.showGroupOverview();
	}
	
	// NOTIFICATIONS
		
	@FXML 
	public Button btn_notifications;
	
	@FXML
	public SVGPath btnRefresh;
	
	@FXML
	public Button btnRefreshHitbox;
	
	public void removeNotificationBold() {
		btn_notifications.setStyle("-fx-font-weight: normal;");
		btn_notifications.setText("Varsler");
	}
	
	public void setNotificationBold() {
		btn_notifications.setStyle("-fx-font-weight: bold;");
		btn_notifications.setText("Nye varsler!");
	}
	
    public void handleRefresh() throws IOException {
    	String str = Client.hasNotifications().trim();
    	
    	if(str.equals("true")) {
    		setNotificationBold();
    	}
    	else {
    		removeNotificationBold();
    	}
    }
    
    @FXML
    Button btn_admin;
    
    // Check if usermanagement should be shown
    @FXML
    private void initialize() {
        if (Client.username.equals("admin")) {
            btn_admin.setVisible(true);  
        }
    }
 
    
    
}
