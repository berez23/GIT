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
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.concedilizie.ConcessioniEdilizieService;
import it.webred.ct.data.access.basic.pgt.PGTService;
import it.webred.ct.data.model.concedilizie.SitCConcessioni;
import it.webred.ct.service.spprof.data.access.SpProfAreaService;
import it.webred.ct.service.spprof.data.access.SpProfEdificiService;
import it.webred.ct.service.spprof.data.access.SpProfInterventoService;
import it.webred.ct.service.spprof.data.access.dto.EdificioDTO;
import it.webred.ct.service.spprof.data.access.dto.SpProfDTO;
import it.webred.ct.service.spprof.data.access.dto.UnitaVolDTO;
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

public class UnitaVolBean extends SpProfBaseBean {

	public SpProfEdificiService spProfEdificiService = (SpProfEdificiService) getEjb("Servizio_SpProf", "Servizio_SpProf_EJB", "SpProfEdificiServiceBean");

	private String idEdificio;

	private String altezza;
	private String supDbt;
	private String slp;
	private String volume;
	private String pianiFt;
	private String pianiEt;
	private String note;

	private String index;
	private boolean mostraDati;
	private boolean update;

	private List<UnitaVolDTO> listaUnitaVol;
	
	private boolean caricaSolo;

	public void doNuovo() {

		try {

			altezza = "";
			supDbt = "";
			slp = "";
			volume = "";
			pianiFt = "";
			pianiEt = "";
			note = "";

		} catch (Throwable t) {
			super.addErrorMessage("interfaccia.error", t.getMessage());
			logger.error(t);
		}

	}

	public void doLista() {

		listaUnitaVol = new ArrayList<UnitaVolDTO>();

		try {

			if(!caricaSolo){
				PageBean pBean = (PageBean) getBeanReference("pageBean");
				pBean.goUnitaVol();
			}
			caricaSolo = false;
			
			SpProfDTO dto = new SpProfDTO();
			dto.setEnteId(getEnte());
			dto.setObj(new Long(idEdificio));
			List<SSpUnitaVol> listaUv = spProfEdificiService.getUnitaVolByEdificio(dto);
			for(SSpUnitaVol uv: listaUv){
				UnitaVolDTO uvdto = new UnitaVolDTO();
				dto.setObj(new Long(uv.getIdSpUnitaVol()));
				List<SSpUiu> listaUiu = spProfEdificiService.getUiuByUnitaVol(dto);
				uvdto.setNumUiuCollegate(listaUiu.size());
				uvdto.setUnitaVol(uv);
				listaUnitaVol.add(uvdto);
			}

		} catch (Throwable t) {
			super.addErrorMessage("interfaccia.error", t.getMessage());
			logger.error(t);
		}

	}

	public void doDettagli() {

		try {

			SSpUnitaVol uv = listaUnitaVol.get(new Integer(index)).getUnitaVol();
			if (uv.getAltezza() != null)
				altezza = uv.getAltezza().toString();
			if (uv.getSupDbt() != null)
				supDbt = uv.getSupDbt().toString();
			if (uv.getSlp() != null)
				slp = uv.getSlp().toString();
			if (uv.getVolume() != null)
				volume = uv.getVolume().toString();
			if (uv.getPianiFt() != null)
				pianiFt = uv.getPianiFt().toString();
			if (uv.getPianiEt() != null)
				pianiEt = uv.getPianiEt().toString();
			note = uv.getNote();

		} catch (Throwable t) {
			super.addErrorMessage("interfaccia.error", t.getMessage());
			logger.error(t);
		}

	}

	public void doSalva() {

		try {

			SSpUnitaVol uv = new SSpUnitaVol();
			if (update)
				uv = listaUnitaVol.get(new Integer(index)).getUnitaVol();

			uv.setFkSpEdificio(new Long(idEdificio));
			uv.setDtIns(new Date());
			uv.setUserIns(getUsername());
			if (update) {
				uv.setDtMod(new Date());
				uv.setUserMod(getUsername());
			}

			if (!altezza.equals(""))
				uv.setAltezza(new BigDecimal(altezza));
			uv.setNote(note);
			if (!pianiEt.equals(""))
				uv.setPianiEt(new BigDecimal(pianiEt));
			if (!pianiFt.equals(""))
				uv.setPianiFt(new BigDecimal(pianiFt));
			if (!pianiFt.equals(""))
				uv.setPianiFt(new BigDecimal(pianiFt));
			if (!slp.equals(""))
				uv.setSlp(new BigDecimal(slp));
			if (!supDbt.equals(""))
				uv.setSupDbt(new BigDecimal(supDbt));
			if (!volume.equals(""))
				uv.setVolume(new BigDecimal(volume));

			SpProfDTO dto = new SpProfDTO();
			dto.setEnteId(getEnte());
			dto.setObj(uv);

			if (update)
				spProfEdificiService.updateUnitaVol(dto);
			else
				spProfEdificiService.saveUnitaVol(dto);

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

			SSpUnitaVol uv = listaUnitaVol.get(new Integer(index)).getUnitaVol();
			SpProfDTO dto = new SpProfDTO();
			dto.setEnteId(getEnte());
			dto.setObj(uv.getIdSpUnitaVol());
			spProfEdificiService.deleteUnitaVol(dto);

			// ricarica lista
			doLista();

			super.addInfoMessage("delete");

		} catch (Throwable t) {
			super.addErrorMessage("delete.error", t.getMessage());
			logger.error(t);
		}
	}

	public String getIdEdificio() {
		return idEdificio;
	}

	public void setIdEdificio(String idEdificio) {
		this.idEdificio = idEdificio;
	}

	public String getAltezza() {
		return altezza;
	}

	public void setAltezza(String altezza) {
		this.altezza = altezza;
	}

	public String getSupDbt() {
		return supDbt;
	}

	public void setSupDbt(String supDbt) {
		this.supDbt = supDbt;
	}

	public String getPianiFt() {
		return pianiFt;
	}

	public void setPianiFt(String pianiFt) {
		this.pianiFt = pianiFt;
	}

	public String getPianiEt() {
		return pianiEt;
	}

	public void setPianiEt(String pianiEt) {
		this.pianiEt = pianiEt;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public List<UnitaVolDTO> getListaUnitaVol() {
		return listaUnitaVol;
	}

	public void setListaUnitaVol(List<UnitaVolDTO> listaUnitaVol) {
		this.listaUnitaVol = listaUnitaVol;
	}

	public String getSlp() {
		return slp;
	}

	public void setSlp(String slp) {
		this.slp = slp;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
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

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public boolean isCaricaSolo() {
		return caricaSolo;
	}

	public void setCaricaSolo(boolean caricaSolo) {
		this.caricaSolo = caricaSolo;
	}

}
