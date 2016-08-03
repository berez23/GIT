package it.webred.ct.service.carContrib.data.access.anagrafe;

import it.webred.ct.service.carContrib.data.access.common.dto.RicercaDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.SoggettoDTO;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

@Remote
public interface AnagrafeCarContribServiceModel {
	//anagrafe
	public List<Object>  getSoggettiCorrelatiAnagrafe(RicercaDTO dati);
		
}
