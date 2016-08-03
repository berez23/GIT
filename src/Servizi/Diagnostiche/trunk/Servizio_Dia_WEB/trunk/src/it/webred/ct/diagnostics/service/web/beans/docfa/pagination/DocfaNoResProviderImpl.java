package it.webred.ct.diagnostics.service.web.beans.docfa.pagination;

import it.webred.ct.diagnostics.service.data.access.DiaDocfaService;
import it.webred.ct.diagnostics.service.data.dto.DiaDocfaDTO;
import it.webred.ct.diagnostics.service.data.model.DocfaNonResidenziale;
import it.webred.ct.diagnostics.service.data.util.DiaUtility;
import it.webred.ct.diagnostics.service.web.beans.docfa.DocfaNonResidenzialiBean;
import it.webred.ct.diagnostics.service.web.user.UserBean;

import java.util.ArrayList;
import java.util.List;


public class DocfaNoResProviderImpl extends UserBean implements DocfaNoResDataProvider{

	private static final long serialVersionUID = 1L;
	
	private String categoria;
	private String fornituraDa;
	private String fornituraA;
	
	private DiaDocfaService diaDocfaService;
	
	public String doInit(){
		getLogger().info("[DocfaNoResProviderImpl.doInit] - Start");
		
		if (getBeanReference("docfaNonResidenzialiBean") !=  null){
			DocfaNonResidenzialiBean bean = (DocfaNonResidenzialiBean)getBeanReference("docfaNonResidenzialiBean");
			fornituraDa = bean.getDaFornitura();
			fornituraA = bean.getaFornitura();
			
		}
		getLogger().debug("[DocfaNoResProviderImpl.doInit] - Fornitura Da:" + fornituraDa );
		getLogger().debug("[DocfaNoResProviderImpl.doInit] - Fornitura A:" + fornituraA );
		getLogger().info("[DocfaNoResProviderImpl.doInit] - End");	
		
		return "diagnostiche.docfaListaNoRes";
	}
	
	public List<DocfaNonResidenziale> getVisualizzaByRange(int start, int rowNumber) {
		getLogger().info("[Lista docfa no res] - Start");				
		
		List<DocfaNonResidenziale> listaDocfa = new ArrayList<DocfaNonResidenziale>();
			
		try {
																		
			DiaDocfaDTO dc = new DiaDocfaDTO(getUser().getCurrentEnte(),getUser().getName());
			dc.setCategoria(getCategoria());
			
			if(fornituraDa != null) {
				dc.setFornituraDa(DiaUtility.stringMonthToFirstDayDate(fornituraDa).getTime());
			}
			
			if(fornituraA != null) {
				dc.setFornituraA(DiaUtility.stringMonthToLastDayDate(fornituraA).getTime());
			}
					
			dc.setStart(start);
			dc.setMaxrows(rowNumber);
			listaDocfa = diaDocfaService.getDocfaNonResidenziale(dc);						
						
			getLogger().info("[Lista docfa no res] - End");
			
		}catch(Exception e) {
			getLogger().warn("Eccezione: "+e.getMessage());
			listaDocfa = new ArrayList<DocfaNonResidenziale>();
		}
		
		return listaDocfa;
	}

	public long getVisualizzaCount() {
		long size = 0;
		
		try {
			if(super.getUser() != null) {
				DiaDocfaDTO dc = new DiaDocfaDTO(getUser().getCurrentEnte(),getUser().getName());
				dc.setCategoria(getCategoria());
				if(fornituraDa != null) {
					dc.setFornituraDa(DiaUtility.stringMonthToFirstDayDate(fornituraDa).getTime());
				}
				
				if(fornituraA != null) {
					dc.setFornituraA(DiaUtility.stringMonthToLastDayDate(fornituraA).getTime());
				}
				size =  diaDocfaService.getDocfaNonResidenzialeCount(dc);
			}
		}catch(Exception e) {
			getLogger().error("Eccezione: "+e.getMessage(),e);
		}
		
		return size;
	}
	

	public DiaDocfaService getDiaDocfaService() {
		return diaDocfaService;
	}

	public void setDiaDocfaService(DiaDocfaService diaDocfaService) {
		this.diaDocfaService = diaDocfaService;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getFornituraDa() {
		return fornituraDa;
	}

	public void setFornituraDa(String fornituraDa) {
		this.fornituraDa = fornituraDa;
	}

	public String getFornituraA() {
		return fornituraA;
	}

	public void setFornituraA(String fornituraA) {
		this.fornituraA = fornituraA;
	}


}
