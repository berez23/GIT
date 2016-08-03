package it.webred.ct.service.muidocfa.web.bean.ici;

import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.diagnostiche.DiagnosticheDataIn;
import it.webred.ct.data.access.basic.diagnostiche.dto.RicercaDocfaReportDTO;
import it.webred.ct.data.access.basic.diagnostiche.dto.RicercaIciTarsuDTO;
import it.webred.ct.data.access.basic.diagnostiche.ici.dto.DocfaIciReportDTO;
import it.webred.ct.data.access.basic.diagnostiche.ici.dto.SitTIciOggettoDTO;
import it.webred.ct.data.access.basic.diagnostiche.ici.dto.UnitaImmobiliareIciDTO;
import it.webred.ct.data.access.basic.docfa.dto.RicercaOggettoDocfaDTO;
import it.webred.ct.data.access.basic.ici.dto.SoggettoIciDTO;
import it.webred.ct.data.model.catasto.Sitideco;
import it.webred.ct.data.model.diagnostiche.DocfaIciReport;
import it.webred.ct.service.muidocfa.web.bean.util.CommonDataBean;
import it.webred.ct.service.muidocfa.web.bean.util.GestioneFileBean;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

public class DetailBean extends GestioneFileBean {

	private static final long serialVersionUID = 1L;
	private String idRep;
	private String idRepUI;
	private String idRepDocfa;
	private String idExtIci;
	private List<SelectItem> listaUI;
	private DocfaIciReport docfa;
	private DocfaIciReportDTO dto;
	private RicercaIciTarsuDTO ricercaIciTarsuDto;
	private UnitaImmobiliareIciDTO selectedUI;
	private List<SitTIciOggettoDTO> listaIci;
	private List<SoggettoIciDTO> listaSoggettiIci;
	private final String PIPE = "|";
	
	

	@PostConstruct
	public void initService() {

		dto = new DocfaIciReportDTO();
		ricercaIciTarsuDto = new RicercaIciTarsuDTO();
	}

	public String goDetail() {

		doCaricaDto();
		doCaricaUnitaImmobiliari();
		return "muidocfa.ici.detail";

	}

	public void doCaricaDto() {
		try {

			DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
			fillEnte(dataIn);
			dataIn.setObj(idRep);
			dto.setDocfaIciReport(docfaIciReportService.getDocfaIciReportById(dataIn));
			dto.setTitolariAnno(docfaIciReportService.getReportSogg(dataIn));
			
			dto.getDocfaIciReport().setDicResNumciv(this.cleanLeftPad(dto.getDocfaIciReport().getDicResNumciv(),'0'));
			
			RicercaOggettoDocfaDTO ricercaDocfaDto = new RicercaOggettoDocfaDTO();
			fillEnte(ricercaDocfaDto);
			ricercaDocfaDto.setFornitura(dto.getDocfaIciReport().getFornitura());
			ricercaDocfaDto.setProtocollo(dto.getDocfaIciReport().getProtocolloDocfa());
			
			dto.setListaIntestati(docfaService.getIntestati(ricercaDocfaDto));

			dto.setCausaleDocfaExt(getCausale(dto.getDocfaIciReport()
					.getCausaleDocfa()));

			doCaricaQuadroRenditeDocfa();
			
		} catch (Throwable t) {
			super.addErrorMessage("lista.error", t.getMessage());
			t.printStackTrace();
		}
	}
	
	public void doCaricaQuadroRenditeDocfa(){
		
		RicercaOggettoDocfaDTO ricercaDocfaDto = new RicercaOggettoDocfaDTO();
		fillEnte(ricercaDocfaDto);
		ricercaDocfaDto.setFornitura(dto.getDocfaIciReport().getFornitura());
		ricercaDocfaDto.setProtocollo(dto.getDocfaIciReport().getProtocolloDocfa());
		//ricercaDocfaDto.setDataRegistrazione(dto.getDocfaIciReport().getDataDocfa());
		ricercaDocfaDto.setTipoOperDocfa("S");
		dto.setQuadroPre(docfaIciReportService.getQuadroRendite(ricercaDocfaDto));
		
		ricercaDocfaDto.setTipoOperDocfa("C");
		dto.setQuadroPost(docfaIciReportService.getQuadroRendite(ricercaDocfaDto));
		
	}

