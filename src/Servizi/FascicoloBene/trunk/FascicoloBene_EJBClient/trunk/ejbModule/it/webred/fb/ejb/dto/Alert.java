package it.webred.fb.ejb.dto;

import it.webred.fb.data.DataModelCostanti;
import it.webred.fb.data.DataModelCostanti.Segnalibri.TipoAlert;

import java.io.Serializable;

public class Alert implements Serializable {

	private static final long serialVersionUID = 1L;
	
	protected String type = new String();
	protected String description = new String();
	
	public Alert(String type, String description){
		this.type = type;
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getHeight() {
		if (this.type.equals(TipoAlert.IMM_VALIDATE)) {
			return 24;
		}
		if (this.type.equals(TipoAlert.IMM_NOT_VALIDATE)) {
			return 24;
		}
		if (this.type.equals(TipoAlert.TERR_VALIDATE)) {
			return 24;
		}
		if (this.type.equals(TipoAlert.TERR_NOT_VALIDATE)) {
			return 24;
		}
		return 13;
	}

}
