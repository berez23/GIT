package it.webred.ct.service.comma340.web.catasto;

import it.webred.ct.data.access.basic.catasto.*;
import it.webred.ct.data.access.basic.catasto.dto.PlanimetriaComma340DTO;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class ListaPlanimetrieBean extends CatastoBaseBean{

	private String viewPage;
	
	private List<PlanimetriaComma340DTO> planimetrieC340;
	
	private String foglio;
	private String particella;
	private String unimm;
	
	public void setViewPage(String viewPage) {
		this.viewPage = viewPage;
	}

	public String getViewPage() {
		return viewPage;
	}

	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}

	public String getFoglio() {
		return foglio;
	}

	public String getParticella() {
		return particella;
	}


	public void setParticella(String particella) {
		this.particella = particella;
	}


	public List<PlanimetriaComma340DTO> getPlanimetrieC340() {
		if(planimetrieC340==null)
			planimetrieC340 = new ArrayList<PlanimetriaComma340DTO>();
		getLogger().debug("Restituiti [num:"+planimetrieC340.size()+"] file planimetrici.");
		return planimetrieC340;
	}
	
	
	public void setPlanimetrieC340(List<PlanimetriaComma340DTO> planimetrieC340) {
		this.planimetrieC340 = planimetrieC340;
	}

	public void setUnimm(String unimm) {
		this.unimm = unimm;
	}

	public String getUnimm() {
		return unimm;
	}

	public void creaListaPlanimetrieC340(){
			
			String msg = "dettaglioImmobile.planimetrie.c340.apri";
			try{
				String codNazionale = getCodNazionaleFromUser();
				
				CatastoService service = super.getCatastoService();
				List<PlanimetriaComma340DTO> lista = service.getPlanimetrieComma340(codNazionale, foglio,particella,unimm);
				
				//Elabora i percorsi delle planimetrie
				this.findFilePlanimetrie(lista);
				
				getLogger().debug("CARICAMENTO PLANIMETRIE COMMA 340 COMPLETATO");
				
			}
			catch(Throwable t) {
				super.addErrorMessage(msg+".error", t.getMessage());
				getLogger().error("",t);
				t.printStackTrace();
			}
		}
	
	

	public void findFilePlanimetrie(List<PlanimetriaComma340DTO> planimC340_parziali){
		planimetrieC340 = new ArrayList<PlanimetriaComma340DTO>();
		getLogger().debug("Rilevati [num:"+planimC340_parziali.size()+"] nomi planimetrici.");
		String pathPlanimetrieC340 = super.getParamValue("path_planimetrie_c340");
		
		if (pathPlanimetrieC340 != null && !pathPlanimetrieC340.trim().equals("")) {
			File dirpath = new File(pathPlanimetrieC340);
			if(dirpath.exists()){
				if(planimC340_parziali.size()>0){
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
					getLogger().debug("Rilevati [num:"+planimetrieC340.size()+"] file planimetrici.");
					if(planimetrieC340.size()==0){
						super.addErrorMessage("dettaglioImmobile.planimetrie.c340.lista.error.filesnotfound","");
					}
				}
			}else{
				getLogger().warn("Percorso Planimetrie Comma 340 non esistente");
				super.addErrorMessage("dettaglioImmobile.planimetrie.c340.lista.error.pathnotfound","");
			}
		}else{
			getLogger().warn("Percorso Planimetrie Comma 340 non impostato!");
			super.addErrorMessage("dettaglioImmobile.planimetrie.c340.lista.error.nopath","");
		}

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
