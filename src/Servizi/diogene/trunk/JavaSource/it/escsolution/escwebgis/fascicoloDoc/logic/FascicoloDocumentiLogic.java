package it.escsolution.escwebgis.fascicoloDoc.logic;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.fascicoloDoc.bean.DocfaPlanimetrieDatiCensuari;
import it.escsolution.escwebgis.fascicoloDoc.bean.FascicoloDocumentiFinder;
import it.escsolution.escwebgis.fascicoloDoc.bean.PlanimetriaComma340ExtDTO;
import it.webred.DecodificaPermessi;
import it.webred.cet.permission.GestionePermessi;
import it.webred.ct.config.model.AmKeyValueExt;
import it.webred.ct.config.parameters.ParameterService;
import it.webred.ct.config.parameters.dto.ParameterSearchCriteria;
import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.PlanimetriaComma340DTO;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.docfa.DocfaService;
import it.webred.ct.data.access.basic.docfa.dto.RicercaOggettoDocfaDTO;
import it.webred.ct.data.model.catasto.Sitiuiu;
import it.webred.ct.data.model.docfa.DocfaDatiCensuari;
import it.webred.ct.data.model.docfa.DocfaPlanimetrie;

public class FascicoloDocumentiLogic extends EscLogic {
	
	private String appoggioDataSource;
	
	public static final String ERROR = "FascicoloDocumentiLogic@ERROR";
	public static final String CHIAVE = "FascicoloDocumentiLogic@CHIAVE";	
	public static final String LISTA_DOCFA_PLANIMETRIE_UIU = "FascicoloDocumentiLogic@listaDocfaPlanimetrieUiu";
	public static final String LISTA_DOCFA_PLANIMETRIE_FAB = "FascicoloDocumentiLogic@listaDocfaPlanimetrieFab";
	public static final String LISTA_C340_PLANIMETRIE_UIU = "FascicoloDocumentiLogic@listaC340PlanimetrieUiu";
	public static final String LISTA_C340_PLANIMETRIE_FAB = "FascicoloDocumentiLogic@listaC340PlanimetrieFab";
	public static final String VIEW_NO_WATERMARK = "FascicoloDocumentiLogic@listaC340viewNoWatermark";
	
	public final static String FINDER = "FINDER136";
	
	public static final String DIR_PLANIMETRIE = "planimetrie";
	public static final String DIR_PLANIMETRIE_COMMA_340 = "planimetrieComma340";
	
	public static final String TIPO_PLAN_DOCFA = "DOCFA";
	public static final String TIPO_PLAN_C340 = "C340";
	
	public FascicoloDocumentiLogic(EnvUtente eu) {
		super(eu);
		appoggioDataSource = eu.getDataSource();
	}

