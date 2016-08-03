package it.webred.ct.diagnostics.service.data.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;


@Entity
@Table(name="DIA_TESTATA")
public class DiaTestata implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID")
	private long id;
	
	@Column(name="IDCATALOGODIA")
	private long idCatalogoDia;
	
	@Column(name="PROCESSID")
	private String processId;
	
	@Temporal( TemporalType.TIMESTAMP)
	@Column(name="DATA_ESEC")
	private Date dataEsec;
		
	@Column(name="DATA_RIF")
	private String dataRif;
	
	@Column(name="NUM_REC")
	private long numRec;
	
	@Column(name="DES_PARAM")
	private String desParam;
	
	@Column(name="DES_SQL")
	private String desSql;
	
	@Column(name="TABLE_CLASSNAME")
	private String tableClassname;
	
	@Column(name="STANDARD")
	private String standard;
	
	@Column(name="NOME_CAMPO_FK_TABDETT")
	private String nomeCampoFkTabdett;
	
	@Column(name="NUM_TIPO_GEST")
	private int numTipoGest;
	
	@Column(name="NUM_TIPO_GEST_VALUE")
	private String numTipoGestValue;
	
	public DiaTestata(){}

	public int getNumTipoGest() {
		return numTipoGest;
	}

	public void setNumTipoGest(int numTipoGest) {
		this.numTipoGest = numTipoGest;
	}

	public String getNumTipoGestValue() {
		return numTipoGestValue;
	}

	public void setNumTipoGestValue(String numTipoGestValue) {
		this.numTipoGestValue = numTipoGestValue;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getIdCatalogoDia() {
		return idCatalogoDia;
	}

	public void setIdCatalogoDia(long idCatalogoDia) {
		this.idCatalogoDia = idCatalogoDia;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public Date getDataEsec() {
		return dataEsec;
	}

	public void setDataEsec(Date dataEsec) {
		this.dataEsec = dataEsec;
	}

	public String getDataRif() {
		return dataRif;
	}

	public void setDataRif(String dataRif) {
		this.dataRif = dataRif;
	}

	public long getNumRec() {
		return numRec;
	}

	public void setNumRec(long numRec) {
		this.numRec = numRec;
	}

	public String getDesParam() {
		return desParam;
	}

	public void setDesParam(String desParam) {
		this.desParam = desParam;
	}

	public String getDesSql() {
		return desSql;
	}

	public void setDesSql(String desSql) {
		this.desSql = desSql;
	}

	public String getTableClassname() {
		return tableClassname;
	}

	public void setTableClassname(String tableClassname) {
		this.tableClassname = tableClassname;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getNomeCampoFkTabdett() {
		return nomeCampoFkTabdett;
	}

	public void setNomeCampoFkTabdett(String nomeCampoFkTabdett) {
		this.nomeCampoFkTabdett = nomeCampoFkTabdett;
	}
	
	
}
