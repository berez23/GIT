package it.webred.ct.data.access.basic.docfa;

import it.webred.ct.data.access.basic.diagnostiche.DiagnosticheDataIn;
import it.webred.ct.data.access.basic.diagnostiche.dto.RicercaDocfaReportDTO;
import it.webred.ct.data.access.basic.diagnostiche.ici.DiagnosticheIciServiceException;
import it.webred.ct.data.access.basic.docfa.dto.DatiDocfaDTO;
import it.webred.ct.data.access.basic.docfa.dto.RicercaOggettoDocfaDTO;
import it.webred.ct.data.model.docfa.DocfaDatiCensuari;
import it.webred.ct.data.model.docfa.DocfaDatiMetrici;
import it.webred.ct.data.model.docfa.DocfaDichiaranti;
import it.webred.ct.data.model.docfa.DocfaInParteDueH;
import it.webred.ct.data.model.docfa.DocfaIntestati;
import it.webred.ct.data.model.docfa.DocfaPlanimetrie;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface DocfaService {
	
	public List<DocfaDatiCensuari> getListaDocfaImmobile(RicercaOggettoDocfaDTO ro);
	public List<DatiDocfaDTO> getListaDatiDocfaFabbricato(RicercaOggettoDocfaDTO ro);

	public List<DocfaDichiaranti> getDichiaranti(RicercaOggettoDocfaDTO dataIn);
	public List<DocfaIntestati> getIntestati(RicercaOggettoDocfaDTO dataIn);
	public List<DocfaDatiMetrici> getDatiMetrici(RicercaOggettoDocfaDTO rs);
	public DocfaInParteDueH getDatiMetriciABC(RicercaOggettoDocfaDTO rs);
	public List<DocfaPlanimetrie> getPlanimetrieDocfa(RicercaOggettoDocfaDTO rs);
}
