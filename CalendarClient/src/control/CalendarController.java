package control;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;


public class CalendarController {
	
 
	private ObservableList<Appointment> PersonalAppointmentList = FXCollections.observableArrayList();
	
	
    @FXML
    GridPane calendarGridPane;
    
    @FXML
    Label weekLabel;
    
    

	MainApp mainApp;
	
	public void setMainApp(MainApp mainApp) {
	        this.mainApp = mainApp;
	}
	
	private int weekNumber;
	Map<String, Integer> totalMap = new HashMap<String, Integer>();
	Map<String, Integer> currentMap = new HashMap<String, Integer>();
	List<Color> colors = new ArrayList<Color>();

	

    @FXML
    private void initialize() throws IOException {
    	
    	colors.add(Color.BLUE);
    	colors.add(Color.RED);
    	colors.add(Color.YELLOW);
    	colors.add(Color.GREEN);
    	colors.add(Color.ORANGE);
    	colors.add(Color.PINK);
    	colors.add(Color.PURPLE);
    	
    	//Set correct week:
    	LocalDate date = LocalDate.now();
    	TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear(); 
    	this.weekNumber = date.get(woy);
    	weekLabel.setText(Integer.toString(weekNumber));
    	//generateExampleAppointment();
    	
    	String str = Client.getAppointmentList();
    	
    	System.out.println(str);
    	
    	
    	String[] appStrings = str.split(Pattern.quote("$%"));
    	
    	/*
    	for(int i = 0; i < appStrings.length; i++) {
    		System.out.println(appStrings[i] + "\n");
    	}
    	*/
    	
    	
    	for(int i = 0; i < appStrings.length; i++) {
    		if(appStrings[i].length() > 3) { //dirtyfix
    			addAppointment(appStrings[i]);    			
    		}
    	}
    	
    	
		generateCalendar();


	}
    
    public void nextWeekButton(ActionEvent event){
    	
    	weekNumber++;
    	weekLabel.setText(Integer.toString(weekNumber));
    	generateCalendar();
    	
    }
    
    public void previousWeekButton(ActionEvent event){
    	
    	if(weekNumber>0){
    	weekNumber--;
    	weekLabel.setText(Integer.toString(weekNumber));
    	generateCalendar();;
    	}
    	
    }
    
    public void generateGUICalendar() {
    	
    	
    	//DAYS
    	Label mandag = new Label("Mandag");
    	mandag.setTranslateX(30);
    	Label tirsdag = new Label("Tirsdag");
    	tirsdag.setTranslateX(30);
    	Label onsdag = new Label("Onsdag");
    	onsdag.setTranslateX(30);
    	Label torsdag = new Label("Torsdag");
    	torsdag.setTranslateX(30);
    	Label fredag = new Label("Fredag");
    	fredag.setTranslateX(30);
    	Label lordag = new Label("Lørdag");
    	lordag.setTranslateX(30);
    	Label sondag = new Label("Søndag");
    	sondag.setTranslateX(30);
    	calendarGridPane.add(mandag, 1, 0);
    	calendarGridPane.add(tirsdag, 2, 0);
    	calendarGridPane.add(onsdag, 3, 0);
    	calendarGridPane.add(torsdag, 4, 0);
    	calendarGridPane.add(fredag, 5, 0);
    	calendarGridPane.add(lordag, 6, 0);
    	calendarGridPane.add(sondag, 7, 0);
    	
    	//hours
       	Label hour1 = new Label("00:00-");
       	hour1.setTranslateX(30);
    	Label hour2 = new Label("08:00");
    	hour2.setTranslateX(30);
    	Label hour3 = new Label("09:00");
    	hour3.setTranslateX(30);
    	Label hour4 = new Label("10:00");
    	hour4.setTranslateX(30);
    	Label hour5 = new Label("11:00");
    	hour5.setTranslateX(30);
    	Label hour6 = new Label("12:00");
    	hour6.setTranslateX(30);
    	Label hour7 = new Label("13:00");
    	hour7.setTranslateX(30);
       	Label hour8 = new Label("14:00");
    	hour8.setTranslateX(30);
    	Label hour9 = new Label("15:00");
    	hour9.setTranslateX(30);
    	Label hour10 = new Label("16:00");
    	hour10.setTranslateX(30);
    	Label hour11 = new Label("17:00");
    	hour11.setTranslateX(30);
    	Label hour12 = new Label("18:00");
    	hour12.setTranslateX(30);
    	Label hour13 = new Label("19:00");
    	hour13.setTranslateX(30);
    	Label hour14 = new Label("20:00");
    	hour14.setTranslateX(30);
    	Label hour15 = new Label("21:00");
    	hour15.setTranslateX(30);
    	Label hour16 = new Label("22:00-");
    	hour16.setTranslateX(30);
    	
    	calendarGridPane.add(hour1, 0, 1);
    	calendarGridPane.add(hour2, 0, 2);
    	calendarGridPane.add(hour3, 0, 3);
    	calendarGridPane.add(hour4, 0, 4);
    	calendarGridPane.add(hour5, 0, 5);
    	calendarGridPane.add(hour6, 0, 6);
    	calendarGridPane.add(hour7, 0, 7);
    	calendarGridPane.add(hour8, 0, 8);
    	calendarGridPane.add(hour9, 0, 9);
    	calendarGridPane.add(hour10, 0, 10);
    	calendarGridPane.add(hour11, 0, 11);
    	calendarGridPane.add(hour12, 0, 12);
    	calendarGridPane.add(hour13, 0, 13);
    	calendarGridPane.add(hour14, 0, 14);    	
    	calendarGridPane.add(hour15, 0, 15);
    	calendarGridPane.add(hour16, 0, 16);
    	
    }
	
