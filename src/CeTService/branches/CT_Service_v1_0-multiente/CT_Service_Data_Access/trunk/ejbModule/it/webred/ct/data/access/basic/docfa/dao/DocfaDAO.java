package it.webred.ct.data.access.basic.docfa.dao;

import it.webred.ct.data.access.basic.diagnostiche.dto.RicercaDocfaReportDTO;
import it.webred.ct.data.access.basic.docfa.dto.DatiDocfaDTO;
import it.webred.ct.data.access.basic.docfa.dto.RicercaOggettoDocfaDTO;
import it.webred.ct.data.model.docfa.DocfaDatiCensuari;
import it.webred.ct.data.model.docfa.DocfaDatiMetrici;
import it.webred.ct.data.model.docfa.DocfaDichiaranti;
import it.webred.ct.data.model.docfa.DocfaInParteDueH;
import it.webred.ct.data.model.docfa.DocfaIntestati;
import it.webred.ct.data.model.docfa.DocfaPlanimetrie;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

public interface DocfaDAO {
	
	public List<DocfaDatiCensuari> getListaDocfaImmobile(RicercaOggettoDocfaDTO ro);
	public List<DatiDocfaDTO> getListaDatiDocfaUiu(RicercaOggettoDocfaDTO ro) ;
	public List<DatiDocfaDTO> getListaDatiDocfaFPSubNonVal(RicercaOggettoDocfaDTO ro) ;
	
	public List<DocfaDichiaranti> getDichiaranti(Date fornitura, String protocollo);
	public List<DocfaIntestati> getIntestati(Date fornitura, String protocollo);
	public List<DocfaDatiMetrici> getDatiMetrici(RicercaOggettoDocfaDTO rs);
	public DocfaInParteDueH getDatiMetriciABC(RicercaOggettoDocfaDTO rs);
	public List<DocfaPlanimetrie> getPlanimetrieDocfa(RicercaOggettoDocfaDTO rs);
	
}
