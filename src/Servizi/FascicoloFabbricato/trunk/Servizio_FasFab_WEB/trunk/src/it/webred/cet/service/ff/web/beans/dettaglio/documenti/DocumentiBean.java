package it.webred.cet.service.ff.web.beans.dettaglio.documenti;

import it.webred.cet.service.ff.web.beans.dettaglio.DatiDettaglio;
import it.webred.cet.service.ff.web.util.PermessiHandler;
import it.webred.cet.service.ff.web.util.TiffUtill;
import it.webred.ct.config.parameters.ParameterService;
import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.PlanimetriaComma340DTO;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.docfa.DocfaService;
import it.webred.ct.data.access.basic.docfa.dto.RicercaOggettoDocfaDTO;
import it.webred.ct.data.model.docfa.DocfaPlanimetrie;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

public class DocumentiBean extends DatiDettaglio implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private DocfaService docfaService;
	private ParameterService parameterService;
	private CatastoService catastoService;
	
	private List<DocfaPlanimetrie> listaDocfaPlanimetrie;
	private List<PlanimetriaComma340DTO> listaC340Planimetrie;
	
	private List<DocfaPlanimetrieDatiCensuari> listaDocfaPlanimetrieUiu;
	private List<DocfaPlanimetrie> listaDocfaPlanimetrieFab;
	private List<PlanimetriaComma340DTO> listaC340PlanimetrieUiu;
	private List<PlanimetriaComma340DTO> listaC340PlanimetrieFab;
	
	private String nomePlan;
	private String padProgressivo;
	private String fornituraStr;
	private String formato;
	private boolean watermark;
	private boolean openJpg;
	private String tipoPlan;
	private String linkPlan;
	
	public void doSwitch() {
		loadDocfaPlanimetrie();
		loadC340Planimetrie();
	}
	
	public void loadDocfaPlanimetrie() {
			
		logger.debug("Load Docfa Planimetrie");
		
		RicercaOggettoDocfaDTO ro = new RicercaOggettoDocfaDTO();
		
		ro.setEnteId(super.getEnte());
		ro.setUserId(super.getUsername());
		
		ro.setSezione(this.getSezione());
		ro.setFoglio(this.getFoglio());
		ro.setParticella(this.getParticella());
		
		listaDocfaPlanimetrie = docfaService.getPlanimetriePerSezFglNum(ro);
		
		listaDocfaPlanimetrieUiu = null;
		listaDocfaPlanimetrieFab = null;
		
	}
	
	public void loadC340Planimetrie() {
		
		logger.debug("Load C340 Planimetrie");
		
		RicercaOggettoCatDTO ro = new RicercaOggettoCatDTO();
		
		ro.setEnteId(super.getEnte());
		ro.setUserId(super.getUsername());
		
		ro.setSezione(this.getSezione());
		ro.setFoglio(this.getFoglio());
		ro.setParticella(this.getParticella());
		
		List<PlanimetriaComma340DTO> lista = catastoService.getPlanimetrieComma340SezFglNum(ro);
		
		//Elabora i percorsi delle planimetrie
		findFilePlanimetrie(lista);
		
		listaC340PlanimetrieUiu = null;
		listaC340PlanimetrieFab = null;
		
	}
	
	public void openPlanimetria() {	
		
		File image = null;
		
		InputStream is = null;
		InputStream isByte = null;
		ByteArrayOutputStream baos = null;
		OutputStream out = null;
		String pathFile = "";
		
		HttpServletResponse response = (HttpServletResponse)super.getResponse();  
		
		try {
			
			if (tipoPlan.equalsIgnoreCase(getTipoPlanDocfa())) {
				nomePlan = nomePlan + "." + padProgressivo + ".tif";
				String dirPlan = super.getGlobalConfig().get("dirPlanimetrie");
				pathFile = super.getPathDatiDiogeneEnte(parameterService) + File.separatorChar + dirPlan + File.separatorChar + fornituraStr.substring(0, 6) +  File.separatorChar + nomePlan;
			} else if (tipoPlan.equalsIgnoreCase(getTipoPlanC340())) {
				pathFile = linkPlan;
			}
			
			logger.debug("Planimetria:" + pathFile);
			File f = new File(pathFile);
						
			image = f;
			is = new FileInputStream(image);
					    
			List<ByteArrayOutputStream> jpgs = TiffUtill.tiffToJpeg(is, watermark, openJpg);
				if (openJpg) {
					baos = jpgs.get(0);
				} else {
					baos =  TiffUtill.jpgsTopdf(jpgs, false, Integer.valueOf(formato).intValue());
				}
				isByte = new ByteArrayInputStream(baos.toByteArray());
				is.close();
				
				
				if (openJpg && watermark) {
					response.addHeader("Content-Disposition","attachment; filename=" + nomePlan + ".jpg");
					response.setContentType("image/jpeg");
				} else if (openJpg && !watermark){
					response.addHeader("Content-Disposition","attachment; filename=" + nomePlan+ ".tiff");
					response.setContentType("image/tiff");
				} else {
					response.addHeader("Content-Disposition","attachment; filename=" + nomePlan + ".pdf");
					response.setContentType("application/pdf");
				}		
				
				out = response.getOutputStream();
				
				byte[] b = new byte[1024];
	            while (isByte.read(b) != -1) {
	                out.write(b);
	            }
	            
	            out.flush();	            
	            out.close();
	            
	            FacesContext context = FacesContext.getCurrentInstance(); 
	    		context.responseComplete();
	    		
		} catch (java.io.FileNotFoundException e) {			
			super.addErrorMessage("ff.file.non.trovato", "");
			logger.error("FILE NON TROVATO: " + pathFile, e);
		} catch (Throwable e) {			
			logger.error(e.getMessage(),e);
		}
	}
	
	protected void findFilePlanimetrie(List<PlanimetriaComma340DTO> planimC340_parziali) {
		
		listaC340Planimetrie = new ArrayList<PlanimetriaComma340DTO>();
		
		logger.debug("Rilevati [num:"+planimC340_parziali.size()+"] nomi planimetrici.");
		
		String dirPlanC340 = super.getGlobalConfig().get("dirPlanimetrieComma340");
		
		String pathPlanimetrieC340 = super.getPathDatiDiogeneEnte(parameterService) + File.separator + dirPlanC340;
		if (pathPlanimetrieC340 != null && !pathPlanimetrieC340.trim().equals("")) {
			File dirpath = new File(pathPlanimetrieC340);
			if(dirpath.exists()){
				if(planimC340_parziali.size() > 0){
					for (PlanimetriaComma340DTO pPar : planimC340_parziali) {
						PrefissoComma340Filter filter = new PrefissoComma340Filter(pPar.getPrefissoNomeFile());
						String[] files = dirpath.list(filter);
						if (files != null) {
							for (String file : files) {
								PlanimetriaComma340DTO pCompl = new PlanimetriaComma340DTO();
								pCompl.setFile(file);
								if ("\\".equals(File.separator)) {
									pCompl.setLink((pathPlanimetrieC340 + File.separator + file).replace(File.separator, File.separator + File.separator));
								} else {
									pCompl.setLink(pathPlanimetrieC340 + File.separator + file);
								}								
								pCompl.setSubalterno(pPar.getSubalterno());
								listaC340Planimetrie.add(pCompl);
							}
						}
					}

					if(listaC340Planimetrie.size() == 0){
						logger.error("Nessun file planimetrico trovato");
					} else {
						logger.debug("Rilevati [num:"+listaC340Planimetrie.size()+"] file planimetrici.");
					}
				}
			} else {
				logger.error("Percorso Planimetrie Comma 340 non esistente!");
			}
		} else {
			logger.error("Percorso Planimetrie Comma 340 non impostato!");
		}
	}
	
	public DocfaService getDocfaService() {
		return docfaService;
	}

	public void setDocfaService(DocfaService docfaService) {
		this.docfaService = docfaService;
	}
	
	public ParameterService getParameterService() {
		return parameterService;
	}

	public void setParameterService(ParameterService parameterService) {
		this.parameterService = parameterService;
	}
	
	public CatastoService getCatastoService() {
		return catastoService;
	}

	public void setCatastoService(CatastoService catastoService) {
		this.catastoService = catastoService;
	}

	public List<DocfaPlanimetrie> getListaDocfaPlanimetrie() {
		return listaDocfaPlanimetrie;
	}

	public void setListaDocfaPlanimetrie(List<DocfaPlanimetrie> listaDocfaPlanimetrie) {
		this.listaDocfaPlanimetrie = listaDocfaPlanimetrie;
	}

	public List<PlanimetriaComma340DTO> getListaC340Planimetrie() {
		return listaC340Planimetrie;
	}

	public void setListaC340Planimetrie(List<PlanimetriaComma340DTO> listaC340Planimetrie) {
		this.listaC340Planimetrie = listaC340Planimetrie;
	}
	
	public List<DocfaPlanimetrieDatiCensuari> getListaDocfaPlanimetrieUiu() {
		if (listaDocfaPlanimetrieUiu == null) {
			listaDocfaPlanimetrieUiu = new ArrayList<DocfaPlanimetrieDatiCensuari>();
			if (listaDocfaPlanimetrie != null && listaDocfaPlanimetrie.size() > 0) {
				RicercaOggettoDocfaDTO ro = new RicercaOggettoDocfaDTO();			
				ro.setEnteId(super.getEnte());
				ro.setUserId(super.getUsername());			
				ro.setSezione(this.getSezione());
				ro.setFoglio(this.getFoglio());
				ro.setParticella(this.getParticella());
				for (DocfaPlanimetrie planDocfa : listaDocfaPlanimetrie) {
					if (planDocfa.getIdentificativoImmo() != null && planDocfa.getIdentificativoImmo().intValue() != 0) {
						ro.setNomePlan(planDocfa.getId().getNomePlan());
						ro.setProgressivo(planDocfa.getId().getProgressivo());
						listaDocfaPlanimetrieUiu.add(new DocfaPlanimetrieDatiCensuari(planDocfa, docfaService.getDocfaDatiCensuariPerNomePlan(ro)));
					}
				}
			}
		}		
		return listaDocfaPlanimetrieUiu;
	}
	
	public void setListaDocfaPlanimetrieUiu(List<DocfaPlanimetrieDatiCensuari> listaDocfaPlanimetrieUiu) {
		this.listaDocfaPlanimetrieUiu = listaDocfaPlanimetrieUiu;
	}

	public List<DocfaPlanimetrie> getListaDocfaPlanimetrieFab() {
		if (listaDocfaPlanimetrieFab == null) {
			listaDocfaPlanimetrieFab = new ArrayList<DocfaPlanimetrie>();
			if (listaDocfaPlanimetrie != null && listaDocfaPlanimetrie.size() > 0) {
				for (DocfaPlanimetrie planDocfa : listaDocfaPlanimetrie) {
					if (planDocfa.getIdentificativoImmo() == null || planDocfa.getIdentificativoImmo().intValue() == 0) {
						listaDocfaPlanimetrieFab.add(planDocfa);
					}
				}
			}
		}
		return listaDocfaPlanimetrieFab;
	}
	
	public void setListaDocfaPlanimetrieFab(List<DocfaPlanimetrie> listaDocfaPlanimetrieFab) {
		this.listaDocfaPlanimetrieFab = listaDocfaPlanimetrieFab;
	}

	public List<PlanimetriaComma340DTO> getListaC340PlanimetrieUiu() {
		if (listaC340PlanimetrieUiu == null) {
			listaC340PlanimetrieUiu = new ArrayList<PlanimetriaComma340DTO>();
			if (listaC340Planimetrie != null && listaC340Planimetrie.size() > 0) {
				for (PlanimetriaComma340DTO planC340 : listaC340Planimetrie) {
					if (!planC340.getSubalterno().equals("9999")) {
						listaC340PlanimetrieUiu.add(planC340);
					}
				}
			}
		}		
		return listaC340PlanimetrieUiu;
	}

	public void setListaC340PlanimetrieUiu(List<PlanimetriaComma340DTO> listaC340PlanimetrieUiu) {
		this.listaC340PlanimetrieUiu = listaC340PlanimetrieUiu;
	}

	public List<PlanimetriaComma340DTO> getListaC340PlanimetrieFab() {
		if (listaC340PlanimetrieFab == null) {
			listaC340PlanimetrieFab = new ArrayList<PlanimetriaComma340DTO>();
			if (listaC340Planimetrie != null && listaC340Planimetrie.size() > 0) {
				for (PlanimetriaComma340DTO planC340 : listaC340Planimetrie) {
					if (planC340.getSubalterno().equals("9999")) {
						listaC340PlanimetrieFab.add(planC340);
					}
				}
			}
		}
		return listaC340PlanimetrieFab;
	}	
	
	public void setListaC340PlanimetrieFab(List<PlanimetriaComma340DTO> listaC340PlanimetrieFab) {
		this.listaC340PlanimetrieFab = listaC340PlanimetrieFab;
	}

	public String getNomePlan() {
		return nomePlan;
	}

	public void setNomePlan(String nomePlan) {
		this.nomePlan = nomePlan;
	}

	public String getPadProgressivo() {
		return padProgressivo;
	}

	public void setPadProgressivo(String padProgressivo) {
		this.padProgressivo = padProgressivo;
	}

	public String getFornituraStr() {
		return fornituraStr;
	}

	public void setFornituraStr(String fornituraStr) {
		this.fornituraStr = fornituraStr;
	}

	public String getFormato() {
		return formato;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}

	public boolean isWatermark() {
		return watermark;
	}

	public void setWatermark(boolean watermark) {
		this.watermark = watermark;
	}

	public boolean isOpenJpg() {
		return openJpg;
	}

	public void setOpenJpg(boolean openJpg) {
		this.openJpg = openJpg;
	}

	public String getTipoPlan() {
		return tipoPlan;
	}

	public void setTipoPlan(String tipoPlan) {
		this.tipoPlan = tipoPlan;
	}

	public String getLinkPlan() {
		return linkPlan;
	}

	public void setLinkPlan(String linkPlan) {
		this.linkPlan = linkPlan;
	}

	public String getTipoPlanDocfa() {
		return "DOCFA";
	}

	public String getTipoPlanC340() {
		return "C340";
	}

	public boolean isViewNoWatermark() {
		return PermessiHandler.controlla(super.getCeTUser(), PermessiHandler.PERMESSO_ELIMINA_WATERMARK);
	}
	
	protected class PrefissoComma340Filter implements FilenameFilter {

		private String prefisso;

		public PrefissoComma340Filter(String prefisso) {
			this.prefisso = prefisso;
		}

		public boolean accept(File dir, String name) {
			return name.startsWith(prefisso + ".") || name.startsWith(prefisso + "_");
		}
	}

}
