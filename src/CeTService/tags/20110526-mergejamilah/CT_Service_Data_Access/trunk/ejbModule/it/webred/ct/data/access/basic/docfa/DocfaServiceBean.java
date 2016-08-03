package it.webred.ct.data.access.basic.docfa;

import it.webred.ct.data.access.basic.diagnostiche.DiagnosticheDataIn;
import it.webred.ct.data.access.basic.diagnostiche.dto.RicercaDocfaReportDTO;
import it.webred.ct.data.access.basic.diagnostiche.ici.DiagnosticheIciServiceException;
import it.webred.ct.data.access.basic.diagnostiche.tarsu.DiagnosticheTarServiceException;
import it.webred.ct.data.access.basic.diagnostiche.tarsu.dto.SitTTarOggettoDTO;
import it.webred.ct.data.access.basic.docfa.dao.DocfaDAO;
import it.webred.ct.data.access.basic.docfa.dto.DatiDocfaDTO;
import it.webred.ct.data.access.basic.docfa.dto.RicercaOggettoDocfaDTO;
import it.webred.ct.data.model.docfa.DocfaDatiCensuari;
import it.webred.ct.data.model.docfa.DocfaDatiMetrici;
import it.webred.ct.data.model.docfa.DocfaDichiaranti;
import it.webred.ct.data.model.docfa.DocfaInParteDueH;
import it.webred.ct.data.model.docfa.DocfaIntestati;
import it.webred.ct.data.model.docfa.DocfaPlanimetrie;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;
 
@Stateless
public class DocfaServiceBean extends DocfaBaseService implements DocfaService {

	@Autowired
	private DocfaDAO docfaDAO;
	
	public List<DocfaDatiCensuari> getListaDocfaImmobile(RicercaOggettoDocfaDTO ro) {		
		return docfaDAO.getListaDocfaImmobile(ro);
	}
	public List<DatiDocfaDTO> getListaDatiDocfaFabbricato(RicercaOggettoDocfaDTO ro) {		
		if (ro.getUnimm()!=null && !ro.getUnimm().equals(""))
			return docfaDAO.getListaDatiDocfaUiu(ro);
		else
			return docfaDAO.getListaDatiDocfaFPSubNonVal(ro);
	}
	
	public List<DocfaDichiaranti> getDichiaranti(RicercaOggettoDocfaDTO dataIn){
		Date fornitura = dataIn.getFornitura();
		String protocollo = dataIn.getProtocollo();
		return docfaDAO.getDichiaranti(fornitura, protocollo);
	}

	public List<DocfaIntestati> getIntestati(RicercaOggettoDocfaDTO dataIn){
		
		Date fornitura = dataIn.getFornitura();
		String protocollo = dataIn.getProtocollo();
		return docfaDAO.getIntestati(fornitura, protocollo);
	}
	
	
	public DocfaInParteDueH getDatiMetriciABC(RicercaOggettoDocfaDTO ro){
		return docfaDAO.getDatiMetriciABC(ro);
	}
	
	public List<DocfaDatiMetrici> getDatiMetrici(RicercaOggettoDocfaDTO ro){
		return docfaDAO.getDatiMetrici(ro);
	}
	
	public List<DocfaPlanimetrie> getPlanimetrieDocfa(RicercaOggettoDocfaDTO ro){
		return docfaDAO.getPlanimetrieDocfa(ro);
	}
	
}
