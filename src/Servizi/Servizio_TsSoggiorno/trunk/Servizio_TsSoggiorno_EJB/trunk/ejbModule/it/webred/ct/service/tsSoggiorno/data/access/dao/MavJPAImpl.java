package it.webred.ct.service.tsSoggiorno.data.access.dao;

import it.webred.ct.service.tsSoggiorno.data.access.TsSoggiornoServiceException;
import it.webred.ct.service.tsSoggiorno.data.access.dto.DataInDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.TariffaDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.VersamentoDTO;
import it.webred.ct.service.tsSoggiorno.data.model.IsClasse;
import it.webred.ct.service.tsSoggiorno.data.model.IsDichiarazione;
import it.webred.ct.service.tsSoggiorno.data.model.IsPeriodo;
import it.webred.ct.service.tsSoggiorno.data.model.IsTariffa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

public class MavJPAImpl extends TsSoggiornoBaseDAO implements
		MavDAO {

	public List<VersamentoDTO> getVersamentiMavByCodFiscale(String cf)
			throws TsSoggiornoServiceException {
		List<VersamentoDTO> listaVersamenti = new ArrayList<VersamentoDTO>();
		try {

			Query q = manager
					.createNamedQuery("Mav.getVersamentiByCf")
					.setParameter("codFiscale", cf);
			List<Object[]> lista = q.getResultList();
			if (lista != null && lista.size() > 0){
				for(Object[] obj : lista){
					VersamentoDTO vers = new VersamentoDTO();
				
					vers.setImporto((String)obj[0]);
					vers.setImportoSpeseComm((BigDecimal)obj[1]);
					vers.setDataContabile((Date)obj[2]);
					
					String idFlusso = (String)obj[3];
					Long progressivo = (Long)obj[4];
					
					vers.setMotivo(getMavMotivo(idFlusso, progressivo));
					
					vers.setCodFiscale((String)obj[5]);
					vers.setSocieta((String)obj[6]);
					vers.setStruttura((String)obj[7]);
					vers.setSegmento((String)obj[8]);
					
					listaVersamenti.add(vers);
				}
			}

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
			
		}
		return listaVersamenti;
	}
	
	public List<VersamentoDTO> getVersamentiMavByCodFiscalePeriodo(DataInDTO dataIn)
			throws TsSoggiornoServiceException {
		List<VersamentoDTO> listaVersamenti = new ArrayList<VersamentoDTO>();
		try {

			Query q = manager
					.createNamedQuery("Mav.getVersamentiByCfPeriodo")
					.setParameter("codFiscale", dataIn.getCodFiscale())
					.setParameter("idDa", dataIn.getIdPeriodoDa())
					.setParameter("idA", dataIn.getIdPeriodoA());
			List<Object[]> lista = q.getResultList();
			if (lista != null && lista.size() > 0){
				for(Object[] obj : lista){
					VersamentoDTO vers = new VersamentoDTO();
				
					vers.setImporto((String)obj[0]);
					vers.setImportoSpeseComm((BigDecimal)obj[1]);
					vers.setDataContabile((Date)obj[2]);
					
					String idFlusso = (String)obj[3];
					Long progressivo = (Long)obj[4];
					
					vers.setMotivo(getMavMotivo(idFlusso, progressivo));
					
					vers.setCodFiscale((String)obj[5]);
					vers.setSocieta((String)obj[6]);
					vers.setStruttura((String)obj[7]);
					vers.setSegmento((String)obj[8]);
					
					listaVersamenti.add(vers);
				}
			}

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
			
		}
		return listaVersamenti;
	}
	
	private String getMavMotivo(String idFlusso, Long progressivo){
		String motivo = "";
		
		try {

			Query q = manager
					.createNamedQuery("Mav.getMotivoVersamento")
					.setParameter("idFlusso",idFlusso)
					.setParameter("progressivo",new BigDecimal(progressivo));
			
			List<String> lista = q.getResultList();
				for(String s : lista)
					motivo+=s!=null ? s : "";

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
			
		}
		return motivo;
	}
	
}
