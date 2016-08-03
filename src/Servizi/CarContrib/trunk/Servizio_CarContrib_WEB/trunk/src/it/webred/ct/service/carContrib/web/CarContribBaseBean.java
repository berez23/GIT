package it.webred.ct.service.carContrib.web;

import it.webred.ct.config.parameters.ParameterService;
import it.webred.ct.config.parameters.dto.AmKeyValueDTO;
import it.webred.ct.config.parameters.dto.ParameterSearchCriteria;
import it.webred.ct.data.access.basic.anagrafe.AnagrafeService;
import it.webred.ct.data.access.basic.bolliVeicoli.BolliVeicoliService;
import it.webred.ct.data.access.basic.common.CommonService;
import it.webred.ct.data.access.basic.fonti.FontiService;
import it.webred.ct.data.access.basic.imu.SaldoImuService;
import it.webred.ct.data.model.common.SitEnte;
import it.webred.ct.service.carContrib.data.access.carsociale.CarSocialeCarContribService;
import it.webred.ct.service.carContrib.data.access.catasto.CatastoCarContribService;
import it.webred.ct.service.carContrib.data.access.cc.CarContribService;
import it.webred.ct.service.carContrib.data.access.cc.FiltroRichiesteService;
import it.webred.ct.service.carContrib.data.access.cnc.CncCarContribService;
import it.webred.ct.service.carContrib.data.access.common.GeneralService;
import it.webred.ct.service.carContrib.data.access.common.dto.SoggettoDTO;
import it.webred.ct.service.carContrib.data.access.cosap.CosapCarContribService;
import it.webred.ct.service.carContrib.data.access.ici.IciServiceCarContrib;
import it.webred.ct.service.carContrib.data.access.locazioni.LocazioniCarContribService;
import it.webred.ct.service.carContrib.data.access.redditi.RedditiCarContribService;
import it.webred.ct.service.carContrib.data.access.tarsu.TarsuCarContribService;
import it.webred.ct.service.carContrib.web.pages.ContribuenteBean;

import java.io.File;
import java.io.FileInputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;


public class CarContribBaseBean {
	
	private static ResourceBundle bundle;
	
	static {
		bundle = ResourceBundle
				.getBundle("it.webred.ct.service.carContrib.web.resources.messages");
	}
	
	protected Logger logger = Logger.getLogger("carcontrib.log");
	
	public CarContribBaseBean ()
	{
		FacesContext ctx = FacesContext.getCurrentInstance();
		this.userBean =(UserBean)ctx.getApplication().getVariableResolver().resolveVariable(ctx, "userBean");
	}

	
	public Date dataRiferimento= new Date();
	
	private UserBean userBean; 
	private String titolo;
	
	//private static ResourceBundle bundle;
	private CarContribService carContribService;
	private FiltroRichiesteService filtroRichiesteService;
	private CatastoCarContribService catastoCarContribService;
	private RedditiCarContribService redditiCarContribService;
	private LocazioniCarContribService locazioniCarContribService;
	private GeneralService generalService;
	private AnagrafeService anagrafeService;
	private CommonService commonService;
	private IciServiceCarContrib iciServiceCarContrib;
	private TarsuCarContribService tarsuCarContribService;
	private CosapCarContribService cosapCarContribService;
	private CncCarContribService cncCarContribService;
	private CarSocialeCarContribService carSocialeCarContribService;
	private ParameterService parameterService;
	private FontiService fontiService;
	private SaldoImuService imuService;
	private BolliVeicoliService bolliVeicoliService;
	
	private SitEnte ente;
	
	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public void setCarContribService(CarContribService carContribService) {
		this.carContribService = carContribService;
	}

	public CarContribService getCarContribService() {
		return carContribService;
	}

	public void setFiltroRichiesteService(FiltroRichiesteService filtroRichiesteService) {
		this.filtroRichiesteService = filtroRichiesteService;
	}

	public FiltroRichiesteService getFiltroRichiesteService() {
		return filtroRichiesteService;
	}

	public void setCatastoCarContribService(CatastoCarContribService catastoCarContribService) {
		this.catastoCarContribService = catastoCarContribService;
	}

	public CatastoCarContribService getCatastoCarContribService() {
		return catastoCarContribService;
	}

