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
public class ContribuentiFinder extends EscFinder implements Serializable {
	
		 String cognome;
		 String nome;
		 String sesso;
		 String dataNascita;
		 String piano;
		 String scala;
		 String interno;
		 String desComuneNascita;
		 String denominazione;
		 String civico;
		 String codiceSoggetto;
		 String indirizzo;
		 String residenza;
		 String codContribuente;
		 String anagrafe;
		 String comune;
		 String dataCaricamento;
		 String codFiscale;
		 String	partitaIVA;
		 
		 long totaleRecord;
		 long totaleRecordFiltrati;
		 long paginaAttuale;
		 long pagineTotali;
		 long righePerPagina;	
		 long recordAttuale;
		 
		 
		 
	public ContribuentiFinder() {
		cognome = "";nome = "";sesso = "";dataNascita = "";
		piano = "";scala= "";interno= ""; desComuneNascita= "";
		denominazione= ""; civico= "";codiceSoggetto= "";
		indirizzo = "";residenza = "";codContribuente = "";
		anagrafe = "";comune = "";dataCaricamento = "";
		codFiscale="";partitaIVA="";
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
		public String getCivico() {
			return civico;
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
		public String getCodiceSoggetto() {
			return codiceSoggetto;
		}

		/**
		 * @return
		 */
		public String getCognome() {
			return cognome;
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
		public String getDataCaricamento() {
			return dataCaricamento;
		}

		/**
		 * @return
		 */
		public String getDataNascita() {
			return dataNascita;
		}

		/**
		 * @return
		 */
		public String getDenominazione() {
			return denominazione;
		}

		/**
		 * @return
		 */
		public String getDesComuneNascita() {
			return desComuneNascita;
		}

		/**
		 * @return
		 */
		public String getIndirizzo() {
			return indirizzo;
		}

		/**
		 * @return
		 */
		public String getInterno() {
			return interno;
		}

		/**
		 * @return
		 */
		public String getNome() {
			return nome;
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
		public String getPiano() {
			return piano;
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
		public String getResidenza() {
			return residenza;
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
		public String getScala() {
			return scala;
		}

		/**
		 * @return
		 */
		public String getSesso() {
			return sesso;
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
		public void setAnagrafe(String string) {
			anagrafe = string;
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
		public void setCodContribuente(String string) {
			codContribuente = string;
		}

		/**
		 * @param string
		 */
		public void setCodiceSoggetto(String string) {
			codiceSoggetto = string;
		}

		/**
		 * @param string
		 */
		public void setCognome(String string) {
			cognome = string;
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
		public void setDataCaricamento(String string) {
			dataCaricamento = string;
		}

		/**
		 * @param string
		 */
		public void setDataNascita(String string) {
			dataNascita = string;
		}

		/**
		 * @param string
		 */
		public void setDenominazione(String string) {
			denominazione = string;
		}

		/**
		 * @param string
		 */
		public void setDesComuneNascita(String string) {
			desComuneNascita = string;
		}

		/**
		 * @param string
		 */
		public void setIndirizzo(String string) {
			indirizzo = string;
		}

		/**
		 * @param string
		 */
		public void setInterno(String string) {
			interno = string;
		}

		/**
		 * @param string
		 */
		public void setNome(String string) {
			nome = string;
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
		public void setPiano(String string) {
			piano = string;
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
		public void setResidenza(String string) {
			residenza = string;
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
		public void setScala(String string) {
			scala = string;
		}

		/**
		 * @param string
		 */
		public void setSesso(String string) {
			sesso = string;
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
		 * @return
		 */
		public String getCodFiscale() {
			return codFiscale;
		}

		/**
		 * @return
		 */
		public String getPartitaIVA() {
			return partitaIVA;
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
		public void setPartitaIVA(String string) {
			partitaIVA = string;
		}

}
