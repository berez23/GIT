package it.escsolution.escwebgis.urbanistica.bean;

public class UrbaCommissioneEdilizia extends Urbanistica{

	private static final long serialVersionUID = 3978569144938678400L;
	
	private String anno = "";
	private String verb = "";
	private String parere = "";
	private String data_ce = "";
	private String pos = "";
	private String ce_aut = "";
	private String zona_urbanistica = "";
	private String lotto = "";
	private String tipo_intervento = "";
	private String sup_lotto = "";
	private String esistente = "";
	private String progetto = "";
	private String esistente2 = "";
	private String progetto2 = "";
	

	public UrbaCommissioneEdilizia() {
		// TODO Auto-generated constructor stub
	}


	public String getAnno() {
		return anno;
	}


	public void setAnno(String anno) {
		this.anno = anno;
	}


	public String getVerb() {
		return verb;
	}


	public void setVerb(String verbale) {
		this.verb = verbale;
	}


	public String getParere() {
		return parere;
	}


	public void setParere(String parere) {
		this.parere = parere;
	}


	public String getData_ce() {
		return data_ce;
	}


	public void setData_ce(String data_com) {
		this.data_ce = data_com;
	}


	public String getPos() {
		return pos;
	}


	public void setPos(String pos) {
		this.pos = pos;
	}


	public String getCe_aut() {
		return ce_aut;
	}


	public void setCe_aut(String ce_aut) {
		this.ce_aut = ce_aut;
	}


	public String getZona_urbanistica() {
		return zona_urbanistica;
	}


	public void setZona_urbanistica(String zona_urbanistica) {
		this.zona_urbanistica = zona_urbanistica;
	}


	public String getLotto() {
		return lotto;
	}


	public void setLotto(String lotto) {
		this.lotto = lotto;
	}


	public String getTipo_intervento() {
		return tipo_intervento;
	}


	public void setTipo_intervento(String tipo_intervento) {
		this.tipo_intervento = tipo_intervento;
	}


	public String getSup_lotto() {
		return sup_lotto;
	}


	public void setSup_lotto(String sup_lotto) {
		this.sup_lotto = sup_lotto;
	}


	public String getEsistente() {
		return esistente;
	}


	public void setEsistente(String esistente) {
		this.esistente = esistente;
	}


	public String getProgetto() {
		return progetto;
	}


	public void setProgetto(String progetto) {
		this.progetto = progetto;
	}


	public String getEsistente2() {
		return esistente2;
	}


	public void setEsistente2(String esistente2) {
		this.esistente2 = esistente2;
	}


	public String getProgetto2() {
		return progetto2;
	}


	public void setProgetto2(String progetto2) {
		this.progetto2 = progetto2;
	}


	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

}
