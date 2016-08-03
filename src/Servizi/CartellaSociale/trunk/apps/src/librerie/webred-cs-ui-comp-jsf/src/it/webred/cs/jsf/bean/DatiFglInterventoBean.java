package it.webred.cs.jsf.bean;

import it.webred.cs.csa.ejb.client.AccessTableConfigurazioneSessionBeanRemote;
import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.data.model.CsDDiario;
import it.webred.cs.data.model.CsDRelazione;
import it.webred.cs.data.model.CsFlgIntervento;
import it.webred.cs.data.model.CsTbMotivoChiusuraInt;
import it.webred.cs.jsf.manbean.ComponenteAltroMan;
import it.webred.cs.jsf.manbean.superc.CsUiCompBaseBean;
import it.webred.ejb.utility.ClientUtility;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.NoneScoped;
import javax.naming.NamingException;

@ManagedBean
@NoneScoped
public class DatiFglInterventoBean extends CsUiCompBaseBean implements Serializable {	

	private static final long serialVersionUID = 1L;
	
	private long tipoDiario;
	private long casoId;
	
	private Date dataDomanda;

	private Boolean flagRespinto;
	private String motivoRespinto;
	private String motivoChiusura;
	private String flagAttivazione;
	private String tipoAttivazione;
	private String descrSospensione;
	private Date dtTipoAttDa;
	private Date dtTipoAttA;
	private Long idRelazione;
	
	private ComponenteAltroMan componente;
	
	public Date getDataDomanda() {
		return dataDomanda;
	}

	public void setDataDomanda(Date dataDomanda) {
		this.dataDomanda = dataDomanda;
	}

	public Boolean getFlagRespinto() {
		return flagRespinto;
	}

	public void setFlagRespinto(Boolean flagRespinto) {
		this.flagRespinto = flagRespinto;
	}

	public String getMotivoRespinto() {
		return motivoRespinto;
	}

	public void setMotivoRespinto(String motivoRespinto) {
		this.motivoRespinto = motivoRespinto;
	}

	public String getFlagAttivazione() {
		return flagAttivazione;
	}

	public void setFlagAttivazione(String flagAttivazione) {
		this.flagAttivazione = flagAttivazione;
	}

	public String getTipoAttivazione() {
		return tipoAttivazione;
	}

	public void setTipoAttivazione(String tipoAttivazione) {
		this.tipoAttivazione = tipoAttivazione;
	}

	public String getDescrSospensione() {
		return descrSospensione;
	}

	public void setDescrSospensione(String descrSospensione) {
		this.descrSospensione = descrSospensione;
	}

	public Date getDtTipoAttDa() {
		return dtTipoAttDa;
	}

	public void setDtTipoAttDa(Date dtTipoAttDa) {
		this.dtTipoAttDa = dtTipoAttDa;
	}

	public Date getDtTipoAttA() {
		return dtTipoAttA;
	}

	public void setDtTipoAttA(Date dtTipoAttA) {
		this.dtTipoAttA = dtTipoAttA;
	}

	
	public String getMotivoChiusura() {
		return motivoChiusura;
	}

	public void setMotivoChiusura(String motivoChiusura) {
		this.motivoChiusura = motivoChiusura;
	}

	public long getCasoId() {
		return casoId;
	}

	public void setCasoId(long casoId) {
		this.casoId = casoId;
	}

	public long getTipoDiario() {
		return tipoDiario;
	}

	public void setTipoDiario(long tipoDiario) {
		this.tipoDiario = tipoDiario;
	}

	public ComponenteAltroMan getComponente() {
		return componente;
	}

	public void setComponente(ComponenteAltroMan componente) {
		this.componente = componente;
	}
	
	public void valorizzaBean(CsFlgIntervento cs){
		
		this.setDataDomanda(cs.getDataDomanda());
		
		this.setDescrSospensione(cs.getDescrSospensione());
		this.setDtTipoAttDa(cs.getAttSospCDal());
		this.setDtTipoAttA(cs.getAttSospCAl());
		
		this.setFlagAttivazione(cs.getFlagAttSospC());
		this.setFlagRespinto((cs.getFlagRespinto()!=null && cs.getFlagRespinto().intValue()==1) ? Boolean.TRUE : Boolean.FALSE); 
		
		this.setMotivoChiusura(cs.getCsTbMotivoChiusuraInt()!=null ? String.valueOf(cs.getCsTbMotivoChiusuraInt().getId()): null);
		this.setMotivoRespinto(cs.getMotivoRespinto());
		this.setTipoAttivazione(cs.getTipoAttivazione());
		
		//Valorizzo dati componente familiare
		this.getComponente().setCompIndirizzo(cs.getCompIndirizzo());
		this.getComponente().setCompCitta(cs.getCompCitta());
		this.getComponente().setCompDenominazione(cs.getCompDenominazione());
		this.getComponente().setCompTelefono(cs.getCompTel());
		if(cs.getCompCitta()!=null){
			int index = cs.getCompCitta().lastIndexOf('-');
			String citta = cs.getCompCitta().substring(0,index);
			String prov = cs.getCompCitta().substring(index+1);
			this.getComponente().setComuneResidenzaMan(citta, prov);
		}
		this.getComponente().setIdComponente(cs.getCsAComponente()!=null ? cs.getCsAComponente().getId() : null);
		
		//valorizzare relazione_diario
		List<CsDDiario> lstDiario = cs.getCsDDiario().getCsDDiariFiglio();
		if(lstDiario!=null && lstDiario.size()>0)
			for(CsDDiario d : lstDiario)
				if(d.getCsDRelazione()!=null)
					this.setIdRelazione(d.getId());
		
	}
	
	public void valorizzaJpa(CsFlgIntervento cs) throws NamingException{
		
		BaseDTO b = new BaseDTO();
		fillEnte(b);
		
		cs.setDataDomanda(this.getDataDomanda());
		
		//Recupero i dati dalla scelta componente
		ComponenteAltroMan comp = this.getComponente();
		cs.setCompDenominazione(comp.getCompDenominazione());
		cs.setCompCitta(comp.getCompCitta());
		cs.setCompTel(comp.getCompTelefono());	
		cs.setCompIndirizzo(comp.getCompIndirizzo());
		cs.setCsAComponente(comp.getComponenteSel());
		
		cs.setDescrSospensione(this.getDescrSospensione());
		cs.setAttSospCDal(this.getDtTipoAttDa());
		cs.setAttSospCAl(this.getDtTipoAttA());
	
		cs.setFlagAttSospC(this.getFlagAttivazione());
			
		cs.setFlagRespinto(this.getFlagRespinto() ? new BigDecimal(1) : new BigDecimal(0)); 
		
		//Recupero motivo chiusura
		AccessTableConfigurazioneSessionBeanRemote confService = (AccessTableConfigurazioneSessionBeanRemote) 
				ClientUtility.getEjbInterface("CarSocialeA", "CarSocialeA_EJB", "AccessTableConfigurazioneSessionBean");
		
		b.setObj(this.getMotivoChiusura());
		CsTbMotivoChiusuraInt tb = confService.getMotivoChiusuraIntervento(b);
		cs.setCsTbMotivoChiusuraInt(tb);
		
		cs.setMotivoRespinto(this.getMotivoRespinto());
		cs.setTipoAttivazione(this.getTipoAttivazione());
		
	}

	public Long getIdRelazione() {
		return idRelazione;
	}

	public void setIdRelazione(Long idRelazione) {
		this.idRelazione = idRelazione;
	}

}
