/*
 * Created on 10-nov-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.escsolution.escwebgis.acquedotto.bean;

import it.escsolution.escwebgis.common.EscComboObject;

/**
 * @author Utente
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Sedime extends EscComboObject {
	
	private String sedime = "";
	
	public Sedime(String sedime)
	{
		if ("".equals(sedime))
			sedime = " - ";
		if (sedime == null)
			sedime = "";
		this.sedime = sedime;
	}
	
	public String getCode() {
		return sedime;
	}
	public String getDescrizione() {
		if ("".equals(sedime))
			return "Qualsiasi";
		return sedime;
	}
}
