package it.webred.ct.service.ff.data.access.common.dao;

import it.webred.ct.service.ff.data.access.common.dto.CodiciTipoMezzoRispostaDTO;
import it.webred.ct.service.ff.data.access.dao.FFBaseDAO;
import it.webred.ct.service.ff.data.access.dao.FFDAOException;
import it.webred.ct.service.ff.data.model.CodiciTipoMezzoRisposta;
import it.webred.ct.service.ff.data.model.FFTipoDoc;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class FFCommonJpaImpl extends FFBaseDAO implements FFCommonDAO {

	public List<FFTipoDoc> getTipoDoc()throws FFDAOException {
		try {
			System.out.println("Qui. Manager ["+manager+"]");
			Query q = manager.createNamedQuery("FF.getTipoDoc");
			return q.getResultList();
		}
		catch(Exception e) {
			throw new FFDAOException(e);
		}
	}
	
	public List<String> getDistinctUserName(CeTBaseObject obj) throws FFDAOException 
	{
			
		try{
			List<String> lista =new ArrayList<String> ();
			Query q= manager.createNamedQuery("FF.getDistinctUserNameGestRic");
			List<String> result = (List<String>)q.getResultList();
			if (result!=null && result.size()>0) {
				for (String ele : result)		{
					if (ele != null)
					lista.add((String)ele);
				}
			}
			return lista;
		}
		catch(Exception e) {
			throw new FFDAOException(e);
		} 
	}
	
	public List<CodiciTipoMezzoRisposta> getListaCodiciRisp(CeTBaseObject obj)
			throws FFDAOException {
		List<CodiciTipoMezzoRisposta> lista = null;
		logger.debug("FFJPAImpl.getListaCodiciRisp");
		try {
			Query q = manager
					.createNamedQuery("CodiciTipoMezzoRisposta.getListaCodiciRisp");
			lista = (List) q.getResultList();
		} catch (Throwable t) {
			logger.error("", t);
			throw new FFDAOException(t);
		}
		return lista;
	}
	
	public CodiciTipoMezzoRisposta getDescCodiciRisp(
			CodiciTipoMezzoRispostaDTO codiceDTO) throws FFDAOException {
		try {
			return manager.find(CodiciTipoMezzoRisposta.class, codiceDTO
					.getCodice().getCodice());
		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
			throw new FFDAOException(t);
		}
	}

}
