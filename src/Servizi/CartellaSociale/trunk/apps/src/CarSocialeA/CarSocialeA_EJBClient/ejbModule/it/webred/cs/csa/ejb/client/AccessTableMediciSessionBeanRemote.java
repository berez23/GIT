package it.webred.cs.csa.ejb.client;

import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.data.model.CsCMedico;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface AccessTableMediciSessionBeanRemote {
	
	public List<CsCMedico> getMedici(CeTBaseObject cet);
    public CsCMedico findMedicoById(BaseDTO dto) throws Exception;

}
