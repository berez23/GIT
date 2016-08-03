package it.webred.AMProfiler.parameters.fonte;

import it.webred.AMProfiler.parameters.AmProfilerBaseBean;
import it.webred.ct.config.parameters.fonte.FonteService;

import javax.ejb.EJB;

public class FonteBaseBean extends AmProfilerBaseBean{

	@EJB(mappedName = "CT_Service/FonteServiceBean/remote")
	protected FonteService fonteService;

}
