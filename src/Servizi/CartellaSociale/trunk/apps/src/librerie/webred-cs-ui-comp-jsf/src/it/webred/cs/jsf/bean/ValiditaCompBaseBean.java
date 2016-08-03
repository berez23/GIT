package it.webred.cs.jsf.bean;

import it.webred.cs.data.DataModelCostanti;
import it.webred.cs.jsf.interfaces.IDatiValidita;
import it.webred.cs.jsf.manbean.superc.CsUiCompBaseBean;

import java.util.Date;

public class ValiditaCompBaseBean extends CsUiCompBaseBean implements IDatiValidita{

	private Long id;
	private String tipo;
	private String descrizione;
	private Date dataInizio;
	private Date dataFine;
	private Date dataTemp = new Date();
	
	protected void reset() {
		id = null;
		dataFine = null;
		dataInizio = null;
	}
	
	@Override
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public Date getDataInizio() {
		return dataInizio;
	}
	
	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}
	
	@Override
	public Date getDataFine() {
		return dataFine;
	}
	
	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}
	
	@Override
	public boolean isAttivo() {
		if (dataInizio != null) 
			return (dataInizio.before(new Date()) || dataInizio.equals(new Date())) && (dataFine == null || dataFine.after(new Date()));
		else return false;
	}
	
	@Override
	public boolean isFinito() {
		if (dataFine != null) 
			return dataFine.compareTo(DataModelCostanti.END_DATE) != 0;
		else return false;
	}

	@Override
	public Date getDataTemp() {
		return dataTemp;
	}

	public void setDataTemp(Date dataTemp) {
		this.dataTemp = dataTemp;
	}

	@Override
	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	
	@Override
	public String getTipo() {
		return tipo;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}
