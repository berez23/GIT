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
import it.webred.ct.service.geospatial.data.access.dto.ParticellaDTO;
import it.webred.ct.service.spprof.data.access.SpProfAreaService;
import it.webred.ct.service.spprof.data.access.SpProfEdificiService;
import it.webred.ct.service.spprof.data.access.SpProfInterventoService;
import it.webred.ct.service.spprof.data.access.SpProfLocalizzaService;
import it.webred.ct.service.spprof.data.access.dto.CivicoDTO;
import it.webred.ct.service.spprof.data.access.dto.EdificioDTO;
import it.webred.ct.service.spprof.data.access.dto.SpProfDTO;
import it.webred.ct.service.spprof.data.model.SSpAreaPart;
import it.webred.ct.service.spprof.data.model.SSpCedificato;
import it.webred.ct.service.spprof.data.model.SSpDestUrb;
import it.webred.ct.service.spprof.data.model.SSpEdificio;
import it.webred.ct.service.spprof.data.model.SSpEdificioMinore;
import it.webred.ct.service.spprof.data.model.SSpIntervento;
import it.webred.ct.service.spprof.data.model.SSpTipologiaEdi;
import it.webred.ct.service.spprof.data.model.SSpTipologiaEdiMin;
import it.webred.ct.service.spprof.web.user.SpProfBaseBean;
import it.webred.ct.service.spprof.web.user.bean.util.MapBean;
import it.webred.ct.service.spprof.web.user.bean.util.PageBean;

import org.apache.log4j.Logger;

public class EdificioBean extends SpProfBaseBean {

	public SpProfAreaService spProfAreaService = (SpProfAreaService) getEjb("Servizio_SpProf", "Servizio_SpProf_EJB", "SpProfAreaServiceBean");

	public SpProfEdificiService spProfEdificiService = (SpProfEdificiService) getEjb("Servizio_SpProf", "Servizio_SpProf_EJB", "SpProfEdificiServiceBean");

	public SpProfLocalizzaService spProfLocalizzaService = (SpProfLocalizzaService) getEjb("Servizio_SpProf", "Servizio_SpProf_EJB", "SpProfLocalizzaServiceBean");

	private String idIntervento;
	private String idAreaPart;
	private String note;
	private String tipologia;
	private String destinazioneUso;
	private String destinazioneUsoPrev;
	private String idCivico;
	
	private String index;
	private boolean update;
	private boolean mostraDUPrev;
	private EdificioDTO edificioDTO;

	private List<SelectItem> selectParticella;
	private List<SelectItem> selectTipologia;
	private List<SelectItem> selectDestUso;
	private List<SelectItem> selectCivici;

	public void doNuovo() {

		selectParticella = new ArrayList<SelectItem>();
		selectTipologia = new ArrayList<SelectItem>();
		selectDestUso = new ArrayList<SelectItem>();
		selectCivici = new ArrayList<SelectItem>();
		idAreaPart = "";
		note = "";
		tipologia = "";
		destinazioneUso = "";
		destinazioneUsoPrev = "";
		idCivico = "";

		try {

			PageBean pBean = (PageBean) getBeanReference("pageBean");
			pBean.goNuovoEdificio();

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

			List<SSpTipologiaEdi> listaTip = spProfEdificiService
					.getTipologiaEdifici(dto);
			for (SSpTipologiaEdi tip : listaTip) {

				selectTipologia.add(new SelectItem(tip.getCodTipologiaEdi(),
						tip.getDescr()));

			}
			
			List<SSpDestUrb> listaDest = spProfEdificiService.getDestinazioneUso(dto);
			for (SSpDestUrb dest : listaDest) {

				selectDestUso.add(new SelectItem(dest.getCodDestUrb(),
						dest.getDescr()));

			}

		} catch (Throwable t) {
			super.addErrorMessage("interfaccia.error", t.getMessage());
			logger.error(t);
		}

	}
	
	public void doCaricaCivici() {
		
		selectCivici = new ArrayList<SelectItem>();
		
		if(!idAreaPart.equals("")){
			try {
				
				ParticellaDTO pDto = new ParticellaDTO();
				pDto.setEnteId(getEnte());
				
				SpProfDTO dto = new SpProfDTO();
				dto.setEnteId(getEnte());
				dto.setObj(new Long(idAreaPart));
				SSpAreaPart part = spProfAreaService.getAreaPartById(dto);
				pDto.setFoglio(part.getFoglio().toString());
				pDto.setParticella(part.getParticella());
				List<CivicoDTO> listaCivici = spProfLocalizzaService
						.getListaCiviciByFP(pDto);
				for (CivicoDTO civ : listaCivici) {
					selectCivici.add(new SelectItem(civ.getPkidCivi() + "-"
							+ civ.getPrefisso() + "-" + civ.getNome() + "-"
							+ civ.getCivico(), civ.getPrefisso() + " "
							+ civ.getNome() + " " + civ.getCivico()));
				}
				
			} catch (Throwable t) {
				super.addErrorMessage("interfaccia.error", t.getMessage());
				logger.error(t);
			}
		}
	}
	
