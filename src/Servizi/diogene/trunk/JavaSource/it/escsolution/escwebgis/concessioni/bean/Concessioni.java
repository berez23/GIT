/*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.concessioni.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;
/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Concessioni extends EscObject implements Serializable{

	private Integer id;                  
	private String codente;             
	private String numero;              
	private String tipo;                
	private String dataProt;           
	private String nProt;              
	private String richiedente;         
	private String richiedente2;        
	private String codfis;              
	private String oggetto;             
	private String indirizzoConc;      
	private String civico;              
	private String foglio;              
	private String particelle;          
	private String dataRilascio;       
	private String dataValidita;       
	private String dataFineValidita;
	private String subalterno;
	private String zona;
	private String procedimento;

	public String getChiave()
	{
		return id.toString();
	}

	public String getCivico()
	{
		return civico;
	}

	public void setCivico(String civico)
	{
		this.civico = civico;
	}

	public String getCodente()
	{
		return codente;
	}

	public void setCodente(String codente)
	{
		this.codente = codente;
	}

	public String getCodfis()
	{
		return codfis;
	}

	public void setCodfis(String codfis)
	{
		this.codfis = codfis;
	}

	public String getDataFineValidita()
	{
		return dataFineValidita;
	}

	public void setDataFineValidita(String dataFineValidita)
	{
		this.dataFineValidita = dataFineValidita;
	}

	public String getDataProt()
	{
		return dataProt;
	}

	public void setDataProt(String dataProt)
	{
		this.dataProt = dataProt;
	}

	public String getDataRilascio()
	{
		return dataRilascio;
	}

	public void setDataRilascio(String dataRilascio)
	{
		this.dataRilascio = dataRilascio;
	}

	public String getDataValidita()
	{
		return dataValidita;
	}

	public void setDataValidita(String dataValidita)
	{
		this.dataValidita = dataValidita;
	}

	public String getFoglio()
	{
		return foglio;
	}

	public void setFoglio(String foglio)
	{
		this.foglio = foglio;
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getIndirizzoConc()
	{
		return indirizzoConc;
	}

	public void setIndirizzoConc(String indirizzoConc)
	{
		this.indirizzoConc = indirizzoConc;
	}

	public String getNProt()
	{
		return nProt;
	}

	public void setNProt(String prot)
	{
		nProt = prot;
	}

	public String getNumero()
	{
		return numero;
	}

	public void setNumero(String numero)
	{
		this.numero = numero;
	}

	public String getOggetto()
	{
		return oggetto;
	}

	public void setOggetto(String oggetto)
	{
		this.oggetto = oggetto;
	}

	public String getParticelle()
	{
		return particelle;
	}

	public void setParticelle(String particelle)
	{
		this.particelle = particelle;
	}

	public String getRichiedente()
	{
		return richiedente;
	}

	public void setRichiedente(String richiedente)
	{
		this.richiedente = richiedente;
	}

	public String getRichiedente2()
	{
		return richiedente2;
	}

	public void setRichiedente2(String richiedente2)
	{
		this.richiedente2 = richiedente2;
	}

	public String getTipo()
	{
		return tipo;
	}

	public void setTipo(String tipo)
	{
		this.tipo = tipo;
	}

	public String getProcedimento()
	{
		return procedimento;
	}

	public void setProcedimento(String procedimento)
	{
		this.procedimento = procedimento;
	}

	public String getSubalterno()
	{
		return subalterno;
	}

	public void setSubalterno(String subalterno)
	{
		this.subalterno = subalterno;
	}

	public String getZona()
	{
		return zona;
	}

	public void setZona(String zona)
	{
		this.zona = zona;
	}

	
}
