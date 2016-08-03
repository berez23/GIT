
package it.escsolution.escwebgis.diagnostiche.bean;

import it.escsolution.escwebgis.common.EscObject;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class DiagnosticheDocfaNoRes extends EscObject implements Serializable{
	
	String fornitura ; 
	String nrecDatiCensuari;
	String nrecDataRegNoCoer;
	String nrecTipoOperNoUnico;
	String nrecTipoOperNull;
	String nrecTipoOperCess;
	String nrecDocfaOK;
	String nrecUIU;
	
	public String getChiave(){ 
		return ""+fornitura;
	}
	
	public String getFornitura() {
		return fornitura;
	}
	public void setFornitura(String fornitura) {
		this.fornitura = fornitura;
	}
	public String getNrecDatiCensuari() {
		return nrecDatiCensuari;
	}
	public void setNrecDatiCensuari(String nrecDatiCensuari) {
		this.nrecDatiCensuari = nrecDatiCensuari;
	}
	public String getNrecDataRegNoCoer() {
		return nrecDataRegNoCoer;
	}
	public void setNrecDataRegNoCoer(String nrecDataRegNoCoer) {
		this.nrecDataRegNoCoer = nrecDataRegNoCoer;
	}
	public String getNrecTipoOperNoUnico() {
		return nrecTipoOperNoUnico;
	}
	public void setNrecTipoOperNoUnico(String nrecTipoOperNoUnico) {
		this.nrecTipoOperNoUnico = nrecTipoOperNoUnico;
	}
	public String getNrecTipoOperNull() {
		return nrecTipoOperNull;
	}
	public void setNrecTipoOperNull(String nrecTipoOperNull) {
		this.nrecTipoOperNull = nrecTipoOperNull;
	}
	public String getNrecTipoOperCess() {
		return nrecTipoOperCess;
	}
	public void setNrecTipoOperCess(String nrecTipoOperCess) {
		this.nrecTipoOperCess = nrecTipoOperCess;
	}
	public String getNrecDocfaOK() {
		return nrecDocfaOK;
	}
	public void setNrecDocfaOK(String nrecDocfaOK) {
		this.nrecDocfaOK = nrecDocfaOK;
	}
	public String getNrecUIU() {
		return nrecUIU;
	}
	public void setNrecUIU(String nrecUIU) {
		this.nrecUIU = nrecUIU;
	}
	
		



}
