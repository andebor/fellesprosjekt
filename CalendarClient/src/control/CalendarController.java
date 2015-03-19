package control;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import model.Appointment;
import model.Employee;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.util.Callback;
import javafx.util.Pair;


public class CalendarController {
	
 
	private ObservableList<Appointment> PersonalAppointmentList = FXCollections.observableArrayList();
	
	
    @FXML
    GridPane calendarGridPane;
    
    @FXML
    Label weekLabel;
    
    @FXML
    ListView calenderList;
    
    @FXML
    Pane userToColorPane;
    
    @FXML
    AnchorPane anchorPane;


	MainApp mainApp;
	
	public void setMainApp(MainApp mainApp) {
	        this.mainApp = mainApp;
	}
	
	private int weekNumber;
	Map<String, Integer> totalMap = new HashMap<String, Integer>();
	Map<String, Integer> currentMap = new HashMap<String, Integer>();
	List<Color> colors = new ArrayList<Color>();
	private String youAreOwner;
	ObservableList<Employee> userList = FXCollections.observableArrayList();
	ArrayList<Employee> userListCopy = new ArrayList<Employee>();
    Rectangle hoverBox = new Rectangle(0,0,230 , 85);
	Label desc;
	Label sted;
	Label attendees;
	//List<Employee> userList;
	

