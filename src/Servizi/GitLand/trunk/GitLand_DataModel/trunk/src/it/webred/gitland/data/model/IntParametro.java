package it.webred.gitland.data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "INT_PARAMETRO")
public class IntParametro extends MasterItem {
	@Id
	@Column(name="ID", unique = true, nullable = false)
	private Long id = 0l;

	@Column(name="DESCRIZIONE")
	private String descrizione = "";
	
	@Column(name="OBBLIGATORIO")
	private String obbligatorio = "";
	
	@Column(name="NOME_PARAMETRO")
	private String nomeParametro = "";
	
	@Column(name="AGGIUNTA_WHERE")
	private String aggiuntaWhere = "";
	
	@Column(name="DATA_TYPE")
	private String dataType = "";
	
	@Column(name="DYNAFORM_TYPE")
	private String dynaformType = "";

	@Column(name="MATCH_TYPE")
	private String matchType = "";
	
	@Column(name="TIPO")
	private String tipo = "";

	@Column(name="VISIBILE")
	private String visibile = "";

	@Column(name="EDITABILE")
	private String editabile = "";

	@Column(name="RIGA")
	private Long riga = 0l;

	@Column(name="POSIZIONE")
	private Long posizione = 0l;

	@Column(name="ROWSPAN")
	private Long rowSpan = 0l;

	@Column(name="COLSPAN")
	private Long colSpan = 0l;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ID_QUERY")
	private IntQuery query =null;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7717439305449623382L;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getObbligatorio() {
		return obbligatorio;
	}

	public void setObbligatorio(String obbligatorio) {
		this.obbligatorio = obbligatorio;
	}

	public String getNomeParametro() {
		return nomeParametro;
	}

	public void setNomeParametro(String nomeParametro) {
		this.nomeParametro = nomeParametro;
	}

	public String getAggiuntaWhere() {
		return aggiuntaWhere;
	}

	public void setAggiuntaWhere(String aggiuntaWhere) {
		this.aggiuntaWhere = aggiuntaWhere;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDynaformType() {
		return dynaformType;
	}

	public void setDynaformType(String dynaformType) {
		this.dynaformType = dynaformType;
	}

	public IntQuery getQuery() {
		return query;
	}

	public void setQuery(IntQuery query) {
		this.query = query;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getVisibile() {
		return visibile;
	}

	public void setVisibile(String visibile) {
		this.visibile = visibile;
	}

	public String getEditabile() {
		return editabile;
	}

	public void setEditabile(String editabile) {
		this.editabile = editabile;
	}

	public Long getRiga() {
		return riga;
	}

	public void setRiga(Long riga) {
		this.riga = riga;
	}

	public Long getPosizione() {
		return posizione;
	}

	public void setPosizione(Long posizione) {
		this.posizione = posizione;
	}

	public Long getRowSpan() {
		return rowSpan;
	}

	public void setRowSpan(Long rowSpan) {
		this.rowSpan = rowSpan;
	}

	public Long getColSpan() {
		return colSpan;
	}

	public void setColSpan(Long colSpan) {
		this.colSpan = colSpan;
	}

	public String getMatchType() {
		return matchType;
	}

	public void setMatchType(String matchType) {
		this.matchType = matchType;
	}

}
