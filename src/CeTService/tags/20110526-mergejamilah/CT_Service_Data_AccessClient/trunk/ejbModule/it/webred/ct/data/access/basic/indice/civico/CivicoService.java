package it.webred.ct.data.access.basic.indice.civico;

import it.webred.ct.data.access.basic.indice.IndiceDataIn;
import it.webred.ct.data.access.basic.indice.civico.dto.SitCivicoDTO;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface CivicoService{

	//public List<SitCivicoDTO> getListaCiviciByVia(int start, int rowNumber, String id);	
	//public Long getListaCiviciByViaRecordCount(String id);
	
	public List<SitCivicoDTO> getListaCiviciByVia(IndiceDataIn indDataIn);	
	public Long getListaCiviciByViaRecordCount(IndiceDataIn indDataIn);

}
