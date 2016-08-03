package it.webred.fb.ejb.dto;

import it.webred.fb.data.model.DmBBene;
import it.webred.fb.data.model.DmBTitolo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DocumentoDTO implements Serializable {

	private String nomeFile;
	private String ext;
	private DmBBene bene;
	private String codMacro;
	private String codCategoria;
	private String prog;
	private String dtInizio;
	private String dtFine;
	private String dtModifica;
	private String descrizione;
	
	private Date dtIni;
	private Date dtFin;
	private Date dtMod;
	
	
	public String getNomeFile() {
		return nomeFile;
	}
	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getCodMacro() {
		return codMacro;
	}
	public void setCodMacro(String codMacro) {
		this.codMacro = codMacro;
	}
	public String getCodCategoria() {
		return codCategoria;
	}
	public void setCodCategoria(String codCategoria) {
		this.codCategoria = codCategoria;
	}
	public DmBBene getBene() {
		return bene;
	}
	public void setBene(DmBBene bene) {
		this.bene = bene;
	}
	public String getProg() {
		return prog;
	}
	public void setProg(String prog) {
		this.prog = prog;
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
	public String getDtModifica() {
		return dtModifica;
	}
	public void setDtModifica(String dtModifica) {
		this.dtModifica = dtModifica;
	}
	public Date getDtIni() {
		return dtIni;
	}
	public void setDtIni(Date dtIni) {
		this.dtIni = dtIni;
	}
	public Date getDtFin() {
		return dtFin;
	}
	public void setDtFin(Date dtFin) {
		this.dtFin = dtFin;
	}
	public Date getDtMod() {
		return dtMod;
	}
	public void setDtMod(Date dtMod) {
		this.dtMod = dtMod;
	}
	
}
