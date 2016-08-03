package it.webred.ct.service.spprof.web.user.bean.interventi;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import it.webred.cet.permission.CeTUser;
import it.webred.ct.config.model.AmComune;
import it.webred.ct.config.parameters.comune.ComuneService;
import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.concedilizie.ConcessioniEdilizieService;
import it.webred.ct.data.access.basic.pgt.PGTService;
import it.webred.ct.data.model.catasto.Sitideco;
import it.webred.ct.data.model.concedilizie.SitCConcessioni;
import it.webred.ct.service.spprof.data.access.SpProfAreaService;
import it.webred.ct.service.spprof.data.access.SpProfEdificiService;
import it.webred.ct.service.spprof.data.access.SpProfInterventoService;
import it.webred.ct.service.spprof.data.access.dto.EdificioDTO;
import it.webred.ct.service.spprof.data.access.dto.SpProfDTO;
import it.webred.ct.service.spprof.data.access.dto.UiuDTO;
import it.webred.ct.service.spprof.data.model.SSpAreaPart;
import it.webred.ct.service.spprof.data.model.SSpCedificato;
import it.webred.ct.service.spprof.data.model.SSpEdificioMinore;
import it.webred.ct.service.spprof.data.model.SSpIntervento;
import it.webred.ct.service.spprof.data.model.SSpTipologiaEdiMin;
import it.webred.ct.service.spprof.data.model.SSpUiu;
import it.webred.ct.service.spprof.data.model.SSpUnitaVol;
import it.webred.ct.service.spprof.web.user.SpProfBaseBean;
import it.webred.ct.service.spprof.web.user.bean.util.MapBean;
import it.webred.ct.service.spprof.web.user.bean.util.PageBean;

import org.apache.log4j.Logger;

public class UiuBean extends SpProfBaseBean {

	protected CatastoService catastoService = (CatastoService) getEjb("CT_Service", "CT_Service_Data_Access", "CatastoServiceBean");

	public SpProfAreaService spProfAreaService = (SpProfAreaService) getEjb("Servizio_SpProf", "Servizio_SpProf_EJB", "SpProfAreaServiceBean");

	public SpProfEdificiService spProfEdificiService = (SpProfEdificiService) getEjb("Servizio_SpProf", "Servizio_SpProf_EJB", "SpProfEdificiServiceBean");

	private String idEdificio;

	private String foglio;
	private String particella;

	private String idUnitaVol;
	private String numUiu;
	private String categoria;
	private String classe;
	private String note;

	private String index;
	private boolean mostraDati;
	private boolean update;

	private List<SelectItem> listaCategorie;
	private List<SelectItem> listaUnitaVol;
	private List<UiuDTO> listaUiu;
	private String filtroIdUnitaVol;

	private boolean caricaSolo;

	public void doNuovo() {

		try {

			idUnitaVol = "";
			numUiu = "";
			categoria = "";
			classe = "";
			note = "";

			caricaCategorie();

		} catch (Throwable t) {
			super.addErrorMessage("interfaccia.error", t.getMessage());
			logger.error(t);
		}

	}

	public void doLista() {

		listaUiu = new ArrayList<UiuDTO>();

		try {

			if (!caricaSolo) {
				PageBean pBean = (PageBean) getBeanReference("pageBean");
				pBean.goUiu();
			}
			caricaSolo = false;

			SpProfDTO dto = new SpProfDTO();
			dto.setEnteId(getEnte());
			dto.setObj(new Long(idEdificio));
			List<SSpUiu> lista = spProfEdificiService.getUiuByEdificio(dto);
			RicercaOggettoCatDTO ro = new RicercaOggettoCatDTO();
			ro.setEnteId(getEnte());
			for (SSpUiu u : lista) {
				UiuDTO uDto = new UiuDTO();
				uDto.setUiu(u);
				ro.setCodCategoria(u.getCategoria());
				Sitideco cat = catastoService.getSitideco(ro);
				uDto.setCategoriaDescr(cat.getDescription());
				if(filtroIdUnitaVol == null || filtroIdUnitaVol.equals(""))
					listaUiu.add(uDto);
				else if(u.getFkSpUnitaVol() != null && filtroIdUnitaVol.equals(u.getFkSpUnitaVol().toString()))
					listaUiu.add(uDto);
			}
			
			caricaUnitaVol();

		} catch (Throwable t) {
			super.addErrorMessage("interfaccia.error", t.getMessage());
			logger.error(t);
		}

	}

	public void doDettagli() {

		try {

			caricaCategorie();
			caricaUnitaVol();

			SSpUiu ua = listaUiu.get(new Integer(index)).getUiu();
			if (ua.getFkSpUnitaVol() != null)
				idUnitaVol = ua.getFkSpUnitaVol().toString();
			if (ua.getNumUiu() != null)
				numUiu = ua.getNumUiu().toString();
			categoria = ua.getCategoria();
			classe = ua.getClasse().toString();
			note = ua.getNote();

		} catch (Throwable t) {
			super.addErrorMessage("interfaccia.error", t.getMessage());
			logger.error(t);
		}

	}

