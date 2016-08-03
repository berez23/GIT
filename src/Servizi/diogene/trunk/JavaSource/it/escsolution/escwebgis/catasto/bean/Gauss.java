/*
 * Created on 12-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.catasto.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Gauss extends EscObject implements Serializable{

		private String comune;
		private String sezione;
		private String foglio;
		private String particella;		
		private String layer;
		private String area;
		private String perimetro;		
		private String particellaCatasto;
		private String chiave;
		private String particellaTerreno;
		long recordAttuale;
		long recordTotali;
		private String fkComune;
		private String subalterno;
		private String dataFine;
		private String indIci;
		private String indCata;
		private String indViarioRif;
		
		private String latitudine;
		private String longitudine;
		
		public String getIndIci()
		{
			return indIci;
		}


		public void setIndIci(String indIci)
		{
			this.indIci = indIci;
		}


		public String getIndCata()
		{
			return indCata;
		}


		public void setIndCata(String indCata)
		{
			this.indCata = indCata;
		}


		public String getIndViarioRif()
		{
			return indViarioRif;
		}


		public void setIndViarioRif(String indViarioRif)
		{
			this.indViarioRif = indViarioRif;
		}


		public Gauss() {
			comune = "";
			fkComune = "";
			sezione = "";
			foglio = "";
			particella = "";
			layer = "";
			area = "";
			perimetro = "";
			particellaCatasto="";
			particellaTerreno="";
			subalterno = "";
			dataFine = "";
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
		public String getChiave() {
			return chiave;
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
		public String getSezione() {
			return sezione;
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
		public String getLayer() {
			return layer;
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
		public String getPerimetro() {
			return perimetro;
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
		public long getRecordTotali() {
			return recordTotali;
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
		public void setChiave(String string) {
			chiave = string;
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
		public void setSezione(String string) {
			sezione = string;
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
		public void setLayer(String string) {
			layer = string;
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
		public void setPerimetro(String string) {
			perimetro = string;
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
		public void setRecordTotali(long l) {
			recordTotali = l;
		}

		/**
		 * @return
		 */
		public String getParticellaCatasto() {
			return particellaCatasto;
		}

		/**
		 * @param string
		 */
		public void setParticellaCatasto(String string) {
			particellaCatasto = string;
		}

		/**
		 * @return
		 */
		public String getParticellaTerreno() {
			return particellaTerreno;
		}

		/**
		 * @param string
		 */
		public void setParticellaTerreno(String string) {
			particellaTerreno = string;
		}

		/**
		 * @return Returns the fkComune.
		 */
		public String getFkComune() {
			return fkComune;
		}
		/**
		 * @param fkComune The fkComune to set.
		 */
		public void setFkComune(String fkComune) {
			this.fkComune = fkComune;
		}
		/**
		 * @return Returns the subalterno.
		 */
		public String getSubalterno() {
			return subalterno;
		}
		/**
		 * @param subalterno The subalterno to set.
		 */
		public void setSubalterno(String subalterno) {
			this.subalterno = subalterno;
		}
		public String getDataFine() {
			return dataFine;
		}
		public void setDataFine(String dataFine) {
			this.dataFine = dataFine;
		}


		public String getLatitudine() {
			return latitudine;
		}


		public void setLatitudine(String latitudine) {
			this.latitudine = latitudine;
		}


		public String getLongitudine() {
			return longitudine;
		}


		public void setLongitudine(String longitudine) {
			this.longitudine = longitudine;
		}
}
