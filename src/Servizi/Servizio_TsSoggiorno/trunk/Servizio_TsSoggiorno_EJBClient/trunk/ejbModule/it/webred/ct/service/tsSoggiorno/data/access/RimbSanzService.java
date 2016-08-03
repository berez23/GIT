package it.webred.ct.service.tsSoggiorno.data.access;

import it.webred.ct.service.tsSoggiorno.data.access.dto.DataInDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.RimbSanzDTO;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface RimbSanzService {

	public List<RimbSanzDTO> getRimbSanzByCriteria(DataInDTO dataIn);
	
	public Long getRimbSanzCountByCriteria(DataInDTO dataIn);
	
	public void saveRimborso(DataInDTO dataIn);
	
	public void deleteRimborso(DataInDTO dataIn);
	
	public void saveSanzione(DataInDTO dataIn);
	
	public void deleteSanzione(DataInDTO dataIn);

}