	public void doCaricaUnitaImmobiliari() {

		try {

			listaUI = new ArrayList<SelectItem>();
			RicercaDocfaReportDTO ricerca = new RicercaDocfaReportDTO();
			ricerca.setFornitura(dto.getDocfaIciReport().getFornitura());
			ricerca.setProtocolloDocfa(dto.getDocfaIciReport().getProtocolloDocfa());
		//	ricerca.setDataRegistrazioneDocfa(dto.getDocfaIciReport().getDataDocfaToDate());

			DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
			fillEnte(dataIn);
			dataIn.setRicerca(ricerca);
			List<DocfaIciReport> lista = docfaIciReportService
					.searchReport(dataIn);
			for (int i = 0; i < lista.size(); i++) {

				DocfaIciReport report = lista.get(i);
				UnitaImmobiliareIciDTO uidto = new UnitaImmobiliareIciDTO();
				DocfaIciReportDTO dto = new DocfaIciReportDTO();
				dto.setDocfaIciReport(report);
				uidto.setDocfaIciReportDTO(dto);
				if (idRep.equals(report.getIdRep())) {
					selectedUI = uidto;
					idRepUI = idRep;
					// unità immobiliare selezionata dalla ricerca
					selectedUI = doCaricaUnitaImmobiliare(idRep, false);

				}

				SelectItem si = new SelectItem(report.getIdRep(),
						"Unità immobiliare con Foglio: "
								+ report.getFoglio()
								+ ", Particella: "
								+ report.getParticella()
								+ ", Subalterno: "
								+ (report.getSubalterno() != null ? report.getSubalterno() : "")
								+ " (" +report.getTipoOperDocfaEx()+ ")");
				listaUI.add(si);

			}

		} catch (Throwable t) {
			super.addErrorMessage("lista.error", t.getMessage());
			t.printStackTrace();
		}

	}

	public void doCaricaUnitaImmobiliare() {

		UnitaImmobiliareIciDTO uiDto = doCaricaUnitaImmobiliare(idRepUI, true);
		selectedUI = uiDto;

	}

