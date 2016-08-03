package it.escsolution.escwebgis.tributiNew.bean;

import it.escsolution.escwebgis.common.EscComboInterface;
import it.escsolution.escwebgis.common.EscComboObject;

import java.io.Serializable;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class TitoloSoggetto extends EscComboObject implements Serializable, EscComboInterface {
	
	private String codTitoloSoggetto;
	private String desTitoloSoggetto;
	
	public String getCode(){
		return codTitoloSoggetto;
	}
	
	public String getDescrizione(){
		return desTitoloSoggetto;
	}
	
	public TitoloSoggetto() {
	}
	
	public TitoloSoggetto(String cod, String  des) {
		codTitoloSoggetto = cod;
		desTitoloSoggetto = des;
	}
	
	public String toString() {
		return desTitoloSoggetto;
	}

	public String getCodTitoloSoggetto() {
		return codTitoloSoggetto;
	}

	public void setCodTitoloSoggetto(String codTitoloSoggetto) {
		this.codTitoloSoggetto = codTitoloSoggetto;
	}

	public String getDesTitoloSoggetto() {
		return desTitoloSoggetto;
	}

	public void setDesTitoloSoggetto(String desTitoloSoggetto) {
		this.desTitoloSoggetto = desTitoloSoggetto;
	}

}