    @FXML
    private void initialize() throws IOException {
    	
    	userList.clear();
    	
    	//Get employee list from server
    	String serverResponse = Client.getEmployees();
    	
    	String[] userStrings = serverResponse.split(Pattern.quote("@/@"));
    	
    	for(int i = 0; i < userStrings.length; i++) {
    		addEmployee(userStrings[i]);
		}
    	
    	for(int i = 0; i < userListCopy.size(); i++) {
    		userList.add(userListCopy.get(i));
    	}

    	
    	calenderList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	calenderList.setItems(userList);
    	
        calenderList.setCellFactory(new Callback<ListView<Employee>, ListCell<Employee>>(){
        	 
            @Override
            public ListCell<Employee> call(ListView<Employee> p) {
                 
                ListCell<Employee> cell = new ListCell<Employee>(){
 
                    @Override
                    protected void updateItem(Employee t, boolean bln) {
                        super.updateItem(t, bln);
                        if (t != null) {
                            setText(t.getFirstname().getValue() + " " + t.getLastname().getValue());
                        }
                    }
 
                };
                 
                return cell;
            }
        });
        
    	Platform.runLater(new Runnable()
    	{
    	    @Override
    	    public void run()
    	    {
    	        calenderList.requestFocus();
    	        calenderList.getSelectionModel().select(0);
    	        calenderList.getFocusModel().focus(0);
    	        try {
    	        	PersonalAppointmentList.clear();
    	        	getAppointmentList(userList.get(0), 0);
					generateCalendar();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    	    }
    	});
    	
        calenderList.setOnMouseClicked(new EventHandler<Event>() {

            @Override
            public void handle(Event event) {
                try {
					generateThatShit();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }

        });
    	
    	colors.add(Color.ROYALBLUE);
    	colors.add(Color.GREEN);
    	colors.add(Color.GRAY);
    	colors.add(Color.CHOCOLATE);
    	colors.add(Color.ORANGE);
    	colors.add(Color.PURPLE);
    	colors.add(Color.PINK);

    	
    	//Set correct week:
    	LocalDate date = LocalDate.now();
    	TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear(); 
    	this.weekNumber = date.get(woy);
    	weekLabel.setText(Integer.toString(weekNumber));
    	//generateExampleAppointment();
    	

    	
    	


	}
    
    private void addEmployee(String emp) {
    	
    	Employee employee = new Employee();
    	
    	String[] fields = emp.split(Pattern.quote("&/&"));
    	
    	employee.setEmpNo(new SimpleIntegerProperty(Integer.parseInt(fields[0])));
    	employee.setFirstname(new SimpleStringProperty(fields[1]));
    	employee.setLastname(new SimpleStringProperty(fields[2]));
    	employee.setUsername(new SimpleStringProperty(fields[3]));
    	
    	
		if(employee.getUsername().getValue().equals(Client.username)) {
			userList.add(employee);
		}
		else {
			userListCopy.add(employee);
		}
		
    	
    }

    
    public void nextWeekButton(ActionEvent event) throws IOException{
    	

        Calendar c = Calendar.getInstance();     
        c.set(Calendar.getInstance().get(Calendar.YEAR), 0, 1);  
    	
    	if(weekNumber<c.getMaximum(Calendar.WEEK_OF_YEAR)){
    		weekNumber++;
    		weekLabel.setText(Integer.toString(weekNumber));
    		generateThatShit();
    	}

    	
    }
    
    public void previousWeekButton(ActionEvent event) throws IOException{
    	
    	if(weekNumber>0){
    	weekNumber--;
    	weekLabel.setText(Integer.toString(weekNumber));
    	generateThatShit();
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
    
    public void getAppointmentList(Employee employee, int colorIndex) throws IOException {
    	
    	String str = Client.getAppointmentList(employee.getUsername().getValue());
    	
    	
    	
    	String[] appStrings = str.split(Pattern.quote("$%"));
    	
    	/*
    	for(int i = 0; i < appStrings.length; i++) {
    		System.out.println(appStrings[i] + "\n");
    	}
    	*/
    	
    	
    	for(int i = 0; i < appStrings.length; i++) {
    		if(appStrings[i].length() > 3) { //dirtyfix
    			addAppointment(appStrings[i], colorIndex);    			
    		}
    	}
    }
    
    public void generateThatShit() throws IOException {
		PersonalAppointmentList.clear();
		
        ObservableList<Employee> selectedItems =  calenderList.getSelectionModel().getSelectedItems();

        
        for(int i = 0; i < selectedItems.size(); i++){
            try {
            	getAppointmentList(selectedItems.get(i), i);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        
            
            generateCalendar();
        }
    }
    
    public void generateColorToUserList() {
    	
    	ObservableList<Employee> selectedItems =  calenderList.getSelectionModel().getSelectedItems();
    	userToColorPane.getChildren().clear();
    	
    	for(int i = 0; i < selectedItems.size(); i++) {
    		Label label = new Label(selectedItems.get(i).getFirstname().getValue() + " " + userList.get(i).getLastname().getValue());
    		label.setTranslateY(30 * i);
    		label.setTranslateX(40);
			Rectangle rect = new Rectangle(0,0,20 , 20);
			rect.setTranslateY(30 * i);
			rect.setFill(colors.get(i));
    		userToColorPane.getChildren().add(label);
    		userToColorPane.getChildren().add(rect);
    	}
    }
	
	public void generateCalendar() throws IOException{
		
		
		
		generateColorToUserList();
		
		
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
			for(int row2 = start2; row2<=end2; row2++){
				if(column2 != 0 && row2 != 0) { //dirtyfix
					//Label appointmentLabel = new Label(appointment2.getDescription());
					//appointmentLabel.setTranslateX(30);
					int width = 97 / totalMap.get("" + row2 + column2);
					int current = currentMap.get("" + row2 + column2);
					int height = 26;
					if(row2 == end2) {
						height = 27;
					}
					
					Rectangle rect = new Rectangle(0,0,width,height);
					rect.setTranslateX(current * width);
					rect.setFill(colors.get(appointment2.getColor()));
					rect.setStroke(Color.BLACK);
					calendarGridPane.add(rect, column2, row2);
					currentMap.put("" + row2 + column2, currentMap.get("" + row2 + column2) + 1);
					
					
					
					Boolean bol = appointment2.getOwner().equals(Client.fullName);
					
					if(bol == true) {
						youAreOwner = "true";
					}
					else {
						youAreOwner = "false";
					}
					//youAreOwner = Client.checkAppointmentOwnership(appointment2.getID());
					
					String status = appointment2.getStatus();
					if(status == null) //dirtyfix
					{
						status = "nothing";
					}
					
					if(youAreOwner.equals("true")) {
						SVGPath man = new SVGPath();
						man.setContent("M7.725,2.146c-1.016,0.756-1.289,1.953-1.239,2.59C6.55,5.515,6.708,6.529,6.708,6.529s-0.313,0.17-0.313,0.854C6.504,9.1,7.078,8.359,7.196,9.112c0.284,1.814,0.933,1.491,0.933,2.481c0,1.649-0.68,2.42-2.803,3.334C3.196,15.845,1,17,1,19v1h18v-1c0-2-2.197-3.155-4.328-4.072c-2.123-0.914-2.801-1.684-2.801-3.334c0-0.99,0.647-0.667,0.932-2.481c0.119-0.753,0.692-0.012,0.803-1.729c0-0.684-0.314-0.854-0.314-0.854s0.158-1.014,0.221-1.793c0.065-0.817-0.398-2.561-2.3-3.096c-0.333-0.34-0.558-0.881,0.466-1.424C9.439,0.112,8.918,1.284,7.725,2.146z");						
						calendarGridPane.add(man, column2, row2);
						man.setTranslateX(current * width + width/2 - 9);
						man.setMouseTransparent(true);
					}
					else if(status.equals("Venter")) {
						SVGPath questionmark = new SVGPath();
						questionmark.setContent("M14.09,2.233C12.95,1.411,11.518,1,9.794,1C8.483,1,7.376,1.289,6.477,1.868C5.05,2.774,4.292,4.313,4.2,6.483h3.307c0-0.633,0.185-1.24,0.553-1.828c0.369-0.586,0.995-0.879,1.878-0.879c0.898,0,1.517,0.238,1.854,0.713C12.131,4.966,12.3,5.493,12.3,6.071c0,0.504-0.252,0.965-0.557,1.383c-0.167,0.244-0.387,0.469-0.661,0.674c0,0-1.793,1.15-2.58,2.074c-0.456,0.535-0.497,1.338-0.538,2.488c-0.002,0.082,0.029,0.252,0.315,0.252c0.287,0,2.316,0,2.571,0c0.256,0,0.309-0.189,0.312-0.274c0.018-0.418,0.064-0.633,0.141-0.875c0.144-0.457,0.538-0.855,0.979-1.199l0.91-0.627c0.822-0.641,1.477-1.166,1.767-1.578c0.494-0.676,0.842-1.51,0.842-2.5C15.8,4.274,15.23,3.057,14.09,2.233z M9.741,14.924c-1.139-0.035-2.079,0.754-2.115,1.99c-0.035,1.234,0.858,2.051,1.998,2.084c1.189,0.035,2.104-0.727,2.141-1.963C11.799,15.799,10.931,14.959,9.741,14.924z");						
						calendarGridPane.add(questionmark, column2, row2);
						questionmark.setTranslateX(current * width + width/2 - 7);
					}
					else if(status.equals("Deltar")) {
						SVGPath check = new SVGPath();
						check.setContent("M8.294,16.998c-0.435,0-0.847-0.203-1.111-0.553L3.61,11.724c-0.465-0.613-0.344-1.486,0.27-1.951c0.615-0.467,1.488-0.344,1.953,0.27l2.351,3.104l5.911-9.492c0.407-0.652,1.267-0.852,1.921-0.445c0.653,0.406,0.854,1.266,0.446,1.92L9.478,16.34c-0.242,0.391-0.661,0.635-1.12,0.656C8.336,16.998,8.316,16.998,8.294,16.998z");						
						calendarGridPane.add(check, column2, row2);
						check.setTranslateX(current * width + width/2 - 8);
					}
					else if(status.equals("Avkreftet") || status.equals("Avbud")) {
						SVGPath cross = new SVGPath();
						cross.setContent("M14.348,14.849c-0.469,0.469-1.229,0.469-1.697,0L10,11.819l-2.651,3.029c-0.469,0.469-1.229,0.469-1.697,0c-0.469-0.469-0.469-1.229,0-1.697l2.758-3.15L5.651,6.849c-0.469-0.469-0.469-1.228,0-1.697s1.228-0.469,1.697,0L10,8.183l2.651-3.031c0.469-0.469,1.228-0.469,1.697,0s0.469,1.229,0,1.697l-2.758,3.152l2.758,3.15C14.817,13.62,14.817,14.38,14.348,14.849z");						
						calendarGridPane.add(cross, column2, row2);
						cross.setTranslateX(current * width + width/2 - 7);
					}

					
				    rect.setOnMouseClicked(new EventHandler<MouseEvent>() {

				        @Override
				        public void handle(MouseEvent event) {
				        	try {
								youAreOwner = Client.checkAppointmentOwnership(appointment2.getID());
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				        	
				        	mainApp.appointmentToSelect = appointment2;
				        	
				        	
				        	if(youAreOwner.equals("true")) {
				        		mainApp.showAppointmentOverview();				        		
				        	}
				        	else {
				        		mainApp.showInvitations();
				        	}
				        }
				    });
				    
				    rect.setOnMouseEntered(new EventHandler<MouseEvent>() {
				    	


				        @Override
				        public void handle(MouseEvent event) {
				        	Glow glow = new Glow();
				        	glow.setLevel(1.0);
				        	rect.setEffect(glow);
				        	
				        	int xPadding = 150;
				        	int xTextPadding = 10;
				        	int yTextPadding = 5;
				        	
				        	
							hoverBox.setFill(Color.LIGHTSALMON);
							hoverBox.setTranslateX(rect.getLayoutX() + xPadding);
							hoverBox.setTranslateY(rect.getLayoutY());
							hoverBox.setOpacity(1.0);
							hoverBox.setStroke(Color.BLACK);
							
							InnerShadow is = new InnerShadow();
							//hoverBox.setEffect(is);
							
							desc = new Label();
							sted = new Label();
							attendees = new Label();
							
							String descString = appointment2.getDescription();
							String stedString = appointment2.getPlace();
							if(stedString.equals("null")) {
								stedString = appointment2.getRoom();
								if(stedString.equalsIgnoreCase("null")) {
									stedString = "Ikke spesifisert";
								}
							}
							
							if(descString.length() > 25) {
								descString = descString.substring(0, 25) + "...";
							}
							
							
							desc.setText("Beskrivelse: " + descString);
							sted.setText("Sted: " + stedString);
							attendees.setText("1/9 deltagere");
							
							desc.setTranslateX(rect.getLayoutX() + xPadding + xTextPadding);
							desc.setTranslateY(rect.getLayoutY() + yTextPadding);
							
							sted.setTranslateX(rect.getLayoutX() + xPadding + xTextPadding);
							sted.setTranslateY(rect.getLayoutY() + yTextPadding + 20);
							
							attendees.setTranslateX(rect.getLayoutX() + xPadding + xTextPadding);
							attendees.setTranslateY(rect.getLayoutY() + yTextPadding + 40);
							
				    		anchorPane.getChildren().add(hoverBox);
				    		anchorPane.getChildren().add(desc);
				    		anchorPane.getChildren().add(sted);
				    		anchorPane.getChildren().add(attendees);
				        }
				    });
				    
				    rect.setOnMouseExited(new EventHandler<MouseEvent>() {

				        @Override
				        public void handle(MouseEvent event) {
				        	rect.setEffect(null);
				        	anchorPane.getChildren().remove(hoverBox);
				        	anchorPane.getChildren().remove(desc);
				        	anchorPane.getChildren().remove(sted);	
				        	anchorPane.getChildren().remove(attendees);		
				        }
				    });
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
    

    public void addAppointment(String str, int colorIndex) throws IOException {
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
    	
    	
    	String[] deltagere = z[8].split("@/@");
    	
    	String yourID = Client.userID;
    	
    	for(int i = 0; i < deltagere.length; i++) {
    		String[] x = deltagere[i].split(" ");
    		
    		if(x[0].equals(yourID)) {
    			appointment.setStatus(x[3]);
    		}
    	}
    	
    	
    	
    	ObservableList<String> usersList = FXCollections.observableArrayList();
    	
    	for(int i = 0; i < deltagere.length; i++) {
    		usersList.add(deltagere[i]);
    	}
    	
    	
    	appointment.setDate(LocalDate.of(Integer.parseInt(datoList[0]), Integer.parseInt(datoList[1]), Integer.parseInt(datoList[2])));
    	appointment.setStart(LocalTime.of(Integer.parseInt(startTidList[0]), Integer.parseInt(startTidList[1])));
    	appointment.setFrom(LocalTime.of(11,30));
    	appointment.setUsers(usersList);
    	appointment.setRoomAmount(2);
    	appointment.setID(z[7]);
    	appointment.setOwner(z[5] + " " + z[6]);
    	appointment.setColor(colorIndex);
    	
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
