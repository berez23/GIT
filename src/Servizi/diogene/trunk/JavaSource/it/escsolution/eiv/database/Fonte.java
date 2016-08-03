package it.escsolution.eiv.database;

public class Fonte {

	private Long idTema = new Long(0);
	private Integer idProgetto;
	private String descrizioneProgetto;
	private String descrizioneFonte;
	private String dataAggiornamento;
	private String note;
	private String dataInizio;
	private String logSitSintesiProcessi;
	private String sqlDataAgg = "";
	private String sqlDataIni = "";
	
	public Integer getIdProgetto() {
		return idProgetto;
	}
	public void setIdProgetto(Integer idProgetto) {
		this.idProgetto = idProgetto;
	}
	public String getDescrizioneProgetto() {
		return descrizioneProgetto;
	}
	public void setDescrizioneProgetto(String descrizioneProgetto) {
		this.descrizioneProgetto = descrizioneProgetto;
	}
	public String getDescrizioneFonte() {
		return descrizioneFonte;
	}
	public void setDescrizioneFonte(String descrizioneFonte) {
		this.descrizioneFonte = descrizioneFonte;
	}
	public String getDataAggiornamento() {
		return dataAggiornamento;
	}
	public void setDataAggiornamento(String dataAggiornamento) {
		this.dataAggiornamento = dataAggiornamento;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getDataInizio() {
		return dataInizio;
	}
	public void setDataInizio(String dataInizio) {
		this.dataInizio = dataInizio;
	}
	public String getLogSitSintesiProcessi() {
		return logSitSintesiProcessi;
	}
	public void setLogSitSintesiProcessi(String logSitSintesiProcessi) {
		this.logSitSintesiProcessi = logSitSintesiProcessi;
	}
	public Long getIdTema() {
		return idTema;
	}
	public void setIdTema(Long idTema) {
		this.idTema = idTema;
	}
	public String getSqlDataAgg() {
		return sqlDataAgg;
	}
	public void setSqlDataAgg(String sqlDataAgg) {
		this.sqlDataAgg = sqlDataAgg;
	}
	public String getSqlDataIni() {
		return sqlDataIni;
	}
	public void setSqlDataIni(String sqlDataIni) {
		this.sqlDataIni = sqlDataIni;
	}

}
