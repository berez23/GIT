package it.webred.ct.service.spprof.web.user.bean.nuovo;

import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.PartDTO;
import it.webred.ct.data.access.basic.common.CommonDataIn;
import it.webred.ct.data.access.basic.common.CommonService;
import it.webred.ct.data.access.basic.pgt.PGTService;
import it.webred.ct.data.access.basic.pgt.dto.RicercaPgtDTO;
import it.webred.ct.data.model.catasto.Sitipart;
import it.webred.ct.data.model.catasto.Sitisuolo;
import it.webred.ct.data.model.pgt.PgtSqlLayer;
import it.webred.ct.data.spatial.JGeometryType;
import it.webred.ct.service.geospatial.data.access.GeospatialAreaLayerService;
import it.webred.ct.service.geospatial.data.access.GeospatialLocalizzaService;
import it.webred.ct.service.geospatial.data.access.dto.ParticellaDTO;
import it.webred.ct.service.geospatial.data.access.dto.SpatialLayerDTO;
import it.webred.ct.service.spprof.data.access.SpProfAreaService;
import it.webred.ct.service.spprof.data.access.SpProfInterventoService;
import it.webred.ct.service.spprof.data.access.SpProfLocalizzaService;
import it.webred.ct.service.spprof.data.access.dto.CivicoDTO;
import it.webred.ct.service.spprof.data.access.dto.SpProfAreaDTO;
import it.webred.ct.service.spprof.data.access.dto.SpProfDTO;
import it.webred.ct.service.spprof.data.model.SSpAreaFabb;
import it.webred.ct.service.spprof.data.model.SSpAreaLayer;
import it.webred.ct.service.spprof.data.model.SSpAreaPart;
import it.webred.ct.service.spprof.data.model.SSpIntervento;
import it.webred.ct.service.spprof.web.user.SpProfBaseBean;
import it.webred.ct.service.spprof.web.user.bean.interventi.DatiBean;
import it.webred.ct.service.spprof.web.user.bean.util.PageBean;
import it.webred.ct.service.spprof.web.user.bean.util.UserBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import oracle.spatial.geometry.JGeometry;

public class DataProviderImpl extends SpProfBaseBean implements DataProvider {

	private CivicoDTO criteriaCiv = new CivicoDTO();
	private ParticellaDTO criteriaPart = new ParticellaDTO();
	private ParticellaDTO criteriaPartByCivico = new ParticellaDTO();

	private boolean renderCivici;
	private boolean renderParticelle;
	private boolean renderParticelleByCivico;

	private String foglioSel;
	private String particellaSel;
	private String subSel;
	private String index;
	private String idIntervento;

	private String listaFogliRicerca;
	private String listaParticelleRicerca;
	private List<ParticellaDTO> listaSelezione;

	private List<SelectItem> listaLayer = new ArrayList<SelectItem>();
	private List<PgtSqlLayer> listaPgtSqlLayer = new ArrayList<PgtSqlLayer>();
	private List<String> layerSelezionati;

	public SpProfLocalizzaService localizzaService = (SpProfLocalizzaService) getEjb("Servizio_SpProf", "Servizio_SpProf_EJB", "SpProfLocalizzaServiceBean");

	public SpProfAreaService spProfAreaService = (SpProfAreaService) getEjb("Servizio_SpProf", "Servizio_SpProf_EJB", "SpProfAreaServiceBean");

	public SpProfInterventoService spProfInterventoService = (SpProfInterventoService) getEjb("Servizio_SpProf", "Servizio_SpProf_EJB", "SpProfInterventoServiceBean");

	public GeospatialLocalizzaService geospatialLocalizzaService = (GeospatialLocalizzaService) getEjb("GeospatialProvider", "GeospatialProvider_EJB", "GeospatialLocalizzaServiceBean");

	public GeospatialAreaLayerService geospatialAreaLayerService = (GeospatialAreaLayerService) getEjb("GeospatialProvider", "GeospatialProvider_EJB", "GeospatialAreaLayerServiceBean");

	public PGTService pgtService = (PGTService) getEjb("CT_Service", "CT_Service_Data_Access", "PGTServiceBean");

	public CatastoService catastoService = (CatastoService) getEjb("CT_Service", "CT_Service_Data_Access", "CatastoServiceBean");
	
	public CommonService commonService = (CommonService) getEjb("CT_Service", "CT_Service_Data_Access", "CommonServiceBean");

	public Long getViaCount() {
		criteriaCiv.setEnteId(getEnte());
		Long result = localizzaService.getListaCiviciCount(criteriaCiv);
		return result;
	}

