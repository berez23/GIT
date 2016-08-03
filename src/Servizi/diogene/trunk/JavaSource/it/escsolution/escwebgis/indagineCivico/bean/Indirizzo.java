package it.escsolution.escwebgis.indagineCivico.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Indirizzo extends EscObject implements Serializable {
	int pkidStra;
	int pkidCivi;
	String chiave;
	String prefisso;
	String nomeVia;
	String civico;
	String codNazionale;
	String indirizzoCompleto;
	String foglio;
	String particella;
	String latitudine;
	String longitudine;
	boolean fp;
	ArrayList<Indirizzo> fps;
	
	public Indirizzo() {
		pkidStra=0;
		pkidCivi=0;
		prefisso="";
		nomeVia="";
		civico="";
		chiave="";
		codNazionale="";
		indirizzoCompleto="";
		foglio="";
		particella="";
		latitudine="";
		longitudine="";
		fp = false;
		fps = null;
	}
	public Indirizzo(Indirizzo ind) {
		pkidStra=ind.getPkidStra();
		pkidCivi=ind.getPkidCivi();
		prefisso=ind.getPrefisso();
		nomeVia=ind.getNomeVia();
		civico=ind.getCivico();
		chiave=ind.getChiave();
		codNazionale=ind.getCodNazionale();
		indirizzoCompleto=ind.getIndirizzoCompleto();
		fp = ind.isFp();
		fps=ind.getFps();
	}
	public int getPkidStra() {
		return pkidStra;
	}

	public void setPkidStra(int pkidStra) {
		this.pkidStra = pkidStra;
	}

	public int getPkidCivi() {
		return pkidCivi;
	}

	public void setPkidCivi(int pkidCivi) {
		this.pkidCivi = pkidCivi;
	}

	public String getPrefisso() {
		return prefisso;
	}

	public void setPrefisso(String prefisso) {
		this.prefisso = prefisso;
	}

	public String getNomeVia() {
		return nomeVia;
	}

	public void setNomeVia(String nomeVia) {
		this.nomeVia = nomeVia;
	}

	public String getCivico() {
		return civico;
	}

	public void setCivico(String civico) {
		this.civico = civico;
	}

	public String getChiave() {
		return chiave;
	}

	public void setChiave(String chiave) {
		this.chiave = chiave;
	}

	public String getCodNazionale() {
		return codNazionale;
	}

	public void setCodNazionale(String codNazionale) {
		this.codNazionale = codNazionale;
	}

	public String getIndirizzoCompleto() {
		return indirizzoCompleto;
	}

	public void setIndirizzoCompleto(String indirizzoCompleto) {
		this.indirizzoCompleto = indirizzoCompleto;
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
	public boolean isFp() {
		return fp;
	}
	public void setFp(boolean fp) {
		this.fp = fp;
	}
		public ArrayList<Indirizzo> getFps() {
		return fps;
	}
	public void setFps(ArrayList<Indirizzo> fps) {
		this.fps = fps;
	}
	public boolean hasFps() {
		return this.fps != null && this.fps.size() > 0;
	}

}
