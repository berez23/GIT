package it.webred.ct.service.segnalazioniqualificate.web.bean;

import javax.annotation.PostConstruct;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.segnalazionequalificata.*;
import it.webred.ct.data.access.basic.segnalazionequalificata.dto.*;
import it.webred.ct.service.segnalazioniqualificate.web.bean.util.UserBean;

public class DetailBean extends SegnalazioniQualificateBaseBean {

	private static final long serialVersionUID = 1L;

	private long idPratica;
	private PraticaSegnalazioneDTO dto;
	private boolean visIndietro;
	private boolean utenteGestore;
	
	public boolean isUtenteGestore() {
		boolean creatore = getCurrentUsernameUtente().equals(dto.getPratica().getOperatoreId());
		UserBean ub = (UserBean)this.getBeanReference("userBean");
		boolean supervisore = ub.isSupervisore();
		utenteGestore = supervisore || creatore;
		return utenteGestore;
	}

	public void setUtenteGestore(boolean utenteGestore) {
		this.utenteGestore = utenteGestore;
	}

	@PostConstruct
	public void initService() {
		visIndietro = true;
		dto = new PraticaSegnalazioneDTO();
		dto.setEnteId(this.getCurrentEnte());
	}

	public String goDetail() {
			doCaricaDto();
		return "segnalazioni.detail";

	}

	public void doCaricaDto() {
		try {

			SegnalazioniDataIn dataIn = this.getInitRicercaParams();
			getLogger().info("CARICAMENTO DETTAGLIO PRATICA [ID]: ["+ idPratica+","+dto+"]");
			dataIn.getRicercaPratica().setIdPra(idPratica);
			this.fillEnte(dataIn);
			this.fillEnte(dataIn.getRicercaPratica());
			dto = segnalazioneService.getPraticaById(dataIn);
			dto.setDescAccComuNasc(this.getDescEnte(dto.getPratica().getAccComuNasc()));

		} catch (Throwable t) {
			super.addErrorMessage("detail.error", t.getMessage());
			t.printStackTrace();
		}
	}
	
	public PraticaSegnalazioneDTO getDto() {
		return dto;
	}

	public void setDto(PraticaSegnalazioneDTO dto) {
		this.dto = dto;
	}

	public long getIdPratica() {
		return idPratica;
	}

	public void setIdPratica(long idPratica) {
		this.idPratica = idPratica;
	}

	public boolean isVisIndietro() {
		return visIndietro;
	}

	public void setVisIndietro(boolean visIndietro) {
		this.visIndietro = visIndietro;
	}

}
