/*
 * Created on 13-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.istat.bean;

import java.io.Serializable;

/**
 * @author administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Istat implements Serializable{
	
	private String codSezione;
	private String comune;
	private String nomeLocalita;
	private String nomeSezione;
	private String numeroSezione;
	private String popResidTotale;
	private String area;
	private String popResidMaschi;
	private String popResidFemmine;
	private String abitazTotali;
	private String abitazOccupate;
	private String abitazNonOccupate;
	private String abitazAnte1919;
	private String abitazTra1919e1945;
	private String abitazTra1946e1960;
	private String abitazTra1961e1971;
	private String abitazTra1972e1981;
	private String abitazDopo1981;
	
	
	public Istat(){
		codSezione = "";
		comune = "";
		nomeLocalita = "";
		nomeSezione = "";
		numeroSezione = "";
		popResidTotale = "";
		String area = "";
		popResidMaschi = "";
		popResidFemmine = "";
		abitazTotali = "";
		abitazOccupate = "";
		abitazNonOccupate = "";
		abitazAnte1919 = "";
		abitazTra1919e1945 = "";
		abitazTra1946e1960 = "";
		abitazTra1961e1971 = "";
		abitazTra1972e1981 = "";
		abitazDopo1981 = "";
	
	}

	/**
	 * @return
	 */
	public String getChiave() {
			return codSezione;
		}
	
	public String getAbitazAnte1919() {
		return abitazAnte1919;
	}

	/**
	 * @return
	 */
	public String getAbitazDopo1981() {
		return abitazDopo1981;
	}

	/**
	 * @return
	 */
	public String getAbitazNonOccupate() {
		return abitazNonOccupate;
	}

	/**
	 * @return
	 */
	public String getAbitazOccupate() {
		return abitazOccupate;
	}

	/**
	 * @return
	 */
	public String getAbitazTotali() {
		return abitazTotali;
	}

	/**
	 * @return
	 */
	public String getAbitazTra1919e1945() {
		return abitazTra1919e1945;
	}

	/**
	 * @return
	 */
	public String getAbitazTra1946e1960() {
		return abitazTra1946e1960;
	}

	/**
	 * @return
	 */
	public String getAbitazTra1961e1971() {
		return abitazTra1961e1971;
	}

	/**
	 * @return
	 */
	public String getAbitazTra1972e1981() {
		return abitazTra1972e1981;
	}

	/**
	 * @return
	 */
	public String getArea() {
		return area;
	}

	/**
	 * @return
	 */
	public String getCodSezione() {
		return codSezione;
	}

	/**
	 * @return
	 */
	public String getComune() {
		return comune;
	}

	/**
	 * @return
	 */
	public String getNomeLocalita() {
		return nomeLocalita;
	}

	/**
	 * @return
	 */
	public String getNomeSezione() {
		return nomeSezione;
	}

	/**
	 * @return
	 */
	public String getNumeroSezione() {
		return numeroSezione;
	}

	/**
	 * @return
	 */
	public String getPopResidFemmine() {
		return popResidFemmine;
	}

	/**
	 * @return
	 */
	public String getPopResidMaschi() {
		return popResidMaschi;
	}

	/**
	 * @return
	 */
	public String getPopResidTotale() {
		return popResidTotale;
	}

	/**
	 * @param string
	 */
	public void setAbitazAnte1919(String string) {
		abitazAnte1919 = string;
	}

	/**
	 * @param string
	 */
	public void setAbitazDopo1981(String string) {
		abitazDopo1981 = string;
	}

	/**
	 * @param string
	 */
	public void setAbitazNonOccupate(String string) {
		abitazNonOccupate = string;
	}

	/**
	 * @param string
	 */
	public void setAbitazOccupate(String string) {
		abitazOccupate = string;
	}

	/**
	 * @param string
	 */
	public void setAbitazTotali(String string) {
		abitazTotali = string;
	}

	/**
	 * @param string
	 */
	public void setAbitazTra1919e1945(String string) {
		abitazTra1919e1945 = string;
	}

	/**
	 * @param string
	 */
	public void setAbitazTra1946e1960(String string) {
		abitazTra1946e1960 = string;
	}

	/**
	 * @param string
	 */
	public void setAbitazTra1961e1971(String string) {
		abitazTra1961e1971 = string;
	}

	/**
	 * @param string
	 */
	public void setAbitazTra1972e1981(String string) {
		abitazTra1972e1981 = string;
	}

	/**
	 * @param string
	 */
	public void setArea(String string) {
		area = string;
	}

	/**
	 * @param string
	 */
	public void setCodSezione(String string) {
		codSezione = string;
	}

	/**
	 * @param string
	 */
	public void setComune(String string) {
		comune = string;
	}

	/**
	 * @param string
	 */
	public void setNomeLocalita(String string) {
		nomeLocalita = string;
	}

	/**
	 * @param string
	 */
	public void setNomeSezione(String string) {
		nomeSezione = string;
	}

	/**
	 * @param string
	 */
	public void setNumeroSezione(String string) {
		numeroSezione = string;
	}

	/**
	 * @param string
	 */
	public void setPopResidFemmine(String string) {
		popResidFemmine = string;
	}

	/**
	 * @param string
	 */
	public void setPopResidMaschi(String string) {
		popResidMaschi = string;
	}

	/**
	 * @param string
	 */
	public void setPopResidTotale(String string) {
		popResidTotale = string;
	}

	
}
