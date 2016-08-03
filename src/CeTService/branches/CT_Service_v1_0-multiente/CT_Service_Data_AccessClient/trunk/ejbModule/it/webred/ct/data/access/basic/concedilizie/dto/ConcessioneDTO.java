package it.webred.ct.data.access.basic.concedilizie.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import it.webred.ct.data.model.concedilizie.SitCConcessioni;
import it.webred.ct.support.datarouter.CeTBaseObject;

public class ConcessioneDTO extends CeTBaseObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String concNumero; //CONCESSIONE_NUMERO oopure PROGRESSIVO_ANNO + PROGRESSIVO_NUMERO
	private Date dataProt;
	private String dataProtStr;
	private String tipoIntervento;
	private String oggetto;
	private Date dataRilascio;
	private String dataRilascioStr;
	private Date dataInizioLavori;
	private String dataInizioLavoriStr;
	private Date dataFineLavori;
	private String dataFineLavoriStr;
	private List<SoggettoConcessioneDTO> listaSoggetti;
	private String listaSoggettiHtml;
	private String stringaImmobili;
	
	public String getConcNumero() {
		return concNumero;
	}
	public void setConcNumero(String concNumero) {
		this.concNumero = concNumero;
	}
	
	public Date getDataProt() {
		return dataProt;
	}
	public void setDataProt(Date dataProt) {
		this.dataProt = dataProt;
		if (dataProt!=null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			dataProtStr= sdf.format(dataProt);	
		}else
			dataProtStr="";
		
	}
	public String getDataProtStr() {
		return dataProtStr;
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
		oggetto = oggetto;
	}
	public Date getDataRilascio() {
		return dataRilascio;
	}
	public void setDataRilascio(Date dataRilascio) {
		this.dataRilascio = dataRilascio;
	}
	public String getDataRilascioStr() {
		return dataRilascioStr;
	}
	public void setDataRilascioStr(String dataRilascioStr) {
		this.dataRilascioStr = dataRilascioStr;
	}
	public Date getDataInizioLavori() {
		return dataInizioLavori;
	}
	public void setDataInizioLavori(Date dataInizioLavori) {
		this.dataInizioLavori = dataInizioLavori;
		if (dataInizioLavori!=null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			dataInizioLavoriStr= sdf.format(dataInizioLavori);	
		}else
			dataInizioLavoriStr="";
	}
	public String getDataInizioLavoriStr() {
		return dataInizioLavoriStr;
	}
	
	public Date getDataFineLavori() {
		return dataFineLavori;
	}
	public void setDataFineLavori(Date dataFineLavori) {
		this.dataFineLavori = dataFineLavori;
		if (dataFineLavori!=null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			dataFineLavoriStr= sdf.format(dataFineLavori);	
		}else
			dataFineLavoriStr="";
	}
	public String getDataFineLavoriStr() {
		return dataFineLavoriStr;
	}
	
	public List<SoggettoConcessioneDTO> getListaSoggetti() {
		return listaSoggetti;
	}
	public void setListaSoggetti(List<SoggettoConcessioneDTO> listaSoggetti) {
		this.listaSoggetti = listaSoggetti;
		String listaStr="";
		if (listaSoggetti !=null ){
			for (SoggettoConcessioneDTO sogg: listaSoggetti) {
				if (!listaStr.equals(""))
					listaStr += "<br />";
				listaStr += sogg.getTitolo() + ": " + sogg.getDatiAnag();	
			}
		}
		listaSoggettiHtml = listaStr;
	}
	public String getListaSoggettiHtml() {
		return listaSoggettiHtml;
	}
	
	public String getStringaImmobili() {
		return stringaImmobili;
	}
	
	
	public void setStringaImmobili(String stringaImmobili) {
		this.stringaImmobili = stringaImmobili;
	}
	public void valorizzaDatiConc(SitCConcessioni conc) {
		String numConc="";
		if (conc.getProgressivoAnno()!=null && !conc.getProgressivoAnno().equals("") )
			numConc = conc.getProgressivoAnno();
		if (conc.getProgressivoNumero()!=null && !conc.getProgressivoNumero().equals("") )
			numConc += conc.getProgressivoNumero();
		if (conc.getConcessioneNumero()!=null && !conc.getConcessioneNumero().equals("") )
			numConc = conc.getConcessioneNumero();
		concNumero =numConc;
		setDataProt( conc.getProtocolloData());
		tipoIntervento =conc.getTipoIntervento()!=null ? conc.getTipoIntervento() : "";
		oggetto = conc.getOggetto()!=null ? conc.getOggetto() : "";
		setDataRilascio ( conc.getDataRilascio());
		setDataInizioLavori(getDataInizioLavori());
		setDataFineLavori(getDataFineLavori());
			
	}
	
	public void valorizzaDatiConc(SitCConcessioni conc, List<SoggettoConcessioneDTO> listaSogg, String strImm ) {
		valorizzaDatiConc(conc);
		setListaSoggetti(listaSogg);
		setStringaImmobili(strImm);
	}
}
