package it.webred.ct.service.tsSoggiorno.data.access.dao;

import it.webred.ct.service.tsSoggiorno.data.access.TsSoggiornoServiceException;
import it.webred.ct.service.tsSoggiorno.data.access.dto.RimbSanzDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.RimbSanzSearchCriteria;
import it.webred.ct.service.tsSoggiorno.data.model.IsRimborso;
import it.webred.ct.service.tsSoggiorno.data.model.IsSanzione;

import java.util.List;

public interface RimbSanzDAO {

	public List<RimbSanzDTO> getRimbSanzByCriteria(
			RimbSanzSearchCriteria criteria);
	
	public Long getRimbSanzCountByCriteria(RimbSanzSearchCriteria criteria);
	
	public void saveRimborso(IsRimborso r) throws TsSoggiornoServiceException;
	
	public void saveSanzione(IsSanzione s) throws TsSoggiornoServiceException;
	
	public void deleteRimborso(Long id) throws TsSoggiornoServiceException;
	
	public void deleteSanzione(Long id) throws TsSoggiornoServiceException;
	
}
