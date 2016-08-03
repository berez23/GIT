package it.webred.ct.service.carContrib.web.pages;

import it.webred.ct.service.carContrib.data.access.cc.dto.RichiesteDTO;
import it.webred.ct.service.carContrib.data.access.cc.dto.SoggettiCarContribDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.ParamAccessoDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.SoggettoDTO;
import it.webred.ct.service.carContrib.data.model.CodiciTipoMezzoRisposta;
import it.webred.ct.service.carContrib.data.model.DecodTipDoc;
import it.webred.ct.service.carContrib.data.model.Richieste;
import it.webred.ct.service.carContrib.data.model.SoggettiCarContrib;
import it.webred.ct.service.carContrib.web.CarContribBaseBean;
import it.webred.ct.service.carContrib.web.Utility;
import it.webred.ct.support.datarouter.CeTBaseObject;
import it.webred.rich.common.CalendarBoxRch;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.model.SelectItem;

public class RichiestaBean extends CarContribBaseBean{

	public RichiestaBean ()
	{
	}
	
	private SoggettiCarContribDTO soggettoRichiedente;
	
	private String msgErrore;
	private String codFiscale;
	private String cognome;
	private String nome;
	private String docRiconos;
	private String mezzoRisposta;
	private String numDocRic;
	private String codFiscaleSogg;
	private String cognomeSogg;
	private String nomeSogg;
	private String partIVA;
	private String denominazione;
	private String email;
	private String numTelefono;
	private boolean visualizzaCartella;
	private boolean includiStorico = false;

	private CalendarBoxRch dataNascita = new CalendarBoxRch();
	private CalendarBoxRch dataEmiss = new CalendarBoxRch();
	private CalendarBoxRch dataNascitaSogg = new CalendarBoxRch();
	
	private String cbxTipoSoggetto;
	
	private List<SoggettoDTO> listaContribPersFis = new ArrayList<SoggettoDTO>();
	private List<SoggettoDTO> listaContribPersGiur = new ArrayList<SoggettoDTO>();
	
	private SoggettoDTO soggettoContrib;
	
	public SoggettiCarContribDTO getSoggettoRichiedente() {
		return soggettoRichiedente;
	}
	public void setSoggettoRichiedente(SoggettiCarContribDTO soggettoRichiedente) {
		this.soggettoRichiedente = soggettoRichiedente;
	}
	public String getCodFiscale() {
		return codFiscale;
	}