	private static final Logger log = Logger.getLogger(FascicoloDocumentiLogic.class.getName());
	
	
	public Hashtable mCaricareDettaglio(String chiave, FascicoloDocumentiFinder finder) throws SQLException {
		
		Hashtable ht = new Hashtable();
		
		finder.setTotaleRecordFiltrati(1);
		// pagine totali
		finder.setPagineTotali(1);
		finder.setTotaleRecord(1);
		finder.setRighePerPagina(1);

		ht.put(FINDER, finder);
		ht.put(CHIAVE, chiave);
		
		EnvUtente eu = this.getEnvUtente();
		String enteId = eu.getEnte();
		String userId = eu.getUtente().getUsername();
		String sessionId = eu.getUtente().getSessionId();
		String[] arrChiave = chiave.split("\\|");
		String sezione = arrChiave[0].trim().equals("") ? null : arrChiave[0].trim();
		String foglio = arrChiave[1].trim();
		String particella = arrChiave[2].trim();
		
		DocfaService docfaService = (DocfaService)getEjb("CT_Service", "CT_Service_Data_Access", "DocfaServiceBean");
		CatastoService catastoService = (CatastoService)getEjb("CT_Service", "CT_Service_Data_Access", "CatastoServiceBean");
		
		RicercaOggettoDocfaDTO roD = new RicercaOggettoDocfaDTO();
		roD.setEnteId(enteId);
		roD.setUserId(userId);
		roD.setSessionId(sessionId);
		roD.setSezione(sezione);
		roD.setFoglio(foglio);
		roD.setParticella(particella);
		List<DocfaPlanimetrie> listaDocfaPlanimetrie = docfaService.getPlanimetriePerSezFglNum(roD);
		
		ht.put(LISTA_DOCFA_PLANIMETRIE_UIU, getListaDocfaPlanimetrieUiu(listaDocfaPlanimetrie, docfaService, roD));
		ht.put(LISTA_DOCFA_PLANIMETRIE_FAB, getListaDocfaPlanimetrieFab(listaDocfaPlanimetrie, roD));
		
		RicercaOggettoCatDTO roC = new RicercaOggettoCatDTO();
		roC.setEnteId(enteId);
		roC.setUserId(userId);
		roC.setSessionId(sessionId);
		roC.setSezione(sezione);
		roC.setFoglio(foglio);
		roC.setParticella(particella);		
		List<PlanimetriaComma340DTO> lista = catastoService.getPlanimetrieComma340SezFglNum(roC);
		//Elabora i percorsi delle planimetrie
		List<PlanimetriaComma340DTO> listaC340Planimetrie = findFilePlanimetrie(lista);

		ht.put(LISTA_C340_PLANIMETRIE_UIU, getListaC340PlanimetrieUiu(listaC340Planimetrie, roC, catastoService));
		ht.put(LISTA_C340_PLANIMETRIE_FAB, getListaC340PlanimetrieFab(listaC340Planimetrie));
		
		ht.put(VIEW_NO_WATERMARK, GestionePermessi.autorizzato(eu.getUtente(), eu.getNomeIstanza(), DecodificaPermessi.ITEM_VISUALIZZA_FONTI_DATI, DecodificaPermessi.PERMESSO_ELIMINA_WATERMARK, true));
		
		/*INIZIO AUDIT*/
		try {
			Object[] arguments = new Object[2];
			arguments[0] = chiave;
			arguments[1] = finder;
			writeAudit(Thread.currentThread().getStackTrace()[1], arguments, ht);
		} catch (Throwable e) {				
			log.debug("ERRORE nella scrittura dell'audit", e);
		}
		/*FINE AUDIT*/
		
		return ht;		
	}
	
