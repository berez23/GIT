package it.webred.ct.service.spprof.data.access;


import java.util.List;

import it.webred.ct.service.spprof.data.access.dao.intervento.SpProfInterventoDAO;
import it.webred.ct.service.spprof.data.access.dto.InterventoSearchCriteria;
import it.webred.ct.service.spprof.data.access.dto.SpProfDTO;
import it.webred.ct.service.spprof.data.access.exception.SpProfException;
import it.webred.ct.service.spprof.data.model.SSpIntervento;
import it.webred.ct.service.spprof.data.model.SSpInterventoLayer;
import it.webred.ct.service.spprof.data.model.SSpSoggetto;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Session Bean implementation class SpProfInterventoServiceBean
 */
@Stateless
public class SpProfInterventoServiceBean implements SpProfInterventoService {

	@Autowired
	private SpProfInterventoDAO interventoDAO;

	public List<SSpIntervento> getIntervento(InterventoSearchCriteria criteria) throws SpProfException {
		try {		
			return interventoDAO.getIntervento(criteria);
		}catch(Throwable t) {
			throw new SpProfException(t);
		}
	}
	
	public Long getCountIntervento(InterventoSearchCriteria criteria) throws SpProfException {
		try {		
			return interventoDAO.getCountIntervento(criteria);
		}catch(Throwable t) {
			throw new SpProfException(t);
		}
	}
	
	public List<SSpIntervento> getInterventoByUser(SpProfDTO dto) throws SpProfException {
		try {		
			return interventoDAO.getInterventoByUser(dto.getUserId(), dto.getStart(), dto.getRecord());
		}catch(Throwable t) {
			throw new SpProfException(t);
		}
	}
	
	public Long getCountInterventoByUser(SpProfDTO dto) throws SpProfException {
		try {		
			return interventoDAO.getCountInterventoByUser(dto.getUserId());
		}catch(Throwable t) {
			throw new SpProfException(t);
		}
	}
	
	public SSpIntervento getInterventoById(SpProfDTO dto) throws SpProfException {
		try {		
			return interventoDAO.getIntervento((Long) dto.getObj());
		}catch(Throwable t) {
			throw new SpProfException(t);
		}
	}
	
	public Long save(SpProfDTO dto) throws SpProfException {
		try {		
			return interventoDAO.save(dto).getIdSpIntervento();
		}catch(Throwable t) {
			throw new SpProfException(t);
		}
	}

	public Long update(SpProfDTO dto) throws SpProfException {
		try {		
			return interventoDAO.update(dto);
		}catch(Throwable t) {
			throw new SpProfException(t);
		}
	}

	public void delete(SpProfDTO dto) throws SpProfException {
		try {		
			interventoDAO.delete(dto);
		}catch(Throwable t) {
			throw new SpProfException(t);
		}
	}
	
	//RICERCA
	public List<String> getStatiForSearch() throws SpProfException {
		try {		
			return interventoDAO.getStatiForSearch();
		}catch(Throwable t) {
			throw new SpProfException(t);
		}
	}
	
	public List<SSpSoggetto> getSoggettiForSearch() throws SpProfException {
		try {		
			return interventoDAO.getSoggettiForSearch();
		}catch(Throwable t) {
			throw new SpProfException(t);
		}
	}
	
	public List<String> getConcNumeroForSearch() throws SpProfException {
		try {		
			return interventoDAO.getConcNumeroForSearch();
		}catch(Throwable t) {
			throw new SpProfException(t);
		}
	}
	
	public List<String> getProtDataForSearch() throws SpProfException {
		try {		
			return interventoDAO.getProtDataForSearch();
		}catch(Throwable t) {
			throw new SpProfException(t);
		}
	}
	
	public List<String> getProtNumeroForSearch() throws SpProfException {
		try {		
			return interventoDAO.getProtNumeroForSearch();
		}catch(Throwable t) {
			throw new SpProfException(t);
		}
	}
	
	//INTEVENTO LAYER
	public List<SSpInterventoLayer> getInterventoLayerByIntervento(SpProfDTO dto) throws SpProfException {
		try {		
			return interventoDAO.getSSpInterventoLayerByFkSpIntervento((Long) dto.getObj());
		}catch(Throwable t) {
			throw new SpProfException(t);
		}
	}
	
	public void deleteInteventoLayer(SpProfDTO dto) throws SpProfException {
		try {		
			interventoDAO.deleteInteventoLayer(dto);
		}catch(Throwable t) {
			throw new SpProfException(t);
		}
	}
}
