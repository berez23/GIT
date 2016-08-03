package it.escsolution.escwebgis.pregeo.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;
import java.util.Date;

public class PregeoInfo extends EscObject implements Serializable{
	
	private static final long serialVersionUID = -1985723639933389041L;
	
	private Long idInfo = null;
	private String nomeFilePdf = "";
	private String pathFilePdf= "";
	private Date dataPregeo = null;
	private String codicePregeo = "";
	private String denominazione = "";
	private String tecnico = "";
	private String tipoTecnico = "";
	private String foglio = "";
	private String particella = "";
	private Date dataInserimento = null;
	private String relazioneTecnica = "";
	private String nota = "";
	
	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public String getChiave(){
		return String.valueOf(this.idInfo);
	}
	
	public Long getIdInfo() {
		return idInfo;
	}
	public void setIdInfo(Long idInfo) {
		this.idInfo = idInfo;
	}
	public String getNomeFilePdf() {
		return nomeFilePdf;
	}
	public void setNomeFilePdf(String nomeFilePdf) {
		this.nomeFilePdf = nomeFilePdf;
	}
	public Date getDataPregeo() {
		return dataPregeo;
	}
	public void setDataPregeo(Date dataPregeo) {
		this.dataPregeo = dataPregeo;
	}
	public String getCodicePregeo() {
		return codicePregeo;
	}
	public void setCodicePregeo(String codicePregeo) {
		this.codicePregeo = codicePregeo;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}
	public String getTecnico() {
		return tecnico;
	}
	public void setTecnico(String tecnico) {
		this.tecnico = tecnico;
	}
	public String getTipoTecnico() {
		return tipoTecnico;
	}
	public void setTipoTecnico(String tipoTecnico) {
		this.tipoTecnico = tipoTecnico;
	}
	public String getFoglio() {
		return foglio;
	}
	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}
	public String getParticella() {
		return particella;
	}
	public void setParticella(String particella) {
		this.particella = particella;
	}
	public Date getDataInserimento() {
		return dataInserimento;
	}
	public void setDataInserimento(Date dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	public String getPathFilePdf() {
		return pathFilePdf;
	}

	public void setPathFilePdf(String pathFilePdf) {
		this.pathFilePdf = pathFilePdf;
	}

	public String getRelazioneTecnica() {
		return relazioneTecnica;
	}

	public void setRelazioneTecnica(String relazioneTecnica) {
		this.relazioneTecnica = relazioneTecnica;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
