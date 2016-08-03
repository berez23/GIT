package it.webred.ct.service.carContrib.data.access.anagrafe;

import java.util.ArrayList;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import it.webred.ct.service.carContrib.data.access.catasto.CatastoCarContribService;
import it.webred.ct.service.carContrib.data.access.common.CarContribServiceBaseBean;
import it.webred.ct.service.carContrib.data.access.common.GeneralService;
import it.webred.ct.service.carContrib.data.access.common.dto.RicercaDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.SoggettoDTO;
import it.webred.ct.service.carContrib.data.access.common.utility.Constants;

@Stateless
public class AnagrafeCarContribServiceBean extends CarContribServiceBaseBean implements AnagrafeCarContribLocalService, AnagrafeCarContribService{
	/*@EJB(mappedName = "CT_Service/AnagrafeServiceBean/remote")
	private AnagrafeService anagrafeService;
	*/
	@EJB
	private GeneralService generalService;
	
	public List<Object> getSoggettiCorrelatiAnagrafe(RicercaDTO dati) {
		return generalService.getSoggettiCorrelati(dati,generalService.getProgEs(dati), Constants.DEMOGRAFIA_ENTE_SORGENTE,Constants.DEMOGRAFIA_TIPO_INFO_PERSONA );
	
	}
}
