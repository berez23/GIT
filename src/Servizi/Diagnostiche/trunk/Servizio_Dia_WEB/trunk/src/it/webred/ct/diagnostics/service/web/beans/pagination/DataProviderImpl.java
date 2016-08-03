package it.webred.ct.diagnostics.service.web.beans.pagination;

import java.util.ArrayList;
import java.util.List;

import it.webred.ct.diagnostics.service.data.access.DiaDettaglioService;
import it.webred.ct.diagnostics.service.data.dto.DiaDettaglioDTO;
import it.webred.ct.diagnostics.service.data.util.DiaUtility;
import it.webred.ct.diagnostics.service.web.user.UserBean;


public class DataProviderImpl extends UserBean implements DataProvider{
	
	private DiaDettaglioDTO diaDettaglioDTO = new DiaDettaglioDTO(getUser().getCurrentEnte(), getUser().getName());

	private Long idDiaTestata;
	private String modelClassname;
	private String shortModelClassname;
	
	private List<String> columns;
	
	private DiaDettaglioService diaDettaglioService;
	
	
	public List<String[]> getDettaglioByRange(int start, int rowNumber) {
		
		getLogger().debug("Recupero dettaglio diagnostica");
		List<String[]> dettaglis = new ArrayList<String[]>();
		
		try {
			shortModelClassname = DiaUtility.getNameFromClass(modelClassname);
			
			DiaDettaglioDTO dd = new DiaDettaglioDTO(super.getEnte(),super.getUser().getName());
			dd.setStart(start);
			dd.setMaxrows(rowNumber);
			dd.setIdDiaTestata(idDiaTestata);
			dd.setModelClassname(modelClassname);
			
			dettaglis = diaDettaglioService.getDettaglioDiagnosticaArray(dd);
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
				dd.setModelClassname(modelClassname);
				size =  diaDettaglioService.getCount(dd).longValue();
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

	public String getModelClassname() {
		return modelClassname;
	}

	public void setModelClassname(String modelClassname) {
		this.modelClassname = modelClassname;
	}

	public String getShortModelClassname() {
		return shortModelClassname;
	}

	public void setShortModelClassname(String shortModelClassname) {
		this.shortModelClassname = shortModelClassname;
	}

	public List<String> getColumns() {
		columns = new ArrayList<String>();
		
		try {
			Class c = Class.forName(modelClassname);
			List<String> metodi = DiaUtility.getOrderedModelClassGETMethods(DiaUtility.getModelClassGETMethods(c), c);
			
			for(String m: metodi) {
				columns.add(m.substring(3).toUpperCase());
			}
			
		}catch(Exception e) {
			super.getLogger().error("Eccezione columns: "+e.getMessage(),e);
		}
		
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	public DiaDettaglioService getDiaDettaglioService() {
		return diaDettaglioService;
	}

	public void setDiaDettaglioService(DiaDettaglioService diaDettaglioService) {
		this.diaDettaglioService = diaDettaglioService;
	}
	

}
