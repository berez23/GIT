package it.webred.ct.service.tsSoggiorno.web.bean.anagrafica;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import it.webred.ct.service.tsSoggiorno.data.access.dto.DataInDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.DichiarazioneSearchCriteria;
import it.webred.ct.service.tsSoggiorno.data.access.dto.SocietaDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.StrutturaDTO;
import it.webred.ct.service.tsSoggiorno.data.model.IsClasse;
import it.webred.ct.service.tsSoggiorno.data.model.IsClassiStruttura;
import it.webred.ct.service.tsSoggiorno.data.model.IsClassiStrutturaPK;
import it.webred.ct.service.tsSoggiorno.data.model.IsSocieta;
import it.webred.ct.service.tsSoggiorno.data.model.IsSocietaSogg;
import it.webred.ct.service.tsSoggiorno.data.model.IsSoggetto;
import it.webred.ct.service.tsSoggiorno.data.model.IsStruttura;
import it.webred.ct.service.tsSoggiorno.data.model.Sitidstr;
import it.webred.ct.service.tsSoggiorno.web.bean.TsSoggiornoBaseBean;
import it.webred.ct.service.tsSoggiorno.web.bean.util.UserBean;

public class StrutturaBean extends TsSoggiornoBaseBean {

	private DichiarazioneSearchCriteria criteria = new DichiarazioneSearchCriteria();
	private String codFiscale;
	private List<StrutturaDTO> listaStrutture;
	private Long idSelezionato;
	private String idClasse;
	private IsStruttura struttura;
	private List<SelectItem> listaClassi = new ArrayList<SelectItem>();
	private List<SelectItem> listaSocieta = new ArrayList<SelectItem>();
	private List<IsClassiStruttura> listaClassiStruttura;
	private List<SelectItem> prefissiVia = Arrays.asList(new SelectItem("VIA",
			"Via"), new SelectItem("VIALE", "Viale"), new SelectItem("VICOLO",
			"Vicolo"), new SelectItem("PIAZZA", "Piazza"), new SelectItem(
			"CORSO", "Corso"));
	private String prefissoVia;
	private String via;

	public void doLoadListaStrutture() {
		try {

			if (codFiscale == null) {
				UserBean user = (UserBean) getBeanReference("userBean");
				codFiscale = user.getUsername();
			}
			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			dataIn.setCodFiscale(codFiscale);
			listaStrutture = super.getAnagraficaService().getStrutturaByCodFis(dataIn);
			loadSocieta();
			loadClassi();
			criteria = new DichiarazioneSearchCriteria();

		} catch (Throwable t) {
			super.addErrorMessage("caricamento.error", t.getMessage());
			getLogger().error("Eccezione: " + t.getMessage(), t);
		}
	}

	public void doLoadListaStruttureFiltrate() {
		try {

			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			criteria.setCodFiscale(codFiscale);
			dataIn.setObj(criteria);
			listaStrutture = super.getAnagraficaService().getStrutturaByCriteria(dataIn);

		} catch (Throwable t) {
			super.addErrorMessage("caricamento.error", t.getMessage());
			getLogger().error("Eccezione: " + t.getMessage(), t);
		}
	}

	public void doNewStruttura() {
		if (codFiscale == null) {
			UserBean user = (UserBean) getBeanReference("userBean");
			codFiscale = user.getUsername();
		}
		loadSocieta();
		loadClassi();
		via = "";
		prefissoVia = "";
		struttura = new IsStruttura();
		listaClassiStruttura = new ArrayList<IsClassiStruttura>();
		doNewClasseStruttura();

	}

	public void doNewClasseStruttura() {
		IsClassiStruttura cs = new IsClassiStruttura();
		IsClassiStrutturaPK csPK = new IsClassiStrutturaPK();
		cs.setId(csPK);
		listaClassiStruttura.add(cs);
	}

