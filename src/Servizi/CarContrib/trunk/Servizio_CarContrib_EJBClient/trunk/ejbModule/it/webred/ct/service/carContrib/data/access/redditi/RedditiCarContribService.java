package it.webred.ct.service.carContrib.data.access.redditi;

import it.webred.ct.data.access.basic.redditi.dto.KeySoggDTO;
import it.webred.ct.data.access.basic.redditi.dto.RedditiDicDTO;
import it.webred.ct.data.model.redditi.RedDatiAnagrafici;
import it.webred.ct.data.model.redditi.RedDomicilioFiscale;
import it.webred.ct.service.carContrib.data.access.common.dto.RicercaDTO;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface RedditiCarContribService {
	public List<RedDatiAnagrafici>  searchSoggettiCorrelatiRedditi(RicercaDTO dati);
	public RedDatiAnagrafici getSoggettoByKey(KeySoggDTO key);
	public RedDomicilioFiscale getDomicilioByKey(KeySoggDTO key);
	public List<RedditiDicDTO> getRedditiByKey(KeySoggDTO key);
}
