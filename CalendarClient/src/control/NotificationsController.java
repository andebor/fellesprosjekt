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
    
    @FXML
    TableColumn<Notification, String> timeColumn;
    
    
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
        
        timeColumn.setCellValueFactory(
        		cellData -> cellData.getValue().dateProperty());


        
        
        // Clear person details.
        //showAppointmentDetails(null);

    }
    
    
    @FXML
    private void initNotificationTable() throws IOException {

    	String str = Client.getNewNotifications();    	
    	
    	if(str.equals("null")){
    		str = "";
    	}
    	
    	String[] notiStrings = str.split(Pattern.quote("\n\n"));
 	
    	
    	for(int i = 0; i < notiStrings.length; i++) {
        	if(i == 0 && notiStrings[0].length() < 2) {
        		continue; //Dirtyfix
        	}
    		addNotification(notiStrings[i]);
    	}
    	
    	notificationTable.setItems(notificationList);
    	
    	for(int i = 0; i < notificationList.size(); i++) {
    		System.out.println("zzz: " + notificationList.get(i));
    	}
    	
    	Client.flagAllNotificationsAsSeen();
  
    }
    
    

    public void addNotification(String str) {
    	Notification notification = new Notification();
    	
    	System.out.println("NOTIFICATION " + str);
    	
    	String[] z = str.split(Pattern.quote("\n"));
    	
    	
    	String date = z[1].substring(8);
    	String desc = z[2].substring(8) + "\n";
    	for(int i = 3; i<z.length; i++){
    		desc+= z[i] + "\n";
    	}
    	
    	
    	
    	
    	notification.setDate(date);
    	notification.setDescription(desc);

    	
    	
    	
    	notificationList.add(notification);
    	
    }
    
    
    

}



