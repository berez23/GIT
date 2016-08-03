package it.webred.ct.service.ff.data.access.common.dao;

import it.webred.ct.service.ff.data.access.common.dto.CodiciTipoMezzoRispostaDTO;
import it.webred.ct.service.ff.data.access.dao.FFDAOException;
import it.webred.ct.service.ff.data.model.CodiciTipoMezzoRisposta;
import it.webred.ct.service.ff.data.model.FFTipoDoc;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.util.List;

public interface FFCommonDAO {

	public List<FFTipoDoc> getTipoDoc() throws FFDAOException;
	
	public List<String> getDistinctUserName(CeTBaseObject obj) throws FFDAOException;
	
	public List<CodiciTipoMezzoRisposta> getListaCodiciRisp(CeTBaseObject obj) throws FFDAOException;

	public CodiciTipoMezzoRisposta getDescCodiciRisp(CodiciTipoMezzoRispostaDTO codiceDTO) throws FFDAOException;

}
