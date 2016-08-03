package it.webred.ct.service.tsSoggiorno.data.access.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import it.webred.ct.service.tsSoggiorno.data.model.IsClasse;
import it.webred.ct.service.tsSoggiorno.data.model.IsPeriodo;
import it.webred.ct.service.tsSoggiorno.data.model.IsTariffa;

public class VersamentoDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private String importo;
	private BigDecimal importoSpeseComm;
	private Date dataContabile;
	private String motivo;
	private String codFiscale;
	private String segmento;
	private String societa;
	private String struttura;
	
	public String getCodFiscale() {
		return codFiscale;
	}
	public void setCodFiscale(String codFiscale) {
		this.codFiscale = codFiscale;
	}
	public String getSegmento() {
		return segmento;
	}
	public void setSegmento(String segmento) {
		this.segmento = segmento;
	}
	public String getImporto() {
		return importo;
	}
	public void setImporto(String importo) {
		this.importo = importo;
	}
	
	public BigDecimal getImportoSpeseComm() {
		return importoSpeseComm;
	}
	public void setImportoSpeseComm(BigDecimal importoSpeseComm) {
		this.importoSpeseComm = importoSpeseComm;
	}
	public Date getDataContabile() {
		return dataContabile;
	}
	public void setDataContabile(Date dataContabile) {
		this.dataContabile = dataContabile;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public String getSocieta() {
		return societa;
	}
	public void setSocieta(String societa) {
		this.societa = societa;
	}
	public String getStruttura() {
		return struttura;
	}
	public void setStruttura(String struttura) {
		this.struttura = struttura;
	}
	
	public String getFormatImportoSpeseComm() {
		String fImporto = "";
		try {
			if (getImporto() != null && !getImporto().trim().equals("")) {
				Double dImp =  Double.parseDouble(this.getImportoSpeseComm().toString());
				
				fImporto += " " + new DecimalFormat("#,##0.00").format((dImp/100));
				
				return fImporto;
				
			}			
		}catch (Exception e) {}
		return this.importoSpeseComm.toString();
	}

	public String getFormatImporto(){
		String fImporto = "";
		try {
			if (getImporto() != null && getImporto().trim().length()>1) {
				String sign = importo.substring(0,1);
				fImporto += sign.equals("+")||sign.equals("-") ? sign : "";
				
				Double dImp = Double.parseDouble(getImporto().trim().substring(1));
				
				fImporto += " " + new DecimalFormat("#,##0.00").format((dImp/100));
				
				return fImporto;
				
			}			
		}catch (Exception e) {}
			return importo;
		
	}
	
}