	public void generateCalendar(){
		
		//Clear calendar
		calendarGridPane.getChildren().clear();
		// Generate GUI
		generateGUICalendar();
		
		for(int row = 0; row<30; row++){
			for(int column = 0; column < 30; column++) {
				totalMap.put("" + row + column, 0);				
			}
		}
		
		for(int row = 0; row<30; row++){
			for(int column = 0; column < 30; column++) {
				currentMap.put("" + row + column, 0);				
			}
		}

		
		
		for(Appointment appointment : PersonalAppointmentList){
		
			//Check week
			LocalDate appointmentDate = appointment.getDate();
			TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear(); 
			int appointmentWeek = appointmentDate.get(woy);
			int start = 0;
			int end = 0;
			int column = 0;
		
			if(appointmentWeek==Integer.parseInt(weekLabel.getText())){
				//Find column
				column = appointment.getDate().getDayOfWeek().getValue();
			
				//Find rows
				start = appointment.getStart().getHour();
				if(start<8){
					start = 0;
				}
				else{
					start = start - 6;
				}
				end = appointment.getFrom().getHour();
				if(end<8){
					end = 0;
				}
				else{
					end = end - 6;
				}
			}
				
	
			
			for(int row = start; row<=end; row++){
				totalMap.put("" + row + column, totalMap.get("" + row + column) + 1);
				
			}	
		}
			

		
		for(Appointment appointment2 : PersonalAppointmentList){
		
			//Check week
			LocalDate appointmentDate2 = appointment2.getDate();
			TemporalField woy2 = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear(); 
			int appointmentWeek2 = appointmentDate2.get(woy2);
			int start2 = 0;
			int end2 = 0;
			int column2 = 0;
		
			if(appointmentWeek2==Integer.parseInt(weekLabel.getText())){
				//Find column
				column2 = appointment2.getDate().getDayOfWeek().getValue();
			
				//Find rows
				start2 = appointment2.getStart().getHour();
				if(start2<8){
					start2 = 0;
				}
				else{
					start2 = start2 - 6;
				}
				end2 = appointment2.getFrom().getHour();
				if(end2<8){
					end2 = 0;
				}
				else{
					end2 = end2 - 6;
				}
			}			
		
			// Generate appointment in calendar
			if(column2 != 0) { //dirtyfix
				for(int row2 = start2; row2<=end2; row2++){
					//Label appointmentLabel = new Label(appointment2.getDescription());
					//appointmentLabel.setTranslateX(30);
					int width = 98 / totalMap.get("" + row2 + column2);
					int current = currentMap.get("" + row2 + column2);
					Rectangle rect = new Rectangle(0,0,width,30);
					rect.setTranslateX(current * width);
					rect.setFill(colors.get(current));
					calendarGridPane.add(rect, column2, row2);
					currentMap.put("" + row2 + column2, currentMap.get("" + row2 + column2) + 1);
				}
			}

		}
	}
	
	
	
    public void generateExampleAppointment() {
    	// Just for testing functionality 
    	Appointment appointment = new Appointment();
    	appointment.setDescription("test");
    	appointment.setDate(LocalDate.of(2015,3,4));
    	appointment.setStart(LocalTime.of(9,00));
    	appointment.setFrom(LocalTime.of(15,00));
    	ObservableList<String> list2 = FXCollections.observableArrayList("Ole", "Ansatt 1");
    	appointment.setUsers(list2);
    	appointment.setRoom("Grouproom 1");
    	appointment.setRoomAmount(2);
    	PersonalAppointmentList.add(appointment);
   

    	
    }
    

    public void addAppointment(String str) throws IOException {
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
    	appointment.setOwner(Client.getUser(z[5]));
    	
    	if(z[4].equals("null")){
    		appointment.setPlace(z[3]);
    		appointment.setRoom("null");
    	}
    	else {
    		appointment.setRoom(z[4]);
    		appointment.setPlace("null");
    	}
    	
    	PersonalAppointmentList.add(appointment);
    	
    }
    

	

}
