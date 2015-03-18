package control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import model.Employee;
import model.Group;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class GroupManagementController {
	
	MainApp mainApp;
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	@FXML
	ListView<Group> groupsTable;
	@FXML
	ListView<Employee> membersTable;
	
	@FXML
	Label label_missingUser;
	
	public static ObservableList<Group> groupsList = FXCollections.observableArrayList();
	public static ObservableList<Employee> memberList = FXCollections.observableArrayList();
    ObservableList<Employee> list = FXCollections.observableArrayList(); //temp

	
	@FXML
	private void initialize() throws IOException {
		generateGroupsList();
		
		groupsTable.setCellFactory((list) -> {
			return new ListCell<Group>() {
				protected void updateItem(Group item, boolean empty) {
					super.updateItem(item, empty);
					
					if (item == null || empty) {
						setText(null);
					}else{
						setText(item.getGroupName().getValue());
					}
				}
			};
		});
		
		groupsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			try {
				generateMemberList(newValue);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});	
		
		membersTable.setCellFactory((list) -> {
			return new ListCell<Employee>() {
				protected void updateItem(Employee item, boolean empty) {
					super.updateItem(item, empty);
					
					if (item == null || empty) {
						setText(null);
					}else{
						setText(item.getFullName().getValue());
					}
				}
			};
		});
	}
	
	@FXML
	private void handleAddUser() throws IOException {
		System.out.println("Opening dialog to select user..");
		
		// Create list of selectable choices in dialog box
		List<String> choices = new ArrayList<>();
		String serverResponse = Client.getEmployees();
    	String[] employees = serverResponse.split(Pattern.quote("@/@"));
    	
    	for (int i = 0; i < employees.length; i++) {
    		String[] fields = employees[i].split(Pattern.quote("&/&"));
			choices.add(fields[1] + " " + fields[2]);
		}
    	
    	// Create the dialog box
		ChoiceDialog<String> dialog = new ChoiceDialog<>(null, choices);
		dialog.setTitle("Velg bruker");
		dialog.setHeaderText("Velg bruker du vil legge til i " + groupsTable.getSelectionModel().getSelectedItem().getGroupName().getValue());
		dialog.setContentText("Bruker:");

		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
			//TODO: DO SOMETHING WITH SELECTED VALUE
			String group = groupsTable.getSelectionModel().getSelectedItem().getGroupId().getValue().toString();
		    System.out.println("Your choice: " + result.get());
		}
	}
	
	@FXML
	private void handleRemoveUser() throws IOException {
		//Get selected user to delete
		label_missingUser.setVisible(false);
		String user = membersTable.getSelectionModel().getSelectedItem().getEmpNo().getValue().toString();
		String group = groupsTable.getSelectionModel().getSelectedItem().getGroupId().getValue().toString();
		if (user == null) {
			label_missingUser.setVisible(true);
		}else {
			System.out.println("Removing " + membersTable.getSelectionModel().getSelectedItem().getFullName().getValue() + " from selected group");
			Client.removeMember(group, user);
			memberList.remove(membersTable.getSelectionModel().getSelectedIndex());
			
		}
	}
	
	public void generateGroupsList() {
		//Get list from database
		try {
			String[] groups = Client.getGroups().split(Pattern.quote("\n"));
			for (String group : groups){
				addGroup(group);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		groupsTable.setItems(groupsList);	
	}
	
	private void addGroup(String groupResponse) {
		Group group = new Group();
		String[] fields = groupResponse.split("%&%");
		
		group.setGroupId(new SimpleIntegerProperty(Integer.parseInt(fields[0].substring(3))));
		group.setGroupName(new SimpleStringProperty(fields[1].substring(5)));
		group.setGroupLeader(new SimpleIntegerProperty(Integer.parseInt(fields[2].substring(7,12))));
		
		groupsList.add(group);
	}
	
    public void generateMemberList(Group group) throws IOException {
    	list.clear();
        try {
            String[] members = Client.getGroup(group.getGroupId().getValue().toString()).split("%&%");
            for(String member : members){
                addMember(member);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        memberList = list;
        membersTable.setItems(memberList);
    }
    
    private void addMember(String member) {
        Employee employee = new Employee();
        
        String empId = member.substring(member.length() - 6);
        String name = member.substring(0,member.length() - 6);
        
        employee.setEmpNo(new SimpleIntegerProperty(Integer.parseInt(empId)));
        employee.setFullName(new SimpleStringProperty(name));
        
        list.add(employee);
    }
}