	public List<CivicoDTO> getViaByRange(int start, int rowNumber) {

		try {

			criteriaCiv.setStartRecord(start);
			criteriaCiv.setNumRecord(rowNumber);
			criteriaCiv.setEnteId(getEnte());
			return localizzaService.getListaCivici(criteriaCiv);

		} catch (Throwable t) {
			super.addErrorMessage("ricerca.error", t.getMessage());
			logger.error(t);
			return new ArrayList<CivicoDTO>();
		}
	}

	public Long getParticelleCount() {
		criteriaPart.setEnteId(getEnte());
		Long result = localizzaService.getListaParticelleCount(criteriaPart);
		return result;
	}

	public List<ParticellaDTO> getParticelleByRange(int start, int rowNumber) {

		try {

			criteriaPart.setStartRecord(start);
			criteriaPart.setNumRecord(rowNumber);
			criteriaPart.setEnteId(getEnte());
			return localizzaService.getListaParticelle(criteriaPart);

		} catch (Throwable t) {
			super.addErrorMessage("ricerca.error", t.getMessage());
			logger.error(t);
			return new ArrayList<ParticellaDTO>();
		}
	}

	public Long getParticelleByCivicoCount() {
		criteriaPartByCivico.setEnteId(getEnte());
		criteriaPartByCivico.setCodNazionale(getEnte());
		Long result = geospatialLocalizzaService
				.getListaParticelleCount(criteriaPartByCivico);
		return result;
	}

	public List<ParticellaDTO> getParticelleByCivicoRange(int start,
			int rowNumber) {

		try {

			criteriaPartByCivico.setStartRecord(start);
			criteriaPartByCivico.setNumRecord(rowNumber);
			criteriaPartByCivico.setEnteId(getEnte());
			criteriaPartByCivico.setCodNazionale(getEnte());
			return geospatialLocalizzaService
					.getListaParticelle(criteriaPartByCivico);

		} catch (Throwable t) {
			super.addErrorMessage("ricerca.error", t.getMessage());
			logger.error(t);
			return new ArrayList<ParticellaDTO>();
		}
	}

	public void doSeleziona() {
		if (listaSelezione == null)
			listaSelezione = new ArrayList<ParticellaDTO>();
		boolean trovato = false;
		for (ParticellaDTO part : listaSelezione) {
			if (part.getFoglio().equals(foglioSel)
					&& part.getParticella().equals(particellaSel))
				trovato = true;
		}
		if (trovato)
			super.addErrorMessage("selezione.esistente", "");
		else {
			ParticellaDTO dto = new ParticellaDTO();

			dto.setFoglio(foglioSel);
			dto.setParticella(particellaSel);
			dto.setSub(subSel);
			listaSelezione.add(dto);
		}
	}

	public void doElimina() {
		listaSelezione.remove(Integer.valueOf(index).intValue());
	}

	public void doCaricaLayer() {

		PageBean pBean = (PageBean) getBeanReference("pageBean");
		pBean.goSceltaLayer();
		listaLayer = new ArrayList<SelectItem>();

		try {

			RicercaPgtDTO r = new RicercaPgtDTO();
			r.setEnteId(getEnte());
			r.setUserId(getUsername());
			listaPgtSqlLayer = pgtService.getListaLayersDownloadable(r);

			for (PgtSqlLayer layer : listaPgtSqlLayer) {
				SelectItem item = new SelectItem(layer.getId().getId()
						.toString(), layer.getDesLayer());
				listaLayer.add(item);
			}

		} catch (Throwable t) {
			super.addErrorMessage("layer.error", t.getMessage());
			logger.error(t);
		}
	}

