package it.webred.ct.service.segnalazioniqualificate.web.bean.pagination;

import it.webred.ct.data.access.basic.segnalazionequalificata.SegnalazioniDataIn;
import it.webred.ct.data.access.basic.segnalazionequalificata.dto.PraticaSegnalazioneDTO;
import it.webred.ct.data.access.basic.segnalazionequalificata.dto.RicercaPraticaSegnalazioneDTO;
import it.webred.ct.data.model.segnalazionequalificata.SOfPratica;

import it.webred.ct.service.segnalazioniqualificate.web.bean.SegnalazioniQualificateBaseBean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

public class DataProviderImpl extends SegnalazioniQualificateBaseBean implements DataProvider {

	private RicercaPraticaSegnalazioneDTO criteria = new RicercaPraticaSegnalazioneDTO(super.getUser().getUsername());
	
	private boolean disableFlag;
	
	public List<PraticaSegnalazioneDTO> getReportByRange(int start, int rowNumber) {

		List<PraticaSegnalazioneDTO> list = new ArrayList<PraticaSegnalazioneDTO>();
		try {
			criteria.setStartm(start);
			criteria.setNumberRecord(rowNumber);
			SegnalazioniDataIn dataIn = this.getInitRicercaParams();
			fillEnte(criteria);
			dataIn.setRicercaPratica(criteria);
			List<SOfPratica> result = segnalazioneService.search(dataIn);
			for(SOfPratica p: result){
				PraticaSegnalazioneDTO dto = new PraticaSegnalazioneDTO();
				dto.setPratica(p);
				list.add(dto);
			}
				
		} catch (Throwable t) {
			super.addErrorMessage("lista.error", t.getMessage());
			t.printStackTrace();
		}
		return list;
	}

	public Long getReportCount() {
		SegnalazioniDataIn dataIn = this.getInitRicercaParams();
		fillEnte(criteria);
		dataIn.setRicercaPratica(criteria);
		Long result = segnalazioneService.searchCount(dataIn);
		return result;
	}
	
	public void doConfermaOperatori(){
		criteria.setsOperatori("");
		//String currUser = super.getUser().getUsername();
		String nuovoOp = "";
		boolean esiste = false;
		for (String op : criteria.getOperatori()) {
			nuovoOp += "," + op;
			//if(currUser.equalsIgnoreCase(nuovoOp))
			//	esiste = true;		
		}
		
	//	if (!esiste){
	//		criteria.addCodOperatore(currUser);
	//		nuovoOp += "," + currUser;}
		
		criteria.setsOperatori(nuovoOp.replaceFirst(",", ""));
		this.getLogger().info(nuovoOp);
	}
	
	public String goSearch() {
		criteria = new RicercaPraticaSegnalazioneDTO(super.getUser().getUsername());
		return "segnalazioni.search";
	}

	public void resetData() {
		criteria = new RicercaPraticaSegnalazioneDTO(super.getUser().getUsername());
	}
	
	public RicercaPraticaSegnalazioneDTO getCriteria() {
		return criteria;
	}

	public void setCriteria(RicercaPraticaSegnalazioneDTO criteria) {
		this.criteria = criteria;
	}

}
