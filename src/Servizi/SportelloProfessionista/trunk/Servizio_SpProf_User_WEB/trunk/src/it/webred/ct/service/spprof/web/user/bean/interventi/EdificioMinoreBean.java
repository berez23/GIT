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
import it.webred.ct.service.spprof.data.model.SSpAreaPart;
import it.webred.ct.service.spprof.data.model.SSpCedificato;
import it.webred.ct.service.spprof.data.model.SSpEdificioMinore;
import it.webred.ct.service.spprof.data.model.SSpIntervento;
import it.webred.ct.service.spprof.data.model.SSpTipologiaEdiMin;
import it.webred.ct.service.spprof.web.user.SpProfBaseBean;
import it.webred.ct.service.spprof.web.user.bean.util.MapBean;
import it.webred.ct.service.spprof.web.user.bean.util.PageBean;

import org.apache.log4j.Logger;

public class EdificioMinoreBean extends SpProfBaseBean {

	public SpProfAreaService spProfAreaService = (SpProfAreaService) getEjb("Servizio_SpProf", "Servizio_SpProf_EJB", "SpProfAreaServiceBean");

	public SpProfEdificiService spProfEdificiService = (SpProfEdificiService) getEjb("Servizio_SpProf", "Servizio_SpProf_EJB", "SpProfEdificiServiceBean");

	private String idIntervento;
	private String idAreaPart;
	private String note;
	private String tipologia;
	private boolean precario;

	private String index;
	private boolean update;
	private EdificioDTO edificioDTO;
	
	private List<SelectItem> selectParticella;
	private List<SelectItem> selectTipologia;

	public void doNuovo() {

		selectParticella = new ArrayList<SelectItem>();
		selectTipologia = new ArrayList<SelectItem>();
		idAreaPart = "";
		note = "";
		tipologia = "";

		try {

			PageBean pBean = (PageBean) getBeanReference("pageBean");
			pBean.goNuovoEdificioMin();

			SpProfDTO dto = new SpProfDTO();
			dto.setEnteId(getEnte());
			dto.setObj(new Long(idIntervento));
			List<SSpAreaPart> listaPart = spProfAreaService
					.getListaAreaPart(dto);
			for (SSpAreaPart part : listaPart) {

				selectParticella.add(new SelectItem(part.getIdSpAreaPart(),
						"Foglio: " + part.getFoglio() + ", Particella: "
								+ part.getParticella()));

			}

			List<SSpTipologiaEdiMin> listaTip = spProfEdificiService
					.getTipologiaEdificiMinori(dto);
			for (SSpTipologiaEdiMin tip : listaTip) {

				selectTipologia.add(new SelectItem(tip.getCodTipologiaEdiMin(),
						tip.getDescr()));

			}

		} catch (Throwable t) {
			super.addErrorMessage("interfaccia.error", t.getMessage());
			logger.error(t);
		}

	}
	
	public void doDettagli() {

		selectParticella = new ArrayList<SelectItem>();
		selectTipologia = new ArrayList<SelectItem>();

		try {

			PageBean pBean = (PageBean) getBeanReference("pageBean");
			pBean.goNuovoEdificioMin();

			//carico dati dell'edificio
			DatiBean dBean = (DatiBean) getBeanReference("datiBean");
			edificioDTO = dBean.getListaEdificiMinori().get(new Integer(index));
			idAreaPart = edificioDTO.getEdificato().getFkSpAreaPart().toString();
			note = edificioDTO.getEdificato().getNote();
			tipologia = edificioDTO.getEdificioMinore().getFkSpTipologiaEdiMin();
			
			//carico dati modifica
			SpProfDTO dto = new SpProfDTO();
			dto.setEnteId(getEnte());
			dto.setObj(new Long(idIntervento));
			List<SSpAreaPart> listaPart = spProfAreaService
					.getListaAreaPart(dto);
			for (SSpAreaPart part : listaPart) {

				selectParticella.add(new SelectItem(part.getIdSpAreaPart(),
						"Foglio: " + part.getFoglio() + ", Particella: "
								+ part.getParticella()));

			}

			List<SSpTipologiaEdiMin> listaTip = spProfEdificiService
					.getTipologiaEdificiMinori(dto);
			for (SSpTipologiaEdiMin tip : listaTip) {

				selectTipologia.add(new SelectItem(tip.getCodTipologiaEdiMin(),
						tip.getDescr()));

			}

		} catch (Throwable t) {
			super.addErrorMessage("interfaccia.error", t.getMessage());
			logger.error(t);
		}

	}

