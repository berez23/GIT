
package it.escsolution.escwebgis.diagnostiche.bean;

import it.escsolution.escwebgis.common.EscComboInterface;
import it.escsolution.escwebgis.common.EscComboObject;
import it.escsolution.escwebgis.common.EscObject;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class DateFornituraDocfa extends EscComboObject implements Serializable,EscComboInterface {
	
	String idFornitura ; 
	String dataFornitura;
	
	public String getCode(){
		return idFornitura;
	}
	public String getDescrizione(){
		return dataFornitura;
	}
	public DateFornituraDocfa() {
	}
	public DateFornituraDocfa(String id,String  data) {
		idFornitura = id;
		dataFornitura = data;
	}
	public String toString() {
		return dataFornitura;
	}
	/**
	 * @return
	 */
	public String getIdFornitura() {
		return idFornitura;
	}

	/**
	 * @return
	 */
	public String getDataFornitura() {
		return dataFornitura;
	}

	/**
	 * @param string
	 */
	public void setCodComune(String id) {
		idFornitura = id;
	}

	/**
	 * @param string
	 */
	public void setDesComune(String data) {
		dataFornitura = data;
	}



}
