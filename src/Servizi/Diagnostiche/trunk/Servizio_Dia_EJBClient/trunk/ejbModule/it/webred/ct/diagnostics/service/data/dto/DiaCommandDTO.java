package it.webred.ct.diagnostics.service.data.dto;

import it.webred.ct.rulengine.controller.model.RCommand;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;

public class DiaCommandDTO extends CeTBaseObject implements Serializable {
		
	private static final long serialVersionUID = 1L;
	private Long id;
	private String codCommand;
	private String descrCommand;
	private String descrCommandLong;	
	private String descrCommandType;	
	private String descrCommandStato;
	private String parDiagType;
	
	private boolean selected;
	
	public DiaCommandDTO(){}
	
	public DiaCommandDTO(RCommand cmd,String enteId, String userId){
		this.id = cmd.getId();
		this.codCommand = cmd.getCodCommand();
		this.descrCommand = cmd.getDescr();
		this.descrCommandLong = cmd.getLongDescr();
		this.descrCommandType = cmd.getRCommandType().getDescr();
		
		super.setEnteId(enteId);
		super.setUserId(userId);
	}
	
	/**
	 * Ritorna la prima lettera del codCommand come discriminante tra diagnostica di gruppo o 
	 * diagnostica singola
	 * @return 
	 * G:Diagnostica di gruppo
	 * S:Diagnostica singola
	 */
	public String getParDiagType() {
		if (codCommand == null) 
			return "";
		else
			return codCommand.substring(0,1);
	}

	public void setParDiagType(String parDiagType) {
		this.parDiagType = parDiagType;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCodCommand() {
		return codCommand;
	}
	public void setCodCommand(String codCommand) {
		this.codCommand = codCommand;
	}
	public String getDescrCommand() {
		return descrCommand;
	}
	public void setDescrCommand(String descrCommand) {
		this.descrCommand = descrCommand;
	}
	public String getDescrCommandLong() {
		return descrCommandLong;
	}
	public void setDescrCommandLong(String descrCommandLong) {
		this.descrCommandLong = descrCommandLong;
	}
	public String getDescrCommandType() {
		return descrCommandType;
	}
	public void setDescrCommandType(String descrCommandType) {
		this.descrCommandType = descrCommandType;
	}
	public String getDescrCommandStato() {
		return descrCommandStato;
	}
	public void setDescrCommandStato(String descrCommandStato) {
		this.descrCommandStato = descrCommandStato;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
