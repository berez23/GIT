package it.webred.ct.service.comma336.web.bean;

import it.webred.ct.data.access.basic.docfa.dto.RicercaOggettoDocfaDTO;
import it.webred.ct.data.model.docfa.DocfaPlanimetrie;
import it.webred.ct.service.comma336.web.bean.util.GestioneFileBean;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PlanimetrieDocfaBean extends GestioneFileBean {

	private static final long serialVersionUID = 1L;
	private String foglio;
	private String particella;
	private String unimm;
	private String fornitura;
	private String protocollo;
	private String dataRegistrazione;
	private Date dFornitura;
	private Date dDataRegistrazione;
	
	private List<DocfaPlanimetrie> planimetrieDocfa;
	private final String dirPlanimetrieDocfa = "planimetrie";
	
	SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");

	public void creaListaPlanimetrieDocfa() {

		String msg = "planimetrie.docfa";
		try {
			getLogger().debug("DOCFA:["+fornitura+","+protocollo+","+dataRegistrazione+"]");
			getLogger().debug("FPS:["+foglio+","+particella+","+unimm+"]");

			if(dataRegistrazione!=null){
				RicercaOggettoDocfaDTO ro = new RicercaOggettoDocfaDTO();
				fillEnte(ro);
				ro.setFoglio(foglio);
				ro.setParticella(particella);
				ro.setUnimm(unimm);
				ro.setFornitura(dFornitura);
				ro.setProtocollo(protocollo);
				ro.setDataRegistrazione(dataRegistrazione);
				
				List<DocfaPlanimetrie> lista = docfaService.getPlanimetrieDocfa(ro);
			
				// Elabora i percorsi delle planimetrie
				this.findFilePlanimetrie(lista);
			} else
				super.addErrorMessage("planimetrie.docfa.datareg.null", "");

		} catch (Throwable t) {
			super.addErrorMessage(msg + ".error", t.getMessage());
			logger.error(t.getMessage(), t);
		}
	
	}

	public void findFilePlanimetrie(List<DocfaPlanimetrie> planimDocfa_parziali) {
		planimetrieDocfa = new ArrayList<DocfaPlanimetrie>();
		String pathPlanimetrieDocfa = getFolderPath();

		if (pathPlanimetrieDocfa != null
				&& !pathPlanimetrieDocfa.trim().equals("")) {
			File dirpath = new File(pathPlanimetrieDocfa);
			if (dirpath.exists()) {
				if (planimDocfa_parziali.size() > 0) {
					for (DocfaPlanimetrie pPar : planimDocfa_parziali) {
						PrefissoDocfaFilter filter = new PrefissoDocfaFilter(
								pPar.getId().getNomePlan());
						String[] files = dirpath.list(filter);
						if (files != null) {
							for (String file : files) {
								planimetrieDocfa.add(pPar);
							}
						}
					}
					if (planimetrieDocfa.size() == 0) {
						getLogger().warn("I file delle planimetrie Docfa non sono presenti nel percorso di directory impostato ["+pathPlanimetrieDocfa+"].");
					}
				}
			} else {
				getLogger()
						.warn(
								"Il percorso di directory in cui dovrebbero risiedere i file delle Planimetrie Docfa non esiste ["+pathPlanimetrieDocfa+"].");
			}
		} else {
			getLogger()
					.warn(
							"Il percorso in cui risiedono i file delle Planimetrie Docfa non è stato impostato!");
		}

	}

	protected class PrefissoDocfaFilter implements FilenameFilter {

		private String prefisso;

		public PrefissoDocfaFilter(String prefisso) {
			this.prefisso = prefisso;
		}

		public boolean accept(File dir, String name) {
			return (name.startsWith(prefisso + "."));
		}
	}

	/**
	 * Compone il path completo del file concatenando:
	 * root_diogene/F704/docfa_pdf/200412/num_protocollo.pdf (nel file di
	 * configurazione)con il nome del file (passato come parametro)
	 * 
	 * @param fileName
	 *            nome del file
	 */
	@Override
	protected String getFilePath(String fileName) {
		getLogger().debug("PlanimetrieDocfaBean.getFilePath()");
		String nomeFile = fileName;
		String pathFile = this.getFolderPath() + File.separatorChar + nomeFile;
		this.getLogger().info("Percorso Planimetria Docfa da scaricare:" +pathFile);
		return pathFile;
	}

	protected String getFolderPath() {		
		getLogger().debug("PlanimetrieDocfaBean.getFolderPath()");
		String root = super.getRootPathDatiDiogene();
		String rootPathEnte= root +  getCodEnte();
		String pathPdfDocfa = rootPathEnte + File.separatorChar + 
								dirPlanimetrieDocfa + File.separatorChar
								+ fornitura.substring(0, 6);
		this.getLogger().info("Percorso Planimetrie Docfa:" +pathPdfDocfa);
		return pathPdfDocfa;
	}

	public List<DocfaPlanimetrie> getPlanimetrieDocfa() {
		if (planimetrieDocfa == null)
			planimetrieDocfa = new ArrayList<DocfaPlanimetrie>();
		getLogger().debug(
				"Restituiti [num:" + planimetrieDocfa.size()
						+ "] file planimetrici.");
		return planimetrieDocfa;
	}

	public void setPlanimetrieDocfa(List<DocfaPlanimetrie> planimetrieDocfa) {
		this.planimetrieDocfa = planimetrieDocfa;
	}

	public String getFoglio() {
		return foglio;
	}

	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}

	public String getParticella() {
		return particella;
	}

	public void setParticella(String particella) {
		this.particella = particella;
	}

	public String getUnimm() {
		return unimm;
	}

	public void setUnimm(String unimm) {
		this.unimm = unimm;
	}

	public String getFornitura() {
		return fornitura;
	}

	public void setFornitura(String fornitura) {
		this.fornitura = fornitura;
		try{
			this.dFornitura = yyyyMMdd.parse(fornitura);
		}catch(Exception e){}
	}

	public String getProtocollo() {
		return protocollo;
	}

	public void setProtocollo(String protocollo) {
		this.protocollo = protocollo;
	}

	public String getDataRegistrazione() {
		return dataRegistrazione;
	}

	public void setDataRegistrazione(String dataRegistrazione) {
		this.dataRegistrazione = dataRegistrazione;
		try{
			this.dDataRegistrazione = yyyyMMdd.parse(dataRegistrazione);
		}catch(Exception e){}
	}

	public Date getdFornitura() {
		return dFornitura;
	}

	public Date getdDataRegistrazione() {
		return dDataRegistrazione;
	}


}
