package it.webred.gitout.dto;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="unitaImmoDTO")
public class UnitaImmoDTO implements Serializable{

	private static final long serialVersionUID = -7238021643987931617L;
	
	private String descrizione = "";
	private String foglio = "";
	private String numero = "";
	private String sezione = "";
//	private String subalterno = "";
	private String unimm;
	private String dataInizioVal;
	private String dataFineVal;
	private String categoria;
	private String classe;
	private String consistenza;
	private String rendita;
	private String supCat;
	
//	private String piano;
//	private String edificio;
//	private String annotazioni;
//	private BigDecimal attoFin;
//	private BigDecimal attoIni;
//	private String codNazionaleUrba;
//	private String flagProvenienza;
//	private String foglioUrba;
//	private BigDecimal idTrasf;
//	private BigDecimal idTrasfFine;
//	private BigDecimal ideImmo;
//	private BigDecimal ideMutaFine;
//	private BigDecimal ideMutaIni;
//	private String interno;
//	private String microzona;
//	private String millesimiTerreno;
//	private String note;
//	private String particellaUrba;
//	private String partita;
//	private String scala;
//	private BigDecimal sorgente;
//	private String subUrba;
//	private BigDecimal supeModelli;
//	private String utente;
//	private String utenteFine;
//	private String validatoDaCat;
//	private String validatoDaCatFin;
//	private String zona;
	
	private ArrayList<PersonaDTO> alPersone = null;
	private ArrayList<LocalizzazioneDTO> alLocalizzazioni = null;
	private ArrayList<MetricaDTO> alMetrici = null;

	public UnitaImmoDTO() {

	}//-------------------------------------------------------------------------

	public String getUnimm() {
		return unimm;
	}

	public void setUnimm(String unimm) {
		this.unimm = unimm;
	}

	public String getDataFineVal() {
		return dataFineVal;
	}

	public void setDataFineVal(String dataFineVal) {
		this.dataFineVal = dataFineVal;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public String getConsistenza() {
		return consistenza;
	}

	public void setConsistenza(String consistenza) {
		this.consistenza = consistenza;
	}

	public String getDataInizioVal() {
		return dataInizioVal;
	}

	public void setDataInizioVal(String dataInizioVal) {
		this.dataInizioVal = dataInizioVal;
	}

	public String getRendita() {
		return rendita;
	}

	public void setRendita(String rendita) {
		this.rendita = rendita;
	}

	public String getSupCat() {
		return supCat;
	}

	public void setSupCat(String supCat) {
		this.supCat = supCat;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public ArrayList<PersonaDTO> getAlPersone() {
		return alPersone;
	}

	public void setAlPersone(ArrayList<PersonaDTO> alPersone) {
		this.alPersone = alPersone;
	}

	public String getSezione() {
		return sezione;
	}

	public void setSezione(String sezione) {
		this.sezione = sezione;
	}

	public ArrayList<LocalizzazioneDTO> getAlLocalizzazioni() {
		return alLocalizzazioni;
	}

	public void setAlLocalizzazioni(ArrayList<LocalizzazioneDTO> alLocalizzazioni) {
		this.alLocalizzazioni = alLocalizzazioni;
	}

	public ArrayList<MetricaDTO> getAlMetrici() {
		return alMetrici;
	}

	public void setAlMetrici(ArrayList<MetricaDTO> alMetrici) {
		this.alMetrici = alMetrici;
	}

	public String getFoglio() {
		return foglio;
	}

	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	
	

}
