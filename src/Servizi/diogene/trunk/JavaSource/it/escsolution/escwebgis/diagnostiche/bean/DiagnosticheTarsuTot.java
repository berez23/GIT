
package it.escsolution.escwebgis.diagnostiche.bean;

import it.escsolution.escwebgis.common.EscObject;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class DiagnosticheTarsuTot extends EscObject implements Serializable{
	
	String propResNoTarsu ; 
	
	public String getChiave(){ 
		return "";
	}

	public String getPropResNoTarsu() {
		return propResNoTarsu;
	}

	public void setPropResNoTarsu(String propResNoTarsu) {
		this.propResNoTarsu = propResNoTarsu;
	}


		
}
