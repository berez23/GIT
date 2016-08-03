package it.bod.application.environment;

import it.bod.application.beans.PrgBean;
import it.bod.application.beans.VincoloBean;
import it.bod.business.service.BodLogicService;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Ccontiele e carica una configurazione di base 
 * ed [ capace di leggere configurazioni personalizzate
 * per ente.
 * Viene registrata in MasterClass
 * @author MARCO
 *
 */
public class BaseEnvironment implements IEnvironment{


	private static Logger logger = Logger.getLogger("it.bod.application.beans.VincoloBean.BaseEnvironment");
	protected BodLogicService bodLogicService = null;
	private IEnvironment customEnv=null;

	public BaseEnvironment() {
	}


	public List<VincoloBean> getVincoli(String foglio, String particella, String codEnte) {
		List<VincoloBean> listaVincoli = new ArrayList<VincoloBean>();
		
		IEnvironment custom = getCustomEnv(codEnte);
		if (custom!=null) {
			return custom.getVincoli(foglio, particella, codEnte);
		}
		return listaVincoli;
		

	}

	public List<PrgBean> getPRG(String foglio, String particella, String codEnte) {
		List<PrgBean> listaprg = new ArrayList<PrgBean>();
		
		IEnvironment custom = getCustomEnv(codEnte);
		if (custom!=null) {
			return custom.getPRG(foglio, particella, codEnte);
		}
		return listaprg;
		

	}
	
	
	private IEnvironment getCustomEnv(String codEnte) {
		IEnvironment custom=null;
		try {
			Class<BaseEnvironment> cls = (Class<BaseEnvironment>) Class.forName("it.bod.application.environment.Environment"+codEnte);
			//Class<BaseEnvironment> cls = (Class<BaseEnvironment>) Class.forName("it.bod.application.environment.EnvironmentF205");
			try {
				Constructor<BaseEnvironment> ctor = cls.getConstructor();
				custom =  (BaseEnvironment) ctor.newInstance();
				((BaseEnvironment) custom).setBodLogicService(bodLogicService);
				
			} catch (InstantiationException e) {
				logger.error(e);
			} catch (IllegalAccessException e) {
				logger.error(e);
			} catch (SecurityException e) {
				logger.error(e);
			} catch (NoSuchMethodException e) {
				logger.error(e);
			} catch (IllegalArgumentException e) {
				logger.error(e);
			} catch (InvocationTargetException e) {
				logger.error(e);
			}
		} catch (ClassNotFoundException e) {
			 
		}
		return custom;
	}
	
	public BodLogicService getBodLogicService() {
		return bodLogicService;
	}

	public void setBodLogicService(BodLogicService bodLogicService) {
		this.bodLogicService = bodLogicService;
	}
	
}
