package it.webred.ct.service.carContrib.web.pages.filtro;

import it.webred.ct.service.carContrib.data.access.cc.CarContribService;
import it.webred.ct.service.carContrib.data.access.cc.dto.FiltroRichiesteDTO;
import it.webred.ct.service.carContrib.data.access.cc.dto.FiltroRichiesteSearchCriteria;
import it.webred.ct.service.carContrib.web.beans.FiltroRichieste;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

public class FiltroDataProviderImpl extends FiltroBean implements FiltroDataProvider {
	
	private static final long serialVersionUID = 1L;
	private CarContribService carContribService;
	
	public CarContribService getCarContribService() {
		return carContribService;
	}

	public void setCarContribService(CarContribService carContribService) {
		this.carContribService = carContribService;
	}

	public List<FiltroRichieste> getDataByRange(FiltroRichiesteSearchCriteria criteria,int start, int numberRecord)
	{
		List<FiltroRichieste> listaFiltroRich = new ArrayList<FiltroRichieste>();

		List<FiltroRichiesteDTO> lista = super.getFiltroRichiesteService().filtroRichieste(criteria,start,numberRecord);
		
		for(FiltroRichiesteDTO rich:lista)
		{
			FiltroRichieste e = new FiltroRichieste();
			e.setRichiesta(rich);
			listaFiltroRich.add(e);
		}
		return listaFiltroRich;
	}

	public Long getRecordCount(FiltroRichiesteSearchCriteria criteria) {
	
		return super.getFiltroRichiesteService().getRecordCount(criteria);
	}

	public List<SelectItem> getDistinctUserName(CeTBaseObject cetObj) 
	{
		List<SelectItem> userNameSelect =  new ArrayList<SelectItem>();
		List<String> listaUserName  = carContribService.getDistinctUserName(cetObj);

		for(String userName: listaUserName)
		{
			userNameSelect.add(new SelectItem(userName, userName));
		}
		
		return userNameSelect;
	}
}
