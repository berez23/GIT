package it.webred.cs.csa.ejb.ejb;

import it.webred.cs.csa.ejb.CarSocialeBaseSessionBean;
import it.webred.cs.csa.ejb.client.AccessTableSchedaSegrSessionBeanRemote;
import it.webred.cs.csa.ejb.dao.SchedaSegrDAO;
import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.csa.ejb.dto.SchedaSegrDTO;
import it.webred.cs.data.DataModelCostanti;
import it.webred.cs.data.model.CsCCategoriaSociale;
import it.webred.cs.data.model.CsSsSchedaSegr;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Andrea
 *
 */

@Stateless
public class AccessTableSchedaSegrSessionBean extends CarSocialeBaseSessionBean implements AccessTableSchedaSegrSessionBeanRemote  {
	
	public static Logger logger = Logger.getLogger("carsociale.log");
	
	@Autowired
	private SchedaSegrDAO schedaSegrDao;
	
	@Override
	public CsSsSchedaSegr findSchedaSegrById(BaseDTO dto) {	
		
		return schedaSegrDao.findSchedaSegrById((Long) dto.getObj());
	}
	
	@Override
	public CsSsSchedaSegr findSchedaSegrByIdAnagrafica(BaseDTO dto) {	
		
		return schedaSegrDao.findSchedaSegrByIdAnagrafica((Long) dto.getObj());
	}

	@Override
	public List<CsSsSchedaSegr> getSchedeSegr(SchedaSegrDTO dto) {	
		
		return schedaSegrDao.getSchedeSegr(dto.getFirst(), dto.getPageSize(), dto.isOnlyNew(), dto.getIdSettore(), dto.getIdAnagrafica(), dto.getEnteId());
	}
	
	@Override
	public Integer getSchedeSegrCount(SchedaSegrDTO dto) {
		return schedaSegrDao.getSchedeSegrCount(dto.isOnlyNew(), dto.getIdSettore());
	}
	
	@Override
	public boolean saveSchedaSegr(SchedaSegrDTO dto) {
		try {
			
			//Inserimento
			if(DataModelCostanti.SchedaSegr.STATO_INS.equals(dto.getFlgStato())) {
				CsSsSchedaSegr scheda = new CsSsSchedaSegr();
				scheda.setId(dto.getId());
				scheda.setFlgStato(dto.getFlgStato());
				scheda.setUserIns(dto.getUserId());
				scheda.setDtIns(new Date());
				
				scheda.setCodEnte(dto.getEnteId());
				if(dto.getIdCatSociale() != null) {
					CsCCategoriaSociale catSoc = new CsCCategoriaSociale();
					catSoc.setId(dto.getIdCatSociale());
					scheda.setCsCCategoriaSociale(catSoc);
				}
				
				schedaSegrDao.saveSchedaSegr(scheda);
			}
			
			//Modifica
			if(DataModelCostanti.SchedaSegr.STATO_MOD.equals(dto.getFlgStato())) {
				CsSsSchedaSegr scheda = schedaSegrDao.findSchedaSegrById(dto.getId());
				scheda.setFlgStato(dto.getFlgStato());
				scheda.setUsrMod(dto.getUserId());
				scheda.setDtMod(new Date());
				
				schedaSegrDao.updateSchedaSegr(scheda);
			}
			
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}
	
	@Override
	public void updateSchedaSegr(BaseDTO dto) {
		schedaSegrDao.updateSchedaSegr((CsSsSchedaSegr) dto.getObj());
	}
}
