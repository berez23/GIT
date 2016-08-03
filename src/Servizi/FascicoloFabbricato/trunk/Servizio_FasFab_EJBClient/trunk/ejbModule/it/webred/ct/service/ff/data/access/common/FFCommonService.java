package it.webred.ct.service.ff.data.access.common;
import it.webred.ct.service.ff.data.access.common.dto.CodiciTipoMezzoRispostaDTO;
import it.webred.ct.service.ff.data.model.CodiciTipoMezzoRisposta;
import it.webred.ct.service.ff.data.model.FFTipoDoc;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface FFCommonService {

	public List<FFTipoDoc> getTipoDoc(CeTBaseObject obj);
	
	public List<String> getDistinctUserName(CeTBaseObject obj);
	
	public List<CodiciTipoMezzoRisposta> getListaCodiciRisp(CeTBaseObject obj);
	
	public CodiciTipoMezzoRisposta getDescCodiciRisp(CodiciTipoMezzoRispostaDTO codiceDTO);

}