	public void doSalva() {

		if (idAreaPart.equals("")) {
			super.addErrorMessage("save.particella.error", "");
		}else {

			try {

				SSpCedificato cedi = new SSpCedificato();
				SSpEdificioMinore edi = new SSpEdificioMinore();
				if(update){
					cedi = edificioDTO.getEdificato();
					edi = edificioDTO.getEdificioMinore();
				}
				
				cedi.setFkSpAreaPart(new Long(idAreaPart));
				cedi.setNote(note);
				cedi.setDtIns(new Date());
				cedi.setUserIns(getUsername());
				if(update){
					cedi.setDtMod(new Date());
					cedi.setUserMod(getUsername());
				}
				SpProfDTO dto = new SpProfDTO();
				dto.setEnteId(getEnte());
				dto.setObj(cedi);

				Long idEdificato = null;
				if(update)
					spProfEdificiService.updateEdificato(dto);
				else {
					idEdificato = spProfEdificiService.saveEdificato(dto);
					edi.setFkSpCedificato(idEdificato);
				}

				if(!tipologia.equals(""))
					edi.setFkSpTipologiaEdiMin(tipologia);
				edi.setPrecario(new BigDecimal(precario ? "1" : "0"));
				edi.setDtIns(new Date());
				if(update){
					edi.setDtMod(new Date());
					edi.setUserMod(getUsername());
				}
				dto.setObj(edi);
				
				if(update)
					spProfEdificiService.updateEdificioMinore(dto);
				else spProfEdificiService.saveEdificioMinore(dto);

				//refresh dati
				DatiBean dBean = (DatiBean) getBeanReference("datiBean");
				dBean.doDettaglio();
				
				super.addInfoMessage("save");

			} catch (Throwable t) {
				super.addErrorMessage("save.error", t.getMessage());
				logger.error(t);
			}
		}

	}

	public void doElimina() {

		try {

			DatiBean dBean = (DatiBean) getBeanReference("datiBean");
			edificioDTO = dBean.getListaEdificiMinori().get(new Integer(index));
			SpProfDTO dto = new SpProfDTO();
			dto.setEnteId(getEnte());
			dto.setObj(new Long(edificioDTO.getEdificioMinore().getFkSpCedificato()));
			spProfEdificiService.deleteEdificioMinore(dto);
			spProfEdificiService.deleteEdificato(dto);

			//ricarica lista
			dBean.doDettaglio();
			
			super.addInfoMessage("delete");

		} catch (Throwable t) {
			super.addErrorMessage("delete.error", t.getMessage());
			logger.error(t);
		}
	}
	
	public String getIdIntervento() {
		return idIntervento;
	}

	public void setIdIntervento(String idIntervento) {
		this.idIntervento = idIntervento;
	}

	public List<SelectItem> getSelectParticella() {
		return selectParticella;
	}

	public void setSelectParticella(List<SelectItem> selectParticella) {
		this.selectParticella = selectParticella;
	}

	public String getIdAreaPart() {
		return idAreaPart;
	}

	public void setIdAreaPart(String idAreaPart) {
		this.idAreaPart = idAreaPart;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getTipologia() {
		return tipologia;
	}

	public void setTipologia(String tipologia) {
		this.tipologia = tipologia;
	}

	public boolean isPrecario() {
		return precario;
	}

	public void setPrecario(boolean precario) {
		this.precario = precario;
	}

	public List<SelectItem> getSelectTipologia() {
		return selectTipologia;
	}

	public void setSelectTipologia(List<SelectItem> selectTipologia) {
		this.selectTipologia = selectTipologia;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	public EdificioDTO getEdificioDTO() {
		return edificioDTO;
	}

	public void setEdificioDTO(EdificioDTO edificioDTO) {
		this.edificioDTO = edificioDTO;
	}

}
