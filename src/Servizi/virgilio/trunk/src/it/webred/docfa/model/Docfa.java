package it.webred.docfa.model;

import java.util.ArrayList;

public class Docfa  implements java.io.Serializable {


	private String protocollo ; 
	private String dataVariazione ; 
	private String causale ; 
	private String soppressione ; 
	private String variazione ; 
	private String costituzione ; 
	private String cognome ; 
	private String nome ; 
	private String denominazione ; 
	private String codiceFiscale ; 
	private String partitaIva ; 
	private String operazione ; 
	private String foglio ; 
	private String particella ; 
	private String subalterno ; 
	private String foglio2 ; 
	private String particella2 ; 
	private String subalterno2 ;
	private String foglio3 ; 
	private String particella3 ; 
	private String subalterno3 ; 	
	private String dichiarante ; 
	private String indirizzoDichiarante;
	private String indirizzoUiu;
	private String fornitura;
	private String derivSpe;
	private String annotazioni;
	private String zona;
	private String classe;
	private String categoria;
	private String consistenza;
	private String superfice;
	private String rendita;
	private String tipo;
	private String luogo;
	private String superficeMetrici;
	private String ambiente;
	private String altezza;
	private String identificativoImm;
	private String dataRegistrazione;
	private String annoCostruzione;
	private String annoRistrutturazione;
	private String posizFabbr;
	private String tipoAccesso;
	private String numero;
	private String dataProtocollo;
	private ArrayList indPart = new ArrayList();
	private String presenzaGraffati;
	private RenditaDocfa rendDocfa;
	private String flagNC;
	
	
	private String zonaDocfa;
	private String zonaDocfaProp;
	private String renditaDocfa;
	private String renditaDocfaProp;
	private String classeDocfa;
	private String classeDocfaProp;
	private String superficieDocfa;
	private String superficieDocfaProp;

	private String codCategoriaDocfaProp;
	private ArrayList elencoTitolari;
	
	private int importErrorCount;
	private int integrazioneCount;
    // Constructors

    public int getImportErrorCount()
	{
		return importErrorCount;
	}

	public void setImportErrorCount(int importErrorCount)
	{
		this.importErrorCount = importErrorCount;
	}

	/** default constructor */
    public Docfa() {
    }
    
    public String getNumero()
	{
		return numero;
	}
	public void setNumero(String numero)
	{
		this.numero = numero;
	}
	public String getPosizFabbr()
	{
		return posizFabbr;
	}
	public void setPosizFabbr(String posizFabbr)
	{
		this.posizFabbr = posizFabbr;
	}
	public String getTipoAccesso()
	{
		return tipoAccesso;
	}
	public void setTipoAccesso(String tipoAccesso)
	{
		this.tipoAccesso = tipoAccesso;
	}
	public String getDataRegistrazione()
	{
		return dataRegistrazione;
	}
	public void setDataRegistrazione(String dataRegistrazione)
	{
		this.dataRegistrazione = dataRegistrazione;
	}
	public String getTipo()
	{
		return tipo;
	}
	public void setTipo(String tipo)
	{
		this.tipo = tipo;
	}
	public String getAnnotazioni()
	{
		return annotazioni;
	}
	public void setAnnotazioni(String annotazioni)
	{
		this.annotazioni = annotazioni;
	}
	public String getFornitura()
	{
		return fornitura;
	}
	public void setFornitura(String fornitura)
	{
		this.fornitura = fornitura;
	}
	public String getCausale()
	{
		return causale;
	}
	public void setCausale(String causale)
	{
		this.causale = causale;
	}
	public String getCodiceFiscale()
	{
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale)
	{
		this.codiceFiscale = codiceFiscale;
	}
	public String getCognome()
	{
		return cognome;
	}
	public void setCognome(String cognome)
	{
		this.cognome = cognome;
	}
	public String getCostituzione()
	{
		return costituzione;
	}
	public void setCostituzione(String costituzione)
	{
		this.costituzione = costituzione;
	}
	public String getDataVariazione()
	{
		return dataVariazione;
	}
	public void setDataVariazione(String dataVariazione)
	{
		this.dataVariazione = dataVariazione;
	}
	public String getDenominazione()
	{
		return denominazione;
	}
	public void setDenominazione(String denominazione)
	{
		this.denominazione = denominazione;
	}
	public String getDichiarante()
	{
		return dichiarante;
	}
	public void setDichiarante(String dichiarante)
	{
		this.dichiarante = dichiarante;
	}
	public String getFoglio()
	{
		return foglio;
	}
	public void setFoglio(String foglio)
	{
		this.foglio = foglio;
	}
	public String getIndirizzoDichiarante()
	{
		return indirizzoDichiarante;
	}
	public void setIndirizzoDichiarante(String indirizzoDichiarante)
	{
		this.indirizzoDichiarante = indirizzoDichiarante;
	}
	public String getNome()
	{
		return nome;
	}
	public void setNome(String nome)
	{
		this.nome = nome;
	}
	public String getOperazione()
	{
		return operazione;
	}
	public void setOperazione(String operazione)
	{
		this.operazione = operazione;
	}
	public String getParticella()
	{
		return particella;
	}
	public void setParticella(String particella)
	{
		this.particella = particella;
	}

