package it.bod.application.beans;

import it.bod.application.common.MasterItem;

public class BodModello80Bean extends MasterItem{

	private static final long serialVersionUID = -6969842802386284551L;
	
	private Long idMod = new Long(0);
	private String oggettoDomanda = "";
	private String tipoIntervento = "";
	private String oggetto = "";
	private String numProgressivo = "";
	private String numProtocollo = "";
	private String note = "";
	private String settore = "";
	private String destinazioneZona = "";
	private String significativaPresenza = "";
	private String destinazioneUiu = "";
	private String categoriaProposta = "";
	private String classeProposta = "";
	private Boolean flg662=false;
	private Boolean flgAllineamento=false;
	private Boolean flgNo336=false;
	private Boolean flgAscensore=false;
	private BodIstruttoriaBean istruttoria=null;
	
	public Long getIdMod() {
		return idMod;
	}
	public void setIdMod(Long idMod) {
		this.idMod = idMod;
	}
	public String getOggettoDomanda() {
		return oggettoDomanda;
	}
	public void setOggettoDomanda(String oggettoDomanda) {
		this.oggettoDomanda = oggettoDomanda;
	}
	public String getTipoIntervento() {
		return tipoIntervento;
	}
	public void setTipoIntervento(String tipoIntervento) {
		this.tipoIntervento = tipoIntervento;
	}
	public String getOggetto() {
		return oggetto;
	}
	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}
	public String getNumProgressivo() {
		return numProgressivo;
	}
	public void setNumProgressivo(String numProgressivo) {
		this.numProgressivo = numProgressivo;
	}
	public String getNumProtocollo() {
		return numProtocollo;
	}
	public void setNumProtocollo(String numProtocollo) {
		this.numProtocollo = numProtocollo;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getSettore() {
		return settore;
	}
	public void setSettore(String settore) {
		this.settore = settore;
	}
	public String getDestinazioneZona() {
		return destinazioneZona;
	}
	public void setDestinazioneZona(String destinazioneZona) {
		this.destinazioneZona = destinazioneZona;
	}
	public String getSignificativaPresenza() {
		return significativaPresenza;
	}
	public void setSignificativaPresenza(String significativaPresenza) {
		this.significativaPresenza = significativaPresenza;
	}
	public String getDestinazioneUiu() {
		return destinazioneUiu;
	}
	public void setDestinazioneUiu(String destinazioneUiu) {
		this.destinazioneUiu = destinazioneUiu;
	}
	public String getCategoriaProposta() {
		return categoriaProposta;
	}
	public void setCategoriaProposta(String categoriaProposta) {
		this.categoriaProposta = categoriaProposta;
	}
	public Boolean getFlg662() {
		return flg662;
	}
	public void setFlg662(Boolean flg662) {
		this.flg662 = flg662;
	}
	public Boolean getFlgAllineamento() {
		return flgAllineamento;
	}
	public void setFlgAllineamento(Boolean flgAllineamento) {
		this.flgAllineamento = flgAllineamento;
	}
	public Boolean getFlgNo336() {
		return flgNo336;
	}
	public void setFlgNo336(Boolean flgNo336) {
		this.flgNo336 = flgNo336;
	}
	public BodIstruttoriaBean getIstruttoria() {
		return istruttoria;
	}
	public void setIstruttoria(BodIstruttoriaBean istruttoria) {
		this.istruttoria = istruttoria;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Boolean getFlgAscensore() {
		return flgAscensore;
	}
	public void setFlgAscensore(Boolean flgAscensore) {
		this.flgAscensore = flgAscensore;
	}
	public String getClasseProposta() {
		return classeProposta;
	}
	public void setClasseProposta(String classeProposta) {
		this.classeProposta = classeProposta;
	}
	
	
	
	
	
	
	
}
