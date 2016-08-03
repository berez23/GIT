package it.webred.ct.service.tsSoggiorno.data.access.dao;

import it.webred.ct.service.tsSoggiorno.data.access.TsSoggiornoServiceException;
import it.webred.ct.service.tsSoggiorno.data.access.dto.DataInDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.VersamentoDTO;


import java.util.List;

public interface MavDAO {

	public List<VersamentoDTO> getVersamentiMavByCodFiscale(String cf)
			throws TsSoggiornoServiceException;
	
	public List<VersamentoDTO> getVersamentiMavByCodFiscalePeriodo(DataInDTO dataIn)
			throws TsSoggiornoServiceException;
}