	public String getPartitaIva()
	{
		return partitaIva;
	}
	public void setPartitaIva(String partitaIva)
	{
		this.partitaIva = partitaIva;
	}
	public String getProtocollo()
	{
		return protocollo;
	}
	public void setProtocollo(String protocollo)
	{
		this.protocollo = protocollo;
	}
	public String getSoppressione()
	{
		return soppressione;
	}
	
	public void setSoppressione(String soppressione)
	{
		this.soppressione = soppressione;
	}
	public String getSubalterno()
	{
		return subalterno;
	}
	public void setSubalterno(String subalterno)
	{
		this.subalterno = subalterno;
	}
	public String getVariazione()
	{
		return variazione;
	}
	public void setVariazione(String variazione)
	{
		this.variazione = variazione;
	}
	public String getDerivSpe()
	{
		return derivSpe;
	}
	public void setDerivSpe(String derivSpe)
	{
		this.derivSpe = derivSpe;
	}
	public String getFoglio2()
	{
		return foglio2;
	}
	public void setFoglio2(String foglio2)
	{
		this.foglio2 = foglio2;
	}
	public String getFoglio3()
	{
		return foglio3;
	}
	public void setFoglio3(String foglio3)
	{
		this.foglio3 = foglio3;
	}
	public String getParticella2()
	{
		return particella2;
	}
	public void setParticella2(String particella2)
	{
		this.particella2 = particella2;
	}
	public String getParticella3()
	{
		return particella3;
	}
	public void setParticella3(String particella3)
	{
		this.particella3 = particella3;
	}
	public String getSubalterno2()
	{
		return subalterno2;
	}
	public void setSubalterno2(String subalterno2)
	{
		this.subalterno2 = subalterno2;
	}
	public String getSubalterno3()
	{
		return subalterno3;
	}
	public void setSubalterno3(String subalterno3)
	{
		this.subalterno3 = subalterno3;
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
	public String getConsistenza()
	{
		return consistenza;
	}
	public void setConsistenza(String consistenza)
	{
		this.consistenza = consistenza;
	}

	public String getRendita()
	{
		return rendita;
	}
	public void setRendita(String rendita)
	{
		this.rendita = rendita;
	}
	public String getSuperfice()
	{
		return superfice;
	}
	public void setSuperfice(String superfice)
	{
		this.superfice = superfice;
	}
	public String getZona()
	{
		return zona;
	}
	public void setZona(String zona)
	{
		this.zona = zona;
	}
	public String getLuogo()
	{
		return luogo;
	}
	public void setLuogo(String luogo)
	{
		this.luogo = luogo;
	}
	public String getAltezza()
	{
		return altezza;
	}
	public void setAltezza(String altezza)
	{
		this.altezza = altezza;
	}
	public String getAmbiente()
	{
		return ambiente;
	}
	public void setAmbiente(String ambiente)
	{
		this.ambiente = ambiente;
	}
	public String getIdentificativoImm()
	{
		return identificativoImm;
	}
	public void setIdentificativoImm(String identificativoImm)
	{
		this.identificativoImm = identificativoImm;
	}
	public String getSuperficeMetrici()
	{
		return superficeMetrici;
	}
	public void setSuperficeMetrici(String superficeMetrici)
	{
		this.superficeMetrici = superficeMetrici;
	}
	public String getAnnoCostruzione()
	{
		return annoCostruzione;
	}
	public void setAnnoCostruzione(String annoCostruzione)
	{
		this.annoCostruzione = annoCostruzione;
	}
	public String getAnnoRistrutturazione()
	{
		return annoRistrutturazione;
	}
	public void setAnnoRistrutturazione(String annoRistrutturazione)
	{
		this.annoRistrutturazione = annoRistrutturazione;
	}
	public String getDataProtocollo() {
		return dataProtocollo;
	}
	public void setDataProtocollo(String dataProtocollo) {
		this.dataProtocollo = dataProtocollo;
	}
	public ArrayList getIndPart() {
		return indPart;
	}
	public void setIndPart(ArrayList indPart) {
		this.indPart = indPart;
	}
	public String getPresenzaGraffati() {
		return presenzaGraffati;
	}
	public void setPresenzaGraffati(String presenzaGraffati) {
		this.presenzaGraffati = presenzaGraffati;
	}
	public String getIndirizzoUiu() {
		return indirizzoUiu;
	}
	public void setIndirizzoUiu(String indirizzoUiu) {
		this.indirizzoUiu = indirizzoUiu;
	}
	public RenditaDocfa getRendDocfa() {
		return rendDocfa;
	}
	public void setRendDocfa(RenditaDocfa rendDocfa) {
		this.rendDocfa = rendDocfa;
	}
	public String getFlagNC() {
		return flagNC;
	}
	public void setFlagNC(String flagNC) {
		this.flagNC = flagNC;
	}

	public String getClasseDocfa()
	{
		return classeDocfa;
	}

	public void setClasseDocfa(String classeDocfa)
	{
		this.classeDocfa = classeDocfa;
	}

	public String getClasseDocfaProp()
	{
		return classeDocfaProp;
	}

	public void setClasseDocfaProp(String classeDocfaProp)
	{
		this.classeDocfaProp = classeDocfaProp;
	}



	public String getCodCategoriaDocfaProp()
	{
		return codCategoriaDocfaProp;
	}

	public void setCodCategoriaDocfaProp(String codCategoriaDocfaProp)
	{
		this.codCategoriaDocfaProp = codCategoriaDocfaProp;
	}

	public ArrayList getElencoTitolari()
	{
		return elencoTitolari;
	}

	public void setElencoTitolari(ArrayList elencoTitolari)
	{
		this.elencoTitolari = elencoTitolari;
	}

	public String getRenditaDocfa()
	{
		return renditaDocfa;
	}

	public void setRenditaDocfa(String renditaDocfa)
	{
		this.renditaDocfa = renditaDocfa;
	}

	public String getRenditaDocfaProp()
	{
		return renditaDocfaProp;
	}

	public void setRenditaDocfaProp(String renditaDocfaProp)
	{
		this.renditaDocfaProp = renditaDocfaProp;
	}

	public String getSuperficieDocfa()
	{
		return superficieDocfa;
	}

	public void setSuperficieDocfa(String superficieDocfa)
	{
		this.superficieDocfa = superficieDocfa;
	}

	public String getSuperficieDocfaProp()
	{
		return superficieDocfaProp;
	}

	public void setSuperficieDocfaProp(String superficieDocfaProp)
	{
		this.superficieDocfaProp = superficieDocfaProp;
	}

	public String getZonaDocfa()
	{
		return zonaDocfa;
	}

	public void setZonaDocfa(String zonaDocfa)
	{
		this.zonaDocfa = zonaDocfa;
	}

	public String getZonaDocfaProp()
	{
		return zonaDocfaProp;
	}

	public void setZonaDocfaProp(String zonaDocfaProp)
	{
		this.zonaDocfaProp = zonaDocfaProp;
	}

	public int getIntegrazioneCount()
	{
		return integrazioneCount;
	}

	public void setIntegrazioneCount(int integrazioneCount)
	{
		this.integrazioneCount = integrazioneCount;
	}
}
