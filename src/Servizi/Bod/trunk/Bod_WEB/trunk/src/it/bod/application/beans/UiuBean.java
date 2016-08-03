package it.bod.application.beans;

import java.util.Date;

import it.bod.application.common.MasterItem;

public class UiuBean extends MasterItem{

	private static final long serialVersionUID = 3375853598048499063L;
	
	private Date fornitura = null;
	private String protocollo_reg = "";
	private String indir_toponimo = "";
	private String indir_nciv_uno = "";
	private String foglio = "";
	private String particella = "";
	private String subalterno = "";
	private String categoria = "";
	private String tipo_operazione = "";
	

	
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getTipo_operazione() {
		return tipo_operazione;
	}
	public void setTipo_operazione(String tipo_operazione) {
		this.tipo_operazione = tipo_operazione;
	}
	public Date getFornitura() {
		return fornitura;
	}
	public void setFornitura(Date fornitura) {
		this.fornitura = fornitura;
	}
	public String getProtocollo_reg() {
		return protocollo_reg;
	}
	public void setProtocollo_reg(String protocollo_reg) {
		this.protocollo_reg = protocollo_reg;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public String getIndir_toponimo() {
		return indir_toponimo;
	}
	public void setIndir_toponimo(String indir_toponimo) {
		this.indir_toponimo = indir_toponimo;
	}
	public String getIndir_nciv_uno() {
		return indir_nciv_uno;
	}
	public void setIndir_nciv_uno(String indir_nciv_uno) {
		this.indir_nciv_uno = indir_nciv_uno;
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
	

}
