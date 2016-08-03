package it.webred.ct.service.carContrib.data.access.cc;

import it.webred.ct.service.carContrib.data.access.cc.dto.CodiciTipoMezzoRispostaDTO;
import it.webred.ct.service.carContrib.data.access.cc.dto.FonteNoteDTO;
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

import javax.ejb.Remote;

@Remote
public interface CarContribService {
	public DecodTipDoc getTipDoc(TipDocDTO td) ;
	public Long insertRichiesta(SoggettiCarContribDTO soggRicDTO, SoggettiCarContribDTO soggCarDTO , RichiesteDTO richDTO ) ;
	public List<DecodTipDoc> getListaTipDoc(CeTBaseObject obj);
	
	public Richieste getRichiesta(RichiesteDTO richDTO);
	public void updateFilePdfRichiesta(RichiesteDTO richDTO)throws CarContribException;
	public void insertRisposta(RisposteDTO rispDTO);
	public Risposte getRisposta(RichiesteDTO richDTO);
	
	public SoggettiCarContrib getSoggetto(SoggettiCarContribDTO soggCarDTO);
	
	public Long insertOnlyRichiesta(RichiesteDTO richDTO,SoggettiCarContribDTO soggettoCartella ) ;
	
	public List<String> getDistinctUserName(CeTBaseObject obj);  
	
	public List<CodiciTipoMezzoRisposta> getListaCodiciRisp(CeTBaseObject obj);
	public CodiciTipoMezzoRisposta getDescCodiciRisp(CodiciTipoMezzoRispostaDTO codiceDTO);
	public CfgFonteNote getFonteNote(FonteNoteDTO cfgFonteNoteDTO) throws CarContribException;;
}
