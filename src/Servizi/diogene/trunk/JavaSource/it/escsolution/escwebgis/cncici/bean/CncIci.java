
package it.escsolution.escwebgis.cncici.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;
import java.util.ArrayList;

public class CncIci extends EscObject implements Serializable{

	private String fkFornitura; 
	private String protocollo;
	private String nrPacco;   
	private String progressivo;
	
	private ArrayList<CncIciContitolari> contitolari;
	private ArrayList<CncIciImmobili> immobili;
	
	private String contribuenteCodiceFiscale;
	private String contribuenteCognome;
	private String contribuenteNome;
	private String contribuenteNasData;
	private String contribuenteSesso;
	private String contribuenteNasComune;
	private String contribuenteNasProvincia;
	private String contribuenteDFIndirizzo; 
	private String contribuenteDFCap; 
	private String contribuenteDFComune;
	private String contribuenteDFProvincia; 
	
	private String denuncianteCognome;
	private String denuncianteCodiceFiscale;
	private String denuncianteDFIndirizzo; 
	private String denuncianteDFCap; 
	private String denuncianteDFComune;
	private String denuncianteDFProvincia; 	

	public String getChiave()
	{
		return 	fkFornitura+"|"+protocollo+"|"+nrPacco+"|"+progressivo;
	}
public class CncIciContitolari implements Serializable
{
	
	private String conCodiceFiscale;
	private String conDFIndirizzo; 
	private String conDFComune;
	private String conDFProvincia;
	private String possMesi;
	private String possPerc;
	public String getConCodiceFiscale()
	{
		return conCodiceFiscale;
	}
	public void setConCodiceFiscale(String conCodiceFiscale)
	{
		this.conCodiceFiscale = conCodiceFiscale;
	}
	public String getConDFIndirizzo()
	{
		return conDFIndirizzo;
	}
	public void setConDFIndirizzo(String conDFIndirizzo)
	{
		this.conDFIndirizzo = conDFIndirizzo;
	}
	public String getConDFComune()
	{
		return conDFComune;
	}
	public void setConDFComune(String conDFComune)
	{
		this.conDFComune = conDFComune;
	}
	public String getConDFProvincia()
	{
		return conDFProvincia;
	}
	public void setConDFProvincia(String conDFProvincia)
	{
		this.conDFProvincia = conDFProvincia;
	}
	public String getPossMesi()
	{
		return possMesi;
	}
	public void setPossMesi(String possMesi)
	{
		this.possMesi = possMesi;
	}
	public String getPossPerc()
	{
		return possPerc;
	}
	public void setPossPerc(String possPerc)
	{
		this.possPerc = possPerc;
	}

	
}
	
public class CncIciImmobili implements Serializable
{
	private String nrOrdine;
	private String sezione;
	private String foglio;
	private String particella;
	private String subalterno;
	private String protocolloCat;
	private String categoria;
	private String classe;
	private String indirizzo;
	private String valoreRendita;
	private String possessoPerc;
	private String possessoMesi;
	
	public String getValoreRendita()
	{
		return valoreRendita;
	}
	public void setValoreRendita(String valoreRendita)
	{
		this.valoreRendita = valoreRendita;
	}
	public String getPossessoPerc()
	{
		return possessoPerc;
	}
	public void setPossessoPerc(String possessoPerc)
	{
		this.possessoPerc = possessoPerc;
	}
	public String getPossessoMesi()
	{
		return possessoMesi;
	}
	public void setPossessoMesi(String possessoMesi)
	{
		this.possessoMesi = possessoMesi;
	}
	public String getNrOrdine()
	{
		return nrOrdine;
	}
	public void setNrOrdine(String nrOrdine)
	{
		this.nrOrdine = nrOrdine;
	}
	public String getSezione()
	{
		return sezione;
	}
	public void setSezione(String sezione)
	{
		this.sezione = sezione;
	}
	public String getFoglio()
	{
		return foglio;
	}
	public void setFoglio(String foglio)
	{
		this.foglio = foglio;
	}

