package it.webred.ct.service.spprof.web.user.bean.util;

import it.webred.ct.service.geospatial.data.access.dto.ParticellaDTO;
import it.webred.ct.service.spprof.data.access.SpProfAreaService;
import it.webred.ct.service.spprof.data.access.dto.SpProfDTO;
import it.webred.ct.service.spprof.data.model.SSpAreaPart;
import it.webred.ct.service.spprof.web.user.SpProfBaseBean;
import it.webred.ct.service.spprof.web.user.bean.nuovo.DataProviderImpl;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;

import org.apache.log4j.Logger;

public class MapBean extends SpProfBaseBean {

	public SpProfAreaService spProfAreaService = (SpProfAreaService) getEjb("Servizio_SpProf", "Servizio_SpProf_EJB", "SpProfAreaServiceBean");

	private String mapUrl;

	private String fogli;

	private String particelle;

	private String idIntervento;
	
	private String mapHeight;

	public void caricaMapUrl() {
		UserBean uBean = (UserBean) getBeanReference("userBean");
		String utente = uBean.getUsername();
		if(uBean.getAdministrator())
			utente = "administrator";
		mapUrl = "http://" + getRequest().getServerName() + ":"
				+ getRequest().getServerPort()
				+ "/viewerjs_SpProf/extra/viewerjs/map?ente=" + uBean.getEnte()
				+ "&ds=" + uBean.getDatasource()
				+ "&qryoper=equal&cod_nazionale=A'OR'A'<'B&user=" + utente;
	}

	public void caricaMapUrlSelezione() {
		UserBean uBean = (UserBean) getBeanReference("userBean");
		String utente = uBean.getUsername();
		if(uBean.getAdministrator())
			utente = "administrator";
		String[] f = fogli.split("\\|");
		mapUrl = "http://" + getRequest().getServerName() + ":"
				+ getRequest().getServerPort()
				+ "/viewerjs_SpProf/extra/viewerjs/map?ente=" + getEnte()
				+ "&ds=" + uBean.getDatasource()
				+ "&qryoper=equal&cod_nazionale=" + getEnte() + "&foglio="
				+ f[0] + "&fogli=" + fogli + "&particelle=" + particelle + "&user=" + utente;
	}

	public void caricaMapUrlIntervento() {

		String foglio = null;
		
		try {
			
			SpProfDTO dto = new SpProfDTO();
			dto.setEnteId(getEnte());
			dto.setObj(new Long(idIntervento));
			List<SSpAreaPart> listaPart = spProfAreaService.getListaAreaPart(dto);
			if(listaPart.size()>0){
				foglio = listaPart.get(0).getFoglio().toString();
			}
			
		} catch (Throwable t) {
			super.addErrorMessage("dettaglio.error", t.getMessage());
			logger.error(t);
		}

		UserBean uBean = (UserBean) getBeanReference("userBean");
		String utente = uBean.getUsername();
		mapUrl = "http://" + getRequest().getServerName() + ":"
				+ getRequest().getServerPort()
				+ "/viewerjs_SpProf/extra/viewerjs/map?ente=" + getEnte()
				+ "&ds=" + uBean.getDatasource()
				+ "&qryoper=equal&cod_nazionale=" + getEnte() + (foglio!=null?"&foglio=" + foglio:"")
				+ "&intervento=" + idIntervento + "&user=" + utente;
	}

	public void caricaMapUrlDaSelezione() {
		fogli = "";
		particelle = "";
		DataProviderImpl impl = (DataProviderImpl) getBeanReference("nuovoDataProviderImpl");
		List<ParticellaDTO> lista = impl.getListaSelezione();
		if (lista.size() > 0) {
			for (ParticellaDTO dto : lista) {
				if (!fogli.contains(dto.getFoglio()))
					fogli += "|" + dto.getFoglio();
				particelle += "|" + dto.getParticella();
			}
			fogli = fogli.substring(1);
			particelle = particelle.substring(1);
			caricaMapUrlSelezione();
		}
	}

	public void caricaMapUrlDaRisultati() {
		DataProviderImpl impl = (DataProviderImpl) getBeanReference("nuovoDataProviderImpl");
		fogli = impl.getListaFogliRicerca();
		particelle = impl.getListaParticelleRicerca();
		caricaMapUrlSelezione();
	}

	public String getMapUrl() {
		if (mapUrl == null)
			caricaMapUrl();
		return mapUrl;
	}

	public void setMapUrl(String mapUrl) {
		this.mapUrl = mapUrl;
	}
	
	public void setMapHeight(String mapHeight) {
		this.mapHeight = mapHeight;
	}

	public String getMapHeight() {
		return mapHeight;
	}

	public String getIdIntervento() {
		return idIntervento;
	}

	public void setIdIntervento(String idIntervento) {
		this.idIntervento = idIntervento;
	}

}
