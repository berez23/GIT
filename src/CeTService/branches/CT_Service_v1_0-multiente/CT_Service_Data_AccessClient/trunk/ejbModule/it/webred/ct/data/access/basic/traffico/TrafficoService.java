package it.webred.ct.data.access.basic.traffico;

import it.webred.ct.data.model.traffico.SitTrffMulte;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface TrafficoService{

	/** Recupera le multe collegate al codice fiscale
	 * @param il codice fiscale sarà settato su obj
	 * @return lista oggetti SitTrffMulte
	 */
	public List<SitTrffMulte> getListaMulteByCodFis(TrafficoDataIn dataIn)
	throws TrafficoServiceException;
}
