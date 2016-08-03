package it.webred.ct.service.ff.data.access.common;

import it.webred.ct.service.ff.data.access.FFServiceBaseBean;
import it.webred.ct.service.ff.data.access.FFServiceException;
import it.webred.ct.service.ff.data.access.common.dao.FFCommonDAO;
import it.webred.ct.service.ff.data.access.common.dto.CodiciTipoMezzoRispostaDTO;
import it.webred.ct.service.ff.data.access.dao.FFDAOException;
import it.webred.ct.service.ff.data.model.CodiciTipoMezzoRisposta;
import it.webred.ct.service.ff.data.model.FFTipoDoc;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Session Bean implementation class FFCommonServiceBean
 */
@Stateless
public class FFCommonServiceBean extends FFServiceBaseBean implements FFCommonService {

	@Autowired
	private FFCommonDAO ffCommonDAO;
	
	public List<FFTipoDoc> getTipoDoc(CeTBaseObject obj) {
		try {
			System.out.println("DAO ["+ffCommonDAO+"]");
			return ffCommonDAO.getTipoDoc();
		}
		catch(Exception t) {
			throw new FFServiceException(t);
		}
	}
	
	public List<String> getDistinctUserName(CeTBaseObject obj) 
	{
		try {
			System.out.println("DAO ["+ffCommonDAO+"]");
			return ffCommonDAO.getDistinctUserName(obj);
		}
		catch(Exception t) {
			throw new FFServiceException(t);
		}
	}
	
	public List<CodiciTipoMezzoRisposta> getListaCodiciRisp(CeTBaseObject obj)
	{
		try {
			
			return ffCommonDAO.getListaCodiciRisp(obj);
			
		} catch (FFDAOException e) {
			throw new FFServiceException(e);
		}
	}
	
	public CodiciTipoMezzoRisposta getDescCodiciRisp(CodiciTipoMezzoRispostaDTO codiceDTO)
	{
		try
		{
			return ffCommonDAO.getDescCodiciRisp(codiceDTO);
		}
		catch (FFDAOException e) {
			logger.error("SI E' VERIFICATO UN ERRORE: "+e.getMessage(), e);
			return null;
		}catch (Exception e) {
			logger.error("SI E' VERIFICATO UN ERRORE: "+e.getMessage(), e);
			return null;
		}
	}
  
}
