package it.webred.ct.service.carContrib.data.access.cosap;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.webred.ct.data.access.basic.common.dto.RicercaSoggettoDTO;
import it.webred.ct.data.access.basic.cosap.CosapService;
import it.webred.ct.data.access.basic.cosap.dto.RicercaSoggettoCosapDTO;
import it.webred.ct.data.access.basic.ici.dto.RicercaSoggettoIciDTO;
import it.webred.ct.data.model.cosap.SitTCosapContrib;
import it.webred.ct.data.model.cosap.SitTCosapTassa;
import it.webred.ct.service.carContrib.data.access.common.CarContribServiceBaseBean;
import it.webred.ct.service.carContrib.data.access.common.GeneralService;
import it.webred.ct.service.carContrib.data.access.common.utility.DateUtility;
import it.webred.ct.service.carContrib.data.access.common.utility.StringUtility;
import it.webred.ct.service.carContrib.data.access.cosap.CosapCarContribService;

/**
 * Session Bean implementation class CosapCarContribServiceBean
*/
@Stateless
public class CosapCarContribServiceBean extends CarContribServiceBaseBean		implements CosapCarContribService {
	
	@EJB(mappedName = "java:global/CT_Service/CT_Service_Data_Access/CosapServiceBean")
	private CosapService cosapService;
	
	@EJB
	private GeneralService genService;
	
	public List<SitTCosapContrib> searchSoggettoCosap(RicercaSoggettoCosapDTO rs) {
		List<SitTCosapContrib>  listaSogg=null; 
		RicercaSoggettoCosapDTO parms = new RicercaSoggettoCosapDTO();
		parms.val(rs);
		parms.setTipoRicerca("all");
		if (rs.getTipoSogg().equals("F")) {
			//1. Ricerca del soggetto in banca dati: codice fiscale
			if(rs.getCodFis() != null &&  !rs.getCodFis().equals("")) {
				parms.forzaRicercaPerCFPI();
				listaSogg = cosapService.searchSoggetto(parms);
				
			}
			//2. Se non trovo cerco nome-cognome-data nascita-comune nascita 
			if (listaSogg==null || listaSogg.size() ==0) {
				logger.debug("ricerca per cognome-nome-data e luogo nascita");
				parms.val(rs);
				parms.forzaRicercaPerDatiAna();
				listaSogg = cosapService.searchSoggetto(parms);
			}
		}
		if (rs.getTipoSogg().equals("G")) {
			//1. Ricerca del soggetto per : PARTITA IVA + DENOMINAZIONE
			parms.forzaRicercaPerPG();
			if(rs.getParIva() != null &&  !rs.getParIva().equals("")) {
				listaSogg = cosapService.searchSoggetto(parms);
			}
			//2. se non trova, ricerca SOLO per PI -se significativa -
			if (listaSogg==null || listaSogg.size() ==0) {
				if(StringUtility.isANonZeroNumber(parms.getParIva())) {
					parms.val(rs);
					parms.forzaRicercaPerCFPI();
					listaSogg = cosapService.searchSoggetto(parms);
				}	
			}
			//3. Se non trovo cerco per denominazione
			if (listaSogg==null || listaSogg.size() ==0) {
				parms.val(rs);
				parms.forzaRicercaPerDatiAna();
				listaSogg = cosapService.searchSoggetto(parms);
			}
	
		}
		return listaSogg;
		
	}

	public List<SitTCosapTassa> getDatiCosap(RicercaSoggettoCosapDTO rs, Date dtRif) {
		List<SitTCosapTassa> listaCosap =cosapService.getDatiOggettiBySogg(rs);
		if (listaCosap!= null && listaCosap.size()>0) {
			for(SitTCosapTassa ogg: listaCosap) {
				if(ogg.getImportoCanone()!=null)
					ogg.setImportoCanoneStr(StringUtility.DFEURO.format(ogg.getImportoCanone()));
				else
					ogg.setImportoCanoneStr("");
				
				if(ogg.getTariffaPerUnita()!=null)
					ogg.setTariffaPerUnitaStr(StringUtility.DFEURO.format(ogg.getTariffaPerUnita()));
				else
					ogg.setTariffaPerUnitaStr("");
				
				if (ogg.getDtRichiesta() != null)
					ogg.setDtRichiestaStr(DateUtility.formatta(ogg.getDtRichiesta(), DateUtility.FMT_DATE_VIS));
				else
					ogg.setDtRichiestaStr("");
				
				if (ogg.getDtRichiesta() != null)
					ogg.setDtRichiestaStr(DateUtility.formatta(ogg.getDtRichiesta(), DateUtility.FMT_DATE_VIS));
				else
					ogg.setDtRichiestaStr("");
				
				if (ogg.getDtIniValidita() != null)
					ogg.setDtIniValiditaStr(DateUtility.formatta(ogg.getDtIniValidita(), DateUtility.FMT_DATE_VIS));
				else
					ogg.setDtIniValiditaStr("");
				
				if (ogg.getDtIniValiditaTariffa() != null)
					ogg.setDtIniValiditaTariffaStr(DateUtility.formatta(ogg.getDtIniValiditaTariffa(), DateUtility.FMT_DATE_VIS));
				else
					ogg.setDtIniValiditaTariffaStr("");
				
				if (ogg.getDtFinValiditaTariffa() != null)
					ogg.setDtFinValiditaTariffaStr(DateUtility.formatta(ogg.getDtFinValiditaTariffa(), DateUtility.FMT_DATE_VIS));
				else
					ogg.setDtFinValiditaTariffaStr("");
						
			}
		}
		
		return listaCosap;
	}

	public SitTCosapContrib getDatiCosapSoggetto(RicercaSoggettoCosapDTO rs) {
		return cosapService.getDatiSoggettoById(rs);
	}

	

	

}