	public void doCreaIntervento() {

		String message = "save";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SpProfAreaDTO spProfAreaDTO = new SpProfAreaDTO();
		spProfAreaDTO.setEnteId(getEnte());
		spProfAreaDTO.setUserId(getUsername());
		List<SSpAreaPart> listaParticelle = new ArrayList<SSpAreaPart>();
		List<SSpAreaFabb> listaFabbricati = new ArrayList<SSpAreaFabb>();
		List<PgtSqlLayer> listaLayer = new ArrayList<PgtSqlLayer>();
		List<SSpAreaLayer> listaAreaLayer = new ArrayList<SSpAreaLayer>();

		Long idIntervento = null;

		try {
			Date maxdate = sdf.parse("31/12/9999");

			SpProfDTO dataIn = new SpProfDTO();
			dataIn.setEnteId(getEnte());
			SSpIntervento intervento = new SSpIntervento();
			intervento.setUserIns(getUsername());
			intervento.setDtIns(new Date());
			intervento.setStato("IN CREAZIONE");
			UserBean uBean = (UserBean) getBeanReference("userBean");
			intervento.setFkSogg(uBean.controllaAccesso());
			dataIn.setObj(intervento);
			idIntervento = spProfInterventoService.save(dataIn);

			for (ParticellaDTO particella : listaSelezione) {
				PartDTO dto = new PartDTO();
				dto.setEnteId(getEnte());
				dto.setCodiFiscLuna(getEnte());
				dto.setFoglio(new Long(particella.getFoglio()));
				dto.setParticella(particella.getParticella());
				dto.setMaxdate(maxdate);

				List<Sitipart> listapart = catastoService
						.getParticelleSitipart(dto);
				for (Sitipart siti : listapart) {
					listaParticelle.add(createAreaPart(siti, particella,
							idIntervento));
				}
			}

			for (ParticellaDTO particella : listaSelezione) {
				PartDTO dto = new PartDTO();
				dto.setEnteId(getEnte());
				dto.setCodiFiscLuna(getEnte());
				dto.setFoglio(new Long(particella.getFoglio()));
				dto.setParticella(particella.getParticella());
				dto.setMaxdate(maxdate);

				List<Sitisuolo> listasuolo = catastoService
						.getParticelleSitisuolo(dto);
				for (Sitisuolo siti : listasuolo) {
					listaFabbricati.add(createAreaFabb(siti, particella,
							idIntervento));
				}
			}

			RicercaPgtDTO rp = new RicercaPgtDTO();
			rp.setEnteId(getEnte());
			rp.setStandard("N");

			for (String layer : layerSelezionati) {

				rp.setId(new Long(layer));
				listaLayer.add(pgtService.getLayerByPK(rp));
			}

			for (ParticellaDTO particella : listaSelezione) {

				particella.setLista(listaLayer);
				particella.setEnteId(getEnte());
				List<SpatialLayerDTO> spatial = geospatialAreaLayerService
						.getLayer(particella);

				for (SpatialLayerDTO siti : spatial) {
					listaAreaLayer.add(createAreaLayer(siti, particella,
							idIntervento));
				}

			}

			spProfAreaDTO.setListaAreaPart(listaParticelle);
			spProfAreaDTO.setListaAreaFabb(listaFabbricati);
			spProfAreaDTO.setListaAreaLayer(listaAreaLayer);
			spProfAreaDTO.setFkSpIntervento(idIntervento);

			Long esito = spProfAreaService.saveAllArea(spProfAreaDTO);
			
			//validazione layer
			CommonDataIn cDataIn = new CommonDataIn();
			cDataIn.setEnteId(getEnte());
			cDataIn.setUserId(getUsername());
			cDataIn.setObj("select count(*) from S_SP_AREA_FABB a where SDO_GEOM.VALIDATE_GEOMETRY_WITH_CONTEXT(a.shape , 0.000005) != 'TRUE'");
			commonService.executeNativeQuery(cDataIn);
			cDataIn.setObj("select count(*) from S_SP_AREA_LAYER a where SDO_GEOM.VALIDATE_GEOMETRY_WITH_CONTEXT(a.shape , 0.000005) != 'TRUE'");
			commonService.executeNativeQuery(cDataIn);
			cDataIn.setObj("select count(*) from S_SP_AREA_PART a where SDO_GEOM.VALIDATE_GEOMETRY_WITH_CONTEXT(a.shape , 0.000005) != 'TRUE'");
			commonService.executeNativeQuery(cDataIn);

			if (esito == null || esito.intValue() == -1) {

				super.addErrorMessage(message + ".error", "");
			} else {

				// vai a dati intervento creato
				DatiBean datiBean = (DatiBean) getBeanReference("datiBean");
				datiBean.setIdIntervento(idIntervento.toString());
				datiBean.doDettaglio();

				super.addInfoMessage("save.nuovo");
			}

		} catch (Throwable t) {
			if (idIntervento != null) {
				SpProfDTO dataIn = new SpProfDTO();
				dataIn.setEnteId(getEnte());
				dataIn.setObj(idIntervento);
				spProfInterventoService.delete(dataIn);
			}
			super.addErrorMessage(message + ".error", t.getMessage());
			logger.error(t);
		}

	}

	private SSpAreaPart createAreaPart(Sitipart siti, ParticellaDTO dto,
			Long idIntervento) {

		SSpAreaPart area = new SSpAreaPart();
		area.setCodNazionale(getEnte());
		area.setDataFineVal(siti.getId().getDataFineVal());
		area.setDtIns(new Date());
		area.setFoglio(new Long(dto.getFoglio()));
		area.setParticella(dto.getParticella());
		area.setShape(siti.getShape());
		area.setSub(dto.getSub());
		area.setUserIns(getUsername());
		area.setFkSpIntervento(idIntervento);

		return area;
	}

