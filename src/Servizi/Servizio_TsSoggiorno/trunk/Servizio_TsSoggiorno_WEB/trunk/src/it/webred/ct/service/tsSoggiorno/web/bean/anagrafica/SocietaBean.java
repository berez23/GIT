package it.webred.ct.service.tsSoggiorno.web.bean.anagrafica;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import it.webred.ct.service.tsSoggiorno.data.access.dto.DataInDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.SocietaDTO;
import it.webred.ct.service.tsSoggiorno.data.model.IsSocieta;
import it.webred.ct.service.tsSoggiorno.data.model.IsSocietaSogg;
import it.webred.ct.service.tsSoggiorno.web.bean.TsSoggiornoBaseBean;
import it.webred.ct.service.tsSoggiorno.web.bean.util.UserBean;

public class SocietaBean extends TsSoggiornoBaseBean {

	private List<SocietaDTO> listaSocieta;
	private Long idSelezionato;
	private IsSocieta societa;
	private IsSocietaSogg societaSogg;

	public void doLoadListaSocieta() {
		try {

			UserBean user = (UserBean) getBeanReference("userBean");
			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			dataIn.setCodFiscale(user.getUsername());
			listaSocieta = super.getAnagraficaService().getSocietaByCodFis(dataIn);

		} catch (Throwable t) {
			super.addErrorMessage("caricamento.error", t.getMessage());
			getLogger().error("Eccezione: " + t.getMessage(), t);
		}
	}

	public void doNewSocieta() {
		societa = new IsSocieta();
		societaSogg = new IsSocietaSogg();
	}

	public void doLoadSocieta() {
		try {

			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			dataIn.setId(idSelezionato);
			societa = super.getAnagraficaService().getSocietaById(dataIn);
			societaSogg = super.getAnagraficaService().getSocietaSoggById(dataIn);

		} catch (Throwable t) {
			super.addErrorMessage("caricamento.error", t.getMessage());
			getLogger().error("Eccezione: " + t.getMessage(), t);
		}
	}

	public void doSaveSocieta() {
		try {

			boolean validita = true;
			//controllo codfisc e piva
			
			if((societa.getCf()==null || "".equals(societa.getCf().trim()))  && (societa.getPi()==null || "".equals(societa.getPi().trim()))){
				super.addWarningMessage("cf_pi.obbligatori");
				validita=false;
			}
			
			if(societa.getCf() != null && !"".equals(societa.getCf()) && societa.getCf().length() != 16){
				super.addWarningMessage("lunghezza.cf");
				validita = false;
			}
			if(societa.getPi() != null && !"".equals(societa.getPi()) && societa.getPi().length() != 11){
				super.addWarningMessage("lunghezza.pi");
				validita = false;
			}
			
			if(validita){
				DataInDTO dataIn = new DataInDTO();
				fillEnte(dataIn);
				AnagraficaBean ana = (AnagraficaBean) getBeanReference("anagraficaBean");
				if (societa.getId() == null) {
					societa.setUsrIns(ana.getSoggetto().getCodfisc());
					societa.setDtIns(new Date());
					dataIn.setObj(societa);
					societa = super.getAnagraficaService().saveSocieta(dataIn);
					societaSogg.setFkIsSoggetto(new BigDecimal(ana.getSoggetto()
							.getId()));
					societaSogg.setFkIsSocieta(new BigDecimal(societa.getId()));
					societaSogg.setUsrIns(ana.getSoggetto().getCodfisc());
					societaSogg.setDtIns(new Date());
					dataIn.setObj(societaSogg);
					super.getAnagraficaService().saveSocietaSogg(dataIn);
				} else {
					societa.setUsrMod(ana.getSoggetto().getCodfisc());
					societa.setDtMod(new Date());
					dataIn.setObj(societa);
					super.getAnagraficaService().updateSocieta(dataIn);
					societaSogg.setUsrMod(ana.getSoggetto().getCodfisc());
					societaSogg.setDtMod(new Date());
					dataIn.setObj(societaSogg);
					super.getAnagraficaService().updateSocietaSogg(dataIn);
				}
				if(this.showMessage)
					super.addInfoMessage("salvataggio.societa.ok");
				doLoadListaSocieta();
			}

		} catch (Throwable t) {
			super.addErrorMessage("salvataggio.societa.error", t.getMessage());
			getLogger().error("Eccezione: " + t.getMessage(), t);
		}
	}

	public void doDeleteSocieta() {
		try {

			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			dataIn.setId(idSelezionato);
			super.getAnagraficaService().deleteSocietaById(dataIn);
			super.addInfoMessage("salvataggio.societa.ok");
			doLoadListaSocieta();

		} catch (Throwable t) {
			super.addErrorMessage("salvataggio.societa.error", t.getMessage());
			getLogger().error("Eccezione: " + t.getMessage(), t);
		}
	}

	public IsSocieta getSocieta() {
		return societa;
	}

	public void setSocieta(IsSocieta societa) {
		this.societa = societa;
	}

	public Long getIdSelezionato() {
		return idSelezionato;
	}

	public void setIdSelezionato(Long idSelezionato) {
		this.idSelezionato = idSelezionato;
	}

	public IsSocietaSogg getSocietaSogg() {
		return societaSogg;
	}

	public void setSocietaSogg(IsSocietaSogg societaSogg) {
		this.societaSogg = societaSogg;
	}

	public List<SocietaDTO> getListaSocieta() {
		
		if(listaSocieta == null){
			doLoadListaSocieta();
		}
		
		return listaSocieta;
	}

	public void setListaSocieta(List<SocietaDTO> listaSocieta) {
		this.listaSocieta = listaSocieta;
	}

}
