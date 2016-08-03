package it.webred.gitland.web.bean;


import it.webred.gitland.web.bean.util.UserBean;
import it.webred.gitland.ejb.client.GitLandSessionBeanRemote;



import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;


@ManagedBean
@ViewScoped
public class MapMan extends GitLandBaseBean{
	
	private String mapUrl;
	
	public MapMan() {
	}//-------------------------------------------------------------------------
	
	public void initializeData(){
		
    	String enteCorrente = this.getEnte();
   	 	GitLandSessionBeanRemote bnService = null;
   	 
   		try {
   			bnService = this.getGitLandService();
   			
   			caricaMapUrl();
   			
   		} catch (Exception e) {
   			addError("dettaglio.mappa.error", e.getMessage());
   			logger.error(e.getMessage(), e);
   		}
    }//-------------------------------------------------------------------------

	public void initializeData(String codLotto, String foglio){
		
    	String enteCorrente = this.getEnte();
   	 	GitLandSessionBeanRemote bnService = null;

   		try {
   			bnService = this.getGitLandService();
   			
   			caricaMapUrlLotto(codLotto, foglio);
   			
   		} catch (Exception e) {
   			addError("dettaglio.mappa.error", e.getMessage());
   			logger.error(e.getMessage(), e);
   		}
    }//-------------------------------------------------------------------------

	public void caricaMapUrl() {
		UserBean uBean = (UserBean) getBeanReference("userBean");
		String utente = uBean.getUsername();
	
		mapUrl = getBaseUrl() + "&cod_nazionale=A'OR'A'<'B'OR'A'<'B&param=&user=" + utente;
	}//-------------------------------------------------------------------------
	
	public void caricaMapUrlLotto(String codLotto, String foglio) {
		UserBean uBean = (UserBean) getBeanReference("userBean");
		String utente = uBean.getUsername();
		//lottoIdAsiMapSelected
		
		mapUrl = getBaseUrl() + "&cod_nazionale=" + getEnte() + "&param=&user=" + utente + (foglio!=null?"&foglio=" + foglio:"") + "&codLotto=" + codLotto ;
		//mapUrl = getBaseUrl() + "&cod_nazionale=" + getEnte() + "&user=" + utente + "&codLotto=" + codLotto ;
		
	}//-------------------------------------------------------------------------
	
	private String getBaseUrl(){
		UserBean uBean = (UserBean) getBeanReference("userBean");
		String serverName = getRequest().getServerName();
		int port = getRequest().getServerPort();
		String ds = this.getDatasource();
		
		String s = "http://" + serverName + ":" + port
				+ "/viewerjs_GitLand/extra/viewerjs/map?ente=" + uBean.getEnte()
				+ "&ds=" + ds
				+ "&qryoper=equal";
		return s;
	}//-------------------------------------------------------------------------

	public String getMapUrl() {
		return mapUrl;
	}

	public void setMapUrl(String mapUrl) {
		this.mapUrl = mapUrl;
	}


	
}
