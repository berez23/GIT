package it.webred.gitland.web.bean;

import it.webred.amprofiler.ejb.user.UserService;
import it.webred.cet.permission.CeTUser;
import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.pgt.PGTService;
import it.webred.ejb.utility.ClientUtility;
import it.webred.gitland.data.model.Parametro;
import it.webred.gitland.ejb.client.GitLandSessionBeanRemote;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import sun.misc.BASE64Encoder;

public class GitLandBaseBean extends ComponentBaseBean{
	
	private static final String GITLAND_PERMISSION = "GITLAND_PERMISSION";
	private static final String GIT_LAND_AVANZATO = "GitLand Avanzato";
	private static final String GIT_LAND_OPERATORE = "GitLand Operatore";
	private static final String GIT_LAND_ENTE = "GitLand Ente";
	
	private static final String GIT_LAND_AMMINISTRATORE = "GitLand Amministratore";
	private static final String GITLAND_AMMINISTRATORE = "GITLAND_AMMINISTRATORE";
	private static final String GITLAND_OPERATORE = "GITLAND_OPERATORE";
	private static final String GITLAND_AVANZATO = "GITLAND_AVANZATO";
	private static final String GITLAND_ENTE = "GITLAND_ENTE";
	private String datasource;
	public static final String NOME_PROPERTIES = "datarouter.properties";
	public static final String NOME_PATH_JBOSS_CONF = "jboss.server.config.dir";
	
 	//private GitLandSessionBeanRemote gitLandService;
 	
	protected GitLandSessionBeanRemote gitLandService = null;
	private CatastoService catastoService = null;
	 
	protected UserService userService = null;
	 
	public GitLandBaseBean() {
		try{

			gitLandService = getGitLandService();
			
		}catch(Exception e){
			addErrorFromProperties("caricamento.error");
			logger.error(e.getMessage(),e);
		}

	}//-------------------------------------------------------------------------

	protected void initializeData() {
		
	}//-------------------------------------------------------------------------
	
	public Object getBeanReference(String beanName) {
		FacesContext context = FacesContext.getCurrentInstance();
		Application a = context.getApplication();
		Object o = a.getVariableResolver().resolveVariable(context, beanName);
		return o;
	}//-------------------------------------------------------------------------

	public String getEnte() {
		HttpSession session = this.getSession();
		CeTUser user = (CeTUser) session.getAttribute("user");
		if (user != null) {
					
			return user.getCurrentEnte();
			
		}
		return null;
	}//-------------------------------------------------------------------------
	
	public String getCurrUserName() {
		HttpSession session = this.getSession();
		CeTUser user = (CeTUser) session.getAttribute("user");
		if (user != null) {
					
			return user.getName();
			
		}
		return "";
	}//-------------------------------------------------------------------------
	
	public String getGitLandPermission() {
		/*
		 * questo metodo è invocato da un tag hidden nel layout.xhtml presente 
		 * layoutUnit posizionata a south prima del footer
		 */
		HttpSession session = this.getSession();
		String permessi = (String)session.getAttribute(GITLAND_PERMISSION);
		if (permessi == null){
			CeTUser user = (CeTUser) session.getAttribute("user");
			if (user != null) {
				HashMap<String, String> hmPerm = user.getPermList();
				Iterator itPerm = hmPerm.entrySet().iterator();
				while(itPerm.hasNext()){
					Map.Entry obj = (Map.Entry)itPerm.next();
					//permission@-@GitLand@-@GitLand@-@GitLand Amministratore
					//permission@-@GitLand@-@GitLand@-@GitLand Operatore
					if (obj != null && obj.getValue() != null && obj.getValue().toString().indexOf(GIT_LAND_AMMINISTRATORE) != -1){
						permessi = GITLAND_AMMINISTRATORE;
					}else if (obj != null && obj.getValue() != null && obj.getValue().toString().indexOf(GIT_LAND_OPERATORE) != -1){
						permessi = GITLAND_OPERATORE;						
					}else if (obj != null && obj.getValue() != null && obj.getValue().toString().indexOf(GIT_LAND_AVANZATO) != -1){
						permessi = GITLAND_AVANZATO;						
					}else if (obj != null && obj.getValue() != null && obj.getValue().toString().indexOf(GIT_LAND_ENTE) != -1){
						permessi = GITLAND_ENTE;						
					}

					session.setAttribute(GITLAND_PERMISSION, permessi);
					
				}
			}			
		}

		return permessi;
	}//-------------------------------------------------------------------------

	public GitLandSessionBeanRemote getGitLandService(){
		try{
		if(gitLandService==null)
			gitLandService=(GitLandSessionBeanRemote)ClientUtility.getEjbInterface("GitLand", "GitLand_EJB", "GitLandSessionBean");
		} catch (Exception e) {
			e.printStackTrace();
			addError("general", "dettaglio.error");
			logger.error(e.getMessage(), e);
		}
		return gitLandService;
	}//-------------------------------------------------------------------------
	
	public UserService getUserService(){
		try{
		if(userService==null)
			userService=(UserService)ClientUtility.getEjbInterface("AmProfiler", "AmProfilerEjb", "UserServiceBean");
		} catch (Exception e) {
			e.printStackTrace();
			addError("general", "dettaglio.error");
			logger.error(e.getMessage(), e);
		}
		return userService;
	}
	public String format(String s){
		String str = "";
		if(s!=null){
			s=s.trim();
			str=s.replaceAll("\n", "<br />");
		}
		return str;
	}//-------------------------------------------------------------------------
	
