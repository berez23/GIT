package it.webred.ct.service.spprof.web.user.bean.interventi;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import it.webred.ct.service.spprof.data.model.SSpEdificio;
import it.webred.ct.service.spprof.data.model.SSpEdificioMinore;
import it.webred.ct.service.spprof.data.model.SSpIntervento;
import it.webred.ct.service.spprof.data.model.SSpInterventoLayer;
import it.webred.ct.service.spprof.data.model.SSpTipologiaEdiMin;
import it.webred.ct.service.spprof.data.model.SSpUiu;
import it.webred.ct.service.spprof.data.model.SSpUnitaVol;
import it.webred.ct.service.spprof.web.user.SpProfBaseBean;
import it.webred.ct.service.spprof.web.user.bean.util.MailBean;
import it.webred.ct.service.spprof.web.user.bean.util.MapBean;
import it.webred.ct.service.spprof.web.user.bean.util.PageBean;
import it.webred.ct.service.spprof.web.user.bean.util.UserBean;

import org.apache.log4j.Logger;

public class StatoBean extends SpProfBaseBean {

	protected CatastoService catastoService = (CatastoService) getEjb("CT_Service", "CT_Service_Data_Access", "CatastoServiceBean");

	public SpProfInterventoService spProfInterventoService = (SpProfInterventoService) getEjb("Servizio_SpProf", "Servizio_SpProf_EJB", "SpProfInterventoServiceBean");

	public SpProfEdificiService spProfEdificiService = (SpProfEdificiService) getEjb("Servizio_SpProf", "Servizio_SpProf_EJB", "SpProfEdificiServiceBean");

	private String idIntervento;

	private String statoScelto;
	private List<SelectItem> listaStati;
	private boolean mostraSalva;

	public static final List<String> statiUtente = Arrays.asList("INVIATO@Invia",
			"CANCELLATO@Cancella");
	public static final List<String> statiAdmin = Arrays.asList("SCARTATO@Scarta",
			"ACCETTATO@Accetta", "RESPINTO@Respingi");
	

	public void doCaricaStati() {
		
		PageBean pBean = (PageBean) getBeanReference("pageBean");
		pBean.goStato();
		
		listaStati = new ArrayList<SelectItem>();
		UserBean uBean = (UserBean) getBeanReference("userBean");
		
		if(statoScelto!= null && (statoScelto.equals("IN LAVORAZIONE") || statoScelto.equals("RESPINTO"))){
			for (String s : statiUtente) {
				String[] split = s.split("@");
				listaStati.add(new SelectItem(split[0], split[1]));
			}
		}
		if(statoScelto!= null && statoScelto.equals("INVIATO") && uBean.getAdministrator()){
			for (String s : statiAdmin) {
				String[] split = s.split("@");
				listaStati.add(new SelectItem(split[0], split[1]));
			}
		}
		if(listaStati.size() > 0)
			mostraSalva = true;
		else mostraSalva = false;
	
	}
	
	public void doCambiaStato() {

		boolean ok = true;
		
		try {

			SpProfDTO dto = new SpProfDTO();
			dto.setEnteId(getEnte());
			dto.setUserId(getUsername());
			dto.setObj(new Long(idIntervento));
			SSpIntervento intervento = spProfInterventoService
					.getInterventoById(dto);

			if (statoScelto.equals("INVIATO")) {

				// controllo che abbia almeno anagrafica, 1 edificio e progetto valorizzato
				if (intervento.getnAccessiCarraiPrev() == null
						|| intervento.getnBoxAuto() == null
						|| intervento.getnPassiCarraiPrev() == null
						|| intervento.getnPostiAuto() == null) {
					ok = false;
					super.addErrorMessage("stato.error.no.dati", "");
				}
				List<SSpEdificio> lista = spProfEdificiService.getEdificiByintervento(dto);
				List<SSpEdificioMinore> listaMinore = spProfEdificiService.getEdificiMinoriByintervento(dto);
				if(lista.size() == 0 && listaMinore.size() == 0){
					ok = false;
					super.addErrorMessage("stato.error.no.edificio", "");
				}
				List<SSpInterventoLayer> listaLayer = spProfInterventoService.getInterventoLayerByIntervento(dto);
				if(listaLayer.size() == 0){
					ok = false;
					super.addErrorMessage("stato.error.no.progetto", "");
				}
			}

			if(ok){
				intervento.setStato(statoScelto);
				intervento.setUserMod(getUsername());
				intervento.setDtMod(new Date());
				dto.setObj(intervento);
				spProfInterventoService.update(dto);
				
				//invio email dettaglio
				UserBean uBean = (UserBean) getBeanReference("userBean");
				if(!uBean.getAdministrator()){
				MailBean mBean = (MailBean) getBeanReference("mailBean");
				mBean.setIdIntervento(idIntervento);
				mBean.sendMailIntervento();
				}
				
				super.addInfoMessage("save");
			}

		} catch (Throwable t) {
			super.addErrorMessage("save.error", t.getMessage());
			logger.error(t);
		}

	}

	public String getIdIntervento() {
		return idIntervento;
	}

	public void setIdIntervento(String idIntervento) {
		this.idIntervento = idIntervento;
	}

	public String getStatoScelto() {
		return statoScelto;
	}

	public void setStatoScelto(String statoScelto) {
		this.statoScelto = statoScelto;
	}

	public List<SelectItem> getListaStati() {
		return listaStati;
	}

	public void setListaStati(List<SelectItem> listaStati) {
		this.listaStati = listaStati;
	}

	public boolean isMostraSalva() {
		return mostraSalva;
	}

	public void setMostraSalva(boolean mostraSalva) {
		this.mostraSalva = mostraSalva;
	}

}