	private SSpAreaFabb createAreaFabb(Sitisuolo siti, ParticellaDTO dto,
			Long idIntervento) {

		SSpAreaFabb area = new SSpAreaFabb();
		area.setCodNazionale(getEnte());
		area.setDataFineVal(siti.getId().getDataFineVal());
		area.setDtIns(new Date());
		area.setFoglio(new Long(dto.getFoglio()));
		area.setParticella(dto.getParticella());
		area.setShape(siti.getShape());
		area.setSub(dto.getSub());
		area.setProgPoligono(siti.getId().getProgPoligono());
		area.setUserIns(getUsername());
		area.setFkSpIntervento(idIntervento);

		return area;
	}

	private SSpAreaLayer createAreaLayer(SpatialLayerDTO layer,
			ParticellaDTO dto, Long idIntervento) {

		SSpAreaLayer area = new SSpAreaLayer();
		area.setCodiceTema(layer.getCodiceTema());
		area.setDesLayer(layer.getDesLayer());
		area.setDtIns(new Date());
		area.setDesTema(layer.getDesTema());
		area.setIdGeometriaLayer(layer.getIdGeometriaLayer());
		area.setShape(new JGeometryType((JGeometry) layer.getShape()));
		area.setNameTable(layer.getNameTable());
		area.setShapeType("POLYGON");
		area.setTipoLayer(layer.getTipoLayer());
		area.setFkSpIntervento(idIntervento);

		return area;
	}

	public void resetData() {
		criteriaCiv = new CivicoDTO();
		criteriaPart = new ParticellaDTO();
		criteriaPartByCivico = new ParticellaDTO();
	}

	public CivicoDTO getCriteriaCiv() {
		return criteriaCiv;
	}

	public void setCriteriaCiv(CivicoDTO criteriaCiv) {
		this.criteriaCiv = criteriaCiv;
	}

	public ParticellaDTO getCriteriaPart() {
		return criteriaPart;
	}

	public void setCriteriaPart(ParticellaDTO criteriaPart) {
		this.criteriaPart = criteriaPart;
	}

	public boolean isRenderCivici() {
		return renderCivici;
	}

	public void setRenderCivici(boolean renderCivici) {
		this.renderCivici = renderCivici;
	}

	public boolean isRenderParticelle() {
		return renderParticelle;
	}

	public void setRenderParticelle(boolean renderParticelle) {
		this.renderParticelle = renderParticelle;
	}

	public boolean isRenderParticelleByCivico() {
		return renderParticelleByCivico;
	}

	public void setRenderParticelleByCivico(boolean renderParticelleByCivico) {
		this.renderParticelleByCivico = renderParticelleByCivico;
	}

	public ParticellaDTO getCriteriaPartByCivico() {
		return criteriaPartByCivico;
	}

	public void setCriteriaPartByCivico(ParticellaDTO criteriaPartByCivico) {
		this.criteriaPartByCivico = criteriaPartByCivico;
	}

	public List<ParticellaDTO> getListaSelezione() {
		return listaSelezione;
	}

	public void setListaSelezione(List<ParticellaDTO> listaSelezione) {
		this.listaSelezione = listaSelezione;
	}

	public String getFoglioSel() {
		return foglioSel;
	}

	public void setFoglioSel(String foglioSel) {
		this.foglioSel = foglioSel;
	}

	public String getParticellaSel() {
		return particellaSel;
	}

	public void setParticellaSel(String particellaSel) {
		this.particellaSel = particellaSel;
	}

	public String getListaFogliRicerca() {
		return listaFogliRicerca;
	}

	public void setListaFogliRicerca(String listaFogliRicerca) {
		this.listaFogliRicerca = listaFogliRicerca;
	}

	public String getListaParticelleRicerca() {
		return listaParticelleRicerca;
	}

	public void setListaParticelleRicerca(String listaParticelleRicerca) {
		this.listaParticelleRicerca = listaParticelleRicerca;
	}

	public String getSubSel() {
		return subSel;
	}

	public void setSubSel(String subSel) {
		this.subSel = subSel;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public List<SelectItem> getListaLayer() {
		return listaLayer;
	}

	public void setListaLayer(List<SelectItem> listaLayer) {
		this.listaLayer = listaLayer;
	}

	public List<PgtSqlLayer> getListaPgtSqlLayer() {
		return listaPgtSqlLayer;
	}

	public void setListaPgtSqlLayer(List<PgtSqlLayer> listaPgtSqlLayer) {
		this.listaPgtSqlLayer = listaPgtSqlLayer;
	}

	public List<String> getLayerSelezionati() {
		return layerSelezionati;
	}

	public void setLayerSelezionati(List<String> layerSelezionati) {
		this.layerSelezionati = layerSelezionati;
	}

	public String getIdIntervento() {
		return idIntervento;
	}

	public void setIdIntervento(String idIntervento) {
		this.idIntervento = idIntervento;
	}

}
