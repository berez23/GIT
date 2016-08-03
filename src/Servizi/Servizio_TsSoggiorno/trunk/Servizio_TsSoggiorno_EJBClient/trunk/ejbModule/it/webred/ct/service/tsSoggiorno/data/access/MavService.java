package it.webred.ct.service.tsSoggiorno.data.access;

import it.webred.ct.service.tsSoggiorno.data.access.dto.DataInDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.TariffaDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.VersamentoDTO;
import it.webred.ct.service.tsSoggiorno.data.model.IsTariffa;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface MavService {

	public List<VersamentoDTO> getListaVersamentiMav(DataInDTO dataIn);

	public List<VersamentoDTO> getListaVersamentiMavPeriodo(DataInDTO dataIn);
}
