package it.webred.cet.service.ff.web.beans.dettaglio;

import it.webred.cet.service.ff.web.FFBaseBean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public abstract class DatiDettaglio  extends FFBaseBean implements Serializable {

	private String sezione;
	private String foglio;
	private String particella;
	private String sub;
	private String codNaz;
	private Date dataRif;
	private Date dtRic;
	
	public String getSezione() {
		if (sezione != null && (sezione.equals("") || sezione.equals("-")))
				return null;


		return sezione;
	}
	public void setSezione(String sezione) {
		this.sezione = sezione;
	}
	public String getFoglio() {
		return foglio;
	}
	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}
	public String getParticella() {
		return particella;
	}
	public void setParticella(String particella) {
		this.particella = particella;
	}
	public String getSub() {
		return sub;
	}
	public void setSub(String sub) {
		this.sub = sub;
	}
	
	public String getCodNazionale() {
		return super.getEnte();
	}
	
	
	
	public Date getDataRif() {
	    if (dataRif == null)
	    	return dataRif;
		GregorianCalendar gc = new GregorianCalendar();
		gc.set(GregorianCalendar.HOUR_OF_DAY, 0);
		gc.set(GregorianCalendar.MINUTE, 0);
		gc.set(GregorianCalendar.SECOND, 0);
		gc.set(GregorianCalendar.MILLISECOND, 0);
		Date oggi =gc.getTime();
		if (dataRif.compareTo(oggi ) == 0)
			dataRif=null;
		return dataRif;
	}
	public void setDataRif(Date dataRif) {
	//	logger.debug("-- Get Data Rif ["+dataRif+"]");
		this.dataRif = dataRif;
	}
	public abstract void doSwitch() ;
	
	
	public Date getDtRic() {
		return dtRic;
	}
	public void setDtRic(Date dtRic) {
		this.dtRic = dtRic;
	}
	
	public void setDataRifStr(String val) {
		logger.debug("Data Rif Str ["+val+"]");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			dataRif =  sdf.parse(val);
		}
		catch(Exception t) {
			logger.warn("data riferimento errata", t);
		}
	}
	public String getDataRifStr() {
		if (dataRif==null)
			return "";
		String dataRifStr="";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			dataRifStr =  sdf.format(dataRif);
		}
		catch(Throwable t) {
			logger.warn("data riferimento errata 2", t);
		}
		return dataRifStr;
	}
}
