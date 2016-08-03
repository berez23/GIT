package it.webred.cs.csa.ejb.ejb;

import it.webred.cs.csa.ejb.CarSocialeBaseSessionBean;
import it.webred.cs.csa.ejb.client.AccessTableOperatoreSessionBeanRemote;
import it.webred.cs.csa.ejb.dao.OperatoreDAO;
import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.csa.ejb.dto.OperatoreDTO;
import it.webred.cs.data.model.CsOOperatore;
import it.webred.cs.data.model.CsOOperatoreSettore;
import it.webred.cs.data.model.CsOOperatoreTipoOperatore;
import it.webred.cs.data.model.CsOOrganizzazione;
import it.webred.cs.data.model.CsOSettore;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Andrea
 *
 */
@Stateless
public class AccessTableOperatoreSessionBean extends CarSocialeBaseSessionBean implements AccessTableOperatoreSessionBeanRemote {

	@Autowired
	private OperatoreDAO operatoreDao;
	
	@Override
	public CsOOperatoreSettore findOperatoreSettoreById(OperatoreDTO dto) throws Exception {
		
		CsOOperatoreSettore operSett = operatoreDao.findOperatoreSettoreById(dto.getIdOperatore(), dto.getIdSettore(), dto.getDate());
		return operSett;
	}
	
	@Override
	public List<CsOOperatoreSettore> findOperatoreSettoreByOperatore(OperatoreDTO dto) throws Exception {
		
		List<CsOOperatoreSettore> operSetts = operatoreDao.findOperatoreSettoreByOperatore(dto.getIdOperatore(), dto.getDate());
		return operSetts;
	}

	@Override
	public CsOSettore findSettoreById(OperatoreDTO dto) throws Exception {
		
		CsOSettore settore = operatoreDao.findSettoreById(dto.getIdSettore());
		return settore;
	}

	@Override
	public CsOOrganizzazione findOrganizzazioneById(OperatoreDTO dto) throws Exception {
		
		CsOOrganizzazione ente = operatoreDao.findOrganizzazioneById(dto.getIdOrganizzazione());
		return ente;
	}

	@Override
	public CsOOperatore findOperatoreByUsername(OperatoreDTO dto) throws Exception {
		CsOOperatore operatore = operatoreDao.findOperatoreByUsername(dto.getUsername());
		return operatore;
	}

	@Override
	public List<CsOOperatoreSettore> findOperatoreSettoreBySettore(OperatoreDTO dto) throws Exception {
		List<CsOOperatoreSettore> opSett = operatoreDao.findOperatoreSettoreBySettore(dto.getIdSettore());
		return opSett;
	}

	@Override
	public CsOOperatore findOperatoreById(OperatoreDTO dto) throws Exception {
		CsOOperatore operatore = operatoreDao.findOperatoreById(dto.getIdOperatore()); 
		return operatore;
	}
	
	@Override
	public List<CsOOperatore> getOperatoriAll(CeTBaseObject cet) throws Exception {
    	return operatoreDao.getOperatoriAll();
    }
	
	@Override
	public List<CsOOperatoreTipoOperatore> getOperatoriByTipoId(BaseDTO dto) throws Exception {
    	return operatoreDao.getOperatoriByTipoId((Long) dto.getObj());
    }
	
	@Override
	public List<CsOOperatore> getOperatoriByTipoDescrizione(BaseDTO dto) throws Exception {
    	return operatoreDao.getOperatoriByTipoDescrizione((String) dto.getObj());
    }
	
	@Override
	public CsOOperatoreTipoOperatore getTipoByOperatoreSettore(OperatoreDTO dto) throws Exception {
    	return operatoreDao.getTipoByOperatoreSettore(dto.getIdOperatoreSettore(), dto.getDate());
    }
	
	@Override
	public List<CsOOperatore> getOperatoriByCatSociale(BaseDTO dto) throws Exception {
		Long idCatSoc = (Long) dto.getObj();
		return operatoreDao.getOperatoriByCatSociale(idCatSoc);
    }
	
	@Override
	public List<CsOSettore> findSettoreByOrganizzazione(OperatoreDTO dto) throws Exception {
		List<CsOSettore> sett = operatoreDao.findSettoreByOrganizzazione(dto.getIdOrganizzazione());
		return sett;
	}

	@Override
	public List<CsOOperatoreSettore> findOperatoreSettori(OperatoreDTO dto) throws Exception {
		List<CsOOperatoreSettore> opSett = operatoreDao.findOperatoreSettori();
		return opSett;
	}

	@Override
	public List<CsOSettore> findSettori(OperatoreDTO dto) throws Exception {
		List<CsOSettore> sett = operatoreDao.findSettori();
		return sett;
	}

	@Override
	public CsOOperatoreTipoOperatore getOperatoreTipoOpById(BaseDTO dto) throws Exception {
		return operatoreDao.findCsOOperatoreTipoOpById((Long)dto.getObj());
	}
	
	@Override
	public CsOOperatore insertOrUpdateOperatore(CsOOperatore op, boolean update) throws Exception {
		return operatoreDao.insertOrUpdateOperatore(op, update);
	}
	
	@Override
	public CsOOperatoreTipoOperatore insertOrUpdateTipoOperatore(CsOOperatoreTipoOperatore tipoOp, boolean update) throws Exception {
		return operatoreDao.insertOrUpdateTipoOperatore(tipoOp, update);
	}
	
	@Override
	public CsOOperatoreSettore insertOrUpdateOperatoreSettore(CsOOperatoreSettore opSet, boolean update) throws Exception {
		return operatoreDao.insertOrUpdateOperatoreSettore(opSet, update);
	}
	
	@Override
	public void deleteTipoOperatore(CsOOperatoreTipoOperatore tipoOp) throws Exception {
		operatoreDao.deleteTipoOperatore(tipoOp);
	}
	
	@Override
	public void deleteOperatoreSettore(CsOOperatoreSettore opSet) throws Exception {
		operatoreDao.deleteOperatoreSettore(opSet);
	}
	
}