	public String getDatasource() {
		if (datasource == null) {
			try {
				Properties props = new Properties();
				String path = System.getProperty(NOME_PATH_JBOSS_CONF)
						+ "\\" + NOME_PROPERTIES;
				String newpath = "file:///" + path.replaceAll("\\\\", "/");
				URL url;
				url = new URL(newpath);
				props.load(url.openStream());
				String route = props.getProperty(getEnte());
				if (route == null)
					logger.debug("!Data Source " + getEnte()
							+ " Not Found");
				datasource = "java:/jdbc/Diogene_DS_" + route;

			} catch (MalformedURLException e) {
				logger.error(e);
			} catch (IOException e) {
				logger.error(e);
			}
		}
		return datasource;
	}//-------------------------------------------------------------------------
	
	public String encode(String stringToEncode) {
		String returnValue = "";
		BASE64Encoder encrypt = new BASE64Encoder();
		try {
			String codedString = encrypt.encode(stringToEncode.getBytes());
			returnValue = codedString;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return returnValue;
	}//-------------------------------------------------------------------------
	
	public static String removeLeadingZero(String str) {
		if (str==null || str.length() == 0)
			return str; 
		
		String retVal=new String(str);
		int i=0;
		while (i<str.length()) {
			if(str.charAt(i)=='0' && str.length() >i+1 ) {
				retVal=str.substring(i+1);
			}else
				break;
			i++;
		}
		return retVal;
	
	}//-------------------------------------------------------------------------	
	
	protected CatastoService getCatastoService(){
		try{
		if(catastoService==null)
			catastoService=(CatastoService) 
					ClientUtility.getEjbInterface("CT_Service", "CT_Service_Data_Access", "CatastoServiceBean");
		} catch (Exception e) {
			addError("general", "dettaglio.error");
			logger.error(e.getMessage(), e);
		}
		return catastoService;
	}//-------------------------------------------------------------------------
	
	public boolean hasPermission(String permission){
		HttpSession session = this.getSession();
		CeTUser user = (CeTUser) session.getAttribute("user");
		if (user != null) {
			HashMap<String, String> hmPerm = user.getPermList();
			Iterator itPerm = hmPerm.entrySet().iterator();
			while(itPerm.hasNext()){
				Map.Entry obj = (Map.Entry)itPerm.next();
				//permission@-@GitLand@-@GitLand@-@GitLand Amministratore
				//permission@-@GitLand@-@GitLand@-@GitLand Operatore
				if (obj != null && obj.getValue() != null && obj.getValue().toString().indexOf(permission) != -1){
					return true;
				}

				
			}
		}			
		return false;
	}
	
	public boolean isAvanzato() {
		return hasPermission(GitLandBaseBean.GIT_LAND_AVANZATO);
	}//-------------------------------------------------------------------------
	public boolean isManager() {
		return hasPermission(GitLandBaseBean.GIT_LAND_AMMINISTRATORE);
	}//-------------------------------------------------------------------------
	public boolean isOperatore() {
		return hasPermission(GitLandBaseBean.GIT_LAND_OPERATORE);
	}//-------------------------------------------------------------------------
	public boolean isEnte() {
		return hasPermission(GitLandBaseBean.GIT_LAND_ENTE);
	}//-------------------------------------------------------------------------
	
	protected Parametro getParametro(String chiave) {
		Parametro esito=null;
		String qry ="from Parametro p " +
				"where p.belfiore='" + getEnte() + "'  " +
				"and p.chiave='" + chiave+ "'  " ; 

		logger.info(qry);
		List lstParametri = gitLandService.getList(qry, true);
		if (lstParametri != null && lstParametri.size()>0){
			esito = (Parametro)lstParametri.get(0);
		}
		return esito;
	}
	 
	protected boolean setParametro(String chiave, String valore) {
		boolean esito=false;
		String qry ="from Parametro p " +
				"where p.belfiore='" + getEnte() + "'  " +
				"and p.chiave='" + chiave+ "'  " ; 
		try {
			logger.info(qry);
			Parametro par= new Parametro();
			par.setBelfiore(getEnte());
			par.setChiave(chiave);;
			List lstParametri = gitLandService.getList(qry, true);
			if (lstParametri != null && lstParametri.size()==1){
				par= (Parametro)lstParametri.get(0);
				par.setValore(valore);
				gitLandService.updItem(par);
			}else{
				par.setValore(valore);
				gitLandService.addItem(par);
			}
			
		} catch (Exception e) {
			logger.error(e);
		}
		return esito;
	}
	 
	protected PGTService getPgtService(){
         PGTService pgtService = null;
         try{
         if(pgtService==null)
                pgtService=(PGTService)ClientUtility.getEjbInterface("CT_Service", "CT_Service_Data_Access", "PGTServiceBean");
         } catch (Exception e) {
                addError("general", "dettaglio.error");
                logger.error(e.getMessage(), e);
         }
         return pgtService;
   }//-------------------------------------------------------------------------	

}
