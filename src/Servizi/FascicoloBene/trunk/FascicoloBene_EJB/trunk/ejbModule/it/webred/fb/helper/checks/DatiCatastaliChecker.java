package it.webred.fb.helper.checks;

import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.CatastoSearchCriteria;
import it.webred.ct.data.access.basic.catasto.dto.ImmobileBaseDTO;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.model.catasto.Sititrkc;
import it.webred.ct.data.model.catasto.Sitiuiu;
import it.webred.ejb.utility.ClientUtility;
import it.webred.fb.dao.DettaglioBeneDAO;
import it.webred.fb.data.DataModelCostanti.Segnalibri.TipoAlert;
import it.webred.fb.ejb.DettaglioBeneSessionBean;
import it.webred.fb.ejb.client.DettaglioBeneSessionBeanRemote;
import it.webred.fb.ejb.dto.Alert;
import it.webred.fb.ejb.dto.BeneInListaDTO;
import it.webred.fb.ejb.dto.DatoSpec;
import it.webred.fb.ejb.dto.KeyValueDTO;
import it.webred.fb.ejb.dto.RicercaBeneDTO;
import it.webred.fb.ejb.dto.TabellaDatiCollegati;
import it.webred.fb.ejb.dto.locazione.DatiCatastali;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class DatiCatastaliChecker extends  DatiTerreniChecker  {
	
	List<ImmobileBaseDTO> immobili;
	List<Sitiuiu> dettaglioImmobili;
	List<BeneInListaDTO> inventari;
	
	@Autowired
	private DettaglioBeneDAO dettaglioDao;
	
	public DatiCatastaliChecker(String ente) {
		super(ente);
		immobili = new ArrayList<ImmobileBaseDTO>();
		inventari = new ArrayList<BeneInListaDTO>();
	}

	
	protected  List<Alert> checkImpl() throws CheckerException {
		List<Alert> resp = new ArrayList<Alert>();
		checkTerreni(dato.getCodComune().getDato(),dato.getFoglio().getDato(), dato.getMappale().getDato(),resp);
		checkImmobili(dato.getCodComune().getDato(),dato.getFoglio().getDato(), dato.getMappale().getDato(),resp);
		checkEsistenzaAltriInventari(dato.getCodInventario().getDato(),dato.getCodComune().getDato(),dato.getFoglio().getDato(), dato.getMappale().getDato(),resp);
		return resp;
		
	}

	protected LinkedList<TabellaDatiCollegati> collegaDati() {
		
		LinkedList<TabellaDatiCollegati> tabelle = new LinkedList<TabellaDatiCollegati>();
		tabelle.addAll(super.collegaDati());
		
		if(!immobili.isEmpty()){
			tabelle.add(packGraffatiInDatiAggiuntivi());
		if(!dettaglioImmobili.isEmpty())
			tabelle.add(packImmobiliInDatiAggiuntivi());
		}
		if(!inventari.isEmpty())
			tabelle.add(packInventariInDatiAggiuntivi(inventari));
		return tabelle;
		
	}
	
	private TabellaDatiCollegati packImmobiliInDatiAggiuntivi() {
		
		TabellaDatiCollegati tab = new TabellaDatiCollegati("Dettaglio Immobili");
		tab.addNomeColonna("Foglio");
		tab.addNomeColonna("Part.");
		tab.addNomeColonna("Sub");
		tab.addNomeColonna("Data Inizio");
		tab.addNomeColonna("Data Fine");
		tab.addNomeColonna("Cat.");
		tab.addNomeColonna("Classe");
		tab.addNomeColonna("Cons.");
		tab.addNomeColonna("Rendita");
		tab.addNomeColonna("Sup.");
		tab.addNomeColonna("Partita");

		for (Sitiuiu sitiuiu : dettaglioImmobili) {
			tab.addRiga(sitiuiu.getId().getPkidUiu().toString(), 
					new DatoSpec(Long.toString(sitiuiu.getId().getFoglio())),
					new DatoSpec(sitiuiu.getId().getParticella()),
					new DatoSpec(Long.toString(sitiuiu.getId().getUnimm())),
					new DatoSpec(formatData(sitiuiu.getDataInizioVal())),
					new DatoSpec(formatData(sitiuiu.getId().getDataFineVal())),
					new DatoSpec(sitiuiu.getCategoria()!=null ? sitiuiu.getCategoria() : "-"),
					new DatoSpec(sitiuiu.getClasse()!=null ? sitiuiu.getClasse() : "-"),
					new DatoSpec(sitiuiu.getConsistenza()),
					new DatoSpec(sitiuiu.getRendita()),
					new DatoSpec(sitiuiu.getSupCat()!=null ? sitiuiu.getSupCat().toString() : "-"),
					new DatoSpec(sitiuiu.getPartita()!=null ? sitiuiu.getPartita() : "-")
			);
		}	
		
		return tab;

	}
	
	private TabellaDatiCollegati packGraffatiInDatiAggiuntivi() {
		
		TabellaDatiCollegati tab = new TabellaDatiCollegati("Dati da Catasto Urbano");
		tab.addNomeColonna("");
		tab.addNomeColonna("Foglio");
		tab.addNomeColonna("Part.");
		tab.addNomeColonna("Sub");
		tab.addNomeColonna("Data Inizio");
		tab.addNomeColonna("Data Fine");
		tab.addNomeColonna("Dettaglio");
		tab.addNomeColonna("Ui Graffate");
		
		for (ImmobileBaseDTO i : immobili) {
			tab.addRiga(i.getCodNazionale()+i.getFoglio()+i.getParticella()+i.getUnimm(), 
					new DatoSpec(i.isGraffato()? "Graffato a:" : ""),
					new DatoSpec(i.getFoglio()),
					new DatoSpec(i.getParticella()),
					new DatoSpec(i.getUnimm()),
					new DatoSpec(formatData(i.getDataInizioVal())),
					new DatoSpec(formatData(i.getDataFineVal())),
					new DatoSpec(i.getLstStorico()),
					new DatoSpec(i.getLstGraffati())
					
			);
		}	
		
		return tab;

	}
	
	protected void checkImmobili(String enteMappale, String foglio, String mappale, List<Alert> alerts) throws CheckerException {
		
			RicercaOggettoCatDTO roc = new RicercaOggettoCatDTO();
			roc.setEnteId(ente);
			roc.setCodEnte(enteMappale);
			roc.setFoglio(foglio);
			roc.setParticella(mappale);
			roc.setDtVal(new Date());
		
			//immobili  = new ArrayList<Sitiuiu>();  
			immobili  = new ArrayList<ImmobileBaseDTO>();  
			dettaglioImmobili = new ArrayList<Sitiuiu>();
			
			fillListaImmobiliByFP(roc);
			if (immobili.isEmpty()){
				roc.setDtVal(null);
				//Verifico se esiste il mappale cessato nel catasto urbano
				fillListaImmobiliByFP(roc);
				if (immobili.size()>0)
					alerts.add(new Alert(TipoAlert.IMM_NOT_VALIDATE, "Mappale cessato in catasto urbano"));	
				else
					alerts.add(new Alert(TipoAlert.NOT_VALIDATE, "Mappale non presente in catasto urbano"));
			}else{
				alerts.add(new Alert(TipoAlert.IMM_VALIDATE, "Mappale presente in catasto urbano"));
				//packImmobiliInDatiAggiuntivi();
			}
	}
	
	
	private void fillListaImmobiliByFP(RicercaOggettoCatDTO roc) throws CheckerException{
		
		CatastoService ejb=null;
		
		try {
			ejb = (CatastoService) ClientUtility.getEjbInterface("CT_Service", "CT_Service_Data_Access", "CatastoServiceBean");
		
			List<ImmobileBaseDTO>  lstTmp = ejb.getPrincipalieGraffatiByFP(roc);
		
			//Recupero la lista di unità principali
			for(ImmobileBaseDTO imm : lstTmp){
				
				RicercaOggettoCatDTO cg = new RicercaOggettoCatDTO();
				cg.setEnteId(roc.getEnteId());
				cg.setUserId(roc.getUserId());
				cg.setCodEnte(imm.getCodNazionale());
				cg.setFoglio(imm.getFoglio());
				cg.setParticella(imm.getParticella());
				cg.setUnimm(imm.getUnimm());
				cg.setDtVal(roc.getDtVal());
				
				if(imm.isGraffato()){
					//Recupero l'unità principale associata alla graffata
					imm = ejb.getPrincipaleDellaGraffata(cg);
					imm.setGraffato(true);	
					
					cg.setCodEnte(imm.getCodNazionale());
					cg.setFoglio(imm.getFoglio());
					cg.setParticella(imm.getParticella());
					cg.setUnimm(imm.getUnimm());
						
				}
								
				//Per ciascuna recupero la lista dei graffati
				List<ImmobileBaseDTO> lstg = ejb.getListaGraffatiPrincipale(cg);
				String desc = "";
				if(lstg.size()>0){
					desc+="<table  width=\"200px\" style=\"padding:0px;\">"
						+ "<tr>"
							+ "<th width=\"25%\">Sez</th>"
							+ "<th width=\"25%\">Fog</th>"
							+ "<th width=\"25%\">Part.</th>"
							+ "<th width=\"25%\">Sub</th>"
						+ "</tr></table>"
						+ "<div style=\"max-height:50px;overflow-y:auto;width:217px\">"
						+ "<table width=\"200px\"  style=\"padding:0px; \">";
					for(ImmobileBaseDTO ig : lstg){
						desc+="<tr>";
						desc+= "<td width=\"25%\">" + (ig.getSez()!=null ? ig.getSez() : "-") +"</td>";
						desc+= "<td width=\"25%\">" + (ig.getFoglio()!=null ? ig.getFoglio() : "-") +"</td>";
						desc+= "<td width=\"25%\">" + (ig.getParticella()!=null ? ig.getParticella() : "-") +"</td>";
						desc+= "<td width=\"25%\">" + (ig.getUnimm()!=null ? ig.getUnimm() : "-") +"</td>";
						desc+="</tr>";
					}
					desc+="</table></div>";
				}
				imm.setLstGraffati(desc);
				immobili.add(imm);
				
				//Per ciascuna recupero il dettaglio
				List<Sitiuiu> lstDettaglio = new ArrayList<Sitiuiu>();
				lstDettaglio = ejb.getListaImmobiliByFPS(cg);
				
				String descStorico = "";
				if(lstDettaglio.size()>0){
					descStorico+="<table width=\"500px\" style=\"padding:0px; \">"
							+ "<tr>"
								+ "<th width=\"10%\">Cat.</th>"
								+ "<th width=\"10%\">Cls.</th>"
								+ "<th width=\"10%\">Cons.</th>"
								+ "<th width=\"10%\">Rendita</th>"
								+ "<th width=\"10%\">Sup.</th>"
								+ "<th width=\"10%\">Partita</th>"
								+ "<th width=\"20%\">Data Inizio</th>"
								+ "<th width=\"20%\">Data Fine</th>"
								
							+ "</tr></table>"
							+ "<div style=\"max-height:50px;overflow-y:auto;width:517px;\">"
							+ "<table style=\"width:500px;padding:0px;\">";
						for(Sitiuiu sitiuiu : lstDettaglio){
							descStorico+="<tr>";
							descStorico+= "<td width=\"10%\">" + (sitiuiu.getCategoria()!=null ? sitiuiu.getCategoria() : "-") +"</td>";
							descStorico+= "<td width=\"10%\">" + (sitiuiu.getClasse()!=null ? sitiuiu.getClasse() : "-") +"</td>";
							descStorico+= "<td width=\"10%\">" + (sitiuiu.getConsistenza()!=null ? sitiuiu.getConsistenza() : "-") +"</td>";
							descStorico+= "<td width=\"10%\">" + (sitiuiu.getRendita()!=null ? sitiuiu.getRendita() : "-") +"</td>";
							descStorico+= "<td width=\"10%\">" + (sitiuiu.getSupCat()!=null ? sitiuiu.getSupCat().toString() : "-") +"</td>";
							descStorico+= "<td width=\"10%\">" + (sitiuiu.getPartita()!=null ? sitiuiu.getPartita() : "-") +"</td>";
							descStorico+= "<td width=\"20%\">" + (formatData(sitiuiu.getDataInizioVal())) +"</td>";
							descStorico+= "<td width=\"20%\">" + (formatData(sitiuiu.getId().getDataFineVal())) +"</td>";
							descStorico+="</tr>";
						}
						descStorico+="</table></div>";
				}
				imm.setLstStorico(descStorico);
				//dettaglioImmobili.addAll(lstDettaglio);
		
			}
		
			
		} catch (Exception e) {
			logger.error("Impossibile verificare il Mappale a causa della chiamata fallita su EJB di CT_Service");
			throw new CheckerException();
		}	
	}
	

	protected void checkEsistenzaAltriInventari(String codInventario, String enteMappale, String foglio, String mappale, List<Alert> alerts) throws CheckerException {
		try{
			//Verifico la presenza di altri inventari collegati al mappale
			inventari = new ArrayList<BeneInListaDTO>();
			DettaglioBeneSessionBeanRemote beneService = (DettaglioBeneSessionBeanRemote) 
					ClientUtility.getEjbInterface("FascicoloBene", "FascicoloBene_EJB", "DettaglioBeneSessionBean");
			RicercaBeneDTO rb = new RicercaBeneDTO();
			rb.setEnteId(ente);
			rb.setRicercaCatasto(true);
			KeyValueDTO comuneCat = new KeyValueDTO();
			comuneCat.setCodice(enteMappale);
			rb.setComuneCat(comuneCat);
			rb.setFoglio(foglio);
			rb.setMappale(mappale);
			inventari = beneService.searchBene(rb);
			//Rimuovo l'inventario corrente
			int i = inventari.size()-1;
			while(i>=0){
				BeneInListaDTO b = inventari.get(i);
				if(b.getBene().getChiave().equalsIgnoreCase(codInventario))
					inventari.remove(b);
				i--;
			}
			
			if(!inventari.isEmpty())
				alerts.add(new Alert(TipoAlert.INV_VALIDATE, "Mappale presente in altri Inventari"));
		}catch(NamingException ne){
			logger.error("Errore recupero DettaglioBeneSessionBeanRemote");
		}
	}
	

}
