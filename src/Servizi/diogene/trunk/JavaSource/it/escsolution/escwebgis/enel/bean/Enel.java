/*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.enel.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;
/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Enel extends EscObject implements Serializable
{
	private String pkId;
	private String codent;
	private String nominativo;
	private String pivacodfisc;
	private String dataValidita;
	private String sedime;
	private String indirizzo;
	private String identificativo;
	private String statoUtenza;
    private String fkIdentificativo;
    private String potenzaImpegnata;
    private String consumoMedioMensile;
    private String dataAllaccio;
    private String dataContratto;
    private String codiceContratto;
    private String recapitoNominativo;
    private String recapitoIndirizzo;
    private String recapitoLocalita;
    private String recapitoCap;
    private String semestre;
    private String tipoUtenza;
    private String categMerce;
	
	
	public Enel()
	{
		this.codent = "";
		this.dataValidita = "";
		this.nominativo = "";
		this.pkId = "";
		this.pivacodfisc = "";
		this.sedime = "";
		this.indirizzo = "";
		this.identificativo ="";
		this.statoUtenza ="";
	    this.fkIdentificativo="";
	    this.potenzaImpegnata="";
	    this.consumoMedioMensile="";
	    this.dataAllaccio="";
	    this.dataContratto="";
	    this.codiceContratto="";
	    this.recapitoNominativo="";
	    this.recapitoIndirizzo="";
	    this.recapitoLocalita="";
	    this.recapitoCap="";
	    this.semestre="";
	    this.tipoUtenza="";
	    this.categMerce="";

	}

	public String getChiave()
	{
		return pkId;
	}

	public String getCodent()
	{
		return codent;
	}

	public void setCodent(String codent)
	{
		this.codent = codent;
	}

	public String getDataValidita()
	{
		return dataValidita;
	}

	public void setDataValidita(String dataValidita)
	{
		this.dataValidita = dataValidita;
	}

	public String getNominativo()
	{
		return nominativo;
	}

	public void setNominativo(String nominativo)
	{
		this.nominativo = nominativo;
	}

	public String getPkId()
	{
		return pkId;
	}

	public void setPkId(String pdId)
	{
		this.pkId = pdId;
	}

	public String getPivacodfisc()
	{
		return pivacodfisc;
	}

	public void setPivacodfisc(String pivacodfisc)
	{
		this.pivacodfisc = pivacodfisc;
	}

	public String getIndirizzo()
	{
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo)
	{
		this.indirizzo = indirizzo;
	}

	public String getSedime()
	{
		return sedime;
	}

	public void setSedime(String sedime)
	{
		this.sedime = sedime;
	}

	public String getIdentificativo()
	{
		return identificativo;
	}

	public void setIdentificativo(String identificativo)
	{
		this.identificativo = identificativo;
	}

	public String getStatoUtenza()
	{
		return statoUtenza;
	}

	public void setStatoUtenza(String statoUtenza)
	{
		this.statoUtenza = statoUtenza;
	}

	public String getCategMerce()
	{
		return categMerce;
	}

	public void setCategMerce(String categMerce)
	{
		this.categMerce = categMerce;
	}

	public String getCodiceContratto()
	{
		return codiceContratto;
	}

	public void setCodiceContratto(String codiceContratto)
	{
		this.codiceContratto = codiceContratto;
	}

	public String getConsumoMedioMensile()
	{
		return consumoMedioMensile;
	}

	public void setConsumoMedioMensile(String consumoMedioMensile)
	{
		this.consumoMedioMensile = consumoMedioMensile;
	}

	public String getDataAllaccio()
	{
		return dataAllaccio;
	}

	public void setDataAllaccio(String dataAllaccio)
	{
		this.dataAllaccio = dataAllaccio;
	}

	public String getDataContratto()
	{
		return dataContratto;
	}

	public void setDataContratto(String dataContratto)
	{
		this.dataContratto = dataContratto;
	}

	public String getFkIdentificativo()
	{
		return fkIdentificativo;
	}

	public void setFkIdentificativo(String fkIdentificativo)
	{
		this.fkIdentificativo = fkIdentificativo;
	}

	public String getPotenzaImpegnata()
	{
		return potenzaImpegnata;
	}

	public void setPotenzaImpegnata(String potenzaImpegnata)
	{
		this.potenzaImpegnata = potenzaImpegnata;
	}

	public String getRecapitoCap()
	{
		return recapitoCap;
	}

	public void setRecapitoCap(String recapitoCap)
	{
		this.recapitoCap = recapitoCap;
	}

	public String getRecapitoIndirizzo()
	{
		return recapitoIndirizzo;
	}

	public void setRecapitoIndirizzo(String recapitoIndirizzo)
	{
		this.recapitoIndirizzo = recapitoIndirizzo;
	}

	public String getRecapitoLocalita()
	{
		return recapitoLocalita;
	}

	public void setRecapitoLocalita(String recapitoLocalita)
	{
		this.recapitoLocalita = recapitoLocalita;
	}

	public String getRecapitoNominativo()
	{
		return recapitoNominativo;
	}

	public void setRecapitoNominativo(String recapitoNominativo)
	{
		this.recapitoNominativo = recapitoNominativo;
	}

	public String getSemestre()
	{
		return semestre;
	}

	public void setSemestre(String semestre)
	{
		this.semestre = semestre;
	}

	public String getTipoUtenza()
	{
		return tipoUtenza;
	}

	public void setTipoUtenza(String tipoUtenza)
	{
		this.tipoUtenza = tipoUtenza;
	}

	
}