	protected List<PlanimetriaComma340DTO> findFilePlanimetrie(List<PlanimetriaComma340DTO> planimC340_parziali) {
		
		List<PlanimetriaComma340DTO> listaC340Planimetrie = new ArrayList<PlanimetriaComma340DTO>();
		
		ParameterSearchCriteria criteria = new ParameterSearchCriteria();
		criteria.setKey("dir.files.datiDiogene");
		ParameterService parameterService = (ParameterService) getEjb("CT_Service", "CT_Config_Manager", "ParameterBaseService");		
		AmKeyValueExt amKey=parameterService.getAmKeyValueExt(criteria);
		String pathDatiDiogene = amKey.getValueConf();
		pathDatiDiogene = pathDatiDiogene.replace("/", File.separator).replace("\\", File.separator);
		if (pathDatiDiogene.endsWith(File.separator)) {
			pathDatiDiogene = pathDatiDiogene.substring(0, pathDatiDiogene.lastIndexOf(File.separator));
		}
		String pathPlanimetrieC340 = pathDatiDiogene + File.separator + envUtente.getEnte() + File.separator + DIR_PLANIMETRIE_COMMA_340;
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
				}
			} else {
				log.error("Percorso Planimetrie Comma 340 non esistente!");
			}
		} else {
			log.error("Percorso Planimetrie Comma 340 non impostato!");
		}
		return listaC340Planimetrie;
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
	
	protected List<DocfaPlanimetrieDatiCensuari> getListaDocfaPlanimetrieUiu(List<DocfaPlanimetrie> listaDocfaPlanimetrie, DocfaService docfaService, RicercaOggettoDocfaDTO roD) {
		List<DocfaPlanimetrieDatiCensuari> listaDocfaPlanimetrieUiu = new ArrayList<DocfaPlanimetrieDatiCensuari>();
		if (listaDocfaPlanimetrie != null && listaDocfaPlanimetrie.size() > 0) {
			for (DocfaPlanimetrie planDocfa : listaDocfaPlanimetrie) {
				if (planDocfa.getIdentificativoImmo() != null && planDocfa.getIdentificativoImmo().intValue() != 0) {
					roD.setNomePlan(planDocfa.getId().getNomePlan());
					roD.setProgressivo(planDocfa.getId().getProgressivo());
					DocfaPlanimetrieDatiCensuari dpdc = new DocfaPlanimetrieDatiCensuari(planDocfa, docfaService.getDocfaDatiCensuariPerNomePlan(roD));
					setLastSit(dpdc, roD);
					listaDocfaPlanimetrieUiu.add(dpdc);
				}
			}
		}
		
		//ordinamento per subalterno - progressivo
		Comparator<DocfaPlanimetrieDatiCensuari> comp = new Comparator<DocfaPlanimetrieDatiCensuari>() {
			public int compare(DocfaPlanimetrieDatiCensuari o1, DocfaPlanimetrieDatiCensuari o2) {
				if (o1 == null || o1.getSubalternoStr() == null || o1.getSubalternoStr().equals("") || o1.getSubalternoStr().equals("-")) {
					if (o2 == null || o2.getSubalternoStr() == null || o2.getSubalternoStr().equals("") || o2.getSubalternoStr().equals("-"))
						return 0;
					else
						return -1;
				}
				else if (o2 == null || o2.getSubalternoStr() == null || o2.getSubalternoStr().equals("") || o2.getSubalternoStr().equals("-")) {
					return 1;
				} else {
					int retVal = o1.getSubalternoStr().compareTo(o2.getSubalternoStr());
					if (retVal == 0) {
						if (o1.getDocfaPlanimetrie() == null || o1.getDocfaPlanimetrie().getId() == null || o1.getDocfaPlanimetrie().getId().getProgressivo() == null) {
							if (o2.getDocfaPlanimetrie() == null || o2.getDocfaPlanimetrie().getId() == null || o2.getDocfaPlanimetrie().getId().getProgressivo() == null)
								return 0;
							else
								return -1;
						}
						else if (o2.getDocfaPlanimetrie() == null || o2.getDocfaPlanimetrie().getId() == null || o2.getDocfaPlanimetrie().getId().getProgressivo() == null) {
							return 1;
						} else {
							return o1.getDocfaPlanimetrie().getId().getProgressivo().compareTo(o2.getDocfaPlanimetrie().getId().getProgressivo());
						}
					} else return retVal;
				}
			}
		};		
		Collections.sort(listaDocfaPlanimetrieUiu, comp);
		
		return listaDocfaPlanimetrieUiu;
	}
	
	protected List<DocfaPlanimetrieDatiCensuari> getListaDocfaPlanimetrieFab(List<DocfaPlanimetrie> listaDocfaPlanimetrie, RicercaOggettoDocfaDTO roD) {
		List<DocfaPlanimetrieDatiCensuari> listaDocfaPlanimetrieFab = new ArrayList<DocfaPlanimetrieDatiCensuari>();
		if (listaDocfaPlanimetrie != null && listaDocfaPlanimetrie.size() > 0) {
			for (DocfaPlanimetrie planDocfa : listaDocfaPlanimetrie) {
				if (planDocfa.getIdentificativoImmo() == null || planDocfa.getIdentificativoImmo().intValue() == 0) {
					DocfaPlanimetrieDatiCensuari dpdc = new DocfaPlanimetrieDatiCensuari(planDocfa, new ArrayList<DocfaDatiCensuari>());
					setLastSit(dpdc, roD);
					listaDocfaPlanimetrieFab.add(dpdc);
				}
			}
		}
		return listaDocfaPlanimetrieFab;
	}
	
	protected List<PlanimetriaComma340ExtDTO> getListaC340PlanimetrieUiu(List<PlanimetriaComma340DTO> listaC340Planimetrie, RicercaOggettoCatDTO roC, CatastoService catastoService) {
		List<PlanimetriaComma340ExtDTO> listaC340PlanimetrieUiu = new ArrayList<PlanimetriaComma340ExtDTO>();
		if (listaC340Planimetrie != null && listaC340Planimetrie.size() > 0) {
			for (PlanimetriaComma340DTO planC340 : listaC340Planimetrie) {
				if (!planC340.getSubalterno().equals("9999")) {
					
					roC.setUnimm(planC340.getSubalterno());
					List<Object[]> immobili = catastoService.getListaImmobiliByParams(roC);
					
					ArrayList<String> categorie = new ArrayList<String>();
					
					for (Object[] objs : immobili) {
						Sitiuiu uiu =  (Sitiuiu)objs[0];
						String myCategoria = uiu.getCategoria();
						boolean add = myCategoria != null && !myCategoria.equals(" ") && !myCategoria.equals("-");
						if (add) {
							for (String categoria : categorie) {
								if (myCategoria.equals(categoria)) {
									add = false;
									break;
								}
							}
						}						
						if (add) {
							categorie.add(myCategoria);
						}
					}
					String categoriaStr = "";
					if (categorie.size() == 0) {
						categoriaStr = "-";
					} else {
						for (String categoria : categorie) {
							if (!categoriaStr.equals("")) {
								categoriaStr += " - ";
							}
							categoriaStr += categoria;
						}
					}					
					
					listaC340PlanimetrieUiu.add(new PlanimetriaComma340ExtDTO(planC340, categoriaStr));
				}
			}
		}
		
		//ordinamento per subalterno - nome file
		Comparator<PlanimetriaComma340ExtDTO> comp = new Comparator<PlanimetriaComma340ExtDTO>() {
			public int compare(PlanimetriaComma340ExtDTO o1, PlanimetriaComma340ExtDTO o2) {
				if (o1 == null || o1.getPlanimetriaComma340() == null || 
				o1.getPlanimetriaComma340().getSubalterno() == null || o1.getPlanimetriaComma340().getSubalterno().equals("") || o1.getPlanimetriaComma340().getSubalterno().equals("-")) {
					if (o2 == null || o2.getPlanimetriaComma340() == null || 
					o2.getPlanimetriaComma340().getSubalterno() == null || o2.getPlanimetriaComma340().getSubalterno().equals("") || o2.getPlanimetriaComma340().getSubalterno().equals("-"))
						return 0;
					else
						return -1;
				}
				else if (o2 == null || o2.getPlanimetriaComma340() == null || 
				o2.getPlanimetriaComma340().getSubalterno() == null || o2.getPlanimetriaComma340().getSubalterno().equals("") || o2.getPlanimetriaComma340().getSubalterno().equals("-")) {
					return 1;
				} else {
					int retVal = o1.getPlanimetriaComma340().getSubalterno().compareTo(o2.getPlanimetriaComma340().getSubalterno());
					if (retVal == 0) {
						if (o1.getPlanimetriaComma340().getFile() == null || o1.getPlanimetriaComma340().getFile().equals("") || o1.getPlanimetriaComma340().getFile().equals("-")) {
							if (o2.getPlanimetriaComma340().getFile() == null || o2.getPlanimetriaComma340().getFile().equals("") || o2.getPlanimetriaComma340().getFile().equals("-"))
								return 0;
							else
								return -1;
						}
						else if (o2.getPlanimetriaComma340().getFile() == null || o2.getPlanimetriaComma340().getFile().equals("") || o2.getPlanimetriaComma340().getFile().equals("-")) {
							return 1;
						} else {
							return o1.getPlanimetriaComma340().getFile().compareTo(o2.getPlanimetriaComma340().getFile());
						}
					} else return retVal;
				}
			}
		};		
		Collections.sort(listaC340PlanimetrieUiu, comp);
		
		return listaC340PlanimetrieUiu;
	}
	
	protected List<PlanimetriaComma340DTO> getListaC340PlanimetrieFab(List<PlanimetriaComma340DTO> listaC340Planimetrie) {
		List<PlanimetriaComma340DTO> listaC340PlanimetrieFab = new ArrayList<PlanimetriaComma340DTO>();
		if (listaC340Planimetrie != null && listaC340Planimetrie.size() > 0) {
			for (PlanimetriaComma340DTO planC340 : listaC340Planimetrie) {
				if (planC340.getSubalterno().equals("9999")) {
					listaC340PlanimetrieFab.add(planC340);
				}
			}
		}
		return listaC340PlanimetrieFab;
	}
	
	protected void setLastSit(DocfaPlanimetrieDatiCensuari dpdc, RicercaOggettoDocfaDTO roD) {
		if (dpdc.getDocfaPlanimetrie() == null || dpdc.getDocfaPlanimetrie().getFornitura() == null) {
			dpdc.setLastSit(false);
			return;
		}
		if (dpdc.getDocfaPlanimetrie().getIdentificativoImmo() != null && dpdc.getDocfaPlanimetrie().getIdentificativoImmo().intValue() != 0) {
			String sub = dpdc.getSubalternoStr();
			if (sub == null || sub.equals(" ") || sub.equals("-")) {
				dpdc.setLastSit(false);
				return;
			}
			if (sub.indexOf("-") > -1) {
				sub = sub.substring(0, sub.indexOf("-")).trim();
			}
			roD.setUnimm(sub);
		} else {
			roD.setUnimm("9999");
		}
		DocfaService docfaService = (DocfaService)getEjb("CT_Service", "CT_Service_Data_Access", "DocfaServiceBean");
		Date[] lastSit = docfaService.getLastSitPlanimetrie(roD);
		dpdc.setLastSit(lastSit != null
						&& lastSit[0].getTime() == dpdc.getDocfaPlanimetrie().getFornitura().getTime()
						&& lastSit[1].getTime() == dpdc.getDocfaPlanimetrie().getDataRegistrazione().getTime());
	}

}