	public UnitaImmobiliareIciDTO doCaricaUnitaImmobiliare(String idRep, boolean altraUi) {

		UnitaImmobiliareIciDTO uidto = new UnitaImmobiliareIciDTO();
		try {

			if (!altraUi)
				uidto = selectedUI;
			else {
				DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
				fillEnte(dataIn);
				dataIn.setObj(idRep);
				uidto.setDocfaIciReportDTO(new DocfaIciReportDTO());
				uidto.getDocfaIciReportDTO().setDocfaIciReport(
						docfaIciReportService.getDocfaIciReportById(dataIn));
			}

			// descrizione categoria
			uidto.getDocfaIciReportDTO().setCategoriaDocfaExt(
					getCategoria(uidto.getDocfaIciReportDTO()
							.getDocfaIciReport().getCategoriaDocfa()));
			uidto.getDocfaIciReportDTO().setCategoriaCatastoExt(
					getCategoria(uidto.getDocfaIciReportDTO()
							.getDocfaIciReport().getCategoriaCatasto()));

			// formattazione annotazione
			if (uidto.getDocfaIciReportDTO().getDocfaIciReport().getAnnotazioni() != null)
				uidto.getDocfaIciReportDTO().getDocfaIciReport().setAnnotazioni(
								uidto.getDocfaIciReportDTO()
								.getDocfaIciReport().getAnnotazioni().replaceAll("@", ";"));

			// formattazione civici
			if (uidto.getDocfaIciReportDTO().getDocfaIciReport().getDicResNumciv() != null)
				uidto.getDocfaIciReportDTO().getDocfaIciReport().setDicResNumciv(
						this.cleanLeftPad(uidto.getDocfaIciReportDTO().getDocfaIciReport().getDicResNumciv(),'0'));
			
			if (uidto.getDocfaIciReportDTO().getDocfaIciReport().getCiviciDocfa() != null)
				uidto.getDocfaIciReportDTO().getDocfaIciReport().setCiviciDocfa(
								uidto.getDocfaIciReportDTO()
								.getDocfaIciReport().getCiviciDocfa().replace("@", ", "));

			
			// lista altri docfa
			RicercaDocfaReportDTO rs = new RicercaDocfaReportDTO();
			rs.setFoglio(uidto.getDocfaIciReportDTO().getDocfaIciReport()
					.getFoglio());
			rs.setParticella(uidto.getDocfaIciReportDTO().getDocfaIciReport()
					.getParticella());
			if (uidto.getDocfaIciReportDTO().getDocfaIciReport()
					.getSubalterno() != null) {
				rs.setUnimm(uidto.getDocfaIciReportDTO().getDocfaIciReport()
						.getSubalterno());
			} else
				rs.setUnimm("null");
			rs.setIdRepDaEscludere(uidto.getDocfaIciReportDTO()
					.getDocfaIciReport().getIdRep());
			DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
			fillEnte(dataIn);
			dataIn.setRicerca(rs);
			List<DocfaIciReport> listaDocfaPerImmobile = docfaIciReportService
					.searchReport(dataIn);

			List<DocfaIciReportDTO> listaDocfaPerImmobileDTO = new ArrayList<DocfaIciReportDTO>();
			for (DocfaIciReport report : listaDocfaPerImmobile) {
				DocfaIciReportDTO dto = new DocfaIciReportDTO();
				dto.setDocfaIciReport(report);
				dto.setCategoriaDocfaExt(getCategoria(report
						.getCategoriaDocfa()));
				listaDocfaPerImmobileDTO.add(dto);
			}
			uidto.setListaDocfaPerImmobileDTO(listaDocfaPerImmobileDTO);

			// lista soggetti
			dataIn.setObj(uidto.getDocfaIciReportDTO().getDocfaIciReport()
					.getIdRep());
			uidto.setListaSoggetti(docfaIciReportService.getReportSogg(dataIn));

			// indirizzi
			rs.setEnte(uidto.getDocfaIciReportDTO().getDocfaIciReport().getCodEnte());
			rs.setUnimm(uidto.getDocfaIciReportDTO().getDocfaIciReport().getSubalterno());
			doCaricaIndirizziCatasto(uidto, rs);
			doCaricaIndirizziIci(uidto, rs);
			rs.setDataRegistrazioneDocfa(uidto.getDocfaIciReportDTO().getDocfaIciReport().getDataDocfaToDate());
			doCaricaIciDocfa(uidto, rs);

		} catch (Throwable t) {
			super.addErrorMessage("ui.error", t.getMessage());
			t.printStackTrace();
		}

		return uidto;

	}
	
	private String getIdDocfaIndice(){
		
		String idRep = selectedUI.getDocfaIciReportDTO().getDocfaIciReport().getIdRep();
		String[] fields = idRep.split("@");
		String idDocfaIndice = fields[1] + this.PIPE + fields[0] + this.PIPE + fields[5];
		
		return idDocfaIndice;
	}
	
	private void cleanLeftPadCiviciIci(){
		for(SitTIciOggettoDTO dto : listaIci){
			String lcivico = dto.getSitTIciOggetto().getNumCiv();
			String civico = this.cleanLeftPad(lcivico, '0');
			dto.getSitTIciOggetto().setNumCiv(civico);
		}
	}

