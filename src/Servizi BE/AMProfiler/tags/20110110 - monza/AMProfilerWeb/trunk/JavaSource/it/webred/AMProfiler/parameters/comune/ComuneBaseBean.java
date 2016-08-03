package it.webred.AMProfiler.parameters.comune;

import it.webred.AMProfiler.parameters.AmProfilerBaseBean;
import it.webred.ct.config.parameters.comune.ComuneService;

import javax.ejb.EJB;

public class ComuneBaseBean extends AmProfilerBaseBean{

	@EJB(mappedName = "CT_Service/ComuneServiceBean/remote")
	protected ComuneService comuneService;

}
