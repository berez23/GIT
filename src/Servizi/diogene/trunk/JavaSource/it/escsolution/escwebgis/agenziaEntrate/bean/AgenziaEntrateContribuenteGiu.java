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
public class AgenziaEntrateContribuenteGiu extends EscObject implements
		Serializable {

	/* (non-Javadoc)
	 * @see it.escsolution.escwebgis.common.EscObject#getChiave()
	 */
	public String getChiave() {
		// TODO Auto-generated method stub
		return pkIdPg;
	}
	private String pkIdPg;

	private String codent;

	private String tipoRecord;

	private String codRitornoComune;

	private String codFunzione;

	private String codRitorno;

	private String numeroSoggettiTrovati;

	private String codFiscIindividuato;

	private String codFiscSoggetto;

	private String flagFonte;

	private String dataAttribuzione;

	private String denominazione;

	private String sigla;

	private String codNaturaGiu;

	private String flagOver10CodColle;

	private String flagOver20PivColle;

	private String desNaturaGiu;

	private String codDomicilioFisc;

	private String capDomicilioFisc;

	private String indCivDomicilioFisc;

	private String codAttivita;

	private String dataInizioScriCont;

	private String flagTenutario;

	private String flagVarDomicilio;

	private String flagVarAttivita;

	private String flagVarTenutario;

	private String flagVarOpzCont;

	private String comuneDomilicioFisc;

	private String provinDomicilioFisc;

	private String descrAttivita;

	private String codFiscTenutario;

	private String codCatastaleSedeLeg;

	private String capSedeLegale;

	private String indCivSedeLegale;

	private String flagCodCaricaRapLeg;

	private String flagVarSedeLegale;

	private String flagVarRapLegale;

	private String flagVarDenominazione;

	private String flagVarAttivitaBis;

	private String comuneSedeLegale;

	private String provinSedeLegale;

	private String flagDecodRapLeg;

	private String codFiscRapLegPfis;

	private String cognomeRapLegPfis;

	private String nomeRapLegPfis;

	private String indCivRapLegPfis;

	private String capRapLegPfis;

	private String comuneResRapLegPfis;

	private String provinResRapLeg;

	private String codFiscRapLegPgiu;

	private String denominazioneRapPgiu;

	private String indCivRapLegPgiu;

	private String capRapLegPgiu;

	private String comuneResRapLegPgiu;

	private String provinResRapLegPgiu;

	private String flagVarDomicilioBis;

	private String flagOver5VarDomic;

	private String flagDatiRap;

	private String flagOver6Rap;

	private List listPIva;

	private List listSrap;

	private List listVDom;

	public AgenziaEntrateContribuenteGiu() {

		pkIdPg = "";
		codent = "";
		tipoRecord = "";
		codRitornoComune = "";
		codFunzione = "";
		codRitorno = "";
		numeroSoggettiTrovati = "";
		codFiscIindividuato = "";
		codFiscSoggetto = "";
		flagFonte = "";
		dataAttribuzione = "";
		denominazione = "";
		sigla = "";
		codNaturaGiu = "";
		flagOver10CodColle = "";
		flagOver20PivColle = "";
		desNaturaGiu = "";
		codDomicilioFisc = "";
		capDomicilioFisc = "";
		indCivDomicilioFisc = "";
		codAttivita = "";
		dataInizioScriCont = "";
		flagTenutario = "";
		flagVarDomicilio = "";
		flagVarAttivita = "";
		flagVarTenutario = "";
		flagVarOpzCont = "";
		comuneDomilicioFisc = "";
		provinDomicilioFisc = "";
		descrAttivita = "";
		codFiscTenutario = "";
		codCatastaleSedeLeg = "";
		capSedeLegale = "";
		indCivSedeLegale = "";
		flagCodCaricaRapLeg = "";
		flagVarSedeLegale = "";
		flagVarRapLegale = "";
		flagVarDenominazione = "";
		flagVarAttivitaBis = "";
		comuneSedeLegale = "";
		provinSedeLegale = "";
		flagDecodRapLeg = "";
		codFiscRapLegPfis = "";
		cognomeRapLegPfis = "";
		nomeRapLegPfis = "";
		indCivRapLegPfis = "";
		capRapLegPfis = "";
		comuneResRapLegPfis = "";
		provinResRapLeg = "";
		codFiscRapLegPgiu = "";
		denominazioneRapPgiu = "";
		indCivRapLegPgiu = "";
		capRapLegPgiu = "";
		comuneResRapLegPgiu = "";
		provinResRapLegPgiu = "";
		flagVarDomicilioBis = "";
		flagOver5VarDomic = "";
		flagDatiRap = "";
		flagOver6Rap = "";
		listPIva = null;
		listSrap = null;
		listVDom = null;

	}

	/**
	 * @return Returns the capDomicilioFisc.
	 */
	public String getCapDomicilioFisc() {
		return capDomicilioFisc;
	}

	/**
	 * @param capDomicilioFisc
	 *            The capDomicilioFisc to set.
	 */
	public void setCapDomicilioFisc(String capDomicilioFisc) {
		this.capDomicilioFisc = capDomicilioFisc;
	}

	/**
	 * @return Returns the capRapLegPfis.
	 */
	public String getCapRapLegPfis() {
		return capRapLegPfis;
	}

	/**
	 * @param capRapLegPfis
	 *            The capRapLegPfis to set.
	 */
	public void setCapRapLegPfis(String capRapLegPfis) {
		this.capRapLegPfis = capRapLegPfis;
	}

	/**
	 * @return Returns the capRapLegPgiu.
	 */
	public String getCapRapLegPgiu() {
		return capRapLegPgiu;
	}

	/**
	 * @param capRapLegPgiu
	 *            The capRapLegPgiu to set.
	 */
	public void setCapRapLegPgiu(String capRapLegPgiu) {
		this.capRapLegPgiu = capRapLegPgiu;
	}

	/**
	 * @return Returns the capSedeLegale.
	 */
	public String getCapSedeLegale() {
		return capSedeLegale;
	}

	/**
	 * @param capSedeLegale
	 *            The capSedeLegale to set.
	 */
	public void setCapSedeLegale(String capSedeLegale) {
		this.capSedeLegale = capSedeLegale;
	}

	/**
	 * @return Returns the codNaturaGiu.
	 */
	public String getCodNaturaGiu() {
		return codNaturaGiu;
	}

	/**
	 * @param codNaturaGiu
	 *            The codNaturaGiu to set.
	 */
	public void setCodNaturaGiu(String codNaturaGiu) {
		this.codNaturaGiu = codNaturaGiu;
	}

	/**
	 * @return Returns the codAttivita.
	 */
	public String getCodAttivita() {
		return codAttivita;
	}

	/**
	 * @param codAttivita
	 *            The codAttivita to set.
	 */
	public void setCodAttivita(String codAttivita) {
		this.codAttivita = codAttivita;
	}

	/**
	 * @return Returns the codCatastaleSedeLeg.
	 */
	public String getCodCatastaleSedeLeg() {
		return codCatastaleSedeLeg;
	}

	/**
	 * @param codCatastaleSedeLeg
	 *            The codCatastaleSedeLeg to set.
	 */
	public void setCodCatastaleSedeLeg(String codCatastaleSedeLeg) {
		this.codCatastaleSedeLeg = codCatastaleSedeLeg;
	}

	/**
	 * @return Returns the codDomicilioFisc.
	 */
	public String getCodDomicilioFisc() {
		return codDomicilioFisc;
	}

	/**
	 * @param codDomicilioFisc
	 *            The codDomicilioFisc to set.
	 */
	public void setCodDomicilioFisc(String codDomicilioFisc) {
		this.codDomicilioFisc = codDomicilioFisc;
	}

	/**
	 * @return Returns the codent.
	 */
	public String getCodent() {
		return codent;
	}

	/**
	 * @param codent
	 *            The codent to set.
	 */
	public void setCodent(String codent) {
		this.codent = codent;
	}

	/**
	 * @return Returns the codFiscIindividuato.
	 */
	public String getCodFiscIindividuato() {
		return codFiscIindividuato;
	}

	/**
	 * @param codFiscIindividuato
	 *            The codFiscIindividuato to set.
	 */
	public void setCodFiscIindividuato(String codFiscIindividuato) {
		this.codFiscIindividuato = codFiscIindividuato;
	}

	/**
	 * @return Returns the codFiscRapLegPfis.
	 */
	public String getCodFiscRapLegPfis() {
		return codFiscRapLegPfis;
	}

	/**
	 * @param codFiscRapLegPfis
	 *            The codFiscRapLegPfis to set.
	 */
	public void setCodFiscRapLegPfis(String codFiscRapLegPfis) {
		this.codFiscRapLegPfis = codFiscRapLegPfis;
	}

	/**
	 * @return Returns the codFiscRapLegPgiu.
	 */
	public String getCodFiscRapLegPgiu() {
		return codFiscRapLegPgiu;
	}

	/**
	 * @param codFiscRapLegPgiu
	 *            The codFiscRapLegPgiu to set.
	 */
	public void setCodFiscRapLegPgiu(String codFiscRapLegPgiu) {
		this.codFiscRapLegPgiu = codFiscRapLegPgiu;
	}

	/**
	 * @return Returns the codFiscSoggetto.
	 */
	public String getCodFiscSoggetto() {
		return codFiscSoggetto;
	}

	/**
	 * @param codFiscSoggetto
	 *            The codFiscSoggetto to set.
	 */
	public void setCodFiscSoggetto(String codFiscSoggetto) {
		this.codFiscSoggetto = codFiscSoggetto;
	}

	/**
	 * @return Returns the codFiscTenutario.
	 */
	public String getCodFiscTenutario() {
		return codFiscTenutario;
	}

	/**
	 * @param codFiscTenutario
	 *            The codFiscTenutario to set.
	 */
	public void setCodFiscTenutario(String codFiscTenutario) {
		this.codFiscTenutario = codFiscTenutario;
	}

	/**
	 * @return Returns the codFunzione.
	 */
	public String getCodFunzione() {
		return codFunzione;
	}

	/**
	 * @param codFunzione
	 *            The codFunzione to set.
	 */
	public void setCodFunzione(String codFunzione) {
		this.codFunzione = codFunzione;
	}

	/**
	 * @return Returns the codRitorno.
	 */
	public String getCodRitorno() {
		return codRitorno;
	}

	/**
	 * @param codRitorno
	 *            The codRitorno to set.
	 */
	public void setCodRitorno(String codRitorno) {
		this.codRitorno = codRitorno;
	}

	/**
	 * @return Returns the codRitornoComune.
	 */
	public String getCodRitornoComune() {
		return codRitornoComune;
	}

	/**
	 * @param codRitornoComune
	 *            The codRitornoComune to set.
	 */
	public void setCodRitornoComune(String codRitornoComune) {
		this.codRitornoComune = codRitornoComune;
	}

	/**
	 * @return Returns the cognomeRapLegPfis.
	 */
	public String getCognomeRapLegPfis() {
		return cognomeRapLegPfis;
	}

	/**
	 * @param cognomeRapLegPfis
	 *            The cognomeRapLegPfis to set.
	 */
	public void setCognomeRapLegPfis(String cognomeRapLegPfis) {
		this.cognomeRapLegPfis = cognomeRapLegPfis;
	}

	/**
	 * @return Returns the comuneDomilicioFisc.
	 */
	public String getComuneDomilicioFisc() {
		return comuneDomilicioFisc;
	}

	/**
	 * @param comuneDomilicioFisc
	 *            The comuneDomilicioFisc to set.
	 */
	public void setComuneDomilicioFisc(String comuneDomilicioFisc) {
		this.comuneDomilicioFisc = comuneDomilicioFisc;
	}

	/**
	 * @return Returns the comuneResRapLegPfis.
	 */
	public String getComuneResRapLegPfis() {
		return comuneResRapLegPfis;
	}

	/**
	 * @param comuneResRapLegPfis
	 *            The comuneResRapLegPfis to set.
	 */
	public void setComuneResRapLegPfis(String comuneResRapLegPfis) {
		this.comuneResRapLegPfis = comuneResRapLegPfis;
	}

	/**
	 * @return Returns the comuneResRapLegPgiu.
	 */
	public String getComuneResRapLegPgiu() {
		return comuneResRapLegPgiu;
	}

	/**
	 * @param comuneResRapLegPgiu
	 *            The comuneResRapLegPgiu to set.
	 */
	public void setComuneResRapLegPgiu(String comuneResRapLegPgiu) {
		this.comuneResRapLegPgiu = comuneResRapLegPgiu;
	}

	/**
	 * @return Returns the comuneSedeLegale.
	 */
	public String getComuneSedeLegale() {
		return comuneSedeLegale;
	}

	/**
	 * @param comuneSedeLegale
	 *            The comuneSedeLegale to set.
	 */
	public void setComuneSedeLegale(String comuneSedeLegale) {
		this.comuneSedeLegale = comuneSedeLegale;
	}

	/**
	 * @return Returns the dataAttribuzione.
	 */
	public String getDataAttribuzione() {
		return dataAttribuzione;
	}

	/**
	 * @param dataAttribuzione
	 *            The dataAttribuzione to set.
	 */
	public void setDataAttribuzione(String dataAttribuzione) {
		this.dataAttribuzione = dataAttribuzione;
	}

	/**
	 * @return Returns the dataInizioScriCont.
	 */
	public String getDataInizioScriCont() {
		return dataInizioScriCont;
	}

	/**
	 * @param dataInizioScriCont
	 *            The dataInizioScriCont to set.
	 */
	public void setDataInizioScriCont(String dataInizioScriCont) {
		this.dataInizioScriCont = dataInizioScriCont;
	}

	/**
	 * @return Returns the denominazione.
	 */
	public String getDenominazione() {
		return denominazione;
	}

	/**
	 * @param denominazione
	 *            The denominazione to set.
	 */
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	/**
	 * @return Returns the denominazioneRapPgiu.
	 */
	public String getDenominazioneRapPgiu() {
		return denominazioneRapPgiu;
	}

	/**
	 * @param denominazioneRapPgiu
	 *            The denominazioneRapPgiu to set.
	 */
	public void setDenominazioneRapPgiu(String denominazioneRapPgiu) {
		this.denominazioneRapPgiu = denominazioneRapPgiu;
	}

	/**
	 * @return Returns the desNaturaGiu.
	 */
	public String getDesNaturaGiu() {
		return desNaturaGiu;
	}

	/**
	 * @param desNaturaGiu
	 *            The desNaturaGiu to set.
	 */
	public void setDesNaturaGiu(String desNaturaGiu) {
		this.desNaturaGiu = desNaturaGiu;
	}

	/**
	 * @return Returns the descrAttivita.
	 */
	public String getDescrAttivita() {
		return descrAttivita;
	}

	/**
	 * @param descrAttivita
	 *            The descrAttivita to set.
	 */
	public void setDescrAttivita(String descrAttivita) {
		this.descrAttivita = descrAttivita;
	}

	/**
	 * @return Returns the flagCodCaricaRapLeg.
	 */
	public String getFlagCodCaricaRapLeg() {
		return flagCodCaricaRapLeg;
	}

	/**
	 * @param flagCodCaricaRapLeg
	 *            The flagCodCaricaRapLeg to set.
	 */
	public void setFlagCodCaricaRapLeg(String flagCodCaricaRapLeg) {
		this.flagCodCaricaRapLeg = flagCodCaricaRapLeg;
	}

	/**
	 * @return Returns the flagDatiRap.
	 */
	public String getFlagDatiRap() {
		return flagDatiRap;
	}

	/**
	 * @param flagDatiRap
	 *            The flagDatiRap to set.
	 */
	public void setFlagDatiRap(String flagDatiRap) {
		this.flagDatiRap = flagDatiRap;
	}

	/**
	 * @return Returns the flagDecodRapLeg.
	 */
	public String getFlagDecodRapLeg() {
		return flagDecodRapLeg;
	}

	/**
	 * @param flagDecodRapLeg
	 *            The flagDecodRapLeg to set.
	 */
	public void setFlagDecodRapLeg(String flagDecodRapLeg) {
		this.flagDecodRapLeg = flagDecodRapLeg;
	}

	/**
	 * @return Returns the flagFonte.
	 */
	public String getFlagFonte() {
		return flagFonte;
	}

	/**
	 * @param flagFonte
	 *            The flagFonte to set.
	 */
	public void setFlagFonte(String flagFonte) {
		this.flagFonte = flagFonte;
	}

	/**
	 * @return Returns the flagOver10CodColle.
	 */
	public String getFlagOver10CodColle() {
		return flagOver10CodColle;
	}

	/**
	 * @param flagOver10CodColle
	 *            The flagOver10CodColle to set.
	 */
	public void setFlagOver10CodColle(String flagOver10CodColle) {
		this.flagOver10CodColle = flagOver10CodColle;
	}

	/**
	 * @return Returns the flagOver20PivColle.
	 */
	public String getFlagOver20PivColle() {
		return flagOver20PivColle;
	}

	/**
	 * @param flagOver20PivColle
	 *            The flagOver20PivColle to set.
	 */
	public void setFlagOver20PivColle(String flagOver20PivColle) {
		this.flagOver20PivColle = flagOver20PivColle;
	}

	/**
	 * @return Returns the flagOver5VarDomic.
	 */
	public String getFlagOver5VarDomic() {
		return flagOver5VarDomic;
	}

	/**
	 * @param flagOver5VarDomic
	 *            The flagOver5VarDomic to set.
	 */
	public void setFlagOver5VarDomic(String flagOver5VarDomic) {
		this.flagOver5VarDomic = flagOver5VarDomic;
	}

	/**
	 * @return Returns the flagOver6Rap.
	 */
	public String getFlagOver6Rap() {
		return flagOver6Rap;
	}

	/**
	 * @param flagOver6Rap
	 *            The flagOver6Rap to set.
	 */
	public void setFlagOver6Rap(String flagOver6Rap) {
		this.flagOver6Rap = flagOver6Rap;
	}

	/**
	 * @return Returns the flagTenutario.
	 */
	public String getFlagTenutario() {
		return flagTenutario;
	}

	/**
	 * @param flagTenutario
	 *            The flagTenutario to set.
	 */
	public void setFlagTenutario(String flagTenutario) {
		this.flagTenutario = flagTenutario;
	}

	/**
	 * @return Returns the flagVarAttivita.
	 */
	public String getFlagVarAttivita() {
		return flagVarAttivita;
	}

	/**
	 * @param flagVarAttivita
	 *            The flagVarAttivita to set.
	 */
	public void setFlagVarAttivita(String flagVarAttivita) {
		this.flagVarAttivita = flagVarAttivita;
	}

	/**
	 * @return Returns the flagVarAttivitaBis.
	 */
	public String getFlagVarAttivitaBis() {
		return flagVarAttivitaBis;
	}

	/**
	 * @param flagVarAttivitaBis
	 *            The flagVarAttivitaBis to set.
	 */
	public void setFlagVarAttivitaBis(String flagVarAttivitaBis) {
		this.flagVarAttivitaBis = flagVarAttivitaBis;
	}

	/**
	 * @return Returns the flagVarDenominazione.
	 */
	public String getFlagVarDenominazione() {
		return flagVarDenominazione;
	}

	/**
	 * @param flagVarDenominazione
	 *            The flagVarDenominazione to set.
	 */
	public void setFlagVarDenominazione(String flagVarDenominazione) {
		this.flagVarDenominazione = flagVarDenominazione;
	}

	/**
	 * @return Returns the flagVarDomicilio.
	 */
	public String getFlagVarDomicilio() {
		return flagVarDomicilio;
	}

	/**
	 * @param flagVarDomicilio
	 *            The flagVarDomicilio to set.
	 */
	public void setFlagVarDomicilio(String flagVarDomicilio) {
		this.flagVarDomicilio = flagVarDomicilio;
	}

	/**
	 * @return Returns the flagVarDomicilioBis.
	 */
	public String getFlagVarDomicilioBis() {
		return flagVarDomicilioBis;
	}

	/**
	 * @param flagVarDomicilioBis
	 *            The flagVarDomicilioBis to set.
	 */
	public void setFlagVarDomicilioBis(String flagVarDomicilioBis) {
		this.flagVarDomicilioBis = flagVarDomicilioBis;
	}

	/**
	 * @return Returns the flagVarOpzCont.
	 */
	public String getFlagVarOpzCont() {
		return flagVarOpzCont;
	}

	/**
	 * @param flagVarOpzCont
	 *            The flagVarOpzCont to set.
	 */
	public void setFlagVarOpzCont(String flagVarOpzCont) {
		this.flagVarOpzCont = flagVarOpzCont;
	}

	/**
	 * @return Returns the flagVarRapLegale.
	 */
	public String getFlagVarRapLegale() {
		return flagVarRapLegale;
	}

	/**
	 * @param flagVarRapLegale
	 *            The flagVarRapLegale to set.
	 */
	public void setFlagVarRapLegale(String flagVarRapLegale) {
		this.flagVarRapLegale = flagVarRapLegale;
	}

	/**
	 * @return Returns the flagVarSedeLegale.
	 */
	public String getFlagVarSedeLegale() {
		return flagVarSedeLegale;
	}

	/**
	 * @param flagVarSedeLegale
	 *            The flagVarSedeLegale to set.
	 */
	public void setFlagVarSedeLegale(String flagVarSedeLegale) {
		this.flagVarSedeLegale = flagVarSedeLegale;
	}

	/**
	 * @return Returns the flagVarTenutario.
	 */
	public String getFlagVarTenutario() {
		return flagVarTenutario;
	}

	/**
	 * @param flagVarTenutario
	 *            The flagVarTenutario to set.
	 */
	public void setFlagVarTenutario(String flagVarTenutario) {
		this.flagVarTenutario = flagVarTenutario;
	}

	/**
	 * @return Returns the indCivDomicilioFisc.
	 */
	public String getIndCivDomicilioFisc() {
		return indCivDomicilioFisc;
	}

	/**
	 * @param indCivDomicilioFisc
	 *            The indCivDomicilioFisc to set.
	 */
	public void setIndCivDomicilioFisc(String indCivDomicilioFisc) {
		this.indCivDomicilioFisc = indCivDomicilioFisc;
	}

	/**
	 * @return Returns the indCivRapLegPfis.
	 */
	public String getIndCivRapLegPfis() {
		return indCivRapLegPfis;
	}

	/**
	 * @param indCivRapLegPfis
	 *            The indCivRapLegPfis to set.
	 */
	public void setIndCivRapLegPfis(String indCivRapLegPfis) {
		this.indCivRapLegPfis = indCivRapLegPfis;
	}

	/**
	 * @return Returns the indCivRapLegPgiu.
	 */
	public String getIndCivRapLegPgiu() {
		return indCivRapLegPgiu;
	}

	/**
	 * @param indCivRapLegPgiu
	 *            The indCivRapLegPgiu to set.
	 */
	public void setIndCivRapLegPgiu(String indCivRapLegPgiu) {
		this.indCivRapLegPgiu = indCivRapLegPgiu;
	}

	/**
	 * @return Returns the indCivSedeLegale.
	 */
	public String getIndCivSedeLegale() {
		return indCivSedeLegale;
	}

	/**
	 * @param indCivSedeLegale
	 *            The indCivSedeLegale to set.
	 */
	public void setIndCivSedeLegale(String indCivSedeLegale) {
		this.indCivSedeLegale = indCivSedeLegale;
	}

	/**
	 * @return Returns the listPIva.
	 */
	public List getListPIva() {
		return listPIva;
	}

	/**
	 * @param listPIva
	 *            The listPIva to set.
	 */
	public void setListPIva(List listPIva) {
		this.listPIva = listPIva;
	}

	/**
	 * @return Returns the listSrap.
	 */
	public List getListSrap() {
		return listSrap;
	}

	/**
	 * @param listSrap
	 *            The listSrap to set.
	 */
	public void setListSrap(List listSrap) {
		this.listSrap = listSrap;
	}

	/**
	 * @return Returns the listVDom.
	 */
	public List getListVDom() {
		return listVDom;
	}

	/**
	 * @param listVDom
	 *            The listVDom to set.
	 */
	public void setListVDom(List listVDom) {
		this.listVDom = listVDom;
	}

	/**
	 * @return Returns the nomeRapLegPfis.
	 */
	public String getNomeRapLegPfis() {
		return nomeRapLegPfis;
	}

	/**
	 * @param nomeRapLegPfis
	 *            The nomeRapLegPfis to set.
	 */
	public void setNomeRapLegPfis(String nomeRapLegPfis) {
		this.nomeRapLegPfis = nomeRapLegPfis;
	}

	/**
	 * @return Returns the numeroSoggettiTrovati.
	 */
	public String getNumeroSoggettiTrovati() {
		return numeroSoggettiTrovati;
	}

	/**
	 * @param numeroSoggettiTrovati
	 *            The numeroSoggettiTrovati to set.
	 */
	public void setNumeroSoggettiTrovati(String numeroSoggettiTrovati) {
		this.numeroSoggettiTrovati = numeroSoggettiTrovati;
	}

	/**
	 * @return Returns the pkIdPg.
	 */
	public String getPkIdPg() {
		return pkIdPg;
	}

	/**
	 * @param pkIdPg
	 *            The pkIdPg to set.
	 */
	public void setPkIdPg(String pkIdPg) {
		this.pkIdPg = pkIdPg;
	}

	/**
	 * @return Returns the provinDomicilioFisc.
	 */
	public String getProvinDomicilioFisc() {
		return provinDomicilioFisc;
	}

	/**
	 * @param provinDomicilioFisc
	 *            The provinDomicilioFisc to set.
	 */
	public void setProvinDomicilioFisc(String provinDomicilioFisc) {
		this.provinDomicilioFisc = provinDomicilioFisc;
	}

	/**
	 * @return Returns the provinResRapLeg.
	 */
	public String getProvinResRapLeg() {
		return provinResRapLeg;
	}

	/**
	 * @param provinResRapLeg
	 *            The provinResRapLeg to set.
	 */
	public void setProvinResRapLeg(String provinResRapLeg) {
		this.provinResRapLeg = provinResRapLeg;
	}

	/**
	 * @return Returns the provinResRapLegPgiu.
	 */
	public String getProvinResRapLegPgiu() {
		return provinResRapLegPgiu;
	}

	/**
	 * @param provinResRapLegPgiu
	 *            The provinResRapLegPgiu to set.
	 */
	public void setProvinResRapLegPgiu(String provinResRapLegPgiu) {
		this.provinResRapLegPgiu = provinResRapLegPgiu;
	}

	/**
	 * @return Returns the provinSedeLegale.
	 */
	public String getProvinSedeLegale() {
		return provinSedeLegale;
	}

	/**
	 * @param provinSedeLegale
	 *            The provinSedeLegale to set.
	 */
	public void setProvinSedeLegale(String provinSedeLegale) {
		this.provinSedeLegale = provinSedeLegale;
	}

	/**
	 * @return Returns the sigla.
	 */
	public String getSigla() {
		return sigla;
	}

	/**
	 * @param sigla
	 *            The sigla to set.
	 */
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	/**
	 * @return Returns the tipoRecord.
	 */
	public String getTipoRecord() {
		return tipoRecord;
	}

	/**
	 * @param tipoRecord
	 *            The tipoRecord to set.
	 */
	public void setTipoRecord(String tipoRecord) {
		this.tipoRecord = tipoRecord;
	}
}