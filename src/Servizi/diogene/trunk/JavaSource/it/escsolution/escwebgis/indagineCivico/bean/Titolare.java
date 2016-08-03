package it.escsolution.escwebgis.indagineCivico.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;

public class Titolare extends EscObject implements Serializable {
	private String codFisc;
	private String denom;
	private String tipoTitolo;
	
	public Titolare() {
		codFisc="";
		denom="";
		tipoTitolo="";
	}
	
	public Titolare(Titolare tit) {
		codFisc=tit.getCodFisc();
		denom= tit.getDenom();
		tipoTitolo= tit.getTipoTitolo();
	}
	public String getCodFisc() {
		return codFisc;
	}
	public void setCodFisc(String codFisc) {
		this.codFisc = codFisc;
	}
	public String getDenom() {
		return denom;
	}
	public void setDenom(String denom) {
		this.denom = denom;
	}

	public String getTipoTitolo() {
		return tipoTitolo;
	}

	public void setTipoTitolo(String tipoTitolo) {
		this.tipoTitolo = tipoTitolo;
	} 
}
