package control;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class AppointmentOverviewController {

    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private TableColumn<Appointment, String> avtaleColumn;
    @FXML
    private TableColumn<Appointment, String> datoColumn;	
	
	@FXML
	private Label beskrivelseLable;
	@FXML
	private Label datoLable;
	@FXML
	private Label tidspunktLable;
	@FXML
	private Label stedLable;
	@FXML
	private Label moteromLable;
	
	private MainApp mainApp;
	
	public AppointmentOverviewController() {
		
	}
	
	@FXML
	private void handleNewAppointment() {
		mainApp.showNewAppointment(null);
	}
	
	 public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        //personTable.setItems(mainApp.getPersonData());
	 }
}
