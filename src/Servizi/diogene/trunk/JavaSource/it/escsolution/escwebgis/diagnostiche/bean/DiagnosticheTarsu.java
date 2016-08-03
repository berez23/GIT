
package it.escsolution.escwebgis.diagnostiche.bean;

import it.escsolution.escwebgis.common.EscObject;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class DiagnosticheTarsu extends EscObject implements Serializable{
	
	String foglio ; 
	String particella;
	String sub;
	String unimm;
	String categoria;
	String supCat;
	String percPoss;
	String cognome;
	String nome;
	String denominazione;
	String dtNascita;
	String posAna;
	String civi;
	String topo;
	String anagDa;
	String elimina;
	String cfis;
	
	public String getChiave(){ 
		return "";
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


	public String getUnimm() {
		return unimm;
	}


	public void setUnimm(String unimm) {
		this.unimm = unimm;
	}


	public String getCategoria() {
		return categoria;
	}


	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}


	public String getSupCat() {
		return supCat;
	}


	public void setSupCat(String supCat) {
		this.supCat = supCat;
	}


	public String getPercPoss() {
		return percPoss;
	}


	public void setPercPoss(String percPoss) {
		this.percPoss = percPoss;
	}


	public String getCognome() {
		return cognome;
	}


	public void setCognome(String cognome) {
		this.cognome = cognome;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getDenominazione() {
		return denominazione;
	}


	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}


	public String getDtNascita() {
		return dtNascita;
	}


	public void setDtNascita(String dtNascita) {
		this.dtNascita = dtNascita;
	}


	public String getPosAna() {
		return posAna;
	}


	public void setPosAna(String posAna) {
		this.posAna = posAna;
	}


	public String getCivi() {
		return civi;
	}


	public void setCivi(String civi) {
		this.civi = civi;
	}


	public String getTopo() {
		return topo;
	}


	public void setTopo(String topo) {
		this.topo = topo;
	}


	public String getAnagDa() {
		return anagDa;
	}


	public void setAnagDa(String anagDa) {
		this.anagDa = anagDa;
	}


	public String getElimina() {
		return elimina;
	}


	public void setElimina(String elimina) {
		this.elimina = elimina;
	}


	public String getCfis() {
		return cfis;
	}


	public void setCfis(String cfis) {
		this.cfis = cfis;
	}
	
	
}
