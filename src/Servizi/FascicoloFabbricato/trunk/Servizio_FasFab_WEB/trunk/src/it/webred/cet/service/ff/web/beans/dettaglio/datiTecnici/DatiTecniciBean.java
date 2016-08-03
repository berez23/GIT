package it.webred.cet.service.ff.web.beans.dettaglio.datiTecnici;

import it.webred.cet.service.ff.web.beans.dettaglio.DatiDettaglio;
import it.webred.cet.service.ff.web.beans.dettaglio.datiCartografia.DatiCartografiaBean;
import it.webred.cet.service.ff.web.util.PermessiHandler;
import it.webred.cet.service.ff.web.util.TiffUtill;
import it.webred.cet.service.ff.web.util.Utility;
import it.webred.ct.config.parameters.ParameterService;
import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.IndirizzoDTO;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.common.dto.RicercaOggettoDTO;
import it.webred.ct.data.access.basic.concedilizie.ConcVisuraDTO;
import it.webred.ct.data.access.basic.concedilizie.ConcessioniEdilizieService;
import it.webred.ct.data.access.basic.concedilizie.dto.ConcessioneDTO;
import it.webred.ct.data.access.basic.concedilizie.dto.IndirizzoConcessioneDTO;
import it.webred.ct.data.access.basic.concedilizie.dto.RicercaConcEdilizieDTO;
import it.webred.ct.data.access.basic.concedilizie.dto.SoggettoConcessioneDTO;
import it.webred.ct.data.access.basic.condono.CondonoService;
import it.webred.ct.data.access.basic.condono.dto.RicercaCondonoDTO;
import it.webred.ct.data.access.basic.datitecnicifabbricato.DatiTecniciFabbricatoService;
import it.webred.ct.data.access.basic.datitecnicifabbricato.dto.RicercaDatiTecniciDTO;
import it.webred.ct.data.access.basic.pgt.PGTService;
import it.webred.ct.data.access.basic.pgt.dto.CataloghiDataIn;
import it.webred.ct.data.access.basic.pgt.dto.DatoPgtDTO;
import it.webred.ct.data.access.basic.pgt.dto.RicercaPgtDTO;
import it.webred.ct.data.model.condono.CondonoPratica;
import it.webred.ct.data.model.datitecnicifabbricato.CertificazioneEnergetica;
import it.webred.ct.data.model.datitecnicifabbricato.CollaudoStatico;
import it.webred.ct.data.model.datitecnicifabbricato.DichiarazioneConformita;
import it.webred.ct.data.model.datitecnicifabbricato.DocumentiTecniciFabbricato;
import it.webred.ct.data.model.pgt.PgtSqlLayer;
import it.webred.ct.data.model.pgt.PgtSqlVisLayer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;

