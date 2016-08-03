package it.webred.ct.diagnostics.service.web.beans.pagination;

import it.webred.ct.diagnostics.service.data.access.DiagnosticheService;
import it.webred.ct.diagnostics.service.data.dto.DiaDettaglioDTO;
import it.webred.ct.diagnostics.service.data.model.DiaLogAccesso;
import it.webred.ct.diagnostics.service.web.user.UserBean;

import java.util.ArrayList;
import java.util.List;


public class LogDataProviderImpl extends UserBean implements LogDataProvider{
	
	private DiaDettaglioDTO diaDettaglioDTO = new DiaDettaglioDTO(getUser().getCurrentEnte(), getUser().getName());

	private Long idDiaTestata;
	private DiagnosticheService diaService;
	
	public DiagnosticheService getDiaService() {
		return diaService;
	}

	public void setDiaService(DiagnosticheService diaService) {
		this.diaService = diaService;
	}

	public List<DiaLogAccesso> getDettaglioByRange(int start, int rowNumber) {
		
		getLogger().debug("Recupero log accessi paginati");
		List<DiaLogAccesso> dettaglis = new ArrayList<DiaLogAccesso>();
		
		try {
			
			diaDettaglioDTO.setIdDiaTestata(idDiaTestata);
			diaDettaglioDTO.setStart(start);
			diaDettaglioDTO.setMaxrows(rowNumber);
			dettaglis = diaService.getLogAccessiByDiaTestata(diaDettaglioDTO) ;
			if (dettaglis == null || dettaglis.size() == 0) {
				super.getLogger().debug("[LogAccessiBean.doInit] - Nessun accesso per la dia");
			}
			getLogger().debug("Item list: "+ dettaglis.size());
			
		}catch(Exception e) {
			getLogger().error("Eccezione: "+e.getMessage(),e);
		}
		
		return dettaglis;
	}

	public long getDettaglioCount() {
		long size = 0;
		
		try {
			if(super.getUser() != null) {
				DiaDettaglioDTO dd = new DiaDettaglioDTO(super.getUser().getCurrentEnte(),super.getUser().getName());
				dd.setIdDiaTestata(idDiaTestata);				
				size =  diaService.getCountLog(dd).longValue();
			}
		}catch(Exception e) {
			getLogger().error("Eccezione: "+e.getMessage(),e);
		}
		
		return size;
	}
	
	
	public DiaDettaglioDTO getDiaDettaglioDTO() {
		return diaDettaglioDTO;
	}

	public void setDiaDettaglioDTO(DiaDettaglioDTO diaDettaglioDTO) {
		this.diaDettaglioDTO = diaDettaglioDTO;
	}

	public Long getIdDiaTestata() {
		return idDiaTestata;
	}

	public void setIdDiaTestata(Long idDiaTestata) {
		this.idDiaTestata = idDiaTestata;
	}	

}
