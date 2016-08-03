package it.webred.ct.service.spprof.web.user.bean.interventi;

import java.io.IOException;
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
import it.webred.ct.service.spprof.data.model.SSpEdificio;
import it.webred.ct.service.spprof.data.model.SSpEdificioMinore;
import it.webred.ct.service.spprof.data.model.SSpIntervento;
import it.webred.ct.service.spprof.web.user.SpProfBaseBean;
import it.webred.ct.service.spprof.web.user.bean.util.MapBean;
import it.webred.ct.service.spprof.web.user.bean.util.PageBean;

import org.apache.log4j.Logger;

public class DatiBean extends SpProfBaseBean {

	public SpProfInterventoService spProfInterventoService = (SpProfInterventoService) getEjb("Servizio_SpProf", "Servizio_SpProf_EJB", "SpProfInterventoServiceBean");
	
	public SpProfAreaService spProfAreaService = (SpProfAreaService) getEjb("Servizio_SpProf", "Servizio_SpProf_EJB", "SpProfAreaServiceBean");

	public ConcessioniEdilizieService concessioniEdilizieService = (ConcessioniEdilizieService) getEjb("CT_Service", "CT_Service_Data_Access", "ConcessioniEdilizieServiceBean");
	
	public SpProfEdificiService spProfEdificiService = (SpProfEdificiService) getEjb("Servizio_SpProf", "Servizio_SpProf_EJB", "SpProfEdificiServiceBean");
	
	private String idIntervento;
	private boolean mostraDownload;

	private String nPostiAuto;
	private String nBoxAuto;
	private String nPassiCarrai;
	private String nAccessiCarrai;
	private String concessioneNum;
	private String progressivoNum;
	private String progressivoAnno;
	private Date protocolloData;
	private String protocolloNumero;
	private String note;
	private String stato;
	
	private List<EdificioDTO> listaEdifici;
	private List<EdificioDTO> listaEdificiMinori;
	
	private List<SitCConcessioni> listaConcessioni;
	private boolean concessioniExists;
	private String concessioneIdScelta;
	
	private boolean caricaSolo;