	public void doCaricaIciAnteDocfaBySoggetto() {

		ricercaIciTarsuDto.setDataRif(selectedUI.getDocfaIciReportDTO()
				.getDocfaIciReport().getDataDocfa());
		ricercaIciTarsuDto.setFoglio(selectedUI.getDocfaIciReportDTO()
				.getDocfaIciReport().getFoglio());
		ricercaIciTarsuDto.setParticella(selectedUI.getDocfaIciReportDTO()
				.getDocfaIciReport().getParticella());
		ricercaIciTarsuDto.setSub(selectedUI.getDocfaIciReportDTO()
				.getDocfaIciReport().getSubalterno());

		DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
		fillEnte(dataIn);
		dataIn.setRicercaIciTarsuDto(ricercaIciTarsuDto);
		listaIci = docfaIciReportService.getIciAnteDocfaBySoggetto(dataIn);
		cleanLeftPadCiviciIci();

	}

	public void doCaricaIciPostDocfaBySoggetto() {

		ricercaIciTarsuDto.setDataRif(selectedUI.getDocfaIciReportDTO()
				.getDocfaIciReport().getDataDocfa());
		ricercaIciTarsuDto.setFoglio(selectedUI.getDocfaIciReportDTO()
				.getDocfaIciReport().getFoglio());
		ricercaIciTarsuDto.setParticella(selectedUI.getDocfaIciReportDTO()
				.getDocfaIciReport().getParticella());
		ricercaIciTarsuDto.setSub(selectedUI.getDocfaIciReportDTO()
				.getDocfaIciReport().getSubalterno());

		DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
		fillEnte(dataIn);
		dataIn.setRicercaIciTarsuDto(ricercaIciTarsuDto);
		listaIci = docfaIciReportService.getIciPostDocfaBySoggetto(dataIn);
		cleanLeftPadCiviciIci();

	}
	
	public void doCaricaIciUiuAnteDocfaBySoggetto() {

		ricercaIciTarsuDto.setDataRif(selectedUI.getDocfaIciReportDTO()
				.getDocfaIciReport().getDataDocfa());
		ricercaIciTarsuDto.setFoglio(selectedUI.getDocfaIciReportDTO()
				.getDocfaIciReport().getFoglio());
		ricercaIciTarsuDto.setParticella(selectedUI.getDocfaIciReportDTO()
				.getDocfaIciReport().getParticella());
		ricercaIciTarsuDto.setSub(selectedUI.getDocfaIciReportDTO()
				.getDocfaIciReport().getSubalterno());

		DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
		fillEnte(dataIn);
		dataIn.setRicercaIciTarsuDto(ricercaIciTarsuDto);
		listaIci = docfaIciReportService.getIciAnteDocfaUiuBySoggetto(dataIn);
		cleanLeftPadCiviciIci();

	}

	public void doCaricaIciUiuPostDocfaBySoggetto() {

		ricercaIciTarsuDto.setDataRif(selectedUI.getDocfaIciReportDTO()
				.getDocfaIciReport().getDataDocfa());
		ricercaIciTarsuDto.setFoglio(selectedUI.getDocfaIciReportDTO()
				.getDocfaIciReport().getFoglio());
		ricercaIciTarsuDto.setParticella(selectedUI.getDocfaIciReportDTO()
				.getDocfaIciReport().getParticella());
		ricercaIciTarsuDto.setSub(selectedUI.getDocfaIciReportDTO()
				.getDocfaIciReport().getSubalterno());

		DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
		fillEnte(dataIn);
		dataIn.setRicercaIciTarsuDto(ricercaIciTarsuDto);
		listaIci = docfaIciReportService.getIciPostDocfaUiuBySoggetto(dataIn);
		cleanLeftPadCiviciIci();

	}
	
	public void doCaricaIciCivAnteDocfaBySoggetto() {

		ricercaIciTarsuDto.setDataRif(selectedUI.getDocfaIciReportDTO().getDocfaIciReport().getDataDocfa());
		ricercaIciTarsuDto.setIdDwhOrigineCiv(getIdDocfaIndice());

		DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
		fillEnte(dataIn);
		dataIn.setRicercaIciTarsuDto(ricercaIciTarsuDto);
		listaIci = docfaIciReportService.getIciAnteDocfaCivBySoggetto(dataIn);
		cleanLeftPadCiviciIci();

	}

