package control;


import java.time.LocalDate;
import java.time.LocalTime;
//asd
import model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;

public class AppointmentOverviewController {

    @FXML
    TableView<Appointment> appointmentTable;
    @FXML
    TableColumn<Appointment, String> avtaleColumn;
    @FXML
    TableColumn<Appointment, String> datoColumn;
	
	@FXML
	Label beskrivelseLabel;
	@FXML
	Label datoLabel;
	@FXML
	Label tidspunktLabel;
	@FXML
	Label stedLabel;
	@FXML
	Label moteromLabel;
	
	MainApp mainApp;
	
	public static ObservableList<Appointment> appointmentList = FXCollections.observableArrayList(); // Currently list over appointments in appointmentTable

    @FXML
    private void initialize() {

    	
    	// Initialize the person table with the two columns.
    	
        avtaleColumn.setCellValueFactory(
        		cellData -> cellData.getValue().descriptionProperty());
        datoColumn.setCellValueFactory(
        		cellData -> cellData.getValue().dateProperty());
        
        
        // Clear person details.
        showAppointmentDetails(null);

        // Listen for selection changes and show the person details when changed.
		appointmentTable.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> showAppointmentDetails(newValue));
    }
    
    public static ObservableList<Appointment> getAppointmentList() {
    	return appointmentList;
    }
    
    public ObservableList<Appointment> generateExampleAppointment() {
    	// Just for testing functionality 
    	Appointment appointment = new Appointment();
    	appointment.setDescription("test");
    	appointment.setDate(LocalDate.of(2015,12,12));
    	appointment.setPlace("sted");
    	appointment.setStart(LocalTime.of(10,30));
    	appointment.setFrom(LocalTime.of(11,30));
    	ObservableList<String> list2 = FXCollections.observableArrayList("Ole", "Ansatt 1");
    	appointment.setUsers(list2);
    	appointment.setRoom("R123");
    	ObservableList<Appointment> list = FXCollections.observableArrayList();
    	list.add(appointment);
    	return list;
    	
    }
    
    
    public void initAppointmetTable(){
    	
    	// Using generateExampleAppointment for testing. Need a new method for retrieving appointment-list from database
    	appointmentList = generateExampleAppointment(); 
    	appointmentTable.setItems(appointmentList);
    	
    	
    }
	

    
    private void showAppointmentDetails(Appointment appointment) {
    	if (appointment != null) {
    		// Fill the labels with info from the person object.
 
    		beskrivelseLabel.setText(appointment.getDescription());
    		datoLabel.setText(appointment.getDate().toString());
    		tidspunktLabel.setText(appointment.getStart().toString() + " til "  + appointment.getFrom().toString());
    		if(appointment.getPlace()!=null){
    		stedLabel.setText(appointment.getPlace());
    		}
    		if(appointment.getRoom()!=null){
    		moteromLabel.setText(appointment.getRoom());
    		}
    		
    	} else {
    		// Person is null, remove all the text.
    		beskrivelseLabel.setText("");
    		datoLabel.setText("");
    		tidspunktLabel.setText("");
    		datoLabel.setText("");
    		stedLabel.setText("");
    		moteromLabel.setText("");
    	}
    }
	
	
	@FXML
	private void handleNewAppointment() {
		mainApp.showNewAppointment(null);
		
	}
	
	 public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        initAppointmetTable();
	 }

	
	@FXML
	private void handleEditAppointment() {
		try {
			Appointment appointment = appointmentTable.getSelectionModel().getSelectedItem();
			if(appointment == null){ // Check if any appointment is selected from list
				return;
			}
			mainApp.showNewAppointment(appointment);
		}
		catch(Exception e){
			return;
		}
	}
	
	@FXML
	private void handleDelete() {
		Appointment appointment = appointmentTable.getSelectionModel().getSelectedItem();
		if(appointment == null){ // Check if any appointment is selected from list
			return;
		}
		else {
			getAppointmentList().remove(appointment);
		}
		
	}


}