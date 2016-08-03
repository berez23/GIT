package it.webred.gitout.dto;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="docfaDTO")
public class DocfaDTO implements Serializable{

	private static final long serialVersionUID = 4103633292809998399L;
	
	private String descrizione = "";
	
	private DocfaDatoGeneraleDTO datoGenerale = null;
	
	private List<DocfaDichiaranteDTO> dichiarantiLst = null;
	
	private List<DocfaUnitaImmobiliareDTO> unitaImmobilliariLst = null;
	
	private List<DocfaCensuarioValoreDTO> datiCensuariLst = null;

	public DocfaDTO() {

	}//-------------------------------------------------------------------------

	public DocfaDatoGeneraleDTO getDatoGenerale() {
		return datoGenerale;
	}

	public void setDatoGenerale(DocfaDatoGeneraleDTO datoGenerale) {
		this.datoGenerale = datoGenerale;
	}

	public List<DocfaDichiaranteDTO> getDichiarantiLst() {
		return dichiarantiLst;
	}

	public void setDichiarantiLst(List<DocfaDichiaranteDTO> dichiarantiLst) {
		this.dichiarantiLst = dichiarantiLst;
	}

	public List<DocfaUnitaImmobiliareDTO> getUnitaImmobilliariLst() {
		return unitaImmobilliariLst;
	}

	public void setUnitaImmobilliariLst(
			List<DocfaUnitaImmobiliareDTO> unitaImmobilliariLst) {
		this.unitaImmobilliariLst = unitaImmobilliariLst;
	}

	public List<DocfaCensuarioValoreDTO> getDatiCensuariLst() {
		return datiCensuariLst;
	}

	public void setDatiCensuariLst(List<DocfaCensuarioValoreDTO> datiCensuariLst) {
		this.datiCensuariLst = datiCensuariLst;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	
	

}
