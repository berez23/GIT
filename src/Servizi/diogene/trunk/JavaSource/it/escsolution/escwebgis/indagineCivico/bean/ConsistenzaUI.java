package it.escsolution.escwebgis.indagineCivico.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;

public class ConsistenzaUI extends EscObject implements Serializable {
	private String foglio;
	private String mappale;
	private String sub;
	private double supTarsu;
	private String supTarsuNonPres;
	private String dicTarsuCFPi;
	private String dicTarsuDenominazione;
	private String dicTarsuDesClsRsu;
	private String dicTarsuDesTipOgg;
	private double consistenza;
	private double superficie;
	private double supC340;
	private String infoDocfa; // dato visualizzato in lista. Conterrà fornitura/protocollo dell'ultimo docfa per u.i.  
	private DatiDocfa datiDocfa;
	
		
	public ConsistenzaUI() {
		foglio="";
		mappale="";
		sub="";
		supTarsu=0;
		dicTarsuCFPi="";
		consistenza=0;
		superficie=0;
		supC340=0;
		infoDocfa="";
		datiDocfa= new DatiDocfa();
	}
	
	public double getConsistenza() {
		return consistenza;
	}

	public void setConsistenza(double consistenza) {
		this.consistenza = consistenza;
	}

	public double getSuperficie() {
		return superficie;
	}

	public void setSuperficie(double superficie) {
		this.superficie = superficie;
	}

	public double getSupC340() {
		return supC340;
	}

	public void setSupC340(double supC340) {
		this.supC340 = supC340;
	}

	public String getInfoDocfa() {
		return infoDocfa;
	}

	public void setInfoDocfa(String infoDocfa) {
		this.infoDocfa = infoDocfa;
	}

	public DatiDocfa getDatiDocfa() {
		return datiDocfa;
	}

	public void setDatiDocfa(DatiDocfa datiDocfa) {
		this.datiDocfa = datiDocfa;
	}

	public String getFoglio() {
		return foglio;
	}

	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}

	public String getMappale() {
		return mappale;
	}

	public void setMappale(String mappale) {
		this.mappale = mappale;
	}

	public String getSub() {
		return sub;
	}

	public void setSub(String sub) {
		this.sub = sub;
	}

	public double getSupTarsu() {
		return supTarsu;
	}

	public void setSupTarsu(double supTarsu) {
		this.supTarsu = supTarsu;
	}

	public String getSupTarsuNonPres() {
		return supTarsuNonPres;
	}

	public void setSupTarsuNonPres(String supTarsuNonPres) {
		this.supTarsuNonPres = supTarsuNonPres;
	}

	public String getDicTarsuCFPi() {
		return dicTarsuCFPi;
	}

	public void setDicTarsuCFPi(String dicTarsuCFPi) {
		this.dicTarsuCFPi = dicTarsuCFPi;
	}

	public String getDicTarsuDenominazione() {
		return dicTarsuDenominazione;
	}

	public void setDicTarsuDenominazione(String dicTarsuDenominazione) {
		this.dicTarsuDenominazione = dicTarsuDenominazione;
	}

	public String getDicTarsuDesClsRsu() {
		return dicTarsuDesClsRsu;
	}

	public void setDicTarsuDesClsRsu(String dicTarsuDesClsRsu) {
		this.dicTarsuDesClsRsu = dicTarsuDesClsRsu;
	}

	public String getDicTarsuDesTipOgg() {
		return dicTarsuDesTipOgg;
	}

	public void setDicTarsuDesTipOgg(String dicTarsuDesTipOgg) {
		this.dicTarsuDesTipOgg = dicTarsuDesTipOgg;
	}

}