	public void setRedditiCarContribService(RedditiCarContribService redditiCarContribService) {
		this.redditiCarContribService = redditiCarContribService;
	}

	public RedditiCarContribService getRedditiCarContribService() {
		return redditiCarContribService;
	}

	public void setLocazioniCarContribService(LocazioniCarContribService locazioniCarContribService) {
		this.locazioniCarContribService = locazioniCarContribService;
	}

	public LocazioniCarContribService getLocazioniCarContribService() {
		return locazioniCarContribService;
	}

	public void setGeneralService(GeneralService generalService) {
		this.generalService= generalService;
	}

	public GeneralService getGeneralService() {
		return generalService;
	}

	public void setAnagrafeService(AnagrafeService anagrafeService) {
		this.anagrafeService = anagrafeService;
	}

	public AnagrafeService getAnagrafeService() {
		return anagrafeService;
	}
	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setEnte(SitEnte ente) {
		this.ente = ente;
	}

	public SitEnte getEnte() {
		return ente;
	}

	public void setIciServiceCarContrib(IciServiceCarContrib iciServiceCarContrib) {
		this.iciServiceCarContrib = iciServiceCarContrib;
	}

	public IciServiceCarContrib getIciServiceCarContrib() {
		return iciServiceCarContrib;
	}
	
	public void setTarsuCarContribService(TarsuCarContribService tarsuCarContribService) {
		this.tarsuCarContribService = tarsuCarContribService;
	}

	public TarsuCarContribService getTarsuCarContribService() {
		return tarsuCarContribService;
	}

	public void setCosapCarContribService(CosapCarContribService cosapCarContribService) {
		this.cosapCarContribService = cosapCarContribService;
	}

	public CosapCarContribService getCosapCarContribService() {
		return cosapCarContribService;
	}

	public void setCncCarContribService(CncCarContribService cncCarContribService) {
		this.cncCarContribService = cncCarContribService;
	}

	public CncCarContribService getCncCarContribService() {
		return cncCarContribService;
	}

	public CarSocialeCarContribService getCarSocialeCarContribService() {
		return carSocialeCarContribService;
	}

	public void setCarSocialeCarContribService(CarSocialeCarContribService carSocialeCarContribService) {
		this.carSocialeCarContribService = carSocialeCarContribService;
	}

	public void setParameterService(ParameterService parameterService) {
		this.parameterService = parameterService;
	}

	public ParameterService getParameterService() {
		return parameterService;
	}
	
	public FontiService getFontiService() {
		return fontiService; 
	}

	public void setFontiService(FontiService fontiService) {
		this.fontiService = fontiService;
	}

	public String getMessage(String messageKey, Object param) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		String txt = bundle.getString(messageKey);
		
