package control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class GroupManagementController {
	
	MainApp mainApp;
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	@FXML
	ListView<String> groupsTable, membersTable;
	
	@FXML
	Label label_missingUser;
	
	protected ObservableList<String> groupsList = FXCollections.observableArrayList();
	protected ObservableList<String> memberList = FXCollections.observableArrayList();
	
	@FXML
	private void initialize() throws IOException {
		generateGroupsList();
		
		groupsTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		    	try {
					generateMemberList(newValue);
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
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
		dialog.setHeaderText("Velg bruker du vil legge til i " + groupsTable.getSelectionModel().getSelectedItem());
		dialog.setContentText("Bruker:");

		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
			//TODO: DO SOMETHING WITH SELECTED VALUE
		    System.out.println("Your choice: " + result.get());
		}
	}
	
	@FXML
	private void handleRemoveUser() {
		//Get selected user to delete
		label_missingUser.setVisible(false);
		String user = membersTable.getSelectionModel().getSelectedItem();
		if (user == null) {
			label_missingUser.setVisible(true);
		}else {
			System.out.println("Removing " + user + " from selected group");
			//TODO: Delete selected user
		}
	}
	
	public void generateGroupsList() {
		ObservableList<String> list = FXCollections.observableArrayList();
		//Get list from database
		try {
			String[] groups = Client.getGroups().split(Pattern.quote("\n"));
			String str = "";
			for (String group : groups){
				String[] grp = group.split("%&%");
				str = grp[1].substring(5) + " " + grp[0].substring(3);
				list.add(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		groupsList = list;
		groupsTable.setItems(groupsList);	
	}
	
	public void generateMemberList(String group) throws IOException {
		
		//Get list from database
		ObservableList<String> list = FXCollections.observableArrayList();
		
		try {
			String[] members = Client.getGroup(group.substring(group.length()-1)).split("%&%");
			System.out.println(Client.getGroup(group.substring(group.length()-1)));
			for(String member : members){
				list.add(member);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		memberList = list;
		membersTable.setItems(list);	
	}
}
