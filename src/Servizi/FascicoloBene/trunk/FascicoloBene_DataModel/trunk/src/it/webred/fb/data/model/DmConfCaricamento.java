package it.webred.fb.data.model;

import java.io.Serializable;

import javax.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the DM_CONF_CARICAMENTO database table.
 * 
 */
@Entity
@Table(name="DM_CONF_CARICAMENTO")
@NamedQuery(name="DmConfCaricamento.findAll", query="SELECT d FROM DmConfCaricamento d order by d.id.provenienza, d.id.dato")
public class DmConfCaricamento implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private DmConfCaricamentoPK id;

	@Column(name="FLAG_ACCODA")
	private Integer flagAccoda;

	@Column(name="FLAG_ACCODA_E_ANNULLA")
	private Integer flagAccodaEAnnulla;

	@Column(name="FLAG_SOSTITUISCI")
	private Integer flagSostituisci;
	
	@Column(name="FLAG_AGGIORNA")
	private Integer flagAggiorna;
	
	private String vista;
	
	@Column(name="VISTA_S")
	private String vistaS;
	
	@Column(name="DT_ELAB")
	private Timestamp dtElab;
	
	private String descrizione;

	public DmConfCaricamento() {
	}

	public DmConfCaricamentoPK getId() {
		return this.id;
	}

	public void setId(DmConfCaricamentoPK id) {
		this.id = id;
	}

	public String getVista() {
		return vista;
	}

	public void setVista(String vista) {
		this.vista = vista;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Integer getFlagAccoda() {
		return flagAccoda;
	}

	public void setFlagAccoda(Integer flagAccoda) {
		this.flagAccoda = flagAccoda;
	}

	public Integer getFlagAccodaEAnnulla() {
		return flagAccodaEAnnulla;
	}

	public void setFlagAccodaEAnnulla(Integer flagAccodaEAnnulla) {
		this.flagAccodaEAnnulla = flagAccodaEAnnulla;
	}

	public Integer getFlagSostituisci() {
		return flagSostituisci;
	}

	public void setFlagSostituisci(Integer flagSostituisci) {
		this.flagSostituisci = flagSostituisci;
	}

	public Integer getFlagAggiorna() {
		return flagAggiorna;
	}

	public void setFlagAggiorna(Integer flagAggiorna) {
		this.flagAggiorna = flagAggiorna;
	}

	public String getVistaS() {
		return vistaS;
	}

	public void setVistaS(String vistaS) {
		this.vistaS = vistaS;
	}

	public Timestamp getDtElab() {
		return dtElab;
	}

	public void setDtElab(Timestamp dtElab) {
		this.dtElab = dtElab;
	}

	public String getDescTipoCaricamento(){
		if(this.flagAccoda==1)
			return "Accoda";
		else if(this.flagAccodaEAnnulla==1)
			return "Accoda e annulla";
		else if(this.flagSostituisci==1)
			return "Sostituisci";
		else if(this.flagAggiorna==1)
			return "Aggiorna";
		else return "";
		
	}
}