	public void doDettaglio() {

		listaConcessioni = new ArrayList<SitCConcessioni>();
		concessioniExists = false;
		
		listaEdifici = new ArrayList<EdificioDTO>();
		listaEdificiMinori = new ArrayList<EdificioDTO>();
		
		try {

			if(!caricaSolo){
				PageBean pBean = (PageBean) getBeanReference("pageBean");
				pBean.goDati();
				
				//carico visualizzazione in mappa
				MapBean mBean = (MapBean) getBeanReference("mapBean");
				mBean.setIdIntervento(idIntervento);
				mBean.caricaMapUrlIntervento();
			}
			caricaSolo = false;
			
			SpProfDTO dto = new SpProfDTO();
			dto.setEnteId(getEnte());
			dto.setObj(new Long(idIntervento));
			SSpIntervento intervento = spProfInterventoService.getInterventoById(dto);
			jpaToBean(intervento);
			
			//caricamento selezioni da concessioni
			RicercaOggettoCatDTO catDto = new RicercaOggettoCatDTO();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			
			List<SSpAreaPart> listaPart = spProfAreaService.getListaAreaPart(dto);
			for(SSpAreaPart part: listaPart){
				
				//cerco concessioni per foglio/particella
				catDto.setEnteId(getEnte());
				catDto.setFoglio(part.getFoglio().toString());
				catDto.setParticella(part.getParticella());
				listaConcessioni.addAll(concessioniEdilizieService.getConcessioniByFabbricato(catDto));

				
				//cerco concessioni per indirizzi collegati a foglio/particella
				RicercaOggettoCatDTO rocDto = new RicercaOggettoCatDTO();
				rocDto.setEnteId(getEnte());
				rocDto.setFoglio(part.getFoglio().toString());
				rocDto.setParticella(part.getParticella());
				listaConcessioni.addAll(concessioniEdilizieService.getConcessioniByIndirizziByFoglioParticella(rocDto));	
			}
			if(listaConcessioni.size() > 0)
				concessioniExists = true;
			
			//carico edifici
			dto.setObj(new Long(idIntervento));
			List<SSpEdificio> lista = spProfEdificiService.getEdificiByintervento(dto);
			for(SSpEdificio ed: lista){
				EdificioDTO edDto = new EdificioDTO();
				edDto.setEdificio(ed);
				dto.setObj(ed.getFkSpCedificato());
				edDto.setEdificato(spProfEdificiService.getEdificatoById(dto));
				dto.setObj(edDto.getEdificato().getFkSpAreaPart());
				edDto.setParticella(spProfAreaService.getAreaPartById(dto));
				dto.setObj(ed.getFkSpTipologiaEdi());
				edDto.setTipologia(spProfEdificiService.getTipologiaEdificiById(dto));
				if(ed.getFkSpDestUrb() != null){
					dto.setObj(ed.getFkSpDestUrb());
					edDto.setDestUrb(spProfEdificiService.getDestinazioneUsoById(dto).getDescr());
				}
				if(ed.getFkSpDestUrbPreval() != null){
					dto.setObj(ed.getFkSpDestUrbPreval());
					edDto.setDestUrbPrev(spProfEdificiService.getDestinazioneUsoById(dto).getDescr());
				}
				listaEdifici.add(edDto);
			}
			dto.setObj(new Long(idIntervento));
			List<SSpEdificioMinore> listaM = spProfEdificiService.getEdificiMinoriByintervento(dto);
			for(SSpEdificioMinore ed: listaM){
				EdificioDTO edDto = new EdificioDTO();
				edDto.setEdificioMinore(ed);
				dto.setObj(ed.getFkSpCedificato());
				edDto.setEdificato(spProfEdificiService.getEdificatoById(dto));
				dto.setObj(edDto.getEdificato().getFkSpAreaPart());
				edDto.setParticella(spProfAreaService.getAreaPartById(dto));
				dto.setObj(ed.getFkSpTipologiaEdiMin());
				edDto.setTipologiaMin(spProfEdificiService.getTipologiaEdificiMinoriById(dto));
				listaEdificiMinori.add(edDto);
			}

		} catch (Throwable t) {
			super.addErrorMessage("dettaglio.error", t.getMessage());
			logger.error(t);
		}
	}
	
	public void doAggiornaConcessioneScelta(){
		for(SitCConcessioni c: listaConcessioni){
			if(c.getIdOrig().equals(concessioneIdScelta)){
				concessioneNum = c.getConcessioneNumero();
				progressivoAnno = c.getProgressivoAnno();
				progressivoNum = c.getProgressivoNumero();
				protocolloData = c.getProtocolloData();
				protocolloNumero = c.getProtocolloNumero();
			}
		}
	}

	public void doSalva() {

		String message = "save";
		
		try {

			SpProfDTO dto = new SpProfDTO();
			dto.setEnteId(getEnte());
			dto.setObj(new Long(idIntervento));
			SSpIntervento intervento = spProfInterventoService.getInterventoById(dto);
			intervento = beanToJpa(intervento);
			dto.setObj(intervento);
			spProfInterventoService.save(dto);

			super.addInfoMessage(message);
			
		} catch (Throwable t) {
			super.addErrorMessage(message+".error", t.getMessage());
			logger.error(t);
		}

	}

