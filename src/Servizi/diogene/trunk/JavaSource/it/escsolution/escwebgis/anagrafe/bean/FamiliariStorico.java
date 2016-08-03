/*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.anagrafe.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;
import java.util.Date;
/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class FamiliariStorico extends EscObject implements Serializable{

	private String id;
	private String cognome;
	private String nome;
	private String tipoParentela;
	private String dtInizio;
	private String dtFine;
	private String dtNascita;
	
	private Date dataInizioRif;
	private Date dataFineRif;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDtInizio() {
		return dtInizio;
	}

	public void setDtInizio(String dtInizio) {
		this.dtInizio = dtInizio;
	}

	public String getDtFine() {
		return dtFine;
	}

	public void setDtFine(String dtFine) {
		this.dtFine = dtFine;
	}

	public FamiliariStorico(){
    }

	public String getCognome() {
		return cognome;
	}

	public String getNome() {
		return nome;
	}

	public void setCognome(String string) {
		cognome = string;
	}

	public void setNome(String string) {
		nome = string;
	}

	public String getTipoParentela() {
		return tipoParentela;
	}

	public void setTipoParentela(String tipoParentela) {
		this.tipoParentela = tipoParentela;
	}

	public String getDtNascita() {
		return dtNascita;
	}

	public void setDtNascita(String dtNascita) {
		this.dtNascita = dtNascita;
	}

	public Date getDataInizioRif() {
		return dataInizioRif;
	}

	public void setDataInizioRif(Date dataInizioRif) {
		this.dataInizioRif = dataInizioRif;
	}

	public Date getDataFineRif() {
		return dataFineRif;
	}

	public void setDataFineRif(Date dataFineRif) {
		this.dataFineRif = dataFineRif;
	}

}
