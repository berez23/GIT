package it.webred.fb.ejb.client;

import it.webred.ct.support.datarouter.CeTBaseObject;
import it.webred.fb.data.model.DmConfClassificazione;
import it.webred.fb.data.model.DmConfDocDir;
import it.webred.fb.data.model.DmDDoc;
import it.webred.fb.ejb.dto.BaseDTO;
import it.webred.fb.ejb.dto.InfoCarProvenienzaDTO;
import it.webred.fb.ejb.dto.StatoConfDoc;

import java.util.List;

import javax.ejb.Remote;


@Remote
public interface CaricatoreSessionBeanRemote {

	public void caricaDatiBene(BaseDTO base);

	public List<InfoCarProvenienzaDTO> getInfoCaricamento(BaseDTO base);

	public void caricaDerivatiBene(BaseDTO base);

	public void caricaDocumenti(BaseDTO b);
	
	public List<DmConfDocDir> getConfCaricamentoDocs(CeTBaseObject b);

	public List<StatoConfDoc> getConfCaricamentoDocsAbilitati(CeTBaseObject b);

	public void svuotaProvenienza(BaseDTO b);
	
	public Long uploadDocumento(BaseDTO b);

	public DmConfClassificazione getClassificazioneDocumenti(BaseDTO b);

	public DmDDoc getDocumentoById(BaseDTO b);

	public void deleteDocumento(BaseDTO b);

	
}