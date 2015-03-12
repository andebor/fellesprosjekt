package control;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.regex.Pattern;

import model.Appointment;
import model.Notification;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class NotificationsController {
	
	MainApp mainApp;
	
    @FXML
    TableView<Notification> notificationTable;
    
    @FXML
    TableColumn<Notification, String> descriptionColumn;
	
    public void setMainApp(MainApp mainApp) throws IOException {
        this.mainApp = mainApp;
        initNotificationTable();
    }
    
    public static ObservableList<Notification> notificationList = FXCollections.observableArrayList(); // Currently list over appointments in appointmentTable

    @FXML
    private void initialize() {

    	
    	// Initialize the person table with the two columns.
    	
        descriptionColumn.setCellValueFactory(
        		cellData -> cellData.getValue().descriptionProperty());

        
        
        // Clear person details.
        //showAppointmentDetails(null);

    }
    
    
    @FXML
    private void initNotificationTable() throws IOException {

    	String str = Client.getNewNotifications();
    	
    	String[] notiStrings = str.split(Pattern.quote("\n\n"));
    	
    
    	
    	for(int i = 0; i < notiStrings.length; i++) {
    		addNotification(notiStrings[i]);
    	}
    	
    	notificationTable.setItems(notificationList);
    	
    	Client.flagAllNotificationsAsSeen();
  
    }
    
    

    public void addNotification(String str) {
    	Notification notification = new Notification();
    	

    	String[] z = str.split(Pattern.quote("\n"));
    	
    	
    	notification.setDescription(z[1]);
    	
    	
    	
    	notificationList.add(notification);
    	
    }
    
    
    

}



