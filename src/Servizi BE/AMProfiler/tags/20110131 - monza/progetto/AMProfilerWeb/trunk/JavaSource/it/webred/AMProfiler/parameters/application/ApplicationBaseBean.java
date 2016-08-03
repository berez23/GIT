package it.webred.AMProfiler.parameters.application;

import it.webred.AMProfiler.parameters.AmProfilerBaseBean;
import it.webred.ct.config.parameters.application.ApplicationService;

import javax.ejb.EJB;

public class ApplicationBaseBean extends AmProfilerBaseBean{

	@EJB(mappedName = "CT_Service/ApplicationServiceBean/remote")
	protected ApplicationService applicationService;

}
