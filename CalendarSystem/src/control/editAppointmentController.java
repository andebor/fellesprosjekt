package control;

import java.net.URL;
import java.util.ResourceBundle;

import model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;


public class editAppointmentController extends NewAppointmentController implements Initializable {

	
	
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
		super.start.setText(Integer.toString(appointmentToEdit.getStart().getHour() + appointmentToEdit.getStart().getMinute()));
		super.end.setText(Integer.toString(appointmentToEdit.getFrom().getHour() + appointmentToEdit.getFrom().getMinute()));
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
	
	public void cancelApp(){
		
		cancelAppointment = true;
		// NOTIFY
		
	}
	
	

}
