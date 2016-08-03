package it.webred.fb.data.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the DM_CONF_DOC_DIR database table.
 * 
 */
@Entity
@Table(name="DM_CONF_DOC_DIR")
@NamedQuery(name="DmConfDocDir.findAll", query="SELECT d FROM DmConfDocDir d")
public class DmConfDocDir implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String codice;

	private String descrizione;
	
	private String percorso;
	
	private int abilitato;

	//bi-directional many-to-one association to DmDDoc
	@OneToMany(mappedBy="dmConfDocDir")
	private List<DmDDoc> dmDDocs;

	//bi-directional many-to-one association to DmConfDocLog
	@OneToMany(mappedBy="dmConfDocDir")
	@OrderBy("id.dtElab DESC")
	private List<DmConfDocLog> dmConfDocLogs;

	public DmConfDocDir() {
	}

	public String getCodice() {
		return this.codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public List<DmDDoc> getDmDDocs() {
		return this.dmDDocs;
	}

	public String getPercorso() {
		return percorso;
	}

	public void setPercorso(String percorso) {
		this.percorso = percorso;
	}

	public int getAbilitato() {
		return abilitato;
	}

	public void setAbilitato(int abilitato) {
		this.abilitato = abilitato;
	}

	public void setDmDDocs(List<DmDDoc> dmDDocs) {
		this.dmDDocs = dmDDocs;
	}

	public DmDDoc addDmDDoc(DmDDoc dmDDoc) {
		getDmDDocs().add(dmDDoc);
		dmDDoc.setDmConfDocDir(this);

		return dmDDoc;
	}

	public DmDDoc removeDmDDoc(DmDDoc dmDDoc) {
		getDmDDocs().remove(dmDDoc);
		dmDDoc.setDmConfDocDir(null);

		return dmDDoc;
	}

	public List<DmConfDocLog> getDmConfDocLogs() {
		return this.dmConfDocLogs;
	}

	public void setDmConfDocLogs(List<DmConfDocLog> dmConfDocLogs) {
		this.dmConfDocLogs = dmConfDocLogs;
	}

	public DmConfDocLog addDmConfDocLog(DmConfDocLog dmConfDocLog) {
		getDmConfDocLogs().add(dmConfDocLog);
		dmConfDocLog.setDmConfDocDir(this);

		return dmConfDocLog;
	}

	public DmConfDocLog removeDmConfDocLog(DmConfDocLog dmConfDocLog) {
		getDmConfDocLogs().remove(dmConfDocLog);
		dmConfDocLog.setDmConfDocDir(null);

		return dmConfDocLog;
	}

}