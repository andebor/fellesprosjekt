package control;


import java.time.LocalDate;
import java.time.LocalTime;

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
	
	private ObservableList<Appointment> appointmentList = FXCollections.observableArrayList(); // Currently list over appointments in appointmentTable

    @FXML
    private void initialize() {
    	initAppointmetTable();
    	
    	//row selection
    	appointmentTable.setRowFactory( tv -> {
    	    TableRow<Appointment> row = new TableRow<>();
    	    row.setOnMouseClicked(event -> {
    	        if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
    	            Appointment rowData = row.getItem();
    	            showPersonDetails(rowData);
    	            
    	        }
    	    });
    	    return row;
    	});
    	
    	// Initialize the person table with the two columns.
    	
        avtaleColumn.setCellValueFactory(
        		cellData -> cellData.getValue().beskrivelseProperty());
        datoColumn.setCellValueFactory(
        		cellData -> cellData.getValue().datoProperty());
        
        
        // Clear person details.
        showPersonDetails(null);

        // Listen for selection changes and show the person details when changed.
		//appointmentTable.getSelectionModel().selectedItemProperty().addListener(
				//(observable, oldValue, newValue) -> showPersonDetails(newValue));
    }
    
    public ObservableList<Appointment> generateExampleAppointment() {
    	// Just for testing functionality 
    	Appointment appointment = new Appointment();
    	appointment.setDescription("test");
    	appointment.setDate(LocalDate.of(2015,12,12));
    	appointment.setPlace("sted");
    	appointment.setStart(LocalTime.of(10,30));
    	appointment.setFrom(LocalTime.of(11,30));
    	ObservableList<String> list2 = FXCollections.observableArrayList("Ole");
    	appointment.setUsers(list2);
    	ObservableList<Appointment> list = FXCollections.observableArrayList();
    	list.add(appointment);
    	return list;
    	
    }
    
    
    public void initAppointmetTable(){
    	
    	// Using generateExampleAppointment for testing. Need a new method for retrieving appointment-list from database
    	appointmentList = generateExampleAppointment(); 
    	appointmentTable.setItems(appointmentList);
    	
    	
    }
	

    
    private void showPersonDetails(Appointment appointment) {
    	if (appointment != null) {
    		// Fill the labels with info from the person object.
 
    		beskrivelseLabel.setText(appointment.getDescription());
    		datoLabel.setText(appointment.getDate().toString());
    	} else {
    		// Person is null, remove all the text.
    		beskrivelseLabel.setText("");
    		datoLabel.setText("");
    	}
    }
	
	
	@FXML
	private void handleNewAppointment() {
		mainApp.showNewAppointment(null);
	}
	
	 public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        //appointmentTable.setItems(mainApp.getAppointmentList());
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


}
	


