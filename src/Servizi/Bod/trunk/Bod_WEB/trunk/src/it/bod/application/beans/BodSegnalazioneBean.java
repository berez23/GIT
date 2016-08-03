package it.bod.application.beans;


import java.util.Date;
import java.util.Set;

import it.bod.application.common.MasterItem;

public class BodSegnalazioneBean extends MasterItem{

	private static final long serialVersionUID = -7500416290743820783L;
	
	private Long idSeg = null;
	private String protocollo = "";
	private String esitoControllo = "";
	private String codiceEnte = "";
	private Integer totaleAllegati = new Integer(1);
	private String nomeFile = "";
	private Double fileSize = 0d;
	private Double zipFileSize = 0d;
	private Boolean sopralluogo = false;
	private Date dataFineLavori = null;
	private Long istFk = new Long(0);
	private Set<BodFabbricatoBean> fabbricati = null;
	private Set<BodUiuBean> uius = null;
	
	public Long getIdSeg() {
		return idSeg;
	}
	public void setIdSeg(Long idSeg) {
		this.idSeg = idSeg;
	}
	public String getProtocollo() {
		return protocollo;
	}
	public void setProtocollo(String protocollo) {
		this.protocollo = protocollo;
	}
	public String getEsitoControllo() {
		return esitoControllo;
	}
	public void setEsitoControllo(String esitoControllo) {
		this.esitoControllo = esitoControllo;
	}
	public String getCodiceEnte() {
		return codiceEnte;
	}
	public void setCodiceEnte(String codiceEnte) {
		this.codiceEnte = codiceEnte;
	}
	public Integer getTotaleAllegati() {
		return totaleAllegati;
	}
	public void setTotaleAllegati(Integer totaleAllegati) {
		this.totaleAllegati = totaleAllegati;
	}
	public String getNomeFile() {
		return nomeFile;
	}
	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public Boolean getSopralluogo() {
		return sopralluogo;
	}
	public void setSopralluogo(Boolean sopralluogo) {
		this.sopralluogo = sopralluogo;
	}
	public Date getDataFineLavori() {
		return dataFineLavori;
	}
	public void setDataFineLavori(Date dataFineLavori) {
		this.dataFineLavori = dataFineLavori;
	}
	public Long getIstFk() {
		return istFk;
	}
	public void setIstFk(Long istId) {
		this.istFk = istId;
	}
	public Set<BodFabbricatoBean> getFabbricati() {
		return fabbricati;
	}
	public void setFabbricati(Set<BodFabbricatoBean> fabbricati) {
		this.fabbricati = fabbricati;
	}
	public Set<BodUiuBean> getUius() {
		return uius;
	}
	public void setUius(Set<BodUiuBean> uiu) {
		this.uius = uiu;
	}
	public Double getFileSize() {
		return fileSize;
	}
	public void setFileSize(Double fileSize) {
		this.fileSize = fileSize;
	}
	public Double getZipFileSize() {
		return zipFileSize;
	}
	public void setZipFileSize(Double zipFileSize) {
		this.zipFileSize = zipFileSize;
	}

}
