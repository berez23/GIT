package it.escsolution.escwebgis.cosapNew.bean;

import it.escsolution.escwebgis.common.EscComboInterface;
import it.escsolution.escwebgis.common.EscComboObject;

import java.io.Serializable;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class TipoSoggetto extends EscComboObject implements Serializable, EscComboInterface {
	
	private String codTipoSoggetto;
	private String desTipoSoggetto;
	
	public String getCode(){
		return codTipoSoggetto;
	}
	
	public String getDescrizione(){
		return desTipoSoggetto;
	}
	
	public TipoSoggetto() {
	}
	
	public TipoSoggetto(String cod, String  des) {
		codTipoSoggetto = cod;
		desTipoSoggetto = des;
	}
	
	public String toString() {
		return desTipoSoggetto;
	}

	public String getCodTipoSoggetto() {
		return codTipoSoggetto;
	}

	public void setCodTipoSoggetto(String codTipoSoggetto) {
		this.codTipoSoggetto = codTipoSoggetto;
	}

	public String getDesTipoSoggetto() {
		return desTipoSoggetto;
	}

	public void setDesTipoSoggetto(String desTipoSoggetto) {
		this.desTipoSoggetto = desTipoSoggetto;
	}

}

