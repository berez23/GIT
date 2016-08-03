/*
 * Created on 13-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.agenziaEntrate.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;
import java.util.List;
/**
 * @author Giulio Quaresima - WebRed
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AgenziaEntrateContribuente extends EscObject implements Serializable{
	
	private String idContribuente;
	private String codEnt;
	private String tipoRecord;
	private String codRitornoComune;
	private String codFunzione;
	private String  cod_ritorno ;            
	private String numeroSoggettiTrovati;  
	private String codFiscPersona;         
	private String codFiscDitta;           
	private String fillerDitta;             
	private String codFiscDue;             
	private String flagTemporaneo;          
	private String cognome;                  
	private String nome;                     
	private String dataNascita;             
	private String comuneNascita;           
	private String provinciaNascita;        
	private String comuneResidenza;         
	private String provinciaResidenza;      
	private String indirizzo;                
	private String cap;                      
	private String numCodFisc;             
	private String numPartIva;             
	private List varResidLista;           
	private List codFiscLista;             
	private List partIvaLista;             
	private String flagVarResid;
	private String flagRapprSocio;       
	private String flagTerreni;             
	private String flagFabbricati;          
	private String flagDeceduto;       
	
	
	
	public AgenziaEntrateContribuente(){
		   
     	idContribuente="";
     	codEnt="";
     	tipoRecord="";
     	codRitornoComune="";
     	codFunzione="";
     	cod_ritorno ="";            
     	numeroSoggettiTrovati="";  
     	codFiscPersona="";         
     	codFiscDitta="";           
     	fillerDitta="";             
     	codFiscDue="";             
     	flagTemporaneo="";          
     	cognome="";                  
     	nome="";                     
     	dataNascita="";             
     	comuneNascita="";           
     	provinciaNascita="";        
     	comuneResidenza="";         
     	provinciaResidenza="";      
     	indirizzo="";                
     	cap="";                      
     	numCodFisc="";             
     	numPartIva="";             
     	flagVarResid="";           
     	codFiscLista=null;             
     	partIvaLista=null;             
     	varResidLista=null;
     	flagRapprSocio="";       
     	flagTerreni="";             
     	flagFabbricati="";          
     	flagDeceduto="";    
	
	}

	
	/* (non-Javadoc)
	 * @see it.escsolution.escwebgis.common.EscObject#getChiave()
	 */
	public String getChiave() {
		// TODO Auto-generated method stub
		return idContribuente;
	}
	/**
	 * @return Returns the cap.
	 */
	public String getCap() {
		return cap;
	}
	/**
	 * @param cap The cap to set.
	 */
	public void setCap(String cap) {
		this.cap = cap;
	}
	/**
	 * @return Returns the cod_ritorno.
	 */
	public String getCod_ritorno() {
		return cod_ritorno;
	}
	/**
	 * @param cod_ritorno The cod_ritorno to set.
	 */
	public void setCod_ritorno(String cod_ritorno) {
		this.cod_ritorno = cod_ritorno;
	}
	/**
	 * @return Returns the codEnt.
	 */
	public String getCodEnt() {
		return codEnt;
	}
	/**
	 * @param codEnt The codEnt to set.
	 */
	public void setCodEnt(String codEnt) {
		this.codEnt = codEnt;
	}
	/**
	 * @return Returns the codFiscDitta.
	 */
	public String getCodFiscDitta() {
		return codFiscDitta;
	}
	/**
	 * @param codFiscDitta The codFiscDitta to set.
	 */
	public void setCodFiscDitta(String codFiscDitta) {
		this.codFiscDitta = codFiscDitta;
	}
	/**
	 * @return Returns the codFiscDue.
	 */
	public String getCodFiscDue() {
		return codFiscDue;
	}
	/**
	 * @param codFiscDue The codFiscDue to set.
	 */
	public void setCodFiscDue(String codFiscDue) {
		this.codFiscDue = codFiscDue;
	}
	/**
	 * @return Returns the codFiscPersona.
	 */
	public String getCodFiscPersona() {
		return codFiscPersona;
	}
	/**
	 * @param codFiscPersona The codFiscPersona to set.
	 */
	public void setCodFiscPersona(String codFiscPersona) {
		this.codFiscPersona = codFiscPersona;
	}
	/**
	 * @return Returns the codFunzione.
	 */
	public String getCodFunzione() {
		return codFunzione;
	}
	/**
	 * @param codFunzione The codFunzione to set.
	 */
	public void setCodFunzione(String codFunzione) {
		this.codFunzione = codFunzione;
	}
	/**
	 * @return Returns the codRitornoComune.
	 */
	public String getCodRitornoComune() {
		return codRitornoComune;
	}
	/**
	 * @param codRitornoComune The codRitornoComune to set.
	 */
	public void setCodRitornoComune(String codRitornoComune) {
		this.codRitornoComune = codRitornoComune;
	}
	/**
	 * @return Returns the cognome.
	 */
	public String getCognome() {
		return cognome;
	}
	/**
	 * @param cognome The cognome to set.
	 */
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	/**
	 * @return Returns the comuneNascita.
	 */
	public String getComuneNascita() {
		return comuneNascita;
	}
	/**
	 * @param comuneNascita The comuneNascita to set.
	 */
	public void setComuneNascita(String comuneNascita) {
		this.comuneNascita = comuneNascita;
	}
	/**
	 * @return Returns the comuneResidenza.
	 */
	public String getComuneResidenza() {
		return comuneResidenza;
	}
	/**
	 * @param comuneResidenza The comuneResidenza to set.
	 */
	public void setComuneResidenza(String comuneResidenza) {
		this.comuneResidenza = comuneResidenza;
	}
	/**
	 * @return Returns the dataNascita.
	 */
	public String getDataNascita() {
		return dataNascita;
	}
	/**
	 * @param dataNascita The dataNascita to set.
	 */
	public void setDataNascita(String dataNascita) {
		this.dataNascita = dataNascita;
	}
	/**
	 * @return Returns the fillerDitta.
	 */
	public String getFillerDitta() {
		return fillerDitta;
	}
	/**
	 * @param fillerDitta The fillerDitta to set.
	 */
	public void setFillerDitta(String fillerDitta) {
		this.fillerDitta = fillerDitta;
	}
	/**
	 * @return Returns the flagDeceduto.
	 */
	public String getFlagDeceduto() {
		return flagDeceduto;
	}
	/**
	 * @param flagDeceduto The flagDeceduto to set.
	 */
	public void setFlagDeceduto(String flagDeceduto) {
		this.flagDeceduto = flagDeceduto;
	}
	/**
	 * @return Returns the flagFabbricati.
	 */
	public String getFlagFabbricati() {
		return flagFabbricati;
	}
	/**
	 * @param flagFabbricati The flagFabbricati to set.
	 */
	public void setFlagFabbricati(String flagFabbricati) {
		this.flagFabbricati = flagFabbricati;
	}
	/**
	 * @return Returns the flagRapprSocio.
	 */
	public String getFlagRapprSocio() {
		return flagRapprSocio;
	}
	/**
	 * @param flagRapprSocio The flagRapprSocio to set.
	 */
	public void setFlagRapprSocio(String flagRapprSocio) {
		this.flagRapprSocio = flagRapprSocio;
	}
	/**
	 * @return Returns the flagTemporaneo.
	 */
	public String getFlagTemporaneo() {
		return flagTemporaneo;
	}
	/**
	 * @param flagTemporaneo The flagTemporaneo to set.
	 */
	public void setFlagTemporaneo(String flagTemporaneo) {
		this.flagTemporaneo = flagTemporaneo;
	}
	/**
	 * @return Returns the flagTerreni.
	 */
	public String getFlagTerreni() {
		return flagTerreni;
	}
	/**
	 * @param flagTerreni The flagTerreni to set.
	 */
	public void setFlagTerreni(String flagTerreni) {
		this.flagTerreni = flagTerreni;
	}
	/**
	 * @return Returns the flagVarResid.
	 */
	public String getFlagVarResid() {
		return flagVarResid;
	}
	/**
	 * @param flagVarResid The flagVarResid to set.
	 */
	public void setFlagVarResid(String flagVarResid) {
		this.flagVarResid = flagVarResid;
	}
	/**
	 * @return Returns the indirizzo.
	 */
	public String getIndirizzo() {
		return indirizzo;
	}
	/**
	 * @param indirizzo The indirizzo to set.
	 */
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	/**
	 * @return Returns the nome.
	 */
	public String getNome() {
		return nome;
	}
	/**
	 * @param nome The nome to set.
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	/**
	 * @return Returns the numCodFisc.
	 */
	public String getNumCodFisc() {
		return numCodFisc;
	}
	/**
	 * @param numCodFisc The numCodFisc to set.
	 */
	public void setNumCodFisc(String numCodFisc) {
		this.numCodFisc = numCodFisc;
	}
	/**
	 * @return Returns the numeroSoggettiTrovati.
	 */
	public String getNumeroSoggettiTrovati() {
		return numeroSoggettiTrovati;
	}
	/**
	 * @param numeroSoggettiTrovati The numeroSoggettiTrovati to set.
	 */
	public void setNumeroSoggettiTrovati(String numeroSoggettiTrovati) {
		this.numeroSoggettiTrovati = numeroSoggettiTrovati;
	}
	/**
	 * @return Returns the numPartIva.
	 */
	public String getNumPartIva() {
		return numPartIva;
	}
	/**
	 * @param numPartIva The numPartIva to set.
	 */
	public void setNumPartIva(String numPartIva) {
		this.numPartIva = numPartIva;
	}
	/**
	 * @return Returns the pkIdContribuente.
	 */
	public String getidContribuente() {
		return idContribuente;
	}
	/**
	 * @param pkIdContribuente The pkIdContribuente to set.
	 */
	public void setidContribuente(String idContribuente) {
		this.idContribuente = idContribuente;
	}
	/**
	 * @return Returns the provinciaNascita.
	 */
	public String getProvinciaNascita() {
		return provinciaNascita;
	}
	/**
	 * @param provinciaNascita The provinciaNascita to set.
	 */
	public void setProvinciaNascita(String provinciaNascita) {
		this.provinciaNascita = provinciaNascita;
	}
	/**
	 * @return Returns the provinciaResidenza.
	 */
	public String getProvinciaResidenza() {
		return provinciaResidenza;
	}
	/**
	 * @param provinciaResidenza The provinciaResidenza to set.
	 */
	public void setProvinciaResidenza(String provinciaResidenza) {
		this.provinciaResidenza = provinciaResidenza;
	}
	/**
	 * @return Returns the tipoRecord.
	 */
	public String getTipoRecord() {
		return tipoRecord;
	}
	/**
	 * @param tipoRecord The tipoRecord to set.
	 */
	public void setTipoRecord(String tipoRecord) {
		this.tipoRecord = tipoRecord;
	}
	

	/**
	 * @return Returns the codFiscLista.
	 */
	public List getCodFiscLista() {
		return codFiscLista;
	}
	/**
	 * @param codFiscLista The codFiscLista to set.
	 */
	public void setCodFiscLista(List codFiscLista) {
		this.codFiscLista = codFiscLista;
	}
	/**
	 * @return Returns the idContribuente.
	 */
	public String getIdContribuente() {
		return idContribuente;
	}
	/**
	 * @param idContribuente The idContribuente to set.
	 */
	public void setIdContribuente(String idContribuente) {
		this.idContribuente = idContribuente;
	}
	/**
	 * @return Returns the partIvaLista.
	 */
	public List getPartIvaLista() {
		return partIvaLista;
	}
	/**
	 * @param partIvaLista The partIvaLista to set.
	 */
	public void setPartIvaLista(List partIvaLista) {
		this.partIvaLista = partIvaLista;
	}
	/**
	 * @return Returns the varResidLista.
	 */
	public List getVarResidLista() {
		return varResidLista;
	}
	/**
	 * @param varResidLista The varResidLista to set.
	 */
	public void setVarResidLista(List varResidLista) {
		this.varResidLista = varResidLista;
	}
}