package it.webred.cs.csa.ejb.ejb;

import it.webred.cs.csa.ejb.CarSocialeBaseSessionBean;
import it.webred.cs.csa.ejb.client.AccessTableMediciSessionBeanRemote;
import it.webred.cs.csa.ejb.dao.MedicoDAO;
import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.data.model.CsCMedico;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Session Bean implementation class AccessTableDataMediciSessionBean
 */
@Stateless
public class AccessTableMediciSessionBean extends CarSocialeBaseSessionBean implements AccessTableMediciSessionBeanRemote {

	@Autowired
	private MedicoDAO medicoDAO;
	
	/**
     * Default constructor. 
     */
    public AccessTableMediciSessionBean() {
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public List<CsCMedico> getMedici(CeTBaseObject cet) {
    	return medicoDAO.getMedici();
    }
    
    @Override
    public CsCMedico findMedicoById(BaseDTO dto) throws Exception {
    	return medicoDAO.findMedicoById((Long) dto.getObj());
    }

}
