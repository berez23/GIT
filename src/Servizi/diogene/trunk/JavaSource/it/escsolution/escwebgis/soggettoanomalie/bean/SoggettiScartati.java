package it.escsolution.escwebgis.soggettoanomalie.bean;

import java.io.Serializable;

public class SoggettiScartati implements Serializable
{
	public static final String NOME_IN_SESSIONE = "SOGGETTI_SCARTATI";
	private String 
		tipoScarto,
		fkDb,
		descrDb,
		fkChiave,
		pkIdInternoSgtScarti,
		urlAnomalie,
		detailedMessage;
	
	public String getFkChiave()
	{
		return fkChiave;
	}
	public void setFkChiave(String fkChiave)
	{
		this.fkChiave = fkChiave;
	}
	public String getFkDb()
	{
		return fkDb;
	}
	public void setFkDb(String fkDb)
	{
		this.fkDb = fkDb;
	}
	public String getPkIdInternoSgtScarti()
	{
		return pkIdInternoSgtScarti;
	}
	public String getDescrDb()
	{
		return descrDb;
	}
	public void setDescrDb(String descrDb)
	{
		this.descrDb = descrDb;
	}
	public void setPkIdInternoSgtScarti(String pkIdInternoSgtScarti)
	{
		this.pkIdInternoSgtScarti = pkIdInternoSgtScarti;
	}
	public String getTipoScarto()
	{
		return tipoScarto;
	}
	public void setTipoScarto(String tipoScarto)
	{
		this.tipoScarto = tipoScarto;
	}
	public String getUrlAnomalie()
	{
		return urlAnomalie;
	}
	public void setUrlAnomalie(String urlAnomalie)
	{
		this.urlAnomalie = urlAnomalie;
	}
	public String getDetailedMessage()
	{
		return detailedMessage;
	}
	public void setDetailedMessage(String detailedMessage)
	{
		this.detailedMessage = detailedMessage;
	}
}
