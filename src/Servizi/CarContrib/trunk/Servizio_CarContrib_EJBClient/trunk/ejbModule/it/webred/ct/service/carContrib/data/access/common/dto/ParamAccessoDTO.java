package it.webred.ct.service.carContrib.data.access.common.dto;
import java.io.Serializable;
import java.util.Date;

import it.webred.ct.data.access.basic.anagrafe.dto.RicercaSoggettoAnagrafeDTO;
import it.webred.ct.data.access.basic.ici.dto.RicercaSoggettoIciDTO;
import it.webred.ct.data.access.basic.tarsu.dto.RicercaSoggettoTarsuDTO;
import it.webred.ct.support.datarouter.CeTBaseObject;
public class ParamAccessoDTO  extends CeTBaseObject implements Serializable{
	//Attributi associati alla ricerca del soggetto: utilizzato per la ricerca del soggetto dalla pagina di filtro
	private static final long serialVersionUID = 1L;
	private String provenienzaDatiIci;
	private String provenienzaDatiTarsu;
	private String tipoSogg;
	private String codFis;
	private String cognome;
	private String nome;
	private Date dtNas;
	private String parIva;
	private String denom; 
	
	private Date dtRif;
	
	public String getTipoSogg() {
		return tipoSogg;
	}

	public void setTipoSogg(String tipoSogg) {
		this.tipoSogg = tipoSogg;
	}

	public String getCodFis() {
		return codFis;
	}

	public void setCodFis(String codFis) {
		this.codFis = codFis;
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

	public Date getDtNas() {
		return dtNas;
	}

	public void setDtNas(Date dtNas) {
		this.dtNas = dtNas;
	}

	public String getParIva() {
		return parIva;
	}

	public void setParIva(String parIva) {
		this.parIva = parIva;
	}

	public String getDenom() {
		return denom;
	}

	public void setDenom(String denom) {
		this.denom = denom;
	}
	
	public String getProvenienzaDatiIci() {
		return provenienzaDatiIci;
	}

	public void setProvenienzaDatiIci(String provenienzaDatiIci) {
		this.provenienzaDatiIci = provenienzaDatiIci;
	}

	public String getProvenienzaDatiTarsu() {
		return provenienzaDatiTarsu;
	}

	public void setProvenienzaDatiTarsu(String provenienzaDatiTarsu) {
		this.provenienzaDatiTarsu = provenienzaDatiTarsu;
	}
	
	public Date getDtRif() {
		return dtRif;
	}

	public void setDtRif(Date dtRif) {
		this.dtRif = dtRif;
	}

	public void setUpperCase() {
		codFis = codFis!= null ? codFis.toUpperCase(): codFis;
		nome = nome!= null ? nome.toUpperCase(): nome;
		cognome = cognome != null?  cognome.toUpperCase(): cognome;
		denom = denom != null?  denom.toUpperCase(): denom;
		tipoSogg = tipoSogg != null ?  tipoSogg.toUpperCase(): tipoSogg;
		provenienzaDatiIci  = provenienzaDatiIci != null ?  provenienzaDatiIci.toUpperCase(): provenienzaDatiIci;
		provenienzaDatiTarsu  = provenienzaDatiTarsu != null ?  provenienzaDatiTarsu.toUpperCase(): provenienzaDatiTarsu;
	}
	
	public RicercaSoggettoAnagrafeDTO valRicercaAnag() {
		RicercaSoggettoAnagrafeDTO rsa = new RicercaSoggettoAnagrafeDTO();
		rsa.setEnteId(this.getEnteId() );
		rsa.setCodFis(codFis);
		rsa.setCognome(cognome);
		rsa.setNome(nome);
		rsa.setDtNas(dtNas);
		rsa.setUpperCase();
		return rsa;
	}
	public RicercaSoggettoIciDTO valRicercaIci() {
		RicercaSoggettoIciDTO rsi = new RicercaSoggettoIciDTO();
		rsi.setEnteId(this.getEnteId() );
		rsi.setTipoSogg(tipoSogg);
		rsi.setCodFis(codFis);
		rsi.setCognome(cognome);
		rsi.setNome(nome);
		rsi.setDtNas(dtNas);
		rsi.setParIva(parIva);
		rsi.setDenom(denom);
		rsi.setProvenienza(provenienzaDatiIci);
		rsi.setUpperCase();
		return rsi;
	}
	public RicercaSoggettoTarsuDTO valRicercaTarsu() {
		RicercaSoggettoTarsuDTO rst = new RicercaSoggettoTarsuDTO();
		rst.setEnteId(this.getEnteId() );
		rst.setTipoSogg(tipoSogg);
		rst.setCodFis(codFis);
		rst.setCognome(cognome);
		rst.setNome(nome);
		rst.setDtNas(dtNas);
		rst.setParIva(parIva);
		rst.setDenom(denom);
		rst.setProvenienza(provenienzaDatiTarsu);
		rst.setUpperCase();
		return rst;
	}
}
