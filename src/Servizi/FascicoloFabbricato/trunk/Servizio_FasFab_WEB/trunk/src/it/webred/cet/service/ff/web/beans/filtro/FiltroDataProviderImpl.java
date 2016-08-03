package it.webred.cet.service.ff.web.beans.filtro;

import it.webred.cet.permission.CeTUser;
import it.webred.cet.service.ff.web.UserBean;
import it.webred.ct.service.ff.data.access.common.FFCommonService;
import it.webred.ct.service.ff.data.access.filtro.dto.FFFiltroRichiesteSearchCriteria;
import it.webred.ct.service.ff.data.access.filtro.dto.FiltroRichiesteDTO;
import it.webred.ct.service.ff.data.access.richieste.GestRichiestaService;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

public class FiltroDataProviderImpl extends FiltroBean implements FiltroDataProvider {
	
	private static final long serialVersionUID = 1L;
	
	private GestRichiestaService richiestaService;
	private FFCommonService commonService;
	
	public GestRichiestaService getRichiestaService() {
		return richiestaService;
	}
	public void setRichiestaService(GestRichiestaService richiestaService) {
		this.richiestaService = richiestaService;
	}
	public FFCommonService getCommonService() {
		return commonService;
	}
	public void setCommonService(FFCommonService commonService) {
		this.commonService = commonService;
	}

	
	public List<FiltroRichieste> getDataByRange(FFFiltroRichiesteSearchCriteria criteria,int start, int numberRecord)
	{
		List<FiltroRichieste> listaFiltroRich = new ArrayList<FiltroRichieste>();

		List<FiltroRichiesteDTO> lista = richiestaService.filtraRichieste(criteria,start,numberRecord);
		for(FiltroRichiesteDTO rich:lista)
		{
			FiltroRichieste e = new FiltroRichieste();
			e.setRichiesta(rich);
			listaFiltroRich.add(e);
		}
		return listaFiltroRich;
	}

	public Long getRecordCount(FFFiltroRichiesteSearchCriteria criteria) {

		return richiestaService.getRecordCount(criteria);
	}

	public void resetData() {
		// TODO Auto-generated method stub
		
	}

	public List<SelectItem> getDistinctUserName(CeTBaseObject cet) 
	{
	
		List<SelectItem> userNameSelect =  new ArrayList<SelectItem>();
		
		
		List<String> listaUserName  = commonService.getDistinctUserName(cet);
		if (listaUserName != null) {
			for(String userName: listaUserName)
			{
				userNameSelect.add(new SelectItem(userName, userName));
				
			}
		}
		return userNameSelect;
		
		
		
	}
}
