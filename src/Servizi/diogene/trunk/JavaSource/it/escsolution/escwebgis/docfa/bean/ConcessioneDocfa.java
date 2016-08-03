
package it.escsolution.escwebgis.docfa.bean;

import it.escsolution.escwebgis.common.EscObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ConcessioneDocfa extends EscObject implements Serializable{
	
	String idConc;
	String foglio ; 
	String particella ; 
	String subalterno;
	String protocollo;
	String annoProtocollo;
	String dataPotocollo;
	String CodFiscPI;
	String RagSoc;
	String nome;
	String tipoSoggetto;
	public String getIdConc() {
		return idConc;
	}
	public void setIdConc(String idConc) {
		this.idConc = idConc;
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
	public String getSubalterno() {
		return subalterno;
	}
	public void setSubalterno(String subalterno) {
		this.subalterno = subalterno;
	}
	public String getProtocollo() {
		return protocollo;
	}
	public void setProtocollo(String protocollo) {
		this.protocollo = protocollo;
	}
	public String getAnnoProtocollo() {
		return annoProtocollo;
	}
	public void setAnnoProtocollo(String annoProtocollo) {
		this.annoProtocollo = annoProtocollo;
	}
	public String getDataPotocollo() {
		return dataPotocollo;
	}
	public void setDataPotocollo(String dataPotocollo) {
		this.dataPotocollo = dataPotocollo;
	}
	public String getCodFiscPI() {
		return CodFiscPI;
	}
	public void setCodFiscPI(String codFiscPI) {
		CodFiscPI = codFiscPI;
	}
	public String getRagSoc() {
		return RagSoc;
	}
	public void setRagSoc(String ragSoc) {
		RagSoc = ragSoc;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getTipoSoggetto() {
		return tipoSoggetto;
	}
	public void setTipoSoggetto(String tipoSoggetto) {
		this.tipoSoggetto = tipoSoggetto;
	}
	

}
