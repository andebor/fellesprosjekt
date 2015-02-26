package control;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;


public class editAppointmentController extends newAppointmentController implements Initializable {

	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		editNewAppointment = true;
	}
	
	public void initAppointment(Appointment app){
		
		this.appointmentToEdit = app;
		
	}
	
	public void loadAppointment(){
		
		super.description.setText(appointmentToEdit.getDescription());
		super.place.setText(appointmentToEdit.getPlace());
		super.date.setValue(appointmentToEdit.getDate());
		super.startHours.setText(Integer.toString(appointmentToEdit.getStart().getHour()));
		super.startMinutes.setText(Integer.toString(appointmentToEdit.getStart().getMinute()));
		super.endHours.setText(Integer.toString(appointmentToEdit.getFrom().getHour()));
		super.endMinutes.setText(Integer.toString(appointmentToEdit.getFrom().getMinute()));
		ObservableList<String> list = FXCollections.observableArrayList(appointmentToEdit.getUsers());
		super.added.setItems(list);
		if(!(appointmentToEdit.getAlarm()==0)){
			super.alarm.setText(Integer.toString(appointmentToEdit.getAlarm()));
		}
			
	}
	
	public Appointment testAppointment(){
		
		Appointment test = new Appointment();
//		test.createExampleAppointment();
		return test;
		
	}
	
	

}
