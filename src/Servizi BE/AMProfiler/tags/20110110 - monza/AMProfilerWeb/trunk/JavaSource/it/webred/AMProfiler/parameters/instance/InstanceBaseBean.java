package it.webred.AMProfiler.parameters.instance;

import it.webred.AMProfiler.parameters.AmProfilerBaseBean;
import it.webred.ct.config.parameters.application.ApplicationService;
import it.webred.ct.config.parameters.comune.ComuneService;

import javax.ejb.EJB;

public class InstanceBaseBean extends AmProfilerBaseBean{

	@EJB(mappedName = "CT_Service/ApplicationServiceBean/remote")
	protected ApplicationService applicationService;
	
	@EJB(mappedName = "CT_Service/ComuneServiceBean/remote")
	protected ComuneService comuneService;

}