	public List<Sitidstr> getSuggestionIndirizzi(Object obj) {

		List<Sitidstr> lista = new ArrayList<Sitidstr>();
		try {
			String via = (String) obj;

			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			dataIn.setBool(true);
			dataIn.setMaxNumber(10);
			dataIn.setId2(via);
			lista = super.getAnagraficaService().getSitidstrByLikeIndirizzo(dataIn);

		} catch (Throwable t) {
			super.addErrorMessage("caricamento.error", t.getMessage());
			getLogger().error("Eccezione: " + t.getMessage(), t);
		}

		return lista;
	}

	public void doLoadStruttura() {
		try {

			if (codFiscale == null) {
				UserBean user = (UserBean) getBeanReference("userBean");
				codFiscale = user.getUsername();
			}
			if (listaSocieta.size() == 0) {
				loadSocieta();
			}
			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			dataIn.setId(idSelezionato);
			loadSocieta();
			loadClassi();
			struttura = super.getAnagraficaService().getStrutturaById(dataIn)
					.getStruttura();
			listaClassiStruttura = super.getAnagraficaService()
					.getClassiByStruttura(dataIn);
			
			//Scrivere la via nel bean
			if(struttura!=null && struttura.getIndirizzo()!=null){
				String[] s = struttura.getIndirizzo().split(" ");
				if(s.length>1){
					prefissoVia = s[0];
					via="";
					for(int i=1; i<s.length; i++)
						via += s[i]+" ";
				}
				via = via.trim();
			}

		} catch (Throwable t) {
			super.addErrorMessage("caricamento.error", t.getMessage());
			getLogger().error("Eccezione: " + t.getMessage(), t);
		}
	}

	public void doSaveStruttura() {
		try {

			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			// salvo struttura
			dataIn.setId2(via);
			Sitidstr str = super.getAnagraficaService().getSitidstrByIndirizzo(dataIn);
			if(str != null){
				struttura.setCodIndirizzo(str.getPkidStra().toString());
			}
			struttura.setIndirizzo(prefissoVia + " " + via);
			if (struttura.getId() == null) {
				struttura.setUsrIns(codFiscale);
				struttura.setDtIns(new Date());
				dataIn.setObj(struttura);
				idSelezionato = super.getAnagraficaService().saveStruttura(dataIn).getId();
			} else {
				struttura.setUsrMod(codFiscale);
				struttura.setDtMod(new Date());
				dataIn.setObj(struttura);
				super.getAnagraficaService().updateStruttura(dataIn);
			}
			// salvo classi struttura
			dataIn.setId(idSelezionato);
			super.getAnagraficaService().deleteClasseStrutturaByStr(dataIn);
			for (IsClassiStruttura cs : listaClassiStruttura) {
				cs.getId().setFkIsStruttura(idSelezionato);
				// descrizione
				if (!"999".equals(cs.getId().getFkIsClasse())) {
					for (SelectItem cls : listaClassi) {
						if (cls.getValue().equals(cs.getId().getFkIsClasse()))
							cs.setDescrizione(cls.getLabel());
					}
				}
				if (cs.getUsrIns() == null) {
					// nuovo
					cs.setUsrIns(codFiscale);
					cs.setDtIns(new Date());
					dataIn.setObj(cs);
					super.getAnagraficaService().saveClasseStruttura(dataIn);
				} else {
					dataIn.setObj(cs);
					super.getAnagraficaService().updateClasseStruttura(dataIn);
				}
			}
			super.addInfoMessage("salvataggio.ok");
			doLoadListaStrutture();

		} catch (Throwable t) {
			super.addErrorMessage("salvataggio.error", t.getMessage());
			getLogger().error("Eccezione: " + t.getMessage(), t);
		}
	}

	public void doDeleteStruttura() {
		try {

			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			dataIn.setId(idSelezionato);
			super.getAnagraficaService().deleteStrutturaById(dataIn);
			super.addInfoMessage("salvataggio.ok");
			doLoadListaStrutture();

		} catch (Throwable t) {
			super.addErrorMessage("salvataggio.error", t.getMessage());
			getLogger().error("Eccezione: " + t.getMessage(), t);
		}
	}

