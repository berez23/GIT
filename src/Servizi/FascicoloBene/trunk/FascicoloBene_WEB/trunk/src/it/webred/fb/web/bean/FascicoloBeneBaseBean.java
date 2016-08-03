package it.webred.fb.web.bean;

import it.webred.cet.permission.CeTUser;
import it.webred.ct.config.parameters.application.ApplicationService;
import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ejb.utility.ClientUtility;
import it.webred.fb.data.DataModelCostanti.Segnalibri.TipoAlert;
import it.webred.fb.data.model.DmBBene;
import it.webred.fb.ejb.client.CaricatoreSessionBeanRemote;
import it.webred.fb.ejb.client.DettaglioBeneSessionBeanRemote;
import it.webred.fb.ejb.dto.Alert;
import it.webred.fb.ejb.dto.locazione.DatiCatastali;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import sun.misc.BASE64Encoder;

public class FascicoloBeneBaseBean extends ComponentBaseBean{
	
	private String datasource;
	protected SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
	public static final String NOME_PROPERTIES = "datarouter.properties";
	public static final String NOME_PATH_JBOSS_CONF = "jboss.server.config.dir";
	
	protected DmBBene bene;
	private String urlBaseFasFabb = "";
	
 	private DettaglioBeneSessionBeanRemote beneService;
 	private CaricatoreSessionBeanRemote caricatoreService;
 	private CatastoService catastoService;
 	 
	public void initialize(DmBBene beneGlobal) {
		
		try{
			bene = beneGlobal;
			initializeData();
			
		}catch(Exception e){
			addErrorFromProperties("caricamento.error");
			logger.error(e.getMessage(),e);
		}

	}

	protected void initializeData() {
		
	}
	
	public Object getBeanReference(String beanName) {
		FacesContext context = FacesContext.getCurrentInstance();
		Application a = context.getApplication();
		Object o = a.getVariableResolver().resolveVariable(context, beanName);
		return o;
	}
	
	public String getEnte() {
		HttpSession session = this.getSession();
		CeTUser user = (CeTUser) session.getAttribute("user");
		if (user != null) {
			return user.getCurrentEnte();
		}
		return null;
	}

	protected DettaglioBeneSessionBeanRemote getDettaglioBeneService(){
		try{
		if(beneService==null)
			beneService=(DettaglioBeneSessionBeanRemote) 
					ClientUtility.getEjbInterface("FascicoloBene", "FascicoloBene_EJB", "DettaglioBeneSessionBean");
		} catch (Exception e) {
			addError("general", "dettaglio.error");
			logger.error(e.getMessage(), e);
		}
		return beneService;
	}
	
	protected CaricatoreSessionBeanRemote getCaricatoreBeneService(){
		try{
		if(caricatoreService==null)
			caricatoreService=(CaricatoreSessionBeanRemote) 
					ClientUtility.getEjbInterface("FascicoloBene", "FascicoloBene_EJB", "CaricatoreSessionBean");
		} catch (Exception e) {
			addError("general", "dettaglio.error");
			logger.error(e.getMessage(), e);
		}
		return caricatoreService;
	}
	
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
	}
	
	public DmBBene getBene() {
		return bene;
	}

	public void setBene(DmBBene bene) {
		this.bene = bene;
	}
	
	public String format(String s){
		String str = "";
		if(s!=null){
			s=s.trim();
			str=s.replaceAll("\n", "<br />");
		}
		return str;
	}
	
	
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
	}
	
	public String getUrlBaseFascFabb() throws SQLException{
		
		if(this.urlBaseFasFabb==null || this.urlBaseFasFabb.equals("") || this.urlBaseFasFabb.startsWith("ERR")){
			try{
				ApplicationService appSer = (ApplicationService) getEjb("CT_Service", "CT_Config_Manager", "ApplicationServiceBean");
				String utente = this.getUser().getUsername();
				String ente = this.getEnte();
				
				urlBaseFasFabb = appSer.getUrlApplication(utente, this.getEnte(), "FascFabb");
				urlBaseFasFabb+="/jsp/protected/richieste/richieste.faces?es=" + encode(ente);
				
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				this.addError("Errore Recupero Url Fascicolo Fabbricato", e.getMessage());
			}
		}
		return this.urlBaseFasFabb;	
	}

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
	}

	
	public String getUrlFascicoloFabb(String fkParCatastali){
		
		String codNazionale = fkParCatastali.substring(0,4);
		
		//Recuperare sezione da codNazionale
		String sezione = "";
		String foglio = fkParCatastali.substring(5,9);
		String particella = fkParCatastali.substring(9,14);
		
		return this.getUrlFasFabb(codNazionale,sezione, foglio, particella);
	}
	
	
	public String getUrlFasFabb(DatiCatastali dc) throws SQLException{
		boolean trovato = false;
		int i=0;
		while(i<dc.getDatoArricchito().getAlerts().size() && !trovato){
			Alert a = dc.getDatoArricchito().getAlerts().get(i);
			if(TipoAlert.VALIDATE.equals(a.getType()) ||
			   TipoAlert.IMM_VALIDATE.equalsIgnoreCase(a.getType()) ||
			   TipoAlert.TERR_VALIDATE.equalsIgnoreCase(a.getType()))
				trovato=true;
			i++;
		}
		
		if(trovato){
			String sezione=dc.getSezione().getDato();
			String comune = dc.getCodComune().getDato();
			String foglio = dc.getFoglio().getDato();
			String particella = dc.getMappale().getDato();
			return this.getUrlFasFabb(comune, sezione, foglio, particella);
		}else
			return null;
	}
	
	
	public String getUrlFasFabb(String comune, String sezione, String foglio, String particella) {
		String url = null;
	
		try{
			url = this.getUrlBaseFascFabb();
			if(url!=null && !"".equals(url)){
					if(comune.equalsIgnoreCase(this.getUser().getCurrentEnte())){
					if(foglio!=null && !"".equals(foglio) && particella!=null && !"".equals(particella)){
					
						foglio = removeLeadingZero(foglio);
						particella = removeLeadingZero(particella);
						
						url+="&SEZ="+sezione+"&FOGLIO="+foglio+"&PARTICELLA="+particella;
						logger.debug("Fascicolo Fabbricato URL:"+url);
						//url = sezione+"#"+foglio+"#"+particella+"#"+url;
					}else{
						logger.warn("Foglio e/o particella non definite", null);
						url=null;
					}
				}else{
					logger.warn("FOGLIO="+foglio+",PARTICELLA="+particella+" non appartente al comune corrente",null);
					url=null;
				}
			}else{
				this.addError("Accesso al Fascicolo Fabbricato non consentito all'utente "+this.getUser().getUsername(),null);
				url=null;
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			this.addError(e.getMessage(), "");
		}
		return url;	
	}
	
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
	
	}	
	
	
}
