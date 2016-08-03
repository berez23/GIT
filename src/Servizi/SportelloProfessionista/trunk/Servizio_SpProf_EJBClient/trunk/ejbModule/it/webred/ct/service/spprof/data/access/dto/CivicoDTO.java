package it.webred.ct.service.spprof.data.access.dto;

import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;

public class CivicoDTO extends CeTBaseObject implements Serializable {
	
	private Long pkidCivi;
	private String numero;
	private String prefisso;
	private String nome;
	private String civico;
	private int startRecord;
	private int numRecord;
	
	public Long getPkidCivi() {
		return pkidCivi;
	}
	public void setPkidCivi(Long pkidCivi) {
		this.pkidCivi = pkidCivi;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getPrefisso() {
		return prefisso;
	}
	public void setPrefisso(String prefisso) {
		this.prefisso = prefisso;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCivico() {
		return civico;
	}
	public void setCivico(String civico) {
		this.civico = civico;
	}
	public int getStartRecord() {
		return startRecord;
	}
	public void setStartRecord(int startRecord) {
		this.startRecord = startRecord;
	}
	public int getNumRecord() {
		return numRecord;
	}
	public void setNumRecord(int numRecord) {
		this.numRecord = numRecord;
	}
	
}
