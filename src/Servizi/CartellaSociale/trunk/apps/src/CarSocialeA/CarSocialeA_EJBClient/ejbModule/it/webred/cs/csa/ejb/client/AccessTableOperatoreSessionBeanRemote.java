package it.webred.cs.csa.ejb.client;

import it.webred.cs.data.model.CsOOperatore;
import it.webred.cs.data.model.CsOOperatoreSettore;
import it.webred.cs.data.model.CsOOperatoreTipoOperatore;
import it.webred.cs.data.model.CsOOrganizzazione;
import it.webred.cs.data.model.CsOSettore;

import java.util.List;

import javax.ejb.Remote;

import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.csa.ejb.dto.OperatoreDTO;
import it.webred.ct.support.datarouter.CeTBaseObject;
/**
 * @author Andrea
 *
 */
@Remote
public interface AccessTableOperatoreSessionBeanRemote {
	
	public List<CsOSettore> findSettori(OperatoreDTO dto) throws Exception;
	
	public List<CsOOperatoreSettore> findOperatoreSettori(OperatoreDTO dto) throws Exception;

	public CsOOperatore findOperatoreById(OperatoreDTO dto) throws Exception;
	
	public CsOOperatore findOperatoreByUsername(OperatoreDTO dto) throws Exception;
	
	public CsOOperatoreSettore findOperatoreSettoreById(OperatoreDTO dto) throws Exception;
	
	public List<CsOOperatoreSettore> findOperatoreSettoreByOperatore(OperatoreDTO dto) throws Exception;
	
	public List<CsOOperatoreSettore> findOperatoreSettoreBySettore(OperatoreDTO dto) throws Exception;
	
	public CsOSettore findSettoreById(OperatoreDTO dto) throws Exception;
	
	public List<CsOSettore> findSettoreByOrganizzazione(OperatoreDTO dto) throws Exception;
	
	public CsOOrganizzazione findOrganizzazioneById(OperatoreDTO dto) throws Exception;
	
	public List<CsOOperatoreTipoOperatore> getOperatoriByTipoId(BaseDTO dto) throws Exception;
	
	public CsOOperatoreTipoOperatore getTipoByOperatoreSettore(OperatoreDTO dto) throws Exception;
	
	public List<CsOOperatore> getOperatoriByTipoDescrizione(BaseDTO dto) throws Exception;
	
	public CsOOperatoreTipoOperatore getOperatoreTipoOpById(BaseDTO dto) throws Exception;
	
	public List<CsOOperatore> getOperatoriByCatSociale(BaseDTO dto) throws Exception;
	
	public CsOOperatore insertOrUpdateOperatore(CsOOperatore op, boolean update) throws Exception;
	
	public CsOOperatoreTipoOperatore insertOrUpdateTipoOperatore(CsOOperatoreTipoOperatore tipoOp, boolean update) throws Exception;
	
	public CsOOperatoreSettore insertOrUpdateOperatoreSettore(CsOOperatoreSettore opSet, boolean update) throws Exception;
	
	public void deleteTipoOperatore(CsOOperatoreTipoOperatore tipoOp) throws Exception;
	
	public void deleteOperatoreSettore(CsOOperatoreSettore opSet) throws Exception;

	public List<CsOOperatore> getOperatoriAll(CeTBaseObject cet) throws Exception;
}
