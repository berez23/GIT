package it.webred.cs.csa.ejb.ejb;

import it.webred.cs.csa.ejb.CarSocialeBaseSessionBean;
import it.webred.cs.csa.ejb.client.AccessTableSoggettoSessionBeanRemote;
import it.webred.cs.csa.ejb.dao.SoggettoDAO;
import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.csa.ejb.dto.CasoSearchCriteria;
import it.webred.cs.csa.ejb.dto.PaginationDTO;
import it.webred.cs.data.model.CsAAnagrafica;
import it.webred.cs.data.model.CsASoggetto;
import it.webred.cs.data.model.CsASoggettoCategoriaSoc;
import it.webred.cs.data.model.CsASoggettoMedico;
import it.webred.cs.data.model.CsASoggettoStatoCivile;
import it.webred.cs.data.model.CsASoggettoStatus;
import it.webred.cs.data.model.CsCCategoriaSociale;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Session Bean implementation class AccessTableDataMediciSessionBean
 */
@Stateless
public class AccessTableSoggettoSessionBean extends CarSocialeBaseSessionBean implements AccessTableSoggettoSessionBeanRemote {

	@Autowired
	private SoggettoDAO soggettoDAO;

	@Override
	public CsASoggetto getSoggettoById(BaseDTO dto) {
		return soggettoDAO.getSoggettoById((Long) dto.getObj());
	}
    
	@Override
	public CsASoggetto getSoggettiByCF(BaseDTO dto) {
		return soggettoDAO.getSoggettiByCF((String) dto.getObj());
	}
    
	@Override
	public List<CsASoggetto> getSoggettiByDenominazione(BaseDTO dto) {
		return soggettoDAO.getSoggettiByDenominazione((String) dto.getObj());
	}
	
	@Override
	public CsASoggetto saveSoggetto(BaseDTO dto) {
		CsASoggetto cs = (CsASoggetto) dto.getObj();
		CsAAnagrafica csAna = soggettoDAO.saveAnagrafica(cs.getCsAAnagrafica());
		cs.setAnagraficaId(csAna.getId());
		soggettoDAO.saveSoggetto(cs);
		return cs;
	}
	
	@Override
	public void updateSoggetto(BaseDTO dto) {
		CsASoggetto cs = (CsASoggetto) dto.getObj();
		soggettoDAO.updateAnagrafica(cs.getCsAAnagrafica());
		soggettoDAO.updateSoggetto(cs);
	}

	@Override
	public List<CsASoggettoCategoriaSoc> getSoggettoCategorieBySoggetto(BaseDTO dto) {
		return soggettoDAO.getSoggettoCategorieBySoggetto((Long) dto.getObj());
	}
	
	@Override
	public void saveSoggettoCategoria(BaseDTO dto) {
		soggettoDAO.saveSoggettoCategoria((CsASoggettoCategoriaSoc) dto.getObj());
	}
	
	@Override
	public void eliminaSoggettoCategorieBySoggetto(BaseDTO dto) {
		soggettoDAO.eliminaSoggettoCategorieBySoggetto((Long) dto.getObj());
	}
	
	@Override
	public CsCCategoriaSociale getCatSocAttualeBySoggetto(BaseDTO dto) {
		List<CsASoggettoCategoriaSoc> lst = soggettoDAO.getSoggettoCategorieBySoggetto((Long) dto.getObj());
		boolean trovato = false;
		int i = 0;
		CsCCategoriaSociale attuale = null;
		while (i<lst.size() && !trovato){
			CsASoggettoCategoriaSoc cs = lst.get(i);
			if(cs.getId().getDataFineApp().compareTo(new Date())>=0){
				trovato=true;
				attuale = cs.getCsCCategoriaSociale();
			}
			i++;
		} 
		
		return attuale;
	}

	@Override
	public List<CsASoggetto> getCasiSoggetto(PaginationDTO dto) {
		CasoSearchCriteria criteria = (CasoSearchCriteria) dto.getObj();
		criteria.setUsername(dto.getUserId());
		return soggettoDAO.getCasiSoggetto(dto.getFirst(), dto.getPageSize(), criteria);
	}
	
	@Override
	public Integer getCasiSoggettoCount(PaginationDTO dto) {
		CasoSearchCriteria criteria = (CasoSearchCriteria) dto.getObj();
		criteria.setUsername(dto.getUserId());
		return soggettoDAO.getCasiSoggettoCount((CasoSearchCriteria) dto.getObj());
	}
	
	@Override
	public Integer getCasiPerCategoriaCount(PaginationDTO dto) {
		CasoSearchCriteria criteria = (CasoSearchCriteria) dto.getObj();
		criteria.setUsername(dto.getUserId());
		return soggettoDAO.getCasiPerCategoriaCount((CasoSearchCriteria) dto.getObj());
	}
	
	@Override
	public List<CsASoggettoMedico> getSoggettoMedicoBySoggetto(BaseDTO dto) {
		return soggettoDAO.getSoggettoMedicoBySoggetto((Long) dto.getObj());
	}
	
	@Override
	public void saveSoggettoMedico(BaseDTO dto) {
		soggettoDAO.saveSoggettoMedico((CsASoggettoMedico) dto.getObj());
	}
	
	@Override
	public void updateSoggettoMedico(BaseDTO dto) {
		soggettoDAO.updateSoggettoMedico((CsASoggettoMedico) dto.getObj());
	}
	
	@Override
	public void eliminaSoggettoMedicoBySoggetto(BaseDTO dto) {
		soggettoDAO.eliminaSoggettoMedicoBySoggetto((Long) dto.getObj());
	}
	
	@Override
	public List<CsASoggettoStatoCivile> getSoggettoStatoCivileBySoggetto(BaseDTO dto) {
		return soggettoDAO.getSoggettoStatoCivileBySoggetto((Long) dto.getObj());
	}
	
	@Override
	public void saveSoggettoStatoCivile(BaseDTO dto) {
		soggettoDAO.saveSoggettoStatoCivile((CsASoggettoStatoCivile) dto.getObj());
	}
	
	@Override
	public void updateSoggettoStatoCivile(BaseDTO dto) {
		soggettoDAO.updateSoggettoStatoCivile((CsASoggettoStatoCivile) dto.getObj());
	}
	
	@Override
	public void eliminaSoggettoStatoCivileBySoggetto(BaseDTO dto) {
		soggettoDAO.eliminaSoggettoStatoCivileBySoggetto((Long) dto.getObj());
	}
	
	@Override
	public List<CsASoggettoStatus> getSoggettoStatusBySoggetto(BaseDTO dto) {
		return soggettoDAO.getSoggettoStatusBySoggetto((Long) dto.getObj());
	}
	
	@Override
	public void saveSoggettoStatus(BaseDTO dto) {
		soggettoDAO.saveSoggettoStatus((CsASoggettoStatus) dto.getObj());
	}
	
	@Override
	public void updateSoggettoStatus(BaseDTO dto) {
		soggettoDAO.updateSoggettoStatus((CsASoggettoStatus) dto.getObj());
	}
	
	@Override
	public void eliminaSoggettoStatusBySoggetto(BaseDTO dto) {
		soggettoDAO.eliminaSoggettoStatusBySoggetto((Long) dto.getObj());
	}
	
	@Override
	public CsAAnagrafica saveAnagrafica(BaseDTO dto) {
		return soggettoDAO.saveAnagrafica((CsAAnagrafica) dto.getObj());
	}

	
	@Override
	public CsAAnagrafica updateAnagrafica(BaseDTO dto) {
		return soggettoDAO.updateAnagrafica((CsAAnagrafica) dto.getObj());
	}
}
