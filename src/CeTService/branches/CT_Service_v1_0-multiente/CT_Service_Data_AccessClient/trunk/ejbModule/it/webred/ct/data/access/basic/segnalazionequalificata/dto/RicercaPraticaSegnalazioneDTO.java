package it.webred.ct.data.access.basic.segnalazionequalificata.dto;

import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

public class RicercaPraticaSegnalazioneDTO extends CeTBaseObject {
	private static final long serialVersionUID = 1L;
	
	private String enteId;
	
	private Long idPra;
	private Long idAll;
	
	//Soggetto Accertato
	private String accCodiFisc;
	private String accCognome;
	private String accNome;
	private String accCodiPiva;
	private String accDenominazione;
	
	//Responsabile
	private String resCodiFisc;
	private String resCognome;
	private String resNome;
	
	//Dati del procedimento
	private String finalita;           //TFL:Tributi fiscali locali, TFE:Tributi fiscali erariali
	private Date dataInizioDa;
	private Date dataInizioA;
	
	private String stato;    
	/* 0: Istruttoria Comunale in corso
	 * 1: Segnalazione in corso presso Agenzia delle Entrate
	 * 2: Iter concluso
	 */
	
	//proprietà per visualizzazione tabella
	private int startm;
	
	private int numberRecord;
	
	//Operatore
	private ArrayList<String> operatori;
	private String sOperatori;
	
	public RicercaPraticaSegnalazioneDTO(String user){
		operatori = new ArrayList<String>();
		operatori.add(user);
	}

	public String getAccCodiFisc() {
		return accCodiFisc;
	}

	public void setAccCodiFisc(String accCodiFisc) {
		this.accCodiFisc = accCodiFisc;
	}

	public String getAccCognome() {
		return accCognome;
	}

	public void setAccCognome(String accCognome) {
		this.accCognome = accCognome;
	}

	public String getAccNome() {
		return accNome;
	}

	public void setAccNome(String accNome) {
		this.accNome = accNome;
	}

	public String getAccCodiPiva() {
		return accCodiPiva;
	}

	public void setAccCodiPiva(String accCodiPiva) {
		this.accCodiPiva = accCodiPiva;
	}

	public String getAccDenominazione() {
		return accDenominazione;
	}

	public void setAccDenominazione(String accDenominazione) {
		this.accDenominazione = accDenominazione;
	}

	public Long getIdPra() {
		return idPra;
	}

	public Long getIdAll() {
		return idAll;
	}

	public void setIdPra(Long idPra) {
		this.idPra = idPra;
	}

	public void setIdAll(Long idAll) {
		this.idAll = idAll;
	}

	public String getResCodiFisc() {
		return resCodiFisc;
	}

	public void setResCodiFisc(String resCodiFisc) {
		this.resCodiFisc = resCodiFisc;
	}

	public String getResCognome() {
		return resCognome;
	}

	public void setResCognome(String resCognome) {
		this.resCognome = resCognome;
	}

	public String getResNome() {
		return resNome;
	}

	public void setResNome(String resNome) {
		this.resNome = resNome;
	}

	public String getFinalita() {
		return finalita;
	}

	public void setFinalita(String finalita) {
		this.finalita = finalita;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public Date getDataInizioDa() {
		return dataInizioDa;
	}

	public void setDataInizioDa(Date dataInizioDa) {
		this.dataInizioDa = dataInizioDa;
	}

	public Date getDataInizioA() {
		return dataInizioA;
	}

	public void setDataInizioA(Date dataInizioA) {
		this.dataInizioA = dataInizioA;
	}

	public void addCodOperatore(String cod) {
		operatori.add(cod);
	}
	
	public void setOperatori(ArrayList<String> operatori) {
		this.operatori = operatori;
	}

	public ArrayList<String> getOperatori() {
		return operatori;
	}

	public int getStartm() {
		return startm;
	}

	public String getsOperatori() {
		return sOperatori;
	}

	public void setsOperatori(String sOperatori) {
		this.sOperatori = sOperatori;
	}

	public void setStartm(int startm) {
		this.startm = startm;
	}

	public int getNumberRecord() {
		return numberRecord;
	}

	public void setNumberRecord(int numberRecord) {
		this.numberRecord = numberRecord;
	}

	public void setEnteId(String enteId) {
		this.enteId = enteId;
	}

	public String getEnteId() {
		return enteId;
	}

}