	private void jpaToBean(SSpIntervento i) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		if (i.getnPostiAuto() != null)
			nPostiAuto = i.getnPostiAuto().toString();
		else nPostiAuto = "";
		if (i.getnBoxAuto() != null)
			nBoxAuto = i.getnBoxAuto().toString();
		else nBoxAuto = "";
		if (i.getnPassiCarraiPrev() != null)
			nPassiCarrai = i.getnPassiCarraiPrev().toString();
		else nPassiCarrai = "";
		if (i.getnAccessiCarraiPrev() != null)
			nAccessiCarrai = i.getnAccessiCarraiPrev().toString();
		else nAccessiCarrai = "";
		concessioneNum = i.getcConcessioneNumero();
		note = i.getNote();
		stato = i.getStato();

	}

	private SSpIntervento beanToJpa(SSpIntervento i) {

		if (nPostiAuto != null && !nPostiAuto.equals(""))
			i.setnPostiAuto(new Long(nPostiAuto));
		if (nBoxAuto != null && !nBoxAuto.equals(""))
			i.setnBoxAuto(new Long(nBoxAuto));
		if (nPassiCarrai != null && !nPassiCarrai.equals(""))
			i.setnPassiCarraiPrev(new Long(nPassiCarrai));
		if (nAccessiCarrai != null && !nAccessiCarrai.equals(""))
			i.setnAccessiCarraiPrev(new Long(nAccessiCarrai));
		i.setcConcessioneNumero(concessioneNum);
		i.setcProgressivoAnno(progressivoAnno);
		i.setcProgressivoNumero(progressivoNum);
		i.setcProtocolloData(protocolloData);
		i.setcProtocolloNumero(protocolloNumero);
		i.setNote(note);

		return i;
	}

	public String getIdIntervento() {
		return idIntervento;
	}

	public void setIdIntervento(String idIntervento) {
		this.idIntervento = idIntervento;
	}

	public String getnPostiAuto() {
		return nPostiAuto;
	}

	public void setnPostiAuto(String nPostiAuto) {
		this.nPostiAuto = nPostiAuto;
	}

	public String getnBoxAuto() {
		return nBoxAuto;
	}

	public void setnBoxAuto(String nBoxAuto) {
		this.nBoxAuto = nBoxAuto;
	}

	public String getnPassiCarrai() {
		return nPassiCarrai;
	}

	public void setnPassiCarrai(String nPassiCarrai) {
		this.nPassiCarrai = nPassiCarrai;
	}

	public String getnAccessiCarrai() {
		return nAccessiCarrai;
	}

	public void setnAccessiCarrai(String nAccessiCarrai) {
		this.nAccessiCarrai = nAccessiCarrai;
	}

	public String getConcessioneNum() {
		return concessioneNum;
	}

	public void setConcessioneNum(String concessioneNum) {
		this.concessioneNum = concessioneNum;
	}

	public String getProgressivoNum() {
		return progressivoNum;
	}

	public void setProgressivoNum(String progressivoNum) {
		this.progressivoNum = progressivoNum;
	}

	public String getProgressivoAnno() {
		return progressivoAnno;
	}

	public void setProgressivoAnno(String progressivoAnno) {
		this.progressivoAnno = progressivoAnno;
	}

	public Date getProtocolloData() {
		return protocolloData;
	}

	public void setProtocolloData(Date protocolloData) {
		this.protocolloData = protocolloData;
	}

	public String getProtocolloNumero() {
		return protocolloNumero;
	}

	public void setProtocolloNumero(String protocolloNumero) {
		this.protocolloNumero = protocolloNumero;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public List<SitCConcessioni> getListaConcessioni() {
		return listaConcessioni;
	}

	public void setListaConcessioni(List<SitCConcessioni> listaConcessioni) {
		this.listaConcessioni = listaConcessioni;
	}

	public boolean isMostraDownload() {
		return mostraDownload;
	}

	public void setMostraDownload(boolean mostraDownload) {
		this.mostraDownload = mostraDownload;
	}

	public List<EdificioDTO> getListaEdifici() {
		return listaEdifici;
	}

	public void setListaEdifici(List<EdificioDTO> listaEdifici) {
		this.listaEdifici = listaEdifici;
	}

	public List<EdificioDTO> getListaEdificiMinori() {
		return listaEdificiMinori;
	}

	public void setListaEdificiMinori(List<EdificioDTO> listaEdificiMinori) {
		this.listaEdificiMinori = listaEdificiMinori;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public boolean isCaricaSolo() {
		return caricaSolo;
	}

	public void setCaricaSolo(boolean caricaSolo) {
		this.caricaSolo = caricaSolo;
	}

	public boolean isConcessioniExists() {
		return concessioniExists;
	}

	public void setConcessioniExists(boolean concessioniExists) {
		this.concessioniExists = concessioniExists;
	}

	public String getConcessioneIdScelta() {
		return concessioneIdScelta;
	}

	public void setConcessioneIdScelta(String concessioneIdScelta) {
		this.concessioneIdScelta = concessioneIdScelta;
	}

}
