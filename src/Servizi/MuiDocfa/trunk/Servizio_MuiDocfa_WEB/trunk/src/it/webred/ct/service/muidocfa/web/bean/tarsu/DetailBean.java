package it.webred.ct.service.muidocfa.web.bean.tarsu;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.catasto.dto.SoggettoCatastoDTO;
import it.webred.ct.data.access.basic.common.dto.RicercaOggettoDTO;
import it.webred.ct.data.access.basic.diagnostiche.DiagnosticheDataIn;
import it.webred.ct.data.access.basic.diagnostiche.dto.RicercaDocfaReportDTO;
import it.webred.ct.data.access.basic.diagnostiche.dto.RicercaIciTarsuDTO;
import it.webred.ct.data.access.basic.diagnostiche.tarsu.dto.DocfaTarReportDTO;
import it.webred.ct.data.access.basic.diagnostiche.tarsu.dto.SitTTarOggettoDTO;
import it.webred.ct.data.access.basic.diagnostiche.tarsu.dto.UnitaImmobiliareTarDTO;
import it.webred.ct.data.access.basic.docfa.dto.RicercaOggettoDocfaDTO;
import it.webred.ct.data.access.basic.tarsu.dto.RicercaOggettoTarsuDTO;
import it.webred.ct.data.access.basic.tarsu.dto.SoggettoTarsuDTO;
import it.webred.ct.data.model.anagrafe.SitDPersona;
import it.webred.ct.data.model.catasto.LoadCatUiuId;
import it.webred.ct.data.model.catasto.Sitideco;
import it.webred.ct.data.model.diagnostiche.DocfaTarReport;
import it.webred.ct.data.model.locazioni.LocazioniA;
import it.webred.ct.data.model.tarsu.SitTTarOggetto;
import it.webred.ct.service.muidocfa.web.bean.tarsu.planimetrie.PlanimetrieC340Bean;
import it.webred.ct.service.muidocfa.web.bean.tarsu.planimetrie.PlanimetrieDocfaBean;
import it.webred.ct.service.muidocfa.web.bean.util.CommonDataBean;
import it.webred.ct.service.muidocfa.web.bean.util.GestioneFileBean;

public class DetailBean extends GestioneFileBean {

	private static final long serialVersionUID = 1L;
	private String idRep;
	private String idRepUI;
	private String idRepDocfa;
	private String idExtTar;
	private String rowIndex;
	private List<SelectItem> listaUI;
	private DocfaTarReport docfa;
	private DocfaTarReportDTO dto;
	private RicercaIciTarsuDTO ricercaIciTarsuDto;
	private List<SitTTarOggettoDTO> listaTar;
	private UnitaImmobiliareTarDTO selectedUI;
	private List<SoggettoTarsuDTO> listaSoggettiTar;
	private boolean familiare;
	private final Long[] codIndiceDemografia = {1L,1L};
	private final Long[] codIndiceCatastoSoggetti= {4L,3L};
	private final String PIPE = "|";
	private final String dirDocfaPDF = "docfa_pdf";
	private SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
	private boolean renderUiu;
	

	@PostConstruct
	public void initService() {

		dto = new DocfaTarReportDTO();
		ricercaIciTarsuDto = new RicercaIciTarsuDTO();

	}

	public String goDetail() {

		doCaricaDto();
		doCaricaUnitaImmobiliari();
		return "muidocfa.tarsu.detail";

	}

	public void doCaricaDto() {
		try {
			renderUiu = false;
			DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
			fillEnte(dataIn);
			dataIn.setObj(idRep);
			dto.setDocfaTarReport(docfaTarReportService.getDocfaTarReportById(dataIn));
			dto.setTitolariAnno(docfaTarReportService.getReportSogg(dataIn));
			
			dto.getDocfaTarReport().setDicResNumciv(this.cleanLeftPad(dto.getDocfaTarReport().getDicResNumciv(),'0'));

			RicercaOggettoDocfaDTO ricercaDocfaDto = new RicercaOggettoDocfaDTO();
			fillEnte(ricercaDocfaDto);
			ricercaDocfaDto.setFornitura(dto.getDocfaTarReport().getFornitura());
			ricercaDocfaDto.setProtocollo(dto.getDocfaTarReport().getProtocolloDocfa());
			
			dto.setListaIntestati(docfaService.getIntestati(ricercaDocfaDto));

			dto.setCausaleDocfaExt(getCausale(dto.getDocfaTarReport().getCausaleDocfa()));
			
			doCaricaQuadroSuperficiDocfa();
			
		} catch (Throwable t) {
			super.addErrorMessage("lista.error", t.getMessage());
			t.printStackTrace();
		}
	}
	
	public void doCaricaQuadroSuperficiDocfa(){
		
		RicercaOggettoDocfaDTO ricercaDocfaDto = new RicercaOggettoDocfaDTO();
		fillEnte(ricercaDocfaDto);
		ricercaDocfaDto.setFornitura(dto.getDocfaTarReport().getFornitura());
		ricercaDocfaDto.setProtocollo(dto.getDocfaTarReport().getProtocolloDocfa());
		ricercaDocfaDto.setDataRegistrazione(dto.getDocfaTarReport().getDataDocfa());
		ricercaDocfaDto.setTipoOperDocfa("S");
		dto.setQuadroPre(docfaTarReportService.getQuadroSuperfici(ricercaDocfaDto));
		
		ricercaDocfaDto.setTipoOperDocfa("C");
		dto.setQuadroPost(docfaTarReportService.getQuadroSuperfici(ricercaDocfaDto));
		
	}

