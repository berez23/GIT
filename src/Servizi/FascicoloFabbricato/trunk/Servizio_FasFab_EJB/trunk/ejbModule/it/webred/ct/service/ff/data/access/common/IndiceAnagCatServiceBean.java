package it.webred.ct.service.ff.data.access.common;

import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.RicercaSoggettoCatDTO;
import it.webred.ct.data.access.basic.indice.ricerca.IndiceCorrelazioneService;
import it.webred.ct.data.access.basic.indice.ricerca.RicercaIndiceDTO;
import it.webred.ct.data.model.catasto.ConsSoggTab;
import it.webred.ct.service.ff.data.access.common.dto.RicercaDTO;
import it.webred.ct.service.ff.data.access.common.dto.SoggettoDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Session Bean implementation class CarContribServiceBean
 */
@Stateless
public class IndiceAnagCatServiceBean implements IndiceAnagCatService {

	@EJB(mappedName = "java:global/CT_Service/CT_Service_Data_Access/CatastoServiceBean")
	private CatastoService catastoService;

	@EJB(mappedName = "java:global/CT_Service/CT_Service_Data_Access/IndiceCorrelazioneServiceBean")
	private IndiceCorrelazioneService indiceService;

	
	public List<BigDecimal> getIndiciAnagCat(RicercaDTO dati, Date dtRif) {
		List<BigDecimal> indCat=new ArrayList<BigDecimal>();
		Object entitySogg =dati.getObjEntity();
		Object filtroSogg =dati.getObjFiltro();
		if (entitySogg == null && (filtroSogg instanceof SoggettoDTO )){//senza indice di correlazione
			return getIndiciAnagCat((SoggettoDTO)filtroSogg, dtRif);
		}
		List<Object> listaIdSoggCorr = getSoggettiCorrelati(dati,"1", "4","3");
		for (Object ele: listaIdSoggCorr) {
			BigDecimal id = ((ConsSoggTab)ele).getPkid();
			RicercaSoggettoCatDTO rs = new RicercaSoggettoCatDTO();
			rs.setEnteId(dati.getEnteId());
			rs.setUserId(dati.getUserId());
			rs.setPkid(id);
			ConsSoggTab sogg = catastoService.getSoggettoByID(rs);
			boolean scarta=false;
			//CONDIZIONI DI FILTRO
			if (dtRif!=null){
				if (sogg.getDataFine()==null  )  {
					if (sogg.getDataInizio().compareTo(dtRif) > 0)
						scarta=true;
				}
				else {
					if (sogg.getDataInizio().compareTo(dtRif) > 0 || sogg.getDataFine().compareTo(dtRif) <0    )
						scarta=true;
				}
			}
			if(!scarta)
			   indCat.add(sogg.getPkCuaa());
		}	
		return indCat;
	}
	
	
	private List<Object> getSoggettiCorrelati(RicercaDTO dati, String progEs,String destFonte, String destProgEs) {
		RicercaIndiceDTO ri = new RicercaIndiceDTO();
		Object sogg=dati.getObjEntity();
		ri.setEnteId(dati.getEnteId());
		ri.setUserId(dati.getUserId());
		ri.setObj(sogg);
		ri.setProgressivoEs(progEs); 
		ri.setDestFonte(destFonte);
		ri.setDestProgressivoEs(destProgEs);
		return indiceService.getSoggettiCorrelati(ri) ;
	}
	
	private List<BigDecimal> getIndiciAnagCat(SoggettoDTO sogg,	Date dtRif) {
		List<BigDecimal> listaIndSogg = new ArrayList<BigDecimal>();
		RicercaSoggettoCatDTO rsCat  =new RicercaSoggettoCatDTO(sogg.getCodEnte(),dtRif);
		rsCat.setEnteId(sogg.getEnteId());
		rsCat.setUserId(sogg.getUserId());
		if (sogg.getTipoSogg().equals("F")) {
			if(sogg.getCodFis()!= null && !sogg.getCodFis().equals("")) {
				rsCat.setCodFis(sogg.getCodFis());
				if (dtRif!=null) {
					BigDecimal id = catastoService.getIdSoggByCFAllaData(rsCat);
					if (id != null)
						listaIndSogg.add(id);
				}else {
					listaIndSogg= catastoService.getIdSoggByCF(rsCat);
				}
			}
			//CF non valorizzato oppure ricerca per cf non trovato
			if(listaIndSogg.size()==0) {
				rsCat.setDenom(sogg.getCognome() + " " + sogg.getNome());
				rsCat.setDtNas(sogg.getDtNas());
				listaIndSogg = catastoService.getIdSoggByDatiAnag(rsCat);
			}
		} else {
			if(sogg.getParIva() != null && !sogg.getParIva().equals("")) {
				rsCat.setPartIva(sogg.getParIva());
				if (dtRif!=null) {
					BigDecimal id = catastoService.getIdSoggByPIAllaData(rsCat);
					if (id != null)
						listaIndSogg.add(id);
				}else {
					listaIndSogg= catastoService.getIdSoggByPI(rsCat);
				}
			}
			//PI non valorizzato oppure ricerca per PI non trovato
			if(listaIndSogg.size()==0) {
				rsCat.setDenom(sogg.getDenom());
				listaIndSogg = catastoService.getIdSoggPGByDatiAnag(rsCat);
			}
		}
		if (listaIndSogg.size() == 0)
			listaIndSogg=null;
		return listaIndSogg;
	}


}
