package it.webred.cs.csa.web.manbean;

import it.webred.cs.jsf.bean.DatiUserSearchBean;
import it.webred.cs.jsf.interfaces.IUserSearch;
import it.webred.cs.jsf.manbean.superc.CsUiCompBaseBean;
import it.webred.ct.data.access.basic.anagrafe.AnagrafeService;
import it.webred.ct.data.access.basic.anagrafe.dto.RicercaSoggettoAnagrafeDTO;
import it.webred.ct.data.model.anagrafe.SitDPersona;
import it.webred.ejb.utility.ClientUtility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.naming.NamingException;

@ManagedBean
@ViewScoped
public class UserSearchBean extends CsUiCompBaseBean implements IUserSearch{

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
	
	private String widgetVar = "userSearchVar";
	private String idSoggetto;
	private Integer maxResult = 15;
	
	@Override
	public List<DatiUserSearchBean> getLstSoggetti(String query) {
		
		List<DatiUserSearchBean> listAutocomplete = new ArrayList<DatiUserSearchBean>();
		RicercaSoggettoAnagrafeDTO rsDto = new RicercaSoggettoAnagrafeDTO();
		fillEnte(rsDto);
		rsDto.setDenom(query);
		rsDto.setMaxResult(maxResult);
		try {
			
			AnagrafeService anagrafeService = (AnagrafeService) ClientUtility.getEjbInterface("CT_Service", "CT_Service_Data_Access", "AnagrafeServiceBean");
			
			List<SitDPersona> list = anagrafeService.getListaPersoneByDenominazione(rsDto);
			
			for(SitDPersona s: list){
				
				DatiUserSearchBean sDto = new DatiUserSearchBean();
				sDto.setSoggetto(s);	
				String itemLabel = s.getCognome().toUpperCase() + " " + s.getNome().toUpperCase();
				if(s.getDataNascita() != null)
				itemLabel += " nato il: " + sdf.format(s.getDataNascita());
				
				
				sDto.setItemLabel(itemLabel);
				sDto.setId(s.getId());
				listAutocomplete.add(sDto);
			}
		} catch (NamingException e) {
			addError("general", "caricamento.error");
			logger.error(e.getMessage(), e);
		}
		
		return listAutocomplete;
	}

	@Override
	public void handleChangeUser(AjaxBehaviorEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getWidgetVar() {
		return widgetVar;
	}

	public void setWidgetVar(String widgetVar) {
		this.widgetVar = widgetVar;
	}

	@Override
	public String getIdSoggetto() {
		return idSoggetto;
	}

	public void setIdSoggetto(String idSoggetto) {
		this.idSoggetto = idSoggetto;
	}

	@Override
	public Integer getMaxResult() {
		return maxResult;
	}

	public void setMaxResult(Integer maxResult) {
		this.maxResult = maxResult;
	}
	
}
