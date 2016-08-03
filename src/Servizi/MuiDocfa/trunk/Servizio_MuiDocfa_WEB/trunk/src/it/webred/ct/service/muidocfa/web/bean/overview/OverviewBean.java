package it.webred.ct.service.muidocfa.web.bean.overview;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;

import it.webred.ct.data.access.basic.diagnostiche.DiagnosticheDataIn;
import it.webred.ct.data.access.basic.diagnostiche.dto.CountDocfaReportDTO;
import it.webred.ct.service.muidocfa.web.bean.MuiDocfaBaseBean;

public class OverviewBean extends MuiDocfaBaseBean {
	
	private List<CountDocfaReportDTO> listaCountIciDto;
	private List<CountDocfaReportDTO> listaCountTarDto;
	private int totaleICI;
	private int elaboratiICI;
	private int elabNoAnomICI;
	private int elabSiAnomICI;
	private int totaleTAR;
	private int elaboratiTAR;
	private int elabNoAnomTAR;
	private int elabSiAnomTAR;
	private int totale;
	private int elaborati;
	private int elabNoAnom;
	private int elabSiAnom;
	private String dataElaborazioneIci;
	private String dataElaborazioneTarsu;

	@PostConstruct
	public void initService() {

		listaCountIciDto = new ArrayList<CountDocfaReportDTO>();
		listaCountTarDto = new ArrayList<CountDocfaReportDTO>();
		totale = 0;
		elaborati = 0;
		elabNoAnom = 0;
		elabSiAnom = 0;
		
		try {
			DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
			fillEnte(dataIn);
			List<Object[]> resultIci = docfaIciReportService.countDocfaFornitura(dataIn);
			for(Object[] o: resultIci){
				CountDocfaReportDTO dto = new CountDocfaReportDTO();
				dto.setFornitura((Date) o[0]);
				dto.setTotali((Long) o[1]);
				dto.setElaborati((Long) o[2]);
				dto.setElabNoAnom((Long) o[3]);
				dto.setElabSiAnom((Long) o[4]);
				
				listaCountIciDto.add(dto);
				//calcolo totali ici
				totaleICI += dto.getTotali().intValue();
				elaboratiICI += dto.getElaborati().intValue();
				elabNoAnomICI += dto.getElabNoAnom().intValue();
				elabSiAnomICI += dto.getElabSiAnom().intValue();
			}
			
			List<Object[]> resultTar = docfaTarReportService.countDocfaFornitura(dataIn);
			for(Object[] o: resultTar){
				CountDocfaReportDTO dto = new CountDocfaReportDTO();
				dto.setFornitura((Date) o[0]);
				dto.setTotali((Long) o[1]);
				dto.setElaborati((Long) o[2]);
				dto.setElabNoAnom((Long) o[3]);
				dto.setElabSiAnom((Long) o[4]);
				
				listaCountTarDto.add(dto);
				//calcolo totali tarsu
				totaleTAR += dto.getTotali().intValue();
				elaboratiTAR += dto.getElaborati().intValue();
				elabNoAnomTAR += dto.getElabNoAnom().intValue();
				elabSiAnomTAR += dto.getElabSiAnom().intValue();
			}
			
			//calcolo totali
			totale = totaleICI + totaleTAR;
			elaborati = elaboratiICI + elaboratiTAR;
			elabNoAnom = elabNoAnomICI + elabNoAnomTAR;
			elabSiAnom = elabSiAnomICI + elabSiAnomTAR;

		} catch (Throwable t) {
			super.addErrorMessage("lista.error", t.getMessage());
			t.printStackTrace();
		}
	}
	
	public String goOverview() {
		return "muidocfa.overview";
	}

	public int getTotale() {
		return totale;
	}

	public void setTotale(int totale) {
		this.totale = totale;
	}

	public int getElaborati() {
		return elaborati;
	}

	public void setElaborati(int elaborati) {
		this.elaborati = elaborati;
	}

	public int getElabNoAnom() {
		return elabNoAnom;
	}

	public void setElabNoAnom(int elabNoAnom) {
		this.elabNoAnom = elabNoAnom;
	}

	public int getElabSiAnom() {
		return elabSiAnom;
	}

	public void setElabSiAnom(int elabSiAnom) {
		this.elabSiAnom = elabSiAnom;
	}

	public int getTotaleICI() {
		return totaleICI;
	}

	public void setTotaleICI(int totaleICI) {
		this.totaleICI = totaleICI;
	}

	public int getElaboratiICI() {
		return elaboratiICI;
	}

	public void setElaboratiICI(int elaboratiICI) {
		this.elaboratiICI = elaboratiICI;
	}

	public int getElabNoAnomICI() {
		return elabNoAnomICI;
	}

	public void setElabNoAnomICI(int elabNoAnomICI) {
		this.elabNoAnomICI = elabNoAnomICI;
	}

	public int getElabSiAnomICI() {
		return elabSiAnomICI;
	}

	public void setElabSiAnomICI(int elabSiAnomICI) {
		this.elabSiAnomICI = elabSiAnomICI;
	}

	public int getTotaleTAR() {
		return totaleTAR;
	}

	public void setTotaleTAR(int totaleTAR) {
		this.totaleTAR = totaleTAR;
	}

	public int getElaboratiTAR() {
		return elaboratiTAR;
	}

	public void setElaboratiTAR(int elaboratiTAR) {
		this.elaboratiTAR = elaboratiTAR;
	}

	public int getElabNoAnomTAR() {
		return elabNoAnomTAR;
	}

	public void setElabNoAnomTAR(int elabNoAnomTAR) {
		this.elabNoAnomTAR = elabNoAnomTAR;
	}

	public int getElabSiAnomTAR() {
		return elabSiAnomTAR;
	}

	public void setElabSiAnomTAR(int elabSiAnomTAR) {
		this.elabSiAnomTAR = elabSiAnomTAR;
	}

	public List<CountDocfaReportDTO> getListaCountIciDto() {
		return listaCountIciDto;
	}

	public void setListaCountIciDto(List<CountDocfaReportDTO> listaCountIciDto) {
		this.listaCountIciDto = listaCountIciDto;
	}

	public List<CountDocfaReportDTO> getListaCountTarDto() {
		return listaCountTarDto;
	}

	public void setListaCountTarDto(List<CountDocfaReportDTO> listaCountTarDto) {
		this.listaCountTarDto = listaCountTarDto;
	}

}
