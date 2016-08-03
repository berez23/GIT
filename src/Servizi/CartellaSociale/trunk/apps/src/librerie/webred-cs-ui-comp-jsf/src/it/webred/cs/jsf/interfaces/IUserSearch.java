package it.webred.cs.jsf.interfaces;

import java.util.List;

public interface IUserSearch {

	
	public List<?> getLstSoggetti(String query) ;
	
	public void handleChangeUser(javax.faces.event.AjaxBehaviorEvent event);
	
	public String getWidgetVar();
	
	public String getIdSoggetto();
	
	public Integer getMaxResult();

}
