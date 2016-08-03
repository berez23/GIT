package it.webred.ct.diagnostics.service.data.dao;

import java.util.List;

import it.webred.ct.diagnostics.service.data.dto.DiaLogAccessoDTO;
import it.webred.ct.diagnostics.service.data.exception.DIAServiceException;

import it.webred.ct.diagnostics.service.data.model.DiaLogAccesso;
import it.webred.ct.diagnostics.service.data.model.DiaTestata;
import it.webred.ct.diagnostics.service.data.model.controller.DiaCatalogo;

public interface IDAOListaDiagnostiche {
	public List<DiaCatalogo> getDiagnostiche() throws DIAServiceException;
	
	public DiaCatalogo getDiagnostica(long idDiagnostica) throws DIAServiceException;

	public List<DiaCatalogo> getDiagnosticheByCodCmdGrp(String codCommandGroup) throws DIAServiceException;
	
	public List<DiaTestata> getDiaEsecuzioneByIdDia(int start, int maxrows,long idCatalogoDia) throws DIAServiceException;
	
	public DiaTestata getDiaTestata(Long idDiaTestata) throws DIAServiceException;

	public List<DiaCatalogo> getDiagnosticheByCodCommand(String codCommand) throws DIAServiceException;

	public List<DiaLogAccesso> getLogAccessiByDiaTestata(int start, int maxrows, Long idDiaTestata) throws DIAServiceException;

	public void insertLogAccesso(DiaLogAccessoDTO logAccesso) throws DIAServiceException;

	public Long getCountLogByDiaTestata(Long idDiaTestata) throws DIAServiceException;
	
	public List<DiaCatalogo> getDiagnostiche(int start, int maxrows, String belfiore,List<Long> fonti, Long tipoComando) throws DIAServiceException;
	
	public Long getDiagnosticheCount(int start, int maxrows,String belfiore, List<Long> fonti, Long tipoComando) throws DIAServiceException;
	
	public List<DiaCatalogo> getAllDiagnosticheFonte(String belfiore, Long fonte) throws DIAServiceException;

	public DiaTestata getLastEsecuzioneByIdDia(long idCatalogoDia) throws DIAServiceException;

	public Long getStoricoEsecuzioniCount(long idCatalogoDia) throws DIAServiceException;
	
}