	public void setCodFiscale(String codFiscale) {
		this.codFiscale = codFiscale;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public CalendarBoxRch getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(CalendarBoxRch dataNascita) {
		this.dataNascita = dataNascita;
	}

	public String getDocRiconos() {
		return docRiconos;
	}

	public void setDocRiconos(String docRiconos) {
		this.docRiconos = docRiconos;
	}

	public String getMezzoRisposta() {
		return mezzoRisposta;
	}
	public void setMezzoRisposta(String mezzoRisposta) {
		this.mezzoRisposta = mezzoRisposta;
	}
	public String getNumDocRic() {
		return numDocRic;
	}

	public void setNumDocRic(String numDocRic) {
		this.numDocRic = numDocRic;
	}

	public CalendarBoxRch getDataEmiss() {
		return dataEmiss;
	}

	public void setDataEmiss(CalendarBoxRch dataEmiss) {
		this.dataEmiss = dataEmiss;
	}

	public String getCodFiscaleSogg() {
		return codFiscaleSogg;
	}

	public void setCodFiscaleSogg(String codFiscaleSogg) {
		this.codFiscaleSogg = codFiscaleSogg;
	}

	public String getCognomeSogg() {
		return cognomeSogg;
	}

	public void setCognomeSogg(String cognomeSogg) {
		this.cognomeSogg = cognomeSogg;
	}

	public String getNomeSogg() {
		return nomeSogg;
	}

	public void setNomeSogg(String nomeSogg) {
		this.nomeSogg = nomeSogg;
	}

	public CalendarBoxRch getDataNascitaSogg() {
		return dataNascitaSogg;
	}

	public void setDataNascitaSogg(CalendarBoxRch dataNascitaSogg) {
		this.dataNascitaSogg = dataNascitaSogg;
	}

	public String getPartIVA() {
		return partIVA;
	}

	public void setPartIVA(String partIVA) {
		this.partIVA = partIVA;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmail() {
		return email;
	}
	public void setNumTelefono(String numTelefono) {
		this.numTelefono = numTelefono;
	}
	public String getNumTelefono() {
		return numTelefono;
	}
	
	public boolean isVisualizzaCartella() {
		return visualizzaCartella;
	}
	public void setVisualizzaCartella(boolean visualizzaCartella) {
		this.visualizzaCartella = visualizzaCartella;
	}
	public void setCbxTipoSoggetto(String cbxTipoSoggetto) {
		this.cbxTipoSoggetto = cbxTipoSoggetto;
	}

	public String getCbxTipoSoggetto() {
		return cbxTipoSoggetto;
	}

	public void setListaContribPersFis(List<SoggettoDTO> listaContribPersFis) {
		this.listaContribPersFis = listaContribPersFis;
	}

	public List<SoggettoDTO> getListaContribPersFis() {
		return listaContribPersFis;
	}

	public void setListaContribPersGiur(List<SoggettoDTO> listaContribPersGiur) {
		this.listaContribPersGiur = listaContribPersGiur;
	}

	public List<SoggettoDTO> getListaContribPersGiur() {
		return listaContribPersGiur;
	}
	
	public void setSoggettoContrib(SoggettoDTO soggettoContrib) {
		this.soggettoContrib = soggettoContrib;
	}

	public SoggettoDTO getSoggettoContrib() {
		return soggettoContrib;
	}

	public void setMsgErrore(String msgErrore) {
		this.msgErrore = msgErrore;
	}

	public String getMsgErrore() {
		return msgErrore;
	}
	public boolean isIncludiStorico() {
		return includiStorico;
	}
	public void setIncludiStorico(boolean includiStorico) {
		this.includiStorico = includiStorico;
	}

	
	// *****************  SEZIONE ACTION  *****************
	

	public List<SelectItem> getDocTypes() {
		
		List<SelectItem> docTypes =  new ArrayList<SelectItem>();
		
		CeTBaseObject cetObj = new CeTBaseObject();
		cetObj.setEnteId(super.getUserBean().getEnteID());
		cetObj.setUserId(super.getUserBean().getUsername());
		
		List<DecodTipDoc> listaDoc  = super.getCarContribService().getListaTipDoc(cetObj);

		for(DecodTipDoc d:listaDoc)
		{
			SelectItem doc = new SelectItem(d.getCodTipDoc(), d.getDesTipDoc());
			docTypes.add(doc);
		}
		
		return docTypes;
	}
	
	public List<SelectItem> getCodiciRisposta() {
		
		List<SelectItem> codiciRisposta =  new ArrayList<SelectItem>();
		
		CeTBaseObject cetObj = new CeTBaseObject();
		cetObj.setEnteId(super.getUserBean().getEnteID());
		cetObj.setUserId(super.getUserBean().getUsername());
		
		List<CodiciTipoMezzoRisposta> lista  =  super.getCarContribService().getListaCodiciRisp(cetObj);
		
		for(CodiciTipoMezzoRisposta c:lista)
		{
			SelectItem s = new SelectItem(c.getCodice(), c.getDescr());
			codiciRisposta.add(s);
		}
		
		return codiciRisposta;
	}
	
	public String Ricerca()
	{
		msgErrore="";

		Date dataNascitaRichiedente = this.dataNascita.getSelectedDate();
		Date dataEmissRichiedente = this.dataEmiss.getSelectedDate();
		Date dataNascitaSoggCartella = dataNascitaSogg.getSelectedDate();
		
		// CASI x RICERCA:
		// - Sogg Richiedente presente , Sogg Cartella non presente --> sogg Richiedente diventa ANCHE soggetto Cartella
		// - Sogg Richiedente non presente , Sogg Cartella presente --> sogg Richiedente diventa l'operatore
		// - Sogg Richiedente presente , Sogg Cartella presente --> si passano alla funzione di inserimento richieste
		// - Sogg Richiedente non presente , Sogg Cartella non presente --> non si fa la ricerca
		
		// SOGGETTO RICHIEDENTE
		soggettoRichiedente = null;
		
		// CONTROLLO SELEZIONE mezzo risposta non selezionato
		if(mezzoRisposta.equals("0"))
		{// mezzo risposta non selezionato
			logger.info("Mezzo risposta non selezionato.");
			msgErrore = "Mezzo risposta non selezionato.";
			return "goRicerca";
		}
		
		// CONTROLLO FORMALE DELL'EMAIL
		if(email!="" && !Utility.isValidEmailAddress(email))
		{// email inserita errata
			logger.info("Email inserita errata.");
			msgErrore = "Email inserita errata.";
			return "goRicerca";
		}
		
		//se tutti i campi sono valorizzati allora il richiedente è inserito
		if (codFiscale.trim()!=""
			&& cognome.trim()!=""
			&& nome.trim()!=""
			&& dataNascitaRichiedente!=null
			&& docRiconos!="0"
			&& numDocRic.trim()!=""
			&& dataEmissRichiedente!=null)
		{
			soggettoRichiedente = new SoggettiCarContribDTO();
			
			SoggettiCarContrib sRich = new SoggettiCarContrib();

			sRich.setCodTipSogg("F");
			sRich.setCodFis(codFiscale.trim().toUpperCase());
			sRich.setCognome(cognome.trim().toUpperCase());
			sRich.setNome(nome.trim().toUpperCase());
			sRich.setDtNas(dataNascitaRichiedente);
			
			soggettoRichiedente.setSogg(sRich);
			soggettoRichiedente.setEnteId(super.getUserBean().getEnteID());
			soggettoRichiedente.setUserId(super.getUserBean().getUsername());
		}
		else
		{
			if(! (codFiscale.trim().equals("")
				&& cognome.trim().equals("")
				&& nome.trim().equals("")
				&& dataNascitaRichiedente==null
				&& numDocRic.trim().equals("")
				&& dataEmissRichiedente==null))
				{// SE C'E' SOLO QUALCHE CAMPO INSERITO SI MANDA L'ERRORE
				logger.info("DATI SOGGETTO RICHIEDENTE INSERITI ERRATAMENTE");
					msgErrore = "DATI SOGGETTO RICHIEDENTE INSERITI ERRATAMENTE";
					return null;
				}
		}
		
		ParamAccessoDTO parms =  new ParamAccessoDTO();
		
		// SOGGETTO CARTELLA
		boolean insertSoggettoCartella = false;
		if (cbxTipoSoggetto.equals("PF"))
		{// PERSONA FISICA
			if ((codFiscaleSogg.trim()!="") || (nomeSogg.trim()!="" && cognomeSogg.trim()!="" && dataNascitaSoggCartella!=null))
			{
				parms.setTipoSogg("F");
				parms.setCognome(this.getCognomeSogg().trim());				
				parms.setNome(this.getNomeSogg().trim());
				parms.setCodFis(this.getCodFiscaleSogg().trim());
				parms.setDtNas(dataNascitaSoggCartella);
				parms.setParIva("");
				parms.setDenom("");
				
				insertSoggettoCartella = true;
			}
		}
		else
		{// PERSONA GIURIDICA
			if(partIVA.trim()!="" || denominazione.trim()!="")
			{
				parms.setTipoSogg("G");
				parms.setCodFis("");
				parms.setCognome("");			
				parms.setCodFis("");
				parms.setParIva(this.getPartIVA().trim());
				parms.setDenom(this.getDenominazione().trim());
				
				insertSoggettoCartella = true;
			}
		}
		
		if (!insertSoggettoCartella)
		{// NON E' STATO SPECIFICATO IL SOGGETTO x CARTELLA 
		// SE E' STATO INSERITO IL SOGGETTO RICHIEDENTE QUESTI DIVENTA ANCHE SOGGETTO CARTELLA
		// ALTRIMENTI SI TORNA ERRORE
			if (soggettoRichiedente!=null)
			{
				parms.setTipoSogg(soggettoRichiedente.getSogg().getCodTipSogg());
				parms.setNome(soggettoRichiedente.getSogg().getNome());
				parms.setCognome(soggettoRichiedente.getSogg().getCognome());			
				parms.setCodFis(soggettoRichiedente.getSogg().getCodFis());
				parms.setDtNas(soggettoRichiedente.getSogg().getDtNas());
				parms.setParIva("");
				parms.setDenom("");
				
				logger.info("NON E' STATO SPECIFICATO IL SOGGETTO x CARTELLA ");
			}
			else
			{
				logger.info("DATI x RICERCA CARTELLA INSUFFICIENTI");
				msgErrore = "Dati per ricerca cartella contribuente insufficienti";
				return "goRicerca";
			}
		}
		
		String res="goRicerca";
		
		parms.setEnteId(super.getUserBean().getEnteID());
		parms.setUserId(super.getUserBean().getUsername());
		
		Set <SoggettoDTO> listaSoggetti = super.getGeneralService().searchSoggetto(parms);
		
		if (listaSoggetti==null || listaSoggetti.size()==0)
		{
			logger.info("NESSUN SOGGETTO TROVATO");
			msgErrore = "NESSUN SOGGETTO TROVATO";
			res="goRicerca";
		}
		else
		{
			if (listaSoggetti.size()==1)
			{
				SoggettoDTO sogg=null;	Long id =null;			
				for (Iterator<SoggettoDTO> s = listaSoggetti.iterator();s.hasNext();)
				{
					sogg = s.next();
					
					id = this.inserimentoRichiesta(sogg);
				}
				if (visualizzaCartella) {
					super.LoadSoggettoInContribuenteBean(sogg,id,mezzoRisposta,false,Utility.TipoBeanPadre.CHIUDIeAGGIORNA_PADRE, true, this.includiStorico);
					res="goCartellaContribuente";	
				}
				else {
					msgErrore = "RICHIESTA INSERITA";
					res="goRicerca";
				}
			}
			else
			{
				listaContribPersFis.clear();
				listaContribPersGiur.clear();
				
				for (Iterator<SoggettoDTO> s = listaSoggetti.iterator();s.hasNext();)
				{
					SoggettoDTO sogg = s.next();
					
					if (cbxTipoSoggetto.equals("PF"))
						listaContribPersFis.add(sogg);
					else
						listaContribPersGiur.add(sogg);
				}
				res="goSelectContribuente";
			}
		}
		return res ;
	}
	
	private Long inserimentoRichiesta(SoggettoDTO sogg)
	{
		Long idRich=new Long(0);	
		try
		{
			msgErrore ="";		
			
			// RICHIESTA
			RichiesteDTO richDTO= new RichiesteDTO();
			
			Richieste rich = new Richieste();
			rich.setCodTipRic(Utility.TipoRichiesta.CARTELLA.getTipoRichiesta());
			rich.setCodTipProven(Utility.TipoProvenienza.INTERNA.getTipoProvenienza());
			rich.setDtRic(new Date());
			rich.setNumProt(null);
			rich.setDtProt(null);
			rich.setIdSoggRic(null);
			
			if (soggettoRichiedente!=null)
			{// E' STATO SPECIFICATO IL RICHIEDENTE
				rich.setUserNameRic(null);
				rich.setCodTipDocRicon(docRiconos);
				rich.setNumDocRicon(numDocRic.trim().toUpperCase());
				rich.setDtEmiDocRicon(dataEmiss.getSelectedDate());
				rich.setDesNotRic("");
			}
			else
			{
			// NON E' STATO SPECIFICATO IL RICHIEDENTE ALLORA IL RICHIEDENTE SARA' L'OPERATORE
				rich.setUserNameRic(super.getUserBean().getUsername());
				rich.setCodTipDocRicon("");
				rich.setNumDocRicon("");
				rich.setDtEmiDocRicon(null);
				rich.setDesNotRic("");
			}
			rich.setEMail(email.trim());
			rich.setNumTel(numTelefono.trim());
			rich.setCodTipMezRis(mezzoRisposta);
			Integer flgStorico = includiStorico ? new Integer(1) : new Integer(0);
			rich.setFlgStorico(flgStorico);
			
			richDTO.setRich(rich);
			richDTO.setEnteId(super.getUserBean().getEnteID());
			richDTO.setUserId(super.getUserBean().getUsername());
			
			SoggettiCarContribDTO soggettoCartella = new SoggettiCarContribDTO();
			SoggettiCarContrib soggetto = new SoggettiCarContrib();
			
			soggetto.setCodTipSogg(sogg.getTipoSogg());
			soggetto.setCodFis(sogg.getCodFis());
			soggetto.setCognome(sogg.getCognome());
			soggetto.setNome(sogg.getNome());
			soggetto.setDtNas(sogg.getDtNas());
			soggetto.setCodComNas(sogg.getCodComNas());
			soggetto.setParIva(sogg.getParIva());
			soggetto.setDenomSoc(sogg.getDenom());
			
			soggettoCartella.setSogg(soggetto);
			soggettoCartella.setEnteId(super.getUserBean().getEnteID());
			soggettoCartella.setUserId(super.getUserBean().getUsername());
					
			idRich = super.getCarContribService().insertRichiesta(soggettoRichiedente, soggettoCartella, richDTO);
			
			logger.info("Richiesta Inserita.Identificativo: = " + idRich.toString());

		}
		catch(Exception ex)
		{
			logger.info("ERRORE in inserimentoRichiesta = " + ex.getMessage());
		}
		return idRich; 
	}
	
	public String selectSoggContribPF() {
		msgErrore="";
		
		if (soggettoContrib!=null)
			{
			Long id = this.inserimentoRichiesta(soggettoContrib);
			
			if (visualizzaCartella) {
				super.LoadSoggettoInContribuenteBean(soggettoContrib,id, mezzoRisposta,true,Utility.TipoBeanPadre.CHIUDIeAGGIORNA_PADRE, true, this.includiStorico);
				return "goCartellaContribuente";
			}
			else {
				msgErrore = "RICHIESTA INSERITA";
				return "goRicerca";
			}
		}
		else
			return null;
	}

	public String selectSoggContribPG() {
		msgErrore="";
		
		if (soggettoContrib!=null)
		{
			Long id = this.inserimentoRichiesta(soggettoContrib);
			if (visualizzaCartella) {
				super.LoadSoggettoInContribuenteBean(soggettoContrib,id, mezzoRisposta,true,Utility.TipoBeanPadre.CHIUDIeAGGIORNA_PADRE, true, this.includiStorico);
				return "goCartellaContribuente";
			}
			else {
				msgErrore = "RICHIESTA INSERITA";
				return "goRicerca";
			}
		}
		else
			return null;
	}
	
	public String goCartellaRicerca()
	{
		codFiscaleSogg="";
		cognomeSogg="";
		nomeSogg="";
		dataNascitaSogg.setSelectedDate(null);
		partIVA="";
		denominazione="";
		email = "";
		numTelefono = "";
		mezzoRisposta = "";
		codFiscale="";
		cognome="";
		nome="";
		dataNascita.setSelectedDate(null);
		numDocRic="";
		dataEmiss.setSelectedDate(null);
		docRiconos="";
		includiStorico=false;
		
		msgErrore="";

		if (visualizzaCartella)
			this.setTitolo("PREPARAZIONE CARTELLA");
		else
			this.setTitolo("IMMISSIONE DATI RICHIESTA");

		return "goRicerca";
	}
	
	public String goSelectContribuente()
	{
		return "goSelectContribuente";
	}
	
}

