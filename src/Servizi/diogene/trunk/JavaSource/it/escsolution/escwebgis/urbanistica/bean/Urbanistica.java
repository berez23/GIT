package it.escsolution.escwebgis.urbanistica.bean;

import it.escsolution.escwebgis.common.EscObject;
import it.webred.utils.GenericTuples;

import java.io.Serializable;

public class Urbanistica extends EscObject implements Serializable{

	private static final long serialVersionUID = 3505455510069725134L;
	
	private Long pk = new Long(0);
	private String nominativo;
	private String codEnte;
	private String provenienza = "";
	private String ubicazione = "";
	private String chiave = "";

	private GenericTuples.T2<String,String> coord;
	
	
	public String getCodEnte() {
		return codEnte;
	}
	public void setCodEnte(String codEnte) {
		this.codEnte = codEnte;
	}
	public String getNominativo() {
		return nominativo;
	}
	public void setNominativo(String tecCognome) {
		this.nominativo = tecCognome;
	}
	public GenericTuples.T2<String, String> getCoord() {
		return coord;
	}
	public void setCoord(GenericTuples.T2<String, String> coord) {
		this.coord = coord;
	}
	public Long getPk() {
		return pk;
	}
	public void setPk(Long pk) {
		this.pk = pk;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public String getProvenienza() {
		return provenienza;
	}
	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}
	public String getUbicazione() {
		return ubicazione;
	}
	public void setUbicazione(String ubicazione) {
		this.ubicazione = ubicazione;
	}
	public String getChiave() {
		return String.valueOf(pk);
	}
	public void setChiave(String chiave) {
		this.chiave = chiave;
	}


}
