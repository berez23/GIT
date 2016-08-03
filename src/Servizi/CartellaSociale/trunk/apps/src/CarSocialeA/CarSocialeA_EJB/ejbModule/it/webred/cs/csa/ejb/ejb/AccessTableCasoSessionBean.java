package it.webred.cs.csa.ejb.ejb;

import it.webred.cs.csa.ejb.CarSocialeBaseSessionBean;
import it.webred.cs.csa.ejb.client.AccessTableCasoSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableOperatoreSessionBeanRemote;
import it.webred.cs.csa.ejb.dao.CasoDAO;
import it.webred.cs.csa.ejb.dao.SoggettoDAO;
import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.csa.ejb.dto.IterDTO;
import it.webred.cs.data.model.CsACaso;
import it.webred.cs.data.model.CsACasoOpeTipoOpe;
import it.webred.cs.data.model.CsASoggetto;
import it.webred.cs.data.model.CsOOperatoreTipoOperatore;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Andrea
 *
 */

@Stateless
public class AccessTableCasoSessionBean extends CarSocialeBaseSessionBean implements AccessTableCasoSessionBeanRemote  {
	
	@Autowired
	private CasoDAO casoDao;
	
	@Autowired
	private SoggettoDAO soggettoDao;
	
	@EJB
	public AccessTableOperatoreSessionBeanRemote operatoreSessionBean;
	
	@Override
	public CsACaso findCasoById(IterDTO dto) throws Exception {	
		
		CsACaso caso = casoDao.findCasoById( dto.getIdCaso() );
		
		CsASoggetto soggetto = soggettoDao.getSoggettoByCaso(caso);
		caso.setCsASoggetto(soggetto);
		
		return caso;
	}

	@Override
	public CsACaso newCaso(IterDTO dto) throws Exception {
		
		CsACaso newCaso = new CsACaso();
		
		newCaso.setUserIns(dto.getUserId());
		newCaso.setDtIns(new Date());
		newCaso.setUsrMod(null);
		newCaso.setDtMod(null);
		
		casoDao.createCaso(newCaso);
		
		return newCaso;
	}
	
	@Override
	public void updateCaso(BaseDTO dto) throws Exception {
		casoDao.updateCaso((CsACaso) dto.getObj());
	}

	@Override
	public List<CsACaso> findAll(IterDTO dto) {
		return casoDao.findAll();
	}

	@Override
	public void setDataModifica(IterDTO dto) throws Exception {
		casoDao.setDataModifica( dto.getIdCaso());
	}
	
	@Override
	public void salvaOperatoreCaso(BaseDTO dto) throws Exception {
		casoDao.salvaOperatoreTipoOpCaso((CsACasoOpeTipoOpe)dto.getObj());
	}
	
	@Override
	public void updateOperatoreCaso(BaseDTO dto) throws Exception {
		casoDao.updateOperatoreTipoOpCaso((CsACasoOpeTipoOpe)dto.getObj());
	}

	@Override
	public void eliminaOperatoreTipoOpByCasoId(BaseDTO dto) {
		casoDao.eliminaOperatoreTipoOpByCasoId((Long)dto.getObj());
		
	}
	
	@Override
	public List<CsACasoOpeTipoOpe> getListaOperatoreTipoOpByCasoId(BaseDTO dto) {
		return casoDao.getListaOperatoreTipoOpByCasoId(dto.getObj());
	}
	
	@Override
	public CsACasoOpeTipoOpe findResponsabile(BaseDTO dto) throws Exception {
		return casoDao.findResponsabile((Long) dto.getObj());
	}
	
	@Override
	public CsOOperatoreTipoOperatore findOperatoreTipoOperatoreByOpSettore(BaseDTO dto) throws Exception {
		return casoDao.findOperatoreTipoOperatoreByOpSettore((Long) dto.getObj(), (Long) dto.getObj2());
	}
	
	@Override
	public CsACasoOpeTipoOpe findCasoOpeTipoOpeByOpSettore(BaseDTO dto) throws Exception {
		return casoDao.findCasoOpeTipoOpeByOpSettore((Long) dto.getObj(), (Long) dto.getObj2());
	}
	
	@Override
	public Integer countCasiByResponsabileCatSociale(BaseDTO dto) throws Exception {
		return casoDao.countCasiByResponsabileCatSociale((Long) dto.getObj(), (Long) dto.getObj2());
	}
	
}