	public void doCaricaUnitaImmobiliari() {

		try {

			listaUI = new ArrayList<SelectItem>();
			RicercaDocfaReportDTO ricerca = new RicercaDocfaReportDTO();
			ricerca.setFornitura(dto.getDocfaTarReport().getFornitura());
			ricerca.setProtocolloDocfa(dto.getDocfaTarReport().getProtocolloDocfa());
		//	ricerca.setDataRegistrazioneDocfa(dto.getDocfaTarReport().getDataDocfaToDate());

			DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
			fillEnte(dataIn);
			dataIn.setRicerca(ricerca);
			List<DocfaTarReport> lista = docfaTarReportService.searchReport(dataIn);
			for (int i = 0; i < lista.size(); i++) {

				DocfaTarReport report = lista.get(i);
				UnitaImmobiliareTarDTO uidto = new UnitaImmobiliareTarDTO();
				DocfaTarReportDTO dto = new DocfaTarReportDTO();
				dto.setDocfaTarReport(report);
				uidto.setDocfaTarReportDTO(dto);
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
								+ (report.getSubalterno() != null ? report
										.getSubalterno() : "")
								+ " (" +report.getTipoOperDocfaEx()+ ")");
				listaUI.add(si);

			}

		} catch (Throwable t) {
			super.addErrorMessage("lista.error", t.getMessage());
			t.printStackTrace();
		}

	}

	public void doCaricaUnitaImmobiliare() {
		renderUiu = true;
		UnitaImmobiliareTarDTO uiDto = doCaricaUnitaImmobiliare(idRepUI, true);
		selectedUI = uiDto;

	}

	public UnitaImmobiliareTarDTO doCaricaUnitaImmobiliare(String idRep,
			boolean altraUi) {

		UnitaImmobiliareTarDTO uidto = new UnitaImmobiliareTarDTO();
		try {

			if (!altraUi)
				uidto = selectedUI;
			else {
				DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
				fillEnte(dataIn);
				dataIn.setObj(idRep);
				uidto.setDocfaTarReportDTO(new DocfaTarReportDTO());
				uidto.getDocfaTarReportDTO().setDocfaTarReport(
						docfaTarReportService.getDocfaTarReportById(dataIn));
			}

			// descrizione categoria
			uidto.getDocfaTarReportDTO().setCategoriaDocfaExt(
					getCategoria(uidto.getDocfaTarReportDTO()
							.getDocfaTarReport().getCategoriaDocfa()));
			uidto.getDocfaTarReportDTO().setCategoriaCatastoExt(
					getCategoria(uidto.getDocfaTarReportDTO()
							.getDocfaTarReport().getCategoriaCatasto()));

			// formattazione civici
			if (uidto.getDocfaTarReportDTO().getDocfaTarReport().getDicResNumciv() != null)
				uidto.getDocfaTarReportDTO().getDocfaTarReport().setDicResNumciv(
						this.cleanLeftPad(uidto.getDocfaTarReportDTO().getDocfaTarReport().getDicResNumciv(),'0'));
			
			if (uidto.getDocfaTarReportDTO().getDocfaTarReport().getCiviciDocfa() != null)
				uidto.getDocfaTarReportDTO().getDocfaTarReport().setCiviciDocfa(
						uidto.getDocfaTarReportDTO().getDocfaTarReport().getCiviciDocfa()
										.replaceAll("@", ", "));
			
			// formattazione annotazioni
			if (uidto.getDocfaTarReportDTO().getDocfaTarReport().getAnnotazioni() != null)
				uidto.getDocfaTarReportDTO().getDocfaTarReport()
						.setAnnotazioni(uidto.getDocfaTarReportDTO()
										.getDocfaTarReport().getAnnotazioni()
										.replaceAll("@", ";"));

			// lista altri docfa
			RicercaDocfaReportDTO rs = new RicercaDocfaReportDTO();
			rs.setFoglio(uidto.getDocfaTarReportDTO().getDocfaTarReport()
					.getFoglio());
			rs.setParticella(uidto.getDocfaTarReportDTO().getDocfaTarReport()
					.getParticella());
			if (uidto.getDocfaTarReportDTO().getDocfaTarReport()
					.getSubalterno() != null) {
				rs.setUnimm(uidto.getDocfaTarReportDTO().getDocfaTarReport()
						.getSubalterno());
			} else
				rs.setUnimm("null");
			rs.setIdRepDaEscludere(uidto.getDocfaTarReportDTO()
					.getDocfaTarReport().getIdRep());

			DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
			fillEnte(dataIn);
			dataIn.setRicerca(rs);
			List<DocfaTarReport> listaDocfaPerImmobile = docfaTarReportService
					.searchReport(dataIn);

			List<DocfaTarReportDTO> listaDocfaPerImmobileDTO = new ArrayList<DocfaTarReportDTO>();
			for (DocfaTarReport report : listaDocfaPerImmobile) {
				DocfaTarReportDTO dto = new DocfaTarReportDTO();
				dto.setDocfaTarReport(report);
				dto.setCategoriaDocfaExt(getCategoria(report
						.getCategoriaDocfa()));
				listaDocfaPerImmobileDTO.add(dto);
			}
			uidto.setListaDocfaPerImmobileDTO(listaDocfaPerImmobileDTO);

			// lista soggetti
			dataIn = new DiagnosticheDataIn();
			fillEnte(dataIn);
			dataIn.setObj(uidto.getDocfaTarReportDTO().getDocfaTarReport()
					.getIdRep());
			uidto.setListaSoggetti(docfaTarReportService.getReportSogg(dataIn));

			// indirizzi
			rs.setEnte(uidto.getDocfaTarReportDTO().getDocfaTarReport()
					.getCodEnte());
			rs.setUnimm(uidto.getDocfaTarReportDTO().getDocfaTarReport()
					.getSubalterno());
			doCaricaIndirizziCatasto(uidto, rs);

			rs.setDataRegistrazioneDocfa(uidto.getDocfaTarReportDTO()
					.getDocfaTarReport().getDataDocfaToDate());
			doCaricaIndirizziTarsu(uidto, rs);
			
			rs.setFornitura(uidto.getDocfaTarReportDTO().getDocfaTarReport().getFornitura());
			rs.setProtocolloDocfa(uidto.getDocfaTarReportDTO().getDocfaTarReport().getProtocolloDocfa());

			// dati metrici
			doCaricaDatiMetriciABC(uidto, rs);
			doCaricaDatiMetrici(uidto, rs);

			// tarsu
			doCaricaTarDocfa(uidto, rs);
			doCaricaTarDocfaGraffati(uidto, rs);

			// ricerca planimetrie C340
			RicercaOggettoCatDTO ro = new RicercaOggettoCatDTO();
			ro.setCodEnte(uidto.getDocfaTarReportDTO().getDocfaTarReport().getCodEnte());
			ro.setFoglio(uidto.getDocfaTarReportDTO().getDocfaTarReport().getFoglio());
			ro.setParticella(uidto.getDocfaTarReportDTO().getDocfaTarReport().getParticella());
			ro.setUnimm(uidto.getDocfaTarReportDTO().getDocfaTarReport().getSubalterno());
			PlanimetrieC340Bean planC340Bean = (PlanimetrieC340Bean) getBeanReference("planimetrieC340Bean");
			planC340Bean.setCodEnte(uidto.getDocfaTarReportDTO().getDocfaTarReport().getCodEnte());
			uidto.setListaPlanimetrieC340(planC340Bean.creaListaPlanimetrieC340(ro));

			// ricerca planimetrie Docfa
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			PlanimetrieDocfaBean planDocfaBean = (PlanimetrieDocfaBean) getBeanReference("planimetrieDocfaBean");
			planDocfaBean.setCodEnte(uidto.getDocfaTarReportDTO().getDocfaTarReport().getCodEnte());
			planDocfaBean.setFornitura(sdf.format(uidto.getDocfaTarReportDTO().getDocfaTarReport().getFornitura()));
			uidto.setListaPlanimetrieDocfa(planDocfaBean.creaListaPlanimetrieDocfa(rs));

		} catch (Throwable t) {
			super.addErrorMessage("ui.error", t.getMessage());
			t.printStackTrace();
		}

		return uidto;

	}

	public void doCaricaDocfa() {
		try {

			DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
			fillEnte(dataIn);
			dataIn.setObj(idRepDocfa);
			docfa = docfaTarReportService.getDocfaTarReportById(dataIn);
			if (docfa.getCiviciDocfa() != null)
				docfa.setCiviciDocfa(docfa.getCiviciDocfa().replaceAll("@", ", "));
			if (docfa.getAnnotazioni() != null)
				docfa.setAnnotazioni(docfa.getAnnotazioni().replaceAll("@", ";"));

		} catch (Throwable t) {
			super.addErrorMessage("docfa.error", t.getMessage());
			t.printStackTrace();
		}
	}

	public void doCaricaIndirizziCatasto(UnitaImmobiliareTarDTO uiDTO,
			RicercaDocfaReportDTO rs) {
		try {

			DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
			fillEnte(dataIn);
			dataIn.setRicerca(rs);
			uiDTO.setListaIndirizziCatasto(docfaTarReportService
					.getIndirizziCatasto(dataIn));

		} catch (Throwable t) {
			super.addErrorMessage("indirizzi.error", t.getMessage());
			t.printStackTrace();
		}
	}

	public void doCaricaIndirizziTarsu(UnitaImmobiliareTarDTO uiDTO,
			RicercaDocfaReportDTO rs) {
		try {

			DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
			fillEnte(dataIn);
			dataIn.setRicerca(rs);
			uiDTO.setListaIndirizziTarsu(docfaTarReportService.getIndirizziTarsu(dataIn));

		} catch (Throwable t) {
			super.addErrorMessage("indirizzi.error", t.getMessage());
			t.printStackTrace();
		}
	}

	public void doCaricaDatiMetrici(UnitaImmobiliareTarDTO uiDTO,RicercaDocfaReportDTO rs) {
		try {

			
			if (rs.getDataRegistrazioneDocfa() != null) {
				fillEnte(rs);
				uiDTO.setListaDatiMetrici(docfaService.getDatiMetrici(fillRicercaOggettoDocfa(rs)));
			} else
				super.addErrorMessage("datimetrici.docfa.datareg.null", "");

		} catch (Throwable t) {
			super.addErrorMessage("datimetrici.error", t.getMessage());
			t.printStackTrace();
		}
	}

	public void doCaricaDatiMetriciABC(UnitaImmobiliareTarDTO uiDTO,
			RicercaDocfaReportDTO rs) {
		try {

			//DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
			//fillEnte(dataIn);
			//dataIn.setRicerca(rs);
			this.fillEnte(rs);
			uiDTO.setDatiMetriciABC(docfaService.getDatiMetriciABC(fillRicercaOggettoDocfa(rs)));

		} catch (Throwable t) {
			super.addErrorMessage("datimetrici.error", t.getMessage());
			t.printStackTrace();
		}
	}
	
	private void cleanLeftPadCiviciTarsu(List<SitTTarOggettoDTO> listaTarsu){
		for(SitTTarOggettoDTO dto : listaTarsu){
			String lcivico = dto.getSitTTarOggetto().getNumCiv();
			String civico = this.cleanLeftPad(lcivico, '0');
			dto.getSitTTarOggetto().setNumCiv(civico);
		}
		
	}

	public void doCaricaTarUiuAnteDocfaBySoggetto() {

		try {
			if (selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getDataDocfa() != null) {
				ricercaIciTarsuDto.setDataRif(selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getDataDocfa());
				ricercaIciTarsuDto.setFoglio(selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getFoglio());
				ricercaIciTarsuDto.setParticella(selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getParticella());
				ricercaIciTarsuDto.setSub(selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getSubalterno());
				
				ricercaIciTarsuDto.setEnteSorgenteOrigine(codIndiceCatastoSoggetti[0]);
				ricercaIciTarsuDto.setProgOrigine(codIndiceCatastoSoggetti[1]);

				DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
				fillEnte(dataIn);
				dataIn.setRicercaIciTarsuDto(ricercaIciTarsuDto);
				listaTar = docfaTarReportService.getTarAnteDocfaBySoggettoUiu(dataIn);
				cleanLeftPadCiviciTarsu(listaTar);
			}

		} catch (Throwable t) {
			super.addErrorMessage("tarsu.error", t.getMessage());
			t.printStackTrace();
		}

	}

	public void doCaricaTarUiuPostDocfaBySoggetto() {

		try {
			if (selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getDataDocfa() != null) {
				ricercaIciTarsuDto.setDataRif(selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getDataDocfa());
				ricercaIciTarsuDto.setFoglio(selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getFoglio());
				ricercaIciTarsuDto.setParticella(selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getParticella());
				ricercaIciTarsuDto.setSub(selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getSubalterno());
				
				ricercaIciTarsuDto.setEnteSorgenteOrigine(codIndiceCatastoSoggetti[0]);
				ricercaIciTarsuDto.setProgOrigine(codIndiceCatastoSoggetti[1]);

				DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
				fillEnte(dataIn);
				dataIn.setRicercaIciTarsuDto(ricercaIciTarsuDto);
				listaTar = docfaTarReportService.getTarPostDocfaBySoggettoUiu(dataIn);
				cleanLeftPadCiviciTarsu(listaTar);
			}

		} catch (Throwable t) {
			super.addErrorMessage("tarsu.error", t.getMessage());
			t.printStackTrace();
		}

	}
	
	private String getIdDocfaIndice(){
		
		String idRep = selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getIdRep();
		String[] fields = idRep.split("@");
		String idDocfaIndice = fields[1] + this.PIPE + fields[0] + this.PIPE + fields[5];
		
		return idDocfaIndice;
	}
	
	public void doCaricaTarCivAnteDocfaBySoggetto() {

		try {
			if (selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getDataDocfa() != null) {
				ricercaIciTarsuDto.setDataRif(selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getDataDocfa());
				ricercaIciTarsuDto.setIdDwhOrigineCiv(getIdDocfaIndice());
				ricercaIciTarsuDto.setEnteSorgenteOrigine(codIndiceCatastoSoggetti[0]);
				ricercaIciTarsuDto.setProgOrigine(codIndiceCatastoSoggetti[1]);

				DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
				fillEnte(dataIn);
				dataIn.setRicercaIciTarsuDto(ricercaIciTarsuDto);
				listaTar = docfaTarReportService.getTarAnteDocfaBySoggettoCiv(dataIn);
				cleanLeftPadCiviciTarsu(listaTar);
			}

		} catch (Throwable t) {
			super.addErrorMessage("tarsu.error", t.getMessage());
			t.printStackTrace();
		}

	}
	
	
	
	public void doCaricaTarCivPostDocfaBySoggetto() {

		try {
			if (selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getDataDocfa() != null) {
				ricercaIciTarsuDto.setDataRif(selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getDataDocfa());
				ricercaIciTarsuDto.setIdDwhOrigineCiv(getIdDocfaIndice());
				ricercaIciTarsuDto.setEnteSorgenteOrigine(codIndiceCatastoSoggetti[0]);
				ricercaIciTarsuDto.setProgOrigine(codIndiceCatastoSoggetti[1]);


				DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
				fillEnte(dataIn);
				dataIn.setRicercaIciTarsuDto(ricercaIciTarsuDto);
				listaTar = docfaTarReportService.getTarPostDocfaBySoggettoCiv(dataIn);
				cleanLeftPadCiviciTarsu(listaTar);
			}

		} catch (Throwable t) {
			super.addErrorMessage("tarsu.error", t.getMessage());
			t.printStackTrace();
		}

	}
	
	public void doCaricaTarAnteDocfaBySoggetto() {

		try {
			if (selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getDataDocfa() != null) {
				ricercaIciTarsuDto.setDataRif(selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getDataDocfa());
				ricercaIciTarsuDto.setFoglio(selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getFoglio());
				ricercaIciTarsuDto.setParticella(selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getParticella());
				ricercaIciTarsuDto.setSub(selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getSubalterno());
				
				ricercaIciTarsuDto.setEnteSorgenteOrigine(codIndiceCatastoSoggetti[0]);
				ricercaIciTarsuDto.setProgOrigine(codIndiceCatastoSoggetti[1]);


				DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
				fillEnte(dataIn);
				dataIn.setRicercaIciTarsuDto(ricercaIciTarsuDto);
				listaTar = docfaTarReportService.getTarAnteDocfaBySoggettoUiuUnd(dataIn);
				cleanLeftPadCiviciTarsu(listaTar);
			}

		} catch (Throwable t) {
			super.addErrorMessage("tarsu.error", t.getMessage());
			t.printStackTrace();
		}

	}
	
	public void doCaricaTarPostDocfaBySoggetto() {

		try {
			if (selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getDataDocfa() != null) {
				ricercaIciTarsuDto.setDataRif(selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getDataDocfa());
				ricercaIciTarsuDto.setFoglio(selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getFoglio());
				ricercaIciTarsuDto.setParticella(selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getParticella());
				ricercaIciTarsuDto.setSub(selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getSubalterno());
				
				ricercaIciTarsuDto.setEnteSorgenteOrigine(codIndiceCatastoSoggetti[0]);
				ricercaIciTarsuDto.setProgOrigine(codIndiceCatastoSoggetti[1]);


				DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
				fillEnte(dataIn);
				dataIn.setRicercaIciTarsuDto(ricercaIciTarsuDto);
				listaTar = docfaTarReportService.getTarPostDocfaBySoggettoUiuUnd(dataIn);
				cleanLeftPadCiviciTarsu(listaTar);
			}

		} catch (Throwable t) {
			super.addErrorMessage("tarsu.error", t.getMessage());
			t.printStackTrace();
		}

	}
	
	/**
	 * Estrazione dichiarazioni TARSU da familiari del soggetto titolare a catasto
	 */

	public void doCaricaFamTarUiuAnteDocfaBySoggetto() {

		try {
			if (selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getDataDocfa() != null) {
				ricercaIciTarsuDto.setDataRif(selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getDataDocfa());
				ricercaIciTarsuDto.setFoglio(selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getFoglio());
				ricercaIciTarsuDto.setParticella(selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getParticella());
				ricercaIciTarsuDto.setSub(selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getSubalterno());
				
				ricercaIciTarsuDto.setEnteSorgenteOrigine(codIndiceCatastoSoggetti[0]);
				ricercaIciTarsuDto.setProgOrigine(codIndiceCatastoSoggetti[1]);

				DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
				fillEnte(dataIn);
				dataIn.setRicercaIciTarsuDto(ricercaIciTarsuDto);
				listaTar = docfaTarReportService.getTarAnteDocfaByFamiliariUiu(dataIn);
				cleanLeftPadCiviciTarsu(listaTar);
				
				familiare = true;
			}

		} catch (Throwable t) {
			super.addErrorMessage("tarsu.error", t.getMessage());
			t.printStackTrace();
		}

	}

	public void doCaricaFamTarUiuPostDocfaBySoggetto() {

		try {
			if (selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getDataDocfa() != null) {
				ricercaIciTarsuDto.setDataRif(selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getDataDocfa());
				ricercaIciTarsuDto.setFoglio(selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getFoglio());
				ricercaIciTarsuDto.setParticella(selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getParticella());
				ricercaIciTarsuDto.setSub(selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getSubalterno());
				
				ricercaIciTarsuDto.setEnteSorgenteOrigine(codIndiceCatastoSoggetti[0]);
				ricercaIciTarsuDto.setProgOrigine(codIndiceCatastoSoggetti[1]);

				DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
				fillEnte(dataIn);
				dataIn.setRicercaIciTarsuDto(ricercaIciTarsuDto);
				listaTar = docfaTarReportService.getTarPostDocfaByFamiliariUiu(dataIn);
				cleanLeftPadCiviciTarsu(listaTar);
				
				familiare = true;
			}

		} catch (Throwable t) {
			super.addErrorMessage("tarsu.error", t.getMessage());
			t.printStackTrace();
		}

	}
	
	public void doCaricaFamTarCivAnteDocfaBySoggetto() {

		try {
			if (selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getDataDocfa() != null) {
				ricercaIciTarsuDto.setDataRif(selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getDataDocfa());
				ricercaIciTarsuDto.setIdDwhOrigineCiv(getIdDocfaIndice());
				
				ricercaIciTarsuDto.setEnteSorgenteOrigine(codIndiceCatastoSoggetti[0]);
				ricercaIciTarsuDto.setProgOrigine(codIndiceCatastoSoggetti[1]);

				DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
				fillEnte(dataIn);
				dataIn.setRicercaIciTarsuDto(ricercaIciTarsuDto);
				listaTar = docfaTarReportService.getTarAnteDocfaByFamiliariCiv(dataIn);
				cleanLeftPadCiviciTarsu(listaTar);
				
				familiare = true;
			}

		} catch (Throwable t) {
			super.addErrorMessage("tarsu.error", t.getMessage());
			t.printStackTrace();
		}

	}
	
	public void doCaricaFamTarCivPostDocfaBySoggetto() {

		try {
			if (selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getDataDocfa() != null) {
				ricercaIciTarsuDto.setDataRif(selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getDataDocfa());
				ricercaIciTarsuDto.setIdDwhOrigineCiv(getIdDocfaIndice());		
				ricercaIciTarsuDto.setEnteSorgenteOrigine(codIndiceCatastoSoggetti[0]);
				ricercaIciTarsuDto.setProgOrigine(codIndiceCatastoSoggetti[1]);

				DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
				fillEnte(dataIn);
				dataIn.setRicercaIciTarsuDto(ricercaIciTarsuDto);
				listaTar = docfaTarReportService.getTarPostDocfaByFamiliariCiv(dataIn);
				cleanLeftPadCiviciTarsu(listaTar);
				
				familiare = true;
			}

		} catch (Throwable t) {
			super.addErrorMessage("tarsu.error", t.getMessage());
			t.printStackTrace();
		}

	}
	
	public void doCaricaFamTarAnteDocfaBySoggetto() {

		try {
			if (selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getDataDocfa() != null) {
				ricercaIciTarsuDto.setDataRif(selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getDataDocfa());
				ricercaIciTarsuDto.setFoglio(selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getFoglio());
				ricercaIciTarsuDto.setParticella(selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getParticella());
				ricercaIciTarsuDto.setSub(selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getSubalterno());
				
				ricercaIciTarsuDto.setEnteSorgenteOrigine(codIndiceCatastoSoggetti[0]);
				ricercaIciTarsuDto.setProgOrigine(codIndiceCatastoSoggetti[1]);


				DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
				fillEnte(dataIn);
				dataIn.setRicercaIciTarsuDto(ricercaIciTarsuDto);
				listaTar = docfaTarReportService.getTarAnteDocfaByFamiliariUiuUnd(dataIn);
				cleanLeftPadCiviciTarsu(listaTar);
				
				familiare = true;
			}

		} catch (Throwable t) {
			super.addErrorMessage("tarsu.error", t.getMessage());
			t.printStackTrace();
		}

	}
	
	public void doCaricaFamTarPostDocfaBySoggetto() {

		try {
			if (selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getDataDocfa() != null) {
				ricercaIciTarsuDto.setDataRif(selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getDataDocfa());
				ricercaIciTarsuDto.setFoglio(selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getFoglio());
				ricercaIciTarsuDto.setParticella(selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getParticella());
				ricercaIciTarsuDto.setSub(selectedUI.getDocfaTarReportDTO().getDocfaTarReport().getSubalterno());
				
				ricercaIciTarsuDto.setEnteSorgenteOrigine(codIndiceCatastoSoggetti[0]);
				ricercaIciTarsuDto.setProgOrigine(codIndiceCatastoSoggetti[1]);


				DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
				fillEnte(dataIn);
				dataIn.setRicercaIciTarsuDto(ricercaIciTarsuDto);
				listaTar = docfaTarReportService.getTarPostDocfaByFamiliariUiuUnd(dataIn);
				cleanLeftPadCiviciTarsu(listaTar);
				
				familiare = true;
			}

		} catch (Throwable t) {
			super.addErrorMessage("tarsu.error", t.getMessage());
			t.printStackTrace();
		}

	}

	

	public void doCaricaTarDocfa(UnitaImmobiliareTarDTO uiDTO,
			RicercaDocfaReportDTO rs) {

		List<SitTTarOggettoDTO> listaAnte = new ArrayList<SitTTarOggettoDTO>();
		List<SitTTarOggettoDTO> listaPost = new ArrayList<SitTTarOggettoDTO>();

		try {

			if (rs.getDataRegistrazioneDocfa() != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				RicercaOggettoDTO ro = new RicercaOggettoDTO();
				ro.setFoglio(rs.getFoglio());
				ro.setParticella(rs.getParticella());
				ro.setSub(rs.getUnimm());
				fillEnte(ro);
				
				List<SitTTarOggettoDTO> lista = tarsuService.getListaOggettiDTOByUI(ro);
				
				for (SitTTarOggettoDTO dto : lista) {

					Date dataIni = dto.getSitTTarOggetto().getDatIni();
					if (dataIni == null)
						dataIni = sdf.parse("00010101");
					if (dataIni.before(rs.getDataRegistrazioneDocfa()))
						listaAnte.add(dto);
					else if(dataIni.compareTo(rs.getDataRegistrazioneDocfa())>=0)
						listaPost.add(dto);
				}
			} else
				super.addErrorMessage("tarsu.docfa.datareg.null", "");

		} catch (Throwable t) {
			super.addErrorMessage("ui.error", t.getMessage());
			t.printStackTrace();
		}
		
		cleanLeftPadCiviciTarsu(listaAnte);
		cleanLeftPadCiviciTarsu(listaPost);
		
		uiDTO.setListaOggettiTarAnte(listaAnte);
		uiDTO.setListaOggettiTarPost(listaPost);

	}

	
	public void doCaricaTarDocfaGraffati(UnitaImmobiliareTarDTO uiDTO,
			RicercaDocfaReportDTO rs) {

		List<SitTTarOggettoDTO> listaGAnte = new ArrayList<SitTTarOggettoDTO>();
		List<SitTTarOggettoDTO> listaGPost = new ArrayList<SitTTarOggettoDTO>();

		try {

			if (rs.getDataRegistrazioneDocfa() != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

				
				//Estrai i subalterni collegati alla UIU (Graffati)
				RicercaOggettoCatDTO ro = new RicercaOggettoCatDTO();
				fillEnte(ro);
				ro.setFoglio(rs.getFoglio());
				ro.setParticella(rs.getParticella());
				ro.setUnimm(rs.getUnimm());
				List<String> elaborati = new ArrayList<String>();
				List<LoadCatUiuId> graffati = catastoService.getLoadCatUiuIdCollegati(ro);
				uiDTO.setFlgPresenzaGraffati(!graffati.isEmpty());
				getLogger().info("Presenza Graffati: " + uiDTO.getFlgPresenzaGraffati() + "["+graffati.size()+"]");
				
				for(LoadCatUiuId graf : graffati){
					
					String current = graf.getFoglio()+ graf.getMappale()+ graf.getSub();
					logger.info("Graffato Corrente: " + current);
					
					if(!elaborati.contains(current)){
						elaborati.add(current);
						RicercaOggettoDTO rog = new RicercaOggettoDTO();
						rog.setFoglio(graf.getFoglio());
						rog.setParticella(graf.getMappale());
						rog.setSub(graf.getSub());
						rog.setDtRif(rs.getDataRegistrazioneDocfa());
						fillEnte(rog);
						
						List<SitTTarOggettoDTO> lista = tarsuService.getListaOggettiDTOByUI(rog);
						
						for (SitTTarOggettoDTO dto : lista) {
	
							Date dataIni = dto.getSitTTarOggetto().getDatIni();
							if (dataIni == null)
								dataIni = sdf.parse("00010101");
							if (dataIni.before(rs.getDataRegistrazioneDocfa()))
								listaGAnte.add(dto);
							else if(dataIni.after(rs.getDataRegistrazioneDocfa()) || dataIni.equals(rs.getDataRegistrazioneDocfa()))
								listaGPost.add(dto);
						}	
					}
				}
				
			} else
				super.addErrorMessage("tarsu.docfa.datareg.null", "");

		} catch (Throwable t) {
			super.addErrorMessage("ui.error", t.getMessage());
			t.printStackTrace();
		}

		cleanLeftPadCiviciTarsu(listaGAnte);
		cleanLeftPadCiviciTarsu(listaGPost);
		
		uiDTO.setListaOggettiTarAnteGraffati(listaGAnte);
		uiDTO.setListaOggettiTarPostGraffati(listaGPost);

	}
	
	
	public void doCaricaTarsuSoggetti() {

		try {

			RicercaOggettoTarsuDTO rot = new RicercaOggettoTarsuDTO();
			fillEnte(rot);
			rot.setIdExtOgg(idExtTar);
			
			listaSoggettiTar = tarsuService.getListaSoggettiDichiarazioneTarsu(rot);
			 
			SitTTarOggetto oggetto = tarsuService.getOggettoByIdExt(rot);
			
			for(SoggettoTarsuDTO dto : listaSoggettiTar){
				
				if(oggetto!=null){
					
					//Verifica se il soggetto è locatario
					DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
					fillEnte(dataIn);
					dataIn.setObj(idExtTar);
					dataIn.setObj2(oggetto.getDatIni());
				
					dataIn.setObj3(dto.getSoggetto().getId());
					
					List<LocazioniA> locazioni = docfaTarReportService.getLocazioniDaTarsuBySoggCivicoData(dataIn);
					dto.setLocatarioIndTarsu(locazioni.size()>0);
					
					//Verifica se il soggetto è titolare
					dataIn = new DiagnosticheDataIn();
					fillEnte(dataIn);
					
					RicercaOggettoCatDTO ricCat = new RicercaOggettoCatDTO();
					fillEnte(ricCat);
					ricCat.setCodEnte(dataIn.getEnteId());
					ricCat.setFoglio(oggetto.getFoglio());
					ricCat.setParticella(oggetto.getNumero());
					ricCat.setUnimm(oggetto.getSub());
					ricCat.setDtVal(oggetto.getDatIni());
					dataIn.setRicercaOggettoCatDTO(ricCat);
					dataIn.setObj(dto.getSoggetto().getId());
					
					String titolarita = docfaTarReportService.getTitolaritaDaTarsuBySoggUiuData(dataIn);
					dto.setTitoloCatastoUiu(titolarita);
					
					/*Verifica se il soggetto è un familiare,di un titolare a catasto, 
					  alla data della dichiarazione TARSU*/
					
					//Estrazione di tutti i familiari del dichiarante tarsu alla data
					RicercaIciTarsuDTO ricercaTarFam = new RicercaIciTarsuDTO();
					fillEnte(ricercaTarFam);
					ricercaTarFam.setDataRif(yyyyMMdd.format(oggetto.getDatIni()));
					ricercaTarFam.setIdDwhOrigineSogg(dto.getSoggetto().getId());
					List<SitDPersona> familiariTarsu = docfaTarReportService.getFamiliariTarSoggAllaData(ricercaTarFam);
					List<String> listaIdFamTar = new ArrayList();
					for(SitDPersona tarFam : familiariTarsu){
						String idFamTar = tarFam.getId();
						listaIdFamTar.add(idFamTar);
					}
					
					
					//Estrazione di tutti i familiari dei titolari a catasto alla data della dichiarazione tarsu
					List<String> listaIdSoggAnagDaCat = new ArrayList();
					List<SoggettoCatastoDTO> soggetti = catastoService.getListaSoggettiUiuAllaDataByFPS(ricCat);
					for(SoggettoCatastoDTO s: soggetti){
						RicercaIciTarsuDTO rcat = new RicercaIciTarsuDTO();
						fillEnte(rcat);
						rcat.setIdDwhOrigineSogg(s.getConsSoggTab().getPkid().toString());
						List<String> listaIdSoggAnag = docfaTarReportService.getListaIdDwhSoggAnagFromCatasto(rcat);
						listaIdSoggAnagDaCat.addAll(listaIdSoggAnag);
					}
					
					//Per ciascun familiare del dichiarante tarsu, verifico se il suo ID anagrafico è presente nella lista dei soggetti titolari a catasto (trovati in anagrafe)
					boolean trovato = false;
					int iFam = 0;
					//getLogger().info("Lista Familiari da Tarsu: "+listaIdFamTar.size());
					//getLogger().info("Lista Titolari(Anagrafe): "+listaIdSoggAnagDaCat.size());
					
					while(!trovato && iFam < listaIdFamTar.size()){
						String currIdFamTar = listaIdFamTar.get(iFam);
						trovato = listaIdSoggAnagDaCat.contains(currIdFamTar);
						//getLogger().info("Familiare da Tarsu: "+currIdFamTar+"Presente in Catasto: "+trovato);
						iFam++;
					}
					dto.setFamiliareSoggCat(trovato);
				}
				
			}
			
		} catch (Throwable t) {
			super.addErrorMessage("subject.error", t.getMessage());
			t.printStackTrace();
		}
	}
	
	public void doCaricaTarsuFamiliari() {

		try {

			
			RicercaOggettoTarsuDTO rot = new RicercaOggettoTarsuDTO();
			fillEnte(rot);
			rot.setIdExtOgg(listaTar.get(Integer.parseInt(rowIndex)).getSitTTarOggetto().getIdExt());
			listaSoggettiTar = tarsuService.getListaSoggettiDichiarazioneTarsu(rot);
			 
			SitTTarOggetto oggetto = tarsuService.getOggettoByIdExt(rot);
			
			for(SoggettoTarsuDTO dto : listaSoggettiTar){
			
				if(oggetto!=null){
					DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
					fillEnte(dataIn);
					dataIn.setObj(listaTar.get(Integer.parseInt(rowIndex)).getSitTTarOggetto().getIdExt());
					dataIn.setObj2(oggetto.getDatIni());
				
					dataIn.setObj3(dto.getSoggetto().getId());
					
					List<LocazioniA> locazioni = docfaTarReportService.getLocazioniDaTarsuBySoggCivicoData(dataIn);
					
					dto.setLocatarioIndTarsu(locazioni.size()>0);
				
				}
				
			}

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
				List<Sitideco> lista = catastoService.getListaCategorieImmobile(ro);
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
				+ dto.getDocfaTarReport().getCodEnte()
				+ File.separatorChar
				+ dirDocfaPDF
				+ File.separatorChar
				+ sdf.format(dto.getDocfaTarReport().getFornitura()).substring(
						0, 6);
		this.getLogger().info("Percorso PDF Docfa:" +pathPdfDocfa);
		return pathPdfDocfa;
	}

	public String getIdRep() {
		return idRep;
	}

	public void setIdRep(String idRep) {
		this.idRep = idRep;
	}

	public DocfaTarReportDTO getDto() {
		return dto;
	}

	public void setDto(DocfaTarReportDTO dto) {
		this.dto = dto;
	}

	public List<SelectItem> getListaUI() {
		return listaUI;
	}

	public void setListaUI(List<SelectItem> listaUI) {
		this.listaUI = listaUI;
	}

	public String getIdRepUI() {
		return idRepUI;
	}

	public void setIdRepUI(String idRepUI) {
		this.idRepUI = idRepUI;
	}

	public UnitaImmobiliareTarDTO getSelectedUI() {
		return selectedUI;
	}

	public void setSelectedUI(UnitaImmobiliareTarDTO selectedUI) {
		this.selectedUI = selectedUI;
	}

	public String getIdRepDocfa() {
		return idRepDocfa;
	}

	public void setIdRepDocfa(String idRepDocfa) {
		this.idRepDocfa = idRepDocfa;
	}

	public DocfaTarReport getDocfa() {
		return docfa;
	}

	public void setDocfa(DocfaTarReport docfa) {
		this.docfa = docfa;
	}

	public String getIdExtTar() {
		return idExtTar;
	}

	public void setIdExtTar(String idExtTar) {
		this.idExtTar = idExtTar;
	}

	public List<SitTTarOggettoDTO> getListaTar() {
		return listaTar;
	}

	public void setListaTar(List<SitTTarOggettoDTO> listaTar) {
		this.listaTar = listaTar;
	}

	public RicercaIciTarsuDTO getRicercaIciTarsuDto() {
		return ricercaIciTarsuDto;
	}

	public void setRicercaIciTarsuDto(RicercaIciTarsuDTO ricercaIciTarsuDto) {
		this.ricercaIciTarsuDto = ricercaIciTarsuDto;
	}

	public List<SoggettoTarsuDTO> getListaSoggettiTar() {
		return listaSoggettiTar;
	}

	public void setListaSoggettiTar(List<SoggettoTarsuDTO> listaSoggettiTar) {
		this.listaSoggettiTar = listaSoggettiTar;
	}

	public boolean isFamiliare() {
		return familiare;
	}

	public void setFamiliare(boolean familiare) {
		this.familiare = familiare;
	}

	public String getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(String rowIndex) {
		this.rowIndex = rowIndex;
	}

	public void setRenderUiu(boolean renderUiu) {
		this.renderUiu = renderUiu;
	}

	public boolean isRenderUiu() {
		return renderUiu;
	}

}
