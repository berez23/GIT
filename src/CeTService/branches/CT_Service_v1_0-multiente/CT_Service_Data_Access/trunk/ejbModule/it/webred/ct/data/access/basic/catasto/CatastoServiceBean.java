package it.webred.ct.data.access.basic.catasto;

import it.webred.ct.data.access.basic.CTServiceBaseBean;
import it.webred.ct.data.access.basic.catasto.dao.CatastoDAO;
import it.webred.ct.data.access.basic.catasto.dto.DettaglioCatastaleImmobileDTO;
import it.webred.ct.data.access.basic.catasto.dto.ParticellaKeyDTO;
import it.webred.ct.data.access.basic.catasto.dto.PlanimetriaComma340DTO;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.catasto.dto.RicercaSoggettoCatDTO;
import it.webred.ct.data.access.basic.catasto.dto.SintesiImmobileDTO;
import it.webred.ct.data.access.basic.catasto.dto.IndirizzoDTO;
import it.webred.ct.data.access.basic.catasto.dto.SoggettoDTO;
import it.webred.ct.data.access.basic.catasto.dto.TerrenoPerSoggDTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import it.webred.ct.data.model.catasto.*;
import it.webred.ct.data.access.basic.catasto.dto.*;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Session Bean implementation class CatastoImmobiliServiceBean
 */ 
@Stateless 
public class CatastoServiceBean extends CTServiceBaseBean implements CatastoService {
      
	@Autowired
	private CatastoDAO catastoDAO;
	
	public List<Siticomu> getListaSiticomu(String descr){
		return catastoDAO.getListaSiticomu(descr);
	}
	
	public List<Sitideco> getListaCategorieImmobile(RicercaOggettoCatDTO ro){		
		return catastoDAO.getListaCategorieImmobile(ro);
	}

	public List<Sitidstr> getListaVie(RicercaOggettoCatDTO ro) {	
		return catastoDAO.getListaVie(ro);
	}
	
	public List<Siticivi> getListaIndirizzi(RicercaOggettoCatDTO ro) {		
		return catastoDAO.getListaIndirizzi(ro);
	}
	
	public List<ConsSoggTab> getListaSoggettiF(RicercaSoggettoCatDTO rs) {
		return catastoDAO.getListaSoggettiF(rs);
	}

	public List<ConsSoggTab> getListaSoggettiG(RicercaSoggettoCatDTO rs) {		
		return catastoDAO.getListaSoggettiG(rs);
	}

	public List<ConsSoggTab> getSoggettoByCF(RicercaSoggettoCatDTO rs) {		
		return catastoDAO.getSoggettoByCF(rs);
	}

	public ConsSoggTab getSoggettoByPIVA(RicercaSoggettoCatDTO rs) {		
		return catastoDAO.getSoggettoByPIVA(rs);
	}

	public Long getCatastoRecordCount(RicercaOggettoCatDTO ro) {		
		return catastoDAO.getCatastoRecordCount(ro);
	}

	public List<SintesiImmobileDTO> getListaImmobili(RicercaOggettoCatDTO ro) {		
		return catastoDAO.getListaImmobili(ro);
	}
	
	public List<IndirizzoDTO> getListaIndirizziImmobile(RicercaOggettoCatDTO ro) {		
		return catastoDAO.getListaIndirizziImmobile(ro);
	}
	
	public List<SoggettoDTO> getListaSoggettiImmobile(RicercaOggettoCatDTO ro) {
		return catastoDAO.getListaSoggettiImmobile(ro);
	}
	public List<SoggettoDTO> getListaSoggettiAttualiImmobile(RicercaOggettoCatDTO ro) {
		List<SoggettoDTO> lista  = new  ArrayList<SoggettoDTO>();
		List<SoggettoDTO> listaTemp = getListaSoggettiImmobile(ro);
		for (SoggettoDTO sogg: listaTemp) {
			if (sogg.getDataFinePoss() == null || sogg.getDataFinePoss().compareTo(new Date()) > 0)
				lista.add(sogg);
		}
		return lista;
	}
	public List<SoggettoDTO> getListaSoggettiStoriciImmobile(RicercaOggettoCatDTO ro) {
		List<SoggettoDTO> lista  = new  ArrayList<SoggettoDTO>();
		List<SoggettoDTO> listaTemp = getListaSoggettiImmobile(ro);
		for (SoggettoDTO sogg: listaTemp) {
			if (sogg.getDataFinePoss() != null && sogg.getDataFinePoss().compareTo(new Date()) <= 0)
				lista.add(sogg);
		}
		return lista;
	}
	public DettaglioCatastaleImmobileDTO getDettaglioImmobile(RicercaOggettoCatDTO ro) {		
		return catastoDAO.getDettaglioImmobile(ro);
	}

	public Sitiuiu getDatiUiAllaData(RicercaOggettoCatDTO ro) {
		return catastoDAO.getDatiUiAllaData(ro);
	}

	public SiticonduzImmAll getDatiBySoggUiAllaData(RicercaOggettoCatDTO ro, RicercaSoggettoCatDTO rs) {
		return catastoDAO.getDatiBySoggUiAllaData(ro, rs);
	}

