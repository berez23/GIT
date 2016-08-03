package it.webred.ct.service.tsSoggiorno.data.access.dao;

import it.webred.ct.service.tsSoggiorno.data.access.dto.TariffaDTO;
import it.webred.ct.service.tsSoggiorno.data.model.IsTariffa;

import java.util.List;

public interface TariffaDAO {

	public IsTariffa getTariffaByPeriodoClasse(Long periodo, String classe);
	
	public List<TariffaDTO> getListaTariffe();
	
	public void saveTariffa(IsTariffa tar);
	
	public void deleteTariffa(Long periodo, String classe);
}
