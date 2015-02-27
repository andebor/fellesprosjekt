package control;


import model.Appointment;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
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
	Label moteromLable;
	
	MainApp mainApp;
	


    @FXML
    private void initialize() {
    	stedLabel.setText("test");
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
	
    
    private void showPersonDetails(Appointment appointment) {
    	if (appointment != null) {
    		// Fill the labels with info from the person object.
    		beskrivelseLabel.setText(appointment.getBeskrivelse());
    		datoLabel.setText(appointment.getDato());
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
		mainApp.showNewAppointment(appointmentTable.getSelectionModel().getSelectedItem());
	}
}
	


