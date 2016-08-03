package it.webred.ct.service.muidocfa.web.bean.tarsu.planimetrie;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.PlanimetriaComma340DTO;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.service.muidocfa.web.bean.util.GestioneFileBean;

public class PlanimetrieC340Bean extends GestioneFileBean {
	
	private List<PlanimetriaComma340DTO> planimetrieC340;
	private final String DIR_PLANIMETRIEC340 = "planimetriec340";
	private String message;

	public List<PlanimetriaComma340DTO> creaListaPlanimetrieC340(RicercaOggettoCatDTO ro){
			
			String msg = "planimetrie.c340";
			try{

				fillEnte(ro);
				List<PlanimetriaComma340DTO> lista = catastoService.getPlanimetrieComma340(ro);
				
				//Elabora i percorsi delle planimetrie
				this.findFilePlanimetrie(lista);
				
			}
			catch(Throwable t) {
				super.addErrorMessage(msg+".error", t.getMessage());
				t.printStackTrace();
			}
			return planimetrieC340;
		}
	
	

	public void findFilePlanimetrie(List<PlanimetriaComma340DTO> planimC340_parziali){
		planimetrieC340 = new ArrayList<PlanimetriaComma340DTO>();
		
		String pathPlanimetrieC340 = this.getFolderPath();
		
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
					if(planimetrieC340.size()==0){
						message = "I file delle planimetrie non sono presenti nel percorso di directory impostato ["+pathPlanimetrieC340+"].";
						getLogger().warn(message);
					}
				}
			}else{
				message = "Il percorso di directory in cui dovrebbero risiedere i file delle Planimetrie Comma 340 non esiste ["+pathPlanimetrieC340+"].";
				getLogger().warn(message);
			}
		}else{
			message = "Il percorso in cui risiedono i file delle Planimetrie Comma 340 non è stato impostato!";
			getLogger().warn(message);
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

	/**
	 * Compone il path completo del file concatenando il percorso delle planimetrie
	 * (nel file di configurazione)con il nome del file (passato come parametro)
	 * 
	 * @param fileName nome del file
	 */
	@Override
	protected String getFilePath(String fileName) {
		String pathFile = this.getFolderPath() + File.separatorChar + fileName;
		this.getLogger().info("Planimetria C340 da scaricare:" +pathFile);
		return pathFile;
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

	protected String getFolderPath() {
		String path = super.getFolderPath() + File.separatorChar + DIR_PLANIMETRIEC340 ;
		this.getLogger().info("Percorso Planimetrie C340:" +path);
		return path;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
