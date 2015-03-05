package control;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

import model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;


public class CalendarController {
	
 
    @FXML
    GridPane calendarGridPane;
    

	MainApp mainApp;
	
	public void setMainApp(MainApp mainApp) {
	        this.mainApp = mainApp;
	}

    @FXML
    private void initialize() {
    	
		generateCalendar(generateExampleAppointment());


	}
	
	public void generateCalendar(Appointment appointment){
		
		//Find column
		int column = appointment.getDate().getDayOfWeek().getValue();
		
		//Find rows
		int start = appointment.getStart().getHour();
		if(start<8){
			start = 0;
		}
		else{
			start = start - 6;
		}
		int end = appointment.getFrom().getHour();
		if(end<8){
			end = 0;
		}
		else{
			end = end - 6;
		}
		
		// Generate appointment in calendar
		for(int row = start; row<=end; row++){
			Label appointmentLabel = new Label(appointment.getDescription());
			appointmentLabel.setTranslateX(30);
			calendarGridPane.add(appointmentLabel, column, row);
		}	
	}
	
    public Appointment generateExampleAppointment() {
    	// Just for testing functionality 
    	Appointment appointment = new Appointment();
    	appointment.setDescription("test");
    	appointment.setDate(LocalDate.of(2015,12,12));
    	appointment.setStart(LocalTime.of(9,00));
    	appointment.setFrom(LocalTime.of(15,00));
    	ObservableList<String> list2 = FXCollections.observableArrayList("Ole", "Ansatt 1");
    	appointment.setUsers(list2);
    	appointment.setRoom("Grouproom 1");
    	appointment.setRoomAmount(2);
    	return appointment;

    	
    }
	

}
