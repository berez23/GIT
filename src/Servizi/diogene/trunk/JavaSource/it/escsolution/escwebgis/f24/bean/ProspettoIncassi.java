/*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.f24.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ProspettoIncassi extends EscObject implements Serializable
{
	private String condizione;
	private String annoRif;
	private String versato;
	private String tipoImposta;
	private String codTributo;
	private String descTributo;
	private Integer fabbricati;
	

	public String getDescTipoImposta() {
		
		String imp = this.getTipoImposta();
		if("I".equalsIgnoreCase(imp))
			return  "ICI/IMU";
		else if("O".equalsIgnoreCase(imp))
			return"TOSAP/COSAP";
		else if("T".equalsIgnoreCase(imp))
			return "TARSU/TARIFFA";
		else if("S".equalsIgnoreCase(imp))
			return "Tassa di scopo";
		else if("R".equalsIgnoreCase(imp))
			return "Contributo/Imposta di soggiorno";
		else if("U".equalsIgnoreCase(imp))
			return "Tasi";
		else if("A".equalsIgnoreCase(imp))
			return "TARES";
		else
			return imp;
		
	}


	

	public String getCondizione() {
		return condizione;
	}


	public void setCondizione(String condizione) {
		this.condizione = condizione;
	}





	public String getAnnoRif() {
		return annoRif;
	}


	public void setAnnoRif(String annoRif) {
		this.annoRif = annoRif;
	}


	public String getVersato() {
		return versato;
	}


	public void setVersato(String versato) {
		this.versato = versato;
	}


	public String getTipoImposta() {
		return tipoImposta;
	}


	public void setTipoImposta(String tipoImposta) {
		this.tipoImposta = tipoImposta;
	}


	public String getCodTributo() {
		return codTributo;
	}


	public void setCodTributo(String codTributo) {
		this.codTributo = codTributo;
	}


	public String getDescTributo() {
		return descTributo;
	}


	public void setDescTributo(String descTributo) {
		this.descTributo = descTributo;
	}


	public Integer getFabbricati() {
		return fabbricati;
	}


	public void setFabbricati(Integer fabbricati) {
		this.fabbricati = fabbricati;
	}


}