	public void doMostraDUPrev() {
		if(destinazioneUso.equals("MIX"))
			mostraDUPrev = true;
		else mostraDUPrev = false;
	}
	
	public void doDettagli() {
		
		selectParticella = new ArrayList<SelectItem>();
		selectTipologia = new ArrayList<SelectItem>();
		selectDestUso = new ArrayList<SelectItem>();
		selectCivici = new ArrayList<SelectItem>();

		try {
			
			PageBean pBean = (PageBean) getBeanReference("pageBean");
			pBean.goNuovoEdificio();

			//carico dati dell'edificio
			DatiBean dBean = (DatiBean) getBeanReference("datiBean");
			edificioDTO = dBean.getListaEdifici().get(new Integer(index));
			idAreaPart = edificioDTO.getEdificato().getFkSpAreaPart().toString();
			doCaricaCivici();
			note = edificioDTO.getEdificato().getNote();
			tipologia = edificioDTO.getEdificio().getFkSpTipologiaEdi();
			destinazioneUso = edificioDTO.getEdificio().getFkSpDestUrb();
			destinazioneUsoPrev = edificioDTO.getEdificio().getFkSpDestUrbPreval();
			idCivico = edificioDTO.getEdificio().getCivicoCodice() + "-"
			+ edificioDTO.getEdificio().getViaPrefisso() + "-" + edificioDTO.getEdificio().getViaNome() + "-"
			+ edificioDTO.getEdificio().getCivicoNumero();
			
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

			List<SSpTipologiaEdi> listaTip = spProfEdificiService
					.getTipologiaEdifici(dto);
			for (SSpTipologiaEdi tip : listaTip) {

				selectTipologia.add(new SelectItem(tip.getCodTipologiaEdi(),
						tip.getDescr()));

			}
			
			List<SSpDestUrb> listaDest = spProfEdificiService.getDestinazioneUso(dto);
			for (SSpDestUrb dest : listaDest) {

				selectDestUso.add(new SelectItem(dest.getCodDestUrb(),
						dest.getDescr()));

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
				SSpEdificio edi = new SSpEdificio();
				if(update){
					cedi = edificioDTO.getEdificato();
					edi = edificioDTO.getEdificio();
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
					edi.setFkSpTipologiaEdi(tipologia);
				if(!idCivico.equals("")){
					String[] datiCiv = idCivico.split("-");
					edi.setCivicoCodice(datiCiv[0]);
					edi.setViaPrefisso(datiCiv[1]);
					edi.setViaNome(datiCiv[2]);
					edi.setCivicoNumero(datiCiv[3]);
				}
				if(!destinazioneUso.equals(""))
					edi.setFkSpDestUrb(destinazioneUso);
				if(!destinazioneUsoPrev.equals(""))
					edi.setFkSpDestUrbPreval(destinazioneUsoPrev);
				edi.setDtIns(new Date());
				if(update){
					edi.setDtMod(new Date());
					edi.setUserMod(getUsername());
				}
				dto.setObj(edi);
				
				if(update)
					spProfEdificiService.updateEdificio(dto);
				else spProfEdificiService.saveEdificio(dto);

				// refresh dati
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
			edificioDTO = dBean.getListaEdifici().get(new Integer(index));
			SpProfDTO dto = new SpProfDTO();
			dto.setEnteId(getEnte());
			dto.setObj(new Long(edificioDTO.getEdificio().getFkSpCedificato()));
			spProfEdificiService.deleteEdificio(dto);
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

	public List<SelectItem> getSelectTipologia() {
		return selectTipologia;
	}

	public void setSelectTipologia(List<SelectItem> selectTipologia) {
		this.selectTipologia = selectTipologia;
	}

	public String getDestinazioneUso() {
		return destinazioneUso;
	}

	public void setDestinazioneUso(String destinazioneUso) {
		this.destinazioneUso = destinazioneUso;
	}

	public String getDestinazioneUsoPrev() {
		return destinazioneUsoPrev;
	}

	public void setDestinazioneUsoPrev(String destinazioneUsoPrev) {
		this.destinazioneUsoPrev = destinazioneUsoPrev;
	}

	public String getIdCivico() {
		return idCivico;
	}

	public void setIdCivico(String idCivico) {
		this.idCivico = idCivico;
	}

	public List<SelectItem> getSelectDestUso() {
		return selectDestUso;
	}

	public void setSelectDestUso(List<SelectItem> selectDestUso) {
		this.selectDestUso = selectDestUso;
	}

	public List<SelectItem> getSelectCivici() {
		return selectCivici;
	}

	public void setSelectCivici(List<SelectItem> selectCivici) {
		this.selectCivici = selectCivici;
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

	public boolean isMostraDUPrev() {
		return mostraDUPrev;
	}

	public void setMostraDUPrev(boolean mostraDUPrev) {
		this.mostraDUPrev = mostraDUPrev;
	}

}
