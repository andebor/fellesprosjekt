package control;

import java.net.URL;
import java.util.ResourceBundle;

import model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
//asda

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
		if(appointmentToEdit.getPlace()==null){
			super.place.setText(appointmentToEdit.getRoom());
		}
		else{
			super.place.setText(appointmentToEdit.getPlace());
		}
		super.date.setValue(appointmentToEdit.getDate());
	//    super.start.setText(appointmentToEdit.getStart().toString());
	   // super.end.setText("appointmentToEdit.getEnd().toString()");
	//	ObservableList<String> list = FXCollections.observableArrayList(appointmentToEdit.getUsers());
	//	super.added.setItems(list);
	//	if(!(appointmentToEdit.getAlarm()==0)){
	//		super.alarm.setText(Integer.toString(appointmentToEdit.getAlarm()));
	//	}
	
	}
	
	public Appointment testAppointment(){//	ObservableList<String> list = FXCollections.observableArrayList(appointmentToEdit.getUsers());
	

		
		Appointment test = new Appointment();
//		test.createExampleAppointment();
		return test;
		
	}
	
	public void cancelApp(){
		
		cancelAppointment = true;
		// NOTIFY
		
	}
	
	

}