		if (param != null) {
			Object[] params = {param};
			txt = MessageFormat.format(txt, params);
		}
		return txt;
	}
	
	private void addMessage(String messageKey, FacesMessage.Severity severity,
			String details, Object param) {
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		String txt = getMessage(messageKey, param);
		
		facesContext.addMessage(null, new FacesMessage(severity, txt + " "+details,
				details == null ? " " : details));

	}
	
	public void addInfoMessage(String messageKey) {
		addMessage(messageKey, FacesMessage.SEVERITY_INFO, null, null);
	}
	
	public void addInfoMessage(String messageKey, Object param) {
		addMessage(messageKey, FacesMessage.SEVERITY_INFO, null, param);
	}
	

	public void addErrorMessage(String messageKey, String details) {
		addMessage(messageKey, FacesMessage.SEVERITY_ERROR, details, null);
	}
	
	public void LoadSoggettoInContribuenteBean(SoggettoDTO sogg, Long idRichiesta, String mezzoRisposta,boolean returnToList,Utility.TipoBeanPadre padre, boolean preparaCartella, boolean includiStorico)
	{
		FacesContext ctx = FacesContext.getCurrentInstance();
		
		sogg.setEnteId(userBean.getEnteID());
		sogg.setUserId(userBean.getUsername());

		ContribuenteBean contrib =(ContribuenteBean)ctx.getApplication().getVariableResolver().resolveVariable(ctx, "contribuenteBean");
		contrib.resetBean();
		contrib.setSoggettoCartella(sogg);
		contrib.setIdRichiestaCartella(idRichiesta);
		contrib.setTipoAccessoCartella(mezzoRisposta);
		contrib.setReturnToList(returnToList);
		contrib.setShowButtonProduciPdf(true);
		contrib.setShowButtonProduciPdfPerNucleo(true);
		contrib.setViewPDF(false);
		contrib.setSelectedTab("ajaxZero");
		
		contrib.setPadre(padre.getTipoBeanPadre());
		contrib.setVisStoricoCatasto(includiStorico);

		contrib.LoadCartellaContribuente(preparaCartella);
	}
	
	public static String getValueProperties(String key)
	{
		try
		{
			String pathProject = new CarContribBaseBean().getPathProject();
			
			File fileProp = new File(pathProject + "/resources.properties");
			
			Properties properties = new Properties();
			properties.load(new FileInputStream(fileProp.getAbsolutePath()));
			
			return properties.getProperty(key);
		}
		catch(Exception ex)
		{
			return "";
		}
	}

	private String getPathProject()
	{
		return getClass().getProtectionDomain().getCodeSource().getLocation().toString().substring(6);
	}

	public String getHostFtpPratiche() {
		
		if (userBean!=null && userBean.getEnteID()!=null)
		{
			ParameterSearchCriteria criteria = new ParameterSearchCriteria();
			criteria.setKey("ftp.host.scambio.portale");
			criteria.setComune(userBean.getEnteID());
			criteria.setSection(null);
			
			return this.doGetListaKeyValue(criteria);
		}
		else
			return "";
	}	

	public String getMailServer() {
		
		if (userBean!=null && userBean.getEnteID()!=null)
		{
			ParameterSearchCriteria criteria = new ParameterSearchCriteria();
			criteria.setKey("mail.server");
			criteria.setComune(userBean.getEnteID());
			criteria.setSection(null);
			
			return this.doGetListaKeyValue(criteria);
		}
		else
			return "";
	}
	
	public String getPortMailServer() {
		
		if (userBean!=null && userBean.getEnteID()!=null)
		{
			ParameterSearchCriteria criteria = new ParameterSearchCriteria();
			criteria.setKey("mail.server.port");
			criteria.setComune(userBean.getEnteID());
			criteria.setSection(null);
			
			return this.doGetListaKeyValue(criteria);
		}
		else
			return "";
	}

	public String getPathFilesPdf() {
		
		if (userBean!=null && userBean.getEnteID()!=null)
		{		
			ParameterSearchCriteria criteria = new ParameterSearchCriteria();
			criteria.setKey("dir.files.datiDiogene");
			criteria.setComune(null);
			criteria.setSection(null);
			
			return doGetListaKeyValue(criteria) + File.separator + userBean.getEnteID().trim() + File.separator + getValueProperties("NAME_DIR_PDF");
		}
		else
			return "";
		
	}
	
	public String getEmailFrom() {
		
		if (userBean!=null && userBean.getEnteID()!=null)
		{		
			ParameterSearchCriteria criteria = new ParameterSearchCriteria();
			criteria.setKey("email.cartellaC");
			criteria.setComune(userBean.getEnteID());
			criteria.setSection("param.cartellaC");
			
			return this.doGetListaKeyValue(criteria);
		}
		else
			return "";
	}

	public String getProvenienzaDatiIci() {
		
		if (userBean!=null && userBean.getEnteID()!=null)
		{		
			ParameterSearchCriteria criteria = new ParameterSearchCriteria();
			criteria.setKey("provenienza.dati.ici");
			criteria.setComune(userBean.getEnteID());
			criteria.setSection("param.comune");
			
			return this.doGetListaKeyValue(criteria);
		}
		else
			return "";
	}

	public String getProvenienzaDatiTarsu() {

		if (userBean!=null && userBean.getEnteID()!=null)
		{		
			ParameterSearchCriteria criteria = new ParameterSearchCriteria();
			criteria.setKey("provenienza.dati.tarsu");
			criteria.setComune(userBean.getEnteID());
			criteria.setSection("param.comune");
			
			return this.doGetListaKeyValue(criteria);
		}
		else
			return "";
	}

	public String getAbilitaIndiceCorrelazione() {
		
		if (userBean!=null && userBean.getEnteID()!=null)
		{
			ParameterSearchCriteria criteria = new ParameterSearchCriteria();
			criteria.setKey("abilita.query.su.indice");
			criteria.setComune(userBean.getEnteID());
			criteria.setSection("param.cartellaC");
			
			return this.doGetListaKeyValue(criteria);
		}
		else
			return "";
	}
	
	protected List<String> getPdfFontiDefault() {
		
		List<String> lst = new ArrayList<String>();
		try
		{
			ParameterSearchCriteria criteria = new ParameterSearchCriteria();
			criteria.setKey("pdf.fonti.default");
			criteria.setComune(userBean.getEnteID());
			criteria.setSection("param.cartellaC");
	
			String value = this.doGetListaKeyValue(criteria);
			
			logger.debug("pdf.fonti.default="+value);
			if(value!=null){
				value = value.toUpperCase();
				lst = Arrays.asList(value.split(";"));
			}
			
		}
		catch(Exception e) {
			logger.info("Eccezione config: "+e.getMessage());
		}
		return lst;
	}
	
	
	public String getHostFtpScambioPortale() {
		
		String value = null;
		try
		{
			ParameterSearchCriteria criteria = new ParameterSearchCriteria();
			criteria.setKey("ftp.host.scambio.portale");
			criteria.setComune(userBean.getEnteID());
	
			it.webred.ct.config.model.AmKeyValueExt param = parameterService.getAmKeyValueExt(criteria);
			value= param.getValueConf();
		}
		catch(Exception e) {
			logger.info("Eccezione config: "+e.getMessage());
		}
		return value;
	}
	
	public String getDirFtpScambioPortale() {
		
		String value = null;
		try
		{
			ParameterSearchCriteria criteria = new ParameterSearchCriteria();
			criteria.setKey("ftp.pathcc.scambio.portale");
			criteria.setComune(userBean.getEnteID());
	
			it.webred.ct.config.model.AmKeyValueExt param = parameterService.getAmKeyValueExt(criteria);
			value= param.getValueConf() + "/export";
		}
		catch(Exception e) {
			logger.info("Eccezione config: "+e.getMessage());
		}
		return value;
	}
	
	public String getUserFtpScambioPortale() {
		
		String value = null;
		try
		{
			ParameterSearchCriteria criteria = new ParameterSearchCriteria();
			criteria.setKey("ftp.user.scambio.portale");
			criteria.setComune(userBean.getEnteID());
	
			it.webred.ct.config.model.AmKeyValueExt param = parameterService.getAmKeyValueExt(criteria);
			value= param.getValueConf();
		}
		catch(Exception e) {
			logger.info("Eccezione config: "+e.getMessage());
		}
		return value;
	}
	
	public String getPwdFtpScambioPortale() {
		
		String value = null;
		try
		{
			ParameterSearchCriteria criteria = new ParameterSearchCriteria();
			criteria.setKey("ftp.pwd.scambio.portale");
			criteria.setComune(userBean.getEnteID());
	
			it.webred.ct.config.model.AmKeyValueExt param = parameterService.getAmKeyValueExt(criteria);
			value= param.getValueConf();
		}
		catch(Exception e) {
			logger.info("Eccezione config: "+e.getMessage());
		}
		return value;
	}

	

	
	public String doGetListaKeyValue(ParameterSearchCriteria criteria)
	{
		try
		{
			if (parameterService==null)
			{
				logger.info(" parameterService == NULL");
				return "";
			}
			
			List<AmKeyValueDTO> l = parameterService.getListaKeyValue(criteria);
			if (l!=null && l.size()>0)
				return ((AmKeyValueDTO)l.get(0)).getAmKeyValueExt().getValueConf();
			else
			{
				logger.info(" LISTA RITORNO DA parameterService.getListaKeyValue VUOTA");
				return "";
			}
		}
		catch (Exception ex)
		{
			logger.info(" doGetListaKeyValue ERRORE = " + ex.getMessage());
			return "";
		}
	}

	public SaldoImuService getImuService() {
		return imuService;
	}

	public void setImuService(SaldoImuService imuService) {
		this.imuService = imuService;
	}

	public BolliVeicoliService getBolliVeicoliService() {
		return bolliVeicoliService;
	}

	public void setBolliVeicoliService(BolliVeicoliService bolliVeicoliService) {
		this.bolliVeicoliService = bolliVeicoliService;
	}

	
	

}