public class DatiTecniciBean extends DatiDettaglio implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private ConcessioniEdilizieService concessioniEdilizieService;
	private PGTService pgtService;
	private ParameterService parameterService;
	private DatiTecniciFabbricatoService datiTecniciFabbricatoService;
	private CondonoService condonoService; 
	
	private boolean permDocConcVisura;
	private String selIdConcVisura;
	private ConcVisuraDTO selConcVisura;
	private List<ConcVisuraDTO> listaConcVisure;
	private List<ConcessioneDTO> listaConcessioni = new ArrayList<ConcessioneDTO>();
	private List<ConcessioneDTO> listaConcEdiCivici = new ArrayList<ConcessioneDTO>(); 
	private List<TipologiaPRG> listaPRG = new ArrayList<TipologiaPRG>();
	private List<String> listaPratiche = new ArrayList<String>();
	private List<CondonoPratica> listaCondono = new ArrayList<CondonoPratica>();
	private CondonoPratica condonoPratica = new CondonoPratica();
	
	private List<CertificazioneEnergetica> listaCertEnergetiche = new ArrayList<CertificazioneEnergetica>();
	private List<DichiarazioneConformita> listaDichConform = new ArrayList<DichiarazioneConformita>();
	private List<CollaudoStatico> listaCollaudoStatico = new ArrayList<CollaudoStatico>();
	private List<DocumentiTecniciFabbricato> listaDocsTecnici = new ArrayList<DocumentiTecniciFabbricato>();

	private String idDati;
	private String tipoDati;
	private String nomeFileDoc;
	private String dataProtocollo;
	private String numeroProtocollo;
	private String dataScadProtocollo;
	
	private String titoloPanelDocs;
	
	private String codiceCondonoPratica;
	
	private boolean showLabelFPS;
	
	private List<IndirizzoDTO> listaIndCatastali;
	private CertificazioneEnergetica cenedCur = null;
	
	public List<ConcessioneDTO> getListaConcessioni() {
		return listaConcessioni;
	}
	public void setListaConcessioni(List<ConcessioneDTO> listaConcessioni) {
		this.listaConcessioni = listaConcessioni;
	}
	public List<TipologiaPRG> getListaPRG() {
		return listaPRG;
	}
	public void setListaPRG(List<TipologiaPRG> listaPRG) {
		this.listaPRG = listaPRG;
	}
	public List<String> getListaPratiche() {
		return listaPratiche;
	}
	public void setListaPratiche(List<String> listaPratiche) {
		this.listaPratiche = listaPratiche;
	}
	public List<ConcVisuraDTO> getListaConcVisure() {
		return listaConcVisure;
	}
	public void setListaConcVisure(List<ConcVisuraDTO> listaConcVisure) {
		this.listaConcVisure = listaConcVisure;
	}
	public List<CertificazioneEnergetica> getListaCertEnergetiche() {
		return listaCertEnergetiche;
	}
	public void setListaCertEnergetiche(
			List<CertificazioneEnergetica> listaCertEnergetiche) {
		this.listaCertEnergetiche = listaCertEnergetiche;
	}
	public List<CollaudoStatico> getListaCollaudoStatico() {
		return listaCollaudoStatico;
	}
	public void setListaCollaudoStatico(List<CollaudoStatico> listaCollaudoStatico) {
		this.listaCollaudoStatico = listaCollaudoStatico;
	}
	public List<DichiarazioneConformita> getListaDichConform() {
		return listaDichConform;
	}
	public void setListaDichConform(List<DichiarazioneConformita> listaDichConform) {
		this.listaDichConform = listaDichConform;
	}
	public List<DocumentiTecniciFabbricato> getListaDocsTecnici() {
		return listaDocsTecnici;
	}
	public void setListaDocsTecnici(
			List<DocumentiTecniciFabbricato> listaDocsTecnici) {
		this.listaDocsTecnici = listaDocsTecnici;
	}
	public List<CondonoPratica> getListaCondono() {
		return listaCondono;
	}
	public void setListaCondono(List<CondonoPratica> listaCondono) {
		this.listaCondono = listaCondono;
	}
	public CondonoPratica getCondonoPratica() {
		return condonoPratica;
	}
	public void setCondonoPratica(CondonoPratica condonoPratica) {
		this.condonoPratica = condonoPratica;
	}
	public String getIdDati() {
		return idDati;
	}
	public void setIdDati(String idDati) {
		this.idDati = idDati;
	}
	public String getTipoDati() {
		return tipoDati;
	}
	public void setTipoDati(String tipoDati) {
		this.tipoDati = tipoDati;
	}
	public void setNomeFileDoc(String nomeFileDoc) {
		this.nomeFileDoc = nomeFileDoc;
	}
	public String getNomeFileDoc() {
		return nomeFileDoc;
	}
	public String getDataProtocollo() {
		return dataProtocollo;
	}
	public void setDataProtocollo(String dataProtocollo) {
		this.dataProtocollo = dataProtocollo;
	}
	public String getNumeroProtocollo() {
		return numeroProtocollo;
	}
	public void setNumeroProtocollo(String numeroProtocollo) {
		this.numeroProtocollo = numeroProtocollo;
	}
	public void setDataScadProtocollo(String dataScadProtocollo) {
		this.dataScadProtocollo = dataScadProtocollo;
	}
	public String getDataScadProtocollo() {
		return dataScadProtocollo;
	}
	public void setTitoloPanelDocs(String titoloPanelDocs) {
		this.titoloPanelDocs = titoloPanelDocs;
	}
	public String getTitoloPanelDocs() {
		return titoloPanelDocs;
	}
	public void setCodiceCondonoPratica(String codiceCondonoPratica) {
		this.codiceCondonoPratica = codiceCondonoPratica;
	}
	public String getCodiceCondonoPratica() {
		return codiceCondonoPratica;
	}

	
	public void doSwitch()
	{
		logger.debug("DATI TECNICI-DATA RIF ["+super.getDataRifStr()!=null+"]");
		
		/*24-10-2013 - La visualizzazione delle informazioni dei cataloghi è stata spostata nel pannello DATI CARTOGRAFICI*/
		//this.loadPRG();
		
		RicercaOggettoCatDTO ro = new RicercaOggettoCatDTO();
		ro.setSezione(super.getSezione());
		ro.setFoglio(super.getFoglio());
		ro.setParticella(super.getParticella());
		ro.setEnteId(super.getEnte());
		ro.setUserId(super.getUsername());
		ro.setDtVal(super.getDataRif());
		
		listaConcessioni =concessioniEdilizieService.getDatiConcessioniByFabbricato(ro);		
		listaConcessioni = distinctAndOrderConc(listaConcessioni, "listaConcessioni");
		
		listaCertEnergetiche = datiTecniciFabbricatoService.getListaCertificazioniEnergeticheByFP(ro);
	
		listaDichConform = datiTecniciFabbricatoService.getListaDichiarazioneConformitaByFP(ro);

		listaCollaudoStatico = datiTecniciFabbricatoService.getListaCollaudoStaticoByFP(ro);
		
		listaConcVisure = concessioniEdilizieService.getVisureCiviciDelFabbricato(ro);
		listaConcVisure = distinctAndOrderConcVis(listaConcVisure);
		
		logger.debug("listaConcessioni size "+listaConcessioni.size());

		logger.debug("listaConcVisure size "+listaConcVisure.size());
		
		//TODO Permesso visualizzazione senza watermark
		
		RicercaCondonoDTO rc = new RicercaCondonoDTO();
		rc.setFoglio(new BigDecimal(super.getFoglio()));
		rc.setParticella(super.getParticella());
		rc.setDtRiferimento(super.getDataRif());
		rc.setEnteId(super.getEnte());
		rc.setUserId(super.getUsername());
		
		listaCondono = condonoService.getCondoni(rc);

	}
	
	public void setConcessioniEdilizieService(ConcessioniEdilizieService concessioniEdilizieService) {
		this.concessioniEdilizieService = concessioniEdilizieService;
	}
	public ConcessioniEdilizieService getConcessioniEdilizieService() {
		return concessioniEdilizieService;
	}
	public PGTService getPgtService() {
		return pgtService;
	}
	public void setPgtService(PGTService pgtService) {
		this.pgtService = pgtService;
	}
	public ParameterService getParameterService() {
		return parameterService;
	}
	public void setParameterService(ParameterService parameterService) {
		this.parameterService = parameterService;
	}
	public void setDatiTecniciFabbricatoService(
			DatiTecniciFabbricatoService datiTecniciFabbricatoService) {
		this.datiTecniciFabbricatoService = datiTecniciFabbricatoService;
	}
	public DatiTecniciFabbricatoService getDatiTecniciFabbricatoService() {
		return datiTecniciFabbricatoService;
	}
	public void setCondonoService(CondonoService condonoService) {
		this.condonoService = condonoService;
	}
	public CondonoService getCondonoService() {
		return condonoService;
	}

	private void loadPRG()
	{
		listaPRG.clear();
		
		DatiCartografiaBean datCart = (DatiCartografiaBean)super.getBeanReference("datiCartografiaBean");
		datCart.setSezione(this.getSezione());
		datCart.setFoglio(this.getFoglio());
		datCart.setParticella(this.getParticella());
		datCart.setDataRif(this.getDataRif());
		
		Utility utility = new Utility(parameterService);
		String tipologie = utility.getTipiLayers();

		if (tipologie!= null && !tipologie.equals(""))
		{
			String[] listaTipologie = tipologie.split(";");
			for(String descrizioneTipologia:listaTipologie)
			{
				logger.debug("TIPOLOGIA = " + descrizioneTipologia);
				
				RicercaPgtDTO rpTipologia = new RicercaPgtDTO();
				rpTipologia.setTipoLayer(descrizioneTipologia);
				rpTipologia.setEnteId(super.getEnte());
				rpTipologia.setUserId(super.getUsername());
				
				List<PgtSqlLayer> listaLayers = pgtService.getListaLayersByTipo(rpTipologia);
				
			if (listaLayers!=null && listaLayers.size()>0)
				{	
				 	CataloghiDataIn dataIn= new CataloghiDataIn();
					dataIn.setEnteId(super.getEnte());
					dataIn.setUserId(super.getUsername());
					dataIn.setNomeApp("FasFabb");
					for(PgtSqlLayer layer:listaLayers)
					{
	
						logger.debug("layer des " + layer.getDesLayer());
						
						//Recupero la modalità di interrogazione del layer per l'applicativo
						dataIn.setPkLayer(layer.getId());
						PgtSqlVisLayer pgtSqlVis = pgtService.getSqlVisLayerByLayerApp(dataIn);
						
							if(pgtSqlVis!=null && "Y".equalsIgnoreCase(pgtSqlVis.getVisualizza())){
								
								TipologiaPRG tabella = datCart.caricaDatiLayer(layer,pgtSqlVis);
								if(tabella!=null)
									listaPRG.add(tabella);
								
						}
					}
				}
				else
					logger.debug("Lista layers per " + descrizioneTipologia + " VUOTA " );
				
			}
		}
		else
			logger.debug("NESSUNA TIPOLOGIA DA PARAMETRI");
		
	}
	
	
	public void showDocsDownload()
	{
		RicercaDatiTecniciDTO rdt = new RicercaDatiTecniciDTO();
		rdt.setIdDati(new Long (idDati));
		rdt.setTipoDati(tipoDati);
		rdt.setEnteId(super.getEnte());
		rdt.setUserId(super.getUsername());
		
		if(tipoDati.equals("CE"))
			titoloPanelDocs = "Certificazioni energetiche";
		else
		{
			if(tipoDati.equals("CS"))
				titoloPanelDocs = "Collaudi statici";
			else
			{
				if(tipoDati.equals("DC"))
					titoloPanelDocs = "Dichiarazioni Conformita";
			}
		}
		
		logger.debug("showDocsDownload START getListaCollaudoStaticoByFP");
		listaDocsTecnici = datiTecniciFabbricatoService.getListaDocumentiByIdDati(rdt);
		logger.debug("showDocsDownload END getListaCollaudoStaticoByFP");
		if(listaDocsTecnici==null || listaDocsTecnici.size()==0)
			logger.debug("listaDocsTecnici NULLO");
	}//-------------------------------------------------------------------------
	
	public String showCenedDetail(){
		String esito = "datiTecniciBean.showCenedDetail";
		Long uidCened = new Long (idDati);
		
		System.out.println("Identificativo riga: " + idDati);
		
		cenedCur = datiTecniciFabbricatoService.getCertificazioneEnergeticaById(uidCened);
		
		return esito;
	}//-------------------------------------------------------------------------
	
	public void updateDocs()
	{
		String tipologia = "";

		if(tipoDati.equals("CE"))
			tipologia = super.getGlobalConfig().get("dirCertificazioniEnergetiche");
		else
		{
			if(tipoDati.equals("CS"))
				tipologia = super.getGlobalConfig().get("dirCollaudoStatico");
			else
			{
				if(tipoDati.equals("DC"))
					tipologia = super.getGlobalConfig().get("dirDichiarazioniConformita");
			}
		}
		
		logger.debug(" TIPOLOGIA == " + tipologia);
		String pathCompleto = super.getPathDatiDiogeneEnte(parameterService) + File.separatorChar + tipologia + File.separatorChar + nomeFileDoc +".pdf";
		logger.debug(" PERCORSO FILE = " + pathCompleto);
		

        HttpServletResponse resp = (HttpServletResponse) super.getResponse();  
        resp.setContentType("application/pdf");  
        //String filename = String.format("%012d", new Long(this.idRich)) + ".pdf";  

        resp.addHeader("Content-Disposition", "attachment; filename="  + nomeFileDoc + ".pdf");
        
        OutputStream os = null;
        byte[] buffer = new byte[1024];
        FileInputStream fis = null;
        
        try {
        	os = resp.getOutputStream();
			fis = new FileInputStream(new File(pathCompleto));
			while ( fis.read(buffer) != -1) {
				os.write(buffer);
			}
			
            os.flush();
			
		} catch (Throwable e) {			
			logger.error(e.getMessage(),e);
		}
		finally {
			try {
				os.close();
			}
			catch(Throwable t) {}
			
			try {
				fis.close();
			}
			catch(Throwable t) {}
		}
        
		FacesContext context = FacesContext.getCurrentInstance(); 
		context.responseComplete();		
       

	}
	
	public void showPraticaCondono()
	{
		RicercaCondonoDTO rc = new RicercaCondonoDTO();
		rc.setUserId(super.getUsername());
		rc.setEnteId(super.getEnte());
		rc.setCodiceCondono(codiceCondonoPratica);
		condonoPratica = condonoService.getPraticaCondono(rc);
	}

	private String selIdDocVisura;
	private String selPathDocVisura;
	private boolean watermark ;
	private boolean openJpg ;
	
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

	public String getSelIdDocVisura() {
		return selIdDocVisura;
	}
	public void setSelIdDocVisura(String selIdDocVisura) {
		this.selIdDocVisura = selIdDocVisura;
	}
	public String getSelPathDocVisura() {
		return selPathDocVisura;
	}
	public void setSelPathDocVisura(String selPathDocVisura) {
		this.selPathDocVisura = selPathDocVisura;
	}
	
	public void showOggettiConcessioniCivici(){
			
			logger.debug("DETTAGLIO CONCESSIONI EDILIZIE CIVICI - DATA RIF: ["+super.getDataRifStr()+"]" );
			Utility utility = new Utility(parameterService);
			
			RicercaOggettoCatDTO ro = new RicercaOggettoCatDTO();
			ro.setSezione(super.getSezione());
			ro.setFoglio(super.getFoglio());
			ro.setParticella(super.getParticella());
			ro.setSub(null);
			ro.setDtVal(super.getDataRif());
			
			ro.setEnteId(super.getEnte());
			ro.setUserId(super.getUsername());
			
			listaConcEdiCivici=concessioniEdilizieService.getDatiConcCiviciDelFabbricato(ro);
			listaConcEdiCivici = distinctAndOrderConc(listaConcEdiCivici, "listaConcEdiCivici");
			
			this.loadIndCatastali();
			
			this.showLabelFPS = false;
		
	}
	
	private List<ConcessioneDTO> distinctAndOrderConc(List<ConcessioneDTO> input, String type) {
		if (input == null || input.size() == 0) {
			return input;
		}
		List<ConcessioneDTO> output = new ArrayList<ConcessioneDTO>();
		for (ConcessioneDTO concInput : input) {
			boolean add = true;
			for (ConcessioneDTO concOutput : output) {
				if (eqConc(concInput, concOutput, type)) {
					add = false;
					break;
				}
			}
			if (add) {
				output.add(concInput);
			}
		}
		
		Comparator<ConcessioneDTO> compConc = new Comparator<ConcessioneDTO>() {
			public int compare(ConcessioneDTO o1, ConcessioneDTO o2) {
				if (o1 == null) {
					if (o2 == null)
						return 0;
					else
						return -1;
				}
				else if (o2 == null) {
					return 1;
				}
				else {
					String progAnno1 = o1.getProgAnno();
					String progAnno2 = o2.getProgAnno();
					String progNumero1 = o1.getProgNumero();
					String progNumero2 = o2.getProgNumero();
					if (progAnno1 == null) {
						if (progAnno2 == null) {
							if (progNumero1 == null) {
								if (progNumero2 == null)
									return 0;
								else
									return -1;
							}
							else if (progNumero2 == null) {
								return 1;
							}
							else {
								return progNumero1.trim().compareTo(progNumero2.trim());
							}							
						}
						else
							return -1;
					}
					else if (progAnno2 == null) {
						return 1;
					}
					else {
						int compare = progAnno1.trim().compareTo(progAnno2.trim());
						if (compare == 0) {
							if (progNumero1 == null) {
								if (progNumero2 == null)
									return 0;
								else
									return -1;
							}
							else if (progNumero2 == null) {
								return 1;
							}
							else {
								return progNumero1.trim().compareTo(progNumero2.trim());
							}
						} else return compare;
					}					
				}
			}
		};
		Collections.sort(output, compConc);
		
		return output;
	}
	
	private boolean eqObj(Object o1, Object o2, boolean strTrim, boolean strIgnoreCase, String dateFormat) {
		if (o1 == null) {
			if (o2 == null) {
				return true;
			} else {
				return false;
			}
		} else if (o2 == null) {
			return false;
		}
		
		if (!o1.getClass().getName().equals(o2.getClass().getName())) {
			return false;
		}
		
		if (o1 instanceof String) {
			String s1 = (String)o1;
			String s2 = (String)o2;
			if (strTrim) {
				s1 = s1.trim();
				s2 = s2.trim();
			}
			if (strIgnoreCase) {
				return s1.equalsIgnoreCase(s2);
			} else {
				return s1.equals(s2);
			}
		}
		
		if (o1 instanceof Date) {
			Date d1 = (Date)o1;
			Date d2 = (Date)o2;
			if (dateFormat != null && !dateFormat.equals("")) {
				SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
				return sdf.format(d1).equals(sdf.format(d2));
			} else {
				return d1.getTime() == d2.getTime();
			}
		}
		
		if (o1 instanceof BigDecimal) {
			BigDecimal bd1 = (BigDecimal)o1;
			BigDecimal bd2 = (BigDecimal)o2;
			return bd1.doubleValue() == bd2.doubleValue();
		}
		
		return o1.equals(o2);
	}
	
	private boolean eqConc(ConcessioneDTO c1, ConcessioneDTO c2, String type) {
		String s1 = c1.getProgAnno();
		String s2 = c2.getProgAnno();
		if (!eqObj(s1, s2, true, true, null)) {
			return false;
		}
		
		s1 = c1.getProgNumero();
		s2 = c2.getProgNumero();
		if (!eqObj(s1, s2, true, true, null)) {
			return false;
		}
		
		s1 = c1.getConcNumero();
		s2 = c2.getConcNumero();
		if (!eqObj(s1, s2, true, true, null)) {
			return false;
		}

		Date d1 = c1.getDataProt();
		Date d2 = c2.getDataProt();
		if (!eqObj(d1, d2, false, false, "dd/MM/yyyy")) {
			return false;
		}
		
		d1 = c1.getDataRilascio();
		d2 = c2.getDataRilascio();
		if (!eqObj(d1, d2, false, false, "dd/MM/yyyy")) {
			return false;
		}
		
		d1 = c1.getDataInizioLavori();
		d2 = c2.getDataInizioLavori();
		if (!eqObj(d1, d2, false, false, "dd/MM/yyyy")) {
			return false;
		}
		
		d1 = c1.getDataFineLavori();
		d2 = c2.getDataFineLavori();
		if (!eqObj(d1, d2, false, false, "dd/MM/yyyy")) {
			return false;
		}
		
		s1 = c1.getOggetto();
		s2 = c2.getOggetto();
		if (!eqObj(s1, s2, true, true, null)) {
			return false;
		}
		
		int size1 = 0;
		int size2 = 0;
		if (type.equalsIgnoreCase("listaConcessioni")) {
			size1 = c1.getListaSoggetti() == null ? 0 : c1.getListaSoggetti().size();
			size2 = c2.getListaSoggetti() == null ? 0 : c2.getListaSoggetti().size();
			if (size1 != size2) {
				return false;
			} else if (size1 > 0) {
				for (int i = 0; i < size1; i++) {
					SoggettoConcessioneDTO sogg1 = c1.getListaSoggetti().get(i);
					SoggettoConcessioneDTO sogg2 = c2.getListaSoggetti().get(i);
					
					s1 = sogg1.getTitolo();
					s2 = sogg2.getTitolo();
					if (!eqObj(s1, s2, true, true, null)) {
						return false;
					}
					
					s1 = sogg1.getDatiAnag();
					s2 = sogg2.getDatiAnag();
					if (!eqObj(s1, s2, true, true, null)) {
						return false;
					}
				}
			}
		}
		
		if (type.equalsIgnoreCase("listaConcEdiCivici")) {
			s1 = c1.getListaSoggettiHtml();
			s2 = c2.getListaSoggettiHtml();
			if (!eqObj(s1, s2, true, true, null)) {
				return false;
			}
		}
		
		s1 = c1.getStringaImmobili();
		s2 = c2.getStringaImmobili();
		if (!eqObj(s1, s2, true, true, null)) {
			return false;
		}
		
		if (type.equalsIgnoreCase("listaConcEdiCivici")) {
			size1 = c1.getListaIndirizzi() == null ? 0 : c1.getListaIndirizzi().size();
			size2 = c2.getListaIndirizzi() == null ? 0 : c2.getListaIndirizzi().size();
			if (size1 != size2) {
				return false;
			} else if (size1 > 0) {
				for (int i = 0; i < size1; i++) {
					IndirizzoConcessioneDTO ind1 = c1.getListaIndirizzi().get(i);
					IndirizzoConcessioneDTO ind2 = c2.getListaIndirizzi().get(i);
					
					s1 = ind1.getIndirizzo();
					s2 = ind2.getIndirizzo();
					if (!eqObj(s1, s2, true, true, null)) {
						return false;
					}
					
					s1 = ind1.getCivico();
					s2 = ind2.getCivico();
					if (!eqObj(s1, s2, true, true, null)) {
						return false;
					}
				}
			}
		}

		return true;
	}
	
	private List<ConcVisuraDTO> distinctAndOrderConcVis(List<ConcVisuraDTO> input) {
		if (input == null || input.size() == 0) {
			return input;
		}
		List<ConcVisuraDTO> output = new ArrayList<ConcVisuraDTO>();
		for (ConcVisuraDTO concInput : input) {
			boolean add = true;
			for (ConcVisuraDTO concOutput : output) {
				if (eqConcVis(concInput, concOutput)) {
					add = false;
					break;
				}
			}
			if (add) {
				output.add(concInput);
			}
		}
		
		Comparator<ConcVisuraDTO> compConcVis = new Comparator<ConcVisuraDTO>() {
			public int compare(ConcVisuraDTO o1, ConcVisuraDTO o2) {
				if (o1 == null) {
					if (o2 == null)
						return 0;
					else
						return -1;
				}
				else if (o2 == null) {
					return 1;
				}
				else {
					String tipoAtto1 = o1.getDescTipoAtto() == null ? o1.getTipoAtto() : o1.getDescTipoAtto();
					String tipoAtto2 = o2.getDescTipoAtto() == null ? o2.getTipoAtto() : o2.getDescTipoAtto();
					String numeroAtto1 = o1.getNumeroAtto();
					String numeroAtto2 = o2.getNumeroAtto();
					if (tipoAtto1 == null) {
						if (tipoAtto2 == null) {
							if (numeroAtto1 == null) {
								if (numeroAtto2 == null)
									return 0;
								else
									return -1;
							}
							else if (numeroAtto2 == null) {
								return 1;
							}
							else {
								return numeroAtto1.trim().compareTo(numeroAtto2.trim());
							}							
						}
						else
							return -1;
					}
					else if (tipoAtto2 == null) {
						return 1;
					}
					else {
						int compare = tipoAtto1.trim().compareTo(tipoAtto2.trim());
						if (compare == 0) {
							if (numeroAtto1 == null) {
								if (numeroAtto2 == null)
									return 0;
								else
									return -1;
							}
							else if (numeroAtto2 == null) {
								return 1;
							}
							else {
								return numeroAtto1.trim().compareTo(numeroAtto2.trim());
							}
						} else return compare;
					}					
				}
			}
		};
		Collections.sort(output, compConcVis);
		
		return output;
	}
	
	private boolean eqConcVis(ConcVisuraDTO c1, ConcVisuraDTO c2) {
		String s1 = c1.getDescTipoAtto() == null ? c1.getTipoAtto() : c1.getDescTipoAtto();
		String s2 = c2.getDescTipoAtto() == null ? c2.getTipoAtto() : c2.getDescTipoAtto();
		if (!eqObj(s1, s2, true, true, null)) {
			return false;
		}
		
		s1 = c1.getNumeroAtto();
		s2 = c2.getNumeroAtto();
		if (!eqObj(s1, s2, true, true, null)) {
			return false;
		}
		
		s1 = c1.getNomeIntestatario();
		s2 = c2.getNomeIntestatario();
		if (!eqObj(s1, s2, true, true, null)) {
			return false;
		}
		
		s1 = c1.getPrefisso();
		s2 = c2.getPrefisso();
		if (!eqObj(s1, s2, true, true, null)) {
			return false;
		}
		
		s1 = c1.getNomeVia();
		s2 = c2.getNomeVia();
		if (!eqObj(s1, s2, true, true, null)) {
			return false;
		}
		
		BigDecimal bd1 = c1.getCivico();
		BigDecimal bd2 = c2.getCivico();
		if (!eqObj(bd1, bd2, false, false, null)) {
			return false;
		}
		
		s1 = c1.getAnnoProtGen();
		s2 = c2.getAnnoProtGen();
		if (!eqObj(s1, s2, true, true, null)) {
			return false;
		}
		
		s1 = c1.getNumProtGen();
		s2 = c2.getNumProtGen();
		if (!eqObj(s1, s2, true, true, null)) {
			return false;
		}
		
		s1 = c1.getNumProtSett();
		s2 = c2.getNumProtSett();
		if (!eqObj(s1, s2, true, true, null)) {
			return false;
		}
		
		Date d1 = c1.getDataDoc();
		Date d2 = c2.getDataDoc();
		if (!eqObj(d1, d2, false, false, "dd/MM/yyyy")) {
			return false;
		}
		
		return true;
	}
	
	private void loadIndCatastali(){
		
		//Carico la lista di Indirizzi Catastali
		
			RicercaOggettoCatDTO roc = new RicercaOggettoCatDTO();
			
			roc.setDtVal(super.getDataRif());
			roc.setFoglio(this.getFoglio());
			roc.setParticella(this.getParticella());
			roc.setEnteId(super.getEnte());
			roc.setUserId(super.getUsername());
			roc.setCodEnte(super.getEnte());
			
			try {
			
				CatastoService catastoService = (CatastoService) getEjb("CT_Service", "CT_Service_Data_Access", "CatastoServiceBean");
				
				setListaIndCatastali(catastoService.getLocalizzazioneCatastaleDescr(roc));
			
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}
		
	}
	
	public void showDettaglioVisura(){
		
		RicercaConcEdilizieDTO rc = new RicercaConcEdilizieDTO();
		rc.setEnteId(super.getEnte());
		rc.setUserId(super.getUsername());
		rc.setIdConc(this.selIdConcVisura);
		
		this.selConcVisura = concessioniEdilizieService.getVisuraById(rc);
		permDocConcVisura = PermessiHandler.controlla(super.getCeTUser(), PermessiHandler.PERMESSO_ELIMINA_WATERMARK);;
	}
	
	
	public void showDocumentoVisura(){
		
		InputStream is = null;
		InputStream isByte = null;
		ByteArrayOutputStream baos=null;
		OutputStream out =null;
		String pathFile="";
		
		if(selPathDocVisura!=null && selPathDocVisura.length()>0){
		
		 HttpServletResponse response = (HttpServletResponse) super.getResponse(); 
		 
		 try{
			
			String pathDir = super.getPathDatiDiogeneEnte(parameterService) + File.separatorChar + this.getGlobalConfig().get("dirConcessioniVisure");
			logger.debug("Path Concessioni Visure:" + pathFile);
			pathFile = pathDir + selPathDocVisura;
			logger.debug("Concessioni Visure:" + pathFile);
			File doc = new File(pathFile);
			
			is = new FileInputStream(doc);
			    
			List<ByteArrayOutputStream> jpgs = TiffUtill.tiffToJpeg(is, watermark, openJpg);		
			if (openJpg) {
				baos = jpgs.get(0);
			} else {
				baos =  TiffUtill.jpgsTopdf(jpgs, false, TiffUtill.FORMATO_DEF);
			}				
			isByte = new ByteArrayInputStream(baos.toByteArray());
			is.close();
			
			 String nome = "concessione_visura_" + selIdDocVisura;
			 
			 
			 out = response.getOutputStream();
				if (openJpg && watermark) {
					response.setHeader("Content-Disposition","attachment; filename=\"" + nome + ".jpg" + "\"");
					response.setContentType("image/jpeg");
				} 
				else if (openJpg && !watermark){
					response.setHeader("Content-Disposition","attachment; filename=\"" + nome + ".tiff" + "\"");
					response.setContentType("image/tiff");
				}
				else {
					response.setHeader("Content-Disposition","attachment; filename=\"" + nome + ".pdf" + "\"");
					response.setContentType("application/pdf");
				}			
				
				byte[] b = new byte[1024];
	            while ( isByte.read( b ) != -1 )
	            {
	                out.write( b );
	            }
				

            out.flush();
            
            out.close();
            
            FacesContext context = FacesContext.getCurrentInstance(); 
    		context.responseComplete();		
           
		
		} catch (java.io.FileNotFoundException e) {			
			super.addErrorMessage("ff.file.non.trovato", "");
			logger.error("FILE NON TROVATO: "+ pathFile , e);
		}
		
		catch (Throwable e) {			
			logger.error(e.getMessage(),e);
		}
		 
		}else{
			logger.error("File non selezionato");
		}
	}	
	
	public ConcVisuraDTO getSelConcVisura() {
		return selConcVisura;
	}
	public void setSelConcVisura(ConcVisuraDTO selConcVisura) {
		this.selConcVisura = selConcVisura;
	}
	public static List<String> emptyIfNull(List<String> argList) 
	{
		if(argList==null)
		{
			logger.debug("emptyIfNull NULLO");
			return new ArrayList<String>();
			//return (List<String>)Collections.emptyList();
		}
		else
		{
			logger.debug("emptyIfNull NOT NULLO");
			return argList;
		}
		//return (List<String>) ((argList == null) ? Collections.emptyList() : argList); 
	}
	public String getSelIdConcVisura() {
		return selIdConcVisura;
	}
	public void setSelIdConcVisura(String selIdConcVisura) {
		this.selIdConcVisura = selIdConcVisura;
	}
	public boolean isPermDocConcVisura() {
		return permDocConcVisura;
	}
	public void setPermDocConcVisura(boolean permDocConcVisura) {
		this.permDocConcVisura = permDocConcVisura;
	}
	
	public List<IndirizzoDTO> getListaIndCatastali() {
		return listaIndCatastali;
	}
	public void setListaIndCatastali(List<IndirizzoDTO> listaIndCatastali) {
		this.listaIndCatastali = listaIndCatastali;
	}
	public List<ConcessioneDTO> getListaConcEdiCivici() {
		return listaConcEdiCivici;
	}
	public void setListaConcEdiCivici(List<ConcessioneDTO> listaConcEdiCivici) {
		this.listaConcEdiCivici = listaConcEdiCivici;
	}
	public boolean isShowLabelFPS() {
		return showLabelFPS;
	}
	public void setShowLabelFPS(boolean showLabelFPS) {
		this.showLabelFPS = showLabelFPS;
	}
	public CertificazioneEnergetica getCenedCur() {
		return cenedCur;
	}
	public void setCenedCur(CertificazioneEnergetica cenedCur) {
		this.cenedCur = cenedCur;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