	public String getParticella()
	{
		return particella;
	}
	public void setParticella(String particella)
	{
		this.particella = particella;
	}
	public String getSubalterno()
	{
		return subalterno;
	}
	public void setSubalterno(String subalterno)
	{
		this.subalterno = subalterno;
	}
	public String getProtocolloCat()
	{
		return protocolloCat;
	}
	public void setProtocolloCat(String protocolloCat)
	{
		this.protocolloCat = protocolloCat;
	}
	public String getCategoria()
	{
		return categoria;
	}
	public void setCategoria(String categoria)
	{
		this.categoria = categoria;
	}
	public String getClasse()
	{
		return classe;
	}
	public void setClasse(String classe)
	{
		this.classe = classe;
	}
	public String getIndirizzo()
	{
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo)
	{
		this.indirizzo = indirizzo;
	}

}



public ArrayList<CncIciContitolari> getContitolari()
{
	return contitolari;
}



public void setContitolari(ArrayList<CncIciContitolari> contitolari)
{
	this.contitolari = contitolari;
}



public ArrayList<CncIciImmobili> getImmobili()
{
	return immobili;
}



public void setImmobili(ArrayList<CncIciImmobili> immobili)
{
	this.immobili = immobili;
}



public String getDenuncianteDFIndirizzo()
{
	return denuncianteDFIndirizzo;
}



public void setDenuncianteDFIndirizzo(String denuncianteDFIndirizzo)
{
	this.denuncianteDFIndirizzo = denuncianteDFIndirizzo;
}



public String getDenuncianteDFCap()
{
	return denuncianteDFCap;
}



public void setDenuncianteDFCap(String denuncianteDFCap)
{
	this.denuncianteDFCap = denuncianteDFCap;
}



public String getDenuncianteDFComune()
{
	return denuncianteDFComune;
}



public void setDenuncianteDFComune(String denuncianteDFComune)
{
	this.denuncianteDFComune = denuncianteDFComune;
}



public String getDenuncianteDFProvincia()
{
	return denuncianteDFProvincia;
}



public void setDenuncianteDFProvincia(String denuncianteDFProvincia)
{
	this.denuncianteDFProvincia = denuncianteDFProvincia;
}



public String getFkFornitura()
{
	return fkFornitura;
}



public void setFkFornitura(String fkFornitura)
{
	this.fkFornitura = fkFornitura;
}



public String getProtocollo()
{
	return protocollo;
}



public void setProtocollo(String protocollo)
{
	this.protocollo = protocollo;
}



public String getNrPacco()
{
	return nrPacco;
}



public void setNrPacco(String nrPacco)
{
	this.nrPacco = nrPacco;
}



public String getProgressivo()
{
	return progressivo;
}



public void setProgressivo(String progressivo)
{
	this.progressivo = progressivo;
}



public String getContribuenteCodiceFiscale()
{
	return contribuenteCodiceFiscale;
}



public void setContribuenteCodiceFiscale(String contribuenteCodiceFiscale)
{
	this.contribuenteCodiceFiscale = contribuenteCodiceFiscale;
}



public String getContribuenteCognome()
{
	return contribuenteCognome;
}



public void setContribuenteCognome(String contribuenteCognome)
{
	this.contribuenteCognome = contribuenteCognome;
}



public String getContribuenteNome()
{
	return contribuenteNome;
}



public void setContribuenteNome(String contribuenteNome)
{
	this.contribuenteNome = contribuenteNome;
}



public String getContribuenteNasData()
{
	return contribuenteNasData;
}



public void setContribuenteNasData(String contribuenteNasData)
{
	this.contribuenteNasData = contribuenteNasData;
}



public String getContribuenteSesso()
{
	return contribuenteSesso;
}



public void setContribuenteSesso(String contribuenteSesso)
{
	this.contribuenteSesso = contribuenteSesso;
}



public String getContribuenteNasComune()
{
	return contribuenteNasComune;
}



public void setContribuenteNasComune(String contribuenteNasComune)
{
	this.contribuenteNasComune = contribuenteNasComune;
}



public String getContribuenteNasProvincia()
{
	return contribuenteNasProvincia;
}



public void setContribuenteNasProvincia(String contribuenteNasProvincia)
{
	this.contribuenteNasProvincia = contribuenteNasProvincia;
}



public String getContribuenteDFIndirizzo()
{
	return contribuenteDFIndirizzo;
}



public void setContribuenteDFIndirizzo(String contribuenteDFIndirizzo)
{
	this.contribuenteDFIndirizzo = contribuenteDFIndirizzo;
}



public String getContribuenteDFCap()
{
	return contribuenteDFCap;
}



public void setContribuenteDFCap(String contribuenteDFCap)
{
	this.contribuenteDFCap = contribuenteDFCap;
}



public String getContribuenteDFComune()
{
	return contribuenteDFComune;
}



public void setContribuenteDFComune(String contribuenteDFComune)
{
	this.contribuenteDFComune = contribuenteDFComune;
}



public String getContribuenteDFProvincia()
{
	return contribuenteDFProvincia;
}



public void setContribuenteDFProvincia(String contribuenteDFProvincia)
{
	this.contribuenteDFProvincia = contribuenteDFProvincia;
}



public String getDenuncianteCognome()
{
	return denuncianteCognome;
}



public void setDenuncianteCognome(String denuncianteCognome)
{
	this.denuncianteCognome = denuncianteCognome;
}



public String getDenuncianteCodiceFiscale()
{
	return denuncianteCodiceFiscale;
}



public void setDenuncianteCodiceFiscale(String denuncianteCodiceFiscale)
{
	this.denuncianteCodiceFiscale = denuncianteCodiceFiscale;
}

}
