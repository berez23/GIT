package it.webred.ct.service.carContrib.data.access.cnc;

import it.webred.ct.data.access.basic.ici.dto.RicercaSoggettoIciDTO;
import it.webred.ct.data.model.cnc.flusso750.VAnagrafica;
import it.webred.ct.service.carContrib.data.access.cnc.dto.DatiImportiCncDTO;
import it.webred.ct.service.carContrib.data.access.cnc.dto.RicercaCncDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.VersamentoDTO;

import java.util.List;

public interface CncCarContribServiceModel {

	public List<VAnagrafica> getAnagraficaDebitore(RicercaCncDTO rc) ;
	
	public List<DatiImportiCncDTO> getDatiCNC(RicercaCncDTO rc, int annoRif);
	
	public List<VersamentoDTO> getVersamenti(RicercaCncDTO rc, List<VersamentoDTO> listaVersamentiDaAggiornare);
	
	public List<DatiImportiCncDTO> getImportiCnc(RicercaCncDTO rc, List<DatiImportiCncDTO> listaDaAggiornare);
	
}
