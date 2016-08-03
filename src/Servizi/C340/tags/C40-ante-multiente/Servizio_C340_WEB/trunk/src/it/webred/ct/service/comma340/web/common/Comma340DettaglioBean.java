package it.webred.ct.service.comma340.web.common;

import java.io.File;
import java.io.FilenameFilter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import it.webred.ct.data.access.basic.catasto.dto.DataVariazioneImmobileDTO;
import it.webred.ct.data.access.basic.catasto.dto.DettaglioCatastaleImmobileDTO;
import it.webred.ct.data.access.basic.catasto.dto.PlanimetriaComma340DTO;
import it.webred.ct.data.model.catasto.SitRepTarsu;
import it.webred.ct.data.model.catasto.Sitiuiu;
import it.webred.ct.service.comma340.data.access.C340CommonService;
import it.webred.ct.service.comma340.data.access.dto.DettaglioC340ImmobileDTO;
import it.webred.ct.service.comma340.web.Comma340BaseBean;

public class Comma340DettaglioBean extends Comma340BaseBean {
	
	private final String CATASTO = "catasto";
	private final String TARSU = "tarsu";
	
	private String mode;
	private String selIdUiu;
	private DettaglioC340ImmobileDTO dettImmobile;
	private List<SelectItem> dtValCatImmItems;
	private boolean planimetrieLoaded;
	private String viewPage;
	
	private boolean viewRepTarsu;
	
	private List<SitRepTarsu> repTarsu;
	
	public void setViewPage(String viewPage) {
		this.viewPage = viewPage;
	}

	public String getViewPage() {
		return viewPage;
	}

	public boolean isViewRepTarsu() {
		return viewRepTarsu;
	}

	public void setViewRepTarsu(boolean viewRepTarsu) {
		this.viewRepTarsu = viewRepTarsu;
	}

	public List<SitRepTarsu> getRepTarsu() {
		return repTarsu;
	}

	public void setRepTarsu(List<SitRepTarsu> repTarsu) {
		this.repTarsu = repTarsu;
	}

	public boolean isPlanimetrieLoaded() {
		return planimetrieLoaded;
	}

	public void setPlanimetrieLoaded(boolean planimetrieLoaded) {
		this.planimetrieLoaded = planimetrieLoaded;
	}
	
	public String apriRepTarsu() {
		String esito = "success";
		viewRepTarsu = !viewRepTarsu;
		return esito;
	}

	public String apriDettaglio() {
		
		String esito = "comma340DettaglioBean.apriDettaglio";
		String msg = "dettaglioImmobile.apri";
		try{
			String codNazionale = getCodNazionaleFromUser();
			
			C340CommonService c340 = super.getC340CommonService();
			dettImmobile = c340.getDettaglioC340Immobile(codNazionale, selIdUiu);
			planimetrieLoaded = false;
			viewRepTarsu = false;
			
			/*			
			//Inserisce la lista delle planimetrie nella pagina di dettaglio
			ListaPlanimetrieBean list= new ListaPlanimetrieBean();
			list.findFilePlanimetrie(dettImmobile.getCatasto().getPlanimetrieComma340());
			dettImmobile.getCatasto().setPlanimetrieComma340(list.getPlanimetrieC340());
			*/
			
			getLogger().debug("APERTURA DETTAGLIO COMPLETATO");
			
			fillDtValCatImmItems();
			
		}
		catch(Throwable t) {
			super.addErrorMessage(msg+".error", t.getMessage());
			getLogger().error("",t);
			t.printStackTrace();
		}

		return esito;
	}
	