	@Override
	public List<SiticonduzImmAll> getDatiBySoggFabbricatoAllaData(RicercaOggettoCatDTO ro, RicercaSoggettoCatDTO rs) {
		return catastoDAO.getDatiBySoggFabbricatoAllaData(ro, rs);
	}
	public List<BigDecimal> getIdSoggByCF(RicercaSoggettoCatDTO rs)  {
		return catastoDAO.getIdSoggByCF(rs);		
	}
	public BigDecimal getIdSoggByCFAllaData(RicercaSoggettoCatDTO rs)  {
		return catastoDAO.getIdSoggByCFAllaData(rs);		
	}
	public List<BigDecimal> getIdSoggByDatiAnag(RicercaSoggettoCatDTO rs) {
		return catastoDAO.getIdSoggByDatiAnag(rs);
	}
	public List<BigDecimal> getIdSoggByPI(RicercaSoggettoCatDTO rs) {
		return catastoDAO.getIdSoggByPI(rs);
	}
	public BigDecimal getIdSoggByPIAllaData(RicercaSoggettoCatDTO rs) {
		return catastoDAO.getIdSoggByPIAllaData(rs);
	}
	public List<BigDecimal> getIdSoggPGByDatiAnag(RicercaSoggettoCatDTO rs){
		return catastoDAO.getIdSoggPGByDatiAnag(rs);
	}
	public ConsSoggTab getSoggettoByID(RicercaSoggettoCatDTO rs) {
		return catastoDAO.getSoggettoBYID(rs);
	}
	// Recupera gli immobili alla dtVal, se essa è valorizzata, 
	// gli immobile posseduti tra la dtRifDa e la dtRifA, se esse sono valorizzate 
	// tutti gli immobili posseduti nel corso del tempo, se nessuna data è valorizzata 
	public List<SiticonduzImmAll> getImmobiliByIdSogg(RicercaSoggettoCatDTO rs) {
		if (rs.getDtVal()==null)
			return catastoDAO.getImmobiliByIdSogg(rs);
		else
			return catastoDAO.getImmobiliByIdSoggAllaData(rs);
	}
	

	public List<TerrenoPerSoggDTO> getTerreniByIdSogg(RicercaSoggettoCatDTO rs)  {
		if (rs.getDtVal()==null)
			return catastoDAO.getTerreniByIdSogg(rs);
		else
			return catastoDAO.getTerreniByIdSoggAllaData(rs);
	}

	public List<SiticonduzImmAll> getImmobiliByIdSoggCedutiInRangeDate(RicercaSoggettoCatDTO rs) {
		return catastoDAO.getImmobiliByIdSoggCedutiInRangeDate(rs);
	}

	public List<TerrenoPerSoggDTO> getTerreniByIdSoggCedutiInRangeDate(RicercaSoggettoCatDTO rs) {
		return catastoDAO.getTerreniByIdSoggCedutiInRangeDate(rs);
	}
	
	public List<DataVariazioneImmobileDTO> getDateVariazioniImmobile(RicercaOggettoCatDTO ro) {
		return catastoDAO.getDateVariazioniImmobile(ro);
	}
	
	public List<SitRepTarsu> getReportTarsuList(RicercaOggettoCatDTO ro) {
		return catastoDAO.getReportTarsuList(ro);
	}
	
	public List<SitiediVani> getDettaglioVaniC340(RicercaOggettoCatDTO ro){
		return catastoDAO.getDettaglioVaniC340(ro);
	}
	
	public List<PlanimetriaComma340DTO> getPlanimetrieComma340(RicercaOggettoCatDTO ro) {
		return catastoDAO.getPlanimetrieComma340(ro);
	}
	
	public ArrayList<IndirizzoDTO> getLocalizzazioneCatastale(RicercaOggettoCatDTO ro) {
		return catastoDAO.getLocalizzazioneCatastale(ro);
	}

	@Override
	public BigDecimal calcolaSupUtileTarsuC30(RicercaOggettoCatDTO ro) {
		BigDecimal supTarsuC340=null;
		List<SitiediVani> listaVani  = catastoDAO.getDettaglioVaniC340AllaData(ro);
			
		if (listaVani != null ) {
			supTarsuC340=new BigDecimal(0);
			for(SitiediVani vano: listaVani) {
				if (vano.getAmbiente().equalsIgnoreCase("A") || vano.getAmbiente().equalsIgnoreCase("B") || vano.getAmbiente().equalsIgnoreCase("C")) 
					supTarsuC340 =	supTarsuC340.add(vano.getSupeAmbiente());
			}
			
		}
		return supTarsuC340;
	}

	@Override
	public Siticomu getSitiComu(RicercaOggettoCatDTO ro) {
		return catastoDAO.getSitiComu(ro) ;
	}

	@Override
	public List<Siticomu> getSiticomuSezioni(RicercaOggettoCatDTO ro) {
		return catastoDAO.getSiticomuSezioni(ro);
	}

	@Override
	public List<ParticellaKeyDTO> getListaParticelle(RicercaOggettoCatDTO ro) {
		return catastoDAO.getListaParticelle(ro);
	}

	@Override
	public List<Sitiuiu> getListaUiAllaData(RicercaOggettoCatDTO ro) {
		return catastoDAO.getListaUiAllaData(ro) ;
	}

	@Override
	public List<IndirizzoDTO> getListaIndirizziPartAllaData(RicercaOggettoCatDTO ro) {
		return catastoDAO.getListaIndirizziPartAllaData(ro);
	}

	@Override
	public List<Sititrkc> getListaTerreniByFP(RicercaOggettoCatDTO ro) {
		return catastoDAO.getListaTerreniByFP(ro);
	}

	@Override
	public List<SoggettoDTO> getListaSoggettiAttualiTerreno(RicercaOggettoCatDTO ro) {
		return catastoDAO.getListaSoggettiAttualiTerreno(ro);
	}

	@Override
	public List<SoggettoDTO> getListaSoggettiStoriciTerreno(RicercaOggettoCatDTO ro) {
		return catastoDAO.getListaSoggettiStoriciTerreno(ro);
	}

	@Override
	public Sitideco getSitideco(RicercaOggettoCatDTO ro) {
		return catastoDAO.getSitideco(ro);
	}

	
}
