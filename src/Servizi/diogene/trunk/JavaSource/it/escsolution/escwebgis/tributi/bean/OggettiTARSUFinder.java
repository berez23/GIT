/*
 * Created on 28-apr-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.tributi.bean;

import it.escsolution.escwebgis.common.EscFinder;
import it.webred.ct.support.audit.annotation.AuditInherit;

import java.io.Serializable;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
@AuditInherit
public class OggettiTARSUFinder extends EscFinder implements Serializable{
	
		String comune;
		String anagrafe;
		String codSoggetto;
		String foglio;
		String particella;
		String subalterno;
		String parCatastali;
		String civico;
		String garage;
		String fondo;
		String soffitta;
		String commerciale;
		String artigianale;
		String produttivo;
		String servizi;
		String accessori;
		String abitazione;
		String supTotale;
		String contribuente;
		String dataCaricamento;
		
		
		long totaleRecord;
		long totaleRecordFiltrati;
		long paginaAttuale;
		long pagineTotali;
		long righePerPagina;	
		long recordAttuale;
		
	public OggettiTARSUFinder(){
			
			comune = "";anagrafe = "";codSoggetto = "";foglio = "";
			particella = "";subalterno = "";parCatastali = "";civico = "";
			garage = "";fondo = "";soffitta = "";commerciale = "";
			artigianale = "";produttivo = "";servizi = "";accessori = "";
			abitazione = "";supTotale = "";contribuente = "";dataCaricamento = "";	
		}
	

		/**
		 * @return
		 */
		public String getAbitazione() {
			return abitazione;
		}

		/**
		 * @return
		 */
		public String getAccessori() {
			return accessori;
		}

		/**
		 * @return
		 */
		public String getAnagrafe() {
			return anagrafe;
		}

		/**
		 * @return
		 */
		public String getArtigianale() {
			return artigianale;
		}

		/**
		 * @return
		 */
		public String getCivico() {
			return civico;
		}

		/**
		 * @return
		 */
		public String getCodSoggetto() {
			return codSoggetto;
		}

		/**
		 * @return
		 */
		public String getCommerciale() {
			return commerciale;
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
		public String getContribuente() {
			return contribuente;
		}

		/**
		 * @return
		 */
		public String getDataCaricamento() {
			return dataCaricamento;
		}

		/**
		 * @return
		 */
		public String getFoglio() {
			return foglio;
		}

		/**
		 * @return
		 */
		public String getFondo() {
			return fondo;
		}

		/**
		 * @return
		 */
		public String getGarage() {
			return garage;
		}

		/**
		 * @return
		 */
		public long getPaginaAttuale() {
			return paginaAttuale;
		}

		/**
		 * @return
		 */
		public long getPagineTotali() {
			return pagineTotali;
		}

		/**
		 * @return
		 */
		public String getParCatastali() {
			return parCatastali;
		}

		/**
		 * @return
		 */
		public String getParticella() {
			return particella;
		}

		/**
		 * @return
		 */
		public String getProduttivo() {
			return produttivo;
		}

		/**
		 * @return
		 */
		public long getRecordAttuale() {
			return recordAttuale;
		}

		/**
		 * @return
		 */
		public long getRighePerPagina() {
			return righePerPagina;
		}

		/**
		 * @return
		 */
		public String getServizi() {
			return servizi;
		}

		/**
		 * @return
		 */
		public String getSoffitta() {
			return soffitta;
		}

		/**
		 * @return
		 */
		public String getSubalterno() {
			return subalterno;
		}

		/**
		 * @return
		 */
		public String getSupTotale() {
			return supTotale;
		}

		/**
		 * @return
		 */
		public long getTotaleRecord() {
			return totaleRecord;
		}

		/**
		 * @return
		 */
		public long getTotaleRecordFiltrati() {
			return totaleRecordFiltrati;
		}

		/**
		 * @param string
		 */
		public void setAbitazione(String string) {
			abitazione = string;
		}

		/**
		 * @param string
		 */
		public void setAccessori(String string) {
			accessori = string;
		}

		/**
		 * @param string
		 */
		public void setAnagrafe(String string) {
			anagrafe = string;
		}

		/**
		 * @param string
		 */
		public void setArtigianale(String string) {
			artigianale = string;
		}

		/**
		 * @param string
		 */
		public void setCivico(String string) {
			civico = string;
		}

		/**
		 * @param string
		 */
		public void setCodSoggetto(String string) {
			codSoggetto = string;
		}

		/**
		 * @param string
		 */
		public void setCommerciale(String string) {
			commerciale = string;
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
		public void setContribuente(String string) {
			contribuente = string;
		}

		/**
		 * @param string
		 */
		public void setDataCaricamento(String string) {
			dataCaricamento = string;
		}

		/**
		 * @param string
		 */
		public void setFoglio(String string) {
			foglio = string;
		}

		/**
		 * @param string
		 */
		public void setFondo(String string) {
			fondo = string;
		}

		/**
		 * @param string
		 */
		public void setGarage(String string) {
			garage = string;
		}

		/**
		 * @param l
		 */
		public void setPaginaAttuale(long l) {
			paginaAttuale = l;
		}

		/**
		 * @param l
		 */
		public void setPagineTotali(long l) {
			pagineTotali = l;
		}

		/**
		 * @param string
		 */
		public void setParCatastali(String string) {
			parCatastali = string;
		}

		/**
		 * @param string
		 */
		public void setParticella(String string) {
			particella = string;
		}

		/**
		 * @param string
		 */
		public void setProduttivo(String string) {
			produttivo = string;
		}

		/**
		 * @param l
		 */
		public void setRecordAttuale(long l) {
			recordAttuale = l;
		}

		/**
		 * @param l
		 */
		public void setRighePerPagina(long l) {
			righePerPagina = l;
		}

		/**
		 * @param string
		 */
		public void setServizi(String string) {
			servizi = string;
		}

		/**
		 * @param string
		 */
		public void setSoffitta(String string) {
			soffitta = string;
		}

		/**
		 * @param string
		 */
		public void setSubalterno(String string) {
			subalterno = string;
		}

		/**
		 * @param string
		 */
		public void setSupTotale(String string) {
			supTotale = string;
		}

		/**
		 * @param l
		 */
		public void setTotaleRecord(long l) {
			totaleRecord = l;
		}

		/**
		 * @param l
		 */
		public void setTotaleRecordFiltrati(long l) {
			totaleRecordFiltrati = l;
		}

}