	public void apriDettaglioDtVal(ValueChangeEvent vce) {
		selIdUiu = vce.getNewValue().toString();
		apriDettaglio();
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getSelIdUiu() {
		return selIdUiu;
	}

	public void setSelIdUiu(String selIdUiu) {
		this.selIdUiu = selIdUiu;
	}

	public DettaglioC340ImmobileDTO getDettImmobile() {
		return dettImmobile;
	}

	public void setDettImmobile(DettaglioC340ImmobileDTO dettImmobile) {
		this.dettImmobile = dettImmobile;
	}
	
	public String getNomeListaBack() {
		if (mode.equalsIgnoreCase(CATASTO)) {
			return "listRicercaCatasto";
		} else if (mode.equalsIgnoreCase(TARSU)) {
			//TODO
			//return "listRicercaTarsu";
		}
		return "listRicercaCatasto"; //default
	}
	
	public List<SelectItem> getDtValCatImmItems() {
		return dtValCatImmItems;
	}

	public void setDtValCatImmItems(List<SelectItem> dtValCatImmItems) {
		this.dtValCatImmItems = dtValCatImmItems;
	}

	private void fillDtValCatImmItems() {
		dtValCatImmItems = new ArrayList<SelectItem>();
		String msg = "dettaglioImmobile.catasto.apri";
		try{
		
			DettaglioCatastaleImmobileDTO catasto = getDettImmobile().getCatasto();
			Sitiuiu immobile = catasto.getImmobile();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			//data corrente
			dtValCatImmItems.add(new SelectItem(immobile.getPkidUiu(), 
					(immobile.getDataInizioVal() == null ? "" : sdf.format(immobile.getDataInizioVal())) + " - " +
					(immobile.getId().getDataFineVal() == null ? "" : sdf.format(immobile.getId().getDataFineVal()))));
			//altre date validità
			for (DataVariazioneImmobileDTO dtVar : catasto.getAltreDateValidita()) {
				dtValCatImmItems.add(new SelectItem(dtVar.getIdUiu(),
						(dtVar.getDataInizioVal() == null ? "" : sdf.format(dtVar.getDataInizioVal())) + " - " +
						(dtVar.getDataFineVal() == null ? "" : sdf.format(dtVar.getDataFineVal()))));
			}
			
			getLogger().debug("CARICAMENTO DETTAGLIO COMPLETATO");
		}
		catch(Throwable t) {
			super.addErrorMessage(msg+".error", t.getMessage());
			getLogger().error("",t);
			t.printStackTrace();
		}

	}
	
	/**
	 * Se non è stato ancora fatto (planimetrieLoaded = FALSE) 
	 * carica la lista completa delle planimetrie associate all'immobile.
	 */
	public void creaListaPlanimetrieC340(){
		
		String msg = "dettaglioImmobile.planimetrie.c340.apri";
		try{
			List<PlanimetriaComma340DTO> lista = dettImmobile.getCatasto().getPlanimetrieComma340();
			
			if(!planimetrieLoaded){
				//Elabora i percorsi delle planimetrie
				this.findFilePlanimetrie(lista);
			}
			getLogger().debug("CARICAMENTO PLANIMETRIE COMMA 340 COMPLETATO");
			
		}
		catch(Throwable t) {
			super.addErrorMessage(msg+".error", t.getMessage());
			getLogger().error("",t);
			t.printStackTrace();
		}
	}


	/**
	 * Scandisce la lista dei prefissi dei nomi dei file planimetrici associati all'immobile e per ciascuno di essi 
	 * analizza il file system recuperando i nomi dei file completi. 
	 * Alla fine imposta la lista completa nel dettaglio dell'immobile settando la variabile booleana della classe.
	 * 
	 * @param planimC340_parziali Lista dei prefissi dei nomi dei file planimetrici, così come estratti dal DB
	 */
	public void findFilePlanimetrie(List<PlanimetriaComma340DTO> planimC340_parziali){
		ArrayList<PlanimetriaComma340DTO> planimetrieC340 = new ArrayList<PlanimetriaComma340DTO>();
		getLogger().debug("Rilevati [num:"+planimC340_parziali.size()+"] nomi planimetrici.");
		String pathPlanimetrieC340 = super.getParamValue("path_planimetrie_c340");
		
		if (pathPlanimetrieC340 != null && !pathPlanimetrieC340.trim().equals("")) {
			File dirpath = new File(pathPlanimetrieC340);
			if(dirpath.exists()){
				for (PlanimetriaComma340DTO pPar : planimC340_parziali) {
					PrefissoComma340Filter filter = new PrefissoComma340Filter(pPar.getPrefissoNomeFile());
					String[] files = dirpath.list(filter);
					if (files != null) {
						for (String file : files) {
							PlanimetriaComma340DTO pCompl = new PlanimetriaComma340DTO();
							pCompl.setFile(file);
							pCompl.setLink(pathPlanimetrieC340 + File.separatorChar + file);
							pCompl.setSubalterno(pPar.getSubalterno());
							planimetrieC340.add(pCompl);
						}
					}
				}
				dettImmobile.getCatasto().setPlanimetrieComma340(planimetrieC340);
				planimetrieLoaded = true;
				getLogger().debug("Rilevati [num:"+planimetrieC340.size()+"] file planimetrici.");
			}else{
				super.addErrorMessage("dettaglioImmobile.planimetrie.c340.lista.error.pathnotfound","");
			}
		}else{
			super.addErrorMessage("dettaglioImmobile.planimetrie.c340.lista.error.nopath","");
		}
	
	}
	
	public Integer getRepTarsuLocatari() {
		for (SitRepTarsu repTarsuRec : repTarsu) {
			if (repTarsuRec.getLocatari() != null) {
				return new Integer(repTarsuRec.getLocatari().intValue());
			}
		}
		return null;
	}
	
	public Integer getRepTarsuUiuCiv() {
		for (SitRepTarsu repTarsuRec : repTarsu) {
			if (repTarsuRec.getUiuCiv() != null) {
				return new Integer(repTarsuRec.getUiuCiv().intValue());
			}
		}
		return null;
	}
	
	public Integer getRepTarsuAltriResCiv() {
		for (SitRepTarsu repTarsuRec : repTarsu) {
			if (repTarsuRec.getAltriResCiv() != null) {
				return new Integer(repTarsuRec.getAltriResCiv().intValue());
			}
		}
		return null;
	}
	
	public Boolean getRepTarsuLocatarioTarsu() {
		for (SitRepTarsu repTarsuRec : repTarsu) {
			if (repTarsuRec.getLocatarioTarsu() != null) {
				return new Boolean(new BigDecimal(1).equals(repTarsuRec.getLocatarioTarsu()));
			}
		}
		return null;
	}
	
	public Integer getRepTarsuAltriResTarsu() {
		for (SitRepTarsu repTarsuRec : repTarsu) {
			if (repTarsuRec.getAltriResTarsu() != null) {
				return new Integer(repTarsuRec.getAltriResTarsu().intValue());
			}
		}
		return null;
	}
	
	public Integer getRepTarsuLocatariTarsu() {
		for (SitRepTarsu repTarsuRec : repTarsu) {
			if (repTarsuRec.getLocatariTarsu() != null) {
				return new Integer(repTarsuRec.getLocatariTarsu().intValue());
			}
		}
		return null;
	}

protected class PrefissoComma340Filter implements FilenameFilter {

	private String prefisso;

	public PrefissoComma340Filter(String prefisso) {
		this.prefisso = prefisso;
	}

	public boolean accept(File dir, String name) {
		return (name.startsWith(prefisso + "."));
	}
}



	
}
