package it.webred.AMProfiler.parameters.globale;

import it.webred.AMProfiler.parameters.AmProfilerBaseBean;
import it.webred.ct.config.parameters.ParameterService;

import javax.ejb.EJB;

public class GlobaleBaseBean extends AmProfilerBaseBean{

	@EJB(mappedName = "CT_Service/ParameterBaseService/remote")
	protected ParameterService amprofilerService;
	
}