	public void doSalva() {

		try {

			SSpUiu ua = new SSpUiu();
			if (update)
				ua = listaUiu.get(new Integer(index)).getUiu();

			ua.setFkSpEdificio(new Long(idEdificio));
			ua.setDtIns(new Date());
			ua.setUserIns(getUsername());
			if (update) {
				ua.setDtMod(new Date());
				ua.setUserMod(getUsername());
			}

			if (!idUnitaVol.equals(""))
				ua.setFkSpUnitaVol(new Long(idUnitaVol));
			if (!numUiu.equals(""))
				ua.setNumUiu(new BigDecimal(numUiu));
			ua.setNote(note);
			ua.setCategoria(categoria);
			ua.setClasse(classe);
			SpProfDTO dto = new SpProfDTO();
			dto.setEnteId(getEnte());
			dto.setObj(ua);

			if (update)
				spProfEdificiService.updateUiu(dto);
			else
				spProfEdificiService.saveUiu(dto);

			// refresh dati
			doLista();

			super.addInfoMessage("save");

		} catch (Throwable t) {
			super.addErrorMessage("save.error", t.getMessage());
			logger.error(t);
		}
	}

	public void doElimina() {

		try {

			SSpUiu uv = listaUiu.get(new Integer(index)).getUiu();
			SpProfDTO dto = new SpProfDTO();
			dto.setEnteId(getEnte());
			dto.setObj(uv.getIdSpUiu());
			spProfEdificiService.deleteUiu(dto);

			// ricarica lista
			doLista();

			super.addInfoMessage("delete");

		} catch (Throwable t) {
			super.addErrorMessage("delete.error", t.getMessage());
			logger.error(t);
		}
	}

	public void caricaCategorie() {

		if (listaCategorie == null) {
			listaCategorie = new ArrayList<SelectItem>();
			try {

				RicercaOggettoCatDTO ro = new RicercaOggettoCatDTO();
				ro.setEnteId(getEnte());
				List<Sitideco> lista = catastoService
						.getListaCategorieImmobile(ro);

				for (Sitideco cat : lista) {
					SelectItem item = new SelectItem(cat.getId().getCode(), cat
							.getId().getCode() + " - " + cat.getDescription());
					listaCategorie.add(item);
				}

			} catch (Throwable t) {
				super.addErrorMessage("interfaccia.error", t.getMessage());
				logger.error(t);
			}
		}

	}

	public void caricaUnitaVol() {

		listaUnitaVol = new ArrayList<SelectItem>();
		try {

			SpProfDTO dto = new SpProfDTO();
			dto.setEnteId(getEnte());
			dto.setObj(new Long(idEdificio));
			List<SSpUnitaVol> lista = spProfEdificiService
					.getUnitaVolByEdificio(dto);

			for (SSpUnitaVol uv : lista) {
				SelectItem item = new SelectItem(uv.getIdSpUnitaVol(), "Id: "
						+ uv.getIdSpUnitaVol() + ", Altezza: " + uv.getAltezza()
						+ ", Volume:" + uv.getVolume());
				listaUnitaVol.add(item);
			}

		} catch (Throwable t) {
			super.addErrorMessage("interfaccia.error", t.getMessage());
			logger.error(t);
		}
	}

	public String getIdEdificio() {
		return idEdificio;
	}

	public void setIdEdificio(String idEdificio) {
		this.idEdificio = idEdificio;
	}

	public String getIdUnitaVol() {
		return idUnitaVol;
	}

	public void setIdUnitaVol(String idUnitaVol) {
		this.idUnitaVol = idUnitaVol;
	}

	public String getNumUiu() {
		return numUiu;
	}

	public void setNumUiu(String numUiu) {
		this.numUiu = numUiu;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public boolean isMostraDati() {
		return mostraDati;
	}

	public void setMostraDati(boolean mostraDati) {
		this.mostraDati = mostraDati;
	}

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	public List<SelectItem> getListaCategorie() {
		return listaCategorie;
	}

	public void setListaCategorie(List<SelectItem> listaCategorie) {
		this.listaCategorie = listaCategorie;
	}

	public List<SelectItem> getListaUnitaVol() {
		return listaUnitaVol;
	}

	public void setListaUnitaVol(List<SelectItem> listaUnitaVol) {
		this.listaUnitaVol = listaUnitaVol;
	}

	public List<UiuDTO> getListaUiu() {
		return listaUiu;
	}

	public void setListaUiu(List<UiuDTO> listaUiu) {
		this.listaUiu = listaUiu;
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

	public boolean isCaricaSolo() {
		return caricaSolo;
	}

	public void setCaricaSolo(boolean caricaSolo) {
		this.caricaSolo = caricaSolo;
	}

	public String getFiltroIdUnitaVol() {
		return filtroIdUnitaVol;
	}

	public void setFiltroIdUnitaVol(String filtroIdUnitaVol) {
		this.filtroIdUnitaVol = filtroIdUnitaVol;
	}

}
