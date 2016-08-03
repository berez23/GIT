package it.webred.ct.service.spprof.data.access;

import it.webred.ct.service.spprof.data.access.dto.SpProfDTO;
import it.webred.ct.service.spprof.data.model.SSpQualifica;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface SpProfQualificaService {
	public List<SSpQualifica> getQualificaList(SpProfDTO qualDTO) ;
}
