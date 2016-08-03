package it.webred.ct.diagnostics.service.data.access;


import it.webred.ct.diagnostics.service.data.dto.DiaCatalogoDTO;
import it.webred.ct.diagnostics.service.data.dto.DiaDettaglioDTO;
import it.webred.ct.diagnostics.service.data.dto.DiaLogAccessoDTO;
import it.webred.ct.diagnostics.service.data.dto.DiaTestataDTO;
import it.webred.ct.diagnostics.service.data.exception.DIAServiceException;

import it.webred.ct.diagnostics.service.data.model.DiaLogAccesso;
import it.webred.ct.diagnostics.service.data.model.DiaTestata;
import it.webred.ct.diagnostics.service.data.model.controller.DiaCatalogo;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface DiagnosticheService {

	public List<DiaCatalogo> getDiagnostiche(CeTBaseObject o) throws DIAServiceException;
	
	public List<DiaTestata> getDiaTestataByRange(DiaTestataDTO dt) throws DIAServiceException;
	
	public DiaTestata getDiaTestata(DiaTestataDTO dt) throws DIAServiceException;
	
	public DiaCatalogo getDiagnostica(DiaCatalogoDTO dc) throws DIAServiceException;
	
	public List<DiaCatalogo> getDiagnosticheByCodCmdGrp(DiaCatalogoDTO dc) throws DIAServiceException;
	
	/**
	 * Il metodo ritorna la lista delle esecuzioni relativa all'idcatalogoDia passato come parametro
	 * la lista è ordinata per data esecuzione desc.
	 * @param idCatalogoDia
	 * @return
	 * @throws DIAServiceException
	 */
	public List<DiaTestata> getDiaEsecuzioneByIdDia(DiaTestataDTO dt) throws DIAServiceException;
	
	/**
	 * Il metodo ritorna la lista diacatalogo filtrando il codcommand nel campo fk_cod_command_group o fk_cod_command
	 * @param dc
	 * @return
	 * @throws DIAServiceException
	 */
	public List<DiaCatalogo> getDiagnosticheByCodCommand(DiaCatalogoDTO dc) throws DIAServiceException;
	
	/**
	 * Il metodo ritorna la lista log accessi filtrando per dia testata
	 * @param dd
	 * @return
	 * @throws DIAServiceException
	 */
	public List<DiaLogAccesso> getLogAccessiByDiaTestata(DiaDettaglioDTO dd) throws DIAServiceException;
	
	/**
	 * Inserisce una nuova operazione nella tabella
	 * @param logAccesso
	 * @throws DIAServiceException
	 */
	public void insertLogAccesso(DiaLogAccessoDTO logAccesso) throws DIAServiceException;
	
	/**
	 * Ritorna il numero di record log collegati alla dia testata usato per la gestione della paginazione
	 * @param dd
	 * @return
	 * @throws DIAServiceException
	 */
	public Long getCountLog(DiaDettaglioDTO dd) throws DIAServiceException;
	
	
	
	/**
	 * Metodo che restituisce l'elenco delle diagnostiche in base ai parametri di paginazione
	 * 
	 * @param dc
	 * @return
	 * @throws DIAServiceException
	 */
	public List<DiaCatalogo> getDiagnostiche(DiaCatalogoDTO dc) throws DIAServiceException;
	
	
	/**
	 * Ritorna il numero di diagnostiche legato ad un certo ente e determinate fd
	 * @param dd
	 * @return
	 * @throws DIAServiceException
	 */
	public Long getDiagnosticheCount(DiaCatalogoDTO dd) throws DIAServiceException;
	
	
	/**
	 * Ritorna l'elenco delle diagnostiche per ente / fonte
	 * @param dc
	 * @return
	 * @throws DIAServiceException
	 */
	public List<DiaCatalogo> getAllDiagnosticheFonte(DiaCatalogoDTO dc) throws DIAServiceException;
	
	
	/**
	 * Il metodo ritorna l'ultima esecuzioni relativa all'idcatalogoDia passato come parametro
	 * @param idCatalogoDia
	 * @return
	 * @throws DIAServiceException
	 */
	public DiaTestata getLastEsecuzioneByIdDia(DiaTestataDTO dt) throws DIAServiceException;

	
	/**
	 * Ritorna il numero di storico esecuzioni legato ad un certo idCatalogoDia
	 * @param dd
	 * @return
	 * @throws DIAServiceException
	 */
	public Long getStoricoEsecuzioniCount(DiaTestataDTO dd) throws DIAServiceException;
	
}
