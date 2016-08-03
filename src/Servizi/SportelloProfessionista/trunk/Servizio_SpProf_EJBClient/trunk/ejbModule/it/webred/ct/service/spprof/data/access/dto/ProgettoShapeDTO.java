package it.webred.ct.service.spprof.data.access.dto;

import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;

public class ProgettoShapeDTO extends CeTBaseObject implements Serializable {
	
	private String shapeFolder;
	private String shapefileName; //nome del file shape senza path e senza estenzione
	private String origFilename;
	private Long fkIntervento;
	
	public ProgettoShapeDTO(String shapeFolder, String shapefileName, String origFilename, Long fkIntervento) {
		super();
		this.shapeFolder = shapeFolder;
		this.shapefileName = shapefileName;
		this.origFilename = origFilename;
		this.fkIntervento = fkIntervento;
	}

	public String getShapeFolder() {
		return shapeFolder;
	}

	public void setShapeFolder(String shapeFolder) {
		this.shapeFolder = shapeFolder;
	}

	public String getShapefileName() {
		return shapefileName;
	}

	public void setShapefileName(String shapefileName) {
		this.shapefileName = shapefileName;
	}

	public Long getFkIntervento() {
		return fkIntervento;
	}

	public void setFkIntervento(Long fkIntervento) {
		this.fkIntervento = fkIntervento;
	}

	public String getOrigFilename() {
		return origFilename;
	}

	public void setOrigFilename(String origFilename) {
		this.origFilename = origFilename;
	}
	
}
