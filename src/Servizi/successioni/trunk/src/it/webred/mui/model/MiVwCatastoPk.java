package it.webred.mui.model;

import java.io.Serializable;
import java.util.Date;

public class MiVwCatastoPk implements Serializable {
    private String foglio;
    private String particella;
    private String subalterno;
    private Date dataInizio;
    private Date dataFine;
    private String codNazionale;
    
    public MiVwCatastoPk() {
    	
    }
    
	public MiVwCatastoPk(String foglio, String particella,
			String subalterno, Date dataInizio, Date dataFine,
			String codNazionale) {
		this.foglio = foglio;
		this.particella = particella;
		this.subalterno = subalterno;
		this.dataInizio = dataInizio;
		this.dataFine = dataFine;
		this.codNazionale = codNazionale;
		
	}
	public String getCodNazionale() {
		return codNazionale;
	}
	public void setCodNazionale(String codNazionale) {
		this.codNazionale = codNazionale;
	}
	public String getFoglio() {
		return foglio;
	}
	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}
	public String getParticella() {
		return particella;
	}
	public void setParticella(String particella) {
		this.particella = particella;
	}
	public String getSubalterno() {
		return subalterno;
	}
	public void setSubalterno(String subalterno) {
		this.subalterno = subalterno;
	}
	public Date getDataInizio() {
		return dataInizio;
	}
	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}
	public Date getDataFine() {
		return dataFine;
	}
	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}
    
}
