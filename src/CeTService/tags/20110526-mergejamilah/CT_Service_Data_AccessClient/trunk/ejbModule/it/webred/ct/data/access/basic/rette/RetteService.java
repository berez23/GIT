package it.webred.ct.data.access.basic.rette;

import it.webred.ct.data.model.rette.SitRttBollette;
import it.webred.ct.data.model.rette.SitRttDettBollette;
import it.webred.ct.data.model.rette.SitRttRateBollette;
import it.webred.ct.data.model.traffico.SitTrffMulte;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface RetteService{

	/** Recupera le bollette pagate collegate al codice fiscale
	 * @param il codice fiscale sarà settato su obj
	 * @return lista oggetti SitRttBollette
	 */
	public List<SitRttBollette> getListaBollettePagateByCodFis(RetteDataIn dataIn)
	throws RetteServiceException;
	
	/** Recupera le bollette non pagate collegate al codice fiscale
	 * @param il codice fiscale sarà settato su obj
	 * @return lista oggetti SitRttBollette
	 */
	public List<SitRttBollette> getListaBolletteNonPagateByCodFis(RetteDataIn dataIn)
	throws RetteServiceException;
	
	/** Recupera i dettagli bolletta 
	 * @param il cod bolletta sarà settato su obj
	 * @return lista oggetti SitRttDettBollette
	 */
	public List<SitRttDettBollette> getListaDettagliBollettaByCodBoll(RetteDataIn dataIn)
	throws RetteServiceException;
	
	/** Recupera le rate bolletta 
	 * @param il cod bolletta sarà settato su obj
	 * @return lista oggetti SitRttRateBollette
	 */
	public List<SitRttRateBollette> getListaRateBollettaByCodBoll(RetteDataIn dataIn)
	throws RetteServiceException;
}
