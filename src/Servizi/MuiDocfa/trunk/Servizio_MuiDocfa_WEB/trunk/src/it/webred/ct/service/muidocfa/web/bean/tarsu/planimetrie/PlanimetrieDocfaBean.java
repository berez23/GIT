package it.webred.ct.service.muidocfa.web.bean.tarsu.planimetrie;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.webred.ct.data.access.basic.diagnostiche.DiagnosticheDataIn;
import it.webred.ct.data.access.basic.diagnostiche.dto.RicercaDocfaReportDTO;
import it.webred.ct.data.access.basic.docfa.dto.RicercaOggettoDocfaDTO;
import it.webred.ct.data.model.docfa.DocfaPlanimetrie;
import it.webred.ct.service.muidocfa.web.bean.util.GestioneFileBean;

public class PlanimetrieDocfaBean extends GestioneFileBean {

	private List<DocfaPlanimetrie> planimetrieDocfa;
	
	private final String dirPlanimetrieDocfa = "planimetrie";

	private String fornitura;

	public List<DocfaPlanimetrie> creaListaPlanimetrieDocfa(
			RicercaDocfaReportDTO rs) {

		String msg = "planimetrie.docfa";
		try {

			if (rs.getDataRegistrazioneDocfa() != null) {
			
				DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
				dataIn.setRicerca(rs);
				fillEnte(dataIn);
				
				List<DocfaPlanimetrie> lista = docfaService.getPlanimetrieDocfa(fillRicercaOggettoDocfa(dataIn.getRicerca()));

				// Elabora i percorsi delle planimetrie
				this.findFilePlanimetrie(lista);
			} else
				super.addErrorMessage("planimetrie.docfa.datareg.null", "");

		} catch (Throwable t) {
			super.addErrorMessage(msg + ".error", t.getMessage());
			t.printStackTrace();
		}
		return planimetrieDocfa;
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
		String nomeFile = fileName;
		String pathFile = this.getFolderPath() + File.separatorChar + nomeFile;
		this.getLogger().info("Percorso Planimetria Docfa da scaricare:" +pathFile);
		return pathFile;
	}

	protected String getFolderPath() {		
		String pathPdfDocfa = super.getFolderPath() + File.separatorChar + 
								dirPlanimetrieDocfa + File.separatorChar
								+ getFornitura().substring(0, 6);
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


	public String getFornitura() {
		fornitura = fornitura.replaceAll("-", "");
		return fornitura;
	}

	public void setFornitura(String fornitura) {
		this.fornitura = fornitura;
	}

}
