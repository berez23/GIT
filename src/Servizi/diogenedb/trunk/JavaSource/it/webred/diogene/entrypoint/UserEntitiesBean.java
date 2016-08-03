package it.webred.diogene.entrypoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class UserEntitiesBean implements Serializable {
	
	public static final long serialVersionUID = -1;

	private ArrayList<HashMap<String, Object>> userEntities;
	
	public UserEntitiesBean() {
		super();
	}
	
	public UserEntitiesBean(ArrayList<HashMap<String, Object>> userEntities) {
		super();
		this.userEntities = userEntities;
	}

	public ArrayList<HashMap<String, Object>> getUserEntities() {
		return userEntities;
	}

	public void setUserEntities(ArrayList<HashMap<String, Object>> userEntities) {
		this.userEntities = userEntities;
	}
	
}
