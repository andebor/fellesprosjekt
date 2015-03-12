package control;


import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


import java.util.regex.Pattern;

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
	@FXML
	Label deltagereLabel;
	
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
    
    public void initAppointmetTable() throws IOException{
    	
    	// Using generateExampleAppointment for testing. Need a new method for retrieving appointment-list from database
    	//appointmentList = generateExampleAppointment();
    	
    	appointmentList.clear();
    	
    	
    	
    	String str = Client.getAppointmentList();
    	
    	System.out.println("hentet avtaler");
    	System.out.println(str);
    	
    	
    	String[] appStrings = str.split(Pattern.quote("$%"));
    	
    	/*
    	for(int i = 0; i < appStrings.length; i++) {
    		System.out.println(appStrings[i] + "\n");
    	}
    	*/
    	
    	
    	for(int i = 0; i < appStrings.length; i++) {
    		addAppointment(appStrings[i]);
    	}
    	
    	
    	
    	
    	appointmentTable.setItems(appointmentList);
    	
    }
    
    public void addAppointment(String str) {
    	Appointment appointment = new Appointment();
    	

    	String[] z = str.split(Pattern.quote("%$"));
    	
    	//System.out.println("LENGDE: " + z.length);
    	
    	appointment.setDescription(z[0]);
    	
    	
    	
    	String[] startDate = z[1].split(" ");
    	
    	String dato = startDate[0];
    	String[] datoList = dato.split("-");
    	
    	String[] endDate = z[2].split(" ");
    	
    	String startTid = startDate[1];
    	String[] startTidList = startTid.split(":");
    	
    	String endTid = endDate[1];
    	String[] endTidList = startTid.split(":");
    	
    	
    	String[] deltagere = z[7].split("@/@");
    	
    	ObservableList<String> usersList = FXCollections.observableArrayList();
    	
    	for(int i = 0; i < deltagere.length; i++) {
    		usersList.add(deltagere[i]);
    	}
    	
    	
    	appointment.setDate(LocalDate.of(Integer.parseInt(datoList[0]), Integer.parseInt(datoList[1]), Integer.parseInt(datoList[2])));
    	appointment.setStart(LocalTime.of(Integer.parseInt(startTidList[0]), Integer.parseInt(startTidList[1])));
    	appointment.setFrom(LocalTime.of(11,30));
    	appointment.setUsers(usersList);
    	appointment.setRoomAmount(2);
    	appointment.setID(z[6]);
    	
    	if(z[4].equals("null")){
    		appointment.setPlace(z[3]);
    		appointment.setRoom("null");
    	}
    	else {
    		appointment.setRoom(z[4]);
    		appointment.setPlace("null");
    	}
    	
    	appointmentList.add(appointment);
    	
    }
    
    public static ObservableList<Appointment> getAppointmentList() {
    	return appointmentList;
    }
    
    public Appointment generateExampleAppointment() {
    	// Just for testing functionality 
    	Appointment appointment = new Appointment();
    	appointment.setDescription("test");
    	appointment.setDate(LocalDate.of(2015,12,12));
    	appointment.setStart(LocalTime.of(10,30));
    	appointment.setFrom(LocalTime.of(11,30));
    	ObservableList<String> list2 = FXCollections.observableArrayList("Ole", "Ansatt 1");
    	appointment.setUsers(list2);
    	appointment.setRoom("Grouproom 1");
    	appointment.setRoomAmount(2);
    	return appointment;
    	
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
    		List<String> users = appointment.getUsers();
    		
    		String listString = "";

    		for (String s : users)
    		{
    		    listString += s + "\n";
    		}
    		
    		deltagereLabel.setText(listString);
    		
    	} else {
    		// Person is null, remove all the text.
    		beskrivelseLabel.setText("");
    		datoLabel.setText("");
    		tidspunktLabel.setText("");
    		datoLabel.setText("");
    		stedLabel.setText("");
    		moteromLabel.setText("");
    		deltagereLabel.setText("");
    	}
    }
	
	
	@FXML
	private void handleNewAppointment() {
		mainApp.showNewAppointment(null);
		
	}
	
	 public void setMainApp(MainApp mainApp) throws IOException {
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
			if(Client.checkAppointmentOwnership(appointment.getID()).equals("true")){
				mainApp.showNewAppointment(appointment);
			}
		}
		catch(Exception e){
			return;
		}
	}
	
	@FXML
	private void handleDelete() throws IOException {
		Appointment appointment = appointmentTable.getSelectionModel().getSelectedItem();
		if(appointment == null){ // Check if any appointment is selected from list
			return;
		}
		else {
			Client.deleteAppointment(appointment.getID());
			getAppointmentList().remove(appointment);
			initAppointmetTable();
		}
		
	}


}