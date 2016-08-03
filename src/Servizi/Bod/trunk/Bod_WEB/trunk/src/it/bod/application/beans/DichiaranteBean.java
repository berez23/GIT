package it.bod.application.beans;

import java.util.Date;

import it.bod.application.common.MasterItem;

public class DichiaranteBean extends MasterItem{

	private static final long serialVersionUID = 5318153745945891221L;
	
	private Date fornitura = null;
	private String protocollo_reg = "";
	private String dic_cognome = "";
	private String dic_nome = "";
	private String dic_res_com = "";
	private String dic_res_prov = "";
	private String dic_res_indir = "";
	private String dic_res_numciv = "";
	private String dic_res_cap = "";
	private String tec_cognome = "";
	private String tec_nome = "";
	private String tec_codfisc = "";
	private Integer tec_isc_albo = null;
	private Integer tec_isc_numord = null;
	private String tec_isc_prov = "";
	private Byte flag_doc_telem = null;
	
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
	public String getDic_cognome() {
		return dic_cognome;
	}
	public void setDic_cognome(String dic_cognome) {
		this.dic_cognome = dic_cognome;
	}
	public String getDic_nome() {
		return dic_nome;
	}
	public void setDic_nome(String dic_nome) {
		this.dic_nome = dic_nome;
	}
	public String getDic_res_com() {
		return dic_res_com;
	}
	public void setDic_res_com(String dic_res_com) {
		this.dic_res_com = dic_res_com;
	}
	public String getDic_res_prov() {
		return dic_res_prov;
	}
	public void setDic_res_prov(String dic_res_prov) {
		this.dic_res_prov = dic_res_prov;
	}
	public String getDic_res_indir() {
		return dic_res_indir;
	}
	public void setDic_res_indir(String dic_res_indir) {
		this.dic_res_indir = dic_res_indir;
	}
	public String getDic_res_numciv() {
		return dic_res_numciv;
	}
	public void setDic_res_numciv(String dic_res_numciv) {
		this.dic_res_numciv = dic_res_numciv;
	}
	public String getDic_res_cap() {
		return dic_res_cap;
	}
	public void setDic_res_cap(String dic_res_cap) {
		this.dic_res_cap = dic_res_cap;
	}
	public String getTec_cognome() {
		return tec_cognome;
	}
	public void setTec_cognome(String tec_cognome) {
		this.tec_cognome = tec_cognome;
	}
	public String getTec_nome() {
		return tec_nome;
	}
	public void setTec_nome(String tec_nome) {
		this.tec_nome = tec_nome;
	}
	public String getTec_codfisc() {
		return tec_codfisc;
	}
	public void setTec_codfisc(String tec_codfisc) {
		this.tec_codfisc = tec_codfisc;
	}
	public Integer getTec_isc_albo() {
		return tec_isc_albo;
	}
	public void setTec_isc_albo(Integer tec_isc_albo) {
		this.tec_isc_albo = tec_isc_albo;
	}
	public Integer getTec_isc_numord() {
		return tec_isc_numord;
	}
	public void setTec_isc_numord(Integer tec_isc_numord) {
		this.tec_isc_numord = tec_isc_numord;
	}
	public String getTec_isc_prov() {
		return tec_isc_prov;
	}
	public void setTec_isc_prov(String tec_isc_prov) {
		this.tec_isc_prov = tec_isc_prov;
	}
	public Byte getFlag_doc_telem() {
		return flag_doc_telem;
	}
	public void setFlag_doc_telem(Byte flag_doc_telem) {
		this.flag_doc_telem = flag_doc_telem;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

}
