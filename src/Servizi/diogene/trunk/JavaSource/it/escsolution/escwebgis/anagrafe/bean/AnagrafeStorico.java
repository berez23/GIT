/*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.anagrafe.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AnagrafeStorico extends EscObject implements Serializable{

	private String matricola;
	private String codFiscale;
	private String cognome;
	private String nome;
	private String dataNascita;
	private String sesso;
	private String comuneNascita;
	private String dataRiferimento;
	private String codiceVia;
	private String descrVia;
	private String civico;
	private String esponenteCivico;
	private String inizioResidenza;
	private String fineResidenza;
	private String inizioFamiglia;
	private String inizioFamigliaOrig;
	private String fineFamiglia;
	private String inizioResidenzaElab;
	private String fineResidenzaElab;
	private String fkFamiglia;
	private String codFamiglia;
	private List<String> listaIndirizzi;
	private ArrayList elencoFamiliari;  
	

	public AnagrafeStorico(){
    }
	
	public String getChiave() {
		return matricola;
	}
	
	public String getCodFiscale() {
		return codFiscale;
	}

	public String getCognome() {
		return cognome;
	}

	public String getDataNascita() {
		return dataNascita;
	}

	public String getNome() {
		return nome;
	}

	/**
	 * @return
	 */
	public String getSesso() {
		return sesso;
	}

	public void setCodFiscale(String string) {
		codFiscale = string;
	}

	/**
	 * @param string
	 */
	public void setCognome(String string) {
		cognome = string;
	}

public void setComuneNascita(String string) {
		comuneNascita = string;
	}

	public void setDataNascita(String string) {
		dataNascita = string;
	}

	/**
	 * @param string
	 */
	public void setNome(String string) {
		nome = string;
	}

	/**
	 * @param string
	 */
	public void setSesso(String string) {
		sesso = string;
	}


	public String getCivico() {
		return civico;
	}


	public void setCivico(String civico) {
		this.civico = civico;
	}


	public String getCodiceVia() {
		return codiceVia;
	}


	public void setCodiceVia(String codiceVia) {
		this.codiceVia = codiceVia;
	}


	public String getDataRiferimento() {
		return dataRiferimento;
	}


	public void setDataRiferimento(String dataRiferimento) {
		this.dataRiferimento = dataRiferimento;
	}


	public String getEsponenteCivico() {
		return esponenteCivico;
	}


	public void setEsponenteCivico(String esponenteCivico) {
		this.esponenteCivico = esponenteCivico;
	}


	public String getFineResidenza() {
		return fineResidenza;
	}


	public void setFineResidenza(String fineResidenza) {
		this.fineResidenza = fineResidenza;
	}


	public String getInizioResidenza() {
		return inizioResidenza;
	}


	public void setInizioResidenza(String inizioResidenza) {
		this.inizioResidenza = inizioResidenza;
	}


	public String getMatricola() {
		return matricola;
	}


	public void setMatricola(String matricola) {
		this.matricola = matricola;
	}


	public String getComuneNascita() {
		return comuneNascita;
	}


	public String getFineResidenzaElab() {
		return fineResidenzaElab;
	}


	public void setFineResidenzaElab(String fineResidenzaElab) {
		this.fineResidenzaElab = fineResidenzaElab;
	}


	public String getInizioResidenzaElab() {
		return inizioResidenzaElab;
	}


	public void setInizioResidenzaElab(String inizioResidenzaElab) {
		this.inizioResidenzaElab = inizioResidenzaElab;
	}


	public String getDescrVia() {
		return descrVia;
	}


	public void setDescrVia(String descrVia) {
		this.descrVia = descrVia;
	}


	public String getFkFamiglia() {
		return fkFamiglia;
	}


	public void setFkFamiglia(String fkFamiglia) {
		this.fkFamiglia = fkFamiglia;
	}


	public ArrayList getElencoFamiliari() {
		return elencoFamiliari;
	}


	public void setElencoFamiliari(ArrayList elencoFamiliari) {
		this.elencoFamiliari = elencoFamiliari;
	}


	public String getCodFamiglia() {
		return codFamiglia;
	}


	public void setCodFamiglia(String codFamiglia) {
		this.codFamiglia = codFamiglia;
	}
	
	public String getInizioFamiglia() {
		return inizioFamiglia;
	}

	public void setInizioFamiglia(String inizioFamiglia) {
		this.inizioFamiglia = inizioFamiglia;
	}

	public String getInizioFamigliaOrig() {
		return inizioFamigliaOrig;
	}

	public void setInizioFamigliaOrig(String inizioFamigliaOrig) {
		this.inizioFamigliaOrig = inizioFamigliaOrig;
	}

	public String getFineFamiglia() {
		return fineFamiglia;
	}

	public void setFineFamiglia(String fineFamiglia) {
		this.fineFamiglia = fineFamiglia;
	}

	public List<String> getListaIndirizzi() {
		return listaIndirizzi;
	}

	public void setListaIndirizzi(List<String> listaIndirizzi) {
		this.listaIndirizzi = listaIndirizzi;
	}

}
