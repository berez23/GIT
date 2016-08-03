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
public class OggettiICIFinder extends EscFinder implements Serializable{
	
		String anagrafe;
		String comune;
		String foglioCatasto;
		String particellaCatasto;
		String parCatastali;
		String strada;
		String superficie;
		String vani;
		String categoria;
		String classe;
		String rendita;
		String titolo;
		String quota;
		String codContribuente;
		String dataInizio;
		String dataFine;
		String partitaIva;
		String codFiscale;
		String subalterno;
		
	    long totaleRecord;
		long totaleRecordFiltrati;
		long paginaAttuale;
		long pagineTotali;
		long righePerPagina;	
		long recordAttuale;
		
		
	public OggettiICIFinder() {
		
			anagrafe = "";comune = "";foglioCatasto = "";particellaCatasto = "";
			parCatastali = "";strada= "";superficie= ""; vani= "";
			categoria= ""; classe= "";rendita= "";
			titolo = "";quota = "";codContribuente = "";
			dataInizio = "";dataFine = "";partitaIva = "";codFiscale = "";
			subalterno="";
			
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
		public String getCategoria() {
			return categoria;
		}

		/**
		 * @return
		 */
		public String getClasse() {
			return classe;
		}

		/**
		 * @return
		 */
		public String getCodContribuente() {
			return codContribuente;
		}

		/**
		 * @return
		 */
		public String getCodFiscale() {
			return codFiscale;
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
		public String getDataFine() {
			return dataFine;
		}

		/**
		 * @return
		 */
		public String getDataInizio() {
			return dataInizio;
		}

		/**
		 * @return
		 */
		public String getFoglioCatasto() {
			return foglioCatasto;
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
		public String getParticellaCatasto() {
			return particellaCatasto;
		}

		/**
		 * @return
		 */
		public String getPartitaIva() {
			return partitaIva;
		}

		/**
		 * @return
		 */
		public String getQuota() {
			return quota;
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
		public String getRendita() {
			return rendita;
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
		public String getStrada() {
			return strada;
		}

		/**
		 * @return
		 */
		public String getSuperficie() {
			return superficie;
		}

		/**
		 * @return
		 */
		public String getTitolo() {
			return titolo;
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
		 * @return
		 */
		public String getVani() {
			return vani;
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
		public void setCategoria(String string) {
			categoria = string;
		}

		/**
		 * @param string
		 */
		public void setClasse(String string) {
			classe = string;
		}

		/**
		 * @param string
		 */
		public void setCodContribuente(String string) {
			codContribuente = string;
		}

		/**
		 * @param string
		 */
		public void setCodFiscale(String string) {
			codFiscale = string;
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
		public void setDataFine(String string) {
			dataFine = string;
		}

		/**
		 * @param string
		 */
		public void setDataInizio(String string) {
			dataInizio = string;
		}

		/**
		 * @param string
		 */
		public void setFoglioCatasto(String string) {
			foglioCatasto = string;
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
		public void setParticellaCatasto(String string) {
			particellaCatasto = string;
		}

		/**
		 * @param string
		 */
		public void setPartitaIva(String string) {
			partitaIva = string;
		}

		/**
		 * @param string
		 */
		public void setQuota(String string) {
			quota = string;
		}

		/**
		 * @param l
		 */
		public void setRecordAttuale(long l) {
			recordAttuale = l;
		}

		/**
		 * @param string
		 */
		public void setRendita(String string) {
			rendita = string;
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
		public void setStrada(String string) {
			strada = string;
		}

		/**
		 * @param string
		 */
		public void setSuperficie(String string) {
			superficie = string;
		}

		/**
		 * @param string
		 */
		public void setTitolo(String string) {
			titolo = string;
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

		/**
		 * @param string
		 */
		public void setVani(String string) {
			vani = string;
		}

		/**
		 * @return
		 */
		public String getSubalterno() {
			return subalterno;
		}

		/**
		 * @param string
		 */
		public void setSubalterno(String string) {
			subalterno = string;
		}

}
