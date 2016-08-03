package it.webred.ct.diagnostics.service.data.access;

import it.webred.ct.diagnostics.service.data.SuperDia;
import it.webred.ct.diagnostics.service.data.dto.DiaDettaglioDTO;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface DiaDettaglioService {
	
	public List<? extends SuperDia> getDettaglioDiagnostica(DiaDettaglioDTO dd);
	public List<String[]> getDettaglioDiagnosticaArray(DiaDettaglioDTO dd);
	
	public Long getCount(DiaDettaglioDTO dd);
	
	
	public List<? extends SuperDia> getDettaglioDiagnosticaCompleto(DiaDettaglioDTO dd);
}
