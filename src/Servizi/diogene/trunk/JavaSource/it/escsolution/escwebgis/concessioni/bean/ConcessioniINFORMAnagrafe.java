package it.escsolution.escwebgis.concessioni.bean;
import java.io.Serializable;


public class ConcessioniINFORMAnagrafe extends ConcessioniINFORM implements Serializable
{
	private String citta;
	private String indirizzo;
	private String civico;
	private String dataResidenza;
	public String getDataResidenza() {
		return dataResidenza;
	}
	public void setDataResidenza(String dataResidenza) {
		this.dataResidenza = dataResidenza;
	}
	public String getCitta() {
		return citta;
	}
	public void setCitta(String citta) {
		this.citta = citta;
	}
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	public String getCivico() {
		return civico;
	}
	public void setCivico(String civico) {
		this.civico = civico;
	}
	
}
