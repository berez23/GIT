package it.webred.ct.service.geospatial.data.access.dto;

import java.io.Serializable;

public class SpatialLayerDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String idGeometriaLayer;
	private String desLayer;
	private String tipoLayer;
	private String codiceTema;
	private String desTema;
	private String nameTable;
	private Object shape;
	
	public SpatialLayerDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getIdGeometriaLayer() {
		return idGeometriaLayer;
	}

	public void setIdGeometriaLayer(String idGeometriaLayer) {
		this.idGeometriaLayer = idGeometriaLayer;
	}

	public String getDesLayer() {
		return desLayer;
	}

	public void setDesLayer(String desLayer) {
		this.desLayer = desLayer;
	}

	public String getTipoLayer() {
		return tipoLayer;
	}

	public void setTipoLayer(String tipoLayer) {
		this.tipoLayer = tipoLayer;
	}

	public String getCodiceTema() {
		return codiceTema;
	}

	public void setCodiceTema(String codiceTema) {
		this.codiceTema = codiceTema;
	}

	public String getDesTema() {
		return desTema;
	}

	public void setDesTema(String desTema) {
		this.desTema = desTema;
	}

	public String getNameTable() {
		return nameTable;
	}

	public void setNameTable(String nameTable) {
		this.nameTable = nameTable;
	}

	public Object getShape() {
		return shape;
	}

	public void setShape(Object shape) {
		this.shape = shape;
	}
	
	

}
