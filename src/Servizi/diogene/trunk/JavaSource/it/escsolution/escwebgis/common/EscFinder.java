/*
 * Created on 20-apr-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.common;

import java.io.Serializable;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class EscFinder implements Serializable{
	protected long totaleRecord;
	protected long totaleRecordFiltrati;
	protected long paginaAttuale;
	protected long pagineTotali;
	protected long righePerPagina;
	
	long recordAttuale;
	
	protected String keyStr;
	protected String queryForKeyStr;

	/**
	 * 
	 */
	public EscFinder() {
		keyStr = "";
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
	public String getKeyStr() {
		if (queryForKeyStr==null)
			return keyStr;
		else
			return queryForKeyStr;
	}

	/**
	 * @param string
	 */
	public void setKeyStr(String string) {
		keyStr = string;
	}

	public void setQueryForKeyStr(String string) {
		queryForKeyStr = string;
	}
	public String getQueryForKeyStr() {
		return queryForKeyStr ;
	}

}