	public void doCaricaIciCivPostDocfaBySoggetto() {

		ricercaIciTarsuDto.setDataRif(selectedUI.getDocfaIciReportDTO().getDocfaIciReport().getDataDocfa());
		ricercaIciTarsuDto.setIdDwhOrigineCiv(getIdDocfaIndice());

		DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
		fillEnte(dataIn);
		dataIn.setRicercaIciTarsuDto(ricercaIciTarsuDto);
		listaIci = docfaIciReportService.getIciPostDocfaCivBySoggetto(dataIn);
		cleanLeftPadCiviciIci();

	}

	public void doCaricaDocfa() {
		try {

			DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
			fillEnte(dataIn);
			dataIn.setObj(idRepDocfa);
			docfa = docfaIciReportService.getDocfaIciReportById(dataIn);
			if (docfa.getCiviciDocfa() != null)
				docfa.setCiviciDocfa(docfa.getCiviciDocfa()
						.replaceAll("@", ", "));
			if (docfa.getAnnotazioni() != null)
				docfa.setAnnotazioni(docfa.getAnnotazioni()
						.replaceAll("@", ";"));

		} catch (Throwable t) {
			super.addErrorMessage("docfa.error", t.getMessage());
			t.printStackTrace();
		}
	}

	public void doCaricaIndirizziCatasto(UnitaImmobiliareIciDTO uiDTO,
			RicercaDocfaReportDTO rs) {
		try {

			DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
			fillEnte(dataIn);
			dataIn.setRicerca(rs);
			uiDTO.setListaIndirizziCatasto(docfaIciReportService.getIndirizziCatasto(dataIn));

		} catch (Throwable t) {
			super.addErrorMessage("indirizzi.error", t.getMessage());
			t.printStackTrace();
		}
	}

	public void doCaricaIndirizziIci(UnitaImmobiliareIciDTO uiDTO,
			RicercaDocfaReportDTO rs) {
		try {

			DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
			fillEnte(dataIn);
			dataIn.setRicerca(rs);
			uiDTO.setListaIndirizziIci(docfaIciReportService.getIndirizziIci(dataIn));

		} catch (Throwable t) {
			super.addErrorMessage("indirizzi.error", t.getMessage());
			t.printStackTrace();
		}
	}

	public void doCaricaIciDocfa(UnitaImmobiliareIciDTO uiDTO,
			RicercaDocfaReportDTO rs) {

		List<SitTIciOggettoDTO> listaAnte = new ArrayList<SitTIciOggettoDTO>();
		List<SitTIciOggettoDTO> listaPost = new ArrayList<SitTIciOggettoDTO>();

		try {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			int annoDocfa = Integer.parseInt(sdf.format(rs
					.getDataRegistrazioneDocfa()));

			DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
			fillEnte(dataIn);
			dataIn.setRicerca(rs);
			List<SitTIciOggettoDTO> lista = docfaIciReportService
					.getIciDocfa(dataIn);
			for (SitTIciOggettoDTO dto : lista) {

				int annoIci = Integer.parseInt(dto.getSitTIciOggetto()
						.getYeaDen());
				if (annoIci <= annoDocfa)
					listaAnte.add(dto);
				else
					listaPost.add(dto);

			}

		} catch (NullPointerException t) {
			super.addErrorMessage("ici.docfa.datareg.null", t.getMessage());
		} catch (Throwable t) {
			super.addErrorMessage("ici.error", t.getMessage());
			t.printStackTrace();
		}

		uiDTO.setListaOggettiIciAnte(listaAnte);
		uiDTO.setListaOggettiIciPost(listaPost);

	}

