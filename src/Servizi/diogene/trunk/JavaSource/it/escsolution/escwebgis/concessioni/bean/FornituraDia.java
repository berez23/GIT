package it.escsolution.escwebgis.concessioni.bean;

import it.escsolution.escwebgis.common.EscObject;
import it.webred.utils.GenericTuples;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FornituraDia extends EscObject implements Serializable{ 
	
	private String codEnte;
	
	private String fkFornitura;
	private String protocollo;
	private String ricCodiceFiscale;
	private String ricCognome;
	private String ricNome;
	private String qualifica;
	private String intervento;
	private String indirizzo;
	private String tipoUnita;
	private List<String[]> uiu = new ArrayList<String[]>();
	private List<String[]> benefificiari = new ArrayList<String[]>();
	private List<String[]> professionisti = new ArrayList<String[]>();
	private List<String[]> imp = new ArrayList<String[]>();
	
	
	public String getChiave()
	{
		return fkFornitura+"|"+protocollo;
	}
	
	public void addUiu(String[] uiu) {
		this.uiu.add(uiu);
	}
	public List<String[]> getUiu() {
		return uiu;
	}
	
	public void addBeneficiari(String[] ben) {
		this.benefificiari.add(ben);
	}
	public List<String[]> getBeneficiari() {
		return benefificiari;
	}
	
	public void addProfessionista(String[] pro) {
		this.professionisti.add(pro);
	}
	public List<String[]> getProfessionisti() {
		return professionisti;
	}
	
	public void addImp(String[] imp) {
		this.imp.add(imp);
	}
	public List<String[]> getImp() {
		return imp;
	}

	public String getRicCodiceFiscale() {
		return ricCodiceFiscale;
	}
	public void setRicCodiceFiscale(String ricCodiceFiscale) {
		this.ricCodiceFiscale = ricCodiceFiscale;
	}
	public String getRicCognome() {
		return ricCognome;
	}
	public void setRicCognome(String ricCognome) {
		this.ricCognome = ricCognome;
	}
	public String getRicNome() {
		return ricNome;
	}
	public void setRicNome(String ricNome) {
		this.ricNome = ricNome;
	}
	public String getQualifica() {
		return qualifica;
	}
	public void setQualifica(String qualifica) {
		this.qualifica = qualifica;
	}
	public String getIntervento() {
		return intervento;
	}
	public void setIntervento(String intervento) {
		this.intervento = intervento;
	}
	public String getProtocollo() {
		return protocollo;
	}
	public void setProtocollo(String protocollo) {
		this.protocollo = protocollo;
	}
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	public String getTipoUnita() {
		return tipoUnita;
	}
	public void setTipoUnita(String tipoUnita) {
		this.tipoUnita = tipoUnita;
	}

	public String getFkFornitura() {
		return fkFornitura;
	}

	public void setFkFornitura(String fkFornitura) {
		this.fkFornitura = fkFornitura;
	}

	public String getCodEnte() {
		return codEnte;
	}

	public void setCodEnte(String codEnte) {
		this.codEnte = codEnte;
	}
	

}
