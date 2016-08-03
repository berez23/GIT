package it.webred.ct.service.carContrib.data.access.cc;

import java.io.Serializable;
import java.util.List;

import it.webred.ct.service.carContrib.data.access.cc.dto.FiltroRichiesteDTO;
import it.webred.ct.service.carContrib.data.access.cc.dto.FiltroRichiesteSearchCriteria;

import javax.ejb.Remote;

@Remote
public interface FiltroRichiesteService extends Serializable{
	
	public List<FiltroRichiesteDTO> filtroRichieste(FiltroRichiesteSearchCriteria filtro,int start, int numberRecord);
	
	public Long getRecordCount(FiltroRichiesteSearchCriteria filtro);

}
