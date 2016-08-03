package it.webred.ct.diagnostics.service.data.dao;

import it.webred.ct.diagnostics.service.data.SuperDia;
import it.webred.ct.diagnostics.service.data.exception.DIAServiceException;

import java.util.List;

public interface IDAODettaglioDiagnostica {
	
	public List<? extends SuperDia> getDiagnosticaListaDettagli(int start,int maxrows,Long idDiaTestata, String modelClassname) throws DIAServiceException;
	public  List<String[]> getDiagnosticaArrayDettagli(int start,int maxrows,Long idDiaTestata, String modelClassname) throws DIAServiceException;
	
	public Long getCount(Long idDiaTestata, String modelClassname) throws DIAServiceException;
	
	public List<? extends SuperDia> getDiagnosticaListaDettagli(Long idDiaTestata, String modelClassname) throws DIAServiceException;
}
