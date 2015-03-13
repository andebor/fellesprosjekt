package control;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Pattern;

import model.Employee;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.util.Pair;

public class UserManagementController {
	
	MainApp mainApp;
	
    public void setMainApp(MainApp mainApp) throws IOException {
        this.mainApp = mainApp;
        
        initEmpTable();
    }
    
    @FXML 
    TableView<Employee> empTable;
    @FXML 
    TableColumn<Employee, String> empColumn;
    @FXML 
    Label firstnameLabel, lastnameLabel, empNoLabel;
    
    public static ObservableList<Employee> employeeList = FXCollections.observableArrayList();
    	
    public static ObservableList<Employee> getEmployeeList() {
    	return employeeList;
    }
    
    @FXML
    private void initialize() {
    	empColumn.setCellValueFactory(cellData -> cellData.getValue().getUsername());
    
    	empTable.getSelectionModel().selectedItemProperty().addListener(
    			(observable, oldValue, newValue) -> showEmployeeDetails(newValue));
    	
    	showEmployeeDetails(null);
    }
    
    private void showEmployeeDetails(Employee employee) {
    	if (employee != null) {
    		firstnameLabel.setText(employee.getFirstname().getValue());
    		lastnameLabel.setText(employee.getLastname().getValue());
    		empNoLabel.setText(employee.getEmpNo().getValue().toString());
    	}else {
    		firstnameLabel.setText("");
    		lastnameLabel.setText("");
    		empNoLabel.setText("");
    	}
    }
    
    private void initEmpTable() throws IOException {
    	
    	employeeList.clear();
    	
    	//Get employee list from server
    	String serverResponse = Client.getEmployees();
    	
    	String[] employees = serverResponse.split(Pattern.quote("@/@"));
    	
    	for (int i = 0; i < employees.length; i++) {
			addEmployee(employees[i]);
		}
    	
    	empTable.setItems(employeeList);
    	
    }
    
    private void addEmployee(String emp) {
    	
    	Employee employee = new Employee();
    	
    	String[] fields = emp.split(Pattern.quote("&/&"));
    	
//    	for (int i = 0; i < fields.length; i++) {
//			System.out.println(fields[i]);
//		}
    	
    	employee.setEmpNo(new SimpleIntegerProperty(Integer.parseInt(fields[0])));
    	employee.setFirstname(new SimpleStringProperty(fields[1]));
    	employee.setLastname(new SimpleStringProperty(fields[2]));
    	employee.setUsername(new SimpleStringProperty(fields[3]));
    	
    	employeeList.add(employee);
    	
    }
    
    
    @FXML
    private void testAddUser() throws IOException {
//		addUser("andebor", "Anders", "Borud", "andebor");
//		addUser("vigleikl", "Vigleik", "Lund", "vigleikl");
//		addUser("mariusmb", "Marius", "Bang", "mariusmb");
//		addUser("alfredb", "Alfred", "Birketvedt", "alfredb");
//		addUser("lars", "Lars", "Larsen", "lars");
//    	changeUserPass("admin", "admin");
//    	changeUserPass("vigleikl", "vigleikl");
//    	changeUserPass("mariusmb", "mariusmb");
//    	changeUserPass("alfredb", "alfredb");
//    	changeUserPass("andebor", "andebor");
    }
    
    private void addUser(String username, String firstName, String lastName,String password) throws IOException {
    	String response = Client.addUser(username, firstName, lastName, password);
    	System.out.println(response);
    }  
    
    private void changeUserPass(String username, String password) throws IOException {
    	String response = Client.changeUserPass(username, password);
    	System.out.println(response);
    }
    
    private void deleteUser(String username) throws IOException {
          String response = Client.deleteUser(username);
          System.out.println(response);
    }
    
    @FXML
    private void handleDeleteUser() throws IOException {
        Employee employee = empTable.getSelectionModel().getSelectedItem();
        if (employee == null) {
        	System.out.println("No employee selected.");
            return;
        }else {
        	System.out.println("Opening confirmation..");
        	// Dialog box to confirm deletion. (Requires JDK 1.8_u40)
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Er du sikker?");
            alert.setHeaderText("Er du sikker på at du vil slette " + employee.getUsername().getValue() + "?");
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                System.out.println("Bruker vil slette!");
                deleteUser(employee.getUsername().getValue());
                getEmployeeList().remove(employee);
                initEmpTable();
            }else {
                return;
            }
        }
    }
    
    @FXML
    private void handleChangeUserPass() throws IOException {
    	Employee employee = empTable.getSelectionModel().getSelectedItem();
    	if (employee == null) {
        	System.out.println("No employee selected.");
            return;
        }else{
        	TextInputDialog dialog = new TextInputDialog();
        	dialog.setTitle("Sett nytt passord");
        	dialog.setHeaderText("Nytt passord for " + employee.getUsername().getValue() + ": ");
        	
        	Optional<String> result = dialog.showAndWait();
        	if (result.isPresent()) {
        		changeUserPass(employee.getUsername().getValue(), result.get());
        	}else{
        		return;
        	}
        	
        }
    }
    
    @FXML
    private void handleAddNewUser() {
    	Dialog<Pair<String, String>> dialog = new Dialog<>();
    }

}
