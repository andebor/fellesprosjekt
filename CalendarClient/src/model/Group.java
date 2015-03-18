package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Group {
	
	private StringProperty groupName;
	private IntegerProperty groupId, groupLeader;
	
	public Group() {
	}

	public StringProperty getGroupName() {
		return groupName;
	}

	public void setGroupName(StringProperty groupName) {
		this.groupName = groupName;
	}

	public IntegerProperty getGroupId() {
		return groupId;
	}

	public void setGroupId(IntegerProperty groupId) {
		this.groupId = groupId;
	}

	public IntegerProperty getGroupLeader() {
		return groupLeader;
	}

	public void setGroupLeader(IntegerProperty groupLeader) {
		this.groupLeader = groupLeader;
	}

}
