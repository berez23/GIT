package it.bod.application.beans;

import java.util.Date;

import it.bod.application.common.MasterItem;

public class DatiGeneraliBean   extends MasterItem{

	private static final long serialVersionUID = 7914870340471025669L;

	private Date fornitura = null;
	private String annoFornitura = "";
	private String protocollo_reg = "";
	private Date data_variazione = null;
	private Integer uiu_in_soppressione = null;
	private Integer uiu_in_variazione = null;
	private Integer uiu_in_costituzione = null;
	private String causale_nota_vax = "";

	
	public String getCausale_nota_vax() {
		return causale_nota_vax;
	}
	public void setCausale_nota_vax(String causale_nota_vax) {
		this.causale_nota_vax = causale_nota_vax;
	}
	public Date getFornitura() {
		return fornitura;
	}
	public void setFornitura(Date fornitura) {
		this.fornitura = fornitura;
	}
	public String getAnnoFornitura() {
		return annoFornitura;
	}
	public void setAnnoFornitura(String annoFornitura) {
		this.annoFornitura = annoFornitura;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public String getProtocollo_reg() {
		return protocollo_reg;
	}
	public void setProtocollo_reg(String protocollo_reg) {
		this.protocollo_reg = protocollo_reg;
	}
	public Date getData_variazione() {
		return data_variazione;
	}
	public void setData_variazione(Date data_variazione) {
		this.data_variazione = data_variazione;
	}
	public Integer getUiu_in_soppressione() {
		return uiu_in_soppressione;
	}
	public void setUiu_in_soppressione(Integer uiu_in_soppressione) {
		this.uiu_in_soppressione = uiu_in_soppressione;
	}
	public Integer getUiu_in_variazione() {
		return uiu_in_variazione;
	}
	public void setUiu_in_variazione(Integer uiu_in_variazione) {
		this.uiu_in_variazione = uiu_in_variazione;
	}
	public Integer getUiu_in_costituzione() {
		return uiu_in_costituzione;
	}
	public void setUiu_in_costituzione(Integer uiu_in_costituzione) {
		this.uiu_in_costituzione = uiu_in_costituzione;
	}
	

}
