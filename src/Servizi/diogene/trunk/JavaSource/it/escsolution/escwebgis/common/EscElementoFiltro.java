/*
 * Created on 29-apr-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.common;

import it.escsolution.eiv.database.Fonti;

import java.io.Serializable;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class EscElementoFiltro implements Serializable {
	
	
	private static final Logger log = Logger.getLogger("diogene.log");

	private String label;
	private String attributeName;
	private String tipo;
	private Vector listaOperatori;
	private Vector listaValori;
	private String valore;
	private String campoFiltrato;
	private String campoJS;
	private Integer maxSizeCombo;
	private String operatore;
	private boolean soloLabel;
	private String extraHTML;
	private Integer comboSize;
	private String[] valori;
	private boolean checkBox;
	private boolean checkList;
	private boolean obbligatorio;
	
	/**
	 * 
	 */
	public EscElementoFiltro() 
	{
		label = "";
		tipo= "";
		listaOperatori= null;
		listaValori=null;
		valore= "";
		attributeName = "";
		campoFiltrato = "";
		operatore = "";
		campoJS = "";
		soloLabel=false;
		maxSizeCombo=null;
		extraHTML="";
		comboSize=1; //default per combo box
		checkBox=false;
		checkList=false;
		obbligatorio= false;
	}

	public String getExtraHTML()
	{
		return extraHTML;
	}

	public void setExtraHTML(String extraHTML)
	{
		this.extraHTML = extraHTML;
		log.debug("ExtraHTML: " + extraHTML);
	}

	/**
	 * @return
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @return
	 */
	public Vector getListaValori() {
		return listaValori;
	}


	/**
	 * @return
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @return
	 */
	public String getValore() {
		return valore;
	}

	/**
	 * @param string
	 */
	public void setLabel(String string) {
		label = string;
	}

	/**
	 * @param vector
	 */
	public void setListaValori(Vector vector) {
		listaValori = vector;
	}



	/**
	 * @param string
	 */
	public void setTipo(String string) {
		tipo = string;
	}

	/**
	 * @param string
	 */
	public void setValore(String string) {
		valore = string;
	}

	/**
	 * @return
	 */
	public Vector getListaOperatori() {
		return listaOperatori;
	}

	/**
	 * @param vector
	 */
	public void setListaOperatori(Vector vector) {
		listaOperatori = vector;
	}

	/**
	 * @return
	 */
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * @param string
	 */
	public void setAttributeName(String string) {
		attributeName = string;
	}

	/**
	 * @return
	 */
	public String getCampoFiltrato() {
		return campoFiltrato;
	}

	/**
	 * @param string
	 */
	public void setCampoFiltrato(String string) {
		campoFiltrato = string;
	}

	/**
	 * @return
	 */
	public String getOperatore() {
		return operatore;
	}

	/**
	 * @param string
	 */
	public void setOperatore(String string) {
		operatore = string;
	}

	/**
	 * @return
	 */
	public String getCampoJS() {
		return campoJS;
	}

	/**
	 * @param string
	 */
	public void setCampoJS(String string) {
		campoJS = string;
	}

	

	public boolean isSoloLabel() {
		return soloLabel;
	}

	public void setSoloLabel(boolean soloLabel) {
		this.soloLabel = soloLabel;
	}

	public Integer getMaxSizeCombo()
	{
		return maxSizeCombo;
	}

	public void setMaxSizeCombo(Integer maxSizeCombo)
	{
		this.maxSizeCombo = maxSizeCombo;
	}

	public Integer getComboSize() {
		return comboSize;
	}

	public void setComboSize(Integer comboSize) {
		this.comboSize = comboSize;
	}

	public String[] getValori() {
		return valori;
	}

	public void setValori(String[] valori) {
		this.valori = valori;
	}

	public boolean isCheckBox() {
		return checkBox;
	}

	public void setCheckBox(boolean checkBox) {
		this.checkBox = checkBox;
	}

	public boolean isCheckList() {
		return checkList;
	}

	public void setCheckList(boolean checkList) {
		this.checkList = checkList;
	}

	public boolean isObbligatorio() {
		return obbligatorio;
	}

	public void setObbligatorio(boolean obbligatorio) {
		this.obbligatorio = obbligatorio;
	}
	
}
