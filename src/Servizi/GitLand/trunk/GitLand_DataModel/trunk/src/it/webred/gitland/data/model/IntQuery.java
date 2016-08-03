package it.webred.gitland.data.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "INT_QUERY")
public class IntQuery extends MasterItem {
	@Id
	@Column(name="ID", unique = true, nullable = false)
	private Long id = 0l;

	@Column(name="DESCRIZIONE")
	private String descrizione = "";
	
	@Column(name="QUERY")
	private String query = "";
	
	@Column(name="WHERE_DINAMICA")
	private String whereDinamica = "";
	
	@Column(name="ORDER_BY")
	private String orderBy = "";
	
	@Column(name="QUERY_NATIVA")
	private String queryNativa = "";
	
	@Column(name="MAPPING_RISULTATO")
	private String mappingRisultato = "";
	
	@Column(name="TIPO_MAPPING")
	private String tipoMapping = "";
	
	@Column(name="ORIENTAMENTO")
	private String orientamento = "";
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ID_CATEGORIA")
	private IntCategoria categoria  = null;
	
	@OneToMany(fetch=FetchType.EAGER, mappedBy="query")
	private List<IntParametro> parametri = null;

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

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public IntCategoria getCategoria() {
		return categoria;
	}

	public void setCategoria(IntCategoria categoria) {
		this.categoria = categoria;
	}

	public List<IntParametro> getParametri() {
		return parametri;
	}

	public void setParametri(List<IntParametro> parametri) {
		this.parametri = parametri;
	}

	public String getWhereDinamica() {
		return whereDinamica;
	}

	public void setWhereDinamica(String whereDinamica) {
		this.whereDinamica = whereDinamica;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getQueryNativa() {
		return queryNativa;
	}

	public void setQueryNativa(String queryNativa) {
		this.queryNativa = queryNativa;
	}

	public String getMappingRisultato() {
		return mappingRisultato;
	}

	public void setMappingRisultato(String mappingRisultato) {
		this.mappingRisultato = mappingRisultato;
	}

	public String getTipoMapping() {
		return tipoMapping;
	}

	public void setTipoMapping(String tipoMapping) {
		this.tipoMapping = tipoMapping;
	}

	public String getOrientamento() {
		return orientamento;
	}

	public void setOrientamento(String orientamento) {
		this.orientamento = orientamento;
	}

}
