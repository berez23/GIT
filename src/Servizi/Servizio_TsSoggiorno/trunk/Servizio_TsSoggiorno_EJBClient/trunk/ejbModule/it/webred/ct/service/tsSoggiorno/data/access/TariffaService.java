package it.webred.ct.service.tsSoggiorno.data.access;

import it.webred.ct.service.tsSoggiorno.data.access.dto.DataInDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.TariffaDTO;
import it.webred.ct.service.tsSoggiorno.data.model.IsTariffa;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface TariffaService {

	public IsTariffa getTariffaByPeriodoClasse(DataInDTO dataIn);
	
	public List<TariffaDTO> getListaTariffe(DataInDTO dataIn);
	
	public void saveTariffa(DataInDTO dataIn);
	
	public void deleteTariffa(DataInDTO dataIn);

}