	public void doDeleteClasseStruttura() {
		try {

			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			dataIn.setId(idSelezionato);
			dataIn.setId2(idClasse);
			super.getAnagraficaService().deleteClasseStrutturaByStrCls(dataIn);
			super.addInfoMessage("salvataggio.ok");
			listaClassiStruttura = super.getAnagraficaService()
					.getClassiByStruttura(dataIn);
			doLoadListaStrutture();

		} catch (Throwable t) {
			super.addErrorMessage("salvataggio.error", t.getMessage());
			getLogger().error("Eccezione: " + t.getMessage(), t);
		}
	}

	private void loadSocieta() {
		listaSocieta = new ArrayList<SelectItem>();
		DataInDTO dataIn = new DataInDTO();
		fillEnte(dataIn);
		dataIn.setCodFiscale(codFiscale);
		List<SocietaDTO> lista = super.getAnagraficaService().getSocietaByCodFis(dataIn);
		for (SocietaDTO s : lista) {
			listaSocieta.add(new SelectItem(new BigDecimal(s.getSocieta()
					.getId()), s.getSocieta().getRagSoc()));
		}
	}

	private void loadClassi() {
		if (listaClassi == null || listaClassi.size() == 0) {
			DataInDTO dataIn = new DataInDTO();
			fillEnte(dataIn);
			List<IsClasse> lista = super.getAnagraficaService().getClassi(dataIn);
			for (IsClasse c : lista) {
				listaClassi.add(new SelectItem(c.getCodice(), c
						.getDescrizione()));
			}
		}
	}

	public List<StrutturaDTO> getListaStrutture() {

		if (listaStrutture == null) {
			doLoadListaStrutture();
		}

		return listaStrutture;
	}

	public void setListaStrutture(List<StrutturaDTO> listaStrutture) {
		this.listaStrutture = listaStrutture;
	}

	public Long getIdSelezionato() {
		return idSelezionato;
	}

	public void setIdSelezionato(Long idSelezionato) {
		this.idSelezionato = idSelezionato;
	}

	public IsStruttura getStruttura() {
		return struttura;
	}

	public void setStruttura(IsStruttura struttura) {
		this.struttura = struttura;
	}

	public List<IsClassiStruttura> getListaClassiStruttura() {
		return listaClassiStruttura;
	}

	public void setListaClassiStruttura(
			List<IsClassiStruttura> listaClassiStruttura) {
		this.listaClassiStruttura = listaClassiStruttura;
	}

	public String getCodFiscale() {
		return codFiscale;
	}

	public void setCodFiscale(String codFiscale) {
		this.codFiscale = codFiscale;
	}

	public List<SelectItem> getListaClassi() {
		return listaClassi;
	}

	public void setListaClassi(List<SelectItem> listaClassi) {
		this.listaClassi = listaClassi;
	}

	public List<SelectItem> getListaSocieta() {
		return listaSocieta;
	}

	public void setListaSocieta(List<SelectItem> listaSocieta) {
		this.listaSocieta = listaSocieta;
	}

	public String getIdClasse() {
		return idClasse;
	}

	public void setIdClasse(String idClasse) {
		this.idClasse = idClasse;
	}

	public DichiarazioneSearchCriteria getCriteria() {
		return criteria;
	}

	public void setCriteria(DichiarazioneSearchCriteria criteria) {
		this.criteria = criteria;
	}

	public List<SelectItem> getPrefissiVia() {
		return prefissiVia;
	}

	public void setPrefissiVia(List<SelectItem> prefissiVia) {
		this.prefissiVia = prefissiVia;
	}

	public String getPrefissoVia() {
		return prefissoVia;
	}

	public void setPrefissoVia(String prefissoVia) {
		this.prefissoVia = prefissoVia;
	}

	public String getVia() {
		return via;
	}

	public void setVia(String via) {
		this.via = via;
	}

	
	
}
