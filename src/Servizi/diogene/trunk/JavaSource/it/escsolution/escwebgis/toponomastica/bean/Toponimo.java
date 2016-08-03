/*
 * Created on 14-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.toponomastica.bean;

import it.escsolution.escwebgis.common.EscComboInterface;
import it.escsolution.escwebgis.common.EscComboObject;

import java.io.Serializable;
/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Toponimo extends EscComboObject implements Serializable,EscComboInterface {

		private String codToponimo;
		private String desToponimo;
		
	public String getCode(){
			return codToponimo;
		}
		public String getDescrizione(){
			return desToponimo;
		}
		public Toponimo() {
		}
		public Toponimo(String cod,String  des) {
			codToponimo = cod;
			desToponimo = des;
		}
		public String toString() {
			return desToponimo;
		}
		/**
		 * @return
		 */
		public String getCodToponimo() {
			return codToponimo;
		}

		/**
		 * @return
		 */
		public String getDesToponimo() {
			return desToponimo;
		}

		/**
		 * @param string
		 */
		public void setCodDestinazione(String string) {
			codToponimo = string;
		}

		/**
		 * @param string
		 */
		public void setDesDestinazione(String string) {
			desToponimo = string;
		}

	}


