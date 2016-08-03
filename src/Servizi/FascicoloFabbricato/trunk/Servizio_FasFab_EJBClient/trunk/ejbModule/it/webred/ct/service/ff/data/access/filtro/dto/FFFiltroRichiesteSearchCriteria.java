package it.webred.ct.service.ff.data.access.filtro.dto;

import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;
import java.util.Date;

public class FFFiltroRichiesteSearchCriteria extends CeTBaseObject implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String sezione;
	private String foglio;
	private String particella;
	private Date dataRichiestaDal;
	private Date dataRichiestaAl;
	private String tipoRichiesta;
	private boolean richiesteEvase;
	private boolean richiesteNonEvase;
	private Date dataEvasioneDal;
	private Date dataEvasioneAl;
	private String idRichiesta;
	private String userGesRic;
	public String getSezione() {
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
	public Date getDataRichiestaDal() {
		return dataRichiestaDal;
	}
	public void setDataRichiestaDal(Date dataRichiestaDal) {
		this.dataRichiestaDal = dataRichiestaDal;
	}
	public Date getDataRichiestaAl() {
		return dataRichiestaAl;
	}
	public void setDataRichiestaAl(Date dataRichiestaAl) {
		this.dataRichiestaAl = dataRichiestaAl;
	}
	public void setTipoRichiesta(String tipoRichiesta) {
		this.tipoRichiesta = tipoRichiesta;
	}
	public String getTipoRichiesta() {
		return tipoRichiesta;
	}
	public boolean isRichiesteEvase() {
		return richiesteEvase;
	}
	public void setRichiesteEvase(boolean richiesteEvase) {
		this.richiesteEvase = richiesteEvase;
	}
	public boolean isRichiesteNonEvase() {
		return richiesteNonEvase;
	}
	public void setRichiesteNonEvase(boolean richiesteNonEvase) {
		this.richiesteNonEvase = richiesteNonEvase;
	}
	public Date getDataEvasioneDal() {
		return dataEvasioneDal;
	}
	public void setDataEvasioneDal(Date dataEvasioneDal) {
		this.dataEvasioneDal = dataEvasioneDal;
	}
	public Date getDataEvasioneAl() {
		return dataEvasioneAl;
	}
	public void setDataEvasioneAl(Date dataEvasioneAl) {
		this.dataEvasioneAl = dataEvasioneAl;
	}
	public String getIdRichiesta() {
		return idRichiesta;
	}
	public void setIdRichiesta(String idRichiesta) {
		this.idRichiesta = idRichiesta;
	}
	public String getUserGesRic() {
		return userGesRic;
	}
	public void setUserGesRic(String userGesRic) {
		this.userGesRic = userGesRic;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
