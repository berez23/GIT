package it.escsolution.escwebgis.imu.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;
import java.util.Date;

public class ConsulenzaBean extends EscObject implements Serializable {
	
	private String chiave;
	private String cf;
	private String prog;
	private String cognome;
	private String nome;
	private Date dtConsulenza;
	public String getChiave() {
		return chiave;
	}
	public void setChiave(String chiave) {
		this.chiave = chiave;
	}
	public String getCf() {
		return cf;
	}
	public void setCf(String cf) {
		this.cf = cf;
	}
	public String getProg() {
		return prog;
	}
	public void setProg(String prog) {
		this.prog = prog;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Date getDtConsulenza() {
		return dtConsulenza;
	}
	public void setDtConsulenza(Date dtConsulenza) {
		this.dtConsulenza = dtConsulenza;
	}
	

}
