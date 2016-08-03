package it.webred.ct.service.carContrib.data.access.cc.dao;

import it.webred.ct.service.carContrib.data.access.cc.CarContribException;
import it.webred.ct.service.carContrib.data.access.cc.dto.CodiciTipoMezzoRispostaDTO;
import it.webred.ct.service.carContrib.data.access.cc.dto.FiltroRichiesteDTO;
import it.webred.ct.service.carContrib.data.access.cc.dto.FiltroRichiesteSearchCriteria;
import it.webred.ct.service.carContrib.data.access.cc.dto.FonteNoteDTO;
import it.webred.ct.service.carContrib.data.access.cc.dto.GesRicDTO;
import it.webred.ct.service.carContrib.data.access.cc.dto.RichiesteDTO;
import it.webred.ct.service.carContrib.data.access.cc.dto.RisposteDTO;
import it.webred.ct.service.carContrib.data.access.cc.dto.SoggettiCarContribDTO;
import it.webred.ct.service.carContrib.data.access.cc.dto.TipDocDTO;
import it.webred.ct.service.carContrib.data.model.CfgFonteNote;
import it.webred.ct.service.carContrib.data.model.CodiciTipoMezzoRisposta;
import it.webred.ct.service.carContrib.data.model.DecodTipDoc;
import it.webred.ct.service.carContrib.data.model.Richieste;
import it.webred.ct.service.carContrib.data.model.Risposte;
import it.webred.ct.service.carContrib.data.model.SoggettiCarContrib;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.util.List;

public interface CarContribDAO {
	public DecodTipDoc getTipDoc(TipDocDTO td) throws CarContribException;
	public List<DecodTipDoc> getListaTipDoc(CeTBaseObject obj) throws CarContribException;
	//public Long getMaxIdSogg(CeTBaseObject bo) throws CarContribException;
	public SoggettiCarContrib searchSoggettoPF(SoggettiCarContribDTO soggDto) throws CarContribException ;
	public SoggettiCarContrib searchSoggettoPG(SoggettiCarContribDTO soggDto) throws CarContribException ;
	public void insertSoggetto(SoggettiCarContribDTO soggDto) throws CarContribException ;
	public SoggettiCarContrib getSoggetto(SoggettiCarContribDTO soggDTO)	throws CarContribException;
	public void insertRichiesta(RichiesteDTO richDto) throws CarContribException;
	public void insertGesRichiesta(GesRicDTO gesRicDto) throws CarContribException;

	public Richieste getRichiesta(RichiesteDTO richDTO);
	public void updateFilePdfRichiesta(RichiesteDTO richDto)throws CarContribException;
	public void insertRisposta(RisposteDTO rispDto) throws CarContribException;
	
	public Risposte getRisposta(RichiesteDTO richDTO);
	
	public List<FiltroRichiesteDTO> filtroRichiste(FiltroRichiesteSearchCriteria filtro,int start, int numberRecord);
	
	public List<String> getDistinctUserName(CeTBaseObject obj) throws CarContribException ;
	
	public Long getRecordCount(FiltroRichiesteSearchCriteria filtro) throws CarContribException ;
	
	public List<CodiciTipoMezzoRisposta> getListaCodiciRisp(CeTBaseObject obj) throws CarContribException;
	public CodiciTipoMezzoRisposta getDescCodiciRisp(CodiciTipoMezzoRispostaDTO codiceDTO) throws CarContribException;
	public CfgFonteNote getFonteNote(FonteNoteDTO obj);
	
}