	public void doCaricaIciSoggetti() {

		try {

			DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
			fillEnte(dataIn);
			dataIn.setObj(idExtIci);
			listaSoggettiIci = docfaIciReportService.getSoggettiIci(dataIn);

		} catch (Throwable t) {
			super.addErrorMessage("subject.error", t.getMessage());
			t.printStackTrace();
		}
	}

	public String getCategoria(String codCategoria) {

		if (codCategoria != null && !"".equals(codCategoria)) {
			try {

				RicercaOggettoCatDTO ro = new RicercaOggettoCatDTO();
				ro.setCodCategoria(codCategoria);
				fillEnte(ro);
				List<Sitideco> lista = catastoService
						.getListaCategorieImmobile(ro);
				return lista.get(0).getDescription();

			} catch (Throwable t) {
				super.addErrorMessage("categorie.error", t.getMessage());
				t.printStackTrace();
			}
		}
		return "";
	}
	
	public String getCausale(String codCausale) {

		if (codCausale != null && !"".equals(codCausale)) {
			try {

				return CommonDataBean.getCausali().get(codCausale);

			} catch (Throwable t) {
				super.addErrorMessage("categorie.error", t.getMessage());
				t.printStackTrace();
			}
		}
		return "";
	}

	@Override
	protected String getFilePath(String fileName) {
		String nomeFile = fileName;
		String pathFile = getFolderPath() + File.separatorChar + nomeFile;
		return pathFile;
	}

	protected String getFolderPath() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String pathDatiDiogene = super.getRootPathDatiDiogene();
		String pathPdfDocfa = pathDatiDiogene
				//+ File.separatorChar
				+ dto.getDocfaIciReport().getCodEnte()
				+ File.separatorChar
				+ "docfa_pdf"
				+ File.separatorChar
				+ sdf.format(dto.getDocfaIciReport().getFornitura()).substring(
						0, 6);
		return pathPdfDocfa;
	}

	public String getIdRep() {
		return idRep;
	}

	public void setIdRep(String idRep) {
		this.idRep = idRep;
	}

	public DocfaIciReportDTO getDto() {
		return dto;
	}

	public void setDto(DocfaIciReportDTO dto) {
		this.dto = dto;
	}

	public List<SelectItem> getListaUI() {
		return listaUI;
	}

	public void setListaUI(List<SelectItem> listaUI) {
		this.listaUI = listaUI;
	}

	public RicercaIciTarsuDTO getRicercaIciTarsuDto() {
		return ricercaIciTarsuDto;
	}

	public void setRicercaIciTarsuDto(RicercaIciTarsuDTO ricercaIciTarsuDto) {
		this.ricercaIciTarsuDto = ricercaIciTarsuDto;
	}

	public List<SitTIciOggettoDTO> getListaIci() {
		return listaIci;
	}

	public void setListaIci(List<SitTIciOggettoDTO> listaIci) {
		this.listaIci = listaIci;
	}

	public String getIdRepUI() {
		return idRepUI;
	}

	public void setIdRepUI(String idRepUI) {
		this.idRepUI = idRepUI;
	}

	public UnitaImmobiliareIciDTO getSelectedUI() {
		return selectedUI;
	}

	public void setSelectedUI(UnitaImmobiliareIciDTO selectedUI) {
		this.selectedUI = selectedUI;
	}

	public String getIdRepDocfa() {
		return idRepDocfa;
	}

	public void setIdRepDocfa(String idRepDocfa) {
		this.idRepDocfa = idRepDocfa;
	}

	public DocfaIciReport getDocfa() {
		return docfa;
	}

	public void setDocfa(DocfaIciReport docfa) {
		this.docfa = docfa;
	}

	public String getIdExtIci() {
		return idExtIci;
	}

	public void setIdExtIci(String idExtIci) {
		this.idExtIci = idExtIci;
	}

	public List<SoggettoIciDTO> getListaSoggettiIci() {
		return listaSoggettiIci;
	}

	public void setListaSoggettiIci(List<SoggettoIciDTO> listaSoggettiIci) {
		this.listaSoggettiIci = listaSoggettiIci;
	}
}
