package it.webred.ct.service.spprof.web.user.bean.util;

import java.io.IOException;

import it.webred.ct.service.spprof.web.user.SpProfBaseBean;
import it.webred.ct.service.spprof.web.user.bean.interventi.DatiBean;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;

public class PageBean extends SpProfBaseBean{

	private static final long serialVersionUID = 1L;

	private String pageUp = "/jsp/protected/interventi/ricerca.xhtml";
	private String pageLeft = "/jsp/protected/interventi/interventi.xhtml";
	private String pageModal = "/jsp/protected/empty.xhtml";
	private String pageClassamento =  "/jsp/protected/empty.xhtml";
	private String pageAdmin =  "/jsp/protected/empty.xhtml";
	private boolean pageFasFab;
		
	public String goListaRichiesteFasFab(){
		
		pageFasFab = true;
		return "spprof.fasfab.richiesta.lista";
		
	}
	
	public String goNuovaRichiestaFasFab(){
		
		pageFasFab = true;
		return "spprof.fasfab.richiesta.nuova";
		
	}
	
	
	public String goInterventi(){
		
		UserBean uBean = (UserBean) getBeanReference("userBean");
		if(uBean.getAdministrator())
			pageUp = "/jsp/protected/interventi/ricerca.xhtml";
		else pageUp = "/jsp/protected/empty.xhtml";
		pageLeft = "/jsp/protected/interventi/interventi.xhtml";
		pageModal = "/jsp/protected/empty.xhtml";
		
		if(pageFasFab){
			pageFasFab = false;
			return "spprof.user.home";
		}
		return "";
	}
	
	public String goNuovo(){
		
		pageUp = "/jsp/protected/nuovo/ricerca.xhtml";
		pageLeft = "/jsp/protected/nuovo/risultatiRicerca.xhtml";
		pageModal = "/jsp/protected/empty.xhtml";
		
		if(pageFasFab){
			pageFasFab = false;
			return "spprof.user.home";
		}
		return "";
	}
	
	public void goSceltaLayer(){
		
		pageModal = "/jsp/protected/modalPanel/sceltaLayer.xhtml";
		
	}
	
	public void goDati(){
		
		pageUp = "/jsp/protected/interventi/ricerca.xhtml";
		pageLeft = "/jsp/protected/dettaglio/dati.xhtml";
		pageModal = "/jsp/protected/empty.xhtml";
		
	}
	
	public void goUpload(){
		
		pageModal = "/jsp/protected/modalPanel/upload.xhtml";
		
	}
	
	public void goStato(){
		
		pageModal = "/jsp/protected/modalPanel/stato.xhtml";
		
	}
	
	public void goNuovoEdificio(){
		
		pageModal = "/jsp/protected/modalPanel/newEdificio.xhtml";
		
	}
	
	public void goNuovoEdificioMin(){
		
		pageModal = "/jsp/protected/modalPanel/newEdificioMin.xhtml";
		
	}
	
	public void goUiu(){
		
		pageModal = "/jsp/protected/modalPanel/uiu.xhtml";
		
	}
	
	public void goUnitaVol(){
		
		pageModal = "/jsp/protected/modalPanel/unitaVol.xhtml";
		
	}
	
	public String getPageUp() {
		return pageUp;
	}

	public void setPageUp(String pageUp) {
		this.pageUp = pageUp;
	}

	public String getPageLeft() {
		return pageLeft;
	}

	public void setPageLeft(String pageLeft) {
		this.pageLeft = pageLeft;
	}

	public String getPageModal() {
		return pageModal;
	}

	public void setPageModal(String pageModal) {
		this.pageModal = pageModal;
	}


	public String getPageClassamento() {
		String serverName = this.getRequest().getServerName();
		int serverPort = this.getRequest().getServerPort();
		String pageServerRoot = "http://"+serverName+":"+Integer.toString(serverPort)+"/";
		this.pageClassamento = pageServerRoot + "Preclassamento";
		logger.info(pageClassamento);
		
		return pageClassamento;
	}

	public void setPageClassamento(String pageClassamento) {
		this.pageClassamento = pageClassamento;
	}
	
	public String getPageAdmin() {
		String serverName = this.getRequest().getServerName();
		int serverPort = this.getRequest().getServerPort();
		String pageServerRoot = "http://"+serverName+":"+Integer.toString(serverPort)+"/";
		this.pageAdmin = pageServerRoot + "SpProfessionistaAdmin";
		logger.info(pageAdmin);
		
		return pageAdmin;
	}

	public void setPageAdmin(String pageAdmin) {
		this.pageAdmin = pageAdmin;
	}
	
	public boolean isControlloDettaglioIntervento() {
		String idInterventoEncode = getRequest().getParameter("idivt");
		if(idInterventoEncode != null){
			BASE64Decoder dec = new BASE64Decoder();
			byte[] val;
			try {
				val = dec.decodeBuffer(idInterventoEncode);
				String idIntervento = new String(val);
				DatiBean dBean = (DatiBean) getBeanReference("datiBean");
				dBean.setIdIntervento(idIntervento);
				dBean.setCaricaSolo(false);
				dBean.setMostraDownload(false);
				dBean.doDettaglio();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				getResponse().sendRedirect(getRequest().getContextPath() + "/jsp/protected/home.faces");
			} catch (IOException e) {
				logger.error(e);
			}
		}
		return false;
	}

	private String page;

	public String getPage() {
		//System.out.println("UI GET ["+page+"]");
		return page;
	}

	public void setPage(String page) {
		//System.out.println("UI Set ["+page+"]");
		this.page = page;
	}
	
}
