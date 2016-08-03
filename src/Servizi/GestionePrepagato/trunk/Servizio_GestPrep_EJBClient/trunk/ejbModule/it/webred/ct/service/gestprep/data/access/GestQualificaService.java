package it.webred.ct.service.gestprep.data.access;
import it.webred.ct.service.gestprep.data.access.dto.GestPrepDTO;
import it.webred.ct.service.gestprep.data.model.GestPrepQualifica;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface GestQualificaService {

	public List<GestPrepQualifica> getQualificaList(GestPrepDTO qualDTO) ;

}